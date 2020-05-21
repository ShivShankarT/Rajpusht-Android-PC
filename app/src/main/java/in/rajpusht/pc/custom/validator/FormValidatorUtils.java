package in.rajpusht.pc.custom.validator;

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

}
