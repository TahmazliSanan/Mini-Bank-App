package org.pronet.app.services.implementations;

import org.pronet.app.payloads.EmailDetails;
import org.pronet.app.services.EmailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {
    @Value("${spring.mail.username}")
    private String senderMail;
    private final JavaMailSender jms;

    public EmailServiceImpl(JavaMailSender jms) {
        this.jms = jms;
    }

    @Override
    public void sendEmail(EmailDetails emailDetails) {
        try {
            SimpleMailMessage smm = new SimpleMailMessage();
            smm.setFrom(senderMail);
            smm.setTo(emailDetails.getRecipient());
            smm.setSubject(emailDetails.getSubject());
            smm.setText(emailDetails.getTextBody());

            jms.send(smm);
        } catch (MailException ex) {
            throw new RuntimeException(ex);
        }
    }
}
