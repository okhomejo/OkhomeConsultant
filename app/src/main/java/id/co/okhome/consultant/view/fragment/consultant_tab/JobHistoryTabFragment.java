package id.co.okhome.consultant.view.fragment.consultant_tab;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.okhome.consultant.R;
import id.co.okhome.consultant.lib.fragment_pager.TabFragmentStatusListener;
import id.co.okhome.consultant.view.activity.cleaning.NextCleaningsActivity;
import id.co.okhome.consultant.view.activity.cleaning.PreviousCleaningsActivity;

/**
 * Created by jo on 2018-04-07.
 */

public class JobHistoryTabFragment extends Fragment implements TabFragmentStatusListener, OnMapReadyCallback {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tab_jobs, null);
    }

    @Override
    public void onStart() {
        super.onStart();
        ButterKnife.bind(this, getView());
        init();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onSelect() {

    }

    @Override
    public void onDeselect() {

    }

    private void init() {
        MapFragment mapFragment = (MapFragment) getActivity().getFragmentManager()
                .findFragmentById(R.id.fragTabJobs_map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Adjust map style
        try {
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            getContext(), R.raw.map_style_json));

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
    }

    @OnClick(R.id.fragTabJobs_vbtnSeeNextCleaning)
    public void onClickSeeNext() {
        startActivity(new Intent(getContext(), NextCleaningsActivity.class));
    }

    @OnClick(R.id.fragTabJobs_vbtnSeePreviousCleaning)
    public void onClickPreviousNext() {
        startActivity(new Intent(getContext(), PreviousCleaningsActivity.class));
    }
}
