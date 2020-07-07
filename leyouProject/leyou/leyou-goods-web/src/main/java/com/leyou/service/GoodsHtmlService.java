package com.leyou.service;

public interface GoodsHtmlService {

    /**
     * 创建商品详情页的静态页面
     * @param id
     */
    public void createGoodsHtml(Long id);

    /**
     * 删除商品详情页的静态页面
     * @param id
     */
    void deleteGoodsHtml(Long id);
}
