package alfatih.me.moviecatalogue.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtil {
    public static String getCurrentTimeStamp() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());//dd/MM/yyyy
        Date now = new Date();
        return formatter.format(now);
    }
}
