package com.zip.zipdome.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author: huangsk20406
 * @date: 2020/10/9
 * @description: zip解压、压缩工具类
 **/
public class ZipUtils {

    private static final int BUFFER_SIZE = 2048;

    /**
     * 压缩成ZIP 方法
     *
     * @param fileList 需要压缩的文件列表
     * @param bos 需要做为入参传入（Cannot call sendError() after the response has been committed）
     * @throws Exception 压缩失败会抛出运行时异常
     */
    public static void toZip(List<File> fileList, ByteArrayOutputStream bos) throws Exception {
        try (ZipOutputStream zos = new ZipOutputStream(bos)) {
            for (File file : fileList) {
                byte[] buf = new byte[BUFFER_SIZE];
                zos.putNextEntry(new ZipEntry(file.getName()));
                int len;
                FileInputStream fis = new FileInputStream(file);
                while ((len = fis.read(buf)) != -1) {
                    zos.write(buf, 0, len);
                    System.out.println(fis.read(buf));
                }
                zos.closeEntry();
                fis.close();
            }
        }
    }

    /**
     * 设置response头 为下载zip, 设置文件名. 应该在response写出之前被调用
     */
    public static void configDownloadZip(HttpServletResponse response, String fileName) {
        response.setHeader("Content-Type", "application/zip");
        try {
            fileName = URLEncoder.encode(fileName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
    }

    /**
     * 生成压缩包
     * @param request
     * @return
     */
    public static void generateZip(HttpServletRequest request, HttpServletResponse response,List<String> filePathList, String fileName){
        response.setHeader("Content-Type", "application/zip");
        try {
            fileName = URLEncoder.encode(fileName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);


//        //获取excel存储路径
//        String excelDir = "G:\\zip";
        //遍历excel存储路径
        //获取pathName的File对象
//        File dirFile = new File(excelDir);
        //获取此目录下的所有文件名与目录名
//        String[] fileList = dirFile.list();

        ZipOutputStream zos = null;
        try {

            //关联response输出流，直接将zip包文件内容写入到response输出流并下载
            zos = new ZipOutputStream(response.getOutputStream());

            //循环读取文件路径集合，获取每一个文件的路径
            for(String fp : filePathList){
                File f = new File(fp);  //根据文件路径创建文件
                zipFile(f, zos);  //将每一个文件写入zip文件包内，即进行打包
                //刷新缓冲区
                response.flushBuffer();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try{
                if(zos != null){
                    zos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 封装压缩文件的方法
     * @param inputFile
     * @param zipoutputStream
     */
    public static void zipFile(File inputFile,ZipOutputStream zipoutputStream) {
        try {
            if(inputFile.exists()) { //判断文件是否存在
                if (inputFile.isFile()) {  //判断是否属于文件，还是文件夹

                    //创建输入流读取文件
                    FileInputStream fis = new FileInputStream(inputFile);
                    BufferedInputStream bis = new BufferedInputStream(fis);

                    //将文件写入zip内，即将文件进行打包
                    ZipEntry ze = new ZipEntry(inputFile.getName()); //获取文件名
                    zipoutputStream.putNextEntry(ze);

                    //写入文件的方法，同上
                    byte [] b=new byte[1024];
                    long l=0;
                    while(l<inputFile.length()){
                        int j=bis.read(b,0,1024);
                        l+=j;
                        zipoutputStream.write(b,0,j);
                    }
                    //关闭输入输出流
                    fis.close();
                    bis.close();
                } else {  //如果是文件夹，则使用穷举的方法获取文件，写入zip
                    try {
                        File[] files = inputFile.listFiles();
                        for (int i = 0; i < files.length; i++) {
                            zipFile(files[i], zipoutputStream);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(12);
        try {
            File file2 = new File("G:\\a.txt");
            File file3 = new File("G:\\b.txt");
            List<File> list = new ArrayList<>();
            list.add(file2);
            list.add(file3);
            toZip(list,outputStream);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }
}


