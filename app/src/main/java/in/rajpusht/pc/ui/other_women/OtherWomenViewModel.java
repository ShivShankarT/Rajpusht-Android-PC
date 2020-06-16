package in.rajpusht.pc.ui.other_women;

import androidx.lifecycle.LiveData;

import java.util.List;

import in.rajpusht.pc.data.DataRepository;
import in.rajpusht.pc.model.BefModel;
import in.rajpusht.pc.ui.base.BaseViewModel;
import in.rajpusht.pc.utils.rx.SchedulerProvider;

public class OtherWomenViewModel extends BaseViewModel {

    public OtherWomenViewModel(DataRepository dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
    }


    public LiveData<List<BefModel>> otherWomenBefModels() {

        return getDataManager().otherWomenBefModels();
    }
}
