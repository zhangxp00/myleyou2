package com.leyou.search.listener;

import com.leyou.search.service.SearchService;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GoodsSearchListener {

    @Autowired
    private SearchService searchServiceImpl;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "LEYOU.ITEM.INSERT.SEARCH",durable = "true"),
            exchange = @Exchange(value = "LEYOU.ITEM.EXCHANGE",ignoreDeclarationExceptions = "true",type = ExchangeTypes.TOPIC),
            key = {"item.insert","item.update"}
    ))
    private void saveAndUpdateGoodsToSearch(Long id){
        if(id==null){
            return;
        }
        searchServiceImpl.saveOrUpdate(id);
    }
}
