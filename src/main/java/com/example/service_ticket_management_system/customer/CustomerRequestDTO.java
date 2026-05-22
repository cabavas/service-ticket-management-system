package com.example.service_ticket_management_system.customer;

import com.example.service_ticket_management_system.ticket.Ticket;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record CustomerRequestDTO(
        @NotBlank(message = "Name is required")
        String name,

        @NotBlank(message = "Email is required")
        @Email(message = "Email must be valid")
        String email,
        String phone
) {
}
