package in.rajpusht.pc.di.builder;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import in.rajpusht.pc.ui.home.HomeActivity;
import in.rajpusht.pc.ui.login.LoginActivity;
import in.rajpusht.pc.ui.splash.SplashScreenActivity;

@Module
public abstract class ActivityBuilderModule {
    @SuppressWarnings("unused")
    @ContributesAndroidInjector(modules = FragmentBuilderModule.class)//todo
    abstract HomeActivity homeActivity();

    @ContributesAndroidInjector(modules = FragmentBuilderModule.class)
    abstract LoginActivity loginActivity();

    @ContributesAndroidInjector
    abstract SplashScreenActivity bindSplashActivity();

}
