package in.rajpusht.pc.ui.lm_monitoring;

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
import in.rajpusht.pc.data.local.db.entity.LMMonitorEntity;
import in.rajpusht.pc.databinding.LmMonitoringFragmentBinding;
import in.rajpusht.pc.ui.animation.CounsellingAnimationFragment;
import in.rajpusht.pc.ui.base.BaseFragment;
import in.rajpusht.pc.utils.AppDateTimeUtils;
import in.rajpusht.pc.utils.FragmentUtils;
import in.rajpusht.pc.utils.rx.SchedulerProvider;
import io.reactivex.functions.Consumer;

public class LMMonitoringFragment extends BaseFragment<LmMonitoringFragmentBinding, LMMonitoringViewModel> {

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

    public static LMMonitoringFragment newInstance(long childId, String subStage, Long lmFormId) {
        LMMonitoringFragment lmMonitoringFragment = new LMMonitoringFragment();
        Bundle args = new Bundle();
        args.putLong("id", childId);
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
        subStage = getArguments().getString("subStage");
        lmFormId = (Long) getArguments().getSerializable("lmFormId");
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        LmMonitoringFragmentBinding viewDataBinding = getViewDataBinding();
        Toolbar toolbar = viewDataBinding.toolbarLy.toolbar;
        toolbar.setTitle("LM Monitoring");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().onBackPressed();
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
        if (lmFormId != null)
            fetchFormUiData();


    }

    private void fetchFormUiData() {

        dataRepository.lmMonitorById(lmFormId)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui()).subscribe(new Consumer<LMMonitorEntity>() {
            @Override
            public void accept(LMMonitorEntity lmMonitorEntity) throws Exception {

                if (lmMonitorEntity != null) {
                    LMMonitoringFragment.this.mLmMonitorEntity = lmMonitorEntity;
                    setFormUiData(lmMonitorEntity);
                }
            }
        });
    }

    private void setFormUiData(LMMonitorEntity lmMonitorEntity) {

        LmMonitoringFragmentBinding vb = getViewDataBinding();
        vb.benfChildImmune.setSectionByData(lmMonitorEntity.getIsFirstImmunizationComplete());
        vb.benfChildLastRecMuac.setText(String.valueOf(lmMonitorEntity.getLastMuac()));
        vb.benfChildLastRecMuacDate.setDate(lmMonitorEntity.getLastMuacCheckDate());
        vb.benfChildCurrentMuac.setText(String.valueOf(lmMonitorEntity.getCurrentMuac()));
        vb.benfBirthChildWeight.setText(String.valueOf(lmMonitorEntity.getBirthWeight()));
        vb.benfCurrentWeight.setText(String.valueOf(lmMonitorEntity.getChildWeight()));

        Set<Integer> regScheme = new HashSet<>();

        if (lmMonitorEntity.getPmmvyInstallment() != null) {
            vb.benfPmmvvyCount.setSection(lmMonitorEntity.getPmmvyInstallment());
            regScheme.add(0);
        }
        if (lmMonitorEntity.getIgmpyInstallment() != null) {
            vb.benfIgmpyCount.setSection(lmMonitorEntity.getIgmpyInstallment());
            regScheme.add(1);
        }
        if (lmMonitorEntity.getJsyInstallment() != null) {
            vb.benfJsyCount.setSection(lmMonitorEntity.getJsyInstallment());
            regScheme.add(2);
        }
        if (lmMonitorEntity.getRajshriInstallment() != null) {
            vb.benfRajshriCount.setSection(lmMonitorEntity.getRajshriInstallment());
            regScheme.add(3);
        }

        if (regScheme.isEmpty()) {
            regScheme.add(4);
        }

        vb.benfRegisteredProgramme.setSelectedIds(regScheme);
    }

    private void save() {
        LmMonitoringFragmentBinding vb = getViewDataBinding();
        List<Pair<Boolean, View>> validateElement = new ArrayList<>();

        validateElement.add(vb.benfChildImmune.validateWthView());
        validateElement.add(vb.benfChildLastRecMuac.validateWthView());
        validateElement.add(vb.benfChildLastRecMuacDate.validateWthView());
        validateElement.add(vb.benfChildCurrentMuac.validateWthView());
        validateElement.add(vb.benfBirthChildWeight.validateWthView());
        validateElement.add(vb.benfChildCurrentMuac.validateWthView());
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

        LMMonitorEntity lmMonitorEntity;
        if (mLmMonitorEntity == null)
            lmMonitorEntity = new LMMonitorEntity();
        else
            lmMonitorEntity = mLmMonitorEntity;

        lmMonitorEntity.setChildId(childId);
        lmMonitorEntity.setStage("LM");
        lmMonitorEntity.setSubStage(subStage);
        lmMonitorEntity.setIsFirstImmunizationComplete(vb.benfChildImmune.getSelectedData());
        lmMonitorEntity.setLastMuac(Double.valueOf(vb.benfChildLastRecMuac.getText()));
        lmMonitorEntity.setLastMuacCheckDate(vb.benfChildLastRecMuacDate.getDate());
        lmMonitorEntity.setCurrentMuac(Double.valueOf(vb.benfChildCurrentMuac.getText()));
        lmMonitorEntity.setBirthWeight(Double.valueOf(vb.benfBirthChildWeight.getText()));
        lmMonitorEntity.setChildWeight(Double.valueOf(vb.benfCurrentWeight.getText()));

        Set<Integer> data = vb.benfRegisteredProgramme.selectedIds();

        if (data.contains(0)) {
            lmMonitorEntity.setPmmvyInstallment(vb.benfPmmvvyCount.getSelectedPos());
        }

        if (data.contains(1)) {
            lmMonitorEntity.setIgmpyInstallment(vb.benfIgmpyCount.getSelectedPos());
        }

        if (data.contains(2)) {
            lmMonitorEntity.setJsyInstallment(vb.benfJsyCount.getSelectedPos());
        }

        if (data.contains(3)) {
            lmMonitorEntity.setRajshriInstallment(vb.benfRajshriCount.getSelectedPos());
        }

        lmMonitorEntity.setCreatedAt(AppDateTimeUtils.convertServerTimeStampDate(new Date()));
        lmMonitorEntity.setUpdatedAt(AppDateTimeUtils.convertServerTimeStampDate(new Date()));

        dataRepository.insertOrUpdateLmMonitor(lmMonitorEntity)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui()).subscribe(() -> {
            showAlertDialog("Beneficiary Child Report Saved Successfully", () -> {
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .remove(LMMonitoringFragment.this)
                        .commit();

                FragmentUtils.replaceFragment((AppCompatActivity) requireActivity(),
                        CounsellingAnimationFragment.newInstance(0), R.id.fragment_container,
                        true, FragmentUtils.TRANSITION_SLIDE_LEFT_RIGHT);

            });
        });

    }

}
