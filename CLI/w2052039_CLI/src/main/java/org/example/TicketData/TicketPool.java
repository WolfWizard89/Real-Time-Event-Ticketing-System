package org.example.TicketData;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

public class TicketPool {
    private int maximumTicketCapacity;
    // Using synchronized list to ensure thread safety
    private List<Ticket> tickets;

    public TicketPool(int maximumTicketCapacity) {
        this.maximumTicketCapacity = maximumTicketCapacity;
        // Collections.synchronizedList is used to make the list thread-safe
        this.tickets = Collections.synchronizedList(new ArrayList<>());
    }

    // Method for vendors to add tickets to the pool
    public synchronized void addTicket(Ticket ticket) {
        // Wait if the ticket pool has reached its capacity
        while (tickets.size() >= maximumTicketCapacity) {
            try {
                wait();  // Wait for tickets to be removed before adding more
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        // Add the ticket to the pool
        tickets.add(ticket);  // Add the ticket
        notifyAll();  // Notify all waiting threads
        System.out.println("Ticket added by: " + Thread.currentThread().getName() + " - Current pool size: " + tickets.size());
    }

    // Method for customers to purchase tickets from the pool
    public synchronized Ticket removeTicket() {
        // Wait if no tickets are available in the pool
        while (tickets.isEmpty()) {
            try {
                wait();  // Wait for tickets to be added before proceeding
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        // Remove the first ticket (FIFO) from the pool
        Ticket ticket = tickets.remove(0);  // Remove the first ticket (FIFO)
        notifyAll();  // Notify all waiting threads
        System.out.println("Ticket bought by: " + Thread.currentThread().getName() + " - Remaining tickets: " + tickets.size() + " - Ticket info: " + ticket);
        return ticket;
    }

    // Method to check the number of available tickets
    public synchronized int availableTickets() {
        return tickets.size();
    }
}


