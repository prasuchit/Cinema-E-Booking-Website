package edu.uga.cinemaapp.service;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

public class Email {

    private String username = "tempswemail@gmail.com";
    private String password = "123456tempswemail";
    private String subject = "";
    private String body = "";
    private String email = "";

    public Email(String email, String subject, String body) {
        this.email = email;
        this.subject = subject;
        this.body = body;
    }

    public Email(String username, String password) {
        this.username = username;
        this.password = password;

    }

    // String activationHash, String Url
    @Async
    public void SendEmail() {

        Properties props = new Properties();
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
            message.setSubject(subject);
            message.setText(body);
            // message.setText("The Activation Link is http://localhost:8080/" + Url +
            // activationHash);
            Transport.send(message);
            // System.out.println("Message Sent Successfully!");
        } catch (MessagingException e) {
            System.out.println("sending failed!");
            throw new RuntimeException(e);
        }
    }
}