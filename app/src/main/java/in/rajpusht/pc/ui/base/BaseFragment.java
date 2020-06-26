package in.rajpusht.pc.ui.base;


import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.CallSuper;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import dagger.android.support.AndroidSupportInjection;
import in.rajpusht.pc.R;
import in.rajpusht.pc.utils.MyProgressDialogFragment;


public abstract class BaseFragment<T extends ViewDataBinding, V extends BaseViewModel> extends Fragment {

   protected boolean forceExit=true;
    private BaseActivity mActivity;
    private View mRootView;
    private T mViewDataBinding;
    private V mViewModel;
    private MyProgressDialogFragment myDialogFragment;

    /**
     * Override for set binding variable
     *
     * @return variable id
     */
    public abstract int getBindingVariable();

    /**
     * @return layout resource id
     */
    public abstract
    @LayoutRes
    int getLayoutId();

    /**
     * Override for set view model
     *
     * @return view model instance
     */
    public abstract V getViewModel();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BaseActivity) {
            this.mActivity = (BaseActivity) context;

        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        performDependencyInjection();
        super.onCreate(savedInstanceState);
        mViewModel = getViewModel();
        setHasOptionsMenu(false);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mViewDataBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false);
        mRootView = mViewDataBinding.getRoot();
        return mRootView;
    }

    @Override
    public void onDetach() {
        mActivity = null;
        super.onDetach();
    }

    @Override
    @CallSuper
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewDataBinding.setVariable(getBindingVariable(), mViewModel);
        mViewDataBinding.setLifecycleOwner(this);
        mViewDataBinding.executePendingBindings();
    }

    public BaseActivity getBaseActivity() {
        return mActivity;
    }

    public T getViewDataBinding() {
        return mViewDataBinding;
    }

    public void hideKeyboard() {
        if (mActivity != null) {
            mActivity.hideKeyboard();
        }
    }

    public boolean isNetworkConnected() {
        return mActivity != null && mActivity.isNetworkConnected();
    }

    public void showProgressDialog() {
        dismissProgressDialog();
        myDialogFragment = new MyProgressDialogFragment(requireContext());
        myDialogFragment.show();
        myDialogFragment.setCancelable(false);

    }

    public void dismissProgressDialog() {

        if (myDialogFragment != null) {
            myDialogFragment.dismiss();
            myDialogFragment = null;
        }
    }


    private void performDependencyInjection() {
        AndroidSupportInjection.inject(this);
    }


    protected void showAlertDialog(String message, Runnable runnable) {
        new AlertDialog.Builder(requireContext()).setTitle(R.string.alert).setMessage(message).setCancelable(false).setPositiveButton(R.string.ok, (dialog, which) -> {
            if (runnable != null)
                runnable.run();
        }).show();
    }

    public void showMessage(String message) {
        if (isDetached() || getContext() == null || getView() == null)
            return;
        View view = getView();
        Snackbar snack = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        view = snack.getView();
        TextView tv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
        tv.setTextColor(Color.WHITE);
        snack.show();
    }

    public void showEditDialog() {
        new MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.alert)
                .setMessage(R.string.alert_exit_form)
                .setPositiveButton(R.string.Cancel, null)
                .setNegativeButton(R.string.Exit, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        forceExit=true;
                        requireActivity().onBackPressed();
                    }
                }).show();
    }

    public boolean onBackPressed() {
        return true;
    }
}
