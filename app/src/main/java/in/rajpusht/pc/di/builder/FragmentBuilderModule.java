package in.rajpusht.pc.di.builder;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import in.rajpusht.pc.ui.benef_list.BeneficiaryFragment;
import in.rajpusht.pc.ui.change_password.ChangePasswordFragment;
import in.rajpusht.pc.ui.forgot_password.ForgotPasswordFragment;
import in.rajpusht.pc.ui.lm_monitoring.LMMonitoringFragment;
import in.rajpusht.pc.ui.login.LoginFragment;
import in.rajpusht.pc.ui.other_women.OtherWomenFragment;
import in.rajpusht.pc.ui.otp.OtpFragment;
import in.rajpusht.pc.ui.pregnancy_graph.PregnancyGraphFragment;
import in.rajpusht.pc.ui.profile.ProfileFragment;
import in.rajpusht.pc.ui.profile_edit.ProfileEditFragment;
import in.rajpusht.pc.ui.pw_monitoring.PWMonitoringFragment;
import in.rajpusht.pc.ui.registration.RegistrationFragment;
import in.rajpusht.pc.ui.sync.SyncFragment;

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
    abstract ForgotPasswordFragment forgotPasswordFragment();

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


    @SuppressWarnings("unused")
    @ContributesAndroidInjector()
    abstract ProfileEditFragment profileEditFragment();

    @SuppressWarnings("unused")
    @ContributesAndroidInjector()
    abstract SyncFragment syncFragment();

    @SuppressWarnings("unused")
    @ContributesAndroidInjector()
    abstract OtherWomenFragment otherWomenFragment();


    @SuppressWarnings("unused")
    @ContributesAndroidInjector()
    abstract PregnancyGraphFragment pregnancyGraphFragment();


}
