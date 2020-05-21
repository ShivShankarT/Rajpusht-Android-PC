package in.rajpusht.pc.custom.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.android.material.textfield.TextInputLayout;

import in.rajpusht.pc.R;
import in.rajpusht.pc.custom.callback.HValidatorListener;
import in.rajpusht.pc.custom.callback.HValueChangedListener;
import in.rajpusht.pc.custom.utils.HUtil;
import in.rajpusht.pc.custom.validator.ValidationStatus;

public class FormEditFieldElement extends FrameLayout {

    HValidatorListener hValidatorListener;
    private EditText edf_text;
    private TextInputLayout edf_txt_inp_ly;
    private HValueChangedListener<String> hValueChangedListener;
    private boolean required;

    public FormEditFieldElement(Context context) {
        super(context);
        init(null);
    }

    public FormEditFieldElement(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public FormEditFieldElement(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public FormEditFieldElement(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.form_edit_text_with_label, this, true);


        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.FormField, 0, 0);
        String label = a.getString(R.styleable.FormField_ff_label);
        String hint = a.getString(R.styleable.FormField_ff_hint);
        required = a.getBoolean(R.styleable.FormField_ff_required, false);
        boolean isShowDivider = a.getBoolean(R.styleable.FormField_ff_show_divider, true);
        View divider = view.findViewById(R.id.divider);
        if (!isShowDivider) {
            divider.setVisibility(GONE);
        }
        a.recycle();
        TextView labelTv = view.findViewById(R.id.edf_lab);
        edf_text = view.findViewById(R.id.edf_text);
        edf_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (hValueChangedListener != null)
                    hValueChangedListener.onValueChanged(s.toString());
                edf_txt_inp_ly.setError(null);
            }
        });
        edf_txt_inp_ly = view.findViewById(R.id.edf_txt_inp_ly);
        labelTv.setText(label);
        if (TextUtils.isEmpty(label)) {
            labelTv.setVisibility(GONE);
        }
        edf_txt_inp_ly.setHint(hint);
    }

    public boolean validate() {


        if (required) {
            if (TextUtils.isEmpty(getText())) {
                edf_txt_inp_ly.setError("Please Enter *");
                return false;
            }
        }

        if (hValidatorListener != null) {
            ValidationStatus valid = hValidatorListener.isValid(getText());
            if (valid.isInvalid()) {
                edf_txt_inp_ly.setError(valid.getMsg());
                return false;
            } else {
                edf_txt_inp_ly.setError(null);
            }
        }
        return true;
    }

    public void sethValidatorListener(HValidatorListener hValidatorListener) {
        this.hValidatorListener = hValidatorListener;
    }


    public String getText() {
        return edf_text.getText().toString();
    }

    public void requestFocusAndScroll() {
        View targetView = this;
        targetView.getParent().requestChildFocus(targetView, targetView);
    }

    public void setEnableChild(boolean enable) {
        HUtil.recursiveSetEnabled(this, enable);
    }

}
