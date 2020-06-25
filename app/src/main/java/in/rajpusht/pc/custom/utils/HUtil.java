package in.rajpusht.pc.custom.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;

import in.rajpusht.pc.data.local.db.entity.BeneficiaryEntity;
import in.rajpusht.pc.data.local.db.entity.ChildEntity;
import in.rajpusht.pc.data.local.db.entity.PregnantEntity;
import in.rajpusht.pc.model.Tuple;

public class HUtil {

    /*
     * Give an option array, find the index for value
     * return -1 if not found
     * e.g. options: { "Option1","Option2", "Option3" }
     *      value : "Option2" will return index 1
     *
     */
    public static int getSelectedIndex(String options[], String value) {
        int selectedIndex = -1;

        if (value == null || value.length() == 0)
            return -1;

        for (int i = 0; i < options.length; i++) {
            if (options[i].equals(value)) {
                selectedIndex = i;
                break;
            }
        }

        return selectedIndex;
    }

    public static boolean isNumeric(String str) {
        if (str == null)
            return false;

        for (char c : str.toCharArray()) {
            if (!Character.isDigit(c)) return false;
        }
        return true;
    }

    public static boolean isEmail(String str) {
        if (str == null)
            return false;

        return android.util.Patterns.EMAIL_ADDRESS.matcher(str.trim()).matches();
    }

    public static void recursiveSetEnabled(@NonNull final ViewGroup vg, final boolean enabled) {
        boolean old = vg.isEnabled();
        if (old && !enabled || !old && enabled)
            vg.setEnabled(enabled);


        for (int i = 0, count = vg.getChildCount(); i < count; i++) {
            final View child = vg.getChildAt(i);
            child.setEnabled(enabled);
            if (child instanceof ViewGroup) {
                recursiveSetEnabled((ViewGroup) child, enabled);
            }
        }
    }


    public static void recursiveSetEnabled(@NonNull final ViewGroup vg, final boolean enabled, Integer... exceptIds) {
        Set<Integer> integers = new HashSet<Integer>(Arrays.asList(exceptIds));
        //vg.setEnabled(enabled);
        for (int i = 0, count = vg.getChildCount(); i < count; i++) {
            final View child = vg.getChildAt(i);
            if (!integers.contains(child.getId())) {
                child.setEnabled(enabled);
            }
            if (child instanceof ViewGroup) {
                recursiveSetEnabled((ViewGroup) child, enabled, exceptIds);
            }
        }
    }

    public static String getSaveImageFilePath(View view) {
        File mediaStorageDir = view.getContext().getCacheDir();
        // Create a storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageName = "IMG_" + timeStamp + ".jpg";

        String selectedOutputPath = mediaStorageDir.getPath() + File.separator + imageName;


        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());

        int maxSize = 1080;

        int bWidth = bitmap.getWidth();
        int bHeight = bitmap.getHeight();

        if (bWidth > bHeight) {
            int imageHeight = (int) Math.abs(maxSize * ((float) bitmap.getWidth() / (float) bitmap.getHeight()));
            bitmap = Bitmap.createScaledBitmap(bitmap, maxSize, imageHeight, true);
        } else {
            int imageWidth = (int) Math.abs(maxSize * ((float) bitmap.getWidth() / (float) bitmap.getHeight()));
            bitmap = Bitmap.createScaledBitmap(bitmap, imageWidth, maxSize, true);
        }
        view.setDrawingCacheEnabled(false);
        view.destroyDrawingCache();

        OutputStream fOut = null;
        try {
            File file = new File(selectedOutputPath);
            fOut = new FileOutputStream(file);

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
            fOut.flush();
            fOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return selectedOutputPath;
    }

    /*
    PW1 => BETWEEN 0 and 98
    PW2 => BETWEEN 99 and 196
    PW3 => BETWEEN 197 and 252
    PW4 => BETWEEN 253 and 280

    LM1 => BETWEEN 0 and 91
    LM2 => BETWEEN 92 and 182
    LM3 => BETWEEN 183 and 365

    MY1 => BETWEEN 366 and 547
    MY2 => BETWEEN 548 and 730
    MY3 => BETWEEN 731 and 912
    MY4 => BETWEEN 913 and 1095
    MY5 => BETWEEN 1096 and 1155
    */


    public static String getLMMYSubStage(int day) {

        if (day <= 91) {
            return "LM1";
        } else if (day <= 182) {
            return "LM2";
        } else if (day <= 365) {
            return "LM3";
        } else if (day <= 547) {
            return "MY1";
        } else if (day <= 730) {
            return "MY2";
        } else if (day <= 912) {
            return "MY3";
        } else if (day <= 1095) {
            return "MY4";
        } else if (day <= 1155) {
            return "MY5";
        }
        return "";
    }

    public static String getPWSubStage(int day) {

        if (day <= 98) {
            return "PW1";
        } else if (day <= 196) {
            return "PW2";
        } else if (day <= 252) {
            return "PW3";
        } else if (day <= 280) {
            return "PW4";
        }
        return "";
    }


    public static int daysBetween(Date d1, Date d2) {
        return (int) ((d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));
    }


    public static int calcAge(Date birthday) {
        GregorianCalendar today = new GregorianCalendar();
        GregorianCalendar bday = new GregorianCalendar();
        GregorianCalendar bdayThisYear = new GregorianCalendar();

        bday.setTime(birthday);
        bdayThisYear.setTime(birthday);
        bdayThisYear.set(Calendar.YEAR, today.get(Calendar.YEAR));
        int age = today.get(Calendar.YEAR) - bday.get(Calendar.YEAR);
        if (today.getTimeInMillis() < bdayThisYear.getTimeInMillis())
            age--;

        return age;
    }

    public static Tuple<BeneficiaryEntity, PregnantEntity, ChildEntity> staticData() {//todo rm

        String j = "{\"t1\":{\"dob\":\"Jun 30, 1984 12:00:00 AM\",\"age\":36,\"bahamashahId\":\"wr6688899\",\"beneficiaryId\":1590818137299,\"caste\":\"SC\",\"childCount\":2,\"collectedDataSubStage\":[],\"counselingProv\":\"YES\",\"counselingSms\":1,\"createdAt\":\"2020-05-30 11:25:37\",\"dataStatus\":\"NEW\",\"economic\":\"BPL\",\"husbandMobNo\":\"9736484543\",\"husbandName\":\"Ram Kumar\",\"id\":1,\"mobileNo\":\"9876543218\",\"name\":\"Shavani\",\"pctsId\":\"13466778\",\"pmmvyInstallment\":1,\"stage\":\"PW\",\"subStage\":\"PW\",\"updatedAt\":\"2020-05-30 11:25:37\"},\"t2\":{\"beneficiaryId\":1590818137299,\"createdAt\":\"2020-05-30 11:25:37\",\"dataStatus\":\"NEW\",\"id\":1,\"lmpDate\":\"Jun 4, 2020 12:00:00 AM\",\"pregnancyId\":1590818137299,\"updatedAt\":\"2020-05-30 11:25:37\"},\"t3\":{\"dob\":\"Oct 8, 2019 12:00:00 AM\",\"childId\":15908181372991,\"childOrder\":1,\"collectedDataSubStage\":[],\"createdAt\":\"2020-05-30 11:25:37\",\"dataStatus\":\"NEW\",\"deliveryHome\":0,\"deliveryPlace\":\"Bhavu\",\"id\":1,\"motherId\":1590818137299,\"stage\":\"LM\",\"subStage\":\"LM\",\"updatedAt\":\"2020-05-30 11:25:37\"}}";
        String json = j.replace("1590818137299", System.currentTimeMillis() + "");
        Date date = new Date();
        json = json.replace("Shavani", "Shavani " + date.getMinutes() + date.getSeconds());
        json = json.replace("13466778", System.currentTimeMillis() + "");

        Tuple<BeneficiaryEntity, PregnantEntity, ChildEntity> tuple = new Gson().fromJson(json, new TypeToken<Tuple<BeneficiaryEntity, PregnantEntity, ChildEntity>>() {
        }.getType());
        tuple.getT1().setId(0);
        tuple.getT2().setId(0);
        tuple.getT3().setId(0);

        return tuple;

    }

    public static String convYestoY(@Nullable String text) {
        if (!TextUtils.isEmpty(text))
            switch (text) {
                case "YES":
                    return "Y";
                case "NO":
                    return "N";
            }
        return null;
    }


    public static String convYtoYes(@Nullable String text) {
        if (!TextUtils.isEmpty(text))
            switch (text) {
                case "Y":
                    return "YES";
                case "N":
                    return "NO";
            }
        return null;
    }


    public static String readStringFromAsset(Context context, String name) throws IOException {

    /*    StringBuilder sb = new StringBuilder();
        InputStream is = context.getAssets().open(json);
        BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
        String str;
        while ((str = br.readLine()) != null) {
            sb.append(str);
        }
        br.close();*/

        BufferedReader in = null;
        try {
            StringBuilder buf = new StringBuilder();
            InputStream is = context.getAssets().open(name);
            in = new BufferedReader(new InputStreamReader(is));

            String str;
            boolean isFirst = true;
            while ( (str = in.readLine()) != null ) {
                if (isFirst)
                    isFirst = false;
                else
                    buf.append('\n');
                buf.append(str);
            }
            return buf.toString();
        } catch (IOException e) {
            Log.e("ss", "Error opening asset " + name);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    Log.e("ss", "Error closing asset " + name);
                }
            }
        }

        return null;
    }


}
