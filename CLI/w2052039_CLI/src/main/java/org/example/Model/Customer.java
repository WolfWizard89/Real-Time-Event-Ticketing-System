package org.example.Model;

import org.example.TicketData.Ticket;
import org.example.TicketData.TicketPool;

public class Customer implements Runnable {
    private TicketPool ticketPool;
    private int customerRetrivelRate;
    private int ticketToPurchase;
    private String customerName;

    // Constructor to initialize the customer with necessary details
    public Customer(TicketPool ticketPool, int customerRetrivelRate, int ticketToPurchase, String customerName) {
        this.ticketPool = ticketPool;
        this.customerRetrivelRate = customerRetrivelRate;
        this.ticketToPurchase = ticketToPurchase;
        this.customerName = customerName;
    }

    @Override
    public void run() {
        // Loop to purchase tickets from the pool
        for (int i = 0; i < ticketToPurchase; i++) {
            // Attempt to remove a ticket from the pool
            Ticket ticket = ticketPool.removeTicket();
            if (ticket != null) {
                // Print ticket details if available
                System.out.println("Ticket is - " + ticket + " - Customer name is - " + Thread.currentThread().getName());
            } else {
                // Print message if no tickets are available and break the loop
                System.out.println(customerName + " - No tickets available");
                break;
            }
            try {

                Thread.sleep(customerRetrivelRate * 1000);
            } catch (InterruptedException e) {
                // Handle interruption during sleep
                Thread.currentThread().interrupt();
            }
        }
    }


}

