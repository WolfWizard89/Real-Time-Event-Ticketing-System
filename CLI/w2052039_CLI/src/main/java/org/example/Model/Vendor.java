package org.example.Model;

import org.example.TicketData.Ticket;
import org.example.TicketData.TicketPool;

import java.util.concurrent.atomic.AtomicBoolean;

public class Vendor implements Runnable {

    private int totalTickets;
    private String vendorName;
    private int ticketReleaseRate;
    private TicketPool ticketPool;
    private AtomicBoolean isRunning;

    // Constructor to initialize the vendor with necessary details
    public Vendor(int totalTickets, int ticketReleaseRate, TicketPool ticketPool, String vendorName) {

        this.totalTickets = totalTickets;
        this.ticketReleaseRate = ticketReleaseRate;
        this.vendorName = vendorName;
        this.ticketPool = ticketPool;
        // AtomicBoolean is used for thread-safe flag to control the running state
        this.isRunning = new AtomicBoolean(true);
    }

    @Override
    public void run() {
        // Loop to add tickets to the pool
        for (int i = 1; i < totalTickets; i++) {
            // Create a new ticket with unique ID and event name
            Ticket ticket = new Ticket(i, vendorName, 1000);
            // Add the ticket to the pool
            ticketPool.addTicket(ticket);
        }
        try {
            // Sleep to control the rate at which tickets are released
            Thread.sleep(ticketReleaseRate * 1000);
        } catch (InterruptedException e) {
            // Handle interruption during sleep
            throw new RuntimeException(e);
        }
    }
    public void stop() {
        isRunning.set(false);
    }

}

