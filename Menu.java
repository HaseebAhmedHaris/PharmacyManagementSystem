
import java.util.Scanner;

// Contains : Login screen, startup menu, shutdown menu, admin/staff menu
public class Menu {
    
    private static Scanner scanner = new Scanner(System.in);
    private static Medicine[] inventory = Medicine.getInventory();
    private static User currentUser = null; // null until login is successfull
    private static AlertSystem alertSystem = new AlertSystem(7);
    private static Sales sales = new Sales();
    
    
    //  Main has login, startup, main menu, and shutdown
public static void startConsole() {
     
        showLoginScreen();
 
        if(currentUser == null) {
            System.out.println("Exiting system...");
            scanner.close();
            return;
        }
        startup();
        showMainMenu();
        shutdown();
    }
    
    private static void showLoginScreen() {
 
        // I've put 4 default members
        User[] users= {
            new Admin("Limitless","admin123"),new Admin("Ahmed","owner123"),new Staff("staff1","staff123"),new Staff("staff2","staff456")
        };
        System.out.println("============================================");
        System.out.println("     WElcome To Pharmacy Management System     ");
        System.out.println("      PHARMACY MANAGEMENT SYSTEM - LOGIN     ");
        System.out.println("============================================");
 
        int attempts = 0;
        int maxAttempts = 3; // 3 attempts to login
 
        while (attempts < maxAttempts) {
            System.out.print("Username: ");
            String username = scanner.nextLine().trim();
 
            System.out.print("Password: ");
            String password = scanner.nextLine().trim();
            
            for (User u : users) {
                if (u.getUsername().equals(username) && u.getPassword().equals(password)) {
                    currentUser = u;
                    break;
                }
            }
 
            if (currentUser != null){
                System.out.println("\nLogin successful. Welcome, " + currentUser.getUsername()+ "! (" + currentUser.getRole() + ") \n");
                return;
            }
 
            attempts++;
            int remaining = maxAttempts - attempts;
            System.out.println("Incorrect username or password.");
            if (remaining > 0) {
                System.out.println("Attempts remaining: " + remaining);
            }
        }
 
        System.out.println("3 failed attempts. Access denied.");
    }
    
   private static void startup(){
    FileManager.initializeFolders();
    FileManager.loadInventory();

    // First run only — if no file existed, load the hardcoded medicines
    if (Medicine.getCount() == 0) {
        DataLoader.loadDefaultMedicines();
        FileManager.saveInventory();
        Medicine.resetIdCounter();
    }

    inventory = Medicine.getInventory();
    alertSystem.checkAllOnStartup();
    System.out.println("\n System is ready! \n");
}
    
    private static void shutdown() {
        System.out.println("\nSaving data...");
        FileManager.saveInventory();
        FileManager.generateBackup();
        System.out.println("Backup created. Goodbye, " + currentUser.getUsername() + "!");
        scanner.close();
    }
    
    private static void showMainMenu() {
        boolean running = true;
 
        while (running){
            
            if (currentUser.getRole().equals("Admin")){
                showAdminMenu();
            }
            else{
                showStaffMenu();
            }
            int choice = readIntInput("Enter choice: ");
            running = handleInput(choice);
        }
    }
    
    private static void showAdminMenu() {
        
        System.out.println("\nMAIN MENU  (ADMIN)\n");
        System.out.println(" INVENTORY");    
        System.out.println(" 1. View All Medicines");
        System.out.println(" 2. Add Medicine");
        System.out.println(" 3. Search Medicine");
        System.out.println(" 4. Update Medicine");
        System.out.println(" 5. Delete Medicine");
        System.out.println(" ALERTS");
        System.out.println(" 6. View Alert Report");
        System.out.println(" 7. Re-run Alert Checks");
        System.out.println(" REPORTS");
        System.out.println(" 8. View Sales Log");
        System.out.println(" 9. View Activity Log");
        System.out.println(" SALES");
        System.out.println(" 10. New Sale");
        System.out.println(" 11. Process Refund");
        System.out.println(" 12. Sales Reports");
        System.out.println("\n 0. Exit \n");
    }
    
    private static void showStaffMenu() {
        
        System.out.println("\n MAIN MENU  (STAFF) \n");
        System.out.println("  INVENTORY");
        System.out.println(" 1. View All Medicines");
        System.out.println(" 2. Add Medicine");
        System.out.println(" 3. Search Medicine");
        System.out.println(" 4. Update Medicine");
        System.out.println(" ALERTS");
        System.out.println(" 6. View Alert Report");
        System.out.println(" SALES");
        System.out.println(" 10. New Sale");
        System.out.println("\n 0. Exit\n");
    }
    
    //  false when the user chooses 0 to exit.
    //  admin-only options are not accessable to staff
    private static boolean handleInput(int choice) {
 
        boolean isAdmin = currentUser.getRole().equals("Admin");
        
        switch (choice) {
 
            case 1:
                Medicine.viewAll();break;
 
            case 2:
                addMedicineFlow();break;
 
            case 3:
                searchMedicineFlow();break;
 
            case 4:
                updateMedicineFlow();break;
 
            case 5:
                if (isAdmin){
                    deleteMedicineFlow();
                }else {
                    System.out.println("Access denied. This is only accessable to Admin.");
                }break;
 
            case 6:
                if (alertSystem.getAlertCount() == 0) {
                    System.out.println("No alerts at the moment.");
                } else {
                    alertSystem.showAlerts();
                }break;
 
            case 7:
                if (isAdmin) {
                    alertSystem.checkAllOnStartup();
                } else {
                    System.out.println("Access denied. This is only accessable to Admin.");
                }break;
 
            case 8:
                if (isAdmin) {
                    FileManager.printSalesLog();
                } else {
                    System.out.println("Access denied. This is only accessable to Admin.");
                }break;
 
            case 9:
                if (isAdmin) {
                    FileManager.printActivityLog();
                } else {
                    System.out.println("Access denied. This is only accessable to Admin.");
                }break;
 
            case 10:
                 int medId = readIntInput("Enter Medicine ID: ");
                int qty   = readIntInput("Enter Quantity: ");
                Bill bill = sales.sellMedicine(medId, qty, currentUser.getUsername());

                // After sale, admin can optionally apply a discount
                if (bill != null && isAdmin) {
                System.out.print("Apply discount? (yes/no): ");
                String discInput = scanner.nextLine().trim().toLowerCase();
                if (discInput.equals("yes") || discInput.equals("y")) {
                    double disc = readDoubleInput("Discount %: ");
                    sales.applyDiscount(bill.getBillID(), disc);
        }
    }
                FileManager.saveInventory();
                break;
              
            case 11:
                if (isAdmin) {
                int refundId = readIntInput("Enter Bill ID to refund: ");
                sales.processRefund(refundId);
            FileManager.saveInventory();
            } else {
                System.out.println("Access denied. Admin only.");
    }
            break;

case 12:
    if (isAdmin) {
        System.out.println("\n 1. Daily  2. Weekly  3. Monthly  4. Best Seller  5. Profit/Loss");
        int rep = readIntInput("Choice: ");
        String today = java.time.LocalDate.now()
                       .format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        if (rep == 1) sales.getDailySummary(today);
        else if (rep == 2) sales.getWeeklySummary();
        else if (rep == 3) sales.getMonthlySummary();
        else if (rep == 4) sales.getBestSellingMedicine();
        else if (rep == 5) sales.calculateProfitLoss();
        else System.out.println("Invalid choice.");
    } else {
        System.out.println("Access denied. Admin only.");
    }
    break;    
 
            case 0:
                return false; // it will stop the loop
 
            default:
                System.out.println("Invalid choice. Try again.");
        }
        return true; // menu will keep running
    }
    
    // Methods from now on will collect input from the user and pass it to Medicine class.

    // This will pass the info to Medicine.addMedicine()
    private static void addMedicineFlow() {
        System.out.println("\nAdd New Medicine \n");
        System.out.print("Name : ");
        String name = scanner.nextLine().trim();
        System.out.print("Company : ");
        String company = scanner.nextLine().trim();
        double price     = readDoubleInput("Price(Rs.): ");
        System.out.print("Expiry (dd/MM/yyyy) e.g. 01/12/2027 : ");
        String expiry = scanner.nextLine().trim();
        int quantity     = readIntInput("Quantity: ");
        int minThreshold = readIntInput("Min Stock Alert: ");
        System.out.print("Formula Category : ");
        String formula = scanner.nextLine().trim();
        System.out.print("Prescription? (yes/no): ");
        String rxInput = scanner.nextLine().trim().toLowerCase();
        boolean rx = rxInput.equals("yes") || rxInput.equals("y");
 
        Medicine m = new Medicine(name, company, price, expiry, quantity, minThreshold, formula, rx);
        Medicine.addMedicine(m);
        FileManager.logActivity(currentUser.getUsername()+ " added medicine: " + name + " (ID: " + m.getId() + ")");
    }
    
    private static void searchMedicineFlow() {
        System.out.println("\n Search Medicine \n");
        System.out.println(" 1. Search by Name");
        System.out.println(" 2. Search by ID");
        int choice = readIntInput("Choice: ");
        
        if (choice == 1) {
            System.out.print("Enter name keyword: ");
            String keyword = scanner.nextLine().trim();
            Medicine[] results = Medicine.searchByName(keyword);
 
            if (results.length == 0) {
                System.out.println("No medicines found for: " + keyword);
            }
            else{
                System.out.println(results.length + " result(s):");
                for (int i = 0; i < results.length; i++) {
                    results[i].display();
                }
            }
 
        }
        else if (choice == 2){
            int id = readIntInput("Enter Medicine ID: ");
            Medicine m = Medicine.searchById(id);
            if (m == null) {
                System.out.println("No medicine found with ID " + id);
            }else{
                m.display();
            }
        }
        else{
            System.out.println("Invalid choice.");
        }
    }
    
    private static void updateMedicineFlow() {
        int id = readIntInput("\nEnter ID of medicine to update: ");
        Medicine m = Medicine.getMedicineById(id);
 
        if (m == null) {
            System.out.println("Medicine with ID " + id + " not found.");
            return;
        }
        System.out.println("Updating: " + m.getName() + " (press Enter to save the value)");
        
        System.out.print("New Price (current: " + m.getPrice() + "): ");
        String priceInput = scanner.nextLine().trim();
        if (!priceInput.isEmpty()) {
            try {
                m.setPrice(Double.parseDouble(priceInput));
            } catch (NumberFormatException e) {
                System.out.println("Invalid price. Current value cannot be changed.");
            }
        }
 
        System.out.print("New Quantity (current: " + m.getQuantity() + "): ");
        String qtyInput = scanner.nextLine().trim();
        if (!qtyInput.isEmpty()) {
            try {
                m.setQuantity(Integer.parseInt(qtyInput));
            } catch (NumberFormatException e) {
                System.out.println("Invalid quantity. Current value cannot be changed.");
            }
        }
 
        System.out.println("\n Medicine updated successfully! \n");
        
        FileManager.logActivity(currentUser.getUsername()+ " updated medicine ID: " + id);
    }
 
    // Admin only option
    private static void deleteMedicineFlow() {
        int id = readIntInput("\nEnter ID of medicine to delete: ");
        Medicine m = Medicine.getMedicineById(id);
 
        if(m == null){
            System.out.println("Medicine with ID " + id + " not found.");
            return;
        }
        System.out.print("Delete '" + m.getName() + "'? (yes/no): ");
        String confirm = scanner.nextLine().trim().toLowerCase();
 
        if(confirm.equals("yes") || confirm.equals("y")){
            String name = m.getName();
            Medicine.deleteMedicine(id);
            FileManager.logActivity(currentUser.getUsername()+ " deleted medicine: " + name + " (ID: " + id + ")");
        }
        else{
            System.out.println("Deletion cancelled.");
        }
    }
 
    private static int readIntInput(String prompt){
        while(true){
            System.out.print(prompt);
            try{
                return Integer.parseInt(scanner.nextLine().trim());
            } catch(NumberFormatException e){
                System.out.println("ERROR! Please enter a whole number.");
            }
        }
    }
 
    private static double readDoubleInput(String prompt) {
        while(true){
            System.out.print(prompt);
            try{
                return Double.parseDouble(scanner.nextLine().trim());
            }catch(NumberFormatException e) {
                System.out.println("ERROR! Please enter a valid number");
            }
        }
    }
}

