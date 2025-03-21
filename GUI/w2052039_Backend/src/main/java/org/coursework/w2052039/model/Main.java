package org.coursework.w2052039.model;

import java.util.Scanner;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileWriter;
import java.io.IOException;

import org.coursework.w2052039.webConfig.LoggerService;
import org.coursework.w2052039.webConfig.RemainTicketService;
import org.springframework.stereotype.Component;

@Component
public class Main {
    private static volatile boolean stopRequested = false;
    private static LoggerService loggerService = new LoggerService();
    private static RemainTicketService remainTicketService = new RemainTicketService();

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        int totalTickets = 0, ticketReleaseRate = 0, maxTicketCapacity = 0, customerRetrievalRate = 0, vendorCount = 0, customerCount = 0;

        // User input for ticket pool settings
        while (true) {
            try {
                System.out.print("Enter Number of Tickets: ");
                totalTickets = input.nextInt();
                if (totalTickets < 0) {
                    System.out.println("Invalid Input. Please enter a positive number.");
                } else {
                    break;
                }
            } catch (Exception e) {
                System.out.println("Invalid Input. Please enter a number.");
                input.next();
            }
        }

        while (true) {
            try {
                System.out.print("Enter Release rate of tickets: ");
                ticketReleaseRate = input.nextInt();
                if (ticketReleaseRate < 0) {
                    System.out.println("Invalid Input. Please enter a positive number.");
                } else {
                    break;
                }
            } catch (Exception e) {
                System.out.println("Invalid Input. Please enter a number.");
                input.next();
            }
        }

        while (true) {
            try {
                System.out.print("Maximum Ticket Capacity: ");
                maxTicketCapacity = input.nextInt();
                if (maxTicketCapacity < 0) {
                    System.out.println("Invalid Input. Please enter a positive number.");
                } else {
                    break;
                }
            } catch (Exception e) {
                System.out.println("Invalid Input. Please enter a number.");
                input.next();
            }
        }

        while (true) {
            try {
                System.out.print("Enter Customer Retrieval Rate: ");
                customerRetrievalRate = input.nextInt();
                if (customerRetrievalRate < 0) {
                    System.out.println("Invalid Input. Please enter a positive number.");
                } else {
                    break;
                }
            } catch (Exception e) {
                System.out.println("Invalid Input. Please enter a number.");
                input.next();
            }
        }

        // Write user input to file
        TicketPool ticketPool = new TicketPool(maxTicketCapacity, loggerService, remainTicketService);

        // Configure vendor and customer parameters
        while (true) {
            try {
                System.out.print("Enter Number of Vendors: ");
                vendorCount = input.nextInt();
                if (vendorCount < 0) {
                    System.out.println("Invalid Input. Please enter a positive number.");
                } else {
                    break;
                }
            } catch (Exception e) {
                System.out.println("Invalid Input. Please enter a number.");
                input.next();
            }
        }

        while (true) {
            try {
                System.out.print("Enter Number of Customers: ");
                customerCount = input.nextInt();
                if (customerCount < 0) {
                    System.out.println("Invalid Input. Please enter a positive number.");
                } else {
                    break;
                }
            } catch (Exception e) {
                System.out.println("Invalid Input. Please enter a number.");
                input.next();
            }
        }

        TicketPoolData ticketPoolData = new TicketPoolData(totalTickets, ticketReleaseRate, maxTicketCapacity, customerRetrievalRate, vendorCount, customerCount);
        saveTicketPoolDataToJson(ticketPoolData);

        System.out.println("Enter 's' to terminate the simulation");
        Thread stopListener = new Thread(() -> {
            Scanner stopScanner = new Scanner(System.in);
            while (true) {
                String command = stopScanner.nextLine();
                if (command.equalsIgnoreCase("s")) {
                    terminateSimulation();  // Call terminateSimulation when "stop" is typed
                    break;
                }
            }
        });

        stopListener.start();

        Vendor[] vendors = new Vendor[vendorCount];
        for (int i = 0; i < vendors.length; i++) {
            vendors[i] = new Vendor(totalTickets, ticketReleaseRate, ticketPool, "Vendor-" + i);
            Thread vendorThread = new Thread(vendors[i], "Vendor ID-" + i);
            vendorThread.start();
        }

        Customer[] customers = new Customer[customerCount];
        for (int i = 0; i < customers.length; i++) {
            customers[i] = new Customer(ticketPool, customerRetrievalRate, totalTickets, "Customer-" + i, loggerService); // Retrieve tickets from the pool
            Thread customerThread = new Thread(customers[i], "Customer ID-" + i);
            customerThread.start();
        }
    }

    public static void terminateSimulation() {
        stopRequested = true;
        System.out.println("Stop command received. Terminating program...");

        // Safely stop all threads
        System.exit(0);  // This will terminate the program safely
    }

    public static void saveTicketPoolDataToJson(TicketPoolData ticketPoolData) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();  // Pretty printing for better readability
        try (FileWriter writer = new FileWriter("ticket_pool_data.json")) {
            gson.toJson(ticketPoolData, writer);  // Serialize to file
            System.out.println("Data saved to ticket_pool_data.json");
        } catch (IOException e) {
            System.out.println("Error saving data to file: " + e.getMessage());
        }
    }
}