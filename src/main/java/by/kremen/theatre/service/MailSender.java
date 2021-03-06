package by.kremen.theatre.service;

import by.kremen.theatre.controller.ReviewController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Properties;


public class MailSender {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(MailSender.class);

    public JavaMailSenderImpl JavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);

        mailSender.setUsername("nodejs.lab6@gmail.com");
        mailSender.setPassword("1QAZ2wsx3edc");

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        log.info("Mail sender was configured");
        return mailSender;
    }
    public boolean Send(String subject, String text, String email) {
        JavaMailSenderImpl javaMailSender = JavaMailSender();
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setSubject(subject); //The subject of the mail
        mailMessage.setText(text);
        mailMessage.setTo(email); //Who to send to
        mailMessage.setFrom("nodejs.lab6@gmail.com"); //Who sent it
        javaMailSender.send(mailMessage);
        log.info("Mail was sent to " + email);
        return true;
    }
}
