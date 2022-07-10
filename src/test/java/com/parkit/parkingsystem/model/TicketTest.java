package com.parkit.parkingsystem.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TicketTest {



    @Test
    void setId() {
        Ticket ticket = new Ticket();
        ticket.setId(1);
        assertEquals(1, ticket.getId());
    }
}