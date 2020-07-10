package in.rajpusht.pc.utils.event_bus;

import android.util.Log;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.OnLifecycleEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class EventBusLiveData<T> implements LifecycleObserver {
    private static final String TAG = EventBusLiveData.class.getSimpleName();
    private final Observer<T> observer;

    private EventBusLiveData(LifecycleOwner lifecycleOwner, Observer<T> observer) {
        lifecycleOwner.getLifecycle().addObserver(this);
        this.observer = observer;
    }

    public static <D> EventBusLiveData getAddEvtListeners(LifecycleOwner lifecycleOwner, Observer<D> observer) {
        return new EventBusLiveData<>(lifecycleOwner, observer);
    }

    // EventBusLiveData.postMessage(MessageEvent.getMessageEvent(MessageEvent.MessageEventType.POST_FAV,post_id));
    public static <T> void postMessage(T eventData) {
        Log.i(TAG, "postMessage: " + eventData);
        EventBus.getDefault().post(eventData);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    void addListener() {
        Log.i(TAG, "addListener: ");
        EventBus.getDefault().register(this);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    void removeListener() {
        Log.i(TAG, "removeListener: ");
        EventBus.getDefault().unregister(this);
    }

    ;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(T event) {
        Log.i(TAG, "onMessageEvent: " + event);
        observer.onChanged(event);
    }


}
