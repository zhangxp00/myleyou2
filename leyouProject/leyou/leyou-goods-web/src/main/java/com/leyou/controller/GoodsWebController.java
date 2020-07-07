package com.leyou.controller;

import com.leyou.service.GoodsHtmlService;
import com.leyou.service.GoodsWebService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@Controller
public class GoodsWebController {
    @Autowired
    private GoodsWebService goodsWebServiceImpl;

    @Autowired
    private GoodsHtmlService goodsHtmlService;

    @GetMapping("item/{id}.html")
    public String goodsWeb(@PathVariable(value = "id") Long id, Model model){
        Map<String, Object> map = goodsWebServiceImpl.goodWebParam(id);
        model.addAllAttributes(map);
        //创造静态页面
        goodsHtmlService.createGoodsHtml(id);
        return "item";
    }
}
