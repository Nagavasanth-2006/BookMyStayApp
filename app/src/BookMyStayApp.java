import java.io.*;
import java.util.*;

class RoomInventory {
    private Map<String,Integer> inventory;

    public RoomInventory() {
        inventory = new LinkedHashMap<>();
        inventory.put("Single",5);
        inventory.put("Double",3);
        inventory.put("Suite",2);
    }

    public Map<String,Integer> getInventory() {
        return inventory;
    }

    public void setRoomCount(String type,int count) {
        inventory.put(type,count);
    }
}

class FilePersistenceService {

    public void saveInventory(RoomInventory inventory,String filePath) {
        try {
            PrintWriter writer = new PrintWriter(new FileWriter(filePath));
            for(Map.Entry<String,Integer> entry : inventory.getInventory().entrySet()) {
                writer.println(entry.getKey()+"="+entry.getValue());
            }
            writer.close();
            System.out.println("Inventory saved successfully.");
        } catch(Exception e) {
            System.out.println("Error saving inventory.");
        }
    }

    public void loadInventory(RoomInventory inventory,String filePath) {
        File file = new File(filePath);

        if(!file.exists()) {
            System.out.println("No valid inventory data found. Starting fresh.");
            return;
        }

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;

            while((line = reader.readLine()) != null) {
                String[] parts = line.split("=");
                inventory.setRoomCount(parts[0],Integer.parseInt(parts[1]));
            }

            reader.close();
        } catch(Exception e) {
            System.out.println("Error loading inventory.");
        }
    }
}

public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("System Recovery");

        RoomInventory inventory = new RoomInventory();
        FilePersistenceService persistence = new FilePersistenceService();

        String filePath = "inventory.txt";

        persistence.loadInventory(inventory,filePath);

        System.out.println();
        System.out.println("Current Inventory:");

        Map<String,Integer> inv = inventory.getInventory();

        System.out.println("Single: "+inv.get("Single"));
        System.out.println("Double: "+inv.get("Double"));
        System.out.println("Suite: "+inv.get("Suite"));

        persistence.saveInventory(inventory,filePath);
    }
}