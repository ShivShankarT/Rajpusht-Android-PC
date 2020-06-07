package in.rajpusht.pc.ui.profile;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

import in.rajpusht.pc.data.DataRepository;
import in.rajpusht.pc.data.local.db.entity.AssignedLocationEntity;
import in.rajpusht.pc.ui.base.BaseViewModel;
import in.rajpusht.pc.utils.rx.SchedulerProvider;
import io.reactivex.functions.Consumer;

public class ProfileViewModel extends BaseViewModel {

    MutableLiveData<List<AssignedLocationEntity>> assignedLocation = new MutableLiveData<>();

    public ProfileViewModel(DataRepository dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
        getData();
    }

    private void getData() {
        getCompositeDisposable().add(getDataManager().getAllAssignedLocation()
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui()).subscribe(new Consumer<List<AssignedLocationEntity>>() {
                    @Override
                    public void accept(List<AssignedLocationEntity> assignedLocationEntities) throws Exception {
                        assignedLocation.postValue(assignedLocationEntities);
                    }
                }));
    }
}
