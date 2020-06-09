package in.rajpusht.pc.ui.home;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Pair;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.navigation.NavigationView;

import javax.inject.Inject;

import in.rajpusht.pc.BR;
import in.rajpusht.pc.R;
import in.rajpusht.pc.ViewModelProviderFactory;
import in.rajpusht.pc.data.DataRepository;
import in.rajpusht.pc.data.local.pref.AppPreferencesHelper;
import in.rajpusht.pc.databinding.ActivityHomeBinding;
import in.rajpusht.pc.ui.base.BaseActivity;
import in.rajpusht.pc.ui.benef_list.BeneficiaryFragment;
import in.rajpusht.pc.ui.change_password.ChangePasswordFragment;
import in.rajpusht.pc.ui.login.LoginActivity;
import in.rajpusht.pc.ui.profile.ProfileFragment;
import in.rajpusht.pc.utils.FragmentUtils;
import in.rajpusht.pc.utils.rx.SchedulerProvider;
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
        TextView name = navigationView.findViewById(R.id.pc_name_tv);
        awcName = navigationView.findViewById(R.id.pc_sele_awc);
        TextView email = navigationView.findViewById(R.id.pc_email_tv);
        name.setOnClickListener(profileClick);
        email.setOnClickListener(profileClick);
        awcName.setOnClickListener(profileClick);
        name.setText(appPreferencesHelper.getCurrentUserName());
        email.setText(appPreferencesHelper.getCurrentUserEmail());
        setNavUiData();

        navigationView1.setNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_select_awc) {
                profileClick.onClick(null);
            } else if (item.getItemId() == R.id.nav_home) {
                FragmentUtils.replaceFragment(this, new BeneficiaryFragment(), R.id.fragment_container, false, false, FragmentUtils.TRANSITION_NONE);
            } else if (item.getItemId() == R.id.nav_changePassword) {
                FragmentUtils.replaceFragment(HomeActivity.this, new ChangePasswordFragment(), R.id.fragment_container, true, false, FragmentUtils.TRANSITION_NONE);
            } else if (item.getItemId() == R.id.nav_sync) {
                syncData();
            } else if (item.getItemId() == R.id.nav_Logout) {
                syncData();
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
            getViewDataBinding().drawerLayout.closeDrawer(GravityCompat.START);
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

    }

    public void setNavUiData() {
        awcName.setText(appPreferencesHelper.getString("awc_name"));
    }

    public void syncData() {
        mViewModel.syncData();
    }

    public void openDrawer() {
        getViewDataBinding().drawerLayout.openDrawer(GravityCompat.START);
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}
