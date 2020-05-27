package in.rajpusht.pc.ui.pw_monitoring;

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
import in.rajpusht.pc.data.local.db.entity.PWMonitorEntity;
import in.rajpusht.pc.databinding.PwMonitoringFragmentBinding;
import in.rajpusht.pc.ui.base.BaseFragment;
import in.rajpusht.pc.utils.rx.SchedulerProvider;

public class PWMonitoringFragment extends BaseFragment<PwMonitoringFragmentBinding, PWMonitoringViewModel> {

    @Inject
    ViewModelProviderFactory factory;
    @Inject
    DataRepository dataRepository;
    @Inject
    SchedulerProvider schedulerProvider;
    private PWMonitoringViewModel mViewModel;
    private long pregnancyId;

    public static PWMonitoringFragment newInstance(long pregnancyId) {
        PWMonitoringFragment pwMonitoringFragment = new PWMonitoringFragment();
        Bundle args = new Bundle();
        args.putLong("id", pregnancyId);
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

    void save() {
        PwMonitoringFragmentBinding vb = getViewDataBinding();
        vb.benfAncCount.validate();
        vb.benfAncDate.validate();
        vb.benfMamtaCdWeight.validate();
        vb.benfLastcheckupdate.validate();
        vb.benfCurrentWeight.validate();
        vb.benfRegisteredProgramme.validate();
        vb.benfPmmvvyCount.validate();
        vb.benfIgmpyCount.validate();
        vb.benfJsyCount.validate();
        vb.benfRajshriCount.validate();

        PWMonitorEntity pwMonitorEntity = new PWMonitorEntity();

        pwMonitorEntity.setPregnancyId(pregnancyId);
        pwMonitorEntity.setAncCount(vb.benfAncCount.getSelectedPos());
        pwMonitorEntity.setLastAnc(vb.benfAncDate.getDate());
        pwMonitorEntity.setLastWeightInMamta(Double.valueOf(vb.benfMamtaCdWeight.getText()));
        pwMonitorEntity.setLastWeightCheckDate(vb.benfLastcheckupdate.getDate());
        pwMonitorEntity.setCurrentWeight(Double.valueOf(vb.benfCurrentWeight.getText()));


        Set<Integer> data = vb.benfRegisteredProgramme.selectedIds();

        if (data.contains(0)) {
            pwMonitorEntity.setPmmvyInstallmentCt(vb.benfPmmvvyCount.getSelectedPos());
        }

        if (data.contains(1)) {
            pwMonitorEntity.setIgmpyInstallmentCt(vb.benfIgmpyCount.getSelectedPos());
        }

        if (data.contains(2)) {
            pwMonitorEntity.setJsyInstallmentCt(vb.benfJsyCount.getSelectedPos());
        }

        if (data.contains(3)) {
            pwMonitorEntity.setRajshriInstallmentCt(vb.benfRajshriCount.getSelectedPos());
        }

        dataRepository.insertPwMonitor(pwMonitorEntity)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui()).subscribe(() -> {
            showAlertDialog("Beneficiary Created Saved Successfully", () -> {
                requireActivity().onBackPressed();
            });
        });

    }
}
