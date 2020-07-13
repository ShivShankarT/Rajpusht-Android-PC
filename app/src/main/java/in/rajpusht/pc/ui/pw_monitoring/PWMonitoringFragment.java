package in.rajpusht.pc.ui.pw_monitoring;

import android.annotation.SuppressLint;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.DigitsKeyListener;
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
import in.rajpusht.pc.custom.ui.DropDownModel;
import in.rajpusht.pc.custom.ui.FormDropDownElement;
import in.rajpusht.pc.custom.utils.HUtil;
import in.rajpusht.pc.custom.validator.FormValidatorUtils;
import in.rajpusht.pc.data.DataRepository;
import in.rajpusht.pc.data.local.db.entity.BeneficiaryEntity;
import in.rajpusht.pc.data.local.db.entity.ChildEntity;
import in.rajpusht.pc.data.local.db.entity.LocationEntity;
import in.rajpusht.pc.data.local.db.entity.PWMonitorEntity;
import in.rajpusht.pc.data.local.db.entity.PregnantEntity;
import in.rajpusht.pc.databinding.PwMonitoringFragmentBinding;
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
import io.reactivex.functions.BiConsumer;
import timber.log.Timber;

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
    private Location mLocation;

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

        //todo only when ena
        if (subStage.equalsIgnoreCase("PW3") || subStage.equalsIgnoreCase("PW4")) {
            toolbar.getMenu().add(1, 1, 1, "add child").setIcon(R.drawable.ic_baby).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        }
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == 1) {
                    if (!getViewDataBinding().addChildLy.isEnabled())
                        HUtil.recursiveSetEnabled(getViewDataBinding().addChildLy, true);

                    getViewDataBinding().addChildLy.setVisibility(View.VISIBLE);
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

        viewDataBinding.benfDtLy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentUtils.replaceFragment(requireActivity(),
                        RegistrationFragment.newInstance(beneficiaryId), R.id.fragment_container,
                        true, false, FragmentUtils.TRANSITION_SLIDE_LEFT_RIGHT);
            }
        });

        viewDataBinding.benfAncCount.sethValueChangedListener(new HValueChangedListener<Integer>() {
            @Override
            public void onValueChanged(Integer data) {
                if (data == 0 || data == -1)
                    viewDataBinding.benfAncDate.setVisibility(View.GONE);
                else
                    viewDataBinding.benfAncDate.setVisibility(View.VISIBLE);

            }
        });

        viewDataBinding.weightIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (beneficiaryJoin != null) {
                    PregnantEntity pregnantEntity = beneficiaryJoin.getPregnantEntity();
                    launchCounselling(pregnantEntity);
                }
            }
        });
        HUtil.addEditTextFilter( viewDataBinding.benfBhamashaId.getEditText(),HUtil.alphnumaricFilter());        viewDataBinding.benfPctsid.sethValidatorListener(FormValidatorUtils.textLengthBwValidator(12, 26, getResources().getString(R.string.invalid_pcts)));
        viewDataBinding.benfBhamashaId.sethValidatorListener(FormValidatorUtils.textLengthBwValidator(5, 12, getResources().getString(R.string.invalid_bhamasha_id)));
        viewDataBinding.benfCurrentWeight.sethValidatorListener(FormValidatorUtils.valueBwValidator(30.0, 99.0,
                getString(R.string.incorrect_pw_weight)));
        viewDataBinding.benfMamtaCdWeight.sethValidatorListener(FormValidatorUtils.valueBwValidator(30.0, 99.0,
                getString(R.string.incorrect_pw_weight)));


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
        viewDataBinding.saveNaBtn.setOnClickListener(v -> save(true));
        viewDataBinding.saveBtn.setOnClickListener(v -> save(false));
        viewDataBinding.saveChildBtn.setOnClickListener(v -> addChild());


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
            viewDataBinding.benfAncCount.setSectionList(getResources().getStringArray(R.array.count_0_2));

        } else if (subStage.equalsIgnoreCase("PW2")) {
            viewDataBinding.benfPmmvvyCount.setSectionList(getResources().getStringArray(R.array.count_0_2_dot));
            viewDataBinding.benfIgmpyCount.setSectionList(getResources().getStringArray(R.array.count_0_2_dot));
            viewDataBinding.benfAncCount.setSectionList(getResources().getStringArray(R.array.count_0_4));

        } else if (subStage.equalsIgnoreCase("PW3")) {
            viewDataBinding.benfPmmvvyCount.setSectionList(getResources().getStringArray(R.array.count_0_2_dot));
            viewDataBinding.benfIgmpyCount.setSectionList(getResources().getStringArray(R.array.count_0_3_dot));
            viewDataBinding.benfAncCount.setSectionList(getResources().getStringArray(R.array.count_0_6));
        } else if (subStage.equalsIgnoreCase("PW4")) {
            viewDataBinding.benfPmmvvyCount.setSectionList(getResources().getStringArray(R.array.count_0_2_dot));
            viewDataBinding.benfIgmpyCount.setSectionList(getResources().getStringArray(R.array.count_0_3_dot));
            viewDataBinding.benfAncCount.setSectionList(getResources().getStringArray(R.array.count_0_8));
        }


        fetchFormUiData();
        LocationLiveData.getInstance(requireContext()).observe(getViewLifecycleOwner(), new Observer<Location>() {
            @Override
            public void onChanged(Location location) {
                mLocation = location;
            }
        });
    }

    private void setupUiData() {
        PwMonitoringFragmentBinding vb = getViewDataBinding();
        BeneficiaryEntity beneficiaryEntity = beneficiaryJoin.getBeneficiaryEntity();
        PregnantEntity pregnantEntity = beneficiaryJoin.getPregnantEntity();
        vb.benfName.setText(beneficiaryEntity.getName());
        vb.benfHusName.setText("w/o:" + beneficiaryEntity.getHusbandName());
        vb.benfName.setText(beneficiaryEntity.getName());
        vb.pctsId.setText("PCTS ID: " + beneficiaryEntity.getPctsId());
        vb.benfLastcheckupdate.setMinDate(pregnantEntity.getLmpDate().getTime());
        vb.benfAncDate.setMinDate(pregnantEntity.getLmpDate().getTime());


        vb.benfChildDeliveryPlaceType.sethValueChangedListener(new HValueChangedListener<Integer>() {
            @Override
            public void onValueChanged(Integer data) {
                if (data == 0) {
                    vb.benfChildInstitutionalType.setVisibility(View.VISIBLE);
                    vb.benfChildDeliveryPlace.setVisibility(View.VISIBLE);
                } else {
                    vb.benfChildInstitutionalType.setVisibility(View.GONE);
                    vb.benfChildDeliveryPlace.setVisibility(View.GONE);
                }
            }
        });

        vb.benfChildInstitutionalType.sethValueChangedListener(new HValueChangedListener<Integer>() {
            @Override
            public void onValueChanged(Integer data) {
                String ftype = vb.benfChildInstitutionalType.getSelectedData();
                setLocationData(ftype, vb.benfChildDeliveryPlace);

            }
        });
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




       /* a. 1st visit: Within 12 weeks—preferably as soon as pregnancy is suspected—for registration of pregnancy and first ANC
        b. 2nd visit: Between 14 and 26 weeks
        c. 3rd visit: Between 28 and 34 weeks
        d. 4th visit: Between 36 weeks and term*/


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

        Date createdAt = AppDateTimeUtils.convertDateFromServer(beneficiaryEntity.getCreatedAt());
        if (createdAt != null) {
            Calendar created2DaysAtCal = Calendar.getInstance();
            created2DaysAtCal.setTime(createdAt);
            created2DaysAtCal.add(Calendar.DATE, 2);
            if (created2DaysAtCal.getTime().after(new Date())) {
                vb.benfBhamashaId.setRequired(false);
                vb.benfPctsid.setRequired(false);
            } else {
                vb.benfBhamashaId.setRequired(true);
                vb.benfPctsid.setRequired(true);
            }
        }
       /*
        todo feature use
        if (beneficiaryEntity.getPmmvyInstallment() == null)
            getViewDataBinding().benfRegisteredProgramme.changeEleVisible(new Pair<>(0, false));
        if (beneficiaryEntity.getIgmpyInstallment() == null)
            getViewDataBinding().benfRegisteredProgramme.changeEleVisible(new Pair<>(1, false));
        if (beneficiaryEntity.getJsyInstallment() == null)
            getViewDataBinding().benfRegisteredProgramme.changeEleVisible(new Pair<>(2, false));
        if (beneficiaryEntity.getRajshriInstallment() == null)
            getViewDataBinding().benfRegisteredProgramme.changeEleVisible(new Pair<>(3, false)); */

    }

    private void setLocationData(String ftype, FormDropDownElement formDropDownElement) {
        dataRepository.getInstitutionLocation(ftype)
                .observeOn(schedulerProvider.ui())
                .subscribeOn(schedulerProvider.io()).subscribe(new BiConsumer<List<DropDownModel>, Throwable>() {
            @Override
            public void accept(List<DropDownModel> strings, Throwable throwable) throws Exception {
                if (throwable != null)
                    throwable.printStackTrace();
                if (strings != null)
                    formDropDownElement.setDropDownModels(strings);

            }
        });
    }

    private void fetchFormUiData() {


        Single<PWMonitorEntity> other = Single.just(new PWMonitorEntity());
        if (pwFormId != null)
            other = dataRepository.pwMonitorByID(pwFormId).toSingle();

        Disposable disposable = dataRepository.getBeneficiaryJoinDataFromPregnancyId(pregnancyId).zipWith(other, Pair::new)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe((pair, throwable) -> {
                    if (throwable != null)
                        throwable.printStackTrace();
                    beneficiaryJoin = pair.first;
                    PWMonitorEntity pwMonitorEntity = pair.second;
                    if (pwMonitorEntity != null && pwMonitorEntity.getId() != 0) {
                        PWMonitoringFragment.this.mPwMonitorEntity = pwMonitorEntity;
                    }

                    if (beneficiaryJoin != null)
                        setupUiData();
                    if (mPwMonitorEntity != null) {
                        setFormUiData(mPwMonitorEntity);

                    }
                });

    }

    private void setFormUiData(PWMonitorEntity pwMonitorEntity) {
        PwMonitoringFragmentBinding vb = getViewDataBinding();
        vb.weightIv.setVisibility(View.VISIBLE);

        if (pwMonitorEntity.getAvailable()) {
            vb.benfAncCount.setSectionByData("" + pwMonitorEntity.getAncCount());//todo ce
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
        if (true || pwMonitorEntity.getDataStatus() != DataStatus.NEW) {//todo
            HUtil.recursiveSetEnabled(vb.formContainer, false, R.id.benf_dt_ly, R.id.weight_iv);
        }
    }


    private void save(boolean isNa) {
        List<Pair<Boolean, View>> validateElement = new ArrayList<>();


        PwMonitoringFragmentBinding vb = getViewDataBinding();

        if (!isNa) {

            if (vb.benfPctsid.isVisibleAndEnable())
                validateElement.add(vb.benfPctsid.validateWthView());
            if (vb.benfBhamashaId.isVisibleAndEnable())
                validateElement.add(vb.benfBhamashaId.validateWthView());

            validateElement.add(vb.benfAncCount.validateWthView());
            if (vb.benfAncDate.isVisibleAndEnable())
                validateElement.add(vb.benfAncDate.validateWthView());
            validateElement.add(vb.benfMamtaCdWeight.validateWthView());
            validateElement.add(vb.benfLastcheckupdate.validateWthView());
            validateElement.add(vb.benfCurrentWeight.validateWthView());


            if (vb.benfRegisteredProgramme.isVisibleAndEnable())
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

        long pwFormId = mPwMonitorEntity != null ? mPwMonitorEntity.getId() : System.currentTimeMillis();
        PWMonitorEntity pwMonitorEntity;
        if (mPwMonitorEntity == null) {
            pwMonitorEntity = new PWMonitorEntity();
            pwMonitorEntity.setId(pwFormId);
            pwMonitorEntity.setUuid(UUID.randomUUID().toString());
        } else
            pwMonitorEntity = mPwMonitorEntity;

        pwMonitorEntity.setStage("PW");
        pwMonitorEntity.setSubStage(subStage);
        pwMonitorEntity.setPregnancyId(pregnancyId);
        pwMonitorEntity.setBeneficiaryId(beneficiaryId);

        if (!isNa) {
            pwMonitorEntity.setAvailable(true);
            pwMonitorEntity.setAncCount(vb.benfAncCount.getSelectedPos());
            if (vb.benfAncDate.isVisibleAndEnable())
                pwMonitorEntity.setLastAnc(vb.benfAncDate.getDate());
            pwMonitorEntity.setLastWeightInMamta(vb.benfMamtaCdWeight.getMeasValue());
            pwMonitorEntity.setLastWeightCheckDate(vb.benfLastcheckupdate.getDate());
            pwMonitorEntity.setCurrentWeight(vb.benfCurrentWeight.getMeasValue());

            if (vb.benfPctsid.isVisibleAndEnable())
                beneficiaryEntity.setPctsId(vb.benfPctsid.getText());

            if (vb.benfBhamashaId.isVisibleAndEnable())
                beneficiaryEntity.setBahamashahId(vb.benfBhamashaId.getText());


            Set<Integer> data = vb.benfRegisteredProgramme.selectedIds();

            if (data.contains(0)) {
                pwMonitorEntity.setPmmvyInstallment(instalmentValConvt(vb.benfPmmvvyCount.getSelectedData()));
                beneficiaryEntity.setPmmvyInstallment(instalmentValConvt(vb.benfPmmvvyCount.getSelectedData()));
            }

            if (data.contains(1)) {
                pwMonitorEntity.setIgmpyInstallment(instalmentValConvt(vb.benfIgmpyCount.getSelectedData()));
                beneficiaryEntity.setIgmpyInstallment(instalmentValConvt(vb.benfIgmpyCount.getSelectedData()));
            }

            if (data.contains(2)) {
                pwMonitorEntity.setJsyInstallment(instalmentValConvt(vb.benfJsyCount.getSelectedData()));
                beneficiaryEntity.setJsyInstallment(instalmentValConvt(vb.benfJsyCount.getSelectedData()));
            }

            if (data.contains(3)) {
                pwMonitorEntity.setRajshriInstallment(instalmentValConvt(vb.benfRajshriCount.getSelectedData()));
                beneficiaryEntity.setRajshriInstallment(instalmentValConvt(vb.benfRajshriCount.getSelectedData()));
            }

        } else {
            pwMonitorEntity.setAvailable(false);
            int pos = vb.benfNaReason.getSelectedPos();
            if (pos == 1) {
                int res = vb.benfNaOtherReason.getSelectedPos();
                String naReason = FormDataConstant.pwNaReason.get(res);
                pwMonitorEntity.setNaReason(naReason);

                if (res == 0) {//MA
                    pregnantEntity.setIsActive("N");//
                } else if (res == 1) {//WD
                    beneficiaryEntity.setIsActive("N");
                    pregnantEntity.setIsActive("N");
                } else if (res == 2) {//WM
                    beneficiaryEntity.setIsActive("N");
                }

            } else {
                //AC
                pwMonitorEntity.setNaReason(ANC_NOT_COMPLETED);

            }

        /*    pwNaReason.add("MA");// Miscarriage/ abortion
            pwNaReason.add("WD");// women death
            pwNaReason.add("WM");// women migrated
            pwNaReason.add("AC");//anc not completed*/


        }


        List<Completable> completableSources = new ArrayList<>(Arrays.asList(dataRepository.insertOrUpdateBeneficiary(beneficiaryEntity).ignoreElements(),
                dataRepository.insertOrUpdatePregnant(pregnantEntity).ignoreElements(),
                dataRepository.insertOrUpdatePwMonitor(pwMonitorEntity)));

        LocationEntity locationEntity = new LocationEntity(pwMonitorEntity.getUuid(), 1);
        locationEntity.setBeneficiaryId(beneficiaryId);
        if (mLocation != null)
            locationEntity.setGpsLocation(mLocation.getLatitude() + "," + mLocation.getLongitude());
        String networkParam = LocationLiveData.getNetworkParam(requireContext());
        if (!TextUtils.isEmpty(networkParam))
            locationEntity.setNetworkParam(networkParam);
        if (locationEntity.getGpsLocation() != null || locationEntity.getNetworkParam() != null)
            completableSources.add(dataRepository.insertBeneficiaryLocation(locationEntity).ignoreElement());

        Timber.i(getString(R.string.beneficiary_report_save));
        Disposable disposable = Completable.concat(completableSources)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(() -> {
                    mPwMonitorEntity = pwMonitorEntity;
                    vb.saveBtn.setEnabled(false);
                    showAlertDialog(getString(R.string.beneficiary_report_save), () -> {
                        if (!isNa) {
                            PWMonitoringFragment.this.pwFormId = pwFormId;
                            launchCounselling(pregnantEntity);
                        } else {
                            requireActivity().onBackPressed();
                        }
                    });
                });
        Timber.i(getString(R.string.beneficiary_report_save));

    }

    @SuppressLint("CheckResult")
    private void launchCounselling(PregnantEntity pregnantEntity) {

        if (mPwMonitorEntity != null && mPwMonitorEntity.getDataStatus() == DataStatus.OLD) {
            mPwMonitorEntity.setBeneficiaryId(beneficiaryId);
            Completable.concatArray(dataRepository.insertOrUpdateBeneficiary(beneficiaryJoin.getBeneficiaryEntity()).ignoreElements(),
                    dataRepository.insertOrUpdatePwMonitor(mPwMonitorEntity))
                    .subscribeOn(schedulerProvider.io())
                    .observeOn(schedulerProvider.ui())
                    .subscribe(() -> {
                        ///
                    });
        }
        CounsellingMedia.counsellingSubstage = subStage;
        CounsellingMedia.isTesting = false;
        CounsellingMedia.counsellingFormId = pwFormId;
        CounsellingMedia.counsellingPregId = pregnancyId;
        CounsellingMedia.counsellingPregLmp = pregnantEntity.getLmpDate();
        CounsellingMedia.isPmmvyReg = beneficiaryJoin.getBeneficiaryEntity().getPmmvyInstallment() != null;
        FragmentUtils.replaceFragment(requireActivity(),
                CounsellingAnimationFragment.newInstance(0), R.id.fragment_container,
                true, false, FragmentUtils.TRANSITION_SLIDE_LEFT_RIGHT);
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .remove(PWMonitoringFragment.this)
                .commit();
    }

    private void addChild() {

        BeneficiaryEntity beneficiaryEntity = beneficiaryJoin.getBeneficiaryEntity();
        beneficiaryEntity.setSubStage("PW");
        beneficiaryEntity.setSubStage(subStage);

        PregnantEntity pregnantEntity = beneficiaryJoin.getPregnantEntity();

        List<Pair<Boolean, View>> validateElement = new ArrayList<>();
        PwMonitoringFragmentBinding vb = getViewDataBinding();
        validateElement.add(vb.benfChildCount.validateWthView());
        validateElement.add(vb.benfChildDob.validateWthView());
        if (vb.benfChildDeliveryPlaceType.isVisibleAndEnable())
            validateElement.add(vb.benfChildDeliveryPlaceType.validateWthView());
        if (vb.benfChildDeliveryPlace.isVisible())
            validateElement.add(vb.benfChildDeliveryPlace.validateWthView());


        for (Pair<Boolean, View> viewPair : validateElement) {

            if (!viewPair.first) {
                View targetView = viewPair.second;
                targetView.getParent().requestChildFocus(targetView, targetView);
                return;
            }
        }

        List<Completable> completables = new ArrayList<>();

        for (int i = 0; i <= vb.benfChildCount.getSelectedPos(); i++) {//0,1,2
            ChildEntity childEntity = new ChildEntity();
            int childOrder = i + 1;
            childEntity.setChildOrder(childOrder);
            childEntity.setIsActive("Y");
            Date date = vb.benfChildDob.getDate();
            childEntity.setChildId(Long.parseLong(System.currentTimeMillis() + "1" + childOrder));
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

            childEntity.setDob(date);
            childEntity.setMotherId(beneficiaryId);
            childEntity.setDeliveryPlaceType(vb.benfChildDeliveryPlaceType.getSelectedPos());
            DropDownModel placeSelectedModel = vb.benfChildDeliveryPlace.getSelectedModel();
            childEntity.setDeliveryPlace(placeSelectedModel != null ? placeSelectedModel.getId() : null);
            beneficiaryEntity.setStage(childEntity.getStage());
            beneficiaryEntity.setSubStage(childEntity.getStage());//todo

            completables.add(dataRepository.insertOrUpdateChild(childEntity).ignoreElements());

        }

        pregnantEntity.setIsActive("N");
        completables.add(dataRepository.insertOrUpdateBeneficiary(beneficiaryEntity).ignoreElements());
        completables.add(dataRepository.insertOrUpdatePregnant(pregnantEntity).ignoreElements());

        Disposable disposable = Completable.concat(completables).subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(() -> {
                    showAlertDialog(getString(R.string.beneficiary_child_reg_succ), () -> {
                        requireActivity().onBackPressed();
                    });
                });


    }


}
