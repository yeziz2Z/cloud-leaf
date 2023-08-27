//package com.leaf.sms.controller;
//
////import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsRequest;
////import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsResponse;
//import com.leaf.common.result.Result;
//import com.leaf.sms.service.ISmsService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/sms")
//public class SmsController {
//
//    @Autowired
//    private ISmsService smsService;
//
//    @PostMapping("/send")
//    public Result send(String phoneNumber, String code) {
////        smsService.send(phoneNumber, code);
//        return Result.success();
//    }
//
//   /* @GetMapping("querySendDetails")
//    public Result<QuerySendDetailsResponse.SmsSendDetailDTO> querySendDetails(QuerySendDetailsRequest request) {
//        Result success = Result.success();
//        success.setData(smsService.querySendDetails(request));
//        return success;
//    }*/
//}
