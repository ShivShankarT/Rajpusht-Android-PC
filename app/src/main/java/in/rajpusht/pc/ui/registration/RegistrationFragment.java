package in.rajpusht.pc.ui.registration;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import java.util.Set;

import javax.inject.Inject;

import in.rajpusht.pc.BR;
import in.rajpusht.pc.R;
import in.rajpusht.pc.ViewModelProviderFactory;
import in.rajpusht.pc.custom.callback.HValidatorListener;
import in.rajpusht.pc.custom.callback.HValueChangedListener;
import in.rajpusht.pc.custom.validator.ValidationStatus;
import in.rajpusht.pc.databinding.RegistrationFragmentBinding;
import in.rajpusht.pc.ui.base.BaseFragment;

public class RegistrationFragment extends BaseFragment<RegistrationFragmentBinding, RegistrationViewModel> {

    @Inject
    ViewModelProviderFactory factory;
    private RegistrationViewModel mViewModel;

    public static RegistrationFragment newInstance() {
        return new RegistrationFragment();
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.registration_fragment;
    }

    @Override
    public RegistrationViewModel getViewModel() {
        mViewModel = new ViewModelProvider(this, factory).get(RegistrationViewModel.class);
        return mViewModel;
    }


    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RegistrationFragmentBinding viewDataBinding = getViewDataBinding();
        Toolbar toolbar=viewDataBinding.toolbarLy.toolbar;
        toolbar.setTitle("Registration");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().onBackPressed();
            }
        });


        viewDataBinding.benfRegStage.sethValueChangedListener(new HValueChangedListener<Integer>() {
            @Override
            public void onValueChanged(Integer data) {
                if (data == 0) {
                    viewDataBinding.benfChildAge.setVisibility(View.GONE);
                    viewDataBinding.benfLmp.setVisibility(View.VISIBLE);
                    viewDataBinding.benfChildDeliveryPlaceType.setVisibility(View.GONE);
                    viewDataBinding.benfChildDeliveryPlace.setVisibility(View.GONE);

                } else if (data == 1) {
                    viewDataBinding.benfChildAge.setVisibility(View.VISIBLE);
                    viewDataBinding.benfLmp.setVisibility(View.GONE);
                    viewDataBinding.benfChildDeliveryPlaceType.setVisibility(View.VISIBLE);
                    viewDataBinding.benfChildDeliveryPlace.setVisibility(View.VISIBLE);

                } else {
                    viewDataBinding.benfChildAge.setVisibility(View.VISIBLE);
                    viewDataBinding.benfLmp.setVisibility(View.VISIBLE);
                    viewDataBinding.benfChildDeliveryPlaceType.setVisibility(View.VISIBLE);
                    viewDataBinding.benfChildDeliveryPlace.setVisibility(View.VISIBLE);
                }

            }
        });
        viewDataBinding.benfChildCount.sethValueChangedListener(new HValueChangedListener<Integer>() {
            @Override
            public void onValueChanged(Integer data) {

                if (data == 0) {
                    viewDataBinding.benfIgmpyCount.setVisibility(View.GONE);
                    //values viewDataBinding.benfPmmvvyCount update count
                } else {
                    viewDataBinding.benfIgmpyCount.setVisibility(View.VISIBLE);
                }

            }
        });
        viewDataBinding.benfChildAge.sethValidatorListener(new HValidatorListener<String>() {
            @Override
            public ValidationStatus isValid(String data) {
                if (data.isEmpty()) {
                    return new ValidationStatus(false, "select counseling");
                }
                return new ValidationStatus(true);
            }
        });

        viewDataBinding.benfRegisteredProgramme.sethValueChangedListener(new HValueChangedListener<Set<Integer>>() {
            @Override
            public void onValueChanged(Set<Integer> data) {

                if (data.contains(0)) {
                    viewDataBinding.benfPmmvvyCount.setVisibility(View.VISIBLE);
                } else {
                    viewDataBinding.benfPmmvvyCount.setVisibility(View.GONE);
                }

                if (data.contains(1)) {
                    viewDataBinding.benfIgmpyCount.setVisibility(View.VISIBLE);
                } else {
                    viewDataBinding.benfIgmpyCount.setVisibility(View.GONE);
                }

                if (data.contains(2)) {
                    viewDataBinding.benfJsyCount.setVisibility(View.VISIBLE);
                } else {
                    viewDataBinding.benfJsyCount.setVisibility(View.GONE);
                }

                if (data.contains(3)) {
                    viewDataBinding.benfRajshriCount.setVisibility(View.VISIBLE);
                } else {
                    viewDataBinding.benfRajshriCount.setVisibility(View.GONE);
                }

                if (data.contains(4)) {
                    viewDataBinding.benfPmmvvyCount.setVisibility(View.GONE);
                    viewDataBinding.benfIgmpyCount.setVisibility(View.GONE);
                    viewDataBinding.benfJsyCount.setVisibility(View.GONE);
                    viewDataBinding.benfRajshriCount.setVisibility(View.GONE);
                    viewDataBinding.benfInstalLy.setVisibility(View.GONE);
                } else {
                    viewDataBinding.benfInstalLy.setVisibility(View.VISIBLE);

                }


            }
        });

        viewDataBinding.benfAgeType.sethValueChangedListener(new HValueChangedListener<Integer>() {
            @Override
            public void onValueChanged(Integer value) {
                if (value == 0) {
                    viewDataBinding.benfAgeDob.setEnableChild(true);
                    viewDataBinding.benfAge.setEnableChild(false);
                } else {
                    viewDataBinding.benfAgeDob.setEnableChild(false);
                    viewDataBinding.benfAge.setEnableChild(true);
                }

            }
        });

        viewDataBinding.benfMobileSelector.sethValueChangedListener(new HValueChangedListener<Set<Integer>>() {
            @Override
            public void onValueChanged(Set<Integer> data) {

                if (data.size() >= 2) {
                    viewDataBinding.benfSelfMobile.setVisibility(View.VISIBLE);
                    viewDataBinding.benfHusMobile.setVisibility(View.VISIBLE);
                } else if (data.isEmpty()) {
                    viewDataBinding.benfSelfMobile.setVisibility(View.GONE);
                    viewDataBinding.benfHusMobile.setVisibility(View.GONE);
                } else if (data.contains(0)) {
                    viewDataBinding.benfSelfMobile.setVisibility(View.VISIBLE);
                    viewDataBinding.benfHusMobile.setVisibility(View.GONE);
                } else if (data.contains(1)) {
                    viewDataBinding.benfHusMobile.setVisibility(View.VISIBLE);
                    viewDataBinding.benfSelfMobile.setVisibility(View.GONE);
                }


            }
        });

        viewDataBinding.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewDataBinding.benfChildAge.validate();
                View targetView = viewDataBinding.benfChildAge;
                targetView.getParent().requestChildFocus(targetView, targetView);

                validate();
            }
        });

    }

    void validate() {
        RegistrationFragmentBinding viewDataBinding = getViewDataBinding();

        viewDataBinding.benfChildCount.validate();
        viewDataBinding.benfRegStage.validate();
        viewDataBinding.benfChildCount.validate();
        viewDataBinding.benfChildAge.validate();
        viewDataBinding.benfRegisteredProgramme.validate();
        viewDataBinding.benfPmmvvyCount.validate();
        viewDataBinding.benfIgmpyCount.validate();
        viewDataBinding.benfJsyCount.validate();
        viewDataBinding.benfRajshriCount.validate();
        viewDataBinding.benfName.validate();
        viewDataBinding.benfHusName.validate();

        viewDataBinding.benfAgeType.validate();
        viewDataBinding.benfAgeDob.validate();
        viewDataBinding.benfAge.validate();

        viewDataBinding.benfMobileSelector.validate();
        viewDataBinding.benfSelfMobile.validate();
        viewDataBinding.benfHusMobile.validate();


        viewDataBinding.benfCaste.validate();
        viewDataBinding.benfEcon.validate();
        viewDataBinding.benfPctsid.validate();
        viewDataBinding.benfBahamashaId.validate();
        viewDataBinding.benfCounseling.validate();
        viewDataBinding.benfLmp.validate();

        viewDataBinding.benfChildDeliveryPlaceType.validate();
        viewDataBinding.benfChildDeliveryPlace.validate();
        viewDataBinding.benfCousSms.validate();


    }
}

