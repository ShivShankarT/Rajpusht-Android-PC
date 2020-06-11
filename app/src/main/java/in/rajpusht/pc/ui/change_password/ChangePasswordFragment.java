package in.rajpusht.pc.ui.change_password;

import android.os.Bundle;
import android.util.Pair;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.library.baseAdapters.BR;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import javax.inject.Inject;

import in.rajpusht.pc.R;
import in.rajpusht.pc.ViewModelProviderFactory;
import in.rajpusht.pc.databinding.ChangePasswordFragmentBinding;
import in.rajpusht.pc.ui.base.BaseFragment;
import in.rajpusht.pc.ui.login.LoginViewModel;
import in.rajpusht.pc.utils.Event;

public class ChangePasswordFragment extends BaseFragment<ChangePasswordFragmentBinding, ChangePasswordViewModel> {
    @Inject
    ViewModelProviderFactory factory;


    public static ChangePasswordFragment newInstance() {
        return new ChangePasswordFragment();
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.change_password_fragment;
    }

    @Override
    public ChangePasswordViewModel getViewModel() {
        return new ViewModelProvider(this, factory).get(ChangePasswordViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ChangePasswordFragmentBinding viewDataBinding = getViewDataBinding();
        Toolbar toolbar = viewDataBinding.toolbarLy.toolbar;
        toolbar.setTitle(getResources().getString(R.string.change_pass));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().onBackPressed();
            }
        });
        getViewModel().progressDialog.observe(getViewLifecycleOwner(), new Observer<Event<Boolean>>() {
            @Override
            public void onChanged(Event<Boolean> booleanEvent) {
                Boolean isPro = booleanEvent.getContentIfNotHandled();
                if (isPro != null && isPro) {
                    showProgressDialog();
                } else {
                    dismissProgressDialog();
                }
            }
        });


        getViewModel().statusLiveData.observe(getViewLifecycleOwner(), new Observer<Event<Pair<Integer, String>>>() {
            @Override
            public void onChanged(Event<Pair<Integer, String>> pairEvent) {
                Pair<Integer, String> data = pairEvent.getContentIfNotHandled();
                getViewDataBinding().oldNewPassword.setError(null);
                getViewDataBinding().newPassword.setError(null);
                getViewDataBinding().confNewPassword.setError(null);
                if (data != null) {
                    if (data.first == 1) {
                        getViewDataBinding().oldNewPassword.setError(data.second);
                    } else if (data.first == 2) {
                        getViewDataBinding().newPassword.setError(data.second);
                    } else if (data.first == 3) {
                        getViewDataBinding().confNewPassword.setError(data.second);
                    } else if (data.first == 4) {
                        showMessage(data.second);
                        showAlertDialog(data.second, new Runnable() {
                            @Override
                            public void run() {
                                requireActivity().onBackPressed();
                            }
                        });
                    } else if (data.first == 5) {
                        getViewDataBinding().oldNewPassword.setError(data.second);
                    }
                }
            }
        });
    }


}
