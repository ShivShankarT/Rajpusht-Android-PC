package in.rajpusht.pc.custom.validator;


public class ValidationStatus {

	// store the result of validation and error/success message if there is any
	protected String msg;
	protected boolean isValid;
	
	public ValidationStatus(boolean isValid) {
		this.msg = "";
		this.isValid = isValid;
	}
	
	public ValidationStatus(boolean isValid, String msg) {
		this.msg = msg;
		this.isValid = isValid;
	}
	
	
	
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	public boolean isInvalid() {
		return (!isValid);
	}
	
	public boolean isValid() {
		return isValid;
	}
	public void setValid(boolean isValid) {
		this.isValid = isValid;
	}
	
}
