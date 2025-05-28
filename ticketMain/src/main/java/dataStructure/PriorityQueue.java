package dataStructure;

import model.Ticket;

/**
 * Custo Prirotiy queeu implementation using a min_heap data strcutre
 * This implementation does not use java collections as stated in the assignment reqs
 *
 * Min-Heap property: paetn priority <= Children priorities
 * (Lowrer pirority number - higher prioritry in our system
 *
 * Time Complexity:
 * Insert : 0(log n)
 * Extract min: 0(log n)
 * Search: 0(n)
 * Update priority: 0(n) for search + 0(log n) for heapify
 *
 * @author rosslankoulli
 * @version 1.0
 */
public class PriorityQueue {

    //Array to store heap elemetns
    private Ticket[] heap;

    // Current number of elemetns in the heap
    private int size;

    // Maximum capacity of the heap
    private int capacity;

    // Default initial capacity
    private static final int DEFAULT_CAPACITY = 100;

    /**
     * Constructor with defaul capacity
     */

    public PriorityQueue() {
        this(DEFAULT_CAPACITY);
    }

    /** Construcotr with specified intial capacity
     *
     * @param initialCapacity Initial size of the heap array
     */
    public PriorityQueue(int initialCapacity) {
        if(initialCapacity <= 0) {
            throw new IllegalArgumentException("Capacity must be greater than 0");
        }
        this.capacity = initialCapacity;
        this.heap = new Ticket[capacity];
        this.size = 0;
    }

    /** Get the index of the parent node
     * Formula: (i-1)/2
     *
     * @param i Index of current node
     * @return Index of parent node
     */

    private int parent(int i) {
        return(i-1)/2;
    }

    /**
     * Gets the index of the left child node
     * Formula: 2i+1
     *
     * @param i Index of current node
     * @return Index of left child node
     */
    private int leftChild(int i) {
        return 2*i+1;
    }

    /**
     * Gets the index of the right child node
     * Formula: 2i +2
     *
     * @param i Index of current node
     * @return Index of right child node
     */
    private int rightChild(int i) {
        return 2*i+2;
    }

    /**
     * Swaps two elements in the heap array
     *
     * @param i First index
     * @param j Second index
     *
     */
    private void swap(int i, int j){
        Ticket temp = heap[i];
        heap[i] = heap[j];
        heap[j] = temp;
    }
    /**
     * Insets a new ticket into the priority queue
     * Time complexity 0(log n)
     *
     * @param ticket The ticket to insert
     *
     */

    public void insert(Ticket ticket) {
        if(ticket == null) {
            throw new IllegalArgumentException("Cannot insert null ticket");
        }

        // Check if we need to resize
        if (size >= capacity) {
            resize();
        }

        // Insert at the end
        heap[size] = ticket;
        int current = size;
        size++;

        //Bubble up to maintain heap property
        bubbleUp(current);
    }

    /**
     * Bubbles up an element to maintain heap property
     * Continues swapping with parent while parent has lower priority( higher number)
     *
     * @param index Starting index
     */
    private void bubbleUp(int index) {
        while(index > 0 &&
                heap[parent(index)].getPriority() > heap[index].getPriority()) {
            swap(index, parent(index));
            index = parent(index);
        }
    }

    /**
     * Extract and returns the highest priority ticket( minimum priority number)
     * Time Complexity 0(log n)
     *
     * @return The highest priority ticket, or null if queue is empty
     */

    public Ticket extractMin() {
        if(isEmpty()) {
            return null;
        }

        // Save the minimum (root)
        Ticket min = heap[0];

        // Move last element to root
        heap[0] = heap[size -1];
        heap[size-1] = null; // Helps garbage collection
        size--;

        // Restore heap property
        if(size > 0) {
            heapify(0);
        }
        return min;
    }

    /**
     * Restores heap property by bubbling down
     * Also know as heapigy-down or sift-down
     *
     * @param index Starting index
     */

    private void heapify(int index) {
        int smallest = index ;
        int left = leftChild(index);
        int right = rightChild(index);

        // Find the smallest among parent, left child and right child
        if (left < size &&
            heap[left].getPriority() < heap[smallest].getPriority()) {
            smallest = left;
        }

        if(right < size &&
            heap[right].getPriority() < heap[smallest].getPriority()) {
            smallest = right;
            }

        // if smallest is not the parent, swap and continue
        if(smallest != index) {
            swap(index, smallest);
            heapify(smallest);
        }
    }

    /**
     * Searches for a ticket by ID
     * Time Complexity: 0(n) must search entire heap
     *
     * @param ticketID the ID to search for
     * @return The ticket if found, null otherwise
     */

    public Ticket search(int ticketID) {
        for(int i = 0; i < size; i++) {
            if(heap[i].getTicketID()== ticketID) {
                return heap[i];
            }
        }
        return null;
    }

    /**
     * Updates the priority of a ticket
     * Time complexity: 0(n) for search + 0(log n) for reheapify
     *
     * @param ticketID ID of the ticket to update
     * @param newPriority   New priority value
     * @return true if update succesful, false if ticket not found
     *
     */

    public boolean updatePriority(int ticketID, int newPriority) {
        // Validate new priroity
        if (newPriority < 1 || newPriority > 4) {
            throw new IllegalArgumentException("priority must be between 1 and 4");
        }

        // Find the ticket
        int index = -1;
        for(int i = 0; i < size; i++) {
            if(heap[i].getTicketID() == ticketID) {
                index = i;
                break;
            }
        }

        if(index == -1) {
            return false; // Ticket not found
        }

        // update priority
        int oldPriority = heap[index].getPriority();
        heap[index].setPriority(newPriority);

        // Restore heap property
        if(newPriority < oldPriority) {
            // Priority increased (lower number), bubble up
            bubbleUp(index);
        } else if (newPriority > oldPriority) {
            // Prioirty decreased ( higher number, bubble down
            heapify(index);
        }
        // If priority unchanged, no action needed

        return true;
    }
    /**
     * Removes a specific ticket by ID
     * Time complexity 0(n) for search + 0(log n) for reheapify
     *
     * @param ticketID ID of the ticket to remove
     * @return The removed ticket, or null if not found
     */

    public Ticket remove(int ticketID) {
        // Find the ticket
        int index = -1;
        for(int i = 0; i < size; i++) {
            if(heap[i].getTicketID() == ticketID) {
                index = i;
                break;
            }
        }

        if (index == -1) {
            return null; // Ticket not found
        }

        // Save the ticket to retunr
        Ticket removed = heap[index];

        // Move last element to this position
        heap[index] = heap[size -1];
        heap[size -1] = null; // Helps garbage collection
        size--;

        // Restore heap property
        if(index < size) {
            // First try bubbling up
            bubbleUp(index);

            // Then try buble down
            heapify(index);
        }

        return removed;
    }

    /** Returns the highest rpirotiy ticket without removing it
     * Time Complexity 0(1)
     *
     * @return The highest priority ticket, or null if empty
     */

    public Ticket peek() {
        return isEmpty() ? null : heap[0];
    }

    /**
     * Checks if the queue is empty
     *
     * @return true if empty, false otherwise
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Returns the number of tickets in the queue
     *
     * @return current size
     */
    public int size() {
        return size;
    }

    /**
     * Resizes the heap array when cpacity is reached
     * Dobules the capacity
     */
    private void resize() {
        int newCapacity = capacity * 2;
        Ticket[] newHeap = new Ticket[newCapacity];

        //Copy existing element s
        for(int i = 0; i < size; i++) {
            newHeap[i] = heap[i];
        }

        heap = newHeap;
        capacity = newCapacity;
    }

    /**
     * Returns all tickets in an array (not in any particular order)
     * Useful for diapliyng all tickets
     *
     * @returns Array of all tickets
     */

    public Ticket[] getAllTickets() {
        Ticket[] result = new Ticket[size];
        for(int i = 0; i < size; i++) {
            result[i] = heap[i];
        }
        return result;
    }

    /**
     * Returns all tickets sorted by priority
     * Creates a copy and sorts it, doesnt modify the heap
     *
     * @return Array of tickets sorted by priority
     */

    public Ticket[] getAllTicketsSorted() {
        Ticket[] sorted = getAllTickets();

        // Simple bubble sort (adequate for small datasets_
        for(int i =0; i < sorted.length -1; i++) {
            for(int j=0; j < sorted.length - i -1; j++) {
                if(sorted[j].getPriority() >sorted[j+1].getPriority()) {
                    Ticket temp = sorted[j];
                    sorted[j] = sorted[j+1];
                    sorted[j+1] = temp;
                }
            }
        }
        return sorted;
    }
    /**
     * Clears all tickets from the queue
     *
     */
    public void clear() {
        for (int i = 0; i < size; i++) {
            heap[i] = null;// Helps garbage collection
        }
        size = 0;
    }

}

