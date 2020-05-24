package in.rajpusht.pc.ui.change_password;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.library.baseAdapters.BR;

import in.rajpusht.pc.R;
import in.rajpusht.pc.databinding.ChangePasswordFragmentBinding;
import in.rajpusht.pc.ui.base.BaseFragment;

public class ChangePasswordFragment extends BaseFragment<ChangePasswordFragmentBinding, ChangePasswordViewModel> {

    private ChangePasswordViewModel mViewModel;

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
        return mViewModel;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ChangePasswordFragmentBinding viewDataBinding = getViewDataBinding();
        Toolbar toolbar = viewDataBinding.toolbarLy.toolbar;
        toolbar.setTitle("Change Password");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().onBackPressed();
            }
        });
    }


}
