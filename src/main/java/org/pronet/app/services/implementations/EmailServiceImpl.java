package org.pronet.app.services.implementations;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.pronet.app.payloads.EmailDetails;
import org.pronet.app.services.EmailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Objects;

@Service
public class EmailServiceImpl implements EmailService {
    @Value("${spring.mail.username}")
    private String senderMail;
    private final JavaMailSender mailSender;

    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendEmail(EmailDetails details) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(senderMail);
            mailMessage.setTo(details.getRecipient());
            mailMessage.setSubject(details.getSubject());
            mailMessage.setText(details.getTextBody());

            mailSender.send(mailMessage);
        } catch (MailException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void sendEmailWithAttachment(EmailDetails details) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper;
        try {
            messageHelper = new MimeMessageHelper(mimeMessage, true);
            messageHelper.setFrom(senderMail);
            messageHelper.setTo(details.getRecipient());
            messageHelper.setSubject(details.getSubject());
            messageHelper.setText(details.getTextBody());

            FileSystemResource file = new FileSystemResource(new File(details.getAttachment()));
            messageHelper.addAttachment(Objects.requireNonNull(file.getFilename()), file);

            mailSender.send(mimeMessage);
        } catch (MessagingException ex) {
            throw new RuntimeException(ex);
        }
    }
}
