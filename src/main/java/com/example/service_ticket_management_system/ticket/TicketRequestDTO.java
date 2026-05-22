package com.example.service_ticket_management_system.ticket;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TicketRequestDTO(
        @NotBlank(message = "Title is required")
        String title,

        @NotBlank(message = "Description is required")
        String description,

        Status status,

        @NotNull(message = "Customer is required")
        Long customerId
) {
}
