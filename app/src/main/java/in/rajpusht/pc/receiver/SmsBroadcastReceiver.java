package in.rajpusht.pc.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.Status;

import in.rajpusht.pc.utils.event_bus.EventBusLiveData;
import in.rajpusht.pc.utils.event_bus.MessageEvent;

public class SmsBroadcastReceiver extends BroadcastReceiver {

    private static final String TAG = SmsBroadcastReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "onReceive: " + intent);

        if (SmsRetriever.SMS_RETRIEVED_ACTION.equals(intent.getAction())) {
            Bundle extras = intent.getExtras();
            Status status = null;
            if (extras != null) {
                status = (Status) extras.get(SmsRetriever.EXTRA_STATUS);
            }
            if (status != null) {
                switch (status.getStatusCode()) {
                    case CommonStatusCodes.SUCCESS:
                        // Get SMS message contents
                        String message = extras.getString(SmsRetriever.EXTRA_SMS_MESSAGE);
                        if (TextUtils.isEmpty(message))
                            return;
                        Log.i(TAG, "onReceive:message:" + message);
                        //<#>  Hello POSHAN Champion, 697130 is your OTP for mobile verification on RajPusht PC App. NXjWbXQqwib
                        String otp = message.replace("<#>  Hello POSHAN Champion, ", "");
                        String otpN = otp.substring(0, 6);
                        try {
                            long l = Long.parseLong(otpN);
                            EventBusLiveData.postMessage(MessageEvent.getMessageEvent(MessageEvent.MessageEventType.OTP_REC_SUCCESS, String.valueOf(l)));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        // Extract one-time code from the message and complete verification
                        // by sending the code back to your server.
                        break;
                    case CommonStatusCodes.TIMEOUT:
                        // Waiting for SMS timed out (5 minutes)
                        // Handle the error ...
                        break;
                }
            }
        }
    }
}
