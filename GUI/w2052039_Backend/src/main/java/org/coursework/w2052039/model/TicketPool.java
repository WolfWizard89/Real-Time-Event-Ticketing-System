package org.coursework.w2052039.model;

import org.coursework.w2052039.webConfig.LoggerService;
import org.coursework.w2052039.webConfig.RemainTicketService;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TicketPool {
    private final RemainTicketService remainTicketService;
    private int maximumTicketCapacity;
    private List<Ticket> tickets;
    private final LoggerService loggerService;

    // Constructor injection is used to ensure that the dependencies are provided when the TicketPool is instantiated.
    @Autowired
    public TicketPool(@Value("${ticketPool.maxCapacity}") int maximumTicketCapacity, LoggerService loggerService, RemainTicketService remainTicketService) {
        this.maximumTicketCapacity = maximumTicketCapacity;
        // Synchronized list is used to ensure thread safety when multiple threads access the ticket list.
        this.tickets = Collections.synchronizedList(new ArrayList<>());
        this.loggerService = loggerService;
        this.remainTicketService = remainTicketService;
    }


    // Method for vendors to add tickets to the pool
    public synchronized void addTicket(Ticket ticket) {
        // Wait if the ticket pool has reached its capacity
        while (tickets.size() >= maximumTicketCapacity) {
            try {
                wait();  // Wait for tickets to be removed before adding more
            } catch (InterruptedException e) {
                System.out.println("Thread was interrupted, failed to add ticket");
                Thread.currentThread().interrupt();
                break;

            }
        }
        tickets.add(ticket);  // Add the ticket
        notifyAll();  // Notify all waiting threads
        System.out.println("Ticket added by: " + Thread.currentThread().getName() + " - Current pool size: " + tickets.size());
        loggerService.sendLog("Ticket added by: " + Thread.currentThread().getName() + " - Current pool size: " + tickets.size());
    }

    // Method for customers to purchase tickets from the pool
    public synchronized Ticket removeTicket() {
        // Wait if no tickets are available in the pool
        while (tickets.isEmpty()) {
            try {
                wait();  // Wait for tickets to be added before proceeding
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
                return null;
            }
        }
        Ticket ticket = tickets.remove(0);  // Remove the first ticket (FIFO)
        notifyAll();  // Notify all waiting threads
        System.out.println("Ticket bought by: " + Thread.currentThread().getName() + " - Remaining tickets: " + tickets.size() + " - Ticket info: " + ticket);
        loggerService.sendLog("Ticket bought by: " + Thread.currentThread().getName() + " - Remaining tickets: " + tickets.size() + " - Ticket info: " + ticket);

        return ticket;
    }
    // Method to get the number of available tickets
    public synchronized int availableTickets() {
        remainTicketService.sendLog("Available tickets: " + tickets.size());
        return tickets.size();
    }

}

