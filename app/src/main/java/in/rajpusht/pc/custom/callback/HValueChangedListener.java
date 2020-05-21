package in.rajpusht.pc.custom.callback;


public interface HValueChangedListener<T> {
    void onValueChanged(T data);  // inform listener that the value for the field has changed
}
