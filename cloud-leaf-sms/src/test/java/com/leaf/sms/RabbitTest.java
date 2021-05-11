package com.leaf.sms;

import com.leaf.sms.enties.SmsEntity;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;

@SpringBootTest
public class RabbitTest {

    @Autowired
    private AmqpTemplate template;

    @Test
    public void test() {
        System.out.println(template);

        SmsEntity entity = new SmsEntity();
        entity.setCode("1234");
        entity.setPhoneNumber("186****6025");

        template.convertAndSend("cloud-leaf-sms.exchange", "sms.verify.code", entity);

    }

    @Test
    public void testSendMail() {
        HashMap<String, String> map = new HashMap<>();

        map.put("to", "xxxx@qq.com");
        map.put("code", "9527");
        template.convertAndSend("cloud-leaf-sms.exchange", "mail.verify.code", map);
    }

}
