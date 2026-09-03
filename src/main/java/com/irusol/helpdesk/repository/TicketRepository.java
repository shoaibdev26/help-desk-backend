package com.irusol.helpdesk.repository;

import com.irusol.helpdesk.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TicketRepository extends JpaRepository<Ticket,Long> {

   // Optional<Ticket> findByTicketId(Long ticketId);
    Optional<Ticket> findByEmail(String email);



}
