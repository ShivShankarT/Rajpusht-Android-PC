package in.rajpusht.pc.custom.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import in.rajpusht.pc.R;
import in.rajpusht.pc.custom.callback.HValidatorListener;
import in.rajpusht.pc.custom.callback.HValueChangedListener;
import in.rajpusht.pc.custom.utils.HUtil;
import in.rajpusht.pc.custom.validator.ValidationStatus;

public class FormSingleSelectorElement extends FrameLayout implements RadioGroup.OnCheckedChangeListener {

    RadioGroup edf_ch_gp;
    private int mSelectedPos = -1;
    private TextInputLayout edf_txt_inp_ly;
    private HValidatorListener<Integer> hValidatorListener;
    private HValueChangedListener<Integer> hValueChangedListener;
    private boolean required;
    private List<String> sectionData = new ArrayList<>();

    public FormSingleSelectorElement(Context context) {
        super(context);
    }

    public FormSingleSelectorElement(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public FormSingleSelectorElement(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }


    public FormSingleSelectorElement(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.form_radio_gp_with_label, this, true);
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.FormField, 0, 0);
        String label = a.getString(R.styleable.FormField_ff_label);
        String hint = a.getString(R.styleable.FormField_ff_label);
        required = a.getBoolean(R.styleable.FormField_ff_required, false);
        boolean isShowDivider = a.getBoolean(R.styleable.FormField_ff_show_divider, true);
        View divider = view.findViewById(R.id.divider);
        if (!isShowDivider) {
            divider.setVisibility(GONE);
        }
        TextView labelTv = view.findViewById(R.id.edf_lab);
        edf_txt_inp_ly = view.findViewById(R.id.edf_txt_inp_ly);
        labelTv.setText(label);
        CharSequence[] array = a.getTextArray(R.styleable.FormField_ff_selections);
        for (int i = 0; i < array.length; i++) {
            sectionData.add(String.valueOf(array[i]));
        }
        edf_ch_gp = view.findViewById(R.id.edf_rad_gp);

        setUiData();
        a.recycle();
    }

    public void setSectionList(String[] data) {
        List<String> data1 = Arrays.asList(data);
        setSectionList(data1);
    }

    public void setSectionList(List<String> data) {
        sectionData.clear();
        sectionData.addAll(data);
        setUiData();
    }

    private void setUiData() {
        edf_ch_gp.removeAllViews();
        edf_ch_gp.setOnCheckedChangeListener(this);
        for (int i = 0; i < sectionData.size(); i++) {
            RadioButton child = new RadioButton(getContext());
            child.setText(sectionData.get(i));
            child.setId(i);
            edf_ch_gp.addView(child);
        }
    }

    public boolean validate() {
        if (required) {
            if (mSelectedPos == -1) {
                edf_txt_inp_ly.setError(getContext().getString(R.string.Please_Select));
                return false;
            }
        }
        if (hValidatorListener != null) {
            ValidationStatus valid = hValidatorListener.isValid(getSelectedPos());
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

    public void sethValidatorListener(HValidatorListener<Integer> hValidatorListener) {
        this.hValidatorListener = hValidatorListener;
    }

    public void sethValueChangedListener(HValueChangedListener<Integer> hValueChangedListener) {
        this.hValueChangedListener = hValueChangedListener;
    }

    public int getSelectedPos() {
        return mSelectedPos;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        mSelectedPos = checkedId;
        edf_txt_inp_ly.setError(null);
        if (hValueChangedListener != null)
            hValueChangedListener.onValueChanged(mSelectedPos);
    }

    public void requestFocusAndScroll() {
        View targetView = this;
        targetView.getParent().requestChildFocus(targetView, targetView);
    }

    public void setEnableChild(boolean enable) {
        HUtil.recursiveSetEnabled(this, enable);
    }


    public void setSection(Integer pos) {
        if (pos == null || pos < 0)
            return;
        edf_ch_gp.setOnCheckedChangeListener(null);
        if (pos < edf_ch_gp.getChildCount()) {
            mSelectedPos = pos;
            RadioButton radioButton = (RadioButton) edf_ch_gp.getChildAt(pos);
            radioButton.setChecked(true);
        }
        edf_ch_gp.setOnCheckedChangeListener(this);

    }

    public void sendChangedListenerValue() {
        if (hValueChangedListener != null && mSelectedPos != -1)
            hValueChangedListener.onValueChanged(mSelectedPos);
    }


    public boolean isVisible() {
        return getVisibility() == VISIBLE;
    }


    public boolean isVisibleAndEnable() {
        return getVisibility() == VISIBLE && isEnabled();
    }


    public void setSectionByData(String data) {
        int pos = sectionData.indexOf(data);
        if (pos != -1) {
            setSection(pos);
        }

    }


    public String getSelectedData() {
        return sectionData.get(mSelectedPos);
    }

}
