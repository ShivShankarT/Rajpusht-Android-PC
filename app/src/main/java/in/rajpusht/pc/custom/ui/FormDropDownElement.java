package in.rajpusht.pc.custom.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.widget.TextViewCompat;

import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import in.rajpusht.pc.R;
import in.rajpusht.pc.custom.callback.HValidatorListener;
import in.rajpusht.pc.custom.callback.HValueChangedListener;
import in.rajpusht.pc.custom.utils.HUtil;
import in.rajpusht.pc.custom.validator.ValidationStatus;

public class FormDropDownElement extends FrameLayout implements AdapterView.OnItemSelectedListener {

    private int mSelectedPos = -1;
    private TextInputLayout edf_txt_inp_ly;
    private HValidatorListener<Integer> hValidatorListener;
    private HValueChangedListener<Integer> hValueChangedListener;
    private boolean required;
    private List<String> sectionData = new ArrayList<>();
    private Spinner edf_ch_gp;

    public FormDropDownElement(Context context) {
        super(context);
    }

    public FormDropDownElement(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public FormDropDownElement(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public FormDropDownElement(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }


    private void init(AttributeSet attrs) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.form_drop_down_with_label, this, true);
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.FormField, 0, 0);
        String label = a.getString(R.styleable.FormField_ff_label);
        String hint = a.getString(R.styleable.FormField_ff_label);
        required = a.getBoolean(R.styleable.FormField_ff_required, false);
        int ff_label_text_appearance = a.getResourceId(R.styleable.FormField_ff_label_text_appearance, 0);
        boolean isShowDivider = a.getBoolean(R.styleable.FormField_ff_show_divider, true);
        View divider = view.findViewById(R.id.divider);
        if (!isShowDivider) {
            divider.setVisibility(GONE);
        }

        TextView labelTv = view.findViewById(R.id.edf_lab);
        edf_txt_inp_ly = view.findViewById(R.id.edf_txt_inp_ly);
        labelTv.setText(label);
        CharSequence[] array = a.getTextArray(R.styleable.FormField_ff_selections);
        edf_ch_gp = view.findViewById(R.id.edf_rad_gp);

        sectionData.add("Select");
        for (int i = 0; i < array.length; i++) {
            sectionData.add(String.valueOf(array[i]));
        }
        if (ff_label_text_appearance != 0)
            TextViewCompat.setTextAppearance(labelTv, ff_label_text_appearance);
        setUiData();
        a.recycle();

    }

    public void setSectionList(List<String> data) {
        mSelectedPos = -1;
        sectionData.clear();
        sectionData.add("Select");
        sectionData.addAll(data);
        setUiData();
    }

    private void setUiData() {

        edf_ch_gp.setOnItemSelectedListener(null);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, sectionData);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        edf_ch_gp.setAdapter(adapter);
        edf_ch_gp.setOnItemSelectedListener(this);

    }

    public boolean validate() {
        if (required) {
            if (mSelectedPos == -1 || mSelectedPos == 0) {
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

    public void setSection(Integer pos) {
        if (pos == null)
            return;
        edf_ch_gp.setOnItemSelectedListener(null);
        mSelectedPos = pos;
        edf_ch_gp.setSelection(pos);
        edf_ch_gp.setOnItemSelectedListener(this);
    }

    public void setSectionByData(String data) {

        //todo
        if (sectionData.size() == 1 && data != null)
            setSectionList(Collections.singletonList(data));


        int pos = sectionData.indexOf(data);
        if (pos != -1) {
            setSection(pos);
        }

    }

    public String getSelectedData() {

        if (mSelectedPos == 0 || mSelectedPos == -1)
            return null;

        if (mSelectedPos < sectionData.size())
            return sectionData.get(mSelectedPos);

        return null;
    }


    public void requestFocusAndScroll() {
        View targetView = this;
        targetView.getParent().requestChildFocus(targetView, targetView);
    }

    public boolean isVisible() {
        return getVisibility() == VISIBLE;
    }

    public void setEnableChild(boolean enable) {
        HUtil.recursiveSetEnabled(this, enable);
    }

    public boolean isVisibleAndEnable() {
        return getVisibility() == VISIBLE && isEnabled();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        mSelectedPos = position;//todo
        if (hValueChangedListener != null)
            hValueChangedListener.onValueChanged(mSelectedPos);
        edf_txt_inp_ly.setError(null);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
