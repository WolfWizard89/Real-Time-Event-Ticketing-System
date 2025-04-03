package org.coursework.w2052039.controller;

import org.coursework.w2052039.model.TicketPool;
import org.coursework.w2052039.service.SimulationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/simulation")
public class SimulationController {

    @Autowired
    private SimulationService simulationService;
    private final TicketPool ticketPool;

    // Constructor injection is used here to ensure that the TicketPool dependency is provided when the controller is instantiated.
    @Autowired
    public SimulationController(TicketPool ticketPool) {
        this.ticketPool = ticketPool;
    }

    // Endpoint to start the simulation. The parameters for the simulation are provided in the request body.
    @PostMapping("/start-simulation")
    public ResponseEntity<String> startSimulation(@RequestBody SimulationParams params) {
        simulationService.startSimulation(
                params.getTotalTickets(),
                params.getTicketReleaseRate(),
                params.getMaxTicketCapacity(),
                params.getCustomerRetrievalRate(),
                params.getVendorCount(),
                params.getCustomerCount()
        );
        return ResponseEntity.ok("Simulation Started");
    }

    // Endpoint to get the remaining number of tickets. This calls the availableTickets method in the TicketPool class.
    @GetMapping("/get-remaining-tickets")
    public int getRemainingTickets() {
        return ticketPool.availableTickets(); // Returns the current number of remaining tickets
    }

    // Endpoint to stop the simulation. This calls the stopSimulation method in the SimulationService class.
    @PostMapping("/stop-simulation")
    public ResponseEntity<String> stopSimulation() {
        simulationService.stopSimulation();
        return ResponseEntity.ok("Simulation Stopped");
    }
}


// Class to hold the parameters for the simulation. This is used to deserialize the JSON request body.
class SimulationParams {
    private int totalTickets;
    private int ticketReleaseRate;
    private int maxTicketCapacity;
    private int customerRetrievalRate;
    private int vendorCount;
    private int customerCount;

    // Getters and Setters
    public int getTotalTickets() {
        return totalTickets;
    }

    public void setTotalTickets(int totalTickets) {
        this.totalTickets = totalTickets;
    }

    public int getTicketReleaseRate() {
        return ticketReleaseRate;
    }

    public void setTicketReleaseRate(int ticketReleaseRate) {
        this.ticketReleaseRate = ticketReleaseRate;
    }

    public int getMaxTicketCapacity() {
        return maxTicketCapacity;
    }

    public void setMaxTicketCapacity(int maxTicketCapacity) {
        this.maxTicketCapacity = maxTicketCapacity;
    }

    public int getCustomerRetrievalRate() {
        return customerRetrievalRate;
    }

    public void setCustomerRetrievalRate(int customerRetrievalRate) {
        this.customerRetrievalRate = customerRetrievalRate;
    }

    public int getVendorCount() {
        return vendorCount;
    }

    public void setVendorCount(int vendorCount) {
        this.vendorCount = vendorCount;
    }

    public int getCustomerCount() {
        return customerCount;
    }

    public void setCustomerCount(int customerCount) {
        this.customerCount = customerCount;
    }
}
