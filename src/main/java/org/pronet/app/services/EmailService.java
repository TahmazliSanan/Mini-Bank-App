package org.pronet.app.services;

import org.pronet.app.payloads.EmailDetails;

public interface EmailService {
    void sendEmail(EmailDetails details);
    void sendEmailWithAttachment(EmailDetails details);
}
