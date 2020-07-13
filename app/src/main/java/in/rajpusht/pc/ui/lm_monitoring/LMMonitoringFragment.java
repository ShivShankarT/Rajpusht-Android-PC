package in.rajpusht.pc.ui.lm_monitoring;

import android.annotation.SuppressLint;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Pair;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.library.baseAdapters.BR;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

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
import in.rajpusht.pc.data.local.db.entity.LocationEntity;
import in.rajpusht.pc.data.local.db.entity.PregnantEntity;
import in.rajpusht.pc.databinding.LmMonitoringFragmentBinding;
import in.rajpusht.pc.model.BeneficiaryJoin;
import in.rajpusht.pc.model.CounsellingMedia;
import in.rajpusht.pc.model.DataStatus;
import in.rajpusht.pc.ui.animation.CounsellingAnimationFragment;
import in.rajpusht.pc.ui.base.BaseFragment;
import in.rajpusht.pc.ui.registration.RegistrationFragment;
import in.rajpusht.pc.utils.AppDateTimeUtils;
import in.rajpusht.pc.utils.FormDataConstant;
import in.rajpusht.pc.utils.FragmentUtils;
import in.rajpusht.pc.utils.LocationLiveData;
import in.rajpusht.pc.utils.rx.SchedulerProvider;
import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

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
    private Location mLocation;

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
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        LmMonitoringFragmentBinding viewDataBinding = getViewDataBinding();
        Toolbar toolbar = viewDataBinding.toolbarLy.toolbar;
        if (subStage.contains("LM"))
            toolbar.setTitle(R.string.LM_Monitoring);
        else
            toolbar.setTitle(R.string.MY_Monitoring);
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

        HUtil.addEditTextFilter(viewDataBinding.benfBhamashaId.getEditText(), HUtil.alphnumaricFilter());
        viewDataBinding.benfPctsid.sethValidatorListener(FormValidatorUtils.textLengthBwValidator(12, 26, getResources().getString(R.string.invalid_pcts)));
        viewDataBinding.benfBhamashaId.sethValidatorListener(FormValidatorUtils.textLengthBwValidator(5, 12, getResources().getString(R.string.invalid_bhamasha_id)));

        //FormValidator
        viewDataBinding.benfChildCurrentMuac.sethValidatorListener(FormValidatorUtils.valueBwValidator(5.0, 18.00,
                getString(R.string.incorrct_Muac)));
        viewDataBinding.benfChildLastRecMuac.sethValidatorListener(FormValidatorUtils.valueBwValidator(5.0, 18.00,
                getString(R.string.incorrct_Muac)));

        viewDataBinding.benfCurrentHeight.sethValidatorListener(FormValidatorUtils.valueBwValidatorForStringNumber(30d, 100d,
                getString(R.string.incorrct_child_height)));

        viewDataBinding.benfBirthChildWeight.sethValidatorListener(FormValidatorUtils.valueBwValidator(.5, 15.0,
                getString(R.string.incorrct_child_weight)));

        viewDataBinding.benfCurrentWeight.sethValidatorListener(FormValidatorUtils.valueBwValidator(.5, 15.0,
                getString(R.string.incorrct_child_weight)));

        viewDataBinding.benfPctsChildId.sethValidatorListener(FormValidatorUtils.textLengthBwValidator(1, 2, getResources().getString(R.string.invalid_pcts)));
        viewDataBinding.benfOpdIpd.sethValidatorListener(FormValidatorUtils.textLengthBwValidator(1, 25, getResources().getString(R.string.invalid_OPD_IPD)));

        viewDataBinding.benfBirthWeightSource.sethValueChangedListener(new HValueChangedListener<Integer>() {
            @Override
            public void onValueChanged(Integer data) {

                //If Discharge Slip selected then OPD/IPD is mandatory.
                //child ID mandatory if institute delivery else optional.
                if (data == 0) {//discharge_slip-->0 ; mamta_mcp_card->1
                    viewDataBinding.benfOpdIpd.setRequired(true);
                } else {
                    viewDataBinding.benfOpdIpd.setRequired(false);
                }


            }
        });


        viewDataBinding.benfDtLy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentUtils.replaceFragment(requireActivity(),
                        RegistrationFragment.newInstance(motherId), R.id.fragment_container,
                        true, false, FragmentUtils.TRANSITION_SLIDE_LEFT_RIGHT);
            }
        });
        viewDataBinding.benfPmmvvyCount.setVisibility(View.GONE);
        viewDataBinding.benfIgmpyCount.setVisibility(View.GONE);
        viewDataBinding.benfJsyCount.setVisibility(View.GONE);
        viewDataBinding.benfRajshriCount.setVisibility(View.GONE);
        viewDataBinding.benfInstalLy.setVisibility(View.GONE);

        viewDataBinding.benfRegisteredProgramme.changeEleVisible(new Pair<>(1, false));//todo always IGMPY
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
                } else if (data.isEmpty()) {
                    viewDataBinding.benfInstalLy.setVisibility(View.GONE);
                } else {
                    viewDataBinding.benfInstalLy.setVisibility(View.VISIBLE);

                }


            }
        });

        viewDataBinding.benfPctsid.sethValueChangedListener(new HValueChangedListener<String>() {
            @Override
            public void onValueChanged(String data) {
                viewDataBinding.benfPctsChildId.setPrefixText(data);
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


        viewDataBinding.weightIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchCounselling();
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

        LocationLiveData.getInstance(requireContext()).observe(getViewLifecycleOwner(), new Observer<Location>() {
            @Override
            public void onChanged(Location location) {
                mLocation = location;
            }
        });


    }

    private void fetchFormUiData() {
        Single<LMMonitorEntity> other = Single.just(new LMMonitorEntity());
        if (lmFormId != null)
            other = dataRepository.lmMonitorById(lmFormId).toSingle();
        Disposable disposable = dataRepository.getBeneficiaryJoinDataFromChildId(childId).zipWith(other, Pair::new)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui()).subscribe((tupleLMMonitorEntityPair, throwable) -> {
                    beneficiaryJoin = tupleLMMonitorEntityPair.first;
                    LMMonitorEntity lmMonitorEntity = tupleLMMonitorEntityPair.second;
                    if (lmMonitorEntity != null && lmMonitorEntity.getId() != 0) {
                        LMMonitoringFragment.this.mLmMonitorEntity = lmMonitorEntity;
                    }
                    if (beneficiaryJoin != null) {
                        setBenfui();
                        setChildDataUi();
                    }

                    if (mLmMonitorEntity != null) {
                        setFormUiData(mLmMonitorEntity);
                    }
                });


    }

    private void setBenfui() {

        ChildEntity childEntity = beneficiaryJoin.getChildEntity();

        if (!"F".equals(childEntity.getChildSex()))
            getViewDataBinding().benfRegisteredProgramme.changeEleVisible(new Pair<>(3, true));//todo always IGMPY

        int days = HUtil.daysBetween(childEntity.getDob(), new Date());//106.458 ==3.5 month
        CounsellingMedia.counsellingChildIAgeInDay = days;
        LmMonitoringFragmentBinding vb = getViewDataBinding();
        vb.benfChildImmune.setVisibility(days >= 106 ? View.VISIBLE : View.GONE);
        boolean is182Day = days >= 182;
        vb.benfChildLastRecMuac.setVisibility(is182Day ? View.VISIBLE : View.GONE);
        vb.benfChildLastRecMuacDate.setVisibility(is182Day ? View.VISIBLE : View.GONE);
        vb.benfChildCurrentMuac.setVisibility(is182Day ? View.VISIBLE : View.GONE);

        BeneficiaryEntity beneficiaryEntity = beneficiaryJoin.getBeneficiaryEntity();
        vb.benfName.setText(beneficiaryEntity.getName());
        vb.benfHusName.setText("w/o:" + beneficiaryEntity.getHusbandName());
        vb.benfName.setText(beneficiaryEntity.getName());
        vb.pctsId.setText("PCTS ID: " + beneficiaryEntity.getPctsId());

        vb.benfPctsid.setText(beneficiaryEntity.getPctsId());
        vb.benfBhamashaId.setText(beneficiaryEntity.getBahamashahId());
        if (TextUtils.isEmpty(beneficiaryEntity.getPctsId()))
            vb.benfPctsid.setVisibility(View.VISIBLE);
        else
            vb.benfPctsid.setVisibility(View.GONE);

        if (TextUtils.isEmpty(beneficiaryEntity.getBahamashahId()))
            vb.benfBhamashaId.setVisibility(View.VISIBLE);
        else
            vb.benfBhamashaId.setVisibility(View.GONE);


        //child ID mandatory if institute delivery else optional.
        if (childEntity.getDeliveryPlaceType() != null && childEntity.getDeliveryPlaceType() == 0)
            vb.benfPctsChildId.setRequired(true);
        else
            vb.benfPctsChildId.setRequired(false);

        Set<Integer> regScheme = new HashSet<>();
        //set max values because reg screen user select max values
        if (beneficiaryEntity.getPmmvyInstallment() != null) {
            vb.benfPmmvvyCount.setSectionList(getResources().getStringArray(R.array.count_0_3_dot));
            vb.benfPmmvvyCount.setSectionByData(instalmentValConvt(beneficiaryEntity.getPmmvyInstallment()));
            regScheme.add(0);
        }
        if (beneficiaryEntity.getIgmpyInstallment() != null) {
            vb.benfIgmpyCount.setSectionList(getResources().getStringArray(R.array.count_0_4_dot));
            vb.benfIgmpyCount.setSectionByData(instalmentValConvt(beneficiaryEntity.getIgmpyInstallment()));
            regScheme.add(1);
        }
        if (beneficiaryEntity.getJsyInstallment() != null) {
            vb.benfJsyCount.setSectionList(getResources().getStringArray(R.array.count_0_1_dot));
            vb.benfJsyCount.setSectionByData(instalmentValConvt(beneficiaryEntity.getJsyInstallment()));
            regScheme.add(2);
        }
        if (beneficiaryEntity.getRajshriInstallment() != null) {
            vb.benfRajshriCount.setSectionList(getResources().getStringArray(R.array.count_0_2_dot));
            vb.benfRajshriCount.setSectionByData(instalmentValConvt(beneficiaryEntity.getRajshriInstallment()));
            regScheme.add(3);
        }


        if (regScheme.isEmpty()) {
            //regScheme.add(4);
            vb.benfRegisteredProgramme.setVisibility(View.VISIBLE);
        } else {
            vb.benfRegisteredProgramme.setSelectedIds(regScheme);
            vb.benfInstalLy.setVisibility(View.GONE);
            vb.benfRegisteredProgramme.setVisibility(View.GONE);
            vb.benfPmmvvyCount.setVisibility(View.GONE);
            vb.benfIgmpyCount.setVisibility(View.GONE);
            vb.benfRajshriCount.setVisibility(View.GONE);
            vb.benfJsyCount.setVisibility(View.GONE);


        }


        if (!subStage.equals("LM1") && ((beneficiaryJoin.getPregnantEntity() == null) || (beneficiaryJoin.getPregnantEntity() != null && !"Y".equalsIgnoreCase(beneficiaryJoin.getPregnantEntity().getIsActive())))) {
            vb.toolbarLy.toolbar.getMenu().add(1, ADD_PREGNANCY_MENU, 1, "info").setIcon(R.drawable.ic_child_pregnant).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        }

        Date createdAt = AppDateTimeUtils.convertDateFromServer(beneficiaryEntity.getCreatedAt());
        if (createdAt != null) {
            Calendar created2DaysAtCal = Calendar.getInstance();
            created2DaysAtCal.setTime(createdAt);
            created2DaysAtCal.add(Calendar.DATE, 2);
            if (created2DaysAtCal.getTime().after(new Date())) {
                vb.benfBhamashaId.setRequired(false);
                vb.benfPctsid.setRequired(false);
                vb.benfPctsChildId.setRequired(false);
            } else {
                vb.benfBhamashaId.setRequired(true);
                vb.benfPctsid.setRequired(true);
                if (childEntity.getDeliveryPlaceType() != null && childEntity.getDeliveryPlaceType() == 0)
                    vb.benfPctsChildId.setRequired(true);
                else
                    vb.benfPctsChildId.setRequired(false);
            }
        }


    }

    private void setChildDataUi() {

        LmMonitoringFragmentBinding vb = getViewDataBinding();
        LMMonitorEntity lmMonitorEntity = this.mLmMonitorEntity;

        ChildEntity childEntity = beneficiaryJoin.getChildEntity();
        //boolean isDisableChildEdit = lmMonitorEntity != null && lmMonitorEntity.getDataStatus() != DataStatus.NEW;
        boolean isDisableChildEdit = true;//todo check
        if (childEntity.getBirthWeight() != null) {
            vb.benfBirthChildWeight.setText(childEntity.getBirthWeight());
            if (isDisableChildEdit)
                vb.benfBirthChildWeight.setEnableChild(false);
        }

        if (!TextUtils.isEmpty(childEntity.getPctsChildId())) {
            String pctsChildId = childEntity.getPctsChildId().replace(beneficiaryJoin.getBeneficiaryEntity().getPctsId(), "");
            vb.benfPctsChildId.setText(pctsChildId);
            if (isDisableChildEdit)
                vb.benfPctsChildId.setEnableChild(false);
        }

        if (childEntity.getBirthWeightSource() != null) {
            vb.benfBirthWeightSource.setSection(childEntity.getBirthWeightSource());
            if (isDisableChildEdit)
                vb.benfBirthWeightSource.setEnableChild(false);
        }


        if (!TextUtils.isEmpty(childEntity.getIsFirstImmunizationComplete())) {
            vb.benfChildImmune.setSectionByData(childEntity.getIsFirstImmunizationComplete());
            //todo Immune will done laty
            if (vb.benfChildImmune.getSelectedPos() == 0) {
                vb.benfChildImmune.setEnableChild(false);
            } else {
                vb.benfChildImmune.setEnableChild(true);
            }

        }

        if (childEntity.getOpdipd() != null) {
            vb.benfOpdIpd.setText(String.valueOf(childEntity.getOpdipd()));
            if (isDisableChildEdit)
                vb.benfOpdIpd.setEnableChild(false);

        }
    }

    private void setFormUiData(LMMonitorEntity lmMonitorEntity) {
        LmMonitoringFragmentBinding vb = getViewDataBinding();
        vb.weightIv.setVisibility(View.VISIBLE);
        if (lmMonitorEntity.getAvailable()) {

            vb.benfChildLastRecMuac.setText(lmMonitorEntity.getLastMuac());
            vb.benfChildLastRecMuacDate.setDate(lmMonitorEntity.getLastMuacCheckDate());
            vb.benfChildCurrentMuac.setText(lmMonitorEntity.getCurrentMuac());

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

        if (true || lmMonitorEntity.getDataStatus() != DataStatus.NEW) {//todo
            HUtil.recursiveSetEnabled(vb.formContainer, false, R.id.benf_dt_ly, R.id.weight_iv);
        }
    }

    @SuppressLint("CheckResult")
    private void save(boolean isNa) {
        LmMonitoringFragmentBinding vb = getViewDataBinding();
        List<Pair<Boolean, View>> validateElement = new ArrayList<>();

        if (!isNa) {

            if (vb.benfPctsid.isVisibleAndEnable())
                validateElement.add(vb.benfPctsid.validateWthView());
            if (vb.benfBhamashaId.isVisibleAndEnable())
                validateElement.add(vb.benfBhamashaId.validateWthView());

            if (vb.benfChildImmune.isVisibleAndEnable())
                validateElement.add(vb.benfChildImmune.validateWthView());
            if (vb.benfPctsChildId.isVisibleAndEnable())
                validateElement.add(vb.benfPctsChildId.validateWthView());
            validateElement.add(vb.benfBirthWeightSource.validateWthView());
            validateElement.add(vb.benfOpdIpd.validateWthView());
            if (vb.benfChildLastRecMuac.isVisibleAndEnable())
                validateElement.add(vb.benfChildLastRecMuac.validateWthView());
            if (vb.benfChildLastRecMuacDate.isVisibleAndEnable())
                validateElement.add(vb.benfChildLastRecMuacDate.validateWthView());
            validateElement.add(vb.benfBirthChildWeight.validateWthView());
            if (vb.benfChildCurrentMuac.isVisibleAndEnable())
                validateElement.add(vb.benfChildCurrentMuac.validateWthView());
            validateElement.add(vb.benfCurrentWeight.validateWthView());
            validateElement.add(vb.benfCurrentHeight.validateWthView());

            if (vb.benfRegisteredProgramme.isVisible())
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
            validateElement.add(vb.benfNaOtherReason.validateWthView());
        }


        for (Pair<Boolean, View> viewPair : validateElement) {

            if (!viewPair.first) {
                View targetView = viewPair.second;
                targetView.getParent().requestChildFocus(targetView, targetView);
                return;
            }
        }

        long lmFormId = mLmMonitorEntity != null ? mLmMonitorEntity.getId() : System.currentTimeMillis();
        LMMonitorEntity lmMonitorEntity;
        if (mLmMonitorEntity == null) {
            lmMonitorEntity = new LMMonitorEntity();
            lmMonitorEntity.setId(lmFormId);
            lmMonitorEntity.setUuid(UUID.randomUUID().toString());
        } else
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
                childEntity.setIsFirstImmunizationComplete(vb.benfChildImmune.getSelectedData());
            lmMonitorEntity.setLastMuac(vb.benfChildLastRecMuac.getMeasValue());
            lmMonitorEntity.setLastMuacCheckDate(vb.benfChildLastRecMuacDate.getDate());
            lmMonitorEntity.setCurrentMuac(vb.benfChildCurrentMuac.getMeasValue());
            childEntity.setBirthWeight(vb.benfBirthChildWeight.getMeasValue());
            lmMonitorEntity.setChildWeight(vb.benfCurrentWeight.getMeasValue());
            lmMonitorEntity.setChildHeight(Double.valueOf(vb.benfCurrentHeight.getText()));

            if (vb.benfPctsChildId.isVisibleAndEnable() && !TextUtils.isEmpty(vb.benfPctsChildId.getText())) {
                String text = vb.benfPctsid.getText() + vb.benfPctsChildId.getText();
                childEntity.setPctsChildId(text);
            }

            childEntity.setBirthWeightSource(vb.benfBirthWeightSource.getSelectedPos());
            String opdIpd = vb.benfOpdIpd.getText().trim();
            if (!opdIpd.isEmpty())
                childEntity.setOpdipd(Long.valueOf(opdIpd));
            lmMonitorEntity.setAvailable(true);

            if (vb.benfPctsid.isVisibleAndEnable())
                beneficiaryEntity.setPctsId(vb.benfPctsid.getText());

            if (vb.benfBhamashaId.isVisibleAndEnable())
                beneficiaryEntity.setBahamashahId(vb.benfBhamashaId.getText());


            Set<Integer> data = vb.benfRegisteredProgramme.selectedIds();

            if (data.contains(0)) {
                lmMonitorEntity.setPmmvyInstallment(instalmentValConvt(vb.benfPmmvvyCount.getSelectedData()));
                beneficiaryEntity.setPmmvyInstallment(instalmentValConvt(vb.benfPmmvvyCount.getSelectedData()));
            }

            if (data.contains(1)) {
                lmMonitorEntity.setIgmpyInstallment(instalmentValConvt(vb.benfIgmpyCount.getSelectedData()));
                beneficiaryEntity.setIgmpyInstallment(instalmentValConvt(vb.benfIgmpyCount.getSelectedData()));
            }

            if (data.contains(2)) {
                lmMonitorEntity.setJsyInstallment(instalmentValConvt(vb.benfJsyCount.getSelectedData()));
                beneficiaryEntity.setJsyInstallment(instalmentValConvt(vb.benfJsyCount.getSelectedData()));
            }

            if (data.contains(3)) {
                lmMonitorEntity.setRajshriInstallment(instalmentValConvt(vb.benfRajshriCount.getSelectedData()));
                beneficiaryEntity.setRajshriInstallment(instalmentValConvt(vb.benfRajshriCount.getSelectedData()));
            }


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
        }


        List<Completable> completableSources = new ArrayList<>(Arrays.asList(dataRepository.insertOrUpdateBeneficiary(beneficiaryEntity).ignoreElements(),
                dataRepository.insertOrUpdateChild(childEntity).ignoreElements(),
                dataRepository.insertOrUpdateLmMonitor(lmMonitorEntity)));

        lmMonitorEntity.setMotherId(motherId);
        LocationEntity locationEntity = new LocationEntity(lmMonitorEntity.getUuid(), 2);
        locationEntity.setBeneficiaryId(motherId);
        if (mLocation != null)
            locationEntity.setGpsLocation(mLocation.getLatitude() + "," + mLocation.getLongitude());
        String networkParam = LocationLiveData.getNetworkParam(requireContext());
        if (!TextUtils.isEmpty(networkParam))
            locationEntity.setNetworkParam(networkParam);
        if (locationEntity.getGpsLocation() != null || locationEntity.getNetworkParam() != null)
            completableSources.add(dataRepository.insertBeneficiaryLocation(locationEntity).ignoreElement());

        Completable.concat(completableSources)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui()).subscribe(() -> {
            mLmMonitorEntity = lmMonitorEntity;
            vb.saveBtn.setEnabled(false);
            showAlertDialog(getString(R.string.beneficiary_child_report_saved), () -> {
                if (!isNa) {
                    LMMonitoringFragment.this.lmFormId = lmFormId;
                    launchCounselling();
                } else {
                    requireActivity().onBackPressed();
                }

            });
        });
        Timber.i(getString(R.string.beneficiary_child_report_saved));

    }

    @SuppressLint("CheckResult")
    private void launchCounselling() {

        if (mLmMonitorEntity != null && mLmMonitorEntity.getDataStatus() == DataStatus.OLD) {
            mLmMonitorEntity.setMotherId(motherId);
            Completable.concatArray(
                    dataRepository.insertOrUpdateChild(beneficiaryJoin.getChildEntity()).ignoreElements(),
                    dataRepository.insertOrUpdateLmMonitor(mLmMonitorEntity))
                    .subscribeOn(schedulerProvider.io())
                    .observeOn(schedulerProvider.ui())
                    .subscribe(() -> {
                    });
        }

        CounsellingMedia.isTesting = false;
        CounsellingMedia.counsellingSubstage = subStage;
        CounsellingMedia.counsellingChildId = childId;
        CounsellingMedia.counsellingFormId = lmFormId;
        CounsellingMedia.isPmmvyReg = beneficiaryJoin.getBeneficiaryEntity().getPmmvyInstallment() != null;

        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .remove(LMMonitoringFragment.this)
                .commit();

        FragmentUtils.replaceFragment(requireActivity(),
                CounsellingAnimationFragment.newInstance(0), R.id.fragment_container,
                true, false, FragmentUtils.TRANSITION_SLIDE_LEFT_RIGHT);
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
                    showAlertDialog(getString(R.string.beneficiary_pregnancy_reg_succ), () -> {
                        requireActivity().onBackPressed();
                    });
                });


    }


}
