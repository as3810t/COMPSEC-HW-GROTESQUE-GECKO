package hu.grotesque_gecko.caffstore.email.service;

import hu.grotesque_gecko.caffstore.user.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    JavaMailSender emailSender;

    @Value("${spring.mail.sender:noreply@localhost}")
    String sender;

    public void sendPasswordResetNotification(User to) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(sender);
        message.setTo(to.getEmail());
        message.setSubject("CAFFStore - Your password has been reset");
        message.setText("Your password has been reset by an administrator. To login, use the forgot password link on the login screen!");
        emailSender.send(message);
    }

    public void sendNewTemporaryPassword(User to, String rawPassword) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(sender);
        message.setTo(to.getEmail());
        message.setSubject("CAFFStore - Forgotten password");
        message.setText("Your temporary password to login with: " + rawPassword);
        emailSender.send(message);
    }
}
