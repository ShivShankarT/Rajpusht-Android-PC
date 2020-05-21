package in.rajpusht.pc.custom.validator;


import in.rajpusht.pc.custom.callback.HValidatorListener;
import in.rajpusht.pc.custom.utils.HUtil;

public class HEmailValidatorListener implements HValidatorListener<String> {

	@Override
	public ValidationStatus isValid(String value) {

			
			// we only validate if there is a value
			// assumption: required validation is already triggered
			// if this field is optional, we only validate if there is a value
			if(value!=null && value.length() > 0){
				boolean isEmail = HUtil.isEmail(value);
				return new ValidationStatus(isEmail, "Please enter a valid email");
				
			}
		return null;
	}

}
