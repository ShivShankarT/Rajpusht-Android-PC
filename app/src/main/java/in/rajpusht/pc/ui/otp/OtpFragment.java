package in.rajpusht.pc.ui.otp;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.util.Pair;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

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
import in.rajpusht.pc.databinding.OtpFragmentBinding;
import in.rajpusht.pc.ui.base.BaseFragment;
import in.rajpusht.pc.ui.home.HomeActivity;
import in.rajpusht.pc.ui.login.LoginActivity;
import in.rajpusht.pc.utils.event_bus.EventBusLiveData;
import in.rajpusht.pc.utils.event_bus.MessageEvent;

public class OtpFragment extends BaseFragment<OtpFragmentBinding, OtpViewModel> {

    private static final String TAG = OtpFragment.class.getName();
    @Inject
    ViewModelProviderFactory factory;

    @Inject
    AppPreferencesHelper appPreferencesHelper;
    CountDownTimer countDownTimer;

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
            hideKeyboard();
            Pair<Integer, String> contentIfNotHandled = d.getContentIfNotHandled();
            if (contentIfNotHandled != null) {
                if (contentIfNotHandled.first == OtpViewModel.LAUNCH_HOME) {
                    requireActivity().finish();
                    startActivity(new Intent(requireContext(), HomeActivity.class));
                } else if (contentIfNotHandled.first == OtpViewModel.LIMIT_REACHED) {
                    showAlertDialog(contentIfNotHandled.second, () -> {
                        requireActivity().finish();
                        startActivity(new Intent(requireContext(), LoginActivity.class));
                    });
                    return;
                }
                showMessage(contentIfNotHandled.second);


            }
        });

        getViewModel().progressDialog.observe(getViewLifecycleOwner(), d -> {
            hideKeyboard();
            Boolean progress = d.getContentIfNotHandled();
            if (progress != null && progress) {
                showProgressDialog();
            } else {
                dismissProgressDialog();
            }
        });


        getViewModel().resendOtp.observe(getViewLifecycleOwner(), d -> {
            Boolean isSuccess = d.getContentIfNotHandled();
            if (isSuccess != null && isSuccess) {
                getViewDataBinding().resendOTP.setEnabled(false);
                startTimer();
            } else {

            }
        });


        startTimer();
        startSMSListener();
        EventBusLiveData.getAddEvtListeners(this, new Observer<MessageEvent>() {
            @Override
            public void onChanged(MessageEvent messageEvent) {
                if (messageEvent.getEventType() == MessageEvent.MessageEventType.OTP_REC_SUCCESS) {
                    String otp = messageEvent.getId();
                    Log.i(TAG, "onChanged: " + otp);
                    getViewDataBinding().optEt.setText(otp);
                }
            }
        });

    }

    private void startTimer() {
        cancelTimer();
        countDownTimer = new CountDownTimer((long) (15 * 60 * 1000), 1000) {
            public void onTick(long millisUntilFinished) {
                millisUntilFinished = millisUntilFinished / 1000;
                long min = millisUntilFinished / (60);
                long sec;
                if (min != 0)
                    sec = millisUntilFinished % (min * 60);
                else
                    sec = millisUntilFinished;

                getViewDataBinding().resendOTP.setText("Resend in " + min + ":" + sec);
            }

            public void onFinish() {
                getViewDataBinding().resendOTP.setText("Resend");
                getViewDataBinding().resendOTP.setEnabled(true);
            }

        }.start();

    }

    private void cancelTimer() {
        if (countDownTimer != null)
            countDownTimer.cancel();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        cancelTimer();
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
                Log.i(TAG, "onSuccess: ");
                // Successfully started retriever, expect broadcast intent
                // ...
            }
        });

        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "onFailure: ", e);
                // Failed to start retriever, inspect Exception for more details
                // ...
            }
        });
    }
}
