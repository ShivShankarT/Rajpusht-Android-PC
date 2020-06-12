package in.rajpusht.pc.ui.sync;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import javax.inject.Inject;

import in.rajpusht.pc.BR;
import in.rajpusht.pc.R;
import in.rajpusht.pc.ViewModelProviderFactory;
import in.rajpusht.pc.databinding.SyncFragmentBinding;
import in.rajpusht.pc.ui.base.BaseFragment;
import in.rajpusht.pc.ui.home.HomeActivity;

public class SyncFragment extends BaseFragment<SyncFragmentBinding, SyncViewModel> {

    @Inject
    ViewModelProviderFactory factory;

    public static SyncFragment newInstance() {
        return new SyncFragment();
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.sync_fragment;
    }

    @Override
    public SyncViewModel getViewModel() {
        return new ViewModelProvider(this, factory).get(SyncViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SyncFragmentBinding viewDataBinding = getViewDataBinding();
        Toolbar toolbar = viewDataBinding.toolbarLy.toolbar;
        toolbar.setTitle("AWC Sync Details");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().onBackPressed();
            }
        });
        AWCSyncAdapter awcSyncAdapter = new AWCSyncAdapter();
        RecyclerView list = getViewDataBinding().list;
        list.addItemDecoration(new DividerItemDecoration(requireContext(), RecyclerView.VERTICAL));
        list.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false));
        list.setAdapter(awcSyncAdapter);
        getViewModel().getData();
        getViewModel().syncLiveData.observe(getViewLifecycleOwner(), awcSyncCounts -> {
            if (awcSyncCounts.isEmpty()) {
                list.setVisibility(View.GONE);
                getViewDataBinding().noData.setVisibility(View.VISIBLE);
                getViewDataBinding().uploadBtn.setVisibility(View.GONE);
            } else {
                list.setVisibility(View.VISIBLE);
                getViewDataBinding().uploadBtn.setVisibility(View.VISIBLE);
                getViewDataBinding().noData.setVisibility(View.GONE);

            }
            awcSyncAdapter.setAwcSyncCounts(awcSyncCounts);
            awcSyncAdapter.notifyDataSetChanged();
        });
        getViewDataBinding().uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeActivity homeActivity = (HomeActivity) requireActivity();
                homeActivity.syncData();
            }
        });
    }
}
