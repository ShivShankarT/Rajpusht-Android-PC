package in.rajpusht.pc.custom.validator;


import in.rajpusht.pc.custom.callback.HValidatorListener;
import in.rajpusht.pc.custom.utils.HUtil;

// validate numeric field
// make sure that the length is of specified length if necessary
public class HNumericValidatorListener implements HValidatorListener<String> {

    private int length = -1;
    private String msg;

    public HNumericValidatorListener(int length, String msg) {
        this.length = length;
        this.msg = msg;
    }

    @Override
    public ValidationStatus isValid(String value) {

        // we only validate if there is a value
        // assumption: required validation is already triggered
        // if this field is optional, we only validate if there is a value
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

        return null; // no validation is carried out
    }


    public int getLength() {
        return length;
    }


    public void setLength(int length) {
        this.length = length;
    }


    public String getMsg() {
        return msg;
    }


    public void setMsg(String msg) {
        this.msg = msg;
    }


}
