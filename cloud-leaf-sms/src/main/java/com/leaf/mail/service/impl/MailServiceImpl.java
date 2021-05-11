package com.leaf.mail.service.impl;

import com.leaf.mail.service.IMailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Date;

@Service
@Slf4j
public class MailServiceImpl implements IMailService {
    static StringBuilder text = new StringBuilder();

    static {
        text.append("<head>\n")
                .append("    <base target=\"_blank\" />\n")
                .append("    <style type=\"text/css\">::-webkit-scrollbar{ display: none; }</style>\n")
                .append("    <style id=\"cloudAttachStyle\" type=\"text/css\">#divNeteaseBigAttach, #divNeteaseBigAttach_bak{display:none;}</style>\n")
                .append("    <style id=\"blockquoteStyle\" type=\"text/css\">blockquote{display:none;}</style>\n")
                .append("    <style type=\"text/css\">\n")
                .append("        body{font-size:14px;font-family:arial,verdana,sans-serif;line-height:1.666;padding:0;margin:0;overflow:auto;white-space:normal;word-wrap:break-word;min-height:100px}\n")
                .append("        td, input, button, select, body{font-family:Helvetica, 'Microsoft Yahei', verdana}\n")
                .append("        pre {white-space:pre-wrap;white-space:-moz-pre-wrap;white-space:-pre-wrap;white-space:-o-pre-wrap;word-wrap:break-word;width:95%}\n")
                .append("        th,td{font-family:arial,verdana,sans-serif;line-height:1.666}\n")
                .append("        img{ border:0}\n")
                .append("        header,footer,section,aside,article,nav,hgroup,figure,figcaption{display:block}\n")
                .append("        blockquote{margin-right:0px}\n")
                .append("    </style>\n")
                .append("</head>\n")
                .append("<body tabindex=\"0\" role=\"listitem\">\n")
                .append("<table width=\"700\" border=\"0\" align=\"center\" cellspacing=\"0\" style=\"width:700px;\">\n")
                .append("    <tbody>\n")
                .append("    <tr>\n")
                .append("        <td>\n")
                .append("            <div style=\"width:700px;margin:0 auto;border-bottom:1px solid #ccc;margin-bottom:30px;\">\n")
                .append("                <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"700\" height=\"39\" style=\"font:12px Tahoma, Arial, 宋体;\">\n")
                .append("                    <tbody><tr><td width=\"210\"></td></tr></tbody>\n")
                .append("                </table>\n")
                .append("            </div>\n")
                .append("            <div style=\"width:680px;padding:0 10px;margin:0 auto;\">\n")
                .append("                <div style=\"line-height:1.5;font-size:14px;margin-bottom:25px;color:#4d4d4d;\">\n")
                .append("                    <strong style=\"display:block;margin-bottom:15px;\">尊敬的用户：<span style=\"color:#f60;font-size: 16px;\"></span>您好！</strong>\n")
                .append("                    <strong style=\"display:block;margin-bottom:15px;\">\n")
                .append("                        您正在进行<span style=\"color: red\">注册</span>操作，请在验证码输入框中输入：<span style=\"color:#f60;font-size: 24px\">#{code}</span>，以完成操作。验证码有效期为5分钟。\n")
                .append("                    </strong>\n")
                .append("                </div>\n")
                .append("                <div style=\"margin-bottom:30px;\">\n")
                .append("                    <small style=\"display:block;margin-bottom:20px;font-size:12px;\">\n")
                .append("                        <p style=\"color:#747474;\">\n")
                .append("                            注意：此操作可能会修改您的密码、登录邮箱或绑定手机。如非本人操作，请及时登录并修改密码以保证帐户安全\n")
                .append("                            <br>（工作人员不会向你索取此验证码，请勿泄漏！)\n")
                .append("                        </p>\n")
                .append("                    </small>\n")
                .append("                </div>\n")
                .append("            </div>\n")
                .append("            <div style=\"width:700px;margin:0 auto;\">\n")
                .append("                <div style=\"padding:10px 10px 0;border-top:1px solid #ccc;color:#747474;margin-bottom:20px;line-height:1.3em;font-size:12px;\">\n")
                .append("                    <p>此为系统邮件，请勿回复<br>\n")
                .append("                        请保管好您的邮箱，避免账号被他人盗用\n")
                .append("                    </p>\n")
                .append("                    <p>光之国网络科技团队</p>\n")
                .append("                </div>\n")
                .append("            </div>\n")
                .append("        </td>\n")
                .append("    </tr>\n")
                .append("    </tbody>\n")
                .append("</table>\n")
                .append("</body>\n");
    }


    @Autowired
    private JavaMailSender javaMailSender;

    @Override
    public void send(String to, String code) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom("xxxxx@163.com");//发件人  要和 spring.mail.username 一致
            helper.setSentDate(new Date());
            helper.setSubject("光之国商城注册验证码");
            helper.setTo(to);
            helper.setText(text.toString().replace("#{code}", code), true);//第二个参数默认为false 为显示html源码

            javaMailSender.send(mimeMessage);
            log.info("【邮件服务】向{} 发送成功,验证码为【{}】.", to, code);
        } catch (MessagingException e) {
            log.error("【邮件服务】向{} 发送失败,原因：{}.", to, e);
        }

    }


}
