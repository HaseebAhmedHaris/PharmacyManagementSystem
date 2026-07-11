import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * Manages all sales operations: selling, discounting, refunding,
 * and generating summaries. Works directly with Medicine and FileManager.
 * Keeps an in‑memory list of all transactions for quick reporting.
 */
public class Sales {

    // --------------------- FIELDS --------------------------
    private ArrayList<Bill> salesLog;
    private double totalRevenue;      // Sum of all net sale amounts
    private double totalDiscount;     // Total discount given across all bills
    private double totalRefunded;     // Money refunded (added back to stock)

    // --------------------- CONSTRUCTOR ---------------------
    public Sales() {
        salesLog = new ArrayList<>();
        totalRevenue = 0;
        totalDiscount = 0;
        totalRefunded = 0;
        // Load existing sales from file
        loadSalesHistory();
    }

    // --------------------- FILE LOADING --------------------
    /**
     * Reads sales_log.txt line by line, extracts the CSV part
     * (after the timestamp) and rebuilds the Bill list.
     * NOTE: Ideally this logic belongs in FileManager, but we include it
     * here for completeness. Ask the team lead to move it to FileManager later.
     */
    private void loadSalesHistory() {
        File file = new File("data/sales_log.txt");
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                // Log format: "timestamp | billCsv"
                int separatorIndex = line.indexOf(" | ");
                if (separatorIndex == -1) continue; // skip malformed lines
                String billCsv = line.substring(separatorIndex + 3);
                Bill bill = Bill.fromCsvString(billCsv);
                if (bill != null) {
                    salesLog.add(bill);
                    if (bill.isRefunded()) {
                        totalRefunded += bill.getTotalAmount();
                    } else {
                        totalRevenue += bill.getTotalAmount();
                    }
                    totalDiscount += (bill.getPricePerUnit() * bill.getQuantitySold()
                            * bill.getDiscountPercent() / 100.0);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading sales history: " + e.getMessage());
        }
    }

    // --------------------- CORE: SELL MEDICINE -------------
    /**
     * Sells a given quantity of a medicine. Reduces stock, creates a bill,
     * logs the sale, and prints the receipt.
     * @param medicineID   ID of the medicine to sell
     * @param quantity     Quantity to sell
     * @param soldBy       Name of the staff/admin processing the sale
     * @return             The generated Bill if successful, null otherwise
     */
    public Bill sellMedicine(int medicineID, int quantity, String soldBy) {
        // 1. Find the medicine
        Medicine med = Medicine.searchById(medicineID);
        if (med == null) {
            System.out.println("Error: Medicine with ID " + medicineID + " not found.");
            return null;
        }

        // 2. Check stock
        if (med.getQuantity() < quantity) {
            System.out.println("Error: Insufficient stock. Available: " + med.getQuantity());
            return null;
        }
        if (quantity <= 0) {
            System.out.println("Error: Quantity must be positive.");
            return null;
        }

        // 3. Check expiry — block sale of expired medicines
        if (DateHelper.isExpired(med.getExpiryDate())) {
            System.out.println("Error: Cannot sell expired medicine. " 
            + med.getName() + " expired on " + med.getExpiryDate());
        return null;
        }

        // 3. Create the bill (no discount by default)
        Bill bill = new Bill(medicineID, med.getName(), quantity,
                med.getPrice(), 0.0, soldBy);

        // 4. Reduce stock
        med.setQuantity(med.getQuantity() - quantity);

        // 5. Update revenue & discount
        totalRevenue += bill.getTotalAmount();

        // 6. Save to internal log & file
        salesLog.add(bill);
        FileManager.saveInventory();  // stock changed
        FileManager.logSale(bill.toCsvString());  // store with timestamp

        // 7. Print receipt
        bill.printBill();

        return bill;
    }

    // --------------------- APPLY DISCOUNT ------------------
    /**
     * Applies a percentage discount to an existing bill (admin only).
     * Updates revenue, logs the action.
     * @param billID         ID of the bill to modify
     * @param discountPercent  New discount percentage (0‑100)
     * @return               True if successful, false if bill not found
     */
    public boolean applyDiscount(int billID, double discountPercent) {
        Bill bill = findBillByID(billID);
        if (bill == null) {
            System.out.println("Error: Bill ID " + billID + " not found.");
            return false;
        }
        if (bill.isRefunded()) {
            System.out.println("Error: Cannot discount a refunded bill.");
            return false;
        }
        if (discountPercent < 0 || discountPercent > 100) {
            System.out.println("Error: Discount must be between 0 and 100.");
            return false;
        }

        double oldTotal = bill.getTotalAmount();
        bill.setDiscountPercent(discountPercent);
        double newTotal = bill.getTotalAmount();

        // Adjust revenue (remove old, add new)
        totalRevenue = totalRevenue - oldTotal + newTotal;
        // Adjust discount tracking
        double subtotal = bill.getPricePerUnit() * bill.getQuantitySold();
        totalDiscount = totalDiscount - (oldTotal>0 ? (subtotal - oldTotal) : 0)
                + (newTotal>0 ? (subtotal - newTotal) : 0);

        // Save inventory (no stock change) but log activity
        FileManager.logActivity("Discount applied: Bill " + billID
                + " (" + bill.getMedicineName() + ") now at "
                + discountPercent + "%");
        System.out.println("Discount applied. New total: Rs. " + newTotal);
        return true;
    }

    // --------------------- PROCESS REFUND ------------------
    /**
     * Refunds a sale: restores stock, marks bill as refunded,
     * deducts amount from revenue and logs everything.
     * @param billID  ID of the bill to refund
     * @return        True if successful
     */
    public boolean processRefund(int billID) {
        Bill bill = findBillByID(billID);
        if (bill == null) {
            System.out.println("Error: Bill ID " + billID + " not found.");
            return false;
        }
        if (bill.isRefunded()) {
            System.out.println("Error: Bill has already been refunded.");
            return false;
        }

        // Restore stock
        Medicine med = Medicine.searchById(bill.getMedicineID());
        if (med == null) {
            System.out.println("Error: Medicine record missing. Cannot restore stock.");
            return false;
        }
        med.setQuantity(med.getQuantity() + bill.getQuantitySold());

        // Mark as refunded
        bill.setRefunded(true);

        // Update financials
        totalRevenue -= bill.getTotalAmount();
        totalRefunded += bill.getTotalAmount();
        double discountGiven = bill.getPricePerUnit() * bill.getQuantitySold()
                * bill.getDiscountPercent() / 100.0;
        totalDiscount -= discountGiven;  // remove discount from stats

        // Save inventory & log
        FileManager.saveInventory();
        FileManager.logActivity("Refund processed: Bill " + billID
                + " (" + bill.getMedicineName() + ") amount Rs. "
                + bill.getTotalAmount());

        System.out.println("Refund successful. Stock restored.");
        return true;
    }

    // --------------------- REPORTING -----------------------
    /** Prints summary of all sales for a specific date (dd/MM/yyyy). */
    public void getDailySummary(String dateStr) {
        double dailyRevenue = 0;
        int count = 0;
        for (Bill b : salesLog) {
            if (b.getDateOfSale().startsWith(dateStr) && !b.isRefunded()) {
                dailyRevenue += b.getTotalAmount();
                count++;
            }
        }
        System.out.println("\n===== DAILY SUMMARY: " + dateStr + " =====");
        System.out.println("Transactions: " + count);
        System.out.println("Revenue: Rs. " + dailyRevenue);
    }

    /** Prints summary for the last 7 days. */
    public void getWeeklySummary() {
        LocalDate today = LocalDate.now();
        LocalDate weekAgo = today.minusDays(7);
        double revenue = 0;
        int count = 0;
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        for (Bill b : salesLog) {
            if (b.isRefunded()) continue;
            try {
                LocalDate saleDate = LocalDate.parse(
                        b.getDateOfSale().substring(0, 10), fmt);
                if (!saleDate.isBefore(weekAgo) && !saleDate.isAfter(today)) {
                    revenue += b.getTotalAmount();
                    count++;
                }
            } catch (Exception e) { /* skip malformed dates */ }
        }
        System.out.println("\n===== WEEKLY SUMMARY (last 7 days) =====");
        System.out.println("Transactions: " + count);
        System.out.println("Revenue: Rs. " + revenue);
    }

    /** Prints summary for the current month. */
    public void getMonthlySummary() {
        LocalDate today = LocalDate.now();
        int currentMonth = today.getMonthValue();
        int currentYear = today.getYear();
        double revenue = 0;
        int count = 0;
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        for (Bill b : salesLog) {
            if (b.isRefunded()) continue;
            try {
                LocalDate saleDate = LocalDate.parse(
                        b.getDateOfSale().substring(0, 10), fmt);
                if (saleDate.getMonthValue() == currentMonth
                        && saleDate.getYear() == currentYear) {
                    revenue += b.getTotalAmount();
                    count++;
                }
            } catch (Exception e) { }
        }
        System.out.println("\n===== MONTHLY SUMMARY =====");
        System.out.println("Transactions: " + count);
        System.out.println("Revenue: Rs. " + revenue);
    }

    /** Identifies the medicine that sold the most units overall. */
    public void getBestSellingMedicine() {
        if (salesLog.isEmpty()) {
            System.out.println("No sales data available.");
            return;
        }
        // Aggregate quantity per medicine name
        ArrayList<String> names = new ArrayList<>();
        ArrayList<Integer> qtys = new ArrayList<>();
        for (Bill b : salesLog) {
            if (b.isRefunded()) continue;
            String name = b.getMedicineName();
            int idx = names.indexOf(name);
            if (idx == -1) {
                names.add(name);
                qtys.add(b.getQuantitySold());
            } else {
                qtys.set(idx, qtys.get(idx) + b.getQuantitySold());
            }
        }
        // Find max
        int maxIdx = 0;
        for (int i = 1; i < names.size(); i++) {
            if (qtys.get(i) > qtys.get(maxIdx)) {
                maxIdx = i;
            }
        }
        System.out.println("\n===== BEST SELLING MEDICINE =====");
        System.out.println(names.get(maxIdx) + " – " + qtys.get(maxIdx) + " units sold");
    }

    /** Displays profit/loss overview: revenue, discounts given, refunds. */
    public void calculateProfitLoss() {
        System.out.println("\n===== PROFIT & LOSS STATEMENT =====");
        System.out.printf("%-25s Rs. %.2f%n", "Total Revenue:", totalRevenue);
        System.out.printf("%-25s Rs. %.2f%n", "Total Discounts Given:", totalDiscount);
        System.out.printf("%-25s Rs. %.2f%n", "Total Refunded:", totalRefunded);
        double net = totalRevenue - totalRefunded;   // discounts already reduced revenue
        System.out.printf("%-25s Rs. %.2f%n", "Net Earnings:", net);
    }

    /** Prints the entire sales log to the console. */
    public void viewSalesLog() {
        if (salesLog.isEmpty()) {
            System.out.println("No sales recorded yet.");
            return;
        }
        System.out.println("\n========== FULL SALES LOG ==========");
        for (Bill b : salesLog) {
            System.out.printf("Bill #%d | %s | Qty: %d | Total: Rs. %.2f | %s %s%n",
                    b.getBillID(), b.getMedicineName(), b.getQuantitySold(),
                    b.getTotalAmount(), b.getDateOfSale(),
                    b.isRefunded() ? "[REFUNDED]" : "");
        }
        System.out.println("====================================");
    }

    // --------------------- HELPER METHODS ------------------
    private Bill findBillByID(int billID) {
        for (Bill b : salesLog) {
            if (b.getBillID() == billID) return b;
        }
        return null;
    }

    // Getters for Menu / Admin use
    public double getTotalRevenue() { return totalRevenue; }
    public ArrayList<Bill> getSalesLog() { return salesLog; }
}
