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
import in.rajpusht.pc.ui.pregnancy_graph.PregnancyGraphFragment;
import in.rajpusht.pc.ui.registration.RegistrationFragment;
import in.rajpusht.pc.utils.AppDateTimeUtils;
import in.rajpusht.pc.utils.FragmentUtils;
import in.rajpusht.pc.utils.rx.SchedulerProvider;

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

    public static PWMonitoringFragment newInstance(long beneficiaryId, long pregnancyId, String subStage) {
        PWMonitoringFragment pwMonitoringFragment = new PWMonitoringFragment();
        Bundle args = new Bundle();
        args.putLong("id", pregnancyId);
        args.putLong("beneficiaryId", beneficiaryId);
        args.putString("subStage", subStage);
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
                FragmentUtils.replaceFragment((AppCompatActivity) requireActivity(),
                        RegistrationFragment.newInstance(beneficiaryId), R.id.fragment_container,
                        true, FragmentUtils.TRANSITION_SLIDE_LEFT_RIGHT);
            }
        });

        viewDataBinding.weightIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentUtils.replaceFragment((AppCompatActivity) requireActivity(),
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

    }

    private void setupUiData() {
        getViewDataBinding().benfName.setText(beneficiaryEntity.getName());
        getViewDataBinding().benfHusName.setText("w/o:"+beneficiaryEntity.getHusbandName());
        getViewDataBinding().benfName.setText(beneficiaryEntity.getName());
        getViewDataBinding().pctsId.setText("PCTS ID: "+beneficiaryEntity.getPctsId());
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

        PWMonitorEntity pwMonitorEntity = new PWMonitorEntity();
        pwMonitorEntity.setStage("PW");
        pwMonitorEntity.setSubStage(subStage);
        pwMonitorEntity.setPregnancyId(pregnancyId);
        pwMonitorEntity.setAncCount(vb.benfAncCount.getSelectedPos());
        pwMonitorEntity.setLastAnc(vb.benfAncDate.getDate());
        pwMonitorEntity.setLastWeightInMamta(Double.valueOf(vb.benfMamtaCdWeight.getText()));
        pwMonitorEntity.setLastWeightCheckDate(vb.benfLastcheckupdate.getDate());
        pwMonitorEntity.setCurrentWeight(Double.valueOf(vb.benfCurrentWeight.getText()));


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

        pwMonitorEntity.setCreatedAt(AppDateTimeUtils.convertServerTimeStampDate(new Date()));
        pwMonitorEntity.setUpdatedAt(AppDateTimeUtils.convertServerTimeStampDate(new Date()));

        List<String> collectedDataSubStage = new ArrayList<>(beneficiaryEntity.getCollectedDataSubStage());

        if (!collectedDataSubStage.contains(subStage)) {
            collectedDataSubStage.add(subStage);
            beneficiaryEntity.setCollectedDataSubStage(collectedDataSubStage);
        }

        dataRepository.updateBeneficiary(beneficiaryEntity)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe((d) -> {

                });

        dataRepository.insertPwMonitor(pwMonitorEntity)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(() -> {
                    showAlertDialog("Beneficiary Report Saved Successfully", () -> {
                        FragmentUtils.replaceFragment((AppCompatActivity) requireActivity(),
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
