package com.leaf.mail.mq;

import com.leaf.mail.service.IMailService;
import com.leaf.sms.enties.SmsEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Slf4j
public class MailConsumer {

    @Autowired
    private IMailService mailService;

    @RabbitListener(bindings = {
            @QueueBinding(value = @Queue(name = "mail.verify.code", durable = "true"),
                    exchange = @Exchange(value = "cloud-leaf-sms.exchange",
                            type = ExchangeTypes.DIRECT
                    ),
                    key = "mail.verify.code"
            )
    })
    public void receive(Map<String, String> entity) {
        mailService.send(entity.get("to"), entity.get("code"));
    }
}
