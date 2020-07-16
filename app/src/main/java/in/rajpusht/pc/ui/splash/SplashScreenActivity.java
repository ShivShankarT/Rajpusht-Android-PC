package in.rajpusht.pc.ui.splash;

import android.Manifest;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import java.util.Date;

import javax.inject.Inject;

import in.rajpusht.pc.BR;
import in.rajpusht.pc.BuildConfig;
import in.rajpusht.pc.R;
import in.rajpusht.pc.ViewModelProviderFactory;
import in.rajpusht.pc.data.DataRepository;
import in.rajpusht.pc.data.local.pref.AppPreferencesHelper;
import in.rajpusht.pc.databinding.ActivitySplashScreenBinding;
import in.rajpusht.pc.ui.base.BaseActivity;
import in.rajpusht.pc.ui.home.HomeActivity;
import in.rajpusht.pc.ui.login.LoginActivity;
import timber.log.Timber;

public class SplashScreenActivity extends BaseActivity<ActivitySplashScreenBinding, SplashScreenViewModel> implements SplashNav {

    private static final int REQUEST_PERMISSIONS = 1211;
    @Inject
    ViewModelProviderFactory factory;
    @Inject
    DataRepository dataRepository;
    boolean isAppFetch = false;
    boolean isGpsChecked = false;
    private SplashScreenViewModel screenViewModel;
    private SparseIntArray mErrorString = new SparseIntArray();
    private boolean isGpsDialog;

    public static boolean isTimeAutomatic(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return Settings.Global.getInt(context.getContentResolver(), Settings.Global.AUTO_TIME, 0) == 1;
        } else {
            return android.provider.Settings.System.getInt(context.getContentResolver(), android.provider.Settings.System.AUTO_TIME, 0) == 1;
        }
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_splash_screen;
    }

    @Override
    public SplashScreenViewModel getViewModel() {
        screenViewModel = new ViewModelProvider(this, factory).get(SplashScreenViewModel.class);
        screenViewModel.setNavigator(this);
        return screenViewModel;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Timber.i("App launch");
        if (!isTimeAutomatic(this) && !BuildConfig.DEBUG) {
            Timber.i("System time incorrect");
            showAlertDialog("SET SYSTEM TIME  AUTOMATE FORMAT", new Runnable() {
                @Override
                public void run() {
                    startActivityForResult(new Intent(android.provider.Settings.ACTION_DATE_SETTINGS), 0);
                }
            });
            return;
        }
        String time = dataRepository.getPrefString(AppPreferencesHelper.PREF_LAST_APPCONFIG_FTIME);
        boolean callApi = true;
        if (!TextUtils.isEmpty(time)) {
            long l = Long.parseLong(time);
            long dif = new Date().getTime() - l;
            if (dif > 3 * 60 * 60 * 1000) {
                callApi = true;
            } else {
                callApi = false;
            }
        }
        isAppFetch = !callApi;
        if (callApi) {
            screenViewModel.appConfigVersion();

        }


        String message = "<#> Hello POSHAN Champion, 567673 is your OTP for mobile verification on RajPusht PC App. NXjWbXQqwib";
        String otp = message.replace("<#> Hello POSHAN Champion, ", "");
        otp = otp.substring(0, 6);
        Log.i("onCreateonCreate", "onCreate: "+otp);

    }

    @Override
    protected void onResume() {
        super.onResume();
        requestAppPermissions(new
                        String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_FINE_LOCATION,},
                R.string.runtime_permissions_txt
                , REQUEST_PERMISSIONS);
    }

    public void checkGpsOnAndLocPermission() {
        try {
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            if (!isGPSEnabled) {
                Timber.i("System GPS is Off");
                if (!isGpsDialog) {
                    showSettingsAlert();
                    isGpsDialog = true;
                }
            } else {
                isGpsChecked = true;
                launchLogin();
            }
        } catch (SecurityException e) {
            e.printStackTrace();

        }

    }


    public void onPermissionsGranted(int requestCode) {
        checkGpsOnAndLocPermission();

    }

    public void launchLogin() {

        if (!checkAppVersion()) {
            return;
        }
        if (isGpsChecked && isAppFetch)
            new Handler().postDelayed(() -> {
                checkAndLaunchSc();
                finish();

            }, 1000);
    }

    private void checkAndLaunchSc() {
        if (dataRepository.getLogin()) {
            Intent intent = new Intent(SplashScreenActivity.this, HomeActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
            startActivity(intent);

        }
    }


    public void showSettingsAlert() {

        MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(this)
                .setTitle("GPS Location")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setCancelable(false)
                .setMessage("Allow Application To Access your Location?")
                .setPositiveButton("OK", (dialog, which) -> {
                    dialog.dismiss();
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                    isGpsDialog = false;
                }).setNegativeButton("Cancel", (dialog, which) -> {
                    dialog.dismiss();
                    isGpsDialog = false;
                    isGpsChecked = true;
                    launchLogin();
                });
        AlertDialog alertDialog = materialAlertDialogBuilder.show();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        int permissionCheck = PackageManager.PERMISSION_GRANTED;
        for (int permission : grantResults) {
            permissionCheck = permissionCheck + permission;
        }
        if ((grantResults.length > 0) && permissionCheck == PackageManager.PERMISSION_GRANTED) {
            onPermissionsGranted(requestCode);
        } else {
            Snackbar.make(findViewById(android.R.id.content), mErrorString.get(requestCode),
                    Snackbar.LENGTH_INDEFINITE).setAction("ENABLE",
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish();
                            Intent intents = new Intent(getBaseContext(), SplashScreenActivity.class);

                            Intent intent = new Intent();
                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            intent.addCategory(Intent.CATEGORY_DEFAULT);
                            intent.setData(Uri.parse("package:" + getPackageName()));
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);

                            TaskStackBuilder stackBuilder = TaskStackBuilder.create(getBaseContext()).addNextIntent(intents).addNextIntent(intent);
                            stackBuilder.startActivities();

                        }
                    }).show();
        }
    }

    public void requestAppPermissions(final String[] requestedPermissions,
                                      final int stringId, final int requestCode) {
        mErrorString.put(requestCode, stringId);
        int permissionCheck = PackageManager.PERMISSION_GRANTED;
        boolean shouldShowRequestPermissionRationale = false;
        for (String permission : requestedPermissions) {
            permissionCheck = permissionCheck + ContextCompat.checkSelfPermission(this, permission);
            shouldShowRequestPermissionRationale = shouldShowRequestPermissionRationale || ActivityCompat.shouldShowRequestPermissionRationale(this, permission);
        }
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRequestPermissionRationale) {
                Snackbar.make(findViewById(android.R.id.content), stringId,
                        Snackbar.LENGTH_INDEFINITE).setAction("GRANT",
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ActivityCompat.requestPermissions(SplashScreenActivity.this, requestedPermissions, requestCode);
                            }
                        }).show();
            } else {
                ActivityCompat.requestPermissions(this, requestedPermissions, requestCode);
            }
        } else {
            onPermissionsGranted(requestCode);
        }
    }


    @Override
    public void appConfigFetch(boolean isSuccess) {
        isAppFetch = true;
        if (!isFinishing())
            launchLogin();

    }

    boolean checkAppVersion() {

        int min = dataRepository.getPrefInt(AppPreferencesHelper.PREF_MIN_VERSION);
        int currentAppver = dataRepository.getPrefInt(AppPreferencesHelper.PREF_CURRENT_VERSION);
        String url = dataRepository.getPrefString(AppPreferencesHelper.PREF_DRIVE_URL);
        int appVersion = BuildConfig.VERSION_CODE;


        if (min == -1)
            return true;
        boolean isForceUpdate;
        if (appVersion < min) {
            isForceUpdate = true;
        } else if (appVersion < currentAppver) {
            isForceUpdate = false;
        } else {
            return true;
        }

        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.alert)
                .setMessage("Please Update App");
        if (!isForceUpdate)
            dialogBuilder.setNegativeButton(R.string.Cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    checkAndLaunchSc();
                }
            });

        dialogBuilder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent view = new Intent();
                view.setAction(Intent.ACTION_VIEW);
                String uriString = TextUtils.isEmpty(url) ? "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID : url;
                view.setData(Uri.parse(uriString));
                startActivity(view);

            }
        });
        dialogBuilder.show();

        return false;
    }
}
