package id.co.okhome.consultant.view.etc;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBufferResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.RuntimeRemoteException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

public class LocationActivity extends OkHomeParentActivity implements OnMapReadyCallback, LocationListener {

    private static final String TAG = "LocationAdapter";
    private static final LatLngBounds BOUNDS_GREATER_JAKARTA = new LatLngBounds(
            new LatLng(-6.595189, 106.310768), new LatLng(-5.986392, 107.121010));
    protected GeoDataClient mGeoDataClient;

    @BindView(R.id.actLocation_etLocation)      AutoCompleteTextView etLocation;
    @BindView(R.id.actLocation_vbtnClear)       View btnClear;
    @BindView(R.id.actLocation_progressBar)     ProgressBar progressBar;
    @BindView(R.id.actLocation_flProgress)      FrameLayout flProgress;
    @BindView(R.id.actLocation_vbtnConfirm)     View vbtnConfirm;

    boolean isKeyboardOpen = false;

    private GoogleMap googleMap;
    private PlaceAutocompleteAdapter mAdapter;
    private FusedLocationProviderClient mFusedLocationClient;
    private OnCompleteListener<PlaceBufferResponse> mUpdatePlaceDetailsCallback
            = new OnCompleteListener<PlaceBufferResponse>() {

        @Override
        public void onComplete(Task<PlaceBufferResponse> task) {
            try {
                PlaceBufferResponse places = task.getResult();

                // Get the Place object from the buffer.
                final Place place = places.get(0);

                LatLng selectedLocation = new LatLng(place.getLatLng().latitude, place.getLatLng().longitude);

                googleMap.moveCamera(CameraUpdateFactory.newLatLng(selectedLocation));
                googleMap.animateCamera(CameraUpdateFactory.zoomTo(17));

                Log.i(TAG, "Place details received: " + place.getName());

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

        OkhomeUtil.setSystemBarColor(this, ContextCompat.getColor(this, R.color.colorOkhome));
//                ContextCompat.getColor(this, R.color.colorOkhome));


        ButterKnife.bind(this);
        adaptViewsAndData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    //toggle confirm button visiblity
    private void toggleConfirmBtnVisibility(boolean show){

        if(show){
            vbtnConfirm.animate().translationY(0f).setDuration(400).start();
        }else{
            vbtnConfirm.animate().translationY(300f).setDuration(400).start();
        }

    }

    private void adaptViewsAndData() {
        etLocation.setOnItemClickListener(mAutocompleteClickListener);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mAdapter = new PlaceAutocompleteAdapter(this, mGeoDataClient, BOUNDS_GREATER_JAKARTA, null);
        etLocation.setAdapter(mAdapter);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.actLocation_fmMap);

        mapFragment.getMapAsync(this);

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

        etLocation.requestFocus();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        // Change map style
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

        // Get current location
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        googleMap.setMyLocationEnabled(true);

        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            onLocationChanged(location);
                        }
                    }
                });
    }

    @Override
    public void onLocationChanged(Location location) {
        double latitude     = location.getLatitude();
        double longitude    = location.getLongitude();
        LatLng latLng = new LatLng(latitude, longitude);

        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(17));
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    // OnClick listeners
    @OnClick(R.id.actLocation_vbtnX)
    public void onX(View v){
        finish();
    }

    @OnClick({R.id.actLocation_vbtnClear, R.id.actLocation_etLocation})
    public void clearText(){
        etLocation.getText().clear();
    }

    @OnClick(R.id.actLocation_ivMarker)
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

        toggleConfirmBtnVisibility(false);
    }

    public String getAddress(Double lat, Double lon) {
        Geocoder geocoder;
        geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lon, 1);

            String address = addresses.get(0).getAddressLine(0);

            return address;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return "error";
    }

}
