package com.example.offerbrowserprototype.infrastructure.service;

import com.example.offerbrowserprototype.domain.dto.offer.OfferDTO;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.List;
@Service
public class MailService {

    private static final Logger log = LoggerFactory.getLogger(MailService.class);

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    public MailService(JavaMailSender mailSender, TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    public void sendConfirmationEmail(String to, String subject, String username, String confirmationLink) {
        MimeMessage message = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            Context context = new Context();
            context.setVariable("username", username);
            context.setVariable("confirmationLink", confirmationLink);

            String htmlContent = templateEngine.process("registration-email", context);

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);

            log.info("Sending confirmation email to {}", to);
            mailSender.send(message);
        } catch (MessagingException e) {
            log.error("Failed to send confirmation email to {}: {}", to, e.getMessage());
            throw new IllegalStateException("Failed to send email", e);
        }
    }

    public void sendDailyOffersEmail(String recipientEmail, List<OfferDTO> offers) {
        MimeMessage message = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            Context context = new Context();
            context.setVariable("offers", offers); // Przekazanie pełnej listy obiektów OfferDTO

            String htmlContent = templateEngine.process("daily-offers-email", context);

            helper.setTo(recipientEmail);
            helper.setSubject("Your Daily Unapplied Job Offers");
            helper.setText(htmlContent, true);

            log.info("Sending daily offers email to {}", recipientEmail);
            log.info("Offers: {}", offers);

            mailSender.send(message);
        } catch (MessagingException e) {
            log.error("Failed to send daily offers email to {}: {}", recipientEmail, e.getMessage());
            throw new IllegalStateException("Failed to send daily offers email", e);
        }
    }
}
