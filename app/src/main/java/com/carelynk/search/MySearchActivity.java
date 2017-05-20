package com.carelynk.search;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.carelynk.R;
import com.carelynk.base.BaseActivity;
import com.carelynk.databinding.ActivityMySearchBinding;
import com.carelynk.rest.AsyncTaskGetCommon;
import com.carelynk.search.adapter.SearchListAdapter;
import com.carelynk.search.model.SearchModel;
import com.carelynk.storage.SharedPreferenceUtil;
import com.carelynk.utilz.Constants;
import com.carelynk.utilz.DialogUtils;
import com.carelynk.utilz.PrefUtils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import java.util.List;

/**
 * Created by Admin on 15-Sep-16.
 */
public class MySearchActivity extends BaseActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnMapClickListener, GoogleMap.OnMarkerClickListener, GoogleMap.OnMapLongClickListener {

    private ActivityMySearchBinding binding;

    private GoogleMap googleMap;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private LocationRequest mLocationRequest;
	private String mTypeSearch = "hospital";//gym, hair_care, health
    private static final String TAG = "MySearchActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        statusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        setupSlideWindowAnimationSlide(Gravity.BOTTOM);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_search);
        init();
        createLocationRequest();
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        binding.edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(binding.edtSearch.getText().length() > 0){
                    googleSearch();
                }else{
                    binding.recyclerView.setVisibility(View.GONE);
                }
            }
        });
    }

    public void onCurruntLocClick(View view){
        if(googleMap != null && mLastLocation != null){
            moveCameraCurrantLocation(mLastLocation.getLatitude(), mLastLocation.getLongitude());
        }
    }

    private void googleSearch() {
        AsyncTaskGetCommon asyncTaskGetCommon = new AsyncTaskGetCommon(this, new AsyncTaskGetCommon.AsyncTaskCompleteListener() {
            @Override
            public void onTaskComplete(String result) {
                SearchModel searchModel = new Gson().fromJson(result, SearchModel.class);
                if(searchModel != null && searchModel.getStatus().equalsIgnoreCase("ok")){
                    setSearchRecyclerAdapter(searchModel.getResults());
                }
            }
        });
        asyncTaskGetCommon.execute("https://maps.googleapis.com/maps/api/place/textsearch/json?query="+
                binding.edtSearch.getText().toString().trim()+"&location="+ SharedPreferenceUtil.getString(PrefUtils.PREF_LAT, "0")+","+SharedPreferenceUtil.getString(PrefUtils.PREF_LNG, "0")+
                "&radius=5000&type="+ mTypeSearch+"&key="+getString(R.string.google_api_key));
    }

    private void setSearchRecyclerAdapter(List<SearchModel.Result> results) {
        if(results.size() > 0){
            binding.recyclerView.setVisibility(View.VISIBLE);
        }
        SearchListAdapter searchListAdapter= new SearchListAdapter(this, results, MySearchActivity.this);
        binding.recyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        binding.recyclerView.setLayoutManager(mLayoutManager);
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerView.setAdapter(searchListAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        intiGoogleMap();
    }

    @Override
    public void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    public void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }


    void init() {
        setSupportActionBar(binding.includeToolbar.toolbar);
        binding.includeToolbar.toolbarTitle.setText(getString(R.string.discover));
        setTitle("");
        binding.includeToolbar.toolbar.setNavigationIcon(R.drawable.ic_back);
        binding.includeToolbar.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_my_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_categoty) {
            openCategoryDialog();
        }
        return true;
    }

    public void openCategoryDialog() {
        final Dialog dialog = new DialogUtils(this).setupCustomeDialogFromBottom(R.layout.dialog_search_category);
        dialog.show();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    private void intiGoogleMap() {
        SupportMapFragment supportMapFragment = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_search));
        supportMapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (googleMap != null) {
            this.googleMap = googleMap;
            this.googleMap.setOnMapClickListener(this);
            this.googleMap.setOnMarkerClickListener(this);
            this.googleMap.setOnMapLongClickListener(this);
            closeKeyBoard(this);
            //googleMap.setPadding(0, edtSearch.getMeasuredHeight() + 20, 0, 0);
            this.googleMap.getUiSettings().setCompassEnabled(true);
        }
    }

    private void moveMapMarker(double latitude, double longitude, String title, String desc) {
        closeKeyBoard(this);
        googleMap.clear();
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 15.5f), 4000, null);
        googleMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title(title).snippet(desc)).showInfoWindow();
    }

    private void moveCameraCurrantLocation(double latitude, double longitude) {
        googleMap.clear();
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 15.5f), 4000, null);
        SharedPreferenceUtil.putValue(PrefUtils.PREF_LAT, ""+latitude);
        SharedPreferenceUtil.putValue(PrefUtils.PREF_LNG, ""+longitude);
        SharedPreferenceUtil.save();
        googleMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title("Your Location")).showInfoWindow();
    }

    @Override
    public void onConnected(Bundle bundle) {
        if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION, this) &&
                checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION, this)) {
           permissionGrantedAndGetLatLng();
        }else{
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, Constants.REQUEST_CODE_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case Constants.REQUEST_CODE_FINE_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(googleMap != null)
                        permissionGrantedAndGetLatLng();
                }
                break;
        }
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(500);
        mLocationRequest.setFastestInterval(50);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void permissionGrantedAndGetLatLng() {
        LocationManager location = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (location.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            this.googleMap.setMyLocationEnabled(true);
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (googleMap != null) {
                if (mLastLocation != null) {
                    moveCameraCurrantLocation(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                } else
                    openLocationDialog(mGoogleApiClient, this);
            }
        } else {
            openLocationDialog(mGoogleApiClient, this);
        }
    }

    private void openLocationDialog(final GoogleApiClient mGoogleApiClient, final Activity activity) {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(30 * 1000);
        locationRequest.setFastestInterval(5 * 1000);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(this.mGoogleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                final LocationSettingsStates state = result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            status.startResolutionForResult(
                                    activity, Constants.REQUEST_LOCATION);
                        } catch (IntentSender.SendIntentException e) {
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        break;
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(TAG, "onActivityResult: ");
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constants.REQUEST_LOCATION) {
                this.googleMap.setMyLocationEnabled(true);
                mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                if (googleMap != null)
                    if (mLastLocation != null) {
                        moveCameraCurrantLocation(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                    }else{
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                onActivityResult(Constants.REQUEST_LOCATION, Activity.RESULT_OK, null);
                            }
                        }, 1000);
                    }
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }


    @Override
    public void onMapClick(LatLng latLng) {

    }

    @Override
    public void onMapLongClick(LatLng latLng) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.showInfoWindow();
        return true;
    }


    public void onSearchItemClick(SearchModel.Result result) {
        if(result.getGeometry() != null && result.getGeometry().getLocation() != null){
            binding.recyclerView.setVisibility(View.GONE);
            closeKeyBoard(this);
            moveMapMarker(result.getGeometry().getLocation().getLat(), result.getGeometry().getLocation().getLng(),
                    result.getName(), result.getFormattedAddress());
        }
    }
}
