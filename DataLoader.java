public class DataLoader {

    /*
     * Hardcoded medicine data from team documentation.
     * Split into 3 categories 
     *   - Daily Use & Bulk Inventory     
     *   - High Risk & Prescription Only  
     *   - Chronic Care & Specialist      
     *
     * This method is called ONLY if the inventory file doesn't exist yet.  (TEMPORARY: for testing, we can call it from main() to load the data every time)
     * Once loaded, FileManager saves them to file and they persist from there.
     */
    public static void loadDefaultMedicines() {

        System.out.println("Loading default medicine data...");

        // ── CATEGORY 1: Daily Use & Bulk Inventory ────────────────────
        // These are over-the-counter medicines — no prescription needed

        Medicine.addMedicine(new Medicine(
            1001, "Panadol", "GSK",
            580.0, "01/12/2027",
            2500, 500,
            "Paracetamol", false   // formula category used for alternative suggestions
        ));

        Medicine.addMedicine(new Medicine(
            1002, "Brufen", "Abbott",
            650.0, "01/05/2027",
            1200, 300,
            "Ibuprofen", false
        ));

        Medicine.addMedicine(new Medicine(
            1003, "Disprin", "Reckitt",
            280.0, "01/11/2028",
            1500, 400,
            "Aspirin", false
        ));

        Medicine.addMedicine(new Medicine(
            1004, "Softin", "Hilton",
            350.0, "01/09/2027",
            800, 150,
            "Loratadine", false
        ));

        Medicine.addMedicine(new Medicine(
            1005, "Gaviscon", "Reckitt",
            420.0, "01/08/2026",
            500, 100,
            "Antacid", false
        ));

        Medicine.addMedicine(new Medicine(
            1006, "Arinac", "Abbott",
            400.0, "01/01/2027",
            1000, 300,
            "ColdFlu", false
        ));

        // ── CATEGORY 2: High Risk & Prescription Only ─────────────────
        // All require prescription — restricted to Admin/Staff only

        Medicine.addMedicine(new Medicine(
            2001, "Xanax", "Pfizer",
            5000.0, "01/04/2026",
            100, 30,
            "Alprazolam", true   // prescription = true
        ));

        Medicine.addMedicine(new Medicine(
            2002, "Lexotanil", "Roche",
            1200.0, "01/09/2026",
            150, 50,
            "Benzodiazepine", true
        ));

        Medicine.addMedicine(new Medicine(
            2003, "Morphine", "Searle",
            3500.0, "01/02/2026",   // NOTE: expiring very soon — triggers alert
            20, 10,
            "Opioid", true
        ));

        Medicine.addMedicine(new Medicine(
            2004, "Ritalin", "Novartis",
            2100.0, "01/11/2026",
            40, 15,
            "CNSStimulant", true
        ));

        Medicine.addMedicine(new Medicine(
            2005, "Tramadol", "Searle",
            950.0, "01/07/2026",
            200, 50,
            "Analgesic", true
        ));

        Medicine.addMedicine(new Medicine(
            2006, "Dormicum", "Roche",
            1400.0, "01/10/2026",
            60, 20,
            "Sedative", true
        ));

        // ── CATEGORY 3: Chronic Care & Specialist Medicines ──────────
        // Long-term condition medicines — all prescription required

        Medicine.addMedicine(new Medicine(
            3001, "Augmentin", "GSK",
            1550.0, "01/06/2026",
            400, 100,
            "Amoxicillin", true
        ));

        Medicine.addMedicine(new Medicine(
            3002, "Lantus", "Sanofi",
            5200.0, "01/12/2025",   // NOTE: EXPIRED — will trigger expiry alert on startup
            30, 15,
            "Insulin", true
        ));

        Medicine.addMedicine(new Medicine(
            3003, "Glucophage", "Merck",
            480.0, "01/11/2026",
            1000, 200,
            "Metformin", true
        ));

        Medicine.addMedicine(new Medicine(
            3004, "Lipitor", "Viatris",
            3200.0, "01/01/2028",
            120, 50,
            "Statin", true
        ));

        Medicine.addMedicine(new Medicine(
            3005, "Nexum", "Getz",
            1100.0, "01/03/2027",
            600, 100,
            "Omeprazole", true
        ));

        Medicine.addMedicine(new Medicine(
            3006, "Ventolin Inhaler", "GSK",
            750.0, "01/08/2027",
            150, 40,
            "Salbutamol", true
        ));

        Medicine.addMedicine(new Medicine(
            3007, "Amlodipine", "Novartis",
            880.0, "01/12/2026",
            500, 120,
            "CalciumChannelBlocker", true
        ));

        System.out.println("Default data loaded: " + Medicine.getCount() + " medicines.");
    }
}