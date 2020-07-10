package in.rajpusht.pc.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.text.TextUtils;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

public class LocationLiveData extends LiveData<Location> {
    private final LocationRequest lowPowerreq;
    private final FusedLocationProviderClient mFusedLocationClient;
    Context context;
    LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);
            setValue(locationResult.getLastLocation());
        }
    };

    private LocationLiveData(Context context) {
        this.context = context;
        lowPowerreq = new LocationRequest();
        lowPowerreq.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
    }

    public static LocationLiveData getInstance(Context context) {
        return new LocationLiveData(context);
    }

    public static String getNetworkParam(Context context) {
        TelephonyManager tel = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        final TelephonyManager telephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (telephony.getPhoneType() == TelephonyManager.PHONE_TYPE_GSM) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return null;
            }
        }
        StringBuilder jsonObject = new StringBuilder();
        try {

            String networkOperator = tel.getNetworkOperator();
            final GsmCellLocation location = (GsmCellLocation) telephony.getCellLocation();
            if (TextUtils.isEmpty(networkOperator)) {
                jsonObject.append("MCC").append(":").append(Integer.parseInt(networkOperator.substring(0, 3))).append(",");
                jsonObject.append("MNC").append(":").append(Integer.parseInt(networkOperator.substring(3))).append(",");
            }
            if (location!=null) {
                jsonObject.append("LAC").append(":").append(location.getLac()).append(",");
                jsonObject.append("Cell_ID").append(":").append(location.getCid());
            }

        } catch (Exception e) {
            e.printStackTrace();
            jsonObject=new StringBuilder();
        }
        return jsonObject.toString();
    }

    @Override
    protected void onActive() {
        onConnect();
    }

    @Override
    protected void onInactive() {
        mFusedLocationClient.removeLocationUpdates(locationCallback);
    }

    public void onConnect() {
        // Try to immediately find a location
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mFusedLocationClient.getLastLocation().addOnSuccessListener(this::setValue);

        // Request updates if there’s someone observing
        if (hasActiveObservers()) {
            mFusedLocationClient.requestLocationUpdates(lowPowerreq, locationCallback, null);

        }
    }


}