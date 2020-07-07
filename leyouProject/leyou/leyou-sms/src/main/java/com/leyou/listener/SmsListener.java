package com.leyou.listener;

import com.aliyuncs.exceptions.ClientException;
import com.leyou.smsConfig.SmsProperties;
import com.leyou.utils.SmsUtils;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Map;

@Component
public class SmsListener {

    @Autowired
    private SmsUtils smsUtils;

    @Autowired
    private SmsProperties smsProperties;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "LEYOU.SMS.QUEUE",durable = "true"),
            exchange = @Exchange(value = "LEYOU.SMS.EXCHANGE",ignoreDeclarationExceptions = "true",type = ExchangeTypes.TOPIC),
            key = {"leyou.sms"}
    ))
    public void mySmsSend(Map<String,String> smsMap) throws ClientException {
        if(CollectionUtils.isEmpty(smsMap)){
            return;
        }
        String phone = smsMap.get("phone");
        String code = smsMap.get("code");
        if(StringUtils.isEmpty(phone)){
            return;
        }
        if(StringUtils.isEmpty(code)){
            return;
        }
        smsUtils.sendSms(phone, code, smsProperties.getSignName(), smsProperties.getVerifyCodeTemplate());
    }
}
