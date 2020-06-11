package in.rajpusht.pc.ui.forgot_password;

import android.os.Bundle;
import android.util.Pair;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.transition.TransitionManager;

import javax.inject.Inject;

import in.rajpusht.pc.BR;
import in.rajpusht.pc.R;
import in.rajpusht.pc.ViewModelProviderFactory;
import in.rajpusht.pc.data.local.pref.AppPreferencesHelper;
import in.rajpusht.pc.databinding.ForgotPasswordFragmentBinding;
import in.rajpusht.pc.ui.base.BaseFragment;
import in.rajpusht.pc.utils.Event;

public class ForgotPasswordFragment extends BaseFragment<ForgotPasswordFragmentBinding, ForgotPasswordViewModel> {

    @Inject
    ViewModelProviderFactory factory;
    @Inject
    AppPreferencesHelper appPreferencesHelper;


    public static ForgotPasswordFragment newInstance() {
        return new ForgotPasswordFragment();
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.forgot_password_fragment;
    }


    @Override
    public ForgotPasswordViewModel getViewModel() {
        return new ViewModelProvider(this, factory).get(ForgotPasswordViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getViewModel().progressDialog.observe(getViewLifecycleOwner(), new Observer<Event<Boolean>>() {
            @Override
            public void onChanged(Event<Boolean> booleanEvent) {
                Boolean contentIfNotHandled = booleanEvent.getContentIfNotHandled();
                if (booleanEvent.hasBeenHandled() && contentIfNotHandled != null && contentIfNotHandled) {
                    showProgressDialog();
                } else {
                    dismissProgressDialog();
                }

            }
        });
        getViewModel().statusLiveData.observe(getViewLifecycleOwner(), new Observer<Event<Pair<Integer, String>>>() {
            @Override
            public void onChanged(Event<Pair<Integer, String>> pairEvent) {
                Pair<Integer, String> data = pairEvent.getContentIfNotHandled();

                if (data != null) {
                    getViewDataBinding().emailTly.setError(null);
                    getViewDataBinding().otpTxtly.setError(null);
                    getViewDataBinding().newPasswordLy.setError(null);
                    getViewDataBinding().confirmPasswordLy.setError(null);


                    switch (data.first) {
                        case ForgotPasswordViewModel.ERROR_EMAIL:
                            getViewDataBinding().emailTly.setError(data.second);
                            break;
                        case ForgotPasswordViewModel.ERROR_OTP:
                            getViewDataBinding().otpTxtly.setError(data.second);
                            break;
                        case ForgotPasswordViewModel.ERROR_NEW_PASSWORD:
                            getViewDataBinding().newPasswordLy.setError(data.second);
                            break;
                        case ForgotPasswordViewModel.ERROR_CONFIRM_PASSWORD:
                            getViewDataBinding().confirmPasswordLy.setError(data.second);
                            break;
                        case ForgotPasswordViewModel.OPT_SEND:
                            TransitionManager.beginDelayedTransition(getViewDataBinding().ll);
                            getViewDataBinding().emailTly.setEnableChild(false);
                            getViewDataBinding().submitBtn.setEnabled(false);
                            getViewDataBinding().otpTxtly.setVisibility(View.VISIBLE);
                            getViewDataBinding().newPasswordLy.setVisibility(View.VISIBLE);
                            getViewDataBinding().confirmPasswordLy.setVisibility(View.VISIBLE);
                            getViewDataBinding().submitPasswordBtn.setVisibility(View.VISIBLE);
                            showMessage(data.second);
                            getViewDataBinding().otpTxtly.setText(appPreferencesHelper.getString("otp"));
                            break;
                        case ForgotPasswordViewModel.PASSWORD_CHANGED:
                            showAlertDialog(data.second, () -> requireActivity().onBackPressed());
                            break;
                        case ForgotPasswordViewModel.ERROR:
                            showAlertDialog(data.second, null);
                            break;
                    }
                }
            }
        });
    }
}
