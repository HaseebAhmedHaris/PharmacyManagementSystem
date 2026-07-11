// Base class for all users in the system
// Admin and Staff both extend this class
public abstract class User {

    private String username;
    private String password;
    private String role;
    private int loginAttempts;
    private static final int MAX_ATTEMPTS = 3;

    public User(String username, String password, String role) {
        this.username     = username;
        this.password     = password;
        this.role         = role;
        this.loginAttempts = 0;
    }

    // Validates entered credentials against stored ones
    // Tracks failed attempts and locks after MAX_ATTEMPTS
    public boolean login(String enteredUsername, String enteredPassword) {
        if (loginAttempts >= MAX_ATTEMPTS) {
            System.out.println("Account locked after 3 failed attempts.");
            return false;
        }
        if (this.username.equals(enteredUsername) &&
            this.password.equals(enteredPassword)) {
            loginAttempts = 0;
            return true;
        }
        loginAttempts++;
        System.out.println("Incorrect credentials. Attempts remaining: " +
                           (MAX_ATTEMPTS - loginAttempts));
        return false;
    }

    public boolean isLocked() {
        return loginAttempts >= MAX_ATTEMPTS;
    }

    // Abstract — every subclass must implement to show what they can do
    public abstract void showCapabilities();

    // Getters
    public String getUsername()   { return username; }
    public String getPassword()   { return password; }
    public String getRole()       { return role; }
    public int getLoginAttempts() { return loginAttempts; }
}