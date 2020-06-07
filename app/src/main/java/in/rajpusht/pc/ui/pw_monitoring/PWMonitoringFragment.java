package in.rajpusht.pc.ui.pw_monitoring;

import android.os.Bundle;
import android.util.Pair;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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
import in.rajpusht.pc.data.DataRepository;
import in.rajpusht.pc.data.local.db.entity.BeneficiaryEntity;
import in.rajpusht.pc.data.local.db.entity.PWMonitorEntity;
import in.rajpusht.pc.databinding.PwMonitoringFragmentBinding;
import in.rajpusht.pc.ui.animation.CounsellingAnimationFragment;
import in.rajpusht.pc.ui.base.BaseFragment;
import in.rajpusht.pc.ui.registration.RegistrationFragment;
import in.rajpusht.pc.utils.AppDateTimeUtils;
import in.rajpusht.pc.utils.FragmentUtils;
import in.rajpusht.pc.utils.rx.SchedulerProvider;
import io.reactivex.functions.Consumer;

public class PWMonitoringFragment extends BaseFragment<PwMonitoringFragmentBinding, PWMonitoringViewModel> {

    @Inject
    ViewModelProviderFactory factory;
    @Inject
    DataRepository dataRepository;
    @Inject
    SchedulerProvider schedulerProvider;
    BeneficiaryEntity beneficiaryEntity;
    private PWMonitoringViewModel mViewModel;
    private long pregnancyId;
    private String subStage;
    private long beneficiaryId;
    private Long pwFormId;
    private PWMonitorEntity mPwMonitorEntity;

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
                        true, FragmentUtils.TRANSITION_SLIDE_LEFT_RIGHT);
            }
        });

        viewDataBinding.weightIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentUtils.replaceFragment(requireActivity(),
                        CounsellingAnimationFragment.newInstance(0), R.id.fragment_container,
                        true, FragmentUtils.TRANSITION_SLIDE_LEFT_RIGHT);
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

        viewDataBinding.saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();

            }
        });

        dataRepository.getBeneficiary(beneficiaryId)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui()).subscribe((d) -> {
            beneficiaryEntity = d;
            setupUiData();
        });
        if (pwFormId != null)
            fetchFormUiData();
    }

    private void setupUiData() {
        getViewDataBinding().benfName.setText(beneficiaryEntity.getName());
        getViewDataBinding().benfHusName.setText("w/o:" + beneficiaryEntity.getHusbandName());
        getViewDataBinding().benfName.setText(beneficiaryEntity.getName());
        getViewDataBinding().pctsId.setText("PCTS ID: " + beneficiaryEntity.getPctsId());
    }

    private void fetchFormUiData() {

        dataRepository.pwMonitorByID(pwFormId)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui()).subscribe(new Consumer<PWMonitorEntity>() {
            @Override
            public void accept(PWMonitorEntity pwMonitorEntity) throws Exception {

                if (pwMonitorEntity != null) {
                    PWMonitoringFragment.this.mPwMonitorEntity = pwMonitorEntity;
                    setFormUiData(pwMonitorEntity);
                }
            }
        });
    }

    private  void setFormUiData(PWMonitorEntity pwMonitorEntity) {
        PwMonitoringFragmentBinding vb = getViewDataBinding();
        vb.benfAncCount.setSection(pwMonitorEntity.getAncCount());
        vb.benfAncDate.setDate(pwMonitorEntity.getLastAnc());
        vb.benfMamtaCdWeight.setText(pwMonitorEntity.getLastWeightInMamta());
        vb.benfLastcheckupdate.setDate(pwMonitorEntity.getLastWeightCheckDate());
        vb.benfCurrentWeight.setText(pwMonitorEntity.getCurrentWeight());

        Set<Integer> regScheme = new HashSet<>();

        if (pwMonitorEntity.getPmmvyInstallment() != null) {
            vb.benfPmmvvyCount.setSection(pwMonitorEntity.getPmmvyInstallment());
            regScheme.add(0);
        }
        if (pwMonitorEntity.getIgmpyInstallment() != null) {
            vb.benfIgmpyCount.setSection(pwMonitorEntity.getIgmpyInstallment());
            regScheme.add(1);
        }
        if (pwMonitorEntity.getJsyInstallment() != null) {
            vb.benfJsyCount.setSection(pwMonitorEntity.getJsyInstallment());
            regScheme.add(2);
        }
        if (pwMonitorEntity.getRajshriInstallment() != null) {
            vb.benfRajshriCount.setSection(pwMonitorEntity.getRajshriInstallment());
            regScheme.add(3);
        }

        if (regScheme.isEmpty()) {
            regScheme.add(4);
        }

        vb.benfRegisteredProgramme.setSelectedIds(regScheme);
    }


    private void save() {
        List<Pair<Boolean, View>> validateElement = new ArrayList<>();


        PwMonitoringFragmentBinding vb = getViewDataBinding();
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


        for (Pair<Boolean, View> viewPair : validateElement) {

            if (!viewPair.first) {
                View targetView = viewPair.second;
                targetView.getParent().requestChildFocus(targetView, targetView);
                return;
            }
        }

        PWMonitorEntity pwMonitorEntity ;
        if (mPwMonitorEntity==null)
            pwMonitorEntity=new PWMonitorEntity();
        else
            pwMonitorEntity=mPwMonitorEntity;

        pwMonitorEntity.setStage("PW");
        pwMonitorEntity.setSubStage(subStage);
        pwMonitorEntity.setPregnancyId(pregnancyId);
        pwMonitorEntity.setBeneficiaryId(beneficiaryId);
        pwMonitorEntity.setAncCount(vb.benfAncCount.getSelectedPos());
        pwMonitorEntity.setLastAnc(vb.benfAncDate.getDate());
        pwMonitorEntity.setLastWeightInMamta(vb.benfMamtaCdWeight.getMeasValue());
        pwMonitorEntity.setLastWeightCheckDate(vb.benfLastcheckupdate.getDate());
        pwMonitorEntity.setCurrentWeight(vb.benfCurrentWeight.getMeasValue());


        Set<Integer> data = vb.benfRegisteredProgramme.selectedIds();

        if (data.contains(0)) {
            pwMonitorEntity.setPmmvyInstallment(vb.benfPmmvvyCount.getSelectedPos());
        }

        if (data.contains(1)) {
            pwMonitorEntity.setIgmpyInstallment(vb.benfIgmpyCount.getSelectedPos());
        }

        if (data.contains(2)) {
            pwMonitorEntity.setJsyInstallment(vb.benfJsyCount.getSelectedPos());
        }

        if (data.contains(3)) {
            pwMonitorEntity.setRajshriInstallment(vb.benfRajshriCount.getSelectedPos());
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
                                true, FragmentUtils.TRANSITION_SLIDE_LEFT_RIGHT);

                        requireActivity().getSupportFragmentManager()
                                .beginTransaction()
                                .remove(PWMonitoringFragment.this)
                                .commit();
                    });
                });

    }


}
