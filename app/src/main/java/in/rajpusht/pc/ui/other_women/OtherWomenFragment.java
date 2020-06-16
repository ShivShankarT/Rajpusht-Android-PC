package in.rajpusht.pc.ui.other_women;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import in.rajpusht.pc.BR;
import in.rajpusht.pc.R;
import in.rajpusht.pc.ViewModelProviderFactory;
import in.rajpusht.pc.data.DataRepository;
import in.rajpusht.pc.databinding.OtherWomenFragmentBinding;
import in.rajpusht.pc.model.BefModel;
import in.rajpusht.pc.ui.base.BaseFragment;
import in.rajpusht.pc.ui.benef_list.BeneficiaryAdapter;
import in.rajpusht.pc.utils.rx.SchedulerProvider;
import io.reactivex.Single;
import io.reactivex.functions.BiConsumer;
import io.reactivex.functions.Consumer;

public class OtherWomenFragment extends BaseFragment<OtherWomenFragmentBinding, OtherWomenViewModel> implements BeneficiaryAdapter.OnListFragmentInteractionListener {
    @Inject
    ViewModelProviderFactory factory;
    @Inject
    SchedulerProvider schedulerProvider;
    @Inject
    DataRepository dataRepository;


    public static OtherWomenFragment newInstance() {
        return new OtherWomenFragment();
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.other_women_fragment;
    }

    @Override
    public OtherWomenViewModel getViewModel() {

        return new ViewModelProvider(this, factory).get(OtherWomenViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        OtherWomenFragmentBinding viewDataBinding = getViewDataBinding();
        Toolbar toolbar = viewDataBinding.toolbarLy.toolbar;
        toolbar.setTitle(getResources().getString(R.string.nav_other_women_list));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().onBackPressed();
            }
        });

        RecyclerView recyclerView = getViewDataBinding().list;
        Context context = view.getContext();
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        BeneficiaryAdapter adapter = new BeneficiaryAdapter(Collections.emptyList(), this);
        adapter.setOtherWomen(true);
        recyclerView.setAdapter(adapter);
        getViewModel().otherWomenBefModels().observe(getViewLifecycleOwner(), new Observer<List<BefModel>>() {
            @Override
            public void onChanged(List<BefModel> befModels) {
                adapter.setValues(befModels);
                if (befModels.isEmpty()){
                    viewDataBinding.noData.setVisibility(View.VISIBLE);
                }else {
                    viewDataBinding.noData.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void onListFragmentInteraction(BefModel item) {

        if ("MM".equals(item.getNaReason()) || "CM".equals(item.getNaReason()) || "BM".equals(item.getNaReason()) || "WM".equals(item.getNaReason())) {
            new MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Alert")
                    .setMessage("Do you want to track?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @SuppressLint("CheckResult")
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            if (item.getMotherId() != 0) {
                                Single<Boolean> mother = dataRepository.getBeneficiary(item.getMotherId()).flatMap(beneficiaryEntity -> {
                                    beneficiaryEntity.setIsActive("Y");
                                    return dataRepository.insertOrUpdateBeneficiary(beneficiaryEntity).first(true);
                                });

                                Single<Boolean> child = dataRepository.getChild(item.getBeneficiaryId()).flatMap(childEntity -> {
                                    childEntity.setIsActive("Y");
                                    return dataRepository.insertOrUpdateChild(childEntity).firstElement();
                                }).toSingle(true);

                                Single.merge(mother, child)
                                        .subscribeOn(schedulerProvider.io())
                                        .observeOn(schedulerProvider.ui())
                                        .subscribe(new Consumer<Boolean>() {
                                            @Override
                                            public void accept(Boolean aBoolean) throws Exception {
                                                showMessage("Updated");
                                            }
                                        });


                            } else {
                                dataRepository.getBeneficiary(item.getBeneficiaryId()).flatMap(beneficiaryEntity -> {
                                    beneficiaryEntity.setIsActive("Y");
                                    return dataRepository.insertOrUpdateBeneficiary(beneficiaryEntity).first(true);
                                }).subscribeOn(schedulerProvider.io())
                                        .observeOn(schedulerProvider.ui())
                                        .subscribe(new BiConsumer<Boolean, Throwable>() {
                                            @Override
                                            public void accept(Boolean aBoolean, Throwable throwable) throws Exception {
                                                showMessage("Updated");
                                            }
                                        });
                            }


                        }
                    })
                    .setNegativeButton("NO", null)
                    .show();

        }
    }
}
