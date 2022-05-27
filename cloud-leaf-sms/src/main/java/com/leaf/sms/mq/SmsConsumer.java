package com.leaf.sms.mq;

import com.leaf.sms.enties.SmsEntity;
import com.leaf.sms.service.ISmsService;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SmsConsumer {

    @Autowired
    private ISmsService smsService;

    @RabbitListener(bindings = {
            @QueueBinding(value = @Queue(name = "sms.verify.code", durable = "true"),
                    exchange = @Exchange(value = "cloud-leaf-sms.exchange",
                            type = ExchangeTypes.DIRECT
                    ),
                    key = "sms.verify.code"
            )
    })
    public void receive(SmsEntity entity) {
//        smsService.send(entity.getPhoneNumber(), entity.getCode());
    }

}
