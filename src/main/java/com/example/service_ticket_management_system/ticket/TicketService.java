package com.example.service_ticket_management_system.ticket;

import com.example.service_ticket_management_system.customer.Customer;
import com.example.service_ticket_management_system.customer.CustomerRepository;
import com.example.service_ticket_management_system.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final CustomerRepository customerRepository;

    public TicketService(TicketRepository ticketRepository, CustomerRepository customerRepository) {
        this.ticketRepository = ticketRepository;
        this.customerRepository = customerRepository;
    }

    @Transactional(readOnly = true)
    public Page<TicketResponseDTO> findAll(Pageable pageable, Status status) {
        Page<Ticket> ticketPage;
        if (status != null) {
            ticketPage = ticketRepository.findByStatusWithCustomer(status, pageable);
        } else {
            ticketPage = ticketRepository.findAllWithCustomer(pageable);
        }
        return ticketPage.map(this::toResponseDTO);
    }

    @Transactional(readOnly = true)
    public TicketResponseDTO findById(Long id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found with ID: " + id));
        return this.toResponseDTO(ticket);
    }

    @Transactional
    public TicketResponseDTO create(TicketRequestDTO requestDTO) {
        Customer customer = customerRepository.findById(requestDTO.customerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with ID: " + requestDTO.customerId()));
        System.out.println(customer);
        Ticket ticket = toEntity(requestDTO);
        ticket.setProtocol(generateProtocol());
        ticket.setCreatedAt(LocalDateTime.now());
        ticket.setCustomer(customer);
        ticket.setStatus(requestDTO.status() != null ? requestDTO.status() : Status.OPEN);
        Ticket saved = ticketRepository.save(ticket);
        return toResponseDTO(saved);
    }

    @Transactional
    public TicketResponseDTO update(Long id, TicketRequestDTO requestDTO) {
        Ticket existing = ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found with ID: " + id));
        existing.setStatus(requestDTO.status());
        existing.setDescription(requestDTO.description());
        existing.setTitle(requestDTO.title());
        Ticket updated = ticketRepository.save(existing);
        return toResponseDTO(updated);
    }

    @Transactional
    public void delete(Long id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found with ID: " + id));
        ticketRepository.delete(ticket);
    }

    private TicketResponseDTO toResponseDTO(Ticket ticket) {
        Customer customer = ticket.getCustomer();
        return new TicketResponseDTO(
                ticket.getId(),
                ticket.getProtocol(),
                ticket.getDescription(),
                ticket.getTitle(),
                ticket.getCreatedAt(),
                ticket.getStatus(),
                customer.getName(),
                customer.getEmail()
        );
    }

    private Ticket toEntity(TicketRequestDTO requestDTO) {
        Ticket ticket = new Ticket();
        ticket.setTitle(requestDTO.title());
        ticket.setDescription(requestDTO.description());
        ticket.setStatus(requestDTO.status());

        return ticket;
    }

    private String generateProtocol() {
        int year = java.time.Year.now().getValue();

        String uniquePart = UUID.randomUUID().toString()
                .replace("-", "")
                .substring(0, 6)
                .toUpperCase();
        return String.format("SO-%d-%s", year, uniquePart);
    }

    public void close(Long id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found with ID: " + id));
        ticket.setStatus(Status.CLOSED);
    }
}
