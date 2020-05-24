package in.rajpusht.pc.utils;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import in.rajpusht.pc.R;

public class BottomDialogFragment extends BottomSheetDialogFragment {

    public static BottomDialogFragment newInstance() {
        return new BottomDialogFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_color_info, container,
                false);

        return view;

    }
}