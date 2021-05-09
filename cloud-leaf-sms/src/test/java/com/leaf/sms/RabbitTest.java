package com.leaf.sms;

import com.leaf.sms.enties.SmsEntity;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RabbitTest {

    @Autowired
    private AmqpTemplate template;

    @Test
    public void test() {
        System.out.println(template);

        SmsEntity entity = new SmsEntity();
        entity.setCode("1234");
        entity.setPhoneNumber("18634416025");

        template.convertAndSend("cloud-leaf-sms.exchange", "sms.verify.code", entity);

    }

}
