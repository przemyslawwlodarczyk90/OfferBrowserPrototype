package com.example.offerbrowserprototype.infrastructure.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class MailService {

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

            // Tworzenie kontekstu dla Thymeleaf
            Context context = new Context();
            context.setVariable("username", username); // Używaj jednej zmiennej 'username'
            context.setVariable("confirmationLink", confirmationLink);

            // Generowanie treści e-maila na podstawie szablonu Thymeleaf
            String htmlContent = templateEngine.process("registration-email", context); // Upewnij się, że masz ten szablon

            // Ustawienie parametrów e-maila
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true); // 'true' oznacza HTML content

            // Wysłanie e-maila
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new IllegalStateException("Failed to send email", e);
        }
    }
}
