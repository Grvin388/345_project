package com.ticketapp.backend.service;

import com.ticketapp.backend.model.Reservation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailService emailService;

    @Test
    void sendReservationConfirmation_buildsExpectedMessageAndSendsIt() {
        Reservation reservation = new Reservation(42L, "uid123", "user@test.com", 3, "CONFIRMED");
        ArgumentCaptor<SimpleMailMessage> messageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);

        emailService.sendReservationConfirmation("user@test.com", reservation);

        verify(mailSender).send(messageCaptor.capture());

        SimpleMailMessage message = messageCaptor.getValue();
        assertEquals("alex18288@gmail.com", message.getFrom());
        assertThat(message.getTo()).containsExactly("user@test.com");
        assertEquals("Reservation Confirmed", message.getSubject());
        assertThat(message.getText()).contains("Event ID: 42");
        assertThat(message.getText()).contains("Quantity: 3");
        assertThat(message.getText()).contains("Status: CONFIRMED");
    }

    @Test
    void sendReservationConfirmation_propagatesMailSenderFailures() {
        Reservation reservation = new Reservation(42L, "uid123", "user@test.com", 3, "CONFIRMED");
        doThrow(new RuntimeException("smtp down")).when(mailSender)
                .send(org.mockito.ArgumentMatchers.any(SimpleMailMessage.class));

        assertThatThrownBy(() -> emailService.sendReservationConfirmation("user@test.com", reservation))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("smtp down");
    }
}