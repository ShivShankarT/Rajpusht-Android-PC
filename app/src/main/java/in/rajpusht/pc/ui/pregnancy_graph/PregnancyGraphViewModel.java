package in.rajpusht.pc.ui.pregnancy_graph;

import androidx.lifecycle.ViewModel;

import in.rajpusht.pc.data.DataRepository;
import in.rajpusht.pc.ui.base.BaseViewModel;
import in.rajpusht.pc.utils.rx.SchedulerProvider;

public class PregnancyGraphViewModel extends BaseViewModel {
    public PregnancyGraphViewModel(DataRepository dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
    }
    // TODO: Implement the ViewModel
}
