package in.rajpusht.pc.data.remote;

import java.util.concurrent.TimeUnit;

public class AppConstants {
    public static final String BASE_URL = "http://dev.rajpusht.in/survey-server/api/PC/";
    public static final long CONNECT_TIMEOUT = 1000;
    public static final long READ_TIMEOUT = 1000;
    public static final long WRITE_TIMEOUT = 30000;

    public static final long REPEAT_TIME_INTERVAL_IN_HOURS = 20;
    public static final TimeUnit REPEAT_TIME_INTERVAL_UNITS = TimeUnit.MINUTES;
    public static final String SYNC_WORK = "sync_worker";

    private AppConstants() {
        // Private constructor to hide the implicit one
    }

}