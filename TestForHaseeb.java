public class TestForHaseeb {
    public static void main(String[] args) {

        FileManager.initializeFolders();
        FileManager.loadInventory();

        // Only load defaults if nothing came from file
        if (Medicine.getCount() == 0) {
            DataLoader.loadDefaultMedicines();
            FileManager.saveInventory();
            Medicine.resetIdCounter();
        }

        Medicine.viewAll();

        // Test search
        System.out.println("\nSearch 'pan':");
        Medicine[] found = Medicine.searchByName("pan");
        for (Medicine m : found) m.display();

        // Test alternatives for Paracetamol (excluding Panadol ID 1001)
        System.out.println("\nAlternatives for Paracetamol (excluding ID 1001):");
        Medicine[] alts = Medicine.findAlternatives("Paracetamol", 1001);
        for (Medicine m : alts) m.display();

        FileManager.generateBackup();
        FileManager.logActivity("Test run completed");

        System.out.println("\nAll done.");
    }
}