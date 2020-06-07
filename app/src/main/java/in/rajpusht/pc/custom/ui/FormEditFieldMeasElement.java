package in.rajpusht.pc.custom.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Pair;
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

public class FormEditFieldMeasElement extends FrameLayout {

    private HValidatorListener<Double> hValidatorListener;
    private EditText edf_text;
    private EditText edf_text2;
    private TextInputLayout edf_txt_inp_ly;
    private HValueChangedListener<Double> hValueChangedListener;
    private boolean required;

    public FormEditFieldMeasElement(Context context) {
        super(context);
        init(null);
    }

    public FormEditFieldMeasElement(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public FormEditFieldMeasElement(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public FormEditFieldMeasElement(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.form_edit_text_measurement, this, true);


        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.FormField, 0, 0);
        String label = a.getString(R.styleable.FormField_ff_label);
        String hint = a.getString(R.styleable.FormField_ff_hint);
        required = a.getBoolean(R.styleable.FormField_ff_required, false);
        boolean isShowDivider = a.getBoolean(R.styleable.FormField_ff_show_divider, true);
        int measurement_type = a.getInt(R.styleable.FormField_ff_measurement_type, 0);//weight==0 height ==1
        int maxLength = a.getInt(R.styleable.FormField_ff_maxLength, -1);

        View divider = view.findViewById(R.id.divider);
        if (!isShowDivider) {
            divider.setVisibility(GONE);
        }
        a.recycle();
        TextView labelTv = view.findViewById(R.id.edf_lab);

        TextInputLayout edf_sub1 = view.findViewById(R.id.edf_sub1);
        edf_text = view.findViewById(R.id.edf_text);
        TextInputLayout edf_sub2 = view.findViewById(R.id.edf_sub2);
        edf_text2 = view.findViewById(R.id.edf_text2);

        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (hValueChangedListener != null)
                    hValueChangedListener.onValueChanged(getMeasValue());
                edf_txt_inp_ly.setError(null);
            }
        };
        edf_text.addTextChangedListener(watcher);
        edf_text2.addTextChangedListener(watcher);

        edf_txt_inp_ly = view.findViewById(R.id.edf_txt_inp_ly);


        edf_text.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
        edf_text2.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);

        if (maxLength != -1) {
            edf_text.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
            edf_text2.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});

        }

        labelTv.setText(label);
        if (TextUtils.isEmpty(label)) {
            labelTv.setVisibility(GONE);
        }
        edf_txt_inp_ly.setHint(hint);

        if (measurement_type == 0) {
            edf_sub1.setHint("KG");
            edf_sub2.setHint("GM");
        } else {
            edf_sub1.setHint("CM");
            edf_sub2.setHint("MM");
        }
    }

    public boolean validate() {


        Double measValue = getMeasValue();
        if (required) {
            if (measValue == null || measValue == 0) {
                edf_txt_inp_ly.setError("Please Enter *");
                return false;
            }
        }

        if (hValidatorListener != null) {
            ValidationStatus valid = hValidatorListener.isValid(measValue);
            if (valid.isInvalid()) {
                edf_txt_inp_ly.setError(valid.getMsg());
                return false;
            } else {
                edf_txt_inp_ly.setError(null);
            }
        }
        return true;
    }

    // return validate, view for requestFocusAndScroll
    public Pair<Boolean, View> validateWthView() {
        return new Pair<>(validate(), this);
    }

    public void sethValidatorListener(HValidatorListener<Double> hValidatorListener) {
        this.hValidatorListener = hValidatorListener;
    }


    public Double getMeasValue() {
        String n1 = edf_text.getText().toString();
        String n2 = edf_text2.getText().toString();

        String num = null;
        if (!TextUtils.isEmpty(n1) & !TextUtils.isEmpty(n2)) {
            num = n1 + "." + n2;
        } else if (!TextUtils.isEmpty(n1)) {
            num = n1 + "." + n2;
        } else if (!TextUtils.isEmpty(n2)) {
            num = "0." + n2;
        } else return null;

        try {
            return Double.valueOf(num);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public void setText(Double text) {
        edf_txt_inp_ly.setError(null);
        if (text == null)
            return;

        String[] arr = String.valueOf(text).split("\\.");
        if (arr.length > 0) {
            int v1 = Integer.parseInt(arr[0]);
            edf_text.setText(String.valueOf(v1));
        }

        if (arr.length > 1) {
            int v1 = Integer.parseInt(arr[1]);
            if (v1 != 0)
                edf_text2.setText(String.valueOf(v1));
        }


    }


    public void requestFocusAndScroll() {
        View targetView = this;
        targetView.getParent().requestChildFocus(targetView, targetView);
    }

    public void setEnableChild(boolean enable) {
        HUtil.recursiveSetEnabled(this, enable);
    }


    public boolean isVisible() {
        return getVisibility() == VISIBLE;
    }


}
