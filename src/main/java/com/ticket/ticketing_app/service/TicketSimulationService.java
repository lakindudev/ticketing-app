package com.ticket.ticketing_app.service;

import org.springframework.stereotype.Service;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class TicketSimulationService {
    private BlockingQueue<Integer> ticketQueue;
    private AtomicInteger availableTickets;
    private volatile boolean isSimulationRunning;
    
    private Thread releaseThread;
    private Thread retrievalThread;

    public void startSimulation(int totalTickets, int releaseRate, int retrievalRate, int maxCapacity) {
        // Initialize simulation
        ticketQueue = new LinkedBlockingQueue<>(maxCapacity);
        availableTickets = new AtomicInteger(totalTickets);
        isSimulationRunning = true;

        // Create and start release thread
        releaseThread = new Thread(() -> {
            try {
                while (isSimulationRunning && availableTickets.get() > 0) {
                    for (int i = 0; i < releaseRate && availableTickets.get() > 0; i++) {
                        ticketQueue.put(1); // Adding ticket to queue
                        availableTickets.decrementAndGet();
                        System.out.println("Released ticket. Remaining: " + availableTickets.get());
                    }
                    Thread.sleep(1000); // Wait for 1 second
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        // Create and start retrieval thread
        retrievalThread = new Thread(() -> {
            try {
                while (isSimulationRunning && (availableTickets.get() > 0 || !ticketQueue.isEmpty())) {
                    for (int i = 0; i < retrievalRate && !ticketQueue.isEmpty(); i++) {
                        ticketQueue.take(); // Remove ticket from queue
                        System.out.println("Retrieved ticket. Queue size: " + ticketQueue.size());
                    }
                    Thread.sleep(1000); // Wait for 1 second
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        releaseThread.start();
        retrievalThread.start();
    }

    public void stopSimulation() {
        isSimulationRunning = false;
        if (releaseThread != null) {
            releaseThread.interrupt();
        }
        if (retrievalThread != null) {
            retrievalThread.interrupt();
        }
    }

    public SimulationStatus getStatus() {
        return new SimulationStatus(
            availableTickets.get(),
            ticketQueue != null ? ticketQueue.size() : 0,
            isSimulationRunning
        );
    }

    public static class SimulationStatus {
        private final int remainingTickets;
        private final int queueSize;
        private final boolean isRunning;

        public SimulationStatus(int remainingTickets, int queueSize, boolean isRunning) {
            this.remainingTickets = remainingTickets;
            this.queueSize = queueSize;
            this.isRunning = isRunning;
        }

        // Getters
        public int getRemainingTickets() {
            return remainingTickets;
        }

        public int getQueueSize() {
            return queueSize;
        }

        public boolean isRunning() {
            return isRunning;
        }
    }
} 