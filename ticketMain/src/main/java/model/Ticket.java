package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Ticket class represents an IT support request in the ticketing system,
 * Each ticket has unique ID, creator, priority, and other relevant information
 *
 * Priority levels:
 * 1: Security issues (highest priority)
 * 2: Network issue
 * 3: Software/app installation
 * 4: New Computer configuration (lowest Priority)
 *
 * @author Rosslan Koulli
 * @version 1.0
 *
 */

public class Ticket {
    // Usage identifier for each ticket
    private final int ticketID;

    //Person who creat the ticket
    private final String creator;

    // Person responsible for solving the issue (OPTIONAL: can be assigned later on)
    private String owner;

    // Type of request
    private final String requestType;

    // Detained description of the issue
    private final String description;

    // Priority level(1-4, where 1 is highest)
    private int priority;

    // Timestamp when ticket was created
    private final LocalDateTime createdAt;

    // Timestamp when ticket was last updated
    private LocalDateTime updatedAt;

    // Statues of the ticket
    private TicketStatus status;

    /**
     * Enum representing possible ticket statuses
     */

    public enum TicketStatus {
        OPEN,
        IN_PROGRESS,
        RESOLVED,
        CLOSED
    }

    /**
     * Constructor for creating a new ticket
     *
     * @param ticketID Unique identifier
     * @param creator Name of the person creating the ticket
     * @param requestType Type of IT request
     * @param description Detailed description of the issue
     * @param priority Priority level (1 to 4)
     */
    public Ticket(int ticketID, String creator, String requestType,
                  String description, int priority) {
        // Validate inputs
        if(creator == null || creator.trim().isEmpty()){
            throw new IllegalArgumentException("Creator cannot be null or empty");
        }
        if(priority < 1 || priority >4) {
            throw new IllegalArgumentException("Ticket priority must be between 1 and 4");
        }

        this.ticketID = ticketID;
        this.creator = creator;
        this.requestType = requestType;
        this.description = description;
        this.priority = priority;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.status = TicketStatus.OPEN;
        this.owner = null; // Initially unassigned
    }

    // Getter methods
    public int getTicketID() {
        return ticketID;
    }

    public String getCreator() {
        return creator;
    }

    public String getOwner() {
        return owner;
    }

    public String getRequestType() {
        return requestType;
    }

    public String getDescription() {
        return description;
    }

    public int getPriority() {
        return priority;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    public TicketStatus getStatus() {
        return status;
    }

    //Setter methods (only for mutable fileds)

    /**
     * Assigns an owner to the ticket
     * @param owner Name of the person responsible for solving the issue
     */

    public void setOwner(String owner ) {
        this.owner = owner;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Updates the priority of the ticket
     * @param priority New priority level(1 to 4)
     */

    public void setPriority(int priority) {
        if(priority < 1 || priority >4) {
            throw new IllegalArgumentException("Ticket priority must be between 1 and 4");
        }
        this.priority = priority;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Updates the status of the ticket
     * @param status New status
     */
    public void setStatus(TicketStatus status) {
        this.status = status;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Returns a string representation of the priority level
     * @return Priority as descriptive string
     */

    public String getPriorityDescription() {
        switch (priority) {
            case 1:
                return "Security Issue (Highest)";
            case 2:
                return "Network Issue";
            case 3:
                return "Software/app Installation";
            case 4:
                return "New Computer configuration";
            default:
                return "Unknown";
        }
    }

    /**
     * Provides a formatted string representation of ticket
     */
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

        return String.format(
                "=== Ticket #%d ===\n" +
                        "Creator: %s\n" +
                        "Owner: %s\n" +
                        "Type: %s\n" +
                        "Priority: %d - %s\n" +
                        "Status: %s\n" +
                        "Description: %s\n" +
                        "Created: %s\n" +
                        "Updated: %s",
                ticketID,
                creator,
                owner != null ? owner : "Unassigned",
                requestType,
                priority,
                getPriorityDescription(),
                status,
                description,
                createdAt.format(formatter),
                updatedAt.format(formatter)
        );

    }
    /**
     * Provides a brief summary of the ticket (single line)
     * @return Brief ticket summary
     */
    public String toSummaryString() {
        return String.format("Ticket #%d | Priority %d | %s | %s | Status: %s",
                                ticketID, priority, requestType, creator, status);
    }
}
