package com.example.service_ticket_management_system.ticket;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    @Query(value = "SELECT t FROM Ticket t JOIN FETCH t.customer",
            countQuery = "SELECT COUNT(t) FROM Ticket t")
    Page<Ticket> findAllWithCustomer(Pageable pageable);

    @Query(value = "SELECT t FROM Ticket t JOIN FETCH t.customer WHERE t.status = :status",
            countQuery = "SELECT COUNT(t) FROM Ticket t WHERE t.status = :status")
    Page<Ticket> findByStatusWithCustomer(Status status, Pageable pageable);
}
