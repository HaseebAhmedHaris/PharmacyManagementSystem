import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FileManager {

    // File paths 
    private static final String INVENTORY_FILE    = "data/inventory.txt";
    private static final String SALES_LOG_FILE    = "data/sales_log.txt";
    private static final String ACTIVITY_LOG_FILE = "data/activity_log.txt";
    private static final String ALERT_LOG_FILE    = "data/alerts_log.txt";
    private static final String BACKUP_FOLDER     = "data/backups/";

    //  SETUP ─────────────────────────────────────────────────────────
    // Called once on startup to make sure all folders exist before we try to write
    public static void initializeFolders() {
        new File("data").mkdirs();
        new File(BACKUP_FOLDER).mkdirs();
    }

    //  LOAD INVENTORY ────────────────────────────────────────────────
    // Reads the inventory file line by line and rebuilds the Medicine array
    public static void loadInventory() {
        File file = new File(INVENTORY_FILE);

        // If no file exists yet (first run), nothing to load — that is fine
        if (!file.exists()) {
            System.out.println("No existing inventory found. Starting fresh.");
            return;
        }

        Medicine[] loaded = new Medicine[100];
        int loadedCount = 0;

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;

            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue; // skip blank lines

                Medicine m = Medicine.fromCsvString(line);
                if (m != null && loadedCount < 100) {
                    loaded[loadedCount] = m;
                    loadedCount++;
                }
            }
            reader.close();

            // Push the loaded array into the Medicine class
            Medicine.setInventory(loaded, loadedCount);
            System.out.println("Inventory loaded: " + loadedCount + " medicines.");

        } catch (FileNotFoundException e) {
            System.out.println("Inventory file not found: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Error reading inventory: " + e.getMessage());
        } catch (NumberFormatException e) {
            // Catches corrupt lines with bad numbers and skips gracefully
            System.out.println("Inventory file has a corrupted line: " + e.getMessage());
        }
    }

    //  SAVE INVENTORY ────────────────────────────────────────────────
    // Overwrites the inventory file with the current state of the Medicine array
    public static void saveInventory() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(INVENTORY_FILE));

            Medicine[] inventory = Medicine.getInventory();
            int count = Medicine.getCount();

            for (int i = 0; i < count; i++) {
                writer.write(inventory[i].toCsvString());
                writer.newLine();
            }
            writer.close();

        } catch (IOException e) {
            System.out.println("Error saving inventory: " + e.getMessage());
        }
    }

    //  LOG SALE ──────────────────────────────────────────────────────
    // Appends one sale record to the sales log file — never overwrites
    public static void logSale(String billDetails) {
        try {
            // true = append mode so we never overwrite previous sales
            BufferedWriter writer = new BufferedWriter(new FileWriter(SALES_LOG_FILE, true));
            writer.write(getTimestamp() + " | " + billDetails);
            writer.newLine();
            writer.close();

        } catch (IOException e) {
            System.out.println("Error logging sale: " + e.getMessage());
        }
    }

    //  LOG ACTIVITY ──────────────────────────────────────────────────
    // Records every admin action with a timestamp so there is always a trail
    public static void logActivity(String action) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(ACTIVITY_LOG_FILE, true));
            writer.write(getTimestamp() + " | " + action);
            writer.newLine();
            writer.close();

        } catch (IOException e) {
            System.out.println("Error logging activity: " + e.getMessage());
        }
    }

    //  LOG ALERT ─────────────────────────────────────────────────────
    // Saves expiry and low stock alerts to a history file on every startup
    public static void logAlert(String alertMessage) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(ALERT_LOG_FILE, true));
            writer.write(getTimestamp() + " | " + alertMessage);
            writer.newLine();
            writer.close();

        } catch (IOException e) {
            System.out.println("Error logging alert: " + e.getMessage());
        }
    }

    //  GENERATE BACKUP ────────────────────────────────────────────────
    // Creates a dated copy of the inventory file in the backups folder
    // Called on program shutdown so we never lose data
    public static void generateBackup() {
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        String backupPath = BACKUP_FOLDER + "inventory_" + today + ".txt";

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(backupPath));

            Medicine[] inventory = Medicine.getInventory();
            int count = Medicine.getCount();

            for (int i = 0; i < count; i++) {
                writer.write(inventory[i].toCsvString());
                writer.newLine();
            }
            writer.close();
            System.out.println("Backup saved: " + backupPath);

        } catch (IOException e) {
            System.out.println("Error creating backup: " + e.getMessage());
        }
    }

    //  READ SALES LOG ────────────────────────────────────────────────
    // Used by Admin when viewing sales reports
    public static void printSalesLog() {
        File file = new File(SALES_LOG_FILE);
        if (!file.exists()) {
            System.out.println("No sales records found.");
            return;
        }

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            System.out.println("\n===== SALES LOG =====");
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            reader.close();

        } catch (IOException e) {
            System.out.println("Error reading sales log: " + e.getMessage());
        }
    }

    //  READ ACTIVITY LOG ────────────────────────────────────────────────
    // Used by Admin when reviewing who did what
    public static void printActivityLog() {
        File file = new File(ACTIVITY_LOG_FILE);
        if (!file.exists()) {
            System.out.println("No activity records found.");
            return;
        }

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            System.out.println("\n===== ACTIVITY LOG =====");
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            reader.close();

        } catch (IOException e) {
            System.out.println("Error reading activity log: " + e.getMessage());
        }
    }

    //  HELPER — TIMESTAMP ────────────────────────────────────────────
    // Returns current date and time as a formatted string for log entries
    private static String getTimestamp() {
        return LocalDateTime.now()
               .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
    }
}