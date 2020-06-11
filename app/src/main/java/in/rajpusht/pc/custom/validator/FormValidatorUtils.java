package in.rajpusht.pc.custom.validator;

import android.util.Log;

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
                return new ValidationStatus(false);
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
                return new ValidationStatus(false, msg);
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
                return new ValidationStatus(false, message);
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
                return new ValidationStatus(false, message);
            }
        };
    }

    public static HValidatorListener<Integer> valueBwValidator(Integer min, Integer max, String message) {

        return new HValidatorListener<Integer>() {
            @Override
            public ValidationStatus isValid(Integer value) {
                if (value != null) {
                    if (min <= value && value <= max)
                        return new ValidationStatus(true, message);

                    return new ValidationStatus(false, message);
                }
                return new ValidationStatus(false, message);
            }
        };
    }

    public static HValidatorListener<Double> valueBwValidator(Double min, Double max, String message) {

        return new HValidatorListener<Double>() {
            @Override
            public ValidationStatus isValid(Double value) {
                if (value != null) {
                    if (min <= value && value <= max)
                        return new ValidationStatus(true, message);

                    return new ValidationStatus(false, message);
                }
                return new ValidationStatus(false, message);
            }
        };
    }


    public static HValidatorListener<String> valueBwValidatorForStringNumber(Double min, Double max, String message) {

        return new HValidatorListener<String>() {
            @Override
            public ValidationStatus isValid(String value) {
                if (value != null) {
                    Double aDouble = null;
                    try {
                        aDouble = Double.valueOf(value);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (aDouble != null) {
                        if (min <= aDouble && aDouble <= max)
                            return new ValidationStatus(true, message);

                        return new ValidationStatus(false, message);
                    }
                }
                return new ValidationStatus(false, message);
            }
        }

                ;
    }

}
