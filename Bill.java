import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents one sale transaction of a single medicine.
 * All fields are private, accessed via getters.
 * Provides methods to generate totals, print a receipt,
 * and convert to/from CSV for file storage.
 */
public class Bill {

    // --------------------- FIELDS --------------------------
    private int billID;
    private int medicineID;          // Links back to the Medicine ID
    private String medicineName;
    private int quantitySold;
    private double pricePerUnit;     // Price at time of sale
    private double discountPercent;  // e.g. 10 means 10%
    private double totalAmount;      // Net payable after discount
    private String dateOfSale;       // dd/MM/yyyy HH:mm:ss
    private String soldBy;           // Staff/admin name
    private boolean refunded;        // True if this bill was refunded

    private static int idCounter = 1;

    // --------------------- CONSTRUCTOR ---------------------
    public Bill(int billID, int medicineID, String medicineName,
                int quantitySold, double pricePerUnit,
                double discountPercent, String dateOfSale, String soldBy) {
        this.billID = billID;
        this.medicineID = medicineID;
        this.medicineName = medicineName;
        this.quantitySold = quantitySold;
        this.pricePerUnit = pricePerUnit;
        this.discountPercent = discountPercent;
        this.dateOfSale = dateOfSale;
        this.soldBy = soldBy;
        this.refunded = false;
        calculateTotal();   // set totalAmount automatically
    }

    // Overloaded constructor that auto‑generates bill ID and timestamp
    public Bill(int medicineID, String medicineName, int quantitySold,
                double pricePerUnit, double discountPercent, String soldBy) {
        this(idCounter++, medicineID, medicineName, quantitySold,
                pricePerUnit, discountPercent,
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")),
                soldBy);
    }

    // --------------------- METHODS -------------------------

    /** Recalculate totalAmount based on unit price, quantity and discount. */
    public void calculateTotal() {
        double subtotal = pricePerUnit * quantitySold;
        totalAmount = subtotal * (1 - discountPercent / 100.0);
        // Round to 2 decimal places
        totalAmount = Math.round(totalAmount * 100.0) / 100.0;
    }

    /** Prints a formatted receipt to the console. */
    public void printBill() {
        System.out.println("\n================== PHARMACY BILL ==================");
        System.out.printf("%-20s %d%n", "Bill ID:", billID);
        System.out.printf("%-20s %s%n", "Medicine:", medicineName);
        System.out.printf("%-20s %d%n", "Quantity:", quantitySold);
        System.out.printf("%-20s Rs. %.2f%n", "Price per unit:", pricePerUnit);
        if (discountPercent > 0) {
            System.out.printf("%-20s %.2f %% %n", "Discount:", discountPercent);
        }
        System.out.printf("%-20s Rs. %.2f%n", "TOTAL:", totalAmount);
        System.out.printf("%-20s %s%n", "Date:", dateOfSale);
        System.out.printf("%-20s %s%n", "Sold by:", soldBy);
        if (refunded) {
            System.out.println("           *** REFUNDED ***");
        }
        System.out.println("====================================================");
    }

    /** Converts the bill to a CSV string (without timestamp) for logging. */
    public String toCsvString() {
        return billID + "," + medicineID + "," + medicineName + ","
                + quantitySold + "," + pricePerUnit + ","
                + discountPercent + "," + totalAmount + ","
                + dateOfSale + "," + soldBy + "," + refunded;
    }

    /**
     * Recreates a Bill object from a CSV line read from the sales log.
     * Expected format: billID,medID,name,qty,price,discount%,total,date,soldBy,refunded
     */
    public static Bill fromCsvString(String csv) {
        String[] parts = csv.split(",");
        if (parts.length < 10) return null;

        int billID = Integer.parseInt(parts[0].trim());
        int medID = Integer.parseInt(parts[1].trim());
        String name = parts[2].trim();
        int qty = Integer.parseInt(parts[3].trim());
        double price = Double.parseDouble(parts[4].trim());
        double disc = Double.parseDouble(parts[5].trim());
        double total = Double.parseDouble(parts[6].trim());
        String date = parts[7].trim();
        String seller = parts[8].trim();
        boolean refund = Boolean.parseBoolean(parts[9].trim());

        Bill bill = new Bill(billID, medID, name, qty, price, disc, date, seller);
        bill.totalAmount = total;   // preserve exact stored total
        bill.refunded = refund;
        // Update static counter so next auto ID stays unique
        if (billID >= idCounter) {
            idCounter = billID + 1;
        }
        return bill;
    }

    // --------------------- GETTERS / SETTERS ----------------
    public int getBillID() { return billID; }
    public int getMedicineID() { return medicineID; }
    public String getMedicineName() { return medicineName; }
    public int getQuantitySold() { return quantitySold; }
    public double getPricePerUnit() { return pricePerUnit; }
    public double getDiscountPercent() { return discountPercent; }
    public double getTotalAmount() { return totalAmount; }
    public String getDateOfSale() { return dateOfSale; }
    public String getSoldBy() { return soldBy; }
    public boolean isRefunded() { return refunded; }

    public void setDiscountPercent(double discountPercent) {
        this.discountPercent = discountPercent;
        calculateTotal();   // total changes when discount changes
    }
    public void setRefunded(boolean refunded) { this.refunded = refunded; }
}