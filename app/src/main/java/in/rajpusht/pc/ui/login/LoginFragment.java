package in.rajpusht.pc.ui.login;


import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import javax.inject.Inject;

import in.rajpusht.pc
        .BR;
import in.rajpusht.pc.R;
import in.rajpusht.pc.ViewModelProviderFactory;
import in.rajpusht.pc.databinding.LoginFragmentBinding;
import in.rajpusht.pc.ui.base.BaseFragment;
import in.rajpusht.pc.ui.otp.OtpFragment;
import in.rajpusht.pc.utils.FragmentUtils;

public class LoginFragment extends BaseFragment<LoginFragmentBinding, LoginViewModel> {


    @Inject
    ViewModelProviderFactory factory;
    private LoginViewModel mLoginViewModel;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.login_fragment;
    }


    @Override
    public LoginViewModel getViewModel() {
        mLoginViewModel = new ViewModelProvider(this, factory).get(LoginViewModel.class);
        return mLoginViewModel;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mLoginViewModel._navigateToHome.observe(getViewLifecycleOwner(), evt -> {
            if (evt.getContentIfNotHandled() != null)
                FragmentUtils.replaceFragment((AppCompatActivity) requireActivity(), new OtpFragment(), R.id.container, true, FragmentUtils.TRANSITION_NONE);
        });
    }
}
