package in.rajpusht.pc.ui.benef_list;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.library.baseAdapters.BR;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import javax.inject.Inject;

import in.rajpusht.pc.R;
import in.rajpusht.pc.custom.utils.HUtil;
import in.rajpusht.pc.data.DataRepository;
import in.rajpusht.pc.data.local.db.entity.BeneficiaryEntity;
import in.rajpusht.pc.data.local.db.entity.ChildEntity;
import in.rajpusht.pc.data.local.db.entity.PregnantEntity;
import in.rajpusht.pc.databinding.FragmentBeneficiaryBinding;
import in.rajpusht.pc.model.BefModel;
import in.rajpusht.pc.model.Tuple;
import in.rajpusht.pc.ui.base.BaseFragment;
import in.rajpusht.pc.ui.home.HomeActivity;
import in.rajpusht.pc.ui.lm_monitoring.LMMonitoringFragment;
import in.rajpusht.pc.ui.profile.ProfileFragment;
import in.rajpusht.pc.ui.pw_monitoring.PWMonitoringFragment;
import in.rajpusht.pc.ui.registration.RegistrationFragment;
import in.rajpusht.pc.utils.BottomDialogFragment;
import in.rajpusht.pc.utils.FragmentUtils;
import in.rajpusht.pc.utils.rx.SchedulerProvider;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;


public class BeneficiaryFragment extends BaseFragment<FragmentBeneficiaryBinding, BeneficiaryViewModel> implements BeneficiaryAdapter.OnListFragmentInteractionListener {


    @Inject
    DataRepository dataRepository;
    @Inject
    SchedulerProvider schedulerProvider;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public BeneficiaryFragment() {
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_beneficiary;
    }

    @Override
    public BeneficiaryViewModel getViewModel() {
        return null;
    }


    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Toolbar toolbar = getViewDataBinding().toolbarLy.toolbar;
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HomeActivity) requireActivity()).openDrawer();
            }
        });
        toolbar.getMenu().add(1, 1, 1, "info").setIcon(R.drawable.ic_info_outline).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        toolbar.getMenu().add(1, 2, 2, "Insert Dummy").setIcon(R.drawable.ic_info_outline).setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == 1)
                    showColorInfo();
                else if (item.getItemId() == 2) {
                    Tuple<BeneficiaryEntity, PregnantEntity, ChildEntity> tuple = HUtil.staticData();
                    dataRepository.insertOrUpdateBeneficiaryData(tuple.getT1(), tuple.getT3(), tuple.getT2())
                            .subscribeOn(schedulerProvider.io())
                            .observeOn(schedulerProvider.ui())
                            .subscribe(aBoolean -> {
                                if (!aBoolean.isEmpty()) {//todo check
                                    showAlertDialog("Beneficiary Dummy Data  Added", () -> {
                                        FragmentUtils.replaceFragment(requireActivity(), new BeneficiaryFragment(), R.id.fragment_container, false, FragmentUtils.TRANSITION_NONE);
                                    });
                                }

                            });
                }
                return true;
            }
        });


        // dataRepository.bulkdownloadTest();

        RecyclerView recyclerView = getViewDataBinding().list;
        Context context = view.getContext();
        recyclerView.setLayoutManager(new LinearLayoutManager(context));


        List<BefModel> list = dataRepository.getBefModels();
        if (list.isEmpty())
            getViewDataBinding().noData.setVisibility(View.VISIBLE);
        else
            getViewDataBinding().noData.setVisibility(View.GONE);

        recyclerView.setAdapter(new BeneficiaryAdapter(list, this));
        getViewDataBinding().addFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentUtils.replaceFragment(requireActivity(), new RegistrationFragment(), R.id.fragment_container, true, FragmentUtils.TRANSITION_NONE);

            }
        });
        if (TextUtils.isEmpty(dataRepository.getSelectedAwcCode())) {
            FragmentUtils.replaceFragment(requireActivity(), new ProfileFragment(), R.id.fragment_container, true, FragmentUtils.TRANSITION_NONE);

        }

        if (false)
            dataRepository.profileAndBulkDownload()
                    .subscribeOn(schedulerProvider.io()).subscribe(new Action() {
                @Override
                public void run() throws Exception {

                }
            }, new Consumer<Throwable>() {
                @Override
                public void accept(Throwable throwable) throws Exception {

                }
            });
    }

    private void showColorInfo() {

        BottomDialogFragment.newInstance().show(getChildFragmentManager(), "ss");
    }

    @Override
    public void onListFragmentInteraction(BefModel item) {
        if (item.getStage().equals("PW")) {
            FragmentUtils.replaceFragment(requireActivity(), PWMonitoringFragment.newInstance(item.getBeneficiaryId(), item.getPregnancyId(), item.getCurrentSubStage(), item.getPwFormId()), R.id.fragment_container, true, FragmentUtils.TRANSITION_NONE);

        } else {
            FragmentUtils.replaceFragment(requireActivity(), LMMonitoringFragment.newInstance(item.getBeneficiaryId(), item.getMotherId(), item.getCurrentSubStage(), item.getLmFormId()), R.id.fragment_container, true, FragmentUtils.TRANSITION_NONE);

        }

    }
}
