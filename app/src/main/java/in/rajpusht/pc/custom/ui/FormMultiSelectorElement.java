package in.rajpusht.pc.custom.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.android.material.textfield.TextInputLayout;

import java.util.HashSet;
import java.util.Set;

import in.rajpusht.pc.R;
import in.rajpusht.pc.custom.callback.HValidatorListener;
import in.rajpusht.pc.custom.callback.HValueChangedListener;
import in.rajpusht.pc.custom.validator.ValidationStatus;

public class FormMultiSelectorElement extends FrameLayout implements CompoundButton.OnCheckedChangeListener {

    Set<Integer> selectedId = new HashSet<>();
    LinearLayout edf_ch_gp;
    private HValidatorListener<Set<Integer>> hValidatorListener;
    private TextInputLayout edf_txt_inp_ly;
    private HValueChangedListener<Set<Integer>> hValueChangedListener;
    private boolean required;
    private int chboxNonePos;

    public FormMultiSelectorElement(Context context) {
        super(context);
        init(null);
    }

    public FormMultiSelectorElement(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public FormMultiSelectorElement(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public FormMultiSelectorElement(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.form_multi_select_with_label, this, true);
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.FormField, 0, 0);
        String label = a.getString(R.styleable.FormField_ff_label);
        String hint = a.getString(R.styleable.FormField_ff_label);
        required = a.getBoolean(R.styleable.FormField_ff_required, false);
        chboxNonePos = a.getInt(R.styleable.FormField_ff_chbox_none_pos, -1);
        boolean isShowDivider = a.getBoolean(R.styleable.FormField_ff_show_divider, true);
        View divider = view.findViewById(R.id.divider);
        if (!isShowDivider) {
            divider.setVisibility(GONE);
        }

        TextView labelTv = view.findViewById(R.id.edf_lab);
        edf_txt_inp_ly = view.findViewById(R.id.edf_txt_inp_ly);
        labelTv.setText(label);
        CharSequence[] s = a.getTextArray(R.styleable.FormField_ff_selections);
        edf_ch_gp = view.findViewById(R.id.edf_ch_gp);
        edf_ch_gp.removeAllViews();

        for (int i = 0; i < s.length; i++) {
            CheckBox child = new CheckBox(getContext());
            child.setText(s[i]);
            child.setId(i);
            child.setOnCheckedChangeListener(this);
            edf_ch_gp.addView(child);
        }

        a.recycle();

    }

    public boolean validate() {
        if (required) {
            if (selectedId.isEmpty()) {
                edf_txt_inp_ly.setError(getContext().getString(R.string.Please_Select));
                return false;
            }
        }
        if (hValidatorListener != null) {
            ValidationStatus valid = hValidatorListener.isValid(selectedIds());
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

    public void sethValidatorListener(HValidatorListener<Set<Integer>> hValidatorListener) {
        this.hValidatorListener = hValidatorListener;
    }

    public void sethValueChangedListener(HValueChangedListener<Set<Integer>> hValueChangedListener) {
        this.hValueChangedListener = hValueChangedListener;
    }

    public Integer[] selectedIdsArray() {
        return selectedId.toArray(new Integer[0]);
    }

    public boolean removeSelectedValue(int pos) {

        if (pos < edf_ch_gp.getChildCount()) {
            CheckBox checkBox = (CheckBox) edf_ch_gp.getChildAt(pos);
            checkBox.setChecked(false);
        }

        return selectedId.remove(pos);
    }

    public void sendChangedListenerValue() {
        if (hValueChangedListener != null && selectedId != null)
            hValueChangedListener.onValueChanged(selectedId);
    }


    public Set<Integer> selectedIds() {
        return selectedId;
    }

    public void setSelectedIds(Set<Integer> selectedPos) {

        for (Integer pos : selectedPos) {

            if (pos < edf_ch_gp.getChildCount()) {
                CheckBox checkBox = (CheckBox) edf_ch_gp.getChildAt(pos);
                checkBox.setChecked(true);
                selectedId.add(pos);
            }
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        int id = buttonView.getId();
        if (isChecked)
            selectedId.add(id);
        else
            selectedId.remove(id);

        if (id == chboxNonePos) {//todo when none
            for (int i = 0; i < edf_ch_gp.getChildCount() - 1; i++) {//skipping last
                View view = edf_ch_gp.getChildAt(i);
                if (view instanceof CheckBox) {
                    CheckBox view1 = (CheckBox) view;
                    view1.setOnCheckedChangeListener(null);
                    if (isChecked)
                        view1.setEnabled(false);
                    else
                        view1.setEnabled(true);
                    view1.setChecked(false);
                    view1.setOnCheckedChangeListener(this);
                }
            }

            if (isChecked) {
                selectedId.clear();
                selectedId.add(id);
            }
        }


        if (hValueChangedListener != null)
            hValueChangedListener.onValueChanged(selectedId);
        edf_txt_inp_ly.setError(null);
    }

    public void changeEleVisible(Pair<Integer, Boolean> posWith) {
        for (int i = 0; i < edf_ch_gp.getChildCount() - 1; i++) {//skipping last
            View view = edf_ch_gp.getChildAt(i);
            if (posWith.first == i) {
                if (posWith.second) {
                    view.setVisibility(VISIBLE);
                } else {
                    view.setVisibility(GONE);
                }

            }
        }

    }

    public void requestFocusAndScroll() {
        View targetView = this;
        targetView.getParent().requestChildFocus(targetView, targetView);
    }


    public boolean isVisible() {
        return getVisibility() == VISIBLE;
    }


    public boolean isVisibleAndEnable() {
        return getVisibility() == VISIBLE && isEnabled();
    }
}
