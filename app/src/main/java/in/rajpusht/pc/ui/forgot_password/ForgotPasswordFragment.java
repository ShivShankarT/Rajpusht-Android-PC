package in.rajpusht.pc.ui.forgot_password;

import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.transition.TransitionManager;

import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import javax.inject.Inject;

import in.rajpusht.pc.BR;
import in.rajpusht.pc.R;
import in.rajpusht.pc.ViewModelProviderFactory;
import in.rajpusht.pc.data.local.pref.AppPreferencesHelper;
import in.rajpusht.pc.databinding.ForgotPasswordFragmentBinding;
import in.rajpusht.pc.ui.base.BaseFragment;
import in.rajpusht.pc.utils.Event;
import in.rajpusht.pc.utils.event_bus.EventBusLiveData;
import in.rajpusht.pc.utils.event_bus.MessageEvent;

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

        Toolbar toolbar = getViewDataBinding().toolbarLy.toolbar;
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().onBackPressed();
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
        startSMSListener();
        EventBusLiveData.getAddEvtListeners(this, new Observer<MessageEvent>() {
            @Override
            public void onChanged(MessageEvent messageEvent) {
                if (messageEvent.getEventType() == MessageEvent.MessageEventType.OTP_REC_SUCCESS) {
                    String otp = messageEvent.getId();
                    getViewDataBinding().otpTxtly.setText(otp);
                }
            }
        });
    }

    void startSMSListener() {
        // Get an instance of SmsRetrieverClient, used to start listening for a matching
        // SMS message.
        SmsRetrieverClient client = SmsRetriever.getClient(requireContext() /* context */);
        // Starts SmsRetriever, which waits for ONE matching SMS message until timeout
        // (5 minutes). The matching SMS message will be sent via a Broadcast Intent with
        // action SmsRetriever#SMS_RETRIEVED_ACTION.
        Task<Void> task = client.startSmsRetriever();
        // Listen for success/failure of the start Task. If in a background thread, this
        // can be made blocking using Tasks.await(task, [timeout]);
        task.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // Successfully started retriever, expect broadcast intent
                // ...
            }
        });

        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Failed to start retriever, inspect Exception for more details
                // ...
            }
        });
    }

}
