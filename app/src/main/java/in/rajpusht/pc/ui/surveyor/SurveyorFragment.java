package in.rajpusht.pc.ui.surveyor;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import in.rajpusht.pc.R;

public class SurveyorFragment extends Fragment {

    private SurveyorViewModel mViewModel;

    public static SurveyorFragment newInstance() {
        return new SurveyorFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.surveyor_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(SurveyorViewModel.class);
        // TODO: Use the ViewModel
    }

}
