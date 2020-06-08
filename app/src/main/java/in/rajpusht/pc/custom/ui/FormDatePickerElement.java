package in.rajpusht.pc.custom.ui;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;
import java.util.Date;

import in.rajpusht.pc.R;
import in.rajpusht.pc.custom.callback.HValidatorListener;
import in.rajpusht.pc.custom.callback.HValueChangedListener;
import in.rajpusht.pc.custom.utils.HUtil;
import in.rajpusht.pc.custom.validator.ValidationStatus;
import in.rajpusht.pc.utils.AppDateTimeUtils;

public class FormDatePickerElement extends FrameLayout implements View.OnClickListener {

    private EditText edf_text;
    private TextInputLayout edf_txt_inp_ly;
    private HValidatorListener<Date> hValidatorListener;
    private HValueChangedListener<Date> hValueChangedListener;
    private boolean required;
    private Date mDate;
    private long minDate;
    private long maxDate;

    public FormDatePickerElement(Context context) {
        super(context);
    }

    public FormDatePickerElement(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public FormDatePickerElement(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public FormDatePickerElement(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }


    private void init(AttributeSet attrs) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.form_datepicker_with_label, this, true);


        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.FormField, 0, 0);
        String label = a.getString(R.styleable.FormField_ff_label);
        String hint = a.getString(R.styleable.FormField_ff_hint);
        boolean isShowDivider = a.getBoolean(R.styleable.FormField_ff_show_divider, true);
        required = a.getBoolean(R.styleable.FormField_ff_required, false);
        View divider = view.findViewById(R.id.divider);
        if (!isShowDivider) {
            divider.setVisibility(GONE);
        }

        TextView labelTv = view.findViewById(R.id.edf_lab);
        edf_text = view.findViewById(R.id.edf_text);
        edf_txt_inp_ly = view.findViewById(R.id.edf_txt_inp_ly);
        labelTv.setText(label);
        if (TextUtils.isEmpty(label)) {
            labelTv.setVisibility(GONE);
        }
        edf_txt_inp_ly.setHint(hint);
        edf_text.setOnClickListener(this);


        a.recycle();


    }

    public boolean validate() {

        if (required) {
            if (getDate() == null) {
                edf_txt_inp_ly.setError("Please Enter *");
                return false;
            }
        }

        if (hValidatorListener != null) {
            ValidationStatus valid = hValidatorListener.isValid(getDate());
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


    public String getText() {
        return edf_text.getText().toString();
    }

    public Date getDate() {
        return mDate;
    }


    public void setDate(Date mDate) {
        this.mDate = mDate;
        edf_text.setText(AppDateTimeUtils.convertLocalDate(mDate));
        edf_txt_inp_ly.setError(null);
    }

    @Override
    public void onClick(View v) {
        hideKeyboard();
        DatePickerDialog.OnDateSetListener dpd = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {

                int month = monthOfYear + 1;
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.DATE, dayOfMonth);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.YEAR, year);
                mDate = calendar.getTime();
                edf_text.setText(AppDateTimeUtils.convertLocalDate(mDate));
                if (hValueChangedListener != null)
                    hValueChangedListener.onValueChanged(calendar.getTime());
                edf_txt_inp_ly.setError(null);

            }
        };
        Calendar mCurrentDate = Calendar.getInstance();
        int day = mCurrentDate.get(Calendar.DAY_OF_MONTH);
        int month = mCurrentDate.get(Calendar.MONTH);
        int year = mCurrentDate.get(Calendar.YEAR);
        DatePickerDialog d = new DatePickerDialog(getContext(), dpd, year, month, day);
        if (minDate != 0)
            d.getDatePicker().setMinDate(minDate);
        if (maxDate != 0)
            d.getDatePicker().setMaxDate(maxDate);
        else
            d.getDatePicker().setMaxDate(System.currentTimeMillis());
        d.show();
    }

    public void sethValidatorListener(HValidatorListener<Date> hValidatorListener) {
        this.hValidatorListener = hValidatorListener;
    }

    public void sethValueChangedListener(HValueChangedListener<Date> hValueChangedListener) {
        this.hValueChangedListener = hValueChangedListener;
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

    public boolean isVisibleAndEnable() {
        return getVisibility() == VISIBLE && isEnabled();
    }


    public void hideKeyboard() {
        Activity activity = (Activity) getContext();

        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    public long getMinDate() {
        return minDate;
    }

    public void setMinDate(long minDate) {
        this.minDate = minDate;
    }

    public long getMaxDate() {
        return maxDate;
    }

    public void setMaxDate(long maxDate) {
        this.maxDate = maxDate;
    }

}
