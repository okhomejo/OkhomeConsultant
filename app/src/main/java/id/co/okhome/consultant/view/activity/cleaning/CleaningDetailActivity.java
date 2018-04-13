package id.co.okhome.consultant.view.activity.cleaning;

import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mrjodev.jorecyclermanager.QuickReturnViewInitializor;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.okhome.consultant.R;
import id.co.okhome.consultant.lib.app.OkHomeParentActivity;
import id.co.okhome.consultant.lib.app.OkhomeUtil;

public class CleaningDetailActivity extends OkHomeParentActivity implements OnMapReadyCallback {

    @BindView(R.id.actCleaningInfo_svItem)          ScrollView svItem;
    @BindView(R.id.actCleaningInfo_vgActions)       ViewGroup vgActions;
    @BindView(R.id.actCleaningInfo_vLoading)        View vLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cleaning_info);
        OkhomeUtil.setWhiteSystembar(this);

        ButterKnife.bind(this);
        init();
    }

    private void init() {
        boolean isAccepted = getIntent().getBooleanExtra("TASK_ACCEPTED", false);

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.fragTabPickingCleaning_map);
        mapFragment.getMapAsync(this);

        if (isAccepted) {
            vgActions.setVisibility(View.GONE);
        } else {
            QuickReturnViewInitializor.init(svItem, vgActions);
        }
        vLoading.setVisibility(View.GONE);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Adjust map style
        try {
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style_json));

            if (!success) {
                Log.e("Map error", "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e("Map error", "Can't find style. Error: ", e);
        }

        // Setup camera and pinpoint icon
        LatLng taskLocation = new LatLng(-6.218478, 106.836644);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(taskLocation , 16);
        googleMap.addMarker(new MarkerOptions()
                .position(taskLocation)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_blue))
        );
        googleMap.moveCamera(cameraUpdate);
        googleMap.getUiSettings().setScrollGesturesEnabled(false);
    }

    @OnClick(R.id.actCleaningInfo_vbtnX)
    public void onButtonGoBack() {
        finish();
    }
}
