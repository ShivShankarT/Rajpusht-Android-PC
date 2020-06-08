package in.rajpusht.pc.custom.validator;

import android.text.TextUtils;

import in.rajpusht.pc.custom.callback.HValidatorListener;
import in.rajpusht.pc.custom.utils.HUtil;

public class FormValidatorUtils {
    public static HValidatorListener<String> emailValidatorListener() {

        return new HValidatorListener<String>() {
            @Override
            public ValidationStatus isValid(String value) {
                // we only validate if there is a value
                // assumption: required validation is already triggered
                // if this field is optional, we only validate if there is a value
                if (value != null && value.length() > 0) {
                    boolean isEmail = HUtil.isEmail(value);
                    return new ValidationStatus(isEmail, "Please enter a valid email");

                }
                return null;
            }
        };
    }

    public static HValidatorListener<String> numberValidatorListener(int length, String msg) {

        return new HValidatorListener<String>() {
            @Override
            public ValidationStatus isValid(String value) {
                if (value != null && value.length() > 0) {
                    value = value.trim();
                    if (length > -1) {
                        // validate length
                        if (length != value.length())
                            return new ValidationStatus(false, msg);
                    }
                    // check for number
                    return new ValidationStatus(HUtil.isNumeric(value), msg);
                }
                return null;
            }
        };
    }


    public static HValidatorListener<String> textEqualValidator(int length, String message) {

        return new HValidatorListener<String>() {
            @Override
            public ValidationStatus isValid(String value) {
                if (value != null && value.length() > 0) {
                    value = value.trim();
                    if (length > -1) {
                        if (length != value.length())
                            return new ValidationStatus(false, message);
                    }
                    return new ValidationStatus(true, message);
                }
                return null;
            }
        };
    }

    public static HValidatorListener<String> textLengthBwValidator(int min, int max, String message) {

        return new HValidatorListener<String>() {
            @Override
            public ValidationStatus isValid(String value) {
                if (value != null && value.length() > 0) {
                    value = value.trim();

                    int length = value.length();
                    if (length >= min && length <= max) {
                        return new ValidationStatus(true, message);
                    }
                    return new ValidationStatus(false, message);

                }
                return null;
            }
        };
    }

    public static HValidatorListener<Double> valueBwValidator(Double min, Double max, String message) {

        return new HValidatorListener<Double>() {
            @Override
            public ValidationStatus isValid(Double value) {
                if (value != null) {
                    if ((min >= value && value <= max))
                        return new ValidationStatus(true, message);

                    return new ValidationStatus(false, message);
                }
                return null;
            }
        };
    }


    public static HValidatorListener<String> valueBwValidatorForStringNumber(Double min, Double max, String message) {

        return new HValidatorListener<String>() {
            @Override
            public ValidationStatus isValid(String value) {
                if (value != null&& TextUtils.isDigitsOnly(value)) {
                    Double aDouble = new Double(value);
                    if ((min >= aDouble && aDouble <= max))
                        return new ValidationStatus(true, message);

                    return new ValidationStatus(false, message);
                }
                return null;
            }
        };
    }

}
