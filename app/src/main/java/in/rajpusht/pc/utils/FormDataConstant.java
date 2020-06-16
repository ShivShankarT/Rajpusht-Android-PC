package in.rajpusht.pc.utils;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

public class FormDataConstant {

    public static final String ANC_NOT_COMPLETED = ("AC");// Miscarriage/ abortion
    public static final List<String> economic = new ArrayList<>();
    public static List<String> caste = new ArrayList<>();
    public static List<String> pwNaReason = new ArrayList<>();
    public static List<String> lmNaReason = new ArrayList<>();

    static {
        economic.add("APL");
        economic.add("BPL");
        economic.add("ANTODAYA");

        caste.add("SC");
        caste.add("ST");
        caste.add("OBC");
        caste.add("GENERAL");

        lmNaReason.add("MD");//mother death
        lmNaReason.add("MM");//mother migrated
        lmNaReason.add("CD");//child death
        lmNaReason.add("CM");//child migrated
        lmNaReason.add("BD");//both death
        lmNaReason.add("BM");//both migrated

        pwNaReason.add("MA");// Miscarriage/ abortion
        pwNaReason.add("WD");// women death
        pwNaReason.add("WM");// women migrated
        pwNaReason.add("AC");//anc not completed


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
