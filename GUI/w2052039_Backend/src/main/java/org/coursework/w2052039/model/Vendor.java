package org.coursework.w2052039.model;


import java.util.concurrent.atomic.AtomicBoolean;

public class Vendor implements Runnable {
    private int totalTickets;
    private String vendorName;
    private int ticketReleaseRate;
    private TicketPool ticketPool;
    private volatile boolean stopRequested = false;
    private Thread thread;

    // Constructor to initialize the Vendor with necessary parameters and dependencies
    public Vendor(int totalTickets, int ticketReleaseRate, TicketPool ticketPool, String vendorName) {
        this.totalTickets = totalTickets;
        this.ticketReleaseRate = ticketReleaseRate;
        this.ticketPool = ticketPool;
        this.vendorName = vendorName;

    }

    // Method to request the vendor thread to stop
    public void stopWork() {
        stopRequested = true;
        if (thread != null) {
            thread.interrupt();
        }
    }

    // Method to wait for the vendor thread to finish
    public void join() throws InterruptedException {
        if (thread != null) {
            thread.join();
        }
    }

    @Override
    public void run() {
        thread = Thread.currentThread(); // Store the current thread reference
        for (int i = 1; i < totalTickets; i++) {
            if (stopRequested) {
                break; // Exit immediately if stop is requested
            }
            // Create a new ticket and add it to the ticket pool
            Ticket ticket = new Ticket(i, vendorName, 1000);
            ticketPool.addTicket(ticket);
            int availableTickets = ticketPool.availableTickets(); // Get the number of available tickets
        }
        try {
            // Sleep for the specified release rate to simulate the time taken to release a ticket
            Thread.sleep(ticketReleaseRate * 1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restore the interrupted status
        }
    }
}

