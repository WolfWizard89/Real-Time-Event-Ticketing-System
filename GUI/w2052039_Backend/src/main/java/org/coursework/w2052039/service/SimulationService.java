package org.coursework.w2052039.service;

import org.coursework.w2052039.webConfig.LoggerService;
import org.coursework.w2052039.webConfig.RemainTicketService;
import org.coursework.w2052039.webConfig.RemainWebSocketHandler;
import org.coursework.w2052039.model.TicketPoolData;
import org.coursework.w2052039.model.TicketPool;
import org.coursework.w2052039.model.Vendor;
import org.coursework.w2052039.model.Customer;
import org.coursework.w2052039.webConfig.LogWebSocketHandler;


import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SimulationService {
    private boolean stopRequested = false;
    private boolean firstStopIgnored = false;
    private Vendor[] vendors;
    private Customer[] customers;

    @Autowired
    private LogWebSocketHandler logWebSocketHandler;
    @Autowired
    private LoggerService loggerService;
    @Autowired
    private RemainTicketService remainTicketService;
    @Autowired
    private RemainWebSocketHandler remainWebSocketHandler;

    // Method to start the simulation with the given parameters
    public void startSimulation(int totalTickets, int ticketReleaseRate, int maxTicketCapacity, int customerRetrievalRate,
                                int vendorCount, int customerCount) {
        // Log the start of the simulation with the provided parameters
        logWebSocketHandler.sendLog("Starting simulation with params: " + totalTickets + ", " + ticketReleaseRate + ", " + maxTicketCapacity + ", " + customerRetrievalRate + ", " + vendorCount + ", " + customerCount);
        remainWebSocketHandler.sendLog(String.valueOf(totalTickets));
        // Initialize the TicketPool with the given capacity and services
        TicketPool ticketPool = new TicketPool(maxTicketCapacity, loggerService,remainTicketService);

        // Save the initial state of the ticket pool to a JSON file
        TicketPoolData ticketPoolData = new TicketPoolData(totalTickets, ticketReleaseRate, maxTicketCapacity, customerRetrievalRate, vendorCount, customerCount);
        saveTicketPoolDataToJson(ticketPoolData);

        // Create and start vendor threads
        this.vendors = new Vendor[vendorCount];
        for (int i = 0; i < vendors.length; i++) {
            vendors[i] = new Vendor(totalTickets, ticketReleaseRate, ticketPool, "Vendor-" + i);
            Thread vendorThread = new Thread(vendors[i], "Vendor-" + i);
            vendorThread.start();
        }

        // Create and start customer threads
        this.customers = new Customer[customerCount];
        for (int i = 0; i < customers.length; i++) {
            customers[i] = new Customer(ticketPool, customerRetrievalRate, totalTickets, "Customer-" + i, loggerService);
            Thread customerThread = new Thread(customers[i], "Customer-" + i);
            customerThread.start();
        }

        // Listen for "stop" command from the console
        Scanner scanner = new Scanner(System.in);
        while (!stopRequested) {
            String command = scanner.nextLine();
            if ("stop".equalsIgnoreCase(command)) {
                stopSimulation();
                break;
            }
        }
    }

    // Method to stop the simulation
    public void stopSimulation() {
        // Ignore the first stop request to allow for a confirmation mechanism
        if (!firstStopIgnored) {
            firstStopIgnored = true;
            return;
        }
        stopRequested = true;
        

        // Signal all Vendors and Customers to stop their work
        if (vendors != null) {
            for (Vendor vendor : vendors) {
                if (vendor != null) {
                    vendor.stopWork();
                }
            }
        }
        if (customers != null) {
            for (Customer customer : customers) {
                if (customer != null) {
                    customer.stopWork();
                }
            }
        }

        // Wait for all Vendor threads to finish
        if (vendors != null) {
            for (Vendor vendor : vendors) {
                try {
                    if (vendor != null) {
                        vendor.join();
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    logWebSocketHandler.sendLog("Error stopping vendor thread: " + e.getMessage());
                }
            }
        }

        // Wait for all Customer threads to finish
        if (customers != null) {
            for (Customer customer : customers) {
                try {
                    if (customer != null) {
                        customer.join();
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    logWebSocketHandler.sendLog("Error stopping customer thread: " + e.getMessage());
                }
            }
        }


    }

    // Method to save the initial state of the ticket pool to a JSON file
    public void saveTicketPoolDataToJson(TicketPoolData ticketPoolData) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();  // Pretty printing for better readability
        try (FileWriter writer = new FileWriter("ticket_pool_data.json")) {
            gson.toJson(ticketPoolData, writer);  // Serialize to file
            logWebSocketHandler.sendLog("Data saved to ticket_pool_data.json");
        } catch (IOException e) {
            logWebSocketHandler.sendLog("Error saving data to file: " + e.getMessage());
        }
    }
}