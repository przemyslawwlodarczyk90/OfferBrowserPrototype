package com.example.offerbrowserprototype.infrastructure.web;

import com.example.offerbrowserprototype.domain.dto.offer.OfferDTO;
import com.example.offerbrowserprototype.infrastructure.facade.OfferFacade;
import com.example.offerbrowserprototype.domain.user.User;
import com.example.offerbrowserprototype.infrastructure.service.MailService;
import com.example.offerbrowserprototype.infrastructure.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@Tag(name = "Daily Notifications", description = "Endpoints for managing daily notifications")
public class NotificationController {

    private static final Logger log = LoggerFactory.getLogger(NotificationController.class);

    private final OfferFacade offerFacade;
    private final MailService mailService;
    private final UserService userService;

    @Value("${spring.mail.username}")
    private String senderEmail;

    public NotificationController(OfferFacade offerFacade, MailService mailService, UserService userService) {
        this.offerFacade = offerFacade;
        this.mailService = mailService;
        this.userService = userService;
    }

    @Operation(summary = "Send daily unapplied offers email", description = "Manually trigger the daily email with unapplied offers.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Email sent successfully"),
            @ApiResponse(responseCode = "204", description = "No unapplied offers to notify"),
            @ApiResponse(responseCode = "500", description = "Error occurred while sending the email")
    })
    @GetMapping("/daily-unapplied-offers")
    @Scheduled(cron = "${daily.unapplied.offers.cron}")
    public void sendDailyUnappliedOffers() {
        log.info("Scheduler started for sending daily unapplied offers.");


        List<User> users = userService.getAllUsers();

        for (User user : users) {
            List<OfferDTO> unappliedOffers = offerFacade.getNotAppliedOffers();

            if (unappliedOffers.isEmpty()) {
                log.info("No unapplied offers found for user: {}", user.getEmail());
                continue;
            }

            try {

                mailService.sendDailyOffersEmail(user.getEmail(), unappliedOffers);
                log.info("Daily unapplied offers email sent to: {}", user.getEmail());
            } catch (Exception e) {
                log.error("Failed to send email to {}: {}", user.getEmail(), e.getMessage());
            }
        }
    }
}
