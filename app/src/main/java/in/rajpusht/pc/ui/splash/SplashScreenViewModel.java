package in.rajpusht.pc.ui.splash;

import in.rajpusht.pc.data.DataRepository;
import in.rajpusht.pc.ui.base.BaseViewModel;
import in.rajpusht.pc.utils.rx.SchedulerProvider;
import io.reactivex.functions.BiConsumer;
import kotlin.Triple;

public class SplashScreenViewModel extends BaseViewModel<SplashNav> {
    public SplashScreenViewModel(DataRepository dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
    }

    public void appConfigVersion() {
        getDataManager().appConfigVersion()
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(new BiConsumer<Triple<Integer, Integer, String>, Throwable>() {
                    @Override
                    public void accept(Triple<Integer, Integer, String> integerIntegerStringTriple, Throwable throwable) throws Exception {
                        if (throwable != null)
                            throwable.printStackTrace();
                        if (integerIntegerStringTriple != null && throwable == null) {
                            getNavigator().appConfigFetch(true);
                        } else {
                            getNavigator().appConfigFetch(false);
                        }
                    }
                });
    }

}
