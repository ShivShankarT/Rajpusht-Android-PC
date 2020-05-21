package in.rajpusht.pc.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import in.rajpusht.pc.R;
import in.rajpusht.pc.data.local.entity.AssignedLocationEntity;
import in.rajpusht.pc.ui.profile_edit.ProfileEditFragment;
import in.rajpusht.pc.utils.ExpandableRecyclerAdapter;
import in.rajpusht.pc.utils.FragmentUtils;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
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
        AwcLocationAdapter awcLocationAdapter = new AwcLocationAdapter(requireContext());
        List<AssignedLocationListItem> assignedLocationListItems = getstaicdata();


        awcLocationAdapter.setItems(assignedLocationListItems);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(awcLocationAdapter);

        view.findViewById(R.id.edit_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentUtils.replaceFragment((AppCompatActivity) requireActivity(), new ProfileEditFragment(), R.id.fragment_container, true, FragmentUtils.TRANSITION_SLIDE_LEFT_RIGHT);

            }
        });

    }


    private List<AssignedLocationListItem> getstaicdata() {
        List<AssignedLocationListItem> assignedLocationListItems = new ArrayList<>();
        assignedLocationListItems.add(new AssignedLocationListItem("Bengalore"));
        AssignedLocationEntity ss = new AssignedLocationEntity();
        ss.setAwcEng("Bhavani II");
        ss.setSectorName("Bengalore");
        assignedLocationListItems.add(new AssignedLocationListItem(ss));
        ss = new AssignedLocationEntity();
        ss.setAwcEng("Chitur");
        ss.setSectorName("Bengalore");
        assignedLocationListItems.add(new AssignedLocationListItem(ss));


        assignedLocationListItems.add(new AssignedLocationListItem("Sepakam"));
        ss = new AssignedLocationEntity();
        ss.setAwcEng("Siluv");
        ss.setSectorName("Sepakam");
        assignedLocationListItems.add(new AssignedLocationListItem(ss));
        ss = new AssignedLocationEntity();
        ss.setAwcEng("Madur");
        ss.setSectorName("Sepakam");
        assignedLocationListItems.add(new AssignedLocationListItem(ss));


        return assignedLocationListItems;
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
