package in.rajpusht.pc.ui.base;


import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;

import dagger.android.AndroidInjection;
import dagger.android.support.DaggerAppCompatActivity;
import in.rajpusht.pc.R;
import in.rajpusht.pc.utils.ContextWrapper;
import in.rajpusht.pc.utils.MyProgressDialogFragment;
import in.rajpusht.pc.utils.NetworkUtils;

public abstract class BaseActivity<T extends ViewDataBinding, V extends BaseViewModel> extends DaggerAppCompatActivity {

    // TODO
    // this can probably depend on isLoading variable of BaseViewModel,
    // since its going to be common for all the activities
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
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        performDependencyInjection();
        super.onCreate(savedInstanceState);
        performDataBinding();
    }

    public T getViewDataBinding() {
        return mViewDataBinding;
    }

    @TargetApi(Build.VERSION_CODES.M)
    public boolean hasPermission(String permission) {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||
                checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
    }

    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }


    public boolean isNetworkConnected() {
        return NetworkUtils.isNetworkConnected(getApplicationContext());
    }


    public void performDependencyInjection() {
        AndroidInjection.inject(this);
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void requestPermissionsSafely(String[] permissions, int requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, requestCode);
        }
    }


    private void performDataBinding() {
        mViewDataBinding = DataBindingUtil.setContentView(this, getLayoutId());
        this.mViewModel = mViewModel == null ? getViewModel() : mViewModel;
        mViewDataBinding.setVariable(getBindingVariable(), mViewModel);
        mViewDataBinding.executePendingBindings();
    }

    public void showProgressDialog() {
        dismissProgressDialog();
        myDialogFragment = new MyProgressDialogFragment(this);
        myDialogFragment.show();
        myDialogFragment.setCancelable(false);

    }

    public void dismissProgressDialog() {

        if (myDialogFragment != null) {
            myDialogFragment.dismiss();
            myDialogFragment = null;
        }
    }

    public void showMessage(View view, String message) {
        if (isFinishing())
            return;
        Snackbar snack = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        view = snack.getView();
        TextView tv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
        tv.setTextColor(Color.WHITE);
        snack.show();
    }

    public void showMessage(String message) {
        if (isFinishing())
            return;
        showMessage(findViewById(android.R.id.content), message);
    }

    public void showAlertDialog(String message, Runnable runnable) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.alert)
                .setMessage(message)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (runnable != null)
                            runnable.run();
                    }
                })
                .show();
    }

    @Override
    protected void onDestroy() {
        dismissProgressDialog();
        super.onDestroy();
    }


    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (fragment instanceof BaseFragment) {
            BaseFragment base = (BaseFragment) fragment;
            if (base.onBackPressed())
                super.onBackPressed();
        } else
            super.onBackPressed();


    }
}


