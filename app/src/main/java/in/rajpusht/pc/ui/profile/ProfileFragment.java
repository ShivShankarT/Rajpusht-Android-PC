package in.rajpusht.pc.ui.profile;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import in.rajpusht.pc.BR;
import in.rajpusht.pc.R;
import in.rajpusht.pc.ViewModelProviderFactory;
import in.rajpusht.pc.data.local.db.entity.AssignedLocationEntity;
import in.rajpusht.pc.data.local.pref.AppPreferencesHelper;
import in.rajpusht.pc.databinding.FragmentProfileBinding;
import in.rajpusht.pc.ui.base.BaseFragment;
import in.rajpusht.pc.ui.benef_list.BeneficiaryFragment;
import in.rajpusht.pc.ui.home.HomeActivity;
import in.rajpusht.pc.ui.profile_edit.ProfileEditFragment;
import in.rajpusht.pc.utils.ContextWrapper;
import in.rajpusht.pc.utils.ExpandableRecyclerAdapter;
import in.rajpusht.pc.utils.FragmentUtils;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends BaseFragment<FragmentProfileBinding, ProfileViewModel> implements AwcLocationAdapter.AWCChangeLister {

    @Inject
    ViewModelProviderFactory factory;
    @Inject
    AppPreferencesHelper appPreferencesHelper;
    private ProfileViewModel profileViewModel;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_profile;
    }


    @Override
    public ProfileViewModel getViewModel() {
        profileViewModel = new ViewModelProvider(this, factory).get(ProfileViewModel.class);
        return profileViewModel;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().onBackPressed();
            }
        });


        RecyclerView recyclerView = view.findViewById(R.id.list);
        AwcLocationAdapter awcLocationAdapter = new AwcLocationAdapter(requireContext(), this);
        awcLocationAdapter.setSelectedAWC(appPreferencesHelper.getSelectedAwcCode());
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(awcLocationAdapter);

        view.findViewById(R.id.edit_btn).setOnClickListener(v -> {
            FragmentUtils.replaceFragment(requireActivity(), new ProfileEditFragment(), R.id.fragment_container, true, false, FragmentUtils.TRANSITION_SLIDE_LEFT_RIGHT);
        });

        profileViewModel.assignedLocation.observe(getViewLifecycleOwner(), new Observer<List<AssignedLocationEntity>>() {
            @Override
            public void onChanged(List<AssignedLocationEntity> assignedLocationEntities) {
                awcLocationAdapter.setItems(convert(assignedLocationEntities));
            }
        });
        getViewDataBinding().profileName.setText(appPreferencesHelper.getCurrentUserName());
        getViewDataBinding().profileEmail.setText(appPreferencesHelper.getCurrentUserEmail());


        if (appPreferencesHelper.isEnglish()) {
            getViewDataBinding().english.setChecked(true);
        } else {
            getViewDataBinding().hindi.setChecked(true);
        }

        getViewDataBinding().languageRadiogroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.english) {
                appPreferencesHelper.setLanguage(true);
            } else if (checkedId == R.id.hindi) {
                appPreferencesHelper.setLanguage(false);
            }

            ContextWrapper.setLocale(requireActivity());
        });

        getViewDataBinding().saveBtn.setOnClickListener(v -> {
            FragmentUtils.replaceFragment(requireActivity(), new BeneficiaryFragment(),
                    R.id.fragment_container, false, true, FragmentUtils.TRANSITION_NONE);

        });
        getViewDataBinding().logout.setOnClickListener(v -> {
            ((HomeActivity)requireActivity()).syncData();
        });

    }


    private List<AssignedLocationListItem> convert(List<AssignedLocationEntity> assignedLocationEntities) {
        HashMap<String, List<AssignedLocationEntity>> map = new HashMap<>();

        for (AssignedLocationEntity assignedLocationEntity : assignedLocationEntities) {
            List<AssignedLocationEntity> list = map.get(assignedLocationEntity.getSectorName());
            if (list == null)
                list = new ArrayList<>();
            list.add(assignedLocationEntity);
            map.put(assignedLocationEntity.getSectorName(), list);

        }

        List<AssignedLocationListItem> assignedLocationListItems = new ArrayList<>();


        for (Map.Entry<String, List<AssignedLocationEntity>> data : map.entrySet()) {
            assignedLocationListItems.add(new AssignedLocationListItem(data.getKey()));
            for (AssignedLocationEntity entity : data.getValue())
                assignedLocationListItems.add(new AssignedLocationListItem(entity));
        }


        return assignedLocationListItems;
    }

    @Override
    public void onAwcChange(String awcCode, String awcName) {
        appPreferencesHelper.setAwcCode(awcCode);
        appPreferencesHelper.putString("awc_name", awcName);
        ((HomeActivity)requireActivity()).setNavUiData();

    }

    public static class AssignedLocationListItem extends ExpandableRecyclerAdapter.ListItem {
        AssignedLocationEntity assignedLocationEntity;

        public AssignedLocationListItem(AssignedLocationEntity assignedLocationEntity) {
            super(1211);
            this.assignedLocationEntity = assignedLocationEntity;

        }

        public AssignedLocationListItem(String name) {
            super(ExpandableRecyclerAdapter.TYPE_HEADER);
            super.title = name;

        }

    }


}
