package in.rajpusht.pc.ui.benef_list;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.library.baseAdapters.BR;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import in.rajpusht.pc.R;
import in.rajpusht.pc.data.local.db.entity.BeneficiaryEntity;
import in.rajpusht.pc.databinding.FragmentBeneficiaryBinding;
import in.rajpusht.pc.ui.base.BaseFragment;
import in.rajpusht.pc.ui.home.HomeActivity;
import in.rajpusht.pc.ui.lm_monitoring.LMMonitoringFragment;
import in.rajpusht.pc.ui.pw_monitoring.PWMonitoringFragment;
import in.rajpusht.pc.ui.registration.RegistrationFragment;
import in.rajpusht.pc.utils.BottomDialogFragment;
import in.rajpusht.pc.utils.FragmentUtils;


public class BeneficiaryFragment extends BaseFragment<FragmentBeneficiaryBinding, BeneficiaryViewModel> implements BeneficiaryAdapter.OnListFragmentInteractionListener {


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
        toolbar.getMenu().add("info").setIcon(R.drawable.ic_info_outline).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                showColorInfo();
                return true;
            }
        });


        RecyclerView recyclerView = getViewDataBinding().list;
        Context context = view.getContext();
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        List<BeneficiaryEntity> list = new ArrayList<>();
        BeneficiaryEntity beneficiaryEntity = new BeneficiaryEntity();
        beneficiaryEntity.setName("Mala");
        beneficiaryEntity.setHusbandName("Ravi");
        beneficiaryEntity.setLmpDate("20-05-2020");
        beneficiaryEntity.setStage("PW");
        beneficiaryEntity.setSubStage("PW3");
        beneficiaryEntity.setCurrentSubStage("PW4");
        list.add(beneficiaryEntity);

        beneficiaryEntity = new BeneficiaryEntity();
        beneficiaryEntity.setName("Nayan");
        beneficiaryEntity.setHusbandName("Maveen");
        beneficiaryEntity.setLmpDate("20-05-2020");
        beneficiaryEntity.setStage("LM");
        beneficiaryEntity.setSubStage("LM1");
        beneficiaryEntity.setCurrentSubStage("LM2");
        list.add(beneficiaryEntity);

        beneficiaryEntity = new BeneficiaryEntity();
        beneficiaryEntity.setName("Narmaha");
        beneficiaryEntity.setHusbandName("Bala");
        beneficiaryEntity.setLmpDate("20-05-2020");
        beneficiaryEntity.setStage("LM");
        beneficiaryEntity.setSubStage("LM1");
        beneficiaryEntity.setCurrentSubStage("LM1");
        list.add(beneficiaryEntity);

        beneficiaryEntity = new BeneficiaryEntity();
        beneficiaryEntity.setName("Sehla");
        beneficiaryEntity.setHusbandName("Kala");
        beneficiaryEntity.setLmpDate("20-05-2020");
        beneficiaryEntity.setStage("PW");
        beneficiaryEntity.setSubStage("PW2");
        beneficiaryEntity.setCurrentSubStage("PW2");
        list.add(beneficiaryEntity);



        recyclerView.setAdapter(new BeneficiaryAdapter(list, this));
        getViewDataBinding().addFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentUtils.replaceFragment((AppCompatActivity) requireActivity(), new RegistrationFragment(), R.id.fragment_container, true, FragmentUtils.TRANSITION_NONE);

            }
        });
    }

    private void showColorInfo() {

        BottomDialogFragment.newInstance().show(getChildFragmentManager(),"ss");
    }

    @Override
    public void onListFragmentInteraction(BeneficiaryEntity item) {
        if (item.getStage().equals("PW")) {
            FragmentUtils.replaceFragment((AppCompatActivity) requireActivity(), new PWMonitoringFragment(), R.id.fragment_container, true, FragmentUtils.TRANSITION_NONE);

        } else {
            FragmentUtils.replaceFragment((AppCompatActivity) requireActivity(), new LMMonitoringFragment(), R.id.fragment_container, true, FragmentUtils.TRANSITION_NONE);

        }

    }
}
