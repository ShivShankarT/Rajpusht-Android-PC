package in.rajpusht.pc.utils.timber;


import com.google.firebase.crashlytics.FirebaseCrashlytics;

import timber.log.Timber;

import static android.util.Log.ERROR;
import static android.util.Log.WARN;

public class ReleaseTree extends Timber.Tree {
    @Override
    protected void log(int priority, String tag, String message, Throwable t) {
        if (priority == ERROR || priority == WARN) {
            FirebaseCrashlytics.getInstance().recordException(t);
        }
    }
}
