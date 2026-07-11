import javax.swing.SwingUtilities;
import java.util.Scanner;

public class Launcher {

    public static void main(String[] args) {

        // Load all data first — same for both modes
        FileManager.initializeFolders();
        FileManager.loadInventory();
        if (Medicine.getCount() == 0) {
            DataLoader.loadDefaultMedicines();
            FileManager.saveInventory();
            Medicine.resetIdCounter();
        }

        // Ask which mode to launch
        System.out.println("============================================");
        System.out.println("     PHARMACY MANAGEMENT SYSTEM             ");
        System.out.println("============================================");
        System.out.println(" 1. Console Mode");
        System.out.println(" 2. GUI Mode");
        System.out.print(" Select mode: ");

        Scanner sc = new Scanner(System.in);
        String choice = sc.nextLine().trim();

        if (choice.equals("2")) {
            System.out.println("Launching GUI...");
            SwingUtilities.invokeLater(() -> new PharmacyGUI().setVisible(true));
        } else {
            System.out.println("Launching Console...");
            Menu.startConsole();
        }
    }
}