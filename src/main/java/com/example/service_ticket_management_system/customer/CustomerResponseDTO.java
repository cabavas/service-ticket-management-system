package com.example.service_ticket_management_system.customer;

public record CustomerResponseDTO(
        Long id,
        String name,
        String email,
        String phone
) {
}
