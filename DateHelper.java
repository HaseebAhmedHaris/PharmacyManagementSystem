import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateHelper {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static long daysUntilExpiry(String expiryDateStr) {
        try {
            LocalDate expiry = LocalDate.parse(expiryDateStr.trim(), FORMATTER);
            LocalDate today  = LocalDate.now();
            return expiry.toEpochDay() - today.toEpochDay();
        } catch (Exception e) {
            System.out.println("Could not read date: " + expiryDateStr);
            return -999;
        }
    }

    public static boolean isExpired(String expiryDateStr) {
        long days = daysUntilExpiry(expiryDateStr);
        if (days == -999) return false;
        return days <= 0;
    }

    public static boolean isExpiringSoon(String expiryDateStr, int warningDays) {
        long days = daysUntilExpiry(expiryDateStr);
        if (days == -999) return false;
        return days > 0 && days <= warningDays;
    }

    public static String formatForDisplay(String dateStr) {
        try {
            LocalDate date = LocalDate.parse(dateStr.trim(), FORMATTER);
            return date.format(DateTimeFormatter.ofPattern("dd MMM yyyy"));
        } catch (Exception e) {
            return dateStr;
        }
    }

    public static String todayAsString() {
        return LocalDate.now().format(FORMATTER);
    }
}