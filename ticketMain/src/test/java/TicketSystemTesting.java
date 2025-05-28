
import model.Ticket;
import dataStructure.PriorityQueue;
import service.TicketService;
import utils.IDGenerator;

/**
 * Test class for the IT Ticketing System.
 * Tests all major functionality including the priority queue operations,
 * ticket creation, and service methods.
 *
 * Run this class to verify that all components are working correctly.
 *
 * @author Rosslan Koulli
 * @version 1.0
 */
public class TicketSystemTesting {

    // Test counters
    private static int totalTests = 0;
    private static int passedTests = 0;

    /**
     * Main method to run all tests
     */
    public static void main(String[] args) {
        System.out.println("=== IT Ticketing System Test Suite ===\n");

        // Run all test categories
        testPriorityQueueOperations();
        testTicketCreation();
        testServiceOperations();
        testEdgeCases();
        testPerformance();

        // Display summary
        displayTestSummary();
    }

    /**
     * Tests basic priority queue operations
     */
    private static void testPriorityQueueOperations() {
        System.out.println("1. Testing Priority Queue Operations");
        System.out.println("------------------------------------");

        PriorityQueue queue = new PriorityQueue();

        // Test 1.1: Empty queue
        testCase("1.1 Empty queue check",
                queue.isEmpty() == true,
                "Queue should be empty initially");

        // Test 1.2: Insert and size
        Ticket t1 = new Ticket(1, "User1", "Security", "Test", 1);
        Ticket t2 = new Ticket(2, "User2", "Network", "Test", 2);
        Ticket t3 = new Ticket(3, "User3", "Software", "Test", 3);

        queue.insert(t1);
        queue.insert(t2);
        queue.insert(t3);

        testCase("1.2 Insert and size",
                queue.size() == 3,
                "Queue should contain 3 tickets");

        // Test 1.3: Extract min (highest priority)
        Ticket extracted = queue.extractMin();
        testCase("1.3 Extract min priority",
                extracted.getPriority() == 1,
                "Should extract priority 1 ticket first");

        // Test 1.4: Priority order maintained
        Ticket second = queue.extractMin();
        testCase("1.4 Priority order",
                second.getPriority() == 2,
                "Should extract priority 2 ticket second");

        // Test 1.5: Search functionality
        queue.insert(t1); // Re-insert
        Ticket found = queue.search(1);
        testCase("1.5 Search by ID",
                found != null && found.getTicketID() == 1,
                "Should find ticket by ID");

        // Test 1.6: Update priority
        queue.updatePriority(1, 4); // Change to the lowest priority
        queue.extractMin(); // Should get ticket 3 (priority 3)
        Ticket afterUpdate = queue.extractMin();
        testCase("1.6 Update priority",
                afterUpdate.getTicketID() == 1 && afterUpdate.getPriority() == 4,
                "Priority update should work correctly");

        System.out.println();
    }

    /**
     * Tests ticket creation and validation
     */
    private static void testTicketCreation() {
        System.out.println("2. Testing Ticket Creation");
        System.out.println("--------------------------");

        // Test 2.1: Valid ticket creation
        try {
            Ticket ticket = new Ticket(100, "Avram Goldmen", "Security Issue",
                    "Password reset needed", 1);
            testCase("2.1 Valid ticket creation",
                    ticket != null && ticket.getTicketID() == 100,
                    "Should create valid ticket");
        } catch (Exception e) {
            testCase("2.1 Valid ticket creation", false,
                    "Unexpected exception: " + e.getMessage());
        }

        // Test 2.2: Invalid priority
        try {
            new Ticket(101, "Janette Doeviltin", "Invalid", "Test", 5);
            testCase("2.2 Invalid priority", false,
                    "Should throw exception for invalid priority");
        } catch (IllegalArgumentException e) {
            testCase("2.2 Invalid priority", true,
                    "Correctly threw exception for invalid priority");
        }

        // Test 2.3: Null creator
        try {
            new Ticket(102, null, "Network", "Test", 2);
            testCase("2.3 Null creator", false,
                    "Should throw exception for null creator");
        } catch (IllegalArgumentException e) {
            testCase("2.3 Null creator", true,
                    "Correctly threw exception for null creator");
        }

        // Test 2.4: Status and timestamps
        Ticket ticket = new Ticket(103, "Test User", "Software", "Install app", 3);
        testCase("2.4 Initial status",
                ticket.getStatus() == Ticket.TicketStatus.OPEN,
                "New ticket should have OPEN status");

        testCase("2.5 Timestamps set",
                ticket.getCreatedAt() != null && ticket.getUpdatedAt() != null,
                "Timestamps should be set on creation");

        System.out.println();
    }

    /**
     * Tests service layer operations
     */
    private static void testServiceOperations() {
        System.out.println("3. Testing Service Operations");
        System.out.println("-----------------------------");

        TicketService service = new TicketService();

        // Test 3.1: Create ticket through service
        Ticket created = service.createTicket("Alice", 1, "Security breach detected");
        testCase("3.1 Service ticket creation",
                created != null && created.getTicketID() >= 1000,
                "Service should create ticket with proper ID");

        // Test 3.2: Create multiple tickets
        service.createTicket("Bob", 2, "Network down");
        service.createTicket("Carol", 3, "Install software");
        service.createTicket("Dave", 4, "New computer setup");

        testCase("3.2 Multiple tickets",
                service.getTicketCount() == 4,
                "Should have 4 tickets in system");

        // Test 3.3: Process highest priority
        Ticket processed = service.processNextTicket();
        testCase("3.3 Process highest priority",
                processed.getCreator().equals("Alice") && processed.getPriority() == 1,
                "Should process security ticket first");

        // Test 3.4: Search functionality
        Ticket found = service.searchTicket(created.getTicketID());
        testCase("3.4 Service search",
                found == null,
                "Should not find processed ticket");

        // Test 3.5: Update priority
        int bobTicketId = service.getAllTickets()[0].getTicketID();
        boolean updated = service.updateTicketPriority(bobTicketId, 1);
        testCase("3.5 Service priority update",
                updated == true,
                "Should successfully update priority");

        // Test 3.6: Assign owner
        boolean assigned = service.assignOwner(bobTicketId, "IT Admin");
        Ticket withOwner = service.searchTicket(bobTicketId);
        testCase("3.6 Assign owner",
                assigned && withOwner.getOwner().equals("IT Admin"),
                "Should assign owner successfully");

        System.out.println();
    }

    /**
     * Tests edge cases and error handling
     */
    private static void testEdgeCases() {
        System.out.println("4. Testing Edge Cases");
        System.out.println("---------------------");

        PriorityQueue queue = new PriorityQueue(2); // Small initial capacity

        // Test 4.1: Empty queue operations
        testCase("4.1 Extract from empty",
                queue.extractMin() == null,
                "Should return null from empty queue");

        testCase("4.2 Peek empty",
                queue.peek() == null,
                "Should return null when peeking empty queue");

        // Test 4.3: Automatic resizing
        for (int i = 0; i < 5; i++) {
            queue.insert(new Ticket(i, "User" + i, "Type", "Desc", 2));
        }
        testCase("4.3 Automatic resize",
                queue.size() == 5,
                "Should resize and hold more than initial capacity");

        // Test 4.4: Same priority handling
        queue.clear();
        queue.insert(new Ticket(10, "A", "Type", "Desc", 2));
        queue.insert(new Ticket(11, "B", "Type", "Desc", 2));
        queue.insert(new Ticket(12, "C", "Type", "Desc", 2));

        boolean allPriority2 = true;
        for (int i = 0; i < 3; i++) {
            if (queue.extractMin().getPriority() != 2) {
                allPriority2 = false;
            }
        }
        testCase("4.4 Same priority tickets",
                allPriority2,
                "Should handle multiple tickets with same priority");

        // Test 4.5: Remove non-existent
        TicketService service = new TicketService();
        Ticket removed = service.removeTicket(99999);
        testCase("4.5 Remove non-existent",
                removed == null,
                "Should return null for non-existent ticket");

        System.out.println();
    }

    /**
     * Tests performance with larger datasets
     */
    private static void testPerformance() {
        System.out.println("5. Testing Performance");
        System.out.println("----------------------");

        PriorityQueue queue = new PriorityQueue();
        int testSize = 1000;

        // Test 5.1: Bulk insertion
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < testSize; i++) {
            int priority = (i % 4) + 1; // Distribute across priorities
            queue.insert(new Ticket(i, "User" + i, "Type", "Desc", priority));
        }
        long insertTime = System.currentTimeMillis() - startTime;

        testCase("5.1 Bulk insertion",
                queue.size() == testSize,
                "Inserted " + testSize + " tickets in " + insertTime + "ms");

        // Test 5.2: Priority order maintained
        int lastPriority = 0;
        boolean orderMaintained = true;

        startTime = System.currentTimeMillis();
        while (!queue.isEmpty()) {
            Ticket t = queue.extractMin();
            if (t.getPriority() < lastPriority) {
                orderMaintained = false;
                break;
            }
            lastPriority = t.getPriority();
        }
        long extractTime = System.currentTimeMillis() - startTime;

        testCase("5.2 Large dataset ordering",
                orderMaintained,
                "Extracted " + testSize + " tickets in correct order in " + extractTime + "ms");

        // Test 5.3: Search performance
        queue = new PriorityQueue();
        for (int i = 0; i < testSize; i++) {
            queue.insert(new Ticket(i, "User" + i, "Type", "Desc", 2));
        }

        startTime = System.currentTimeMillis();
        Ticket found = queue.search(testSize / 2); // Search middle element
        long searchTime = System.currentTimeMillis() - startTime;

        testCase("5.3 Search performance",
                found != null,
                "Found ticket in " + searchTime + "ms from " + testSize + " tickets");

        System.out.println();
    }

    /**
     * Helper method to run a test case
     */
    private static void testCase(String testName, boolean condition, String message) {
        totalTests++;
        if (condition) {
            passedTests++;
            System.out.println("✔ " + testName + " - PASSED");
        } else {
            System.out.println("x " + testName + " - FAILED: " + message);
        }
    }

    /**
     * Displays test summary
     */
    private static void displayTestSummary() {
        System.out.println("=== Test Summary ===");
        System.out.println("Total tests: " + totalTests);
        System.out.println("Passed: " + passedTests);
        System.out.println("Failed: " + (totalTests - passedTests));
        System.out.println("Success rate: " +
                String.format("%.1f%%", (passedTests * 100.0 / totalTests)));

        if (passedTests == totalTests) {
            System.out.println("\n All tests passed! The system is working correctly.");
        } else {
            System.out.println("\n Some tests failed. Please review the implementation.");
        }
    }
}