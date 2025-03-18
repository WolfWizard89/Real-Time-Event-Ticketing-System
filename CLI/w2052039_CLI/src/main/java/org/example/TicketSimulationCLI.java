package org.example;

import java.util.Scanner;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import org.example.Model.Customer;
import org.example.Model.Vendor;
import org.example.TicketData.TicketPool;
import org.example.TicketData.TicketPoolData;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class TicketSimulationCLI {
    private static volatile boolean stopRequested = false;

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        TicketPoolData ticketPoolData;

        // Ask user if they want to load previous data
        String loadPreviousData = getYesOrNoInput(input, "Do you want to load previous data? (y/n): ");

        if (loadPreviousData.equals("y")) {
            // Attempt to load previous data from JSON file
            ticketPoolData = loadTicketPoolDataFromJson();
            if (ticketPoolData == null) {
                // If no valid data is found, switch to manual input mode
                System.out.println("No valid previous data found. Switching to manual input mode.");
                ticketPoolData = getUserInputData(input);
            }
        } else {
            // If user chooses not to load previous data, get data manually
            ticketPoolData = getUserInputData(input);
        }

        // Create ticket pool with data
        TicketPool ticketPool = new TicketPool(ticketPoolData.getMaxTicketCapacity());

        // Save the current data to JSON for future use
        saveTicketPoolDataToJson(ticketPoolData);

        System.out.println("Enter 's' to terminate the simulation");
        // Start a thread to listen for the stop command
        Thread stopListener = new Thread(() -> {
            Scanner stopScanner = new Scanner(System.in);
            while (true) {
                String command = stopScanner.nextLine();
                if (command.equalsIgnoreCase("s")) {
                    terminateSimulation();
                    break;
                }
            }
        });

        stopListener.start();

        // Initialize and start vendor threads
        Vendor[] vendors = new Vendor[ticketPoolData.getVendorCount()];
        for (int i = 0; i < vendors.length; i++) {
            vendors[i] = new Vendor(
                    ticketPoolData.getTotalTickets(),
                    ticketPoolData.getTicketReleaseRate(),
                    ticketPool,
                    "Vendor-" + i
            );
            Thread vendorThread = new Thread(vendors[i], "Vendor ID-" + i);
            vendorThread.start();
        }

        // Initialize and start customer threads
        Customer[] customers = new Customer[ticketPoolData.getCustomerCount()];
        for (int i = 0; i < customers.length; i++) {
            customers[i] = new Customer(
                    ticketPool,
                    ticketPoolData.getCustomerRetrievalRate(),
                    ticketPoolData.getTotalTickets(),
                    "Customer-" + i
            );
            Thread customerThread = new Thread(customers[i], "Customer ID-" + i);
            customerThread.start();
        }
    }

    // Method to terminate the simulation
    public static void terminateSimulation() {
        stopRequested = true;
        System.out.println("Stop command received. Terminating program...");
        System.exit(0);
    }

    // Method to get user input data
    public static TicketPoolData getUserInputData(Scanner input) {
        int totalTickets = getIntInput(input, "Enter Number of Tickets: ");
        int ticketReleaseRate = getIntInput(input, "Enter Release rate of tickets: ");
        int maxTicketCapacity = getIntInput(input, "Maximum Ticket Capacity: ");
        int customerRetrievalRate = getIntInput(input, "Enter Customer Retrieval Rate: ");
        int vendorCount = getIntInput(input, "Enter Number of Vendors: ");
        int customerCount = getIntInput(input, "Enter Number of Customers: ");

        return new TicketPoolData(totalTickets, ticketReleaseRate, maxTicketCapacity, customerRetrievalRate, vendorCount, customerCount);
    }

    // Method to get integer input from user with validation
    public static int getIntInput(Scanner input, String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                int value = input.nextInt();
                if (value < 0) {
                    System.out.println("Invalid Input. Please enter a positive number.");
                } else {
                    return value;
                }
            } catch (Exception e) {
                System.out.println("Invalid Input. Please enter a number.");
                input.next();
            }
        }
    }

    // Method to get yes or no input from user with validation
    public static String getYesOrNoInput(Scanner input, String prompt) {
        while (true) {
            System.out.print(prompt);
            String response = input.nextLine().trim().toLowerCase();
            if (response.equals("y") || response.equals("n")) {
                return response;
            } else {
                System.out.println("Invalid Input. Please enter 'y' or 'n'.");
            }
        }
    }

    // Method to save ticket pool data to JSON file
    public static void saveTicketPoolDataToJson(TicketPoolData ticketPoolData) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter("ticket_pool_data.json")) {
            gson.toJson(ticketPoolData, writer);
            System.out.println("Data saved to ticket_pool_data.json");
        } catch (IOException e) {
            System.out.println("Error saving data to file: " + e.getMessage());
        }
    }

    // Method to load ticket pool data from JSON file
    public static TicketPoolData loadTicketPoolDataFromJson() {
        Gson gson = new Gson();
        try (FileReader reader = new FileReader("ticket_pool_data.json")) {
            return gson.fromJson(reader, TicketPoolData.class);
        } catch (IOException | JsonSyntaxException e) {
            System.out.println("Error loading data from file: " + e.getMessage());
            return null;
        }
    }
}