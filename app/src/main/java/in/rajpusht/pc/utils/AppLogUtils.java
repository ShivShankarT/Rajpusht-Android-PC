package in.rajpusht.pc.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import in.rajpusht.pc.BuildConfig;
import in.rajpusht.pc.R;
import in.rajpusht.pc.utils.timber.FileLoggingTree;
import timber.log.Timber;

public class AppLogUtils {
    Context context;

    public AppLogUtils(Context context) {
        this.context = context;
    }

    private Intent createEmailIntent() {
        Intent emailIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);

        //   emailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]
                {"mohit@esecforte.com"});
        emailIntent.putExtra(Intent.EXTRA_BCC, new String[]
                {"vinoth.m@esecforte.com"});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT,
                "Rajpusht error reports");

        emailIntent.setData(Uri.parse("mailto:"));
        //emailIntent.setClassName("com.google.android.gm", "com.google.android.gm.ComposeActivityGmail");

        emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        return emailIntent;

    }

    private void sendReports(String emaal_text) {

        String state = Environment.getExternalStorageState();

        boolean mExternalStorageAvailable;
        boolean mExternalStorageWriteable;
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            // Can read and write the media
            mExternalStorageAvailable = mExternalStorageWriteable = true;
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            // Can only read the media
            mExternalStorageAvailable = true;
            mExternalStorageWriteable = false;
        } else {
            // Can't read or write
            mExternalStorageAvailable = mExternalStorageWriteable = false;
        }

        if (mExternalStorageAvailable && mExternalStorageWriteable) {
            writeToSDFile();
            sendReport("external", emaal_text);

        } else {
            sendReport("internal", emaal_text);
        }
    }

    private void writeToSDFile() {

//        Log.e("GEM ", " writing to sdcard");
        File root = Environment.getExternalStorageDirectory();

        File dir = new File(root.getAbsolutePath() + "/Rajpusht Log");

        if (!dir.exists())
            dir.mkdirs();

        File file = new File(dir, "RajpushtSysLog.txt");

        if (!file.exists())
            try {
                file.createNewFile();
            } catch (IOException e) {
                Timber.e(e);
            }

        try {
            FileOutputStream f = new FileOutputStream(file);
            PrintWriter pw = new PrintWriter(f);
//            pw.println(getTime());
            pw.println(Logcat.getLogcat());
            pw.flush();
            pw.close();
            f.close();
        } catch (FileNotFoundException e) {
            Timber.e(e);

        } catch (IOException e) {
            Timber.e(e);
        }
    }


    public void dialogSubmitReason() {
        // custom dialog
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_with_editext);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(lp);
        dialog.setCancelable(false);
        Button send_btn = dialog.findViewById(R.id.send_btn);
        final EditText edt_dialog_body = dialog.findViewById(R.id.edt_dialog_body);
        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                String emaal_text = edt_dialog_body.getText().toString();
                sendReports(emaal_text);

            }
        });

        dialog.show();

    }

    private void sendReport(String type, String emaal_text) {
        String device_type = Build.MODEL + "-" + Build.VERSION.SDK_INT;

        Intent emailIntent = createEmailIntent();
        if (type.equals("external")) {
            ArrayList<File> logFiles = getLastLogfileAndDelete();
            File appLogfile = new File(Environment.getExternalStorageDirectory(), "/Rajpusht Log/RajpushtSysLog.txt");
            logFiles.add(0, appLogfile);
            ArrayList<Uri> logFilesUri = new ArrayList<>(logFiles.size());
            List<ResolveInfo> resolvedInfoActivities = context.getPackageManager().queryIntentActivities(emailIntent, PackageManager.MATCH_DEFAULT_ONLY);
            try {
                SimpleDateFormat date = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss", Locale.ENGLISH);
                String dateStr = date.format(new Date());
                File opt = new File(context.getExternalCacheDir(), "Rajpusht" + dateStr + ".zip");
                ZipUsingJavaUtil.zipFiles(logFiles, opt.getAbsolutePath());

                Uri uri = FileProvider.getUriForFile(context,
                        context.getApplicationContext().getPackageName() + ".provider", opt);
                for (ResolveInfo ri : resolvedInfoActivities) {
                    context.grantUriPermission(ri.activityInfo.packageName, uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                }
                logFilesUri.add(uri);
            } catch (IOException e) {
                Timber.e(e);
                e.printStackTrace();
            }

            emailIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, logFilesUri);
            emailIntent.setType("text/plain");
            emailIntent.putExtra(Intent.EXTRA_TEXT,
                    "Device type:" + device_type + " \n" + "App version:" + BuildConfig.VERSION_NAME + " \n" + "Reason :" + emaal_text);
            Intent send_mail = Intent.createChooser(emailIntent, "Send mail");


            context.startActivity(send_mail);
        } else {

            try {
                emailIntent.setType("message/rfc822");
                emailIntent.putExtra(Intent.EXTRA_TEXT,
                        Logcat.getLogcat());

                context.startActivity(Intent.createChooser(emailIntent, "Send mail"));

            } catch (IOException e) {
                Timber.e(e);
            }
        }

    }

    private ArrayList<File> getLastLogfileAndDelete() {
        ArrayList<File> files = new ArrayList<>();
        File rootDir = FileLoggingTree.logFileRootDir();
        File[] listFiles = rootDir.listFiles();
        Calendar befourCal = Calendar.getInstance();
        befourCal.add(Calendar.DAY_OF_YEAR, -30);
        Date befour7days = befourCal.getTime();
        for (File listFile : listFiles) {
            String fileName = listFile.getName();
            String date = fileName.split("\\.")[0];
            try {
                Date fileNameTimeStamp = new SimpleDateFormat("dd-MM-yyyy",
                        Locale.UK).parse(date);
                if (fileNameTimeStamp.after(befour7days)) {
                    files.add(listFile);
                } else {
                    listFile.delete();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Timber.e(e);
                listFile.delete();
            }

        }

        return files;

    }
}
