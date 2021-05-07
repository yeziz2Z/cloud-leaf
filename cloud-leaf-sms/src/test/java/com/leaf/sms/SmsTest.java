package com.leaf.sms;

import com.alibaba.cloud.spring.boot.sms.AbstractSmsService;
import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsResponse;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
@Slf4j
public class SmsTest {

    @Autowired
    private AbstractSmsService smsService;

    @Test
    public void test() throws ClientException {
        SendSmsRequest request = new SendSmsRequest();
        request.setPhoneNumbers("PhoneNumbers");
        request.setSignName("SignName");
        request.setTemplateCode("TemplateCode");
        request.setSysMethod(MethodType.POST);
        Map<String, String> msg = new HashMap<>();
        msg.put("code", "112234");
        request.setTemplateParam(JSONObject.valueToString(msg));

        SendSmsResponse sendSmsResponse = smsService.sendSmsRequest(request);
        System.out.println(sendSmsResponse);
        if (!"OK".equals(sendSmsResponse.getCode())) {
            log.info("【短信服务】 发送短信失败, phoneNumber:{}, 原因:{}", "phoneNumber", sendSmsResponse.getMessage());
        }


    }

    @Test
    public void testQuery() throws ClientException {
        QuerySendDetailsRequest request = new QuerySendDetailsRequest();
        request.setPhoneNumber("18634416025");
        request.setSendDate("20210506");
//        request.setOwnerId(1000L);
        request.setPageSize(10L);
        request.setCurrentPage(1L);
        QuerySendDetailsResponse response = smsService.querySendDetails(request);
        log.info("response code: {},details:{}",response.getCode(),response.getSmsSendDetailDTOs());
        log.info("account{}",request.getResourceOwnerAccount());
    }
}
