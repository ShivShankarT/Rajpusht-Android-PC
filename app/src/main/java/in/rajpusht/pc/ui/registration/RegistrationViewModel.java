package in.rajpusht.pc.ui.registration;

import androidx.lifecycle.ViewModel;

import in.rajpusht.pc.data.DataRepository;
import in.rajpusht.pc.ui.base.BaseViewModel;
import in.rajpusht.pc.utils.rx.SchedulerProvider;

public class RegistrationViewModel extends BaseViewModel {

    public RegistrationViewModel(DataRepository dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
    }
    // TODO: Implement the ViewModel

}
