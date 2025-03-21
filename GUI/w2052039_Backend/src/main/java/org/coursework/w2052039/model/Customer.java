package org.coursework.w2052039.model;


import org.coursework.w2052039.webConfig.LoggerService;

import java.util.concurrent.atomic.AtomicBoolean;

public class Customer implements Runnable {
    private TicketPool ticketPool;
    private int customerRetrivelRate;
    private int ticketToPurchase;
    private String customerName;
    private AtomicBoolean isRunning;
    private final LoggerService loggerService;
    private volatile boolean stopRequested = false;
    private Thread thread;


    // Constructor to initialize the Customer with necessary dependencies and parameters
    public Customer(TicketPool ticketPool, int customerRetrivelRate, int ticketToPurchase, String customerName, LoggerService loggerService) {
        this.ticketPool = ticketPool;
        this.customerRetrivelRate = customerRetrivelRate;
        this.ticketToPurchase = ticketToPurchase;
        this.customerName = customerName;
        this.loggerService = loggerService;
    }

    // Method to request the customer thread to stop
    public void stopWork() {
        stopRequested = true;
        if (thread != null) {
            thread.interrupt();
        }
    }

    // Method to wait for the customer thread to finish
    public void join() throws InterruptedException {
        if (thread != null) {
            thread.join();
        }
    }


    @Override
    public void run() {
        thread = Thread.currentThread(); // Store the current thread reference
        for (int i = 0; i < ticketToPurchase; i++) {
            if (stopRequested) {
                break; // Exit immediately if stop is requested
            }
            Ticket ticket = ticketPool.removeTicket(); // Attempt to remove a ticket from the pool

            if (ticket != null) {
                // Log the ticket purchase and customer information
                System.out.println("Ticket is - " + ticket + " - Customer name is - " + Thread.currentThread().getName());
                loggerService.sendLog("Ticket is - " + ticket + " - Customer name is - " + Thread.currentThread().getName());
                int availableTickets = ticketPool.availableTickets();// Get the number of available tickets
            } else {
                // Log if no tickets are available
                System.out.println(customerName + " - No tickets available");
                loggerService.sendLog(customerName + " - No tickets available");
                break;
            }
            try {
                // Sleep for the specified retrieval rate to simulate the time taken to purchase a ticket
                Thread.sleep(customerRetrivelRate * 1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}

