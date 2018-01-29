package id.co.okhome.consultant.view.etc;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBufferResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.RuntimeRemoteException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.okhome.consultant.R;
import id.co.okhome.consultant.adapter.PlaceAutocompleteAdapter;
import id.co.okhome.consultant.lib.OkHomeParentActivity;
import id.co.okhome.consultant.lib.OkhomeUtil;

public class LocationActivity extends OkHomeParentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "LocationAdapter";
    private static final LatLngBounds BOUNDS_GREATER_JAKARTA = new LatLngBounds(
            new LatLng(-6.595189, 106.310768), new LatLng(-5.986392, 107.121010));
    protected GeoDataClient mGeoDataClient;

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    @BindView(R.id.actLocation_etLocation)      AutoCompleteTextView etLocation;
    @BindView(R.id.actLocation_progressBar)     ProgressBar progressBar;
    @BindView(R.id.actLocation_flProgress)      FrameLayout flProgress;
    @BindView(R.id.actLocation_fmMap)           FrameLayout flMap;
    @BindView(R.id.actLocation_llActions)       LinearLayout buttonContainer;
    @BindView(R.id.actLocation_vbtnClear)       View btnClear;

    private GoogleMap googleMap;
    private GoogleApiClient mGoogleApiClient;
    private PlaceAutocompleteAdapter mAdapter;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest mLocationRequest;
    private LatLng currentLocation;

    private OnCompleteListener<PlaceBufferResponse> mUpdatePlaceDetailsCallback
            = new OnCompleteListener<PlaceBufferResponse>() {

        @Override
        public void onComplete(Task<PlaceBufferResponse> task) {
            try {
                PlaceBufferResponse places = task.getResult();

                // Get the Place object from the buffer.
                final Place place = places.get(0);

                LatLng selectedLocation = new LatLng(place.getLatLng().latitude, place.getLatLng().longitude);

                CameraUpdate camUpdate = CameraUpdateFactory.newLatLngZoom(selectedLocation, 17);
                googleMap.animateCamera(camUpdate);

                Log.i(TAG, "Place details received: " + place.getName());

                etLocation.setAdapter(null);
                etLocation.setText(place.getName());
                etLocation.setAdapter(mAdapter);

                places.release();

                flProgress.setVisibility(View.GONE);

            } catch (RuntimeRemoteException e) {
                Log.e(TAG, "Place query did not complete.", e);
                return;
            }
        }
    };
    private AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            flProgress.setVisibility(View.VISIBLE);

            final AutocompletePrediction item = mAdapter.getItem(position);
            final String placeId = item.getPlaceId();
            final CharSequence primaryText = item.getPrimaryText(null);

            Log.i(TAG, "Autocomplete item selected: " + primaryText);

            etLocation.setSelection(0);
            etLocation.clearFocus();
            OkhomeUtil.hideKeyboard(LocationActivity.this);

            Task<PlaceBufferResponse> placeResult = mGeoDataClient.getPlaceById(placeId);
            placeResult.addOnCompleteListener(mUpdatePlaceDetailsCallback);

            Log.i(TAG, "Called getPlaceById to get Place details for " + placeId);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        mGeoDataClient = Places.getGeoDataClient(this, null);
        setContentView(R.layout.activity_location);

        ButterKnife.bind(this);

        adaptViewsAndData();
    }

    private void adaptViewsAndData() {
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.actLocation_fmMap);
        mapFragment.getMapAsync(this);

        OkhomeUtil.setSystemBarColor(this, ContextCompat.getColor(this, R.color.colorOkhome));
//                ContextCompat.getColor(this, R.color.colorOkhome));

        etLocation.setOnItemClickListener(mAutocompleteClickListener);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mAdapter = new PlaceAutocompleteAdapter(this, mGeoDataClient, BOUNDS_GREATER_JAKARTA, null);

        etLocation.setAdapter(mAdapter);
        etLocation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (etLocation.length() > 0) {
                    btnClear.setVisibility(View.VISIBLE);
                } else {
                    btnClear.setVisibility(View.GONE);
                }
            }

        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();

        if (mFusedLocationClient != null) {
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        // define point to center on
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(-6.214305,106.842318) , 11)
        );

        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                buildGoogleApiClient();
                googleMap.setMyLocationEnabled(true);
            } else {
                //Request Location Permission
                checkLocationPermission();
            }
        } else {
            buildGoogleApiClient();
            googleMap.setMyLocationEnabled(true);
        }
        googleMap.getUiSettings().setMyLocationButtonEnabled(false);

        // Adjust map style
        try {
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.map_style_json));

            if (!success) {
                Log.e(TAG, "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error: ", e);
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    public void changeLocation(Location location) {

        double latitude     = location.getLatitude();
        double longitude    = location.getLongitude();
        LatLng latLng = new LatLng(latitude, longitude);

        // Not able to move the camera when animation is active
        googleMap.getUiSettings().setAllGesturesEnabled(false);

        CameraUpdate camUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 17);
        googleMap.animateCamera(camUpdate, new GoogleMap.CancelableCallback() {
            @Override
            public void onFinish() {
                googleMap.getUiSettings().setAllGesturesEnabled(true);
            }

            @Override
            public void onCancel() {

            }
        });
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        // Only fire once during creation
        mLocationRequest = new LocationRequest();
        mLocationRequest.setNumUpdates(1);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
        }
    }

    LocationCallback mLocationCallback = new LocationCallback(){
        @Override
        public void onLocationResult(LocationResult locationResult) {
            for (Location location : locationResult.getLocations()) {

                currentLocation = new LatLng(location.getLatitude(), location.getLongitude());

                if(getIntent().getExtras() == null) {
                    // Go to current device location
                    googleMap.getUiSettings().setAllGesturesEnabled(false);

                    CameraUpdate camUpdate = CameraUpdateFactory.newLatLngZoom(currentLocation, 17);
                    googleMap.animateCamera(camUpdate, new GoogleMap.CancelableCallback() {
                        @Override
                        public void onFinish() {
                            googleMap.getUiSettings().setAllGesturesEnabled(true);
                        }

                        @Override
                        public void onCancel() {

                        }
                    });
                } else {
                    // Go to last location
                    Bundle bundle = getIntent().getExtras();
                    LatLng coordinates = new LatLng(
                            bundle.getDouble("latitude"),
                            bundle.getDouble("longitude")
                    );
                    CameraUpdate current = CameraUpdateFactory.newLatLngZoom(coordinates,17);
                    googleMap.moveCamera(current);

                    etLocation.setAdapter(null);
                    etLocation.setText(bundle.getString("address"));
                    etLocation.setAdapter(mAdapter);
                }
            }
            mapCameraMovementListeners();
        }
    };

    //toggle confirm button visiblity
    private void toggleConfirmBtnVisibility(boolean show){

        if(show){
            buttonContainer.animate().translationX(0f).setDuration(200).start();
        }else{
            buttonContainer.animate().translationX(300f).setDuration(200).start();
        }
    }

    public void mapCameraMovementListeners() {

        googleMap.setOnCameraMoveStartedListener(new GoogleMap.OnCameraMoveStartedListener() {
            @Override
            public void onCameraMoveStarted(int i) {
                toggleConfirmBtnVisibility(false);
                flProgress.setVisibility(View.VISIBLE);
                btnClear.setVisibility(View.GONE);
            }
        });

        googleMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {

                flProgress.setVisibility(View.VISIBLE);
                toggleConfirmBtnVisibility(true);

                // Update address when camera returns to idle status
                if (googleMap != null) {
                    Double lat = googleMap.getCameraPosition().target.latitude;
                    Double lon = googleMap.getCameraPosition().target.longitude;

                    // Set adapter null to prevent unnecessary calls to the Google API
                    etLocation.setAdapter(null);
                    etLocation.setText(getAddress(lat, lon));
                    etLocation.setAdapter(mAdapter);
                    etLocation.clearFocus();
                }
                flProgress.setVisibility(View.GONE);
                btnClear.setVisibility(View.VISIBLE);
            }
        });
    }

    public String getAddress(Double lat, Double lon) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lon, 1);
            if (!addresses.isEmpty()) {
                String address = addresses.get(0).getAddressLine(0);
                if (address.contains(",")) {
                    address = address.split(",")[0];
                    return address;
                } else {
                    return address;
                }
            } else {
                return "Error, please check the location.";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Error, please check your connection.";
    }

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                new AlertDialog.Builder(this)
                        .setTitle(R.string.location_permission)
                        .setMessage(R.string.location_permission_dialog)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(LocationActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION );
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION );
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        googleMap.setMyLocationEnabled(true);
                    }

                } else {
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    // OnClick listeners
    @OnClick(R.id.actLocation_vbtnX)
    public void onX(View v){
        finish();
    }

    @OnClick(R.id.actLocation_vbtnClear)
    public void clearText() {

        etLocation.getText().clear();
        etLocation.setFocusableInTouchMode(true);
        etLocation.requestFocus();

        final InputMethodManager inputMethodManager = (InputMethodManager) this
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(etLocation, InputMethodManager.SHOW_IMPLICIT);
    }

    @OnClick(R.id.actLocation_vbtnConfirm)
    public void submitLocation(){

        Double lat = googleMap.getCameraPosition().target.latitude;
        Double lon = googleMap.getCameraPosition().target.longitude;

        setResult(Activity.RESULT_OK,
                new Intent()
                        .putExtra("latitude", lat)
                        .putExtra("longitude", lon)
                        .putExtra("address", getAddress(lat, lon))
        );
        finish();
    }

    @OnClick(R.id.actLocation_vbtnMyLocation)
    public void goToMyLocation() {

        CameraUpdate location = CameraUpdateFactory.newLatLngZoom(currentLocation, 17);
        googleMap.animateCamera(location);
    }
}
