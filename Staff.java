// Staff extends User and gets restricted access
// Can sell medicines and view inventory but cannot delete or change prices
public class Staff extends User {

    public Staff(String username, String password) {
        super(username, password, "Staff");
    }

    @Override
    public void showCapabilities() {
        System.out.println("Staff Access — Restricted:");
        System.out.println("  1.  View and search medicines");
        System.out.println("  2.  Add and update medicines");
        System.out.println("  3.  Sell medicines and generate bills");
        System.out.println("  4.  View alert reports");
    }
}