package org.example.TicketData;


public class Ticket {
    private int ticketId;
    private String vendorName;
    private double ticketPrice;

    // Constructor to initialize the ticket with necessary details
    public Ticket(int ticketId, String vendorName, double ticketPrice) {
        this.ticketId = ticketId;
        this.vendorName = vendorName;
        this.ticketPrice = ticketPrice;
    }

    public int getTicketId() {
        return ticketId;
    }

    public void setTicketId(int ticketId) {
        this.ticketId = ticketId;
    }

    public String getEventName() {
        return vendorName;
    }

    public void setEventName(String eventName) {
        this.vendorName = eventName;
    }

    public double getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(double ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    // Override toString method to provide a string representation of the ticket
    @Override
    public String toString() {
        return "Ticket{" +
                "ticketId=" + ticketId +
                ", vendorName='" + vendorName + '\'' +
                ", ticketPrice=" + ticketPrice +
                '}';
    }
}


