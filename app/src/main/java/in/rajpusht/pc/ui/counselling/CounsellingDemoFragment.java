package in.rajpusht.pc.ui.counselling;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import in.rajpusht.pc.R;
import in.rajpusht.pc.databinding.FragmentCounsellingDemoBinding;
import in.rajpusht.pc.model.CounsellingMedia;
import in.rajpusht.pc.ui.animation.CounsellingAnimationFragment;
import in.rajpusht.pc.utils.FragmentUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class CounsellingDemoFragment extends Fragment {

    private FragmentCounsellingDemoBinding vb;

    public CounsellingDemoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        vb = FragmentCounsellingDemoBinding.inflate(inflater, container, false);
        // Inflate the layout for this fragment
        return vb.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        View.OnClickListener onClickListener = v -> {
            CounsellingMedia.isTesting = true;
            CounsellingMedia.counsellingPregId = 0;
            CounsellingMedia.counsellingPregLmp = null;
            if (v == vb.pw1Btn) {
                CounsellingMedia.counsellingSubstage = "PW1";
            } else if (v == vb.pw2Btn) {
                CounsellingMedia.counsellingSubstage = "PW2";
            } else if (v == vb.pw3Btn) {
                CounsellingMedia.counsellingSubstage = "PW3";
            } else if (v == vb.pw4Btn) {
                CounsellingMedia.counsellingSubstage = "PW4";
            } else if (v == vb.lmBtn) {
                CounsellingMedia.counsellingSubstage = "LM1";
            } else if (v == vb.myBtn) {
                CounsellingMedia.counsellingSubstage = "MY";
            }


            FragmentUtils.replaceFragment(requireActivity(),
                    CounsellingAnimationFragment.newInstance(0), R.id.fragment_container,
                    true, false, FragmentUtils.TRANSITION_SLIDE_LEFT_RIGHT);
        };

        vb.pw1Btn.setOnClickListener(onClickListener);
        vb.pw2Btn.setOnClickListener(onClickListener);
        vb.pw3Btn.setOnClickListener(onClickListener);
        vb.pw4Btn.setOnClickListener(onClickListener);


        vb.lmBtn.setOnClickListener(onClickListener);
        vb.myBtn.setOnClickListener(onClickListener);

        vb.noteTv.setText("PW1 - PMVY\n" +
                "PW2 - Weight Gain/IFA/PMVY\n" +
                "PM3 - Weight Gain/IFA/PMVY\n" +
                "PW4 - Weight Gain/IFA/Birth Preparedness/PMVY\n" +
                "LM1 - IFA/Exclusive Breastfeeding/Immunization/Diet Diversity/PMVY\n" +
                "LM2 - IFA/Exclusive Breastfeeding/Immunization/Diet Diversity/PMVY\n" +
                "MY1 - Diet Diversity\n" +
                "MY2 - Diet Diversity\n" +
                "MY3 - Diet Diversity");


    }
}
