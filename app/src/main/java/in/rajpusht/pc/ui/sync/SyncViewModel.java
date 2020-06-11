package in.rajpusht.pc.ui.sync;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

import in.rajpusht.pc.data.DataRepository;
import in.rajpusht.pc.model.AwcSyncCount;
import in.rajpusht.pc.ui.base.BaseViewModel;
import in.rajpusht.pc.utils.rx.SchedulerProvider;
import io.reactivex.functions.Consumer;

public class SyncViewModel extends BaseViewModel {

    MutableLiveData<List<AwcSyncCount>> syncLiveData = new MutableLiveData<>();

    public SyncViewModel(DataRepository dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);

    }

    public void getData() {

        getCompositeDisposable().add(getDataManager().awcViceSyncData()
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui()).subscribe(new Consumer<List<AwcSyncCount>>() {
                    @Override
                    public void accept(List<AwcSyncCount> awcSyncCounts) throws Exception {
                        syncLiveData.setValue(awcSyncCounts);
                    }
                }));
    }

}
