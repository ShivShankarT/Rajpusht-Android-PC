package in.rajpusht.pc;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import javax.inject.Inject;
import javax.inject.Singleton;

import in.rajpusht.pc.data.DataRepository;
import in.rajpusht.pc.ui.home.HomeViewModel;
import in.rajpusht.pc.ui.lm_monitoring.LMMonitoringViewModel;
import in.rajpusht.pc.ui.login.LoginViewModel;
import in.rajpusht.pc.ui.otp.OtpViewModel;
import in.rajpusht.pc.ui.pw_monitoring.PWMonitoringViewModel;
import in.rajpusht.pc.ui.registration.RegistrationViewModel;
import in.rajpusht.pc.ui.splash.SplashScreenViewModel;
import in.rajpusht.pc.utils.rx.SchedulerProvider;


@Singleton
public class ViewModelProviderFactory extends ViewModelProvider.NewInstanceFactory {

    private final DataRepository dataManager;
    private final SchedulerProvider schedulerProvider;

    @Inject
    public ViewModelProviderFactory(DataRepository dataManager,
                                    SchedulerProvider schedulerProvider) {
        this.dataManager = dataManager;
        this.schedulerProvider = schedulerProvider;
    }


    @NonNull
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(SplashScreenViewModel.class)) {
            //noinspection unchecked
            return (T) new SplashScreenViewModel(dataManager, schedulerProvider);
        }
        if (modelClass.isAssignableFrom(LoginViewModel.class)) {
            //noinspection unchecked
            return (T) new LoginViewModel(dataManager, schedulerProvider);
        } else if (modelClass.isAssignableFrom(OtpViewModel.class)) {
            //noinspection unchecked
            return (T) new OtpViewModel(dataManager, schedulerProvider);
        } else if (modelClass.isAssignableFrom(HomeViewModel.class)) {
            //noinspection unchecked
            return (T) new HomeViewModel(dataManager, schedulerProvider);
        } else if (modelClass.isAssignableFrom(RegistrationViewModel.class)) {
            //noinspection unchecked
            return (T) new RegistrationViewModel(dataManager, schedulerProvider);
        } else if (modelClass.isAssignableFrom(PWMonitoringViewModel.class)) {
            //noinspection unchecked
            return (T) new PWMonitoringViewModel(dataManager, schedulerProvider);
        } else if (modelClass.isAssignableFrom(LMMonitoringViewModel.class)) {
            //noinspection unchecked
            return (T) new LMMonitoringViewModel(dataManager, schedulerProvider);
        }

        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }
}