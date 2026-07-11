// Admin extends User and gets full access to the system
// Can change prices, delete medicines, view logs, apply discounts

public class Admin extends User {

    public Admin(String username, String password) {
        super(username, password, "Admin");
    }

    @Override
    public void showCapabilities() {
        System.out.println("Admin Access — Full Control:");
        System.out.println("  1.  View, Add, Update, Delete medicines");
        System.out.println("  2.  Change medicine prices");
        System.out.println("  3.  Apply discounts and process refunds");
        System.out.println("  4.  View sales reports and activity log");
        System.out.println("  5.  Re-run alert checks");
    }

    // Changes the price of a medicine and logs the action
    public void changePrice(int medicineId, double newPrice) {
        Medicine m = Medicine.getMedicineById(medicineId);
        if (m == null) {
            System.out.println("Medicine ID " + medicineId + " not found.");
            return;
        }
        double oldPrice = m.getPrice();
        m.setPrice(newPrice);
        FileManager.saveInventory();
        FileManager.logActivity(getUsername() + " changed price of " +
            m.getName() + " from Rs." + oldPrice + " to Rs." + newPrice);
        System.out.println("Price updated successfully.");
    }

    // Views the full activity log — admin only feature
    public void viewActivityLog() {
        FileManager.printActivityLog();
    }
}