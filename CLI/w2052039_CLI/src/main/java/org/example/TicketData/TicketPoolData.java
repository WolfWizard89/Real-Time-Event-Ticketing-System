package org.example.TicketData;

public class TicketPoolData {
    private int totalTickets;
    private int ticketReleaseRate;
    private int maxTicketCapacity;
    private int customerRetrievalRate;
    private int vendorCount;
    private int customerCount;

    // Constructor
    public TicketPoolData(int totalTickets, int ticketReleaseRate, int maxTicketCapacity,
                          int customerRetrievalRate, int vendorCount, int customerCount) {
        this.totalTickets = totalTickets;
        this.ticketReleaseRate = ticketReleaseRate;
        this.maxTicketCapacity = maxTicketCapacity;
        this.customerRetrievalRate = customerRetrievalRate;
        this.vendorCount = vendorCount;
        this.customerCount = customerCount;
    }

    // Getters and Setters
    public int getTotalTickets() { return totalTickets; }
    public void setTotalTickets(int totalTickets) { this.totalTickets = totalTickets; }

    public int getTicketReleaseRate() { return ticketReleaseRate; }
    public void setTicketReleaseRate(int ticketReleaseRate) { this.ticketReleaseRate = ticketReleaseRate; }

    public int getMaxTicketCapacity() { return maxTicketCapacity; }
    public void setMaxTicketCapacity(int maxTicketCapacity) { this.maxTicketCapacity = maxTicketCapacity; }

    public int getCustomerRetrievalRate() { return customerRetrievalRate; }
    public void setCustomerRetrievalRate(int customerRetrievalRate) { this.customerRetrievalRate = customerRetrievalRate; }

    public int getVendorCount() { return vendorCount; }
    public void setVendorCount(int vendorCount) { this.vendorCount = vendorCount; }

    public int getCustomerCount() { return customerCount; }
    public void setCustomerCount(int customerCount) { this.customerCount = customerCount; }
}
