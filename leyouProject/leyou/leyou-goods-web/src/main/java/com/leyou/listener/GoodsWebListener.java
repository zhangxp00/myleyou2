package com.leyou.listener;

import com.leyou.service.GoodsHtmlService;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GoodsWebListener {

    @Autowired
    private GoodsHtmlService goodsHtmlServiceImpl;

    /**
     * 商品添加以及修改的同步rabbit方法
     * @param id
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "LEYOU.ITEM.INSERT.QUEUE",durable = "true"),
            exchange = @Exchange(value = "LEYOU.ITEM.EXCHANGE",ignoreDeclarationExceptions = "true",type = ExchangeTypes.TOPIC),
            key = {"item.insert","item.update"}
    ))
    public void saveAndUpdateGoods(Long id){
        if(id==null){
            return;
        }
        goodsHtmlServiceImpl.createGoodsHtml(id);
    }

    /**
     * 删除商品时删除静态页面
     * @param id
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "LEYOU.ITEM.DELETE.QUEUE",durable = "true"),
            exchange = @Exchange(value = "LEYOU.ITEM.EXCHANGE",ignoreDeclarationExceptions = "true",type = ExchangeTypes.TOPIC),
            key = {"item.delete"}
    ))
    public void deleteGoods(Long id){
        if(id==null){
           return;
        }
        goodsHtmlServiceImpl.deleteGoodsHtml(id);
    }
}
