package in.rajpusht.pc.utils.timber;

import android.os.Environment;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.crashlytics.FirebaseCrashlytics;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;

import in.rajpusht.pc.BuildConfig;
import in.rajpusht.pc.data.local.pref.AppPreferencesHelper;
import in.rajpusht.pc.utils.rx.SchedulerProvider;
import io.reactivex.Completable;
import io.reactivex.functions.Action;
import timber.log.Timber;

//import timber.log.BuildConfig;

public class FileLoggingTree extends Timber.DebugTree {

    private static final String LOG_TAG = FileLoggingTree.class.getSimpleName();


    SchedulerProvider schedulerProvider;

    @Inject
    public FileLoggingTree(SchedulerProvider schedulerProvider) {
        this.schedulerProvider = schedulerProvider;
    }

    /*  Helper method to create file*/
    @Nullable
    private static File generateFile(@NonNull String path, @NonNull String fileName) {
        File file = null;
        if (isExternalStorageAvailable()) {
            File root = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),
                    BuildConfig.APPLICATION_ID + File.separator + path);

            boolean dirExists = true;

            if (!root.exists()) {
                dirExists = root.mkdirs();
            }

            if (dirExists) {
                file = new File(root, fileName);
            }
        }
        return file;
    }

    /* Helper method to determine if external storage is available*/
    private static boolean isExternalStorageAvailable() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    public static File logFileRootDir() {
        String path = "Log";
        if (isExternalStorageAvailable()) {
            File root = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),
                    BuildConfig.APPLICATION_ID + File.separator + path);

            boolean dirExists = true;

            if (!root.exists()) {
                dirExists = root.mkdirs();
            }

            return root;
        }
        return null;
    }

    @Override
    protected void log(int priority, String tag, String message, Throwable t) {

        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                writeFileLog(priority, tag, message);
            }
        }).subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.io())
                .subscribe();


    }

    private void writeFileLog(int priority, String tag, String message) {
        String color = "lightgray";
        String priorityStr;

        switch (priority) {
            case Log.VERBOSE:
                priorityStr = "VERBOSE";
                break;
            case Log.DEBUG:
                priorityStr = "DEBUG";
                color = "#F57F17";
                break;
            case Log.INFO:
                priorityStr = "INFO";
                color = "#40C4FF";
                break;
            case Log.WARN:
                priorityStr = "WARN";
                color = "#FFAB40";
                break;
            case Log.ERROR:
                priorityStr = "ERROR";
                color = "red";
                break;
            case Log.ASSERT:
                priorityStr = "ASSERT";
                break;
            default:
                priorityStr = "" + priority;
        }

        try {
            String path = "Log";
            String fileNameTimeStamp = new SimpleDateFormat("dd-MM-yyyy",
                    Locale.UK).format(new Date());
            String logTimeStamp = new SimpleDateFormat("E MMM dd yyyy 'at' hh:mm:ss:SSS aaa",
                    Locale.UK).format(new Date());
            String fileName = fileNameTimeStamp + ".html";
            // Create file
            File file = generateFile(path, fileName);

            // If file created or exists save logs
            if (file != null) {
                boolean isFileExists = file.exists();
                FileWriter writer = new FileWriter(file, true);
                if (!isFileExists) {
                    String userId = String.valueOf(AppPreferencesHelper.getAppPreferencesHelper().getCurrentUserId());
                    writer.append("<h1>");
                    writer.append("UserId:");
                    writer.append(userId);
                    writer.append(", App version code:v" + BuildConfig.VERSION_NAME + "-" + BuildConfig.VERSION_CODE);
                    writer.append("</h1>");
                }

                writer.append("<p style=\"background:").append(color).append(";\"><strong ").append("style=\"background:lightblue;\">&nbsp&nbsp")
                        .append(logTimeStamp)
                        .append(" :&nbsp&nbsp</strong><strong>&nbsp&nbsp")
                        .append(tag)
                        .append("</strong> - ")
                        .append("priority:")
                        .append(priorityStr)
                        .append("   ")
                        .append(message)
                        .append("</p>");
                writer.flush();
                writer.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            FirebaseCrashlytics.getInstance().recordException(e);
        }
    }

    @Override
    protected String createStackElementTag(StackTraceElement element) {
        // Add log statements line number to the log
        return super.createStackElementTag(element) + " - " + element.getLineNumber();
    }
}