package com.leaf.sms.service.impl;

import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsResponse;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.leaf.sms.service.ISmsService;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class SmsServiceImpl implements ISmsService {

    @Autowired
    private com.alibaba.cloud.spring.boot.sms.SmsServiceImpl smsService;

    @Override
    public void send(String phoneNumber, String code) {
        SendSmsRequest request = new SendSmsRequest();
        request.setPhoneNumbers(phoneNumber);
        request.setSignName("阿里云签名");// aliyun sign.
        request.setTemplateCode("******");// aliyun templateCode.
        request.setSysMethod(MethodType.POST);
        Map<String, String> msg = new HashMap<>();
        msg.put("code", code);
        request.setTemplateParam(JSONObject.valueToString(msg));

        SendSmsResponse sendSmsResponse = null;
        try {
            sendSmsResponse = smsService.sendSmsRequest(request);
            if (!"OK".equals(sendSmsResponse.getCode())) {
                log.info("【短信服务】 发送短信失败, phoneNumber:{}, 原因:{}", "phoneNumber", phoneNumber, sendSmsResponse.getMessage());
            }
        } catch (ClientException e) {
            log.info("【短信服务】 发送短信失败, 短信服务连接异常:{}", e);
            throw new RuntimeException(e);
        }

    }

    @Override
    public List<QuerySendDetailsResponse.SmsSendDetailDTO> querySendDetails(QuerySendDetailsRequest request) {
        try {
            return smsService.querySendDetails(request).getSmsSendDetailDTOs();
        } catch (ClientException e) {
            throw new RuntimeException(e);
        }
    }
}
