package com.example.service_ticket_management_system.customer;

import com.example.service_ticket_management_system.ticket.Ticket;

import java.util.List;

public record CustomerResponseDTO(
        Long id,
        String name,
        String email,
        String phone
) {
}
