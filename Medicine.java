import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Medicine {

    //Varialbes

    private int id;
    private String name;
    private String company;
    private double price;
    private String expiryDate;       
    private int quantity;
    private int minStockThreshold;   // alert triggers when quantity falls below this
    private String formulaCategory;  
    private boolean prescriptionRequired;

    // Auto-incrementing ID counter shared across all Medicine objects
    private static int idCounter = 1;

    //   array of Medicine objects, max 100 items
    private static Medicine[] inventory = new Medicine[100];
    private static int count = 0; // Meds Count


    public Medicine(String name, String company, double price, String expiryDate,
                    int quantity, int minStockThreshold,
                    String formulaCategory, boolean prescriptionRequired) {
        this.id = idCounter++;   
        this.name = name;
        this.company = company;
        this.price = price;
        this.expiryDate = expiryDate;
        this.quantity = quantity;
        this.minStockThreshold = minStockThreshold;
        this.formulaCategory = formulaCategory;
        this.prescriptionRequired = prescriptionRequired;
    }

    // Constructor used when loading from file (ID already exists, don't generate new one)
    public Medicine(int id, String name, String company, double price, String expiryDate,
                    int quantity, int minStockThreshold,
                    String formulaCategory, boolean prescriptionRequired) {
        this.id = id;
        this.name = name;
        this.company = company;
        this.price = price;
        this.expiryDate = expiryDate;
        this.quantity = quantity;
        this.minStockThreshold = minStockThreshold;
        this.formulaCategory = formulaCategory;
        this.prescriptionRequired = prescriptionRequired;

        // Keep idCounter ahead of any loaded IDs so new medicines get unique IDs
        if (id >= idCounter) {
            idCounter = id + 1;
        }
    }

    public int getId()                          { return id; }
    public String getName()                     { return name; }
    public String getCompany()                  { return company; }
    public double getPrice()                    { return price; }
    public String getExpiryDate()               { return expiryDate; }
    public int getQuantity()                    { return quantity; }
    public int getMinStockThreshold()           { return minStockThreshold; }
    public String getFormulaCategory()          { return formulaCategory; }
    public boolean isPrescriptionRequired()     { return prescriptionRequired; }

    public void setPrice(double price)                      { this.price = price; }
    public void setQuantity(int quantity)                   { this.quantity = quantity; }
    public void setMinStockThreshold(int threshold)         { this.minStockThreshold = threshold; }
    public void setName(String name)                        { this.name = name; }
    public void setCompany(String company)                  { this.company = company; }
    public void setExpiryDate(String expiryDate)            { this.expiryDate = expiryDate; }
    public void setFormulaCategory(String formulaCategory)  { this.formulaCategory = formulaCategory; }
    public void setPrescriptionRequired(boolean val)        { this.prescriptionRequired = val; }

    // Allow FileManager to restore the full inventory array on startup
    public static void setInventory(Medicine[] inv, int cnt) {
        inventory = inv;
        count = cnt;
    }

    public static Medicine[] getInventory() { return inventory; }
    public static int getCount()            { return count; }


    public static void resetIdCounter() {
    // Scan all loaded medicines and set counter above the highest existing ID
    int maxId = 0;
    for (int i = 0; i < count; i++) {
        if (inventory[i].getId() > maxId) {
            maxId = inventory[i].getId();
        }
    }
    idCounter = maxId + 1;
    }

    // ADD _____________________________________________________________________
    public static boolean addMedicine(Medicine m) {
        if (count >= inventory.length) {
            System.out.println("Inventory full. Cannot add more medicines.");
            return false;
        }
        inventory[count] = m;
        count++;
        System.out.println("Medicine added: " + m.getName() + " (ID: " + m.getId() + ")");
        return true;
    }

    // DELETE ___________________________________________________________________
    public static boolean deleteMedicine(int id) {
        for (int i = 0; i < count; i++) {
            if (inventory[i].getId() == id) {
                String deletedName = inventory[i].getName();

                // Position Shift After Deletion
                for (int j = i; j < count - 1; j++) {
                    inventory[j] = inventory[j + 1];
                }
                inventory[count - 1] = null; // clear the last slot after shifting
                count--;

                System.out.println("Deleted: " + deletedName);
                return true;
            }
        }
        System.out.println("Medicine with ID " + id + " not found.");
        return false;
    }

    //  UPDATE ────────────────────────────────────────────────────────
    public static Medicine getMedicineById(int id) {
        for (int i = 0; i < count; i++) {
            if (inventory[i].getId() == id) {
                return inventory[i];
            }
        }
        return null; // not found
    }

    //  SEARCH BY NAME ────────────────────────────────────────────────
    public static Medicine[] searchByName(String keyword) {
        Medicine[] results = new Medicine[count];
        int resultCount = 0;

        for (int i = 0; i < count; i++) {
            if (inventory[i].getName().toLowerCase().contains(keyword.toLowerCase())) {
                results[resultCount] = inventory[i];
                resultCount++;
            }
        }

        // Return only the filled portion of the results array
        Medicine[] trimmed = new Medicine[resultCount];
        for (int i = 0; i < resultCount; i++) {
            trimmed[i] = results[i];
        }
        return trimmed;
    }

    //  SEARCH BY ID ──────────────────────────────────────────────────
    public static Medicine searchById(int id) {
        for (int i = 0; i < count; i++) {
            if (inventory[i].getId() == id) {
                return inventory[i];
            }
        }
        return null;
    }

    //  FIND ALTERNATIVES ─────────────────────────────────────────────
    // When a medicine is out of stock, find others with the same formula category
    public static Medicine[] findAlternatives(String formulaCategory, int excludeId) {
        Medicine[] results = new Medicine[count];
        int resultCount = 0;

        for (int i = 0; i < count; i++) {
            boolean sameFormula = inventory[i].getFormulaCategory()
                                  .equalsIgnoreCase(formulaCategory);
            boolean notSameItem = inventory[i].getId() != excludeId;
            boolean inStock     = inventory[i].getQuantity() > 0;

            if (sameFormula && notSameItem && inStock) {
                results[resultCount] = inventory[i];
                resultCount++;
            }
        }

        Medicine[] trimmed = new Medicine[resultCount];
        for (int i = 0; i < resultCount; i++) {
            trimmed[i] = results[i];
        }
        return trimmed;
    }

    //  VIEW ALL ──────────────────────────────────────────────────────
    public static void viewAll() {
        if (count == 0) {
            System.out.println("No medicines in inventory.");
            return;
        }

        System.out.println("\n" + "=".repeat(85));
        System.out.printf("%-5s %-18s %-14s %-7s %-12s %-6s %-14s %-8s%n",
                "ID", "Name", "Company", "Price", "Expiry", "Qty", "Formula", "Rx");
        System.out.println("=".repeat(85));

        for (int i = 0; i < count; i++) {
            Medicine m = inventory[i];
            System.out.printf("%-5d %-18s %-14s %-7.2f %-12s %-6d %-14s %-8s%n",
                    m.getId(),
                    m.getName(),
                    m.getCompany(),
                    m.getPrice(),
                    m.getExpiryDate(),
                    m.getQuantity(),
                    m.getFormulaCategory(),
                    m.isPrescriptionRequired() ? "Yes" : "No");

            // Warn inline if this specific medicine is low on stock
            if (m.getQuantity() <= m.getMinStockThreshold()) {
                System.out.println("   *** LOW STOCK WARNING ***");
            }
        }
        System.out.println("=".repeat(85));
        System.out.println("Total medicines: " + count);
    }

    //  DISPLAY SINGLE ────────────────────────────────────────────────
    public void display() {
        System.out.println("-----------------------------");
        System.out.println("ID          : " + id);
        System.out.println("Name        : " + name);
        System.out.println("Company     : " + company);
        System.out.println("Price       : Rs. " + price);
        System.out.println("Expiry      : " + expiryDate);
        System.out.println("Quantity    : " + quantity);
        System.out.println("Min Stock   : " + minStockThreshold);
        System.out.println("Formula     : " + formulaCategory);
        System.out.println("Prescription: " + (prescriptionRequired ? "Required" : "Not Required"));
        System.out.println("-----------------------------");
    }

    //  TO CSV STRING ─────────────────────────────────────────────────
    // Used by FileManager to write each medicine as one line in the inventory file
    public String toCsvString() {
        return id + "," + name + "," + company + "," + price + "," +
               expiryDate + "," + quantity + "," + minStockThreshold + "," +
               formulaCategory + "," + prescriptionRequired;
    }

    //  FROM CSV STRING ───────────────────────────────────────────────
    // Used by FileManager to rebuild a Medicine object from a saved line
    public static Medicine fromCsvString(String line) {
        String[] parts = line.split(",");

        // Guard against corrupted or incomplete lines in the file
        if (parts.length < 9) return null;

        int id                  = Integer.parseInt(parts[0].trim());
        String name             = parts[1].trim();
        String company          = parts[2].trim();
        double price            = Double.parseDouble(parts[3].trim());
        String expiryDate       = parts[4].trim();
        int quantity            = Integer.parseInt(parts[5].trim());
        int minThreshold        = Integer.parseInt(parts[6].trim());
        String formulaCategory  = parts[7].trim();
        boolean prescription    = Boolean.parseBoolean(parts[8].trim());

        return new Medicine(id, name, company, price, expiryDate,
                            quantity, minThreshold, formulaCategory, prescription);
    }
}