 
public class AlertSystem {
    
    // Expiry warning will be given 7 days before expiry
    private int expiryWarningDays;
    
    private String[] alertHistory;
    private int alertCount;
    private static final int MAX_ALERTS = 100;
 
    // AlerSystem with default settings:
    public AlertSystem() {
        this.expiryWarningDays = 7;
        this.alertHistory = new String[MAX_ALERTS];
        this.alertCount = 0;
    }

    public AlertSystem(int expiryWarningDays) {
        this.expiryWarningDays = expiryWarningDays;
        this.alertHistory = new String[MAX_ALERTS];
        this.alertCount = 0;
    }
    
    public int getExpiryWarningDays(){
        return expiryWarningDays;}
    public void setExpiryWarningDays(int days){
        this.expiryWarningDays = days;}
    public int getAlertCount(){
        return alertCount;}
 
    public String[] getAlertHistory(){
        String[] trimmed = new String[alertCount];
        for (int i = 0; i < alertCount; i++){
            trimmed[i] = alertHistory[i];
        }
        return trimmed;
    }

    private void addAlert(String message){
        if (alertCount < MAX_ALERTS) {
            alertHistory[alertCount] = message;
            alertCount++;
        } else {
            System.out.println("Alert log full");
        }
    }
 
    public void checkAllOnStartup(){
 
        // Will clear alerts from a previous run in the same session
        alertCount = 0;
        for (int i = 0; i < MAX_ALERTS; i++) {
            alertHistory[i] = null;
        }
 
        System.out.println("Running startup checks");
 
        checkExpiry(); // will check for expired medicines or medicines that are expiring soon
        checkLowStock();
 
        // Only display and save if there is actually something to report
        if (alertCount > 0){
            showAlerts();
            saveAlertHistory();
        } else {
            System.out.println("No alerts for today.");
        }
    }
    
    public void checkExpiry(){
        Medicine[] inventory= Medicine.getInventory();
        int count= Medicine.getCount();
        int expiredCount= 0;
        int expiringSoonCount= 0;
 
        for (int i = 0; i < count; i++){
            Medicine m = inventory[i];
            String expiryDate = m.getExpiryDate();
            String medicineName = m.getName();
 
            // Already expired medicines
            if (DateHelper.isExpired(expiryDate)) {
                String alert = "EXPIRED! " + medicineName+ " (ID: " + m.getId() + ")"+ " | expired on " + DateHelper.formatForDisplay(expiryDate);
                addAlert(alert);
                expiredCount++;
 
            // Medicines expiring soon
            } else if (DateHelper.isExpiringSoon(expiryDate, expiryWarningDays)) {
                long daysLeft = DateHelper.daysUntilExpiry(expiryDate);
                String alert  = "EXPIRING SOON! " + medicineName+ " (ID: " + m.getId() + ")"+ " | expires in " + daysLeft + " day(s)"+ " on " + DateHelper.formatForDisplay(expiryDate);
                addAlert(alert);
                expiringSoonCount++;
            }
        }
        System.out.println("Expiry check complete. "+ expiredCount + " expired, "+ expiringSoonCount + " expiring within " + expiryWarningDays + " days.");
    }

    public void checkLowStock(){
        Medicine[] inventory= Medicine.getInventory();
        int count= Medicine.getCount();
        int lowStockCount = 0;
 
        for (int i = 0; i < count; i++){
            Medicine m = inventory[i];
            
            if (m.getQuantity() <= m.getMinStockThreshold()) {
                String alert = "LOW STOCK!!! " + m.getName()+ " (ID: " + m.getId() + ")"+ " , only " + m.getQuantity() + " unit(s) left"+ " (min threshold: " + m.getMinStockThreshold() + ")";
                addAlert(alert);
                lowStockCount++;
            }
        }
        System.out.println(" Stock check complete. "
            + lowStockCount + " medicine(s) are below minimum threshold.");
    }

    // All alerts are printed together
    public void showAlerts(){
        System.out.println("\n" + "=".repeat(65));
        System.out.println("  * PHARMACY ALERT REPORT" + DateHelper.todayAsString() + " *");
        System.out.println("=".repeat(65));
 
        for (int i = 0; i < alertCount; i++){
            System.out.println("  " + alertHistory[i]);
        }
 
        System.out.println("=".repeat(65));
        System.out.println("  Total alerts: " + alertCount);
        System.out.println("=".repeat(65) + "\n");
    }
    
    // Alerts will be stored in FileManager.logAlert()
    public void saveAlertHistory(){
        try {
            for (int i = 0; i < alertCount; i++){
                FileManager.logAlert(alertHistory[i]);
            }
            System.out.println(+ alertCount + " alert(s) saved to logAlert file.");
        } catch (Exception e){
            System.out.println("Could not save alert history: " + e.getMessage());
        }
    }
}