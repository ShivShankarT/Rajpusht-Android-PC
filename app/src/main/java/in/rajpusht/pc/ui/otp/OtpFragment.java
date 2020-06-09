package in.rajpusht.pc.ui.otp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import javax.inject.Inject;

import in.rajpusht.pc.BR;
import in.rajpusht.pc.R;
import in.rajpusht.pc.ViewModelProviderFactory;
import in.rajpusht.pc.data.local.pref.AppPreferencesHelper;
import in.rajpusht.pc.databinding.OtpFragmentBinding;
import in.rajpusht.pc.ui.base.BaseFragment;
import in.rajpusht.pc.ui.home.HomeActivity;

public class OtpFragment extends BaseFragment<OtpFragmentBinding, OtpViewModel> {

    @Inject
    ViewModelProviderFactory factory;

    @Inject
    AppPreferencesHelper appPreferencesHelper;

    public static OtpFragment newInstance() {
        return new OtpFragment();
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.otp_fragment;
    }


    @Override
    public OtpViewModel getViewModel() {
        OtpViewModel otpViewModel = new ViewModelProvider(this, factory).get(OtpViewModel.class);
        return otpViewModel;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getViewDataBinding().optEt.setText(appPreferencesHelper.getString("otp"));
        getViewModel()._navigateToHome.observe(getViewLifecycleOwner(), (d) -> {
            Pair<Boolean, String> contentIfNotHandled = d.getContentIfNotHandled();
            if (contentIfNotHandled != null) {
                if(contentIfNotHandled.first) {
                    requireActivity().finish();
                    startActivity(new Intent(requireContext(), HomeActivity.class));
                }
                showMessage(contentIfNotHandled.second);

            }
        });

        getViewModel().progressDialog.observe(getViewLifecycleOwner(),d->{
            Boolean progress = d.getContentIfNotHandled();
            if (progress!=null&&progress){
                showProgressDialog();
            }else {
                dismissProgressDialog();
            }
        });

    }
}
