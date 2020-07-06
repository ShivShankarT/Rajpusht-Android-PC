package in.rajpusht.pc.ui.home;

import android.content.res.Configuration;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import com.google.android.material.navigation.NavigationView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import in.rajpusht.pc.BR;
import in.rajpusht.pc.R;
import in.rajpusht.pc.ViewModelProviderFactory;
import in.rajpusht.pc.data.DataRepository;
import in.rajpusht.pc.data.local.pref.AppPreferencesHelper;
import in.rajpusht.pc.data.remote.AppConstants;
import in.rajpusht.pc.databinding.ActivityHomeBinding;
import in.rajpusht.pc.ui.base.BaseActivity;
import in.rajpusht.pc.ui.benef_list.BeneficiaryFragment;
import in.rajpusht.pc.ui.change_password.ChangePasswordFragment;
import in.rajpusht.pc.ui.counselling.CounsellingDemoFragment;
import in.rajpusht.pc.ui.other_women.OtherWomenFragment;
import in.rajpusht.pc.ui.profile.ProfileFragment;
import in.rajpusht.pc.ui.sync.SyncFragment;
import in.rajpusht.pc.utils.FragmentUtils;
import in.rajpusht.pc.utils.rx.SchedulerProvider;
import in.rajpusht.pc.worker.SyncDataWorker;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

public class HomeActivity extends BaseActivity<ActivityHomeBinding, HomeViewModel> {

    @Inject
    ViewModelProviderFactory factory;
    @Inject
    AppPreferencesHelper appPreferencesHelper;
    @Inject
    DataRepository dataRepository;
    @Inject
    SchedulerProvider schedulerProvider;
    private HomeViewModel mViewModel;
    private TextView awcName;
    private TextView name;

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_home;
    }


    @Override
    public HomeViewModel getViewModel() {
        mViewModel = new ViewModelProvider(this, factory).get(HomeViewModel.class);
        return mViewModel;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fragment fragment = new BeneficiaryFragment();
        //fragment= PregnancyGraphFragment.newInstance();
        //fragment=new TestAnimationFragment();
        if (savedInstanceState == null)
            FragmentUtils.replaceFragment(this, fragment, R.id.fragment_container, false, false, FragmentUtils.TRANSITION_NONE);

        View.OnClickListener profileClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentUtils.replaceFragment(HomeActivity.this, new ProfileFragment(), R.id.fragment_container, true, false, FragmentUtils.TRANSITION_NONE);
                getViewDataBinding().drawerLayout.closeDrawer(GravityCompat.START);
            }
        };

        NavigationView navigationView1 = getViewDataBinding().navigationView;
        View navigationView = navigationView1.getHeaderView(0);
        navigationView.findViewById(R.id.profile_iv).setOnClickListener(profileClick);
        name = navigationView.findViewById(R.id.pc_name_tv);
        awcName = navigationView.findViewById(R.id.pc_sele_awc);
        TextView email = navigationView.findViewById(R.id.pc_email_tv);
        name.setOnClickListener(profileClick);
        email.setOnClickListener(profileClick);
        awcName.setOnClickListener(profileClick);
        name.setText(appPreferencesHelper.getCurrentUserName());
        email.setText(appPreferencesHelper.getCurrentUserEmail());
        setNavUiData();

        navigationView1.setNavigationItemSelectedListener(item -> {
            getViewDataBinding().drawerLayout.closeDrawer(GravityCompat.START);
            if (item.getItemId() == R.id.nav_select_awc) {
                profileClick.onClick(null);
            } else if (item.getItemId() == R.id.nav_home) {
                FragmentUtils.replaceFragment(this, new BeneficiaryFragment(), R.id.fragment_container, false, false, FragmentUtils.TRANSITION_NONE);
            } else if (item.getItemId() == R.id.nav_other_women) {
                FragmentUtils.replaceFragment(HomeActivity.this, new OtherWomenFragment(), R.id.fragment_container, true, false, FragmentUtils.TRANSITION_NONE);
            } else if (item.getItemId() == R.id.nav_changePassword) {
                FragmentUtils.replaceFragment(HomeActivity.this, new ChangePasswordFragment(), R.id.fragment_container, true, false, FragmentUtils.TRANSITION_NONE);
            } else if (item.getItemId() == R.id.nav_sync) {
                FragmentUtils.replaceFragment(this, new SyncFragment(), R.id.fragment_container, true, false, FragmentUtils.TRANSITION_NONE);
                //syncData();
            }
            if (item.getItemId() == R.id.nav_counselling) {
                FragmentUtils.replaceFragment(this, new CounsellingDemoFragment(), R.id.fragment_container, true, false, FragmentUtils.TRANSITION_NONE);
                //syncData();
            } else if (item.getItemId() == R.id.nav_Logout) {
                syncData(true);
            } else if (item.getItemId() == R.id.nav_download) {
                dataRepository.profileAndBulkDownload().subscribeOn(schedulerProvider.io()).subscribe(new Action() {
                    @Override
                    public void run() throws Exception {

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });
            }

            return false;
        });

        mViewModel.progressLive.observe(this, pairEvent -> {
            Pair<Boolean, String> progWithdata = pairEvent.getContentIfNotHandled();
            if (progWithdata != null) {
                if (progWithdata.first)
                    showProgressDialog();
                else
                    dismissProgressDialog();
                if (!TextUtils.isEmpty(progWithdata.second)) {
                    showMessage(getViewDataBinding().fragmentContainer, progWithdata.second);
                }

            }
        });
        syncData();
        logger();

    }

    public void setNavUiData() {
        name.setText(appPreferencesHelper.getCurrentUserName());
        awcName.setText(appPreferencesHelper.getString("awc_name"));
    }

    public void syncData(boolean isLogOut) {
        mViewModel.syncData(isLogOut);
    }

    public void openDrawer() {
        getViewDataBinding().drawerLayout.openDrawer(GravityCompat.START);
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    void syncData() {

        PeriodicWorkRequest periodicWorkRequest =
                new PeriodicWorkRequest.Builder(SyncDataWorker.class,
                        AppConstants.REPEAT_TIME_INTERVAL_IN_HOURS, AppConstants.REPEAT_TIME_INTERVAL_UNITS, 15, TimeUnit.MINUTES
                )
                        .addTag(AppConstants.SYNC_WORK)
                        .setConstraints(new Constraints.Builder()
                                .setRequiredNetworkType(NetworkType.CONNECTED)
                                .build())
                        .build();

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(AppConstants.SYNC_WORK, ExistingPeriodicWorkPolicy.KEEP, periodicWorkRequest);

    }

    void logger() {
        WorkManager.getInstance(this).getWorkInfosByTagLiveData("sync").observe(this, new Observer<List<WorkInfo>>() {
            @Override
            public void onChanged(List<WorkInfo> workInfos) {
                for (int i = 0; i < workInfos.size(); i++) {
                    WorkInfo w = workInfos.get(i);
                    Log.i("syncsync", "onChanged: " + i + w.toString());
                }

            }
        });
    }


}
