package com.ticketapp.backend.service;

import com.ticketapp.backend.model.Reservation;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendReservationConfirmation(String toEmail, Reservation reservation) {
        System.out.println("Preparing to send confirmation email to: " + toEmail);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("alex18288@gmail.com");
        message.setTo(toEmail);
        message.setSubject("Reservation Confirmed");
        message.setText(
                "Hello,\n\n" +
                        "Your reservation has been confirmed.\n\n" +
                        "Event ID: " + reservation.getEventId() + "\n" +
                        "Quantity: " + reservation.getQuantity() + "\n" +
                        "Status: " + reservation.getStatus() + "\n\n" +
                        "Thank you for booking with us.");

        mailSender.send(message);
        System.out.println("Confirmation email sent to: " + toEmail);
    }
}