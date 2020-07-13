package in.rajpusht.pc.ui.benef_list;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.library.baseAdapters.BR;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import in.rajpusht.pc.R;
import in.rajpusht.pc.data.DataRepository;
import in.rajpusht.pc.databinding.FragmentBeneficiaryBinding;
import in.rajpusht.pc.model.BefModel;
import in.rajpusht.pc.ui.base.BaseFragment;
import in.rajpusht.pc.ui.home.HomeActivity;
import in.rajpusht.pc.ui.lm_monitoring.LMMonitoringFragment;
import in.rajpusht.pc.ui.profile.ProfileFragment;
import in.rajpusht.pc.ui.pw_monitoring.PWMonitoringFragment;
import in.rajpusht.pc.ui.registration.RegistrationFragment;
import in.rajpusht.pc.utils.BottomDialogFragment;
import in.rajpusht.pc.utils.FragmentUtils;
import in.rajpusht.pc.utils.rx.SchedulerProvider;


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
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == 1)
                    showColorInfo();
                else if (item.getItemId() == 2) {

                }
                return true;
            }
        });


        // dataRepository.bulkdownloadTest();

        RecyclerView recyclerView = getViewDataBinding().list;
        Context context = view.getContext();
        recyclerView.setLayoutManager(new LinearLayoutManager(context));


        LiveData<List<BefModel>> list = dataRepository.getBefModels();
        BeneficiaryAdapter adapter = new BeneficiaryAdapter(Collections.emptyList(), this);
        list.observe(getViewLifecycleOwner(), befModels -> {
            if (befModels.isEmpty()) {
                getViewDataBinding().noData.setVisibility(View.VISIBLE);
                adapter.setValues(befModels);
            } else {
                adapter.setValues(befModels);
                getViewDataBinding().noData.setVisibility(View.GONE);
            }
        });


        recyclerView.setAdapter(adapter);
        getViewDataBinding().addFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentUtils.replaceFragment(requireActivity(), new RegistrationFragment(), R.id.fragment_container, true, false, FragmentUtils.TRANSITION_NONE);

            }
        });
        if (TextUtils.isEmpty(dataRepository.getSelectedAwcCode())) {
            FragmentActivity activity = requireActivity();
            FragmentManager fragmentManager = activity.getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.fragment_container, new BeneficiaryFragment(), "BeneficiaryFragment");
            transaction.replace(R.id.fragment_container, new ProfileFragment(), "ProfileFragment")
                    .addToBackStack(null).commit();

            //FragmentUtils.replaceFragment(requireActivity(), new ProfileFragment(), R.id.fragment_container, false, true, FragmentUtils.TRANSITION_NONE);

        }

    }

    private void showColorInfo() {

        BottomDialogFragment.newInstance().show(getChildFragmentManager(), "ss");
    }

    @Override
    public void onListFragmentInteraction(BefModel item) {
        Log.i("ddddd", "onListFragmentInteraction: " + item);
        if (item.getCurrentSubStage().contains("PW")) {
            FragmentUtils.replaceFragment(requireActivity(), PWMonitoringFragment.newInstance(item.getBeneficiaryId(), item.getPregnancyId(), item.getCurrentSubStage(), item.getPwFormId()), R.id.fragment_container, true, false, FragmentUtils.TRANSITION_NONE);

        } else {
            FragmentUtils.replaceFragment(requireActivity(), LMMonitoringFragment.newInstance(item.getBeneficiaryId(), item.getMotherId(), item.getCurrentSubStage(), item.getLmFormId()), R.id.fragment_container, true, false, FragmentUtils.TRANSITION_NONE);

        }

    }
}
