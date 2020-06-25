package in.rajpusht.pc.ui.splash;

import in.rajpusht.pc.data.DataRepository;
import in.rajpusht.pc.ui.base.BaseViewModel;
import in.rajpusht.pc.utils.rx.SchedulerProvider;
import io.reactivex.functions.BiConsumer;

public class SplashScreenViewModel extends BaseViewModel {
    public SplashScreenViewModel(DataRepository dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
    }

    public void checkInsetFacilityData() {
        getDataManager().insertFacilityData().subscribeOn(getSchedulerProvider().io()).subscribeOn(getSchedulerProvider().ui()).subscribe(new BiConsumer<Boolean, Throwable>() {
            @Override
            public void accept(Boolean aBoolean, Throwable throwable) throws Exception {
                if (throwable != null)
                    throwable.printStackTrace();

            }
        });
    }
}
