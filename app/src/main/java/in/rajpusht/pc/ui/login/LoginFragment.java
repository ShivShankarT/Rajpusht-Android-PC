package in.rajpusht.pc.ui.login;


import android.os.Bundle;
import android.util.Pair;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import javax.inject.Inject;

import in.rajpusht.pc.BR;
import in.rajpusht.pc.R;
import in.rajpusht.pc.ViewModelProviderFactory;
import in.rajpusht.pc.databinding.LoginFragmentBinding;
import in.rajpusht.pc.ui.base.BaseFragment;
import in.rajpusht.pc.ui.forgot_password.ForgotPasswordFragment;
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
            Pair<Boolean, String> login = evt.getContentIfNotHandled();
            if (login != null)
                if (login.first)
                    FragmentUtils.replaceFragment((AppCompatActivity) requireActivity(), new OtpFragment(), R.id.container, true, false, FragmentUtils.TRANSITION_NONE);
                else {
                    showMessage(login.second);
                    getViewDataBinding().passwordLayout.setError(login.second);
                }
        });

        mLoginViewModel.errorEmail.observe(getViewLifecycleOwner(), s -> {
            getViewDataBinding().usernameType.setError(s);
        });
        mLoginViewModel.errorPassword.observe(getViewLifecycleOwner(), s -> {
            getViewDataBinding().passwordLayout.setError(s);
        });

        mLoginViewModel.progressDialog.observe(getViewLifecycleOwner(), d -> {
            Boolean progress = d.getContentIfNotHandled();
            if (progress != null && progress) {
                showProgressDialog();
            } else {
                dismissProgressDialog();
            }
        });
        getViewDataBinding().forgotPassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentUtils.replaceFragment((AppCompatActivity) requireActivity(), new ForgotPasswordFragment(), R.id.container, true, false, FragmentUtils.TRANSITION_NONE);
            }
        });
    }

}
