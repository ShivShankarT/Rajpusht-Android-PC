package in.rajpusht.pc.ui.splash;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import javax.inject.Inject;

import in.rajpusht.pc.BR;
import in.rajpusht.pc.BuildConfig;
import in.rajpusht.pc.R;
import in.rajpusht.pc.ViewModelProviderFactory;
import in.rajpusht.pc.data.DataRepository;
import in.rajpusht.pc.databinding.ActivitySplashScreenBinding;
import in.rajpusht.pc.ui.base.BaseActivity;
import in.rajpusht.pc.ui.home.HomeActivity;
import in.rajpusht.pc.ui.login.LoginActivity;

public class SplashScreenActivity extends BaseActivity<ActivitySplashScreenBinding, SplashScreenViewModel> {

    @Inject
    ViewModelProviderFactory factory;
    @Inject
    DataRepository dataRepository;
    private SplashScreenViewModel screenViewModel;

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_splash_screen;
    }

    @Override
    public SplashScreenViewModel getViewModel() {
        screenViewModel = new ViewModelProvider(this, factory).get(SplashScreenViewModel.class);
        return screenViewModel;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!isTimeAutomatic()&&!BuildConfig.DEBUG){
            showAlertDialog("SET SYSTEM TIME  AUTOMATE FORMAT");
            return;
        }
        new Handler().postDelayed(() -> {
            finish();
            Intent intent;
            if (!dataRepository.getLogin())
                intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
            else
                intent = new Intent(SplashScreenActivity.this, HomeActivity.class);
            startActivity(intent);
        }, 100);

        getViewModel().checkInsetFacilityData();
    }

    public boolean isTimeAutomatic() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return Settings.Global.getInt(getContentResolver(), Settings.Global.AUTO_TIME, 0) == 1;
        } else {
            return android.provider.Settings.System.getInt(getContentResolver(), android.provider.Settings.System.AUTO_TIME, 0) == 1;
        }
    }

}
