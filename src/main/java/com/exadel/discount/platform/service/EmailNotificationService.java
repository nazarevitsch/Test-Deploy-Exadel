package com.exadel.discount.platform.service;

import com.exadel.discount.platform.domain.EmailType;
import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.StringWriter;
import java.util.Map;

@Service
public class EmailNotificationService {

    @Autowired
    private JavaMailSender mailSender;
    private MustacheFactory mf = new DefaultMustacheFactory();

    public void sendEmail(EmailType emailType, String to, Map<String, String> templateData) {
        Mustache m = null;

        switch (emailType) {
            case TO_VENDOR_USING_DISCOUNT:
                m = mf.compile("templates/email_to_vendor");
                break;
        }
        try {
            StringWriter writer = new StringWriter();
            m.execute(writer, templateData).flush();
            String html = writer.toString();
            sendHtmlMessage(to, "Your discount was used", html);
        } catch (Exception e) {}
    }

    private void sendSimpleTextMessage(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);

        mailSender.send(message);
    }

    private void sendHtmlMessage(String to, String subject, String textHtml) throws MessagingException {
        MimeMessage mail = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mail, true);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(textHtml, true);

        mailSender.send(mail);
    }
}
