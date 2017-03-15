package com.carelynk.search;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.location.Location;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.carelynk.R;
import com.carelynk.base.BaseActivity;
import com.carelynk.dashboard.adapter.AllGroupRecyclerAdapter;
import com.carelynk.dashboard.fragment.MyGroupFragment;
import com.carelynk.databinding.ActivityMySearchBinding;
import com.carelynk.rest.AsyncTaskGetCommon;
import com.carelynk.search.adapter.SearchListAdapter;
import com.carelynk.search.model.SearchModel;
import com.carelynk.utilz.Constants;
import com.carelynk.utilz.DialogUtils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import java.util.ArrayList;
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
                }
            }
        });
    }

    private void googleSearch() {
        AsyncTaskGetCommon asyncTaskGetCommon = new AsyncTaskGetCommon(this, new AsyncTaskGetCommon.AsyncTaskCompleteListener() {
            @Override
            public void onTaskComplete(String result) {
                SearchModel searchModel = new Gson().fromJson(result, SearchModel.class);
                if(searchModel.getStatus().equalsIgnoreCase("ok")){
                    setSearchRecyclerAdapter(searchModel.getResults());
                }
            }
        });
        asyncTaskGetCommon.execute("https://maps.googleapis.com/maps/api/place/textsearch/json?query="+binding.edtSearch.getText().toString()+"&key="+getString(R.string.google_api_key));
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
        binding.includeToolbar.toolbarTitle.setText(getString(R.string.my_search));
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

    private void moveMapMarker(double latitude, double longitude) {
        closeKeyBoard(this);
        googleMap.clear();
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 15.5f), 4000, null);
        googleMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher)).snippet("-1"));
    }

    private void moveMapMarkerForCurrantLocation(double latitude, double longitude) {
        googleMap.clear();
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 15.5f), 4000, null);
        googleMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title("Your Location").snippet("Home")).showInfoWindow();
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
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (googleMap != null)
            if (mLastLocation != null) {
                moveMapMarkerForCurrantLocation(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            } else {
                showSnackbar(binding.getRoot(), "Check location permission");
                showAlertDialog(new OnDialogClick() {
                    @Override
                    public void onPositiveBtnClick() {
                        Intent viewIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivityForResult(viewIntent, Constants.REQUEST_LOCATION);
                    }

                    @Override
                    public void onNegativeBtnClick() {

                    }
                }, "Location", "Check location permission, Press Yes to ON", true);
            }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == Constants.REQUEST_LOCATION){
                if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION, this) &&
                        checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION, this)) {
                    permissionGrantedAndGetLatLng();
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


}
