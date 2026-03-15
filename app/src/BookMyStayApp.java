import java.util.*;

class Reservation {
    String guestName;
    String roomType;

    Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }
}

class BookingRequestQueue {
    private Queue<Reservation> queue = new LinkedList<>();

    public void addRequest(Reservation r) {
        queue.add(r);
    }

    public Reservation getRequest() {
        return queue.poll();
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }
}

class RoomInventory {
    private Map<String,Integer> inventory = new HashMap<>();
    private Map<String,Integer> counters = new HashMap<>();

    RoomInventory() {
        inventory.put("Single",5);
        inventory.put("Double",3);
        inventory.put("Suite",2);

        counters.put("Single",1);
        counters.put("Double",1);
        counters.put("Suite",1);
    }

    public String allocate(String type) {
        int available = inventory.get(type);
        if(available<=0) return null;

        inventory.put(type,available-1);

        int id = counters.get(type);
        counters.put(type,id+1);

        return type + "-" + id;
    }

    public Map<String,Integer> getInventory() {
        return inventory;
    }
}

class RoomAllocationService {
    public void allocateRoom(Reservation r, RoomInventory inventory) {
        String roomId = inventory.allocate(r.roomType);
        if(roomId!=null) {
            System.out.println("Booking confirmed for Guest: "+r.guestName+", Room ID: "+roomId);
        }
    }
}

class ConcurrentBookingProcessor implements Runnable {

    private BookingRequestQueue bookingQueue;
    private RoomInventory inventory;
    private RoomAllocationService allocationService;

    ConcurrentBookingProcessor(BookingRequestQueue bookingQueue,
                               RoomInventory inventory,
                               RoomAllocationService allocationService) {
        this.bookingQueue = bookingQueue;
        this.inventory = inventory;
        this.allocationService = allocationService;
    }

    public void run() {
        while(true) {
            Reservation reservation;

            synchronized (bookingQueue) {
                if(bookingQueue.isEmpty()) break;
                reservation = bookingQueue.getRequest();
            }

            synchronized (inventory) {
                allocationService.allocateRoom(reservation,inventory);
            }
        }
    }
}

public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("Concurrent Booking Simulation");

        BookingRequestQueue bookingQueue = new BookingRequestQueue();
        RoomInventory inventory = new RoomInventory();
        RoomAllocationService allocationService = new RoomAllocationService();

        bookingQueue.addRequest(new Reservation("Abhi","Single"));
        bookingQueue.addRequest(new Reservation("Vanmathi","Double"));
        bookingQueue.addRequest(new Reservation("Kural","Suite"));
        bookingQueue.addRequest(new Reservation("Subha","Single"));

        Thread t1 = new Thread(new ConcurrentBookingProcessor(
                bookingQueue,inventory,allocationService
        ));

        Thread t2 = new Thread(new ConcurrentBookingProcessor(
                bookingQueue,inventory,allocationService
        ));

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            System.out.println("Thread execution interrupted.");
        }

        System.out.println();
        System.out.println("Remaining Inventory:");

        Map<String,Integer> inv = inventory.getInventory();
        System.out.println("Single: "+inv.get("Single"));
        System.out.println("Double: "+inv.get("Double"));
        System.out.println("Suite: "+inv.get("Suite"));
    }
}