package com.zip.zipdome.controller;

import com.zip.zipdome.utils.ZipUtils;
import org.apache.tomcat.util.http.ResponseUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件描述
 *
 * @ProductName: Hundsun HEP
 * @ProjectName: mybatis
 * @Package: com.zip.zipdome.controller
 * @Description: note
 * @Author: changfx43807
 * @CreateDate: 2022/7/13 10:10
 * @UpdateRemark: The modified content
 * @Version: 1.0
 * Copyright © 2022 Hundsun Technologies Inc. All Rights Reserved
 **/
@RestController
@RequestMapping("zip")
public class ZipController {


    @RequestMapping("zip")
    public String zip() {
        return "aaa";
    }


    @RequestMapping("/downFileZip")
    public void downExcel(HttpServletRequest request, HttpServletResponse response, String fileName, String campId) {
        //下载文件配置
//        ResponseUtil.configDownloadExcel(response, outputFileName);
        List<String> list = new ArrayList<>();
        list.add("G:\\zip\\a.txt");
        list.add("G:\\zip\\b.txt");
        list.add("G:\\zip\\test.png");
        ZipUtils.generateZip(request, response,list,"iamac.zip");

    }
}
