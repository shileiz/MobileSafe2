package com.zsl.android.mobilesafe.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;

import com.zsl.android.mobilesafe.utils.PermissionUtils;

import static com.zsl.android.mobilesafe.application.BaseApplication.PREF_KEY_LOCATION;

public class LocationService extends Service {

    private LocationManager lm;
    private MyLocationListener listener;
    private SharedPreferences mPerf;

    @Override
    public void onCreate() {
        super.onCreate();
        mPerf = getSharedPreferences("config", MODE_PRIVATE);
        if (PermissionUtils.checkAccessLocationPermission(this)) {
            lm = (LocationManager) this.getSystemService(LOCATION_SERVICE);
            listener = new MyLocationListener();
            Criteria crit = new Criteria();
            crit.setAccuracy(Criteria.ACCURACY_FINE);
            crit.setCostAllowed(true);
            String bestProvider = lm.getBestProvider(crit, true);
            lm.requestLocationUpdates(bestProvider, 0, 0, listener);
            //lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, listener);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    class MyLocationListener implements LocationListener {
        // 当位置发生变化
        @Override
        public void onLocationChanged(Location location) {
            String longitude = location.getLongitude()+"";
            String latitude =  location.getLatitude()+"";
            mPerf.edit().putString(PREF_KEY_LOCATION, "longitude:"+longitude+","+"latitude:"+latitude).apply();
            lm.removeUpdates(listener);
            LocationService.this.stopSelf();
        }

        // 当某一个位置提供者状态发生变化的时候
        // 比如 拿不到位置--》能拿到位置
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onProviderDisabled(String provider) {
        }
    }
}
