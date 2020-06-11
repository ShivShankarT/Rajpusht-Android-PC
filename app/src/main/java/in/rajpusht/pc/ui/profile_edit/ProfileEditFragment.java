package in.rajpusht.pc.ui.profile_edit;

import android.os.Bundle;
import android.util.Pair;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import javax.inject.Inject;

import in.rajpusht.pc.BR;
import in.rajpusht.pc.R;
import in.rajpusht.pc.ViewModelProviderFactory;
import in.rajpusht.pc.data.local.pref.AppPreferencesHelper;
import in.rajpusht.pc.databinding.FragmentProfileEditBinding;
import in.rajpusht.pc.ui.base.BaseFragment;
import in.rajpusht.pc.ui.home.HomeActivity;
import in.rajpusht.pc.utils.Event;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileEditFragment extends BaseFragment<FragmentProfileEditBinding, ProfileEditViewModel> {


    @Inject
    ViewModelProviderFactory factory;
    @Inject
    AppPreferencesHelper appPreferencesHelper;

    public ProfileEditFragment() {
        // Required empty public constructor
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_profile_edit;
    }


    @Override
    public ProfileEditViewModel getViewModel() {
        return new ViewModelProvider(this, factory).get(ProfileEditViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getViewModel().progressDialog.observe(getViewLifecycleOwner(), new Observer<Event<Boolean>>() {
            @Override
            public void onChanged(Event<Boolean> booleanEvent) {
                Boolean contentIfNotHandled = booleanEvent.getContentIfNotHandled();
                if (booleanEvent.hasBeenHandled() && contentIfNotHandled != null && contentIfNotHandled) {
                    showProgressDialog();
                } else {
                    dismissProgressDialog();
                }
            }
        });

        getViewDataBinding().firstNameLy.setText(appPreferencesHelper.getString(AppPreferencesHelper.PREF_FIRST_NAME));
        getViewDataBinding().lastNameLy.setText(appPreferencesHelper.getString(AppPreferencesHelper.PREF_LAST_NAME));
        getViewDataBinding().mobileLy.setText(appPreferencesHelper.getCurrentUserMob());
        getViewDataBinding().mobileLy.setEnableChild(false);

        getViewModel().statusLiveData.observe(getViewLifecycleOwner(), pairEvent -> {
            Pair<Integer, String> data = pairEvent.getContentIfNotHandled();

            if (data != null) {
                getViewDataBinding().firstNameLy.setError(null);
                getViewDataBinding().lastNameLy.setError(null);
                getViewDataBinding().mobileLy.setError(null);
                switch (data.first) {
                    case ProfileEditViewModel.FNAME_ERROR:
                        getViewDataBinding().firstNameLy.setError(data.second);
                        break;
                    case ProfileEditViewModel.LNAME_ERROR:
                        getViewDataBinding().lastNameLy.setError(data.second);
                        break;
                    case ProfileEditViewModel.MOBILE_ERROR:
                        getViewDataBinding().mobileLy.setEnableChild(true);
                        getViewDataBinding().mobileLy.setError(data.second);
                        break;
                    case ProfileEditViewModel.SUCCESS:
                        showAlertDialog(data.second, () -> {
                            HomeActivity homeActivity= (HomeActivity) requireActivity();
                            homeActivity.setNavUiData();
                            requireActivity().onBackPressed();
                        });
                        break;
                    case ProfileEditViewModel.ERROR:
                        showAlertDialog(data.second, null);
                        break;
                }
            }
        });

    }
}
