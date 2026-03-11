import java.util.LinkedList;
import java.util.Queue;

class Reservation {
    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }
}

class BookingRequestQueue {
    private Queue<Reservation> requests;

    public BookingRequestQueue() {
        requests = new LinkedList<>();
    }

    public void enqueueRequest(Reservation reservation) {
        requests.add(reservation);
    }

    public void displayQueue() {
        System.out.println("Current Booking Request Queue:");
        for (Reservation res : requests) {
            System.out.println("Guest: " + res.getGuestName() + " | Requested: " + res.getRoomType());
        }
    }
}

public class BookMyStayApp {
    public static void main(String[] args) {
        BookingRequestQueue bookingQueue = new BookingRequestQueue();

        System.out.println("Processing Booking Requests (First-Come-First-Served)\n");

        bookingQueue.enqueueRequest(new Reservation("Alice", "Single Room"));
        bookingQueue.enqueueRequest(new Reservation("Bob", "Suite Room"));
        bookingQueue.enqueueRequest(new Reservation("Charlie", "Double Room"));

        bookingQueue.displayQueue();

        System.out.println("\nRequests are now queued and waiting for allocation.");
    }
}