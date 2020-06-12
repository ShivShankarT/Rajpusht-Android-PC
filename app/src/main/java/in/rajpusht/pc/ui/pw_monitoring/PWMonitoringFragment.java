package in.rajpusht.pc.ui.pw_monitoring;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Pair;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.library.baseAdapters.BR;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import in.rajpusht.pc.R;
import in.rajpusht.pc.ViewModelProviderFactory;
import in.rajpusht.pc.custom.callback.HValidatorListener;
import in.rajpusht.pc.custom.callback.HValueChangedListener;
import in.rajpusht.pc.custom.utils.HUtil;
import in.rajpusht.pc.custom.validator.FormValidatorUtils;
import in.rajpusht.pc.custom.validator.ValidationStatus;
import in.rajpusht.pc.data.DataRepository;
import in.rajpusht.pc.data.local.db.entity.BeneficiaryEntity;
import in.rajpusht.pc.data.local.db.entity.PWMonitorEntity;
import in.rajpusht.pc.data.local.db.entity.PregnantEntity;
import in.rajpusht.pc.databinding.PwMonitoringFragmentBinding;
import in.rajpusht.pc.model.BeneficiaryJoin;
import in.rajpusht.pc.model.DataStatus;
import in.rajpusht.pc.ui.animation.CounsellingAnimationFragment;
import in.rajpusht.pc.ui.base.BaseFragment;
import in.rajpusht.pc.ui.registration.RegistrationFragment;
import in.rajpusht.pc.utils.FormDataConstant;
import in.rajpusht.pc.utils.FragmentUtils;
import in.rajpusht.pc.utils.rx.SchedulerProvider;
import io.reactivex.Single;
import io.reactivex.disposables.Disposable;

import static in.rajpusht.pc.utils.FormDataConstant.ANC_NOT_COMPLETED;
import static in.rajpusht.pc.utils.FormDataConstant.instalmentValConvt;

public class PWMonitoringFragment extends BaseFragment<PwMonitoringFragmentBinding, PWMonitoringViewModel> {

    @Inject
    ViewModelProviderFactory factory;
    @Inject
    DataRepository dataRepository;
    @Inject
    SchedulerProvider schedulerProvider;
    private PWMonitoringViewModel mViewModel;
    private long pregnancyId;
    private String subStage;
    private long beneficiaryId;
    private Long pwFormId;
    private PWMonitorEntity mPwMonitorEntity;
    private BeneficiaryJoin beneficiaryJoin;

    public static PWMonitoringFragment newInstance(long beneficiaryId, long pregnancyId, String subStage, Long pwFormId) {
        PWMonitoringFragment pwMonitoringFragment = new PWMonitoringFragment();
        Bundle args = new Bundle();
        args.putLong("id", pregnancyId);
        args.putLong("beneficiaryId", beneficiaryId);
        args.putString("subStage", subStage);
        args.putSerializable("pwFormId", pwFormId);
        pwMonitoringFragment.setArguments(args);
        return pwMonitoringFragment;
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.pw_monitoring_fragment;
    }

    @Override
    public PWMonitoringViewModel getViewModel() {
        mViewModel = new ViewModelProvider(this, factory).get(PWMonitoringViewModel.class);
        return mViewModel;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pregnancyId = getArguments().getLong("id");
        beneficiaryId = getArguments().getLong("beneficiaryId");
        subStage = getArguments().getString("subStage");
        pwFormId = (Long) getArguments().getSerializable("pwFormId");
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        PwMonitoringFragmentBinding viewDataBinding = getViewDataBinding();
        Toolbar toolbar = viewDataBinding.toolbarLy.toolbar;
        toolbar.setTitle("PW Monitoring");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().onBackPressed();
            }
        });

        viewDataBinding.benfDtLy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentUtils.replaceFragment(requireActivity(),
                        RegistrationFragment.newInstance(beneficiaryId), R.id.fragment_container,
                        true, false, FragmentUtils.TRANSITION_SLIDE_LEFT_RIGHT);
            }
        });

        viewDataBinding.weightIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentUtils.replaceFragment(requireActivity(),
                        CounsellingAnimationFragment.newInstance(0), R.id.fragment_container,
                        true, false, FragmentUtils.TRANSITION_SLIDE_LEFT_RIGHT);
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

        viewDataBinding.naBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewDataBinding.naLy.getVisibility() == View.VISIBLE) {
                    viewDataBinding.naLy.setVisibility(View.GONE);
                    viewDataBinding.formLy.setVisibility(View.VISIBLE);
                } else {
                    viewDataBinding.naLy.setVisibility(View.VISIBLE);
                    viewDataBinding.formLy.setVisibility(View.GONE);
                }
            }
        });

        viewDataBinding.benfNaReason.sethValueChangedListener(new HValueChangedListener<Integer>() {
            @Override
            public void onValueChanged(Integer data) {
                if (data == 1) {
                    viewDataBinding.benfNaOtherReason.setVisibility(View.VISIBLE);
                } else {
                    viewDataBinding.benfNaOtherReason.setVisibility(View.GONE);
                }
            }
        });
        viewDataBinding.saveNaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save(true);
            }
        });

        viewDataBinding.saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save(false);

            }
        });


        if (subStage.contains("PW")) {
            viewDataBinding.benfJsyCount.setSectionList(getResources().getStringArray(R.array.count_0));
            viewDataBinding.benfRajshriCount.setSectionList(getResources().getStringArray(R.array.count_0));
        }

        if (subStage.contains("LM")) {
            viewDataBinding.benfPmmvvyCount.setSectionList(getResources().getStringArray(R.array.count_0_3_dot));
            viewDataBinding.benfIgmpyCount.setSectionList(getResources().getStringArray(R.array.count_0_4_dot));
            viewDataBinding.benfJsyCount.setSectionList(getResources().getStringArray(R.array.count_0_1_dot));
            viewDataBinding.benfRajshriCount.setSectionList(getResources().getStringArray(R.array.count_0_1_dot));
        }

        if (subStage.contains("MY")) {
            viewDataBinding.benfIgmpyCount.setSectionList(getResources().getStringArray(R.array.count_0_4_dot));
            viewDataBinding.benfJsyCount.setSectionList(getResources().getStringArray(R.array.count_0_1_dot));
            viewDataBinding.benfRajshriCount.setSectionList(getResources().getStringArray(R.array.count_0_2_dot));
        }


        if (subStage.equalsIgnoreCase("PW1")) {
            viewDataBinding.benfPmmvvyCount.setSectionList(getResources().getStringArray(R.array.count_0_1_dot));
            viewDataBinding.benfIgmpyCount.setSectionList(getResources().getStringArray(R.array.count_0_1_dot));

        } else if (subStage.equalsIgnoreCase("PW2")) {
            viewDataBinding.benfPmmvvyCount.setSectionList(getResources().getStringArray(R.array.count_0_2_dot));
            viewDataBinding.benfIgmpyCount.setSectionList(getResources().getStringArray(R.array.count_0_2_dot));

        } else if (subStage.equalsIgnoreCase("PW3")) {
            viewDataBinding.benfPmmvvyCount.setSectionList(getResources().getStringArray(R.array.count_0_2_dot));
            viewDataBinding.benfIgmpyCount.setSectionList(getResources().getStringArray(R.array.count_0_3_dot));
        } else if (subStage.equalsIgnoreCase("PW4")) {
            viewDataBinding.benfPmmvvyCount.setSectionList(getResources().getStringArray(R.array.count_0_2_dot));
            viewDataBinding.benfIgmpyCount.setSectionList(getResources().getStringArray(R.array.count_0_3_dot));
        }


        fetchFormUiData();
    }

    private void setupUiData() {
        BeneficiaryEntity beneficiaryEntity = beneficiaryJoin.getBeneficiaryEntity();
        PregnantEntity pregnantEntity = beneficiaryJoin.getPregnantEntity();
        getViewDataBinding().benfName.setText(beneficiaryEntity.getName());
        getViewDataBinding().benfHusName.setText("w/o:" + beneficiaryEntity.getHusbandName());
        getViewDataBinding().benfName.setText(beneficiaryEntity.getName());
        getViewDataBinding().pctsId.setText("PCTS ID: " + beneficiaryEntity.getPctsId());
        getViewDataBinding().benfLastcheckupdate.setMinDate(pregnantEntity.getLmpDate().getTime());

        getViewDataBinding().benfCurrentWeight.sethValidatorListener(FormValidatorUtils.valueBwValidator(30.0, 99.0,
                "Weight should be from 30 Kg to 99 Kg"));

       /* a. 1st visit: Within 12 weeks—preferably as soon as pregnancy is suspected—for registration of pregnancy and first ANC
        b. 2nd visit: Between 14 and 26 weeks
        c. 3rd visit: Between 28 and 34 weeks
        d. 4th visit: Between 36 weeks and term*/

        int days = HUtil.daysBetween(pregnantEntity.getLmpDate(), new Date());//106.458 ==3.5 month
        //FormValidator
        getViewDataBinding().benfAncDate.sethValidatorListener(new HValidatorListener<Date>() {
            @Override
            public ValidationStatus isValid(Date data) {
                boolean isValid = true;
                int lmpWek = HUtil.daysBetween(pregnantEntity.getLmpDate(), data) / 7;
                if ("PW1".equalsIgnoreCase(subStage) && lmpWek <= 12 ||
                        "PW2".equalsIgnoreCase(subStage) && lmpWek >= 14 && lmpWek <= 26 ||
                        "PW3".equalsIgnoreCase(subStage) && lmpWek >= 28 && lmpWek <= 34 ||
                        "PW4".equalsIgnoreCase(subStage) && lmpWek >= 36 && lmpWek <= 40) {

                } else isValid = false;
                return new ValidationStatus(isValid, "Invalid Anc Date");
            }
        });

    }

    private void fetchFormUiData() {


        Single<PWMonitorEntity> other = Single.just(new PWMonitorEntity());
        if (pwFormId != null)
            other = dataRepository.pwMonitorByID(pwFormId).toSingle();

        Disposable disposable = dataRepository.getBeneficiaryData(beneficiaryId).zipWith(other, Pair::new)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe((pair, throwable) -> {
                    if (throwable != null)
                        throwable.printStackTrace();
                    beneficiaryJoin = pair.first;
                    PWMonitorEntity pwMonitorEntity = pair.second;
                    if (beneficiaryJoin != null)
                        setupUiData();
                    if (pwMonitorEntity != null && pwMonitorEntity.getId() != 0) {
                        PWMonitoringFragment.this.mPwMonitorEntity = pwMonitorEntity;
                        setFormUiData(pwMonitorEntity);

                    }
                });

    }

    private void setFormUiData(PWMonitorEntity pwMonitorEntity) {
        PwMonitoringFragmentBinding vb = getViewDataBinding();

        if (pwMonitorEntity.getAvailable()) {
            vb.benfAncCount.setSection(pwMonitorEntity.getAncCount());
            vb.benfAncDate.setDate(pwMonitorEntity.getLastAnc());
            vb.benfMamtaCdWeight.setText(pwMonitorEntity.getLastWeightInMamta());
            vb.benfLastcheckupdate.setDate(pwMonitorEntity.getLastWeightCheckDate());
            vb.benfCurrentWeight.setText(pwMonitorEntity.getCurrentWeight());


            Set<Integer> regScheme = new HashSet<>();

            if (pwMonitorEntity.getPmmvyInstallment() != null) {
                vb.benfPmmvvyCount.setSectionByData(instalmentValConvt(pwMonitorEntity.getPmmvyInstallment()));
                regScheme.add(0);
            }
            if (pwMonitorEntity.getIgmpyInstallment() != null) {
                vb.benfIgmpyCount.setSectionByData(instalmentValConvt(pwMonitorEntity.getIgmpyInstallment()));
                regScheme.add(1);
            }
            if (pwMonitorEntity.getJsyInstallment() != null) {
                vb.benfJsyCount.setSectionByData(instalmentValConvt(pwMonitorEntity.getJsyInstallment()));
                regScheme.add(2);
            }
            if (pwMonitorEntity.getRajshriInstallment() != null) {
                vb.benfRajshriCount.setSectionByData(instalmentValConvt(pwMonitorEntity.getRajshriInstallment()));
                regScheme.add(3);
            }

            if (regScheme.isEmpty()) {
                regScheme.add(4);
            }

            vb.benfRegisteredProgramme.setSelectedIds(regScheme);
        } else {
            vb.naLy.setVisibility(View.VISIBLE);
            vb.formLy.setVisibility(View.GONE);
            String naReason = pwMonitorEntity.getNaReason();
            if (!TextUtils.isEmpty(naReason))
                if (naReason.equalsIgnoreCase(ANC_NOT_COMPLETED)) {
                    vb.benfNaReason.setSection(0);
                } else {
                    vb.benfNaReason.setSection(1);
                    vb.benfNaOtherReason.setVisibility(View.VISIBLE);
                    vb.benfNaOtherReason.setSection(FormDataConstant.pwNaReason.indexOf(naReason));
                }
        }
        if (pwMonitorEntity.getDataStatus() != DataStatus.NEW) {
            HUtil.recursiveSetEnabled(vb.formContainer, false, R.id.benf_dt_ly, R.id.weight_iv);
        }
    }


    private void save(boolean isNa) {
        List<Pair<Boolean, View>> validateElement = new ArrayList<>();


        PwMonitoringFragmentBinding vb = getViewDataBinding();

        if (!isNa) {
            validateElement.add(vb.benfAncCount.validateWthView());
            validateElement.add(vb.benfAncDate.validateWthView());
            validateElement.add(vb.benfMamtaCdWeight.validateWthView());
            validateElement.add(vb.benfLastcheckupdate.validateWthView());
            validateElement.add(vb.benfCurrentWeight.validateWthView());


            validateElement.add(vb.benfRegisteredProgramme.validateWthView());
            if (vb.benfPmmvvyCount.isVisible())
                validateElement.add(vb.benfPmmvvyCount.validateWthView());
            if (vb.benfIgmpyCount.isVisible())
                validateElement.add(vb.benfIgmpyCount.validateWthView());
            if (vb.benfJsyCount.isVisible())
                validateElement.add(vb.benfJsyCount.validateWthView());
            if (vb.benfRajshriCount.isVisible())
                validateElement.add(vb.benfRajshriCount.validateWthView());
        } else {
            validateElement.add(vb.benfNaReason.validateWthView());
            if (vb.benfNaOtherReason.isVisible())
                validateElement.add(vb.benfNaOtherReason.validateWthView());

        }


        for (Pair<Boolean, View> viewPair : validateElement) {

            if (!viewPair.first) {
                View targetView = viewPair.second;
                targetView.getParent().requestChildFocus(targetView, targetView);
                return;
            }
        }
        BeneficiaryEntity beneficiaryEntity = beneficiaryJoin.getBeneficiaryEntity();
        beneficiaryEntity.setSubStage("PW");
        beneficiaryEntity.setSubStage(subStage);

        PregnantEntity pregnantEntity = beneficiaryJoin.getPregnantEntity();


        PWMonitorEntity pwMonitorEntity;
        if (mPwMonitorEntity == null)
            pwMonitorEntity = new PWMonitorEntity();
        else
            pwMonitorEntity = mPwMonitorEntity;

        pwMonitorEntity.setStage("PW");
        pwMonitorEntity.setSubStage(subStage);
        pwMonitorEntity.setPregnancyId(pregnancyId);
        pwMonitorEntity.setBeneficiaryId(beneficiaryId);

        if (!isNa) {
            pwMonitorEntity.setAvailable(true);
            pwMonitorEntity.setAncCount(vb.benfAncCount.getSelectedPos());
            pwMonitorEntity.setLastAnc(vb.benfAncDate.getDate());
            pwMonitorEntity.setLastWeightInMamta(vb.benfMamtaCdWeight.getMeasValue());
            pwMonitorEntity.setLastWeightCheckDate(vb.benfLastcheckupdate.getDate());
            pwMonitorEntity.setCurrentWeight(vb.benfCurrentWeight.getMeasValue());

            Set<Integer> data = vb.benfRegisteredProgramme.selectedIds();

            if (data.contains(0)) {
                pwMonitorEntity.setPmmvyInstallment(instalmentValConvt(vb.benfPmmvvyCount.getSelectedData()));
            }

            if (data.contains(1)) {
                pwMonitorEntity.setIgmpyInstallment(instalmentValConvt(vb.benfIgmpyCount.getSelectedData()));
            }

            if (data.contains(2)) {
                pwMonitorEntity.setJsyInstallment(instalmentValConvt(vb.benfJsyCount.getSelectedData()));
            }

            if (data.contains(3)) {
                pwMonitorEntity.setRajshriInstallment(instalmentValConvt(vb.benfRajshriCount.getSelectedData()));
            }

        } else {
            pwMonitorEntity.setAvailable(false);
            int pos = vb.benfNaReason.getSelectedPos();
            if (pos == 1) {
                int res = vb.benfNaOtherReason.getSelectedPos();
                String naReason = FormDataConstant.pwNaReason.get(res);
                pwMonitorEntity.setNaReason(naReason);
            } else {
                pwMonitorEntity.setNaReason(ANC_NOT_COMPLETED);

            }

        /*    pwNaReason.add("MA");// Miscarriage/ abortion
            pwNaReason.add("WD");// women death
            pwNaReason.add("WM");// women migrated
            pwNaReason.add("AC");//anc not completed*/

            if (pos == 0) {//MA
                pregnantEntity.setIsActive("N");
            } else if (pos == 1) {
                beneficiaryEntity.setIsActive("N");
                pregnantEntity.setIsActive("N");
            } else if (pos == 2) {
                beneficiaryEntity.setIsActive("N");
            }

        }


        dataRepository.insertOrUpdateBeneficiary(beneficiaryEntity)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe((d) -> {

                });

        dataRepository.insertOrUpdatePwMonitor(pwMonitorEntity)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(() -> {
                    showAlertDialog("Beneficiary Report Saved Successfully", () -> {
                        FragmentUtils.replaceFragment(requireActivity(),
                                CounsellingAnimationFragment.newInstance(0), R.id.fragment_container,
                                true, false, FragmentUtils.TRANSITION_SLIDE_LEFT_RIGHT);

                        requireActivity().getSupportFragmentManager()
                                .beginTransaction()
                                .remove(PWMonitoringFragment.this)
                                .commit();
                    });
                });

    }


}
