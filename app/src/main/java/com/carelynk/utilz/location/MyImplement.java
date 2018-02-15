package com.carelynk.utilz.location;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.carelynk.R;

/**
 * Created by Mohit-Anjali on 16-Feb-18.
 */

public class MyImplement extends AppCompatActivity {

    private LocationUpdate locationUpdate;
    private static final String TAG = "MyImplement";
    public static final int REQUEST_CHECK_SETTINGS = 0x1;
    /**
     * Code used in requesting runtime permissions.
     */
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        locationUpdate = new LocationUpdate(this, new LocationUpdate.OnLocationInteraction() {
            @Override
            public void onLocationUpdate(double lat, double lng) {
                Log.e(TAG, "onLocationUpdate: "+lat+""+lng);
            }

            @Override
            public void onReqPermission() {
                requestPermissions();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        locationUpdate.stopLocationUpdates();
    }

    private void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_FINE_LOCATION);
        if (shouldProvideRationale) {
            //TODO snackbar
            Log.e(TAG, "Displaying permission rationale to provide additional context.");
            ActivityCompat.requestPermissions(MyImplement.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        } else {
            Log.e(TAG, "Requesting permission");
            ActivityCompat.requestPermissions(MyImplement.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length <= 0) {
                Log.e(TAG, "User interaction was cancelled.");
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.e(TAG, "Permission granted, updates requested, starting location updates");
                locationUpdate.mRequestingLocationUpdates = true;
                locationUpdate.startLocationUpdates();
            } else {

            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        Log.e(TAG, "User agreed to make required location settings changes.");
                        // Nothing to do. startLocationupdates() gets called in onResume again.
                        locationUpdate.startLocationUpdates();
                        break;
                    case Activity.RESULT_CANCELED:
                        Log.e(TAG, "User chose not to make required location settings changes.");
                        locationUpdate.mRequestingLocationUpdates = false;
                        break;
                }
                break;
        }
    }
}
