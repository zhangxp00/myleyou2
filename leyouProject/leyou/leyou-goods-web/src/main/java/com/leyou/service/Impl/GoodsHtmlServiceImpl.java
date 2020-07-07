package com.leyou.service.Impl;

import com.leyou.service.GoodsHtmlService;
import com.leyou.service.GoodsWebService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Map;

@Service
public class GoodsHtmlServiceImpl implements GoodsHtmlService {

    //模板引擎
    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private GoodsWebService goodsWebServiceImpl;

    @Override
    public void createGoodsHtml(Long id) {
        //初始化上下文
        Context context = new Context();
        //准备页面需要的所有数据
        Map<String, Object> stringObjectMap = goodsWebServiceImpl.goodWebParam(id);
        context.setVariables(stringObjectMap);
        //准备输出流，写入本地硬盘

        PrintWriter printWriter = null;
        try {
            File file = new File("D:\\MyGit\\myleyou\\leyouProject\\nginx-1.14.0\\html\\item\\"+id+".html");
            printWriter = new PrintWriter(file);
            templateEngine.process("item", context, printWriter);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (printWriter != null) {
                printWriter.close();
            }
        }
    }

    @Override
    public void deleteGoodsHtml(Long id) {
        File file = new File("D:\\MyGit\\myleyou\\leyouProject\\nginx-1.14.0\\html\\item\\"+id+".html");
        file.deleteOnExit();
    }
}
