package in.rajpusht.pc.custom.callback;


import in.rajpusht.pc.custom.validator.ValidationStatus;

public interface HValidatorListener<T> {
    ValidationStatus isValid(T data);
}
