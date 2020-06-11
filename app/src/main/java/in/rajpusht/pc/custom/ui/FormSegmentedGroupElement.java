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
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.core.widget.TextViewCompat;

import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

import in.rajpusht.pc.R;
import in.rajpusht.pc.custom.callback.HValidatorListener;
import in.rajpusht.pc.custom.callback.HValueChangedListener;
import in.rajpusht.pc.custom.utils.HUtil;
import in.rajpusht.pc.custom.validator.ValidationStatus;
import info.hoang8f.android.segmented.SegmentedGroup;

public class FormSegmentedGroupElement extends FrameLayout implements RadioGroup.OnCheckedChangeListener {

    private int mSelectedPos = -1;
    private TextInputLayout edf_txt_inp_ly;
    private HValidatorListener<Integer> hValidatorListener;
    private HValueChangedListener<Integer> hValueChangedListener;
    private boolean required;
    private List<String> sectionData = new ArrayList<>();
    private SegmentedGroup edf_ch_gp;

    public FormSegmentedGroupElement(Context context) {
        super(context);
    }

    public FormSegmentedGroupElement(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public FormSegmentedGroupElement(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public FormSegmentedGroupElement(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }


    private void init(AttributeSet attrs) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.form_segmented_group_with_label, this, true);
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
        edf_ch_gp.removeAllViews();


        for (int i = 0; i < array.length; i++) {
            sectionData.add(String.valueOf(array[i]));
        }

        if (ff_label_text_appearance != 0)
            TextViewCompat.setTextAppearance(labelTv, ff_label_text_appearance);
        for (int i = 0; i < array.length; i++) {
            RadioButton child = new RadioButton(new ContextThemeWrapper(getContext(), R.style.RadioButton), null, 0);
            child.setEnabled(true);
            child.setClickable(true);
            child.setText(array[i]);
            child.setId(i);
            child.getTag(i);
            edf_ch_gp.addView(child);
        }
        edf_ch_gp.setTintColor(getResources().getColor(R.color.colorAccent));
        edf_ch_gp.setOnCheckedChangeListener(this);
        a.recycle();

    }

    public boolean validate() {
        if (required) {
            if (mSelectedPos == -1) {
                edf_txt_inp_ly.setError("Please Select");
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
        edf_ch_gp.setOnCheckedChangeListener(null);
        if (pos < edf_ch_gp.getChildCount()) {
            mSelectedPos = pos;
            RadioButton radioButton = (RadioButton) edf_ch_gp.getChildAt(pos);
            radioButton.setChecked(true);
        }
        edf_ch_gp.setOnCheckedChangeListener(this);
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

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        mSelectedPos = checkedId;
        if (hValueChangedListener != null)
            hValueChangedListener.onValueChanged(mSelectedPos);
        edf_txt_inp_ly.setError(null);

    }

    public void requestFocusAndScroll() {
        View targetView = this;
        targetView.getParent().requestChildFocus(targetView, targetView);
    }

    public boolean isVisible() {
        return getVisibility() == VISIBLE;
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (enabled)
            edf_ch_gp.setTintColor(getResources().getColor(R.color.colorAccent));
        else
            edf_ch_gp.setTintColor(getResources().getColor(R.color.grey_07));
    }

    public void setEnableChild(boolean enable) {
        HUtil.recursiveSetEnabled(this, enable);
        if (enable)
            edf_ch_gp.setTintColor(getResources().getColor(R.color.colorAccent));
        else
            edf_ch_gp.setTintColor(getResources().getColor(R.color.grey_07));
    }

    public boolean isVisibleAndEnable() {
        return getVisibility() == VISIBLE && isEnabled();
    }

}
