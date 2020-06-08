package in.rajpusht.pc.ui.registration;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
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
import in.rajpusht.pc.model.Tuple;
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
    private long beneficiaryId;
    private Tuple<BeneficiaryEntity, PregnantEntity, ChildEntity> beneficiaryEntityPregnantEntityChildEntityTuple;


    public static RegistrationFragment newInstance(long beneficiaryId) {
        RegistrationFragment registrationFragment = new RegistrationFragment();
        Bundle args = new Bundle();
        args.putLong("id", beneficiaryId);
        registrationFragment.setArguments(args);
        return registrationFragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            beneficiaryId = getArguments().getLong("id");
        }
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
        toolbar.setTitle(getResources().getString(R.string.registration_title));
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
                    return new ValidationStatus(false, getResources().getString(R.string.beneficiary_age_limit));
                } else {
                    return new ValidationStatus(true);
                }


            }
        });

        viewDataBinding.benfHusMobile.sethValidatorListener(FormValidatorUtils.textEqualValidator(10, getResources().getString(R.string.error_invalid_mobile_no)));
        viewDataBinding.benfSelfMobile.sethValidatorListener(FormValidatorUtils.textEqualValidator(10, getResources().getString(R.string.error_invalid_mobile_no)));
        viewDataBinding.benfName.sethValidatorListener(FormValidatorUtils.textLengthBwValidator(5, 100, getResources().getString(R.string.error_invalid_name)));
        viewDataBinding.benfHusName.sethValidatorListener(FormValidatorUtils.textLengthBwValidator(5, 100, getResources().getString(R.string.error_invalid_name)));
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.YEAR, -1);
        viewDataBinding.benfChildDob.setMinDate(instance.getTime().getTime());

        instance = Calendar.getInstance();
        instance.add(Calendar.YEAR, -13);
        viewDataBinding.benfAgeDob.setMaxDate(instance.getTime().getTime());


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
                    return new ValidationStatus(false, getResources().getString(R.string.child_age_limit));
                } else {
                    return new ValidationStatus(true);
                }
            }
        });
        viewDataBinding.benfPmmvvyCount.setVisibility(View.GONE);
        viewDataBinding.benfIgmpyCount.setVisibility(View.GONE);
        viewDataBinding.benfJsyCount.setVisibility(View.GONE);
        viewDataBinding.benfRajshriCount.setVisibility(View.GONE);
        viewDataBinding.benfInstalLy.setVisibility(View.GONE);

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
                viewDataBinding.benfAgeDob.setDate(null);
                viewDataBinding.benfAge.setText(null);
                if (value == 0) {
                    viewDataBinding.benfAgeDob.setEnableChild(true);
                    viewDataBinding.benfAge.setEnableChild(false);
                    viewDataBinding.benfAgeDob.setDate(null);
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

        viewDataBinding.benfCousSms.sethValidatorListener(new HValidatorListener<Integer>() {
            @Override
            public ValidationStatus isValid(Integer data) {

                double selectedPos = data;
                String message = null;
                if (selectedPos == 0 && viewDataBinding.benfSelfMobile.getText().isEmpty()) {
                    message = "Please Add Self Mobile Number ";
                } else if (selectedPos == 1 && viewDataBinding.benfHusMobile.getText().isEmpty()) {
                    message = "Please Add Husband’s Mobile Number ";
                } else if (selectedPos == 2 && (viewDataBinding.benfSelfMobile.getText().isEmpty() || viewDataBinding.benfHusMobile.getText().isEmpty())) {
                    message = "Please Add Self and Husband’s Mobile Number ";
                }
                if (message != null)
                    return new ValidationStatus(false, message);
                else
                    return new ValidationStatus(true);
            }
        });

        viewDataBinding.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                validate();
            }
        });
        Log.i("s", "onViewCreated: " + beneficiaryId);
        if (beneficiaryId != 0)
            dataRepository.getBeneficiaryData(beneficiaryId)
                    .subscribeOn(schedulerProvider.io())
                    .observeOn(schedulerProvider.ui())
                    .subscribe(data -> {
                        if (data != null) {
                            RegistrationFragment.this.beneficiaryEntityPregnantEntityChildEntityTuple = data;
                            beneficiaryEntityUiUpdate(data);
                        }
                    });

    }

    void save() {


        long beneficiaryId = System.currentTimeMillis();
        ChildEntity childEntity = null;
        PregnantEntity pregnantEntity = null;
        BeneficiaryEntity beneficiaryEntity = null;
        if (beneficiaryEntityPregnantEntityChildEntityTuple != null) {
            beneficiaryEntity = beneficiaryEntityPregnantEntityChildEntityTuple.getT1();
            pregnantEntity = beneficiaryEntityPregnantEntityChildEntityTuple.getT2();
            childEntity = beneficiaryEntityPregnantEntityChildEntityTuple.getT3();
            beneficiaryId = beneficiaryEntity.getBeneficiaryId();
        }

        RegistrationFragmentBinding vb = getViewDataBinding();
        if (beneficiaryEntity == null)
            beneficiaryEntity = new BeneficiaryEntity();
        beneficiaryEntity.setBeneficiaryId(beneficiaryId);
        beneficiaryEntity.setName(vb.benfName.getText());
        beneficiaryEntity.setHusbandName(vb.benfHusName.getText());
        beneficiaryEntity.setDob(vb.benfAgeDob.getDate());
        if (!TextUtils.isEmpty(vb.benfAge.getText()))
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
        beneficiaryEntity.setAwcCode(dataRepository.getSelectedAwcCode());


        Set<Integer> data = vb.benfRegisteredProgramme.selectedIds();

        if (data.contains(0)) {
            beneficiaryEntity.setPmmvyInstallment(vb.benfPmmvvyCount.getSelectedPos());
        }

        if (data.contains(1)) {
            beneficiaryEntity.setIgmpyInstallment(vb.benfIgmpyCount.getSelectedPos());
        }

        if (data.contains(2)) {
            beneficiaryEntity.setJsyInstallment(vb.benfJsyCount.getSelectedPos());
        }

        if (data.contains(3)) {
            beneficiaryEntity.setRajshriInstallment(vb.benfRajshriCount.getSelectedPos());
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
            if (pregnantEntity == null)
                pregnantEntity = new PregnantEntity();
            pregnantEntity.setBeneficiaryId(beneficiaryId);
            pregnantEntity.setPregnancyId(beneficiaryId);
            Date lmpdate = vb.benfLmp.getDate();
            pregnantEntity.setLmpDate(lmpdate);

            int days = HUtil.daysBetween(lmpdate, new Date());
            beneficiaryEntity.setStage("PW");
            // beneficiaryEntity.setSubStage(HUtil.getPWSubStage(days));
            beneficiaryEntity.setSubStage("PW");//todo check
        }

        if (hasChild) {

            Date date = vb.benfChildDob.getDate();
            if (childEntity == null)
                childEntity = new ChildEntity();
            childEntity.setChildId(Long.parseLong(beneficiaryId + "1"));
            childEntity.setStage("PW");
            int days = HUtil.daysBetween(date, new Date());
            String lmmySubStage = HUtil.getLMMYSubStage(days);
            //childEntity.setSubStage(lmmySubStage);//todo


            if (lmmySubStage.contains("LM")) {
                childEntity.setStage("LM");
                childEntity.setSubStage("LM");
            } else {
                childEntity.setStage("MY");
                childEntity.setSubStage("MY");
            }

            if (!isPregnant) {
                beneficiaryEntity.setSubStage(childEntity.getStage());//todo
                beneficiaryEntity.setStage(childEntity.getStage());
            }

            childEntity.setChildOrder(1);
            childEntity.setDob(date);
            childEntity.setMotherId(beneficiaryId);
            childEntity.setDeliveryHome(vb.benfChildDeliveryPlaceType.getSelectedPos());
            childEntity.setDeliveryPlace(vb.benfChildDeliveryPlace.getText());


        }

        Disposable sDisposable = dataRepository.insertOrUpdateBeneficiaryData(beneficiaryEntity, childEntity, pregnantEntity)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(aBoolean -> {
                    if (!aBoolean.isEmpty()) {//todo check
                        showAlertDialog(getResources().getString(R.string.benf_success_reg), () -> {
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
        if (vb.benfAgeDob.isVisibleAndEnable())
            validateElement.add(vb.benfAgeDob.validateWthView());

        if (vb.benfAge.isVisibleAndEnable())
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

        /*if (vb.benfCousSms.getSelectedPos()==0 && vb.benfSelfMobile.getText().isEmpty()){
            showMessage("Please Add Self Mobile Number ");
        }
        else if (vb.benfCousSms.getSelectedPos()==1 && vb.benfHusMobile.getText().isEmpty()){
            showMessage("Please Add Husband’s Mobile Number ");
        }
        else if (vb.benfCousSms.getSelectedPos()==2 && (vb.benfSelfMobile.getText().isEmpty() || vb.benfHusMobile.getText().isEmpty()) ){
            showMessage("Please Add Self and Husband’s Mobile Number ");
        }*/

        save();


    }


    private void beneficiaryEntityUiUpdate(Tuple<BeneficiaryEntity, PregnantEntity, ChildEntity> tuple) {

        Log.i("ss", "beneficiaryEntityUiUpdate: " + new Gson().toJson(tuple));

        BeneficiaryEntity beneficiaryEntity = tuple.getT1();
        ChildEntity childEntity = tuple.getT3();
        PregnantEntity pregnantEntity = tuple.getT2();

        RegistrationFragmentBinding vh = getViewDataBinding();

//        vh.save.setEnabled(false);//todo
        vh.benfChildCount.setSection(beneficiaryEntity.getChildCount());
        if (pregnantEntity != null && childEntity != null)
            vh.benfRegStage.setSection(2);
        else if (childEntity != null)
            vh.benfRegStage.setSection(1);
        else if (pregnantEntity != null)
            vh.benfRegStage.setSection(0);
        vh.benfRegStage.sendChangedListenerValue();//ui hide
        vh.benfRegStage.setEnableChild(false);//will create conflict

        if (childEntity != null) {
            vh.benfChildDob.setDate(childEntity.getDob());
            vh.benfChildDeliveryPlace.setText(childEntity.getDeliveryPlace());
            if (childEntity.getDeliveryHome() != null)
                vh.benfChildDeliveryPlaceType.setSection(childEntity.getDeliveryHome());
        }

        if (pregnantEntity != null) {
            vh.benfLmp.setDate(pregnantEntity.getLmpDate());
        }

        Set<Integer> regScheme = new HashSet<>();

        if (beneficiaryEntity.getPmmvyInstallment() != null) {
            vh.benfPmmvvyCount.setSection(beneficiaryEntity.getPmmvyInstallment());
            regScheme.add(0);
        }
        if (beneficiaryEntity.getIgmpyInstallment() != null) {
            vh.benfIgmpyCount.setSection(beneficiaryEntity.getIgmpyInstallment());
            regScheme.add(1);
        }
        if (beneficiaryEntity.getJsyInstallment() != null) {
            vh.benfJsyCount.setSection(beneficiaryEntity.getJsyInstallment());
            regScheme.add(2);
        }
        if (beneficiaryEntity.getRajshriInstallment() != null) {
            vh.benfRajshriCount.setSection(beneficiaryEntity.getRajshriInstallment());
            regScheme.add(3);
        }

        if (regScheme.isEmpty()) {
            regScheme.add(4);
        }

        vh.benfRegisteredProgramme.setSelectedIds(regScheme);

        vh.benfName.setText(beneficiaryEntity.getName());
        vh.benfHusName.setText(beneficiaryEntity.getHusbandName());

        if (beneficiaryEntity.getDob() != null) {
            vh.benfAgeType.setSection(0);
            vh.benfAgeDob.setDate(beneficiaryEntity.getDob());
            vh.benfAge.setEnableChild(false);
        } else {
            vh.benfAgeType.setSection(1);
            vh.benfAgeDob.setEnableChild(false);
            vh.benfAge.setText(String.valueOf(beneficiaryEntity.getAge()));
        }


        vh.benfSelfMobile.setText(beneficiaryEntity.getMobileNo());
        vh.benfHusMobile.setText(beneficiaryEntity.getHusbandMobNo());

        if (!TextUtils.isEmpty(beneficiaryEntity.getMobileNo()) && !TextUtils.isEmpty(beneficiaryEntity.getHusbandMobNo())) {
            vh.benfMobileSelector.setSelectedIds(new HashSet<>(Arrays.asList(0, 1)));
        } else if (!TextUtils.isEmpty(beneficiaryEntity.getMobileNo())) {
            vh.benfMobileSelector.setSelectedIds(new HashSet<>(Collections.singletonList(0)));
        } else if (!TextUtils.isEmpty(beneficiaryEntity.getHusbandMobNo())) {
            vh.benfMobileSelector.setSelectedIds(new HashSet<>(Collections.singletonList(1)));
        }

        vh.benfCaste.setSectionByData(beneficiaryEntity.getCaste());
        vh.benfEcon.setSectionByData(beneficiaryEntity.getEconomic());
        vh.benfBahamashaId.setText(beneficiaryEntity.getBahamashahId());
        vh.benfPctsid.setText(beneficiaryEntity.getPctsId());
        vh.benfCounseling.setSectionByData(beneficiaryEntity.getCounselingProv());
        vh.benfCousSms.setSection(beneficiaryEntity.getCounselingSms());


    }


}

