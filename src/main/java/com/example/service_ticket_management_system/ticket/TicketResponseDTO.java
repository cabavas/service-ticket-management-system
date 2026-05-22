package com.example.service_ticket_management_system.ticket;

import java.time.LocalDateTime;

public record TicketResponseDTO(
        Long id,
        String protocol,
        String title,
        String description,
        LocalDateTime createdAt,
        Status statusResponseEntity,
        String customerName,
        String customerEmail
) {
}
