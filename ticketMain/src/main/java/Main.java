import service.TicketService;
import model.Ticket;
import java.util.Scanner;

/**
 * Main class for the IT ticketing System.
 * Provides a terminal based user interface for interacting with the system;
 *
 * This system manages IT support requests with different priority levels:
 * 1. Security issues (highest priority)
 * 2. Network issues
 * 3. Software/app installation
 * 4. New computer configuration (lowest priority)
 *
 * @author Rosslan Koulli
 * @version 1.0
 */
public class Main {
    // Srvice instance for handling ticket operations
    private static TicketService ticketService;

    // Scanner for user input
    private static Scanner scanner;

    /**
     * Main method: Enthry point of the application
     */

    public static void main(String[] args) {
        // Initiliazise components
        ticketService = new TicketService();
        scanner = new Scanner(System.in);

        // Display a welcome message
        displayWelcome ();

        // Mian application looop
        boolean running = true;
        while(running) {
            displayMenu();
            int choice = getMenuChoice();

            switch (choice) {
                case 1:
                    createNewTicket();
                    break;
                case 2:
                    processNextTicket();
                    break;
                case 3:
                    searchForTicket();
                    break;
                case 4:
                    updateTicketPriority();
                    break;
                case 5:
                    removeTicket();
                    break;
                case 6:
                    assignOwner();
                    break;
                case 7:
                    displayAllTickets();
                    break;
                case 8:
                    displayAllTicketsDetailed();
                    break;
                case 9:
                    displayStatistics();
                    break;
                case 10:
                    peekNextTicket();
                    break;
                case 0:
                    running = false;
                    displayGoodbye();
            }

            if(running) {
                pauseForUser();
            }
        }
        // Clean up
        scanner.close();
    }

    /**
     * Displays welcome message
     *
     */
    private static void displayWelcome() {
        System.out.println("==============================================");
        System.out.println("|        IT ticketing System - Welcome!      |");
        System.out.println("|                                            |");
        System.out.println("|  Manage IT support requests                |");
        System.out.println("|  with priority-based queue system          |");
        System.out.println("===============================================");
        System.out.println();
    }

    /**
     * Displays the main menu
     */

    private static void displayMenu() {
        System.out.println("\n===============MAIN MENU===================");
        System.out.println("| 1.  Create new ticket                     |");
        System.out.println("| 2.  Process next ticket (highest priority)|");
        System.out.println("| 3.  Search for ticket                     |");
        System.out.println("| 4.  Update ticket priority                |");
        System.out.println("| 5.  Remove ticket                         |");
        System.out.println("| 6.  Assign owner to ticket                |");
        System.out.println("| 7.  Display all tickets (summary)         |");
        System.out.println("| 8.  Display all tickets (detailed)        |");
        System.out.println("| 9.  Display statistics                    |");
        System.out.println("| 10. Peek next ticket                      |");
        System.out.println("| 0.  Exit                                  |");
        System.out.println("=============================================");
        System.out.print("Enter your choice: ");
    }
    /**
     * Get users menu choice
     */
    private static int getMenuChoice() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch(NumberFormatException e) {
            return -1; // Invalid choice
        }
    }

    /**
     * Creates a new ticket
     */

    private static void createNewTicket() {
        System.out.println("\n===Create NEW TICKET===");

        // Get creator name
        System.out.print("Enter your name: ");
        String creator = scanner.nextLine().trim();
        if(creator.isEmpty()) {
            System.out.println("Creator name cannot be empty");
            return ;
        }

        // Display request tpye
        System.out.println("\nSelect request type:");
        System.out.println("1. Security Issue (Priority 1 - Highest)");
        System.out.println("2. Network Issue (Priority 2)");
        System.out.println("3. Software/App Installation (Priority 3)");
        System.out.println("4. New Computer Configuration (Priority 4 - Lowest)");
        System.out.print("Enter choice (1-4): ");

        int requestType;
        try {
            requestType = Integer.parseInt(scanner.nextLine());
            if(requestType < 1 || requestType > 4) {
                System.out.println("Invalid request type. Must be between 1 and 4");
                return;
            }
        } catch(NumberFormatException e) {
            System.out.println("Invalid request type. Must be between 1 and 4");
            return;
        }

        // Get Descritpion
        System.out.print("Enter problem ticket description: ");
        String description = scanner.nextLine().trim();
        if(description.isEmpty()) {
            System.out.println("Ticket description cannot be empty");
            return;
        }

        // Create ticket
        try {
            Ticket ticket = ticketService.createTicket(creator, requestType, description);
            System.out.println("\n" + ticket.toSummaryString());
        } catch(Exception e) {
            System.out.println("Error creating ticket: " + e.getMessage());
        }
    }

    /**
     * Processes the next hgihest priority ticket
     */
    private static void processNextTicket() {
        System.out.println("\n===Process NEXT TICKET===");

        Ticket ticket = ticketService.processNextTicket();
        if(ticket != null) {
            System.out.println("\nProcessing ticket:");
            System.out.println(ticket);

            // Simulate processing
            System.out.print("\nMark as resolved? (y/n): ");
            String response = scanner.nextLine().toLowerCase().trim();
            if(response.equals("y")) {
                ticket.setStatus(Ticket.TicketStatus.RESOLVED);
                System.out.println("Ticket resolved successfully");
            }
        }
    }
    /**
     * Searches for a ticket by ID
     */

    private static void searchForTicket() {
        System.out.println("\n===Search FOR TICKET===");

        System.out.print("Enter ticket ID: ");
        try {
            int ticketId = Integer.parseInt(scanner.nextLine());
            Ticket ticket = ticketService.searchTicket(ticketId);

            if(ticket != null){
                System.out.println("\n" + ticket);
            }
        } catch(NumberFormatException e) {
            System.out.println("Invalid ticket ID");
        }
    }

    /**
     * Updates ticket priority
     */
    private static void updateTicketPriority() {
        System.out.println("\n===Update TICKET PRIORITY===");

        System.out.print("Enter ticket ID: ");
        try{
            int ticketId = Integer.parseInt(scanner.nextLine());
            System.out.println("\nNew priority level:");
            System.out.println("1. Security Issue (Highest)");
            System.out.println("2. Network Issue");
            System.out.println("3. Software Installation");
            System.out.println("4. New Computer Configuration (Lowest)");
            System.out.print("Enter new priority (1-4): ");
            int newPriority = Integer.parseInt(scanner.nextLine());
            if(newPriority < 1 || newPriority > 4) {
                System.out.println("Invalid priority. Must be between 1 and 4");
                return;
            }

            ticketService.updateTicketPriority(ticketId, newPriority);
    } catch (NumberFormatException e) {
        System.out.println("Invalid input");
        }
    }

    /**
     * Removes a ticket
     */

    private static void removeTicket() {
        System.out.println("\n===REMOVE TICKET===");

        System.out.print("Enter ticket ID to remove: ");

        try {
            int ticketId = Integer.parseInt(scanner.nextLine());

            // Confirm removal
            System.out.println("Are you sure you want to remove this ticket? (y/n): ");
            String confirm = scanner.nextLine().toLowerCase().trim();

            if(confirm.equals("y")) {
                Ticket removed = ticketService.removeTicket(ticketId);
                if(removed != null) {
                    System.out.println("Removed ticket: "+ removed.toSummaryString());
                }
            } else {
                System.out.println("Ticket removal cancelled");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid ticket ID");
        }
    }

    /**
     * Assigns an owner to a ticket
     */
    private static void assignOwner() {
        System.out.println("\n===ASSIGN OWNER TO TICKET===");

        System.out.print("Enter ticket ID: ");
        try {
            int ticketId = Integer.parseInt(scanner.nextLine());
            System.out.print("Enter owner name: ");
            String owner = scanner.nextLine().trim();
            if(owner.isEmpty()) {
                System.out.println("Owner name cannot be empty");
                return;
            }
            ticketService.assignOwner(ticketId, owner);
        } catch(NumberFormatException e) {
            System.out.println("Invalid ticket ID");
        }
    }

    /**
     * Displays all tickets (summary view)
     */

    private static void displayAllTickets() {
        ticketService.displayAllTickets();
    }

    /**
     * Displays all tickets (detailed view)
     */
    private static void displayAllTicketsDetailed() {
        ticketService.displayAllTicketsDetailed();
    }

    /**
     * Displays system statistics
     */
    private static void displayStatistics() {
        ticketService.displayStatistics();
    }

    /**
     * Peeks at the next ticket without removing it
     */

    private static void peekNextTicket() {
        System.out.println("\n===PEEK NEXT TICKET===");

        Ticket next = ticketService.peekNextTicket();

        if(next != null) {
            System.out.println("\nNext ticket:");
            System.out.println(next);
        } else {
            System.out.println("No tickets in the queue");
        }
    }

    /**
     * Pauses for user to read output
     */
    private static void pauseForUser() {
        System.out.println("\nPress enter to continue...");
        scanner.nextLine();
    }

    /**
     * Displays goodbye message
     */
    private static void displayGoodbye() {
        System.out.println("\n==============================================");
        System.out.println("|   Thanks for using our IT ticketing system!  |");
        System.out.println("|                   Goodbye!                   |");
        System.out.println("================================================");

    }
    /**
     * Creates sample data for testing
     * Uncomment and call this method in main() for testing
     */
    @SuppressWarnings("unused")
    private static void createSampleData() {
        System.out.println("\n === Creating sample data === ");

        ticketService.createTicket("Rosslan Koulli", 1, "Cannot login - password expired");
        ticketService.createTicket("Dricus Elmer", 2, "Internet connection very slow");
        ticketService.createTicket("Ronnie Japitkus", 3, "Need Microsoft Office installed");
        ticketService.createTicket("Daina Zed", 4, "Setting up new laptop");
        ticketService.createTicket("Evellyn Black", 1, "Suspicious email received");
        ticketService.createTicket("Tom Paspinal", 2, "VPN not connecting");

        System.out.println("Sample data created");
    }
}
