package com.ticket.ticketing_app.controller;

import com.ticket.ticketing_app.dto.SimulationRequestDTO;
import com.ticket.ticketing_app.service.TicketSimulationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/simulation")
public class TicketController {

    @Autowired
    private TicketSimulationService simulationService;

    @PostMapping("/start")
    public ResponseEntity<String> startSimulation(@RequestBody SimulationRequestDTO request) {
        simulationService.startSimulation(
            request.getTotalTickets(),
            request.getReleaseRate(),
            request.getRetrievalRate(),
            request.getMaxCapacity()
        );
        return ResponseEntity.ok("Simulation started successfully");
    }

    @PostMapping("/stop")
    public ResponseEntity<String> stopSimulation() {
        simulationService.stopSimulation();
        return ResponseEntity.ok("Simulation stopped successfully");
    }

    @GetMapping("/status")
    public ResponseEntity<TicketSimulationService.SimulationStatus> getStatus() {
        return ResponseEntity.ok(simulationService.getStatus());
    }
}
