package in.rajpusht.pc.di.builder;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import in.rajpusht.pc.ui.benef_list.BeneficiaryFragment;
import in.rajpusht.pc.ui.change_password.ChangePasswordFragment;
import in.rajpusht.pc.ui.lm_monitoring.LMMonitoringFragment;
import in.rajpusht.pc.ui.login.LoginFragment;
import in.rajpusht.pc.ui.otp.OtpFragment;
import in.rajpusht.pc.ui.profile.ProfileFragment;
import in.rajpusht.pc.ui.pw_monitoring.PWMonitoringFragment;
import in.rajpusht.pc.ui.registration.RegistrationFragment;

@Module
public abstract class FragmentBuilderModule {

    @SuppressWarnings("unused")
    @ContributesAndroidInjector()
    abstract LoginFragment loginFragment();

    @SuppressWarnings("unused")
    @ContributesAndroidInjector()
    abstract OtpFragment otpFragment();

    @SuppressWarnings("unused")
    @ContributesAndroidInjector()
    abstract RegistrationFragment registrationFragment();

    @SuppressWarnings("unused")
    @ContributesAndroidInjector()
    abstract BeneficiaryFragment beneficiaryFragment();

    @SuppressWarnings("unused")
    @ContributesAndroidInjector()
    abstract PWMonitoringFragment pwMonitoringFragment();

    @SuppressWarnings("unused")
    @ContributesAndroidInjector()
    abstract LMMonitoringFragment lmMonitoringFragment();

    @SuppressWarnings("unused")
    @ContributesAndroidInjector()
    abstract ChangePasswordFragment changePasswordFragment();

    @SuppressWarnings("unused")
    @ContributesAndroidInjector()
    abstract ProfileFragment profileFragment();


}
