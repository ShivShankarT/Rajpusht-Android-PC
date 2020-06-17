package in.rajpusht.pc.ui.lm_monitoring;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.view.MenuItem;
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
import in.rajpusht.pc.custom.callback.HValueChangedListener;
import in.rajpusht.pc.custom.utils.HUtil;
import in.rajpusht.pc.custom.validator.FormValidatorUtils;
import in.rajpusht.pc.data.DataRepository;
import in.rajpusht.pc.data.local.db.entity.BeneficiaryEntity;
import in.rajpusht.pc.data.local.db.entity.ChildEntity;
import in.rajpusht.pc.data.local.db.entity.LMMonitorEntity;
import in.rajpusht.pc.data.local.db.entity.PregnantEntity;
import in.rajpusht.pc.databinding.LmMonitoringFragmentBinding;
import in.rajpusht.pc.model.BeneficiaryJoin;
import in.rajpusht.pc.model.DataStatus;
import in.rajpusht.pc.ui.animation.CounsellingAnimationFragment;
import in.rajpusht.pc.ui.base.BaseFragment;
import in.rajpusht.pc.ui.registration.RegistrationFragment;
import in.rajpusht.pc.utils.FormDataConstant;
import in.rajpusht.pc.utils.FragmentUtils;
import in.rajpusht.pc.utils.rx.SchedulerProvider;
import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.disposables.Disposable;

import static in.rajpusht.pc.utils.FormDataConstant.instalmentValConvt;

public class LMMonitoringFragment extends BaseFragment<LmMonitoringFragmentBinding, LMMonitoringViewModel> {

    private static final int ADD_PREGNANCY_MENU = 12;
    @Inject
    ViewModelProviderFactory factory;
    @Inject
    DataRepository dataRepository;
    @Inject
    SchedulerProvider schedulerProvider;
    private long childId;
    private LMMonitoringViewModel mViewModel;
    private String subStage;
    private Long lmFormId;
    private LMMonitorEntity mLmMonitorEntity;
    private long motherId;
    private BeneficiaryJoin beneficiaryJoin;

    public static LMMonitoringFragment newInstance(long childId, long motherId, String subStage, Long lmFormId) {
        LMMonitoringFragment lmMonitoringFragment = new LMMonitoringFragment();
        Bundle args = new Bundle();
        args.putLong("id", childId);
        args.putLong("motherId", motherId);
        args.putString("subStage", subStage);
        args.putSerializable("lmFormId", lmFormId);
        lmMonitoringFragment.setArguments(args);
        return lmMonitoringFragment;
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.lm_monitoring_fragment;
    }

    @Override
    public LMMonitoringViewModel getViewModel() {
        mViewModel = new ViewModelProvider(this, factory).get(LMMonitoringViewModel.class);
        return mViewModel;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        childId = getArguments().getLong("id");
        motherId = getArguments().getLong("motherId");
        subStage = getArguments().getString("subStage");
        lmFormId = (Long) getArguments().getSerializable("lmFormId");
        Log.i("motherId", "newInstance: motherIddd" + motherId);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        LmMonitoringFragmentBinding viewDataBinding = getViewDataBinding();
        Toolbar toolbar = viewDataBinding.toolbarLy.toolbar;
        toolbar.setTitle(R.string.LM_Monitoring);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == ADD_PREGNANCY_MENU) {
                    if (!getViewDataBinding().addPregnantLy.isEnabled())
                        HUtil.recursiveSetEnabled(getViewDataBinding().addPregnantLy, true);

                    getViewDataBinding().addPregnantLy.setVisibility(View.VISIBLE);
                    getViewDataBinding().naLy.setVisibility(View.GONE);
                    getViewDataBinding().formLy.setVisibility(View.GONE);
                }
                return false;
            }
        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().onBackPressed();
            }
        });

        //FormValidator
        viewDataBinding.benfChildCurrentMuac.sethValidatorListener(FormValidatorUtils.valueBwValidator(5.0, 18.00,
                getString(R.string.incorrct_Muac)));
        viewDataBinding.benfChildLastRecMuac.sethValidatorListener(FormValidatorUtils.valueBwValidator(5.0, 18.00,
                getString(R.string.incorrct_Muac)));

        viewDataBinding.benfCurrentHeight.sethValidatorListener(FormValidatorUtils.valueBwValidatorForStringNumber(10.0, 30.00,
                getString(R.string.incorrct_child_height)));

        viewDataBinding.benfBirthChildWeight.sethValidatorListener(FormValidatorUtils.valueBwValidator(.5, 10.0,
                getString(R.string.incorrct_child_weight)));


        viewDataBinding.benfDtLy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentUtils.replaceFragment(requireActivity(),
                        RegistrationFragment.newInstance(motherId), R.id.fragment_container,
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


        viewDataBinding.saveNaBtn.setOnClickListener(v -> save(true));
        viewDataBinding.saveBtn.setOnClickListener(v -> save(false));
        viewDataBinding.savePrgBtn.setOnClickListener(v -> addPregnancy());


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

    private void fetchFormUiData() {
        Single<LMMonitorEntity> other = Single.just(new LMMonitorEntity());
        if (lmFormId != null)
            other = dataRepository.lmMonitorById(lmFormId).toSingle();
        Disposable disposable = dataRepository.getBeneficiaryData(motherId).zipWith(other, Pair::new)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui()).subscribe((tupleLMMonitorEntityPair, throwable) -> {
                    beneficiaryJoin = tupleLMMonitorEntityPair.first;
                    LMMonitorEntity lmMonitorEntity = tupleLMMonitorEntityPair.second;
                    if (beneficiaryJoin != null)
                        setBenfui();
                    if (lmMonitorEntity != null && lmMonitorEntity.getId() != 0) {
                        LMMonitoringFragment.this.mLmMonitorEntity = lmMonitorEntity;
                        setFormUiData(lmMonitorEntity);
                    }
                });


    }

    private void setBenfui() {

        ChildEntity childEntity = beneficiaryJoin.getChildEntity();
        int days = HUtil.daysBetween(childEntity.getDob(), new Date());//106.458 ==3.5 month
        getViewDataBinding().benfChildImmune.setVisibility(days >= 106 ? View.VISIBLE : View.GONE);
        boolean is182Day = days >= 182;
        getViewDataBinding().benfChildLastRecMuac.setVisibility(is182Day ? View.VISIBLE : View.GONE);
        getViewDataBinding().benfChildLastRecMuacDate.setVisibility(is182Day ? View.VISIBLE : View.GONE);
        getViewDataBinding().benfChildCurrentMuac.setVisibility(is182Day ? View.VISIBLE : View.GONE);

        BeneficiaryEntity beneficiaryEntity = beneficiaryJoin.getBeneficiaryEntity();
        getViewDataBinding().benfName.setText(beneficiaryEntity.getName());
        getViewDataBinding().benfHusName.setText("w/o:" + beneficiaryEntity.getHusbandName());
        getViewDataBinding().benfName.setText(beneficiaryEntity.getName());
        getViewDataBinding().pctsId.setText("PCTS ID: " + beneficiaryEntity.getPctsId());

        //todo only when lm3
        if ((beneficiaryJoin.getPregnantEntity()==null )||(beneficiaryJoin.getPregnantEntity() != null && !"Y".equalsIgnoreCase(beneficiaryJoin.getPregnantEntity().getIsActive()))) {
            getViewDataBinding().toolbarLy.toolbar.getMenu().add(1, ADD_PREGNANCY_MENU, 1, "info").setIcon(R.drawable.ic_child_pregnant).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        }


    }

    private void setFormUiData(LMMonitorEntity lmMonitorEntity) {

        LmMonitoringFragmentBinding vb = getViewDataBinding();

        if (lmMonitorEntity.getAvailable()) {
            vb.benfChildImmune.setSectionByData(lmMonitorEntity.getIsFirstImmunizationComplete());
            vb.benfChildLastRecMuac.setText(lmMonitorEntity.getLastMuac());
            vb.benfChildLastRecMuacDate.setDate(lmMonitorEntity.getLastMuacCheckDate());
            vb.benfChildCurrentMuac.setText(lmMonitorEntity.getCurrentMuac());
            vb.benfBirthChildWeight.setText(lmMonitorEntity.getBirthWeight());
            vb.benfCurrentWeight.setText(lmMonitorEntity.getChildWeight());
            vb.benfCurrentHeight.setText(String.valueOf(lmMonitorEntity.getChildHeight()));


            Set<Integer> regScheme = new HashSet<>();

            if (lmMonitorEntity.getPmmvyInstallment() != null) {
                vb.benfPmmvvyCount.setSectionByData(instalmentValConvt(lmMonitorEntity.getPmmvyInstallment()));
                regScheme.add(0);
            }
            if (lmMonitorEntity.getIgmpyInstallment() != null) {
                vb.benfIgmpyCount.setSectionByData(instalmentValConvt(lmMonitorEntity.getIgmpyInstallment()));
                regScheme.add(1);
            }
            if (lmMonitorEntity.getJsyInstallment() != null) {
                vb.benfJsyCount.setSectionByData(instalmentValConvt(lmMonitorEntity.getJsyInstallment()));
                regScheme.add(2);
            }
            if (lmMonitorEntity.getRajshriInstallment() != null) {
                vb.benfRajshriCount.setSectionByData(instalmentValConvt(lmMonitorEntity.getRajshriInstallment()));
                regScheme.add(3);
            }

            if (regScheme.isEmpty()) {
                regScheme.add(4);
            }

            vb.benfRegisteredProgramme.setSelectedIds(regScheme);


        } else {
            vb.naLy.setVisibility(View.VISIBLE);
            vb.formLy.setVisibility(View.GONE);
            if (!TextUtils.isEmpty(lmMonitorEntity.getNaReason()))
                vb.benfNaOtherReason.setSection(FormDataConstant.lmNaReason.indexOf(lmMonitorEntity.getNaReason()));

        }

        if (lmMonitorEntity.getDataStatus() != DataStatus.NEW) {
            HUtil.recursiveSetEnabled(vb.formContainer, false, R.id.benf_dt_ly, R.id.weight_iv);
        }
    }

    private void save(boolean isNa) {
        LmMonitoringFragmentBinding vb = getViewDataBinding();
        List<Pair<Boolean, View>> validateElement = new ArrayList<>();

        if (!isNa) {
            if (vb.benfChildImmune.isVisibleAndEnable())
                validateElement.add(vb.benfChildImmune.validateWthView());
            if (vb.benfChildLastRecMuac.isVisibleAndEnable())
                validateElement.add(vb.benfChildLastRecMuac.validateWthView());
            if (vb.benfChildLastRecMuacDate.isVisibleAndEnable())
                validateElement.add(vb.benfChildLastRecMuacDate.validateWthView());
            validateElement.add(vb.benfBirthChildWeight.validateWthView());
            if (vb.benfChildCurrentMuac.isVisibleAndEnable())
                validateElement.add(vb.benfChildCurrentMuac.validateWthView());
            validateElement.add(vb.benfCurrentWeight.validateWthView());
            validateElement.add(vb.benfCurrentHeight.validateWthView());


            validateElement.add(vb.benfRegisteredProgramme.validateWthView());
            if (vb.benfPmmvvyCount.isVisible())
                validateElement.add(vb.benfPmmvvyCount.validateWthView());
            if (vb.benfIgmpyCount.isVisible())
                validateElement.add(vb.benfIgmpyCount.validateWthView());
            if (vb.benfJsyCount.isVisible())
                validateElement.add(vb.benfJsyCount.validateWthView());
            if (vb.benfRajshriCount.isVisible())
                validateElement.add(vb.benfRajshriCount.validateWthView());
        }


        for (Pair<Boolean, View> viewPair : validateElement) {

            if (!viewPair.first) {
                View targetView = viewPair.second;
                targetView.getParent().requestChildFocus(targetView, targetView);
                return;
            }
        }

        LMMonitorEntity lmMonitorEntity;
        if (mLmMonitorEntity == null)
            lmMonitorEntity = new LMMonitorEntity();
        else
            lmMonitorEntity = mLmMonitorEntity;

        lmMonitorEntity.setChildId(childId);
        lmMonitorEntity.setStage("LM");
        lmMonitorEntity.setSubStage(subStage);

        BeneficiaryEntity beneficiaryEntity = beneficiaryJoin.getBeneficiaryEntity();
        PregnantEntity pregnantEntity = beneficiaryJoin.getPregnantEntity();
        ChildEntity childEntity = beneficiaryJoin.getChildEntity();

        if (pregnantEntity == null) {
            beneficiaryEntity.setStage(lmMonitorEntity.getStage());
            beneficiaryEntity.setSubStage(lmMonitorEntity.getSubStage());
        }

        childEntity.setStage(lmMonitorEntity.getStage());
        childEntity.setSubStage(lmMonitorEntity.getSubStage());

        if (!isNa) {
            if (vb.benfChildImmune.isVisibleAndEnable())
                lmMonitorEntity.setIsFirstImmunizationComplete(vb.benfChildImmune.getSelectedData());
            lmMonitorEntity.setLastMuac(vb.benfChildLastRecMuac.getMeasValue());
            lmMonitorEntity.setLastMuacCheckDate(vb.benfChildLastRecMuacDate.getDate());
            lmMonitorEntity.setCurrentMuac(vb.benfChildCurrentMuac.getMeasValue());
            lmMonitorEntity.setBirthWeight(vb.benfBirthChildWeight.getMeasValue());
            lmMonitorEntity.setChildWeight(vb.benfCurrentWeight.getMeasValue());
            lmMonitorEntity.setChildHeight(Double.valueOf(vb.benfCurrentHeight.getText()));
            lmMonitorEntity.setAvailable(true);
        } else {
            lmMonitorEntity.setAvailable(false);
            int res = vb.benfNaOtherReason.getSelectedPos();
            String naReason = FormDataConstant.lmNaReason.get(res);
            lmMonitorEntity.setNaReason(naReason);

            if (res == 0) {//mother death
                beneficiaryEntity.setIsActive("N");
                childEntity.setIsActive("N");
            } else if (res == 1) {//mother migrated
                beneficiaryEntity.setIsActive("N");
                childEntity.setIsActive("N");
            } else if (res == 2) {//child death
                childEntity.setIsActive("N");
            } else if (res == 3) {//child migrated
                childEntity.setIsActive("N");
            } else if (res == 4) {//both death
                beneficiaryEntity.setIsActive("N");
                childEntity.setIsActive("N");
            } else if (res == 5) {
                beneficiaryEntity.setIsActive("N");
                childEntity.setIsActive("N");//both migrated
            }

/*            lmNaReason.add("MD");//mother death
            lmNaReason.add("MM");//mother migrated
            lmNaReason.add("CD");//child death
            lmNaReason.add("CM");//child migrated
            lmNaReason.add("BD");//both death
            lmNaReason.add("BM");//both migrated*/


        }

        Set<Integer> data = vb.benfRegisteredProgramme.selectedIds();

        if (data.contains(0)) {
            lmMonitorEntity.setPmmvyInstallment(instalmentValConvt(vb.benfPmmvvyCount.getSelectedData()));
        }

        if (data.contains(1)) {
            lmMonitorEntity.setIgmpyInstallment(instalmentValConvt(vb.benfIgmpyCount.getSelectedData()));
        }

        if (data.contains(2)) {
            lmMonitorEntity.setJsyInstallment(instalmentValConvt(vb.benfJsyCount.getSelectedData()));
        }

        if (data.contains(3)) {
            lmMonitorEntity.setRajshriInstallment(instalmentValConvt(vb.benfRajshriCount.getSelectedData()));
        }
        lmMonitorEntity.setMotherId(motherId);


        Completable.concatArray(dataRepository.insertOrUpdateBeneficiary(beneficiaryEntity).ignoreElements(),
                dataRepository.insertOrUpdateChild(childEntity).ignoreElements(),
                dataRepository.insertOrUpdateLmMonitor(lmMonitorEntity))
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui()).subscribe(() -> {
            showAlertDialog("Beneficiary Child Report Saved Successfully", () -> {
                if (!isNa) {
                    requireActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .remove(LMMonitoringFragment.this)
                            .commit();
                    FragmentUtils.replaceFragment(requireActivity(),
                            CounsellingAnimationFragment.newInstance(0), R.id.fragment_container,
                            true, false, FragmentUtils.TRANSITION_SLIDE_LEFT_RIGHT);
                } else {
                    requireActivity().onBackPressed();
                }

            });
        });

    }


    private void addPregnancy() {

        LmMonitoringFragmentBinding vb = getViewDataBinding();
        if (!vb.benfLmp.validate()) {
            return;
        }
        BeneficiaryEntity beneficiaryEntity = beneficiaryJoin.getBeneficiaryEntity();
        PregnantEntity pregnantEntity = new PregnantEntity();
        pregnantEntity.setIsActive("Y");
        pregnantEntity.setBeneficiaryId(beneficiaryEntity.getBeneficiaryId());
        pregnantEntity.setPregnancyId(System.currentTimeMillis());

        Date lmpdate = vb.benfLmp.getDate();
        pregnantEntity.setLmpDate(lmpdate);
        beneficiaryEntity.setStage("PW");
        beneficiaryEntity.setSubStage("PW");
        beneficiaryEntity.setIsActive("Y");

        Disposable disposable = Completable.concatArray(dataRepository.insertOrUpdateBeneficiary(beneficiaryEntity).ignoreElements(),
                dataRepository.insertOrUpdatePregnant(pregnantEntity).ignoreElements())
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(() -> {
                    showAlertDialog("Beneficiary Pregnancy Registration Successfully", () -> {
                        requireActivity().onBackPressed();
                    });
                });


    }


}
