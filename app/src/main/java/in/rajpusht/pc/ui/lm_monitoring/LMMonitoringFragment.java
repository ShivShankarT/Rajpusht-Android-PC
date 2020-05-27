package in.rajpusht.pc.ui.lm_monitoring;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.library.baseAdapters.BR;
import androidx.lifecycle.ViewModelProvider;

import java.util.Set;

import javax.inject.Inject;

import in.rajpusht.pc.R;
import in.rajpusht.pc.ViewModelProviderFactory;
import in.rajpusht.pc.custom.callback.HValueChangedListener;
import in.rajpusht.pc.data.DataRepository;
import in.rajpusht.pc.data.local.db.entity.LMMonitorEntity;
import in.rajpusht.pc.databinding.LmMonitoringFragmentBinding;
import in.rajpusht.pc.ui.base.BaseFragment;
import in.rajpusht.pc.utils.rx.SchedulerProvider;

public class LMMonitoringFragment extends BaseFragment<LmMonitoringFragmentBinding, LMMonitoringViewModel> {

    @Inject
    ViewModelProviderFactory factory;
    @Inject
    DataRepository dataRepository;
    @Inject
    SchedulerProvider schedulerProvider;
    private long childId;
    private LMMonitoringViewModel mViewModel;

    public static LMMonitoringFragment newInstance(long childId) {
        LMMonitoringFragment lmMonitoringFragment = new LMMonitoringFragment();
        Bundle args = new Bundle();
        args.putLong("id", childId);
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


    }

    private void save() {
        LmMonitoringFragmentBinding vb = getViewDataBinding();


        vb.benfChildImmune.validate();
        vb.benfChildLastRecMuac.validate();
        vb.benfChildLastRecMuacDate.validate();
        vb.benfChildCurrentMuac.validate();
        vb.benfBirthChildWeight.validate();
        vb.benfChildCurrentMuac.validate();
        vb.benfCurrentWeight.validate();


        vb.benfRegisteredProgramme.validate();
        vb.benfPmmvvyCount.validate();
        vb.benfIgmpyCount.validate();
        vb.benfJsyCount.validate();
        vb.benfRajshriCount.validate();

        LMMonitorEntity lmMonitorEntity = new LMMonitorEntity();
        lmMonitorEntity.setChildId(childId);
        lmMonitorEntity.setIsFirstImmunizationComplete(vb.benfChildImmune.getSelectedData());
        lmMonitorEntity.setLastMuac(Double.valueOf(vb.benfChildLastRecMuac.getText()));
        lmMonitorEntity.setLastMuacCheckDate(vb.benfChildLastRecMuacDate.getDate());
        lmMonitorEntity.setCurrentMuac(Double.valueOf(vb.benfChildCurrentMuac.getText()));
        lmMonitorEntity.setBirthWeight(Double.valueOf(vb.benfBirthChildWeight.getText()));
        lmMonitorEntity.setChildWeight(Double.valueOf(vb.benfCurrentWeight.getText()));

        Set<Integer> data = vb.benfRegisteredProgramme.selectedIds();

        if (data.contains(0)) {
            lmMonitorEntity.setPmmvyInstallmentCt(vb.benfPmmvvyCount.getSelectedPos());
        }

        if (data.contains(1)) {
            lmMonitorEntity.setIgmpyInstallmentCt(vb.benfIgmpyCount.getSelectedPos());
        }

        if (data.contains(2)) {
            lmMonitorEntity.setJsyInstallmentCt(vb.benfJsyCount.getSelectedPos());
        }

        if (data.contains(3)) {
            lmMonitorEntity.setRajshriInstallmentCt(vb.benfRajshriCount.getSelectedPos());
        }

        dataRepository.insertLmMonitor(lmMonitorEntity)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui()).subscribe(() -> {
            showAlertDialog("Beneficiary Created Saved Successfully", () -> {
                requireActivity().onBackPressed();
            });
        });

    }

}
