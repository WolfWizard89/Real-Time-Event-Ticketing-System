package org.coursework.w2052039.model;


public class Ticket {
    private int ticketId;
    private String vendorName;
    private double ticketPrice;

    public Ticket(int ticketId, String vendorName, double ticketPrice) {
        this.ticketId = ticketId;
        this.vendorName = vendorName;
        this.ticketPrice = ticketPrice;
    }


    @Override
    public String toString() {
        return "Ticket{" +
                "ticketId=" + ticketId +
                ", vendorName='" + vendorName + '\'' +
                ", ticketPrice=" + ticketPrice +
                '}';
    }
}
