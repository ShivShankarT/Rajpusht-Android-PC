package in.rajpusht.pc.ui.otp;

import in.rajpusht.pc.data.DataRepository;
import in.rajpusht.pc.ui.base.BaseViewModel;
import in.rajpusht.pc.utils.Event;
import in.rajpusht.pc.utils.SingleLiveEvent;
import in.rajpusht.pc.utils.rx.SchedulerProvider;

public class OtpViewModel extends BaseViewModel {
    public SingleLiveEvent<Event<Boolean>> _navigateToHome = new SingleLiveEvent<>();

    public OtpViewModel(DataRepository dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
    }
    // TODO: Implement the ViewModel

    public void verifyOtp(String opt) {
        _navigateToHome.postValue(new Event<Boolean>(true));
    }
}
