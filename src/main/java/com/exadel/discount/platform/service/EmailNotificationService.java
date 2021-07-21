package com.exadel.discount.platform.service;

import com.exadel.discount.platform.domain.*;
import com.exadel.discount.platform.domain.enums.EmailType;
import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class EmailNotificationService {

    @Value("${spring.mail.username}")
    private String email;
    @Value("${spring.mail.password}")
    private String password;

    @Autowired
    private JavaMailSender mailSender;
    private MustacheFactory mf = new DefaultMustacheFactory();

    public void notifyVendorAboutUsageOfDiscount(String to, Discount discount, MyUserDetails details,
                                                 UsedDiscount usedDiscount, UserDetails userDetails) {
        HashMap<String, String> dataEmailTemplate = new HashMap<>();
        dataEmailTemplate.put("discountTitle", discount.getName());
        dataEmailTemplate.put("username", userDetails.getName());
        dataEmailTemplate.put("userEmail", details.getUsername());
        dataEmailTemplate.put("uniqueCode", usedDiscount.getId().toString());
        dataEmailTemplate.put("vendorTitle", discount.getVendor().getName());

        try {
            sendHtmlMessage(EmailType.DISCOUNT_USED_NOTIFY_VENDOR, to, "Your discount was used", dataEmailTemplate);
        } catch (Exception e) {
            System.out.println(email);
            System.out.println(password);
            log.info("Email wasn't sent :(");
            e.printStackTrace();
        }
    }

    private void sendSimpleTextMessage(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);

        mailSender.send(message);
    }

    private void sendHtmlMessage(EmailType emailType, String to, String subject, Map<String, String> templateData) throws MessagingException, IOException {
        MimeMessage mail = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mail, true);
        Mustache m = null;

        switch (emailType) {
            case DISCOUNT_USED_NOTIFY_VENDOR:
                m = mf.compile("templates/discount_used_notify_vendor.html");
                break;
            default:
                throw new IllegalArgumentException("Incorrect email's type.");
        }

        StringWriter writer = new StringWriter();
        m.execute(writer, templateData).flush();
        String html = writer.toString();

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(html, true);

        mailSender.send(mail);
    }
}
