package com.leaf.sms;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RabbitTest {

    @Autowired
    private AmqpTemplate template;

    @Test
    public void test() {
        System.out.println(template);
        /*Message message = new Message();
        template.send();*/
    }

}
