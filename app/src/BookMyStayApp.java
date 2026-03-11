import java.util.*;

abstract class Room {
    protected int numberOfBeds;
    protected int squareFeet;
    protected double pricePerNight;

    public Room(int numberOfBeds, int squareFeet, double pricePerNight) {
        this.numberOfBeds = numberOfBeds;
        this.squareFeet = squareFeet;
        this.pricePerNight = pricePerNight;
    }

    public void displayRoomDetails() {
        System.out.println("Beds: " + numberOfBeds);
        System.out.println("Size: " + squareFeet + " sqft");
        System.out.println("Price per night: " + pricePerNight);
    }
}

class SingleRoom extends Room {
    public SingleRoom() {
        super(1, 250, 1500.0);
    }
}

class DoubleRoom extends Room {
    public DoubleRoom() {
        super(2, 400, 2500.0);
    }
}

class SuiteRoom extends Room {
    public SuiteRoom() {
        super(3, 750, 5000.0);
    }
}

class Reservation {
    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() { return guestName; }
    public String getRoomType() { return roomType; }
}

class RoomInventory {
    private Map<String, Integer> availability = new HashMap<>();
    private Map<String, Set<String>> allocatedRoomIds = new HashMap<>();

    public RoomInventory() {
        availability.put("Single Room", 5);
        availability.put("Double Room", 3);
        availability.put("Suite Room", 2);
        allocatedRoomIds.put("Single Room", new HashSet<>());
        allocatedRoomIds.put("Double Room", new HashSet<>());
        allocatedRoomIds.put("Suite Room", new HashSet<>());
    }

    public int getAvailableCount(String type) {
        return availability.getOrDefault(type, 0);
    }

    public void allocateRoom(String type, String roomId) {
        availability.put(type, availability.get(type) - 1);
        allocatedRoomIds.get(type).add(roomId);
    }
}

class RoomAllocationService {
    public void processRequests(Queue<Reservation> queue, RoomInventory inventory) {
        System.out.println("Hotel Room Reservation Confirmation\n");

        while (!queue.isEmpty()) {
            Reservation request = queue.poll();
            String type = request.getRoomType();

            if (inventory.getAvailableCount(type) > 0) {
                String roomId = type.substring(0, 1).toUpperCase() + "-" + (100 + new Random().nextInt(900));
                inventory.allocateRoom(type, roomId);
                System.out.println("Confirmed: " + request.getGuestName() + " | " + type + " | Room ID: " + roomId);
            } else {
                System.out.println("Failed: No availability for " + request.getGuestName() + " (" + type + ")");
            }
        }
    }
}

public class BookMyStayApp {
    public static void main(String[] args) {
        RoomInventory inventory = new RoomInventory();
        RoomAllocationService allocationService = new RoomAllocationService();
        Queue<Reservation> bookingQueue = new LinkedList<>();

        bookingQueue.add(new Reservation("Alice", "Single Room"));
        bookingQueue.add(new Reservation("Bob", "Suite Room"));
        bookingQueue.add(new Reservation("Charlie", "Single Room"));
        bookingQueue.add(new Reservation("David", "Suite Room"));
        bookingQueue.add(new Reservation("Eve", "Suite Room"));

        allocationService.processRequests(bookingQueue, inventory);

        System.out.println("\nPost-Allocation Inventory:");
        System.out.println("Single Rooms Available: " + inventory.getAvailableCount("Single Room"));
        System.out.println("Suite Rooms Available: " + inventory.getAvailableCount("Suite Room"));
    }
}