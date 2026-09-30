import java.io.*;
import java.util.*;

public class HotelReservationSystem {
    static class Room {
        int roomNumber;
        String category;
        double price;
        boolean booked;

        Room(int roomNumber, String category, double price) {
            this.roomNumber = roomNumber;
            this.category = category;
            this.price = price;
            this.booked = false;
        }
    }

    static class Reservation {
        int reservationId;
        String customerName;
        int roomNumber;
        int nights;
        double amount;

        Reservation(int reservationId, String customerName,
                    int roomNumber, int nights, double amount) {
            this.reservationId = reservationId;
            this.customerName = customerName;
            this.roomNumber = roomNumber;
            this.nights = nights;
            this.amount = amount;
        }

        String toFileString() {
            return reservationId + "|" + customerName.replace("|", " ") + "|"
                    + roomNumber + "|" + nights + "|" + amount;
        }
    }

    static ArrayList<Room> rooms = new ArrayList<>();
    static ArrayList<Reservation> reservations = new ArrayList<>();
    static Scanner sc = new Scanner(System.in);
    static final String FILE = "reservations.txt";
    static int nextReservationId = 1001;

    public static void main(String[] args) {
        createRooms();
        loadReservations();

        while (true) {
            System.out.println("\n===== HOTEL RESERVATION SYSTEM =====");
            System.out.println("1. Search Available Rooms");
            System.out.println("2. Book Room");
            System.out.println("3. View Reservations");
            System.out.println("4. Cancel Reservation");
            System.out.println("5. Exit");
            System.out.print("Enter choice: ");

            int choice = readInt();

            switch (choice) {
                case 1 -> searchRooms();
                case 2 -> bookRoom();
                case 3 -> viewReservations();
                case 4 -> cancelReservation();
                case 5 -> {
                    saveReservations();
                    System.out.println("Data saved. Thank you!");
                    return;
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    static void createRooms() {
        rooms.add(new Room(101, "Standard", 2000));
        rooms.add(new Room(102, "Standard", 2000));
        rooms.add(new Room(201, "Deluxe", 3500));
        rooms.add(new Room(202, "Deluxe", 3500));
        rooms.add(new Room(301, "Suite", 5500));
    }

    static void searchRooms() {
        System.out.println("\n----- AVAILABLE ROOMS -----");
        boolean found = false;

        for (Room r : rooms) {
            if (!r.booked) {
                System.out.printf("Room: %d | Category: %s | Price/Night: Rs. %.2f%n",
                        r.roomNumber, r.category, r.price);
                found = true;
            }
        }

        if (!found) {
            System.out.println("No rooms available.");
        }
    }

    static void bookRoom() {
        searchRooms();

        System.out.print("Enter room number: ");
        int roomNumber = readInt();

        Room room = findRoom(roomNumber);

        if (room == null || room.booked) {
            System.out.println("Room is not available.");
            return;
        }

        System.out.print("Enter customer name: ");
        String name = sc.nextLine();

        System.out.print("Enter number of nights: ");
        int nights = readInt();

        if (nights <= 0) {
            System.out.println("Nights must be positive.");
            return;
        }

        double amount = room.price * nights;

        System.out.println("\n----- BOOKING SUMMARY -----");
        System.out.println("Customer : " + name);
        System.out.println("Room     : " + room.roomNumber);
        System.out.println("Category : " + room.category);
        System.out.println("Nights   : " + nights);
        System.out.printf("Amount   : Rs. %.2f%n", amount);

        System.out.print("Confirm booking? (Y/N): ");
        String confirm = sc.nextLine();

        if (!confirm.equalsIgnoreCase("Y")) {
            System.out.println("Booking cancelled.");
            return;
        }

        Reservation reservation = new Reservation(
                nextReservationId++, name, room.roomNumber, nights, amount);

        reservations.add(reservation);
        room.booked = true;

        System.out.println("Payment simulation: Payment successful.");
        System.out.println("Booking successful.");
        System.out.println("Reservation ID: " + reservation.reservationId);

        saveReservations();
    }

    static void viewReservations() {
        if (reservations.isEmpty()) {
            System.out.println("No reservations found.");
            return;
        }

        System.out.println("\n----- RESERVATIONS -----");

        for (Reservation r : reservations) {
            System.out.printf(
                    "ID: %d | Customer: %s | Room: %d | Nights: %d | Amount: Rs. %.2f%n",
                    r.reservationId, r.customerName,
                    r.roomNumber, r.nights, r.amount);
        }
    }

    static void cancelReservation() {
        viewReservations();

        if (reservations.isEmpty()) return;

        System.out.print("Enter reservation ID: ");
        int id = readInt();

        Reservation found = null;

        for (Reservation r : reservations) {
            if (r.reservationId == id) {
                found = r;
                break;
            }
        }

        if (found == null) {
            System.out.println("Reservation not found.");
            return;
        }

        Room room = findRoom(found.roomNumber);
        if (room != null) {
            room.booked = false;
        }

        reservations.remove(found);
        saveReservations();

        System.out.println("Reservation cancelled successfully.");
        System.out.printf("Refund simulation: Rs. %.2f%n", found.amount);
    }

    static Room findRoom(int number) {
        for (Room r : rooms) {
            if (r.roomNumber == number) {
                return r;
            }
        }
        return null;
    }

    static void saveReservations() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE))) {
            for (Reservation r : reservations) {
                writer.println(r.toFileString());
            }
        } catch (IOException e) {
            System.out.println("Could not save reservations: " + e.getMessage());
        }
    }

    static void loadReservations() {
        File file = new File(FILE);

        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");

                if (parts.length == 5) {
                    int id = Integer.parseInt(parts[0]);
                    String name = parts[1];
                    int roomNumber = Integer.parseInt(parts[2]);
                    int nights = Integer.parseInt(parts[3]);
                    double amount = Double.parseDouble(parts[4]);

                    reservations.add(
                            new Reservation(id, name, roomNumber, nights, amount));

                    Room room = findRoom(roomNumber);
                    if (room != null) room.booked = true;

                    if (id >= nextReservationId) {
                        nextReservationId = id + 1;
                    }
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("Could not load saved reservations.");
        }
    }

    static int readInt() {
        while (true) {
            try {
                return Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("Enter a valid number: ");
            }
        }
    }
}
