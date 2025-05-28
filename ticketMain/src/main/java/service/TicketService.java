package service;
import model.Ticket;
import dataStructure.PriorityQueue;
import utils.IDGenerator;

/**
 * Service class that provides business lgoic for the IT support ticket system.
 * This class acts as an interface between the user Interface and the data structure.
 * It handles all ticket operations and maintains system state.
 *
 * @author Rosslan Koulli
 * @version 1.0
 */
public class TicketService {
    // The priority queue to store tickets
    private PriorityQueue ticketQueue;

    // ID generator for unique ticket IDs
    private IDGenerator idGenerator;

    // Statistics tracking
    private int totalTicketsCreated;
    private int totalTicketsResolved;

    /**
     * Constructor for initializing the service
     */

    public TicketService() {
        this.ticketQueue = new PriorityQueue();
        this.idGenerator = IDGenerator.getInstance();
        this.totalTicketsCreated = 0;
        this.totalTicketsResolved = 0;

    }

    /**
     * Creates a new ticket in the system
     *
     * @param creator Name of the person creating the ticket
     * @param requestType Type of IT request (1 to 4).
     * @param description Detailed description of the issue
     * @return The created ticket
     */
    public Ticket createTicket(String creator, int requestType, String description) {
        // Validate inputs
        if(creator == null || creator.trim().isEmpty()){
            throw new IllegalArgumentException("Creator cannot be null or empty");
        }
        if(description == null || description.trim().isEmpty()){
            throw new IllegalArgumentException("Description cannot be null or empty");
        }

        // Genrate unique ID
        int ticketId = idGenerator.generateId();

        // Determine request type string and priority

        String typeString;
        int priority;

        switch (requestType) {
            case 1:
                typeString = "Security Issue";
                priority = 1;
                break;
            case 2:
                typeString = "Network Issue";
                priority = 2;
                break;
            case 3:
                typeString = "Software/app Installation";
                priority = 3;
                break;
            case 4:
                typeString = "New Computer configuration";
                priority = 4;
                break;
            default:
                throw new IllegalArgumentException("Invalid request type. Must be between 1 or 4.");
        }
        // Create ticket
        Ticket ticket = new Ticket(ticketId, creator, typeString, description, priority);

        // Add to queue

        ticketQueue.insert(ticket);
        totalTicketsCreated++;

        System.out.println("Ticket has been created successfully. ID of the ticket is: " + ticketId);

        return ticket;
    }
    /**
     * Processes the next hgihest priority ticket
     *
     * @return the processed ticketm or null if queue is empty
     */

    public Ticket processNextTicket() {
        Ticket ticket = ticketQueue.extractMin();

        if(ticket != null) {
            ticket.setStatus(Ticket.TicketStatus.IN_PROGRESS);
            totalTicketsResolved++;
            System.out.println("Processing ticket # " + ticket.getTicketID());
        } else {
            System.out.println(" No tickets currently in the queue");
        }

        return ticket;
    }
    /**
     *  Searching for a ticket by its ID
     *
     * @param ticketID the ID to search for
     * @return the ticket if found, null otherwise
     */
    public Ticket searchTicket(int ticketID) {
        Ticket ticket = ticketQueue.search(ticketID);

        if(ticket != null) {
            System.out.println(" Ticket Found !");
        } else {
            System.out.println(" Ticket not found");
        }

        return ticket;
    }
    /**
     * Updates the priority of a ticket
     *
     * @param ticketId ID of the ticket to update
     * @param newPriority New priority between 1 or 4
     * @return true if successful. false if ticket not found
     */
    public boolean updateTicketPriority(int ticketId, int newPriority) {
        boolean success = ticketQueue.updatePriority(ticketId, newPriority);

        if (success) {
            System.out.println("Priority updated successfully");
        } else {
            System.out.println("Ticket not found");
        }
        return success;
    }

    /**
     * Removes a specific ticket from the system
     *
     * @param ticketId ID of the ticket to remove
     * @return the removed ticket, or null if not found
     */

    public Ticket removeTicket(int ticketId) {
        Ticket removed = ticketQueue.remove(ticketId);

        if(removed != null) {
            removed.setStatus(Ticket.TicketStatus.CLOSED);
            System.out.println("Ticket #" + ticketId + " has been removed successfully");
        } else {
            System.out.println("Ticket #" + ticketId + " not found");
        }

        return removed;
    }

    /**
     * Assigns an owner to a ticket
     *
     * @param ticketId ID of the ticket
     * @param owner Name of the owner to assign
     * @return true if successful, flase if ticket not found
     *
     */
    public boolean assignOwner(int ticketId, String owner) {
        Ticket ticket = ticketQueue.search(ticketId);

        if(ticket != null) {
            ticket.setOwner(owner);
            ticket.setStatus(Ticket.TicketStatus.IN_PROGRESS);
            System.out.println("Owner assigned successfully \nTicket #" + ticketId + " has been assigned to " + owner);
            return true;
        } else {
            System.out.println("Ticket #" + ticketId + " not found");
            return false;
        }
    }

    /**
     * Gets all tickets in the system
     *
     * @return Array of all the tickets (they will be unsorted)
     */
    public Ticket[] getAllTickets() {
        return ticketQueue.getAllTickets();
    }

    /**
     * Get all tickets sorted by priority
     *
     * @return Array of tickets sorted by priority
     */

    public Ticket[] getAllTicketsSorted() {
        return ticketQueue.getAllTicketsSorted();
    }

    /**
     * Displays all tickets in priority order
     */
    public void displayAllTickets() {
        if(ticketQueue.isEmpty()) {
            System.out.println("No tickets in the system");
            return;
        }

        Ticket[] sorted = getAllTicketsSorted();
        System.out.println("\n=== All Tickets (Sorted by priority===");
        System.out.println("Total tickets: " + sorted.length );
        System.out.println("-------------------------------------------------------");

        for(Ticket ticket : sorted) {
            System.out.println(ticket.toSummaryString());
        }
    }
    /**
     * Displays detailed information about all tickets
     *
     */

    public void displayAllTicketsDetailed(){
        if(ticketQueue.isEmpty()) {
            System.out.println("No tickets in the system");
            return;
        }

        Ticket[] sorted = getAllTicketsSorted();
        System.out.println("\n=== All Tickets ===");

        for (Ticket ticket : sorted) {
            System.out.println("\n" + ticket);
            System.out.println("-------------------------------------------------------");
        }
    }

    /**
     * Gets the next ticket wihtout removing it
     *
     * @return The highest priority ticket, or null if empty
     */

    public Ticket peekNextTicket() {
        return ticketQueue.peek();
    }

    /**
     * Gets the number of tickets in the system
     *
     * @return Current number of tickets
     */

    public int getTicketCount() {
        return ticketQueue.size();
    }

    /**
     * Displays system statistics
     */
    public void displayStatistics() {
        System.out.println("\n=== System Statistics ===");
        System.out.println("Total tickets created: " + totalTicketsCreated);
        System.out.println("Total tickets resolved: " + totalTicketsResolved);
        System.out.println("Current tickets in queue: " + ticketQueue.size());

        // Count by priority
        Ticket[] allTickets = getAllTickets();
        int[] priorityCount = new int[4];

        for(Ticket ticket : allTickets) {
            priorityCount[ticket.getPriority()-1]++;
        }

        System.out.println("\nTickets by Priority:");
        System.out.println("Priority 1 (Security): " + priorityCount[0]);
        System.out.println("Priority 2 (Network): " + priorityCount[1]);
        System.out.println("Priority 3 (Software): " + priorityCount[2]);
        System.out.println("Priority 4 (New Computer): " + priorityCount[3]);
    }
    /**
     * Clears all tickets from the system
     * WARNING this action cannot be undone WARNING
     */

    public void clearAllTickets() {
        ticketQueue.clear();
        System.out.println("All tickets succesfully cleared from the system");
    }

    /**
     * Validates if the srvice is ready
     *
     * @return true if the service is operational
     */

    public boolean isReady() {
        return ticketQueue != null && idGenerator != null;
    }
}

