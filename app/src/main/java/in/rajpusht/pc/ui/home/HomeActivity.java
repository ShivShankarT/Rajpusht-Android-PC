package in.rajpusht.pc.ui.home;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.navigation.NavigationView;

import javax.inject.Inject;

import in.rajpusht.pc.BR;
import in.rajpusht.pc.R;
import in.rajpusht.pc.ViewModelProviderFactory;
import in.rajpusht.pc.databinding.ActivityHomeBinding;
import in.rajpusht.pc.ui.base.BaseActivity;
import in.rajpusht.pc.ui.benef_list.BeneficiaryFragment;
import in.rajpusht.pc.ui.change_password.ChangePasswordFragment;
import in.rajpusht.pc.ui.profile.ProfileFragment;
import in.rajpusht.pc.utils.FragmentUtils;

public class HomeActivity extends BaseActivity<ActivityHomeBinding, HomeViewModel> {

    @Inject
    ViewModelProviderFactory factory;

    private HomeViewModel mViewModel;

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
        FragmentUtils.replaceFragment(this, new BeneficiaryFragment(), R.id.fragment_container, false, FragmentUtils.TRANSITION_NONE);

        View.OnClickListener profileClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentUtils.replaceFragment(HomeActivity.this, new ProfileFragment(), R.id.fragment_container, true, FragmentUtils.TRANSITION_NONE);
                getViewDataBinding().drawerLayout.closeDrawer(GravityCompat.START);
            }
        };

        NavigationView navigationView1 = getViewDataBinding().navigationView;
        View navigationView = navigationView1.getHeaderView(0);
        navigationView.findViewById(R.id.profile_iv).setOnClickListener(profileClick);
        navigationView.findViewById(R.id.pc_name_tv).setOnClickListener(profileClick);
        navigationView.findViewById(R.id.pc_email_tv).setOnClickListener(profileClick);
        navigationView.findViewById(R.id.pc_sele_awc).setOnClickListener(profileClick);

        navigationView1.setNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_select_awc) {
                profileClick.onClick(null);
            } else if (item.getItemId() == R.id.nav_home) {
                FragmentUtils.replaceFragment(this, new BeneficiaryFragment(), R.id.fragment_container, false, FragmentUtils.TRANSITION_NONE);
            } else if (item.getItemId() == R.id.nav_changePassword) {
                FragmentUtils.replaceFragment(HomeActivity.this, new ChangePasswordFragment(), R.id.fragment_container, true, FragmentUtils.TRANSITION_NONE);

            }
            getViewDataBinding().drawerLayout.closeDrawer(GravityCompat.START);
            return false;
        });

    }

    public void openDrawer() {
        getViewDataBinding().drawerLayout.openDrawer(GravityCompat.START);
    }
}
