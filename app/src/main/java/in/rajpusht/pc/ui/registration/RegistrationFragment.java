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
import in.rajpusht.pc.model.BeneficiaryWithChild;
import in.rajpusht.pc.model.DataStatus;
import in.rajpusht.pc.ui.base.BaseFragment;
import in.rajpusht.pc.utils.FormDataConstant;
import in.rajpusht.pc.utils.rx.SchedulerProvider;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

import static in.rajpusht.pc.utils.FormDataConstant.instalmentValConvt;

public class RegistrationFragment extends BaseFragment<RegistrationFragmentBinding, RegistrationViewModel> {

    @Inject
    ViewModelProviderFactory factory;
    @Inject
    DataRepository dataRepository;
    @Inject
    SchedulerProvider schedulerProvider;
    boolean firstChildinvalidDob = false;
    private RegistrationViewModel mViewModel;
    private long beneficiaryId;
    private BeneficiaryWithChild beneficiaryJoin;

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

        viewDataBinding.benfRegisteredProgramme.changeEleVisible(new Pair<>(1, false));//todo always IGMPY

        viewDataBinding.benfChildCount.sethValidatorListener(FormValidatorUtils.valueBwValidator(0, 2, "NOT ELIGIBLE"));
        viewDataBinding.benfChildCount.sethValueChangedListener(new HValueChangedListener<Integer>() {
            @Override
            public void onValueChanged(Integer data) {


                if (data == 2) {
                    viewDataBinding.benfChildDeliveryPlaceType.setVisibility(View.GONE);
                    viewDataBinding.benfChildDeliveryPlace.setVisibility(View.GONE);
                    viewDataBinding.benfChildTwin.setVisibility(View.VISIBLE);
                } else {
                    viewDataBinding.benfChildDeliveryPlaceType.setVisibility(View.VISIBLE);
                    viewDataBinding.benfChildDeliveryPlace.setVisibility(View.VISIBLE);
                    viewDataBinding.benfChildTwin.setVisibility(View.GONE);
                }


                if (data == 0) {

                    viewDataBinding.benfRegStage.setEnableChild(false);
                    viewDataBinding.benfRegStage.setSection(0);
                } else if (data == 2) {
                    viewDataBinding.benfRegStage.setEnableChild(false);
                    viewDataBinding.benfRegStage.setSection(1);
                } else {
                    viewDataBinding.benfRegStage.setEnableChild(true);
                }

                viewDataBinding.benfRegStage.sendChangedListenerValue();//ui hide
                viewDataBinding.benfChildTwin.setSection(1);
                viewDataBinding.benfChildTwin.sendChangedListenerValue();


            }
        });

        viewDataBinding.benfChildDob.sethValueChangedListener(new HValueChangedListener<Date>() {
            @Override
            public void onValueChanged(Date data) {

                int age = HUtil.calcAge(data);
                firstChildinvalidDob = false;
                if (viewDataBinding.benfChildCount.getSelectedPos() >= 2 && age >= 1) {
                    firstChildinvalidDob = true;

                    showAlertDialog(getString(R.string.first_child_no_elig), new Runnable() {
                        @Override
                        public void run() {
                            viewDataBinding.benfChildDob.setError(getString(R.string.first_child_no_elig));

                        }
                    });
                } else {
                    if (age >= 1) {
                        showAlertDialog(getString(R.string.beneficiary_not_eligible), new Runnable() {
                            @Override
                            public void run() {
                                requireActivity().onBackPressed();
                            }
                        });
                    }
                }
            }
        });

        viewDataBinding.benfChild2Dob.sethValueChangedListener(new HValueChangedListener<Date>() {
            @Override
            public void onValueChanged(Date data) {

                int age = HUtil.calcAge(data);
                if (age >= 1)
                    showAlertDialog(getString(R.string.beneficiary_not_eligible), new Runnable() {
                        @Override
                        public void run() {
                            requireActivity().onBackPressed();
                        }
                    });

                if (viewDataBinding.benfChildTwin.getSelectedPos() == 0) {
                    viewDataBinding.benfChildDob.setDate(data);
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
        viewDataBinding.benfChild2Dob.setMinDate(instance.getTime().getTime());

        instance = Calendar.getInstance();
        instance.add(Calendar.YEAR, -13);
        viewDataBinding.benfAgeDob.setMaxDate(instance.getTime().getTime());

        instance = Calendar.getInstance();
        instance.add(Calendar.YEAR, -278);
        viewDataBinding.benfLmp.setMinDate(instance.getTime().getTime());



        viewDataBinding.benfRegStage.sethValueChangedListener(new HValueChangedListener<Integer>() {
            @Override
            public void onValueChanged(Integer data) {
                if (data == 0) {
                    viewDataBinding.firstChildLy.setVisibility(View.GONE);
                    viewDataBinding.secondChildLy.setVisibility(View.GONE);
                    viewDataBinding.benfLmp.setVisibility(View.VISIBLE);

                } else if (data == 1) {
                    viewDataBinding.firstChildLy.setVisibility(View.VISIBLE);
                    viewDataBinding.benfLmp.setVisibility(View.GONE);
                    if (viewDataBinding.benfChildCount.getSelectedPos() >= 2) {
                        viewDataBinding.secondChildLy.setVisibility(View.VISIBLE);
                    } else
                        viewDataBinding.secondChildLy.setVisibility(View.GONE);

                } else {
                    viewDataBinding.firstChildLy.setVisibility(View.VISIBLE);
                    viewDataBinding.benfLmp.setVisibility(View.VISIBLE);
                    if (viewDataBinding.benfChildCount.getSelectedPos() >= 2) {
                        viewDataBinding.secondChildLy.setVisibility(View.VISIBLE);
                    }
                }


                //Registration Form Validtions:
                //0 Pregnant PMMVY(1,2), JSY, RAJSHRI
                //1 pregnant PMMVY, JSY, RAJSHRI
                //1  lactating PMMVY, JSY
                //1 Both PMMVY, JSY, RAJSHRI
                //2 Lactating PMMVY,JSY,Rajshri


                int childCount = viewDataBinding.benfChildCount.getSelectedPos();

                //setting
                viewDataBinding.benfPmmvvyCount.setSectionList(getResources().getStringArray(R.array.count_0_3_dot));
                viewDataBinding.benfRegisteredProgramme.changeEleVisible(new Pair<>(3, true));

                if (childCount == 0) {
                    viewDataBinding.benfPmmvvyCount.setSectionList(getResources().getStringArray(R.array.count_0_2_dot));
                } else if (childCount == 1 && data == 0) {

                } else if (childCount == 1 && data == 1) {
                    viewDataBinding.benfRegisteredProgramme.changeEleVisible(new Pair<>(3, false));
                } else if (childCount == 1 && data == 2) {

                } else if (childCount == 2) {

                }


            }
        });
        viewDataBinding.benfChildTwin.setSection(1);
        viewDataBinding.benfChildTwin.sethValueChangedListener(new HValueChangedListener<Integer>() {
            @Override
            public void onValueChanged(Integer data) {

                if (data == 0) {
                    HUtil.recursiveSetEnabled(viewDataBinding.firstChildLy, false);
                } else {
                    HUtil.recursiveSetEnabled(viewDataBinding.firstChildLy, true);
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

        viewDataBinding.firstChildLy.setVisibility(View.GONE);
        viewDataBinding.secondChildLy.setVisibility(View.GONE);

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
                    showAlertDialog(getString(R.string.beneficiary_not_eligible), new Runnable() {
                        @Override
                        public void run() {
                            requireActivity().onBackPressed();
                        }
                    });

                } else {
                    viewDataBinding.benfInstalLy.setVisibility(View.VISIBLE);

                }
                if (data.isEmpty()) {
                    viewDataBinding.benfInstalLy.setVisibility(View.GONE);
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
                    message = getString(R.string.add_self_mobile_val_mes);
                } else if (selectedPos == 1 && viewDataBinding.benfHusMobile.getText().isEmpty()) {
                    message = getString(R.string.add_husb_mobile_val_mes);
                } else if (selectedPos == 2 && (viewDataBinding.benfSelfMobile.getText().isEmpty() && viewDataBinding.benfHusMobile.getText().isEmpty())) {
                    message = getString(R.string.add_self_husb_mobile_val_mes);
                } else if (selectedPos == 2 && viewDataBinding.benfSelfMobile.getText().isEmpty()) {
                    message = getString(R.string.add_self_mobile_val_mes);
                } else if (selectedPos == 2 && viewDataBinding.benfHusMobile.getText().isEmpty()) {
                    message = getString(R.string.add_husb_mobile_val_mes);
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


        if (beneficiaryId != 0)
            dataRepository.getBeneficiaryChildDataById(beneficiaryId)
                    .subscribeOn(schedulerProvider.io())
                    .observeOn(schedulerProvider.ui())
                    .subscribe(data -> {
                        if (data != null) {
                            RegistrationFragment.this.beneficiaryJoin = data;
                            beneficiaryEntityUiUpdate(data);
                        }
                    });

    }

    private void save() {


        long beneficiaryId = System.currentTimeMillis();
        ChildEntity childEntity = null;
        ChildEntity secondChildEntity = null;
        PregnantEntity pregnantEntity = null;
        BeneficiaryEntity beneficiaryEntity = null;
        if (beneficiaryJoin != null) {
            beneficiaryEntity = beneficiaryJoin.getBeneficiaryEntity();
            pregnantEntity = beneficiaryJoin.getPregnantEntity();
            childEntity = beneficiaryJoin.getChildEntities().size() > 0 ? beneficiaryJoin.getChildEntities().get(0) : null;
            secondChildEntity = beneficiaryJoin.getChildEntities().size() > 1 ? beneficiaryJoin.getChildEntities().get(1) : null;
            beneficiaryId = beneficiaryEntity.getBeneficiaryId();
        }

        RegistrationFragmentBinding vb = getViewDataBinding();
        if (beneficiaryEntity == null) {
            beneficiaryEntity = new BeneficiaryEntity();
            beneficiaryEntity.setIsActive("Y");
        }
        beneficiaryEntity.setBeneficiaryId(beneficiaryId);
        beneficiaryEntity.setName(vb.benfName.getText());
        beneficiaryEntity.setHusbandName(vb.benfHusName.getText());
        beneficiaryEntity.setDob(vb.benfAgeDob.getDate());
        if (!TextUtils.isEmpty(vb.benfAge.getText()))
            beneficiaryEntity.setAge(Integer.valueOf(vb.benfAge.getText()));
        beneficiaryEntity.setChildCount(vb.benfChildCount.getSelectedPos());
        beneficiaryEntity.setMobileNo(vb.benfSelfMobile.getText());
        beneficiaryEntity.setHusbandMobNo(vb.benfHusMobile.getText());
        beneficiaryEntity.setCaste(FormDataConstant.caste.get(vb.benfCaste.getSelectedPos()));
        beneficiaryEntity.setEconomic(FormDataConstant.economic.get(vb.benfEcon.getSelectedPos()));
        beneficiaryEntity.setPctsId(vb.benfPctsid.getText());
        beneficiaryEntity.setBahamashahId(vb.benfBahamashaId.getText());
        beneficiaryEntity.setCounselingSms(vb.benfCousSms.getSelectedPos());
        beneficiaryEntity.setAwcCode(dataRepository.getSelectedAwcCode());


        Set<Integer> data = vb.benfRegisteredProgramme.selectedIds();

        if (data.contains(0)) {
            beneficiaryEntity.setPmmvyInstallment(instalmentValConvt(vb.benfPmmvvyCount.getSelectedData()));
        }

        if (data.contains(1)) {
            beneficiaryEntity.setIgmpyInstallment(instalmentValConvt(vb.benfIgmpyCount.getSelectedData()));
        }

        if (data.contains(2)) {
            beneficiaryEntity.setJsyInstallment(instalmentValConvt(vb.benfJsyCount.getSelectedData()));
        }

        if (data.contains(3)) {
            beneficiaryEntity.setRajshriInstallment(instalmentValConvt(vb.benfRajshriCount.getSelectedData()));
        }


        boolean isPregnant = false;
        boolean hasChild = false;
        boolean hasSecondChild = false;


        if (vb.benfRegStage.getSelectedPos() == 0) {
            isPregnant = true;
        } else if (vb.benfRegStage.getSelectedPos() == 1) {
            hasChild = true;
        } else if (vb.benfRegStage.getSelectedPos() == 2) {
            hasChild = true;
            isPregnant = true;
        }

        if (vb.benfChildCount.getSelectedPos() >= 2) {
            hasSecondChild = true;
        }

        if (firstChildinvalidDob) {
            hasChild = false;
        }


        if (isPregnant) {
            if (pregnantEntity == null) {
                pregnantEntity = new PregnantEntity();
                pregnantEntity.setIsActive("Y");
                pregnantEntity.setBeneficiaryId(beneficiaryId);
                pregnantEntity.setPregnancyId(beneficiaryId);
            }
            Date lmpdate = vb.benfLmp.getDate();
            pregnantEntity.setLmpDate(lmpdate);

            int days = HUtil.daysBetween(lmpdate, new Date());
            beneficiaryEntity.setStage("PW");
            // beneficiaryEntity.setSubStage(HUtil.getPWSubStage(days));
            beneficiaryEntity.setSubStage("PW");//todo check
        }

        if (hasChild) {

            Date date = vb.benfChildDob.getDate();
            if (childEntity == null) {
                childEntity = new ChildEntity();
                childEntity.setIsActive("Y");
                childEntity.setChildId(Long.parseLong(beneficiaryId + "1"));
                childEntity.setChildOrder(1);
            }

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


            childEntity.setDob(date);
            childEntity.setMotherId(beneficiaryId);
            childEntity.setDeliveryHome(vb.benfChildDeliveryPlaceType.getSelectedPos());
            childEntity.setDeliveryPlace(vb.benfChildDeliveryPlace.getText());


        }

        if (hasSecondChild) {

            Date date = vb.benfChild2Dob.getDate();
            if (secondChildEntity == null) {
                secondChildEntity = new ChildEntity();
                secondChildEntity.setIsActive("Y");
                secondChildEntity.setChildId(Long.parseLong(beneficiaryId + "2"));
                secondChildEntity.setChildOrder(2);
            }

            int days = HUtil.daysBetween(date, new Date());
            String lmmySubStage = HUtil.getLMMYSubStage(days);
            //childEntity.setSubStage(lmmySubStage);//todo


            if (lmmySubStage.contains("LM")) {
                secondChildEntity.setStage("LM");
                secondChildEntity.setSubStage("LM");
            } else {
                secondChildEntity.setStage("MY");
                secondChildEntity.setSubStage("MY");
            }

            if (!isPregnant) {
                beneficiaryEntity.setSubStage(secondChildEntity.getStage());//todo
                beneficiaryEntity.setStage(secondChildEntity.getStage());
            }

            secondChildEntity.setDob(date);
            secondChildEntity.setMotherId(beneficiaryId);
            secondChildEntity.setDeliveryHome(vb.benfChild2DeliveryPlaceType.getSelectedPos());
            secondChildEntity.setDeliveryPlace(vb.benfChild2DeliveryPlace.getText());


        }

        List<Observable<Boolean>> insertOrUpdate = new ArrayList<>();
        insertOrUpdate.add(dataRepository.insertOrUpdateBeneficiary(beneficiaryEntity));
        if (pregnantEntity != null)
            insertOrUpdate.add(dataRepository.insertOrUpdatePregnant(pregnantEntity));
        if (childEntity != null)
            insertOrUpdate.add(dataRepository.insertOrUpdateChild(childEntity));
        if (secondChildEntity != null)
            insertOrUpdate.add(dataRepository.insertOrUpdateChild(secondChildEntity));
        Disposable sDisposable = Observable.merge(insertOrUpdate)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .toList()
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
        boolean hasSecondChild = false;

        if (vb.benfRegStage.getSelectedPos() == 0) {
            isPregnant = true;
        } else if (vb.benfRegStage.getSelectedPos() == 1) {
            hasChild = true;
        } else if (vb.benfRegStage.getSelectedPos() == 2) {
            hasChild = true;
            isPregnant = true;
        }
        if (vb.benfChildCount.getSelectedPos() >= 2) {
            hasSecondChild = true;
        }

        if (firstChildinvalidDob) {
            hasChild = false;
        }

        validateElement.add(vb.benfChildCount.validateWthView());
        validateElement.add(vb.benfRegStage.validateWthView());

        if (hasChild) {
            validateElement.add(vb.benfChildDob.validateWthView());
            if (vb.benfChildDeliveryPlaceType.isVisible())
                validateElement.add(vb.benfChildDeliveryPlaceType.validateWthView());
            if (vb.benfChildDeliveryPlace.isVisible())
                validateElement.add(vb.benfChildDeliveryPlace.validateWthView());
        }

        if (hasSecondChild) {
            validateElement.add(vb.benfChild2Dob.validateWthView());
            validateElement.add(vb.benfChild2DeliveryPlaceType.validateWthView());
            validateElement.add(vb.benfChild2DeliveryPlace.validateWthView());
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


        validateElement.add(vb.benfCousSms.validateWthView());

        if (isPregnant)
            validateElement.add(vb.benfLmp.validateWthView());


        for (Pair<Boolean, View> viewPair : validateElement) {

            if (!viewPair.first) {
                View targetView = viewPair.second;
                targetView.getParent().requestChildFocus(targetView, targetView);
                return;
            }
        }


        if (vb.benfChildTwin.getSelectedPos() != 0 && !firstChildinvalidDob) {
            if (vb.benfChildCount.getSelectedPos() == 2 && vb.benfChildDob.getDate().getTime() > vb.benfChild2Dob.getDate().getTime()) {
                vb.benfChild2Dob.setError(getString(R.string.Second_child_age_less));
                View targetView = vb.benfChild2Dob;
                targetView.getParent().requestChildFocus(targetView, targetView);

                return;
            }
        }


        save();


    }


    private void beneficiaryEntityUiUpdate(BeneficiaryWithChild tuple) {

        Log.i("ss", "beneficiaryEntityUiUpdate: " + new Gson().toJson(tuple));

        BeneficiaryEntity beneficiaryEntity = tuple.getBeneficiaryEntity();
        List<ChildEntity> childEntities = tuple.getChildEntities();
        PregnantEntity pregnantEntity = tuple.getPregnantEntity();

        RegistrationFragmentBinding vh = getViewDataBinding();

        vh.benfChildCount.setSection(beneficiaryEntity.getChildCount());
        if (pregnantEntity != null && !childEntities.isEmpty())
            vh.benfRegStage.setSection(2);
        else if (!childEntities.isEmpty())
            vh.benfRegStage.setSection(1);
        else if (pregnantEntity != null)
            vh.benfRegStage.setSection(0);
        vh.benfRegStage.sendChangedListenerValue();//ui hide
        vh.benfRegStage.setEnableChild(false);//will create conflict
        vh.benfChildCount.sendChangedListenerValue();
        vh.benfChildCount.setEnabled(false);
        vh.benfChildTwin.setEnabled(false);

        if (!childEntities.isEmpty()) {
            ChildEntity childEntity = childEntities.get(0);
            vh.benfChildDob.setDate(childEntity.getDob());
            vh.benfChildDeliveryPlace.setText(childEntity.getDeliveryPlace());
            if (childEntity.getDeliveryHome() != null)
                vh.benfChildDeliveryPlaceType.setSection(childEntity.getDeliveryHome());

            if (childEntities.size() >= 2) {
                ChildEntity secondChild = childEntities.get(1);
                vh.benfChild2Dob.setDate(secondChild.getDob());
                vh.benfChild2DeliveryPlace.setText(secondChild.getDeliveryPlace());
                if (secondChild.getDeliveryHome() != null)
                    vh.benfChild2DeliveryPlaceType.setSection(secondChild.getDeliveryHome());
            }
        }

        if (pregnantEntity != null) {
            vh.benfLmp.setDate(pregnantEntity.getLmpDate());
        }

        Set<Integer> regScheme = new HashSet<>();

        if (beneficiaryEntity.getPmmvyInstallment() != null) {
            vh.benfPmmvvyCount.setSectionByData(instalmentValConvt(beneficiaryEntity.getPmmvyInstallment()));
            regScheme.add(0);
        }
        if (beneficiaryEntity.getIgmpyInstallment() != null) {
            vh.benfIgmpyCount.setSectionByData(instalmentValConvt(beneficiaryEntity.getIgmpyInstallment()));
            regScheme.add(1);
        }
        if (beneficiaryEntity.getJsyInstallment() != null) {
            vh.benfJsyCount.setSectionByData(instalmentValConvt(beneficiaryEntity.getJsyInstallment()));
            regScheme.add(2);
        }
        if (beneficiaryEntity.getRajshriInstallment() != null) {
            vh.benfRajshriCount.setSectionByData(instalmentValConvt(beneficiaryEntity.getRajshriInstallment()));
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

        vh.benfCaste.setSection(FormDataConstant.caste.indexOf(beneficiaryEntity.getCaste()));
        vh.benfEcon.setSection(FormDataConstant.economic.indexOf(beneficiaryEntity.getEconomic()));
        vh.benfBahamashaId.setText(beneficiaryEntity.getBahamashahId());
        vh.benfPctsid.setText(beneficiaryEntity.getPctsId());
        vh.benfCousSms.setSection(beneficiaryEntity.getCounselingSms());


        if (beneficiaryEntity.getDataStatus() != DataStatus.NEW) {
            vh.save.setEnabled(false);
            HUtil.recursiveSetEnabled(vh.formContainer, false);
        }

    }


}

