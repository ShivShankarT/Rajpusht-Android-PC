package in.rajpusht.pc.ui.registration;

import android.os.Bundle;
import android.util.Pair;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import in.rajpusht.pc.BR;
import in.rajpusht.pc.R;
import in.rajpusht.pc.ViewModelProviderFactory;
import in.rajpusht.pc.custom.callback.HValidatorListener;
import in.rajpusht.pc.custom.callback.HValueChangedListener;
import in.rajpusht.pc.custom.utils.HUtil;
import in.rajpusht.pc.custom.validator.FormValidatorUtils;
import in.rajpusht.pc.custom.validator.ValidationStatus;
import in.rajpusht.pc.data.DataRepository;
import in.rajpusht.pc.data.local.db.entity.BeneficiaryEntity;
import in.rajpusht.pc.data.local.db.entity.ChildEntity;
import in.rajpusht.pc.data.local.db.entity.PregnantEntity;
import in.rajpusht.pc.databinding.RegistrationFragmentBinding;
import in.rajpusht.pc.model.DataStatus;
import in.rajpusht.pc.ui.base.BaseFragment;
import in.rajpusht.pc.utils.rx.SchedulerProvider;
import io.reactivex.disposables.Disposable;

public class RegistrationFragment extends BaseFragment<RegistrationFragmentBinding, RegistrationViewModel> {

    @Inject
    ViewModelProviderFactory factory;
    @Inject
    DataRepository dataRepository;
    @Inject
    SchedulerProvider schedulerProvider;
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
        Toolbar toolbar = viewDataBinding.toolbarLy.toolbar;
        toolbar.setTitle("Registration");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().onBackPressed();
            }
        });


        viewDataBinding.benfAgeDob.sethValidatorListener(new HValidatorListener<Date>() {
            @Override
            public ValidationStatus isValid(Date data) {
                int age = HUtil.calcAge(data);
                if (age < 13) {
                    return new ValidationStatus(false, "Beneficiary age should be greater then 13");
                } else {
                    return new ValidationStatus(true);
                }


            }
        });

        viewDataBinding.benfHusMobile.sethValidatorListener(FormValidatorUtils.textEqualValidator(10, "Invalid Mobile No"));
        viewDataBinding.benfSelfMobile.sethValidatorListener(FormValidatorUtils.textEqualValidator(10, "Invalid Mobile No"));
        viewDataBinding.benfName.sethValidatorListener(FormValidatorUtils.textBwValidator(5, 100, "Invalid Name"));
        viewDataBinding.benfHusName.sethValidatorListener(FormValidatorUtils.textBwValidator(5, 100, "Invalid Name"));


        viewDataBinding.benfRegStage.sethValueChangedListener(new HValueChangedListener<Integer>() {
            @Override
            public void onValueChanged(Integer data) {
                if (data == 0) {
                    viewDataBinding.benfChildDob.setVisibility(View.GONE);
                    viewDataBinding.benfLmp.setVisibility(View.VISIBLE);
                    viewDataBinding.benfChildDeliveryPlaceType.setVisibility(View.GONE);
                    viewDataBinding.benfChildDeliveryPlace.setVisibility(View.GONE);

                } else if (data == 1) {
                    viewDataBinding.benfChildDob.setVisibility(View.VISIBLE);
                    viewDataBinding.benfLmp.setVisibility(View.GONE);
                    viewDataBinding.benfChildDeliveryPlaceType.setVisibility(View.VISIBLE);
                    viewDataBinding.benfChildDeliveryPlace.setVisibility(View.VISIBLE);

                } else {
                    viewDataBinding.benfChildDob.setVisibility(View.VISIBLE);
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
        viewDataBinding.benfChildDob.sethValidatorListener(new HValidatorListener<Date>() {
            @Override
            public ValidationStatus isValid(Date data) {

                int age = HUtil.calcAge(data);
                if (age > 1) {
                    return new ValidationStatus(false, "Child age should be less then 1 year");
                } else {
                    return new ValidationStatus(true);
                }
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

        viewDataBinding.benfAgeDob.sethValueChangedListener(new HValueChangedListener<Date>() {
            @Override
            public void onValueChanged(Date data) {

                int age = HUtil.calcAge(data);
                viewDataBinding.benfAge.setText("" + age);

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
                validate();
            }
        });

    }

    void save() {


        long beneficiaryId = System.currentTimeMillis();
        ChildEntity childEntity = null;
        PregnantEntity pregnantEntity = null;

        RegistrationFragmentBinding vb = getViewDataBinding();
        BeneficiaryEntity beneficiaryEntity = new BeneficiaryEntity();
        beneficiaryEntity.setBeneficiaryId(beneficiaryId);
        beneficiaryEntity.setName(vb.benfName.getText());
        beneficiaryEntity.setHusbandName(vb.benfHusName.getText());
        beneficiaryEntity.setDOB(vb.benfAgeDob.getDate());
        beneficiaryEntity.setAge(Integer.valueOf(vb.benfAge.getText()));
        beneficiaryEntity.setChildCount(vb.benfChildCount.getSelectedPos());
        beneficiaryEntity.setMobileNo(vb.benfSelfMobile.getText());
        beneficiaryEntity.setHusbandMobNo(vb.benfHusMobile.getText());
        beneficiaryEntity.setCaste(vb.benfCaste.getSelectedData());
        beneficiaryEntity.setEconomic(vb.benfEcon.getSelectedData());
        beneficiaryEntity.setPctsId(vb.benfPctsid.getText());
        beneficiaryEntity.setBahamashahId(vb.benfBahamashaId.getText());
        beneficiaryEntity.setCounselingProv(vb.benfCounseling.getSelectedData());
        beneficiaryEntity.setCounselingSms(vb.benfCousSms.getSelectedPos());
        beneficiaryEntity.setDataStatus(DataStatus.NEW);

        Set<Integer> data = vb.benfRegisteredProgramme.selectedIds();

        if (data.contains(0)) {
            beneficiaryEntity.setPmmvyInstallmentCt(vb.benfPmmvvyCount.getSelectedPos());
        }

        if (data.contains(1)) {
            beneficiaryEntity.setIgmpyInstallmentCt(vb.benfIgmpyCount.getSelectedPos());
        }

        if (data.contains(2)) {
            beneficiaryEntity.setJsyInstallmentCt(vb.benfJsyCount.getSelectedPos());
        }

        if (data.contains(3)) {
            beneficiaryEntity.setRajshriInstallmentCt(vb.benfRajshriCount.getSelectedPos());
        }


        boolean isPregnant = false;
        boolean hasChild = false;

        if (vb.benfRegStage.getSelectedPos() == 0) {
            isPregnant = true;
        } else if (vb.benfRegStage.getSelectedPos() == 1) {
            hasChild = true;
        } else if (vb.benfRegStage.getSelectedPos() == 2) {
            hasChild = true;
            isPregnant = true;
        }


        if (isPregnant) {
            //preg
            pregnantEntity = new PregnantEntity();
            pregnantEntity.setBeneficiaryId(beneficiaryId);
            pregnantEntity.setPregnancyId(beneficiaryId);
            Date lmpdate = vb.benfLmp.getDate();
            pregnantEntity.setLmpDate(lmpdate);
            pregnantEntity.setDataStatus(DataStatus.NEW);

            int days = HUtil.daysBetween(lmpdate, new Date());
            beneficiaryEntity.setStage("PW");
            beneficiaryEntity.setSubStage(HUtil.getPWSubStage(days));
        }

        if (hasChild) {

            Date date = vb.benfChildDob.getDate();
            childEntity = new ChildEntity();
            childEntity.setChildId(Long.parseLong(beneficiaryId+"1"));
            childEntity.setStage("PW");
            int days = HUtil.daysBetween(date, new Date());
            String lmmySubStage = HUtil.getLMMYSubStage(days);
            childEntity.setSubStage(lmmySubStage);

            if (lmmySubStage.contains("LM")) {
                childEntity.setStage("LM");
            } else {
                childEntity.setStage("MY");
            }

            if (!isPregnant) {
                beneficiaryEntity.setSubStage(childEntity.getSubStage());
                beneficiaryEntity.setStage(childEntity.getStage());
            }

            childEntity.setChildOrder(1);
            childEntity.setDOB(date);
            childEntity.setMotherId(beneficiaryId);
            childEntity.setDeliveryHome(vb.benfChildDeliveryPlaceType.getSelectedPos());
            childEntity.setDeliveryPlace(vb.benfChildDeliveryPlace.getText());
            childEntity.setDataStatus(DataStatus.NEW);


        }

        Disposable sDisposable = dataRepository.insertBeneficiaryData(beneficiaryEntity, childEntity, pregnantEntity)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(aBoolean -> {
                    if (!aBoolean.isEmpty()) {//todo check
                        showAlertDialog("Beneficiary Created Saved Successfully", () -> {
                            requireActivity().onBackPressed();
                        });
                    }

                });

    }


    private void validate() {
        RegistrationFragmentBinding vb = getViewDataBinding();

        List<Pair<Boolean, View>> validateElement = new ArrayList<>();

        boolean isPregnant = false;
        boolean hasChild = false;
        if (vb.benfRegStage.getSelectedPos() == 0) {
            isPregnant = true;
        } else if (vb.benfRegStage.getSelectedPos() == 1) {
            hasChild = true;
        } else if (vb.benfRegStage.getSelectedPos() == 2) {
            hasChild = true;
            isPregnant = true;
        }

        validateElement.add(vb.benfChildCount.validateWthView());
        validateElement.add(vb.benfRegStage.validateWthView());

        if (hasChild) {
            validateElement.add(vb.benfChildDob.validateWthView());
        }

        validateElement.add(vb.benfName.validateWthView());
        validateElement.add(vb.benfHusName.validateWthView());


        validateElement.add(vb.benfRegisteredProgramme.validateWthView());
        if (vb.benfPmmvvyCount.isVisible())
            validateElement.add(vb.benfPmmvvyCount.validateWthView());
        if (vb.benfIgmpyCount.isVisible())
            validateElement.add(vb.benfIgmpyCount.validateWthView());
        if (vb.benfJsyCount.isVisible())
            validateElement.add(vb.benfJsyCount.validateWthView());
        if (vb.benfRajshriCount.isVisible())
            validateElement.add(vb.benfRajshriCount.validateWthView());


        validateElement.add(vb.benfAgeType.validateWthView());
        if (vb.benfAgeDob.isVisible())
            validateElement.add(vb.benfAgeDob.validateWthView());

        if (vb.benfAge.isVisible())
            validateElement.add(vb.benfAge.validateWthView());

        validateElement.add(vb.benfMobileSelector.validateWthView());

        if (vb.benfSelfMobile.isVisible()) {
            validateElement.add(vb.benfSelfMobile.validateWthView());
        }

        if (vb.benfHusMobile.isVisible()) {
            validateElement.add(vb.benfHusMobile.validateWthView());
        }


        validateElement.add(vb.benfCaste.validateWthView());
        validateElement.add(vb.benfEcon.validateWthView());
        validateElement.add(vb.benfPctsid.validateWthView());
        validateElement.add(vb.benfBahamashaId.validateWthView());
        validateElement.add(vb.benfCounseling.validateWthView());


        validateElement.add(vb.benfCousSms.validateWthView());

        if (isPregnant)
            validateElement.add(vb.benfLmp.validateWthView());

        if (hasChild) {
            validateElement.add(vb.benfChildDeliveryPlaceType.validateWthView());
            validateElement.add(vb.benfChildDeliveryPlace.validateWthView());
        }

        for (Pair<Boolean, View> viewPair : validateElement) {

            if (!viewPair.first) {
                View targetView = viewPair.second;
                targetView.getParent().requestChildFocus(targetView, targetView);
                return;
            }
        }

        save();


    }

}

