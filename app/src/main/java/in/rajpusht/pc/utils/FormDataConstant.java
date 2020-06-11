package in.rajpusht.pc.utils;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

public class FormDataConstant {

    public static List<String> economic = new ArrayList<>();

    public static List<String> caste = new ArrayList<>();

    static {
        economic.add("APL");
        economic.add("BPL");
        economic.add("ANTODAYA");

        caste.add("SC");
        caste.add("ST");
        caste.add("OBC");
        caste.add("GENERAL");

    }


    public static Integer instalmentValConvt(String value) {
        if (!TextUtils.isEmpty(value)) {
            if (value.equalsIgnoreCase("Don't Know") || value.equalsIgnoreCase("hin")) {
                return -1;
            } else {
                return Integer.valueOf(value);//todo
            }
        }
        return null;
    }


    public static String instalmentValConvt(Integer value) {
        if (value != null) {
            if (value == -1) {
                return "Don't Know";
            } else {
                return String.valueOf(value);
            }
        }
        return null;
    }


}
