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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.okhome.consultant.R;
import id.co.okhome.consultant.lib.app.OkhomeUtil;
import id.co.okhome.consultant.lib.fragment_pager.TabFragmentStatusListener;
import id.co.okhome.consultant.model.cleaning.CleaningInfoModel;
import id.co.okhome.consultant.view.activity.cleaning.CleaningDetailActivity;

public class NextCleaningTabFragment extends Fragment implements TabFragmentStatusListener, OnMapReadyCallback {

    @BindView(R.id.fragTabNextCleaning_tvAddress)       TextView tvAddress;
    @BindView(R.id.fragTabNextCleaning_tvDate)          TextView tvDate;
    @BindView(R.id.fragTabNextCleaning_tvHomeInfo)      TextView tvHomeInfo;
    @BindView(R.id.fragTabNextCleaning_tvPerson)        TextView tvPerson;
    @BindView(R.id.fragTabNextCleaning_tvPrice)         TextView tvPrice;

    private static final String TASK_KEY = "cleaning_model_key";
    private CleaningInfoModel cleaningModel;

    public static NextCleaningTabFragment newInstance(CleaningInfoModel model) {
        NextCleaningTabFragment fragment = new NextCleaningTabFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(TASK_KEY, model);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        cleaningModel = (CleaningInfoModel) getArguments().getSerializable(TASK_KEY);
        return inflater.inflate(R.layout.fragment_tab_next_cleaning, null);
    }

    @Override
    public void onStart() {
        super.onStart();
        ButterKnife.bind(this, getView());
    }

    @Override
    public void onResume() {
        super.onResume();
        adaptViewsAndData();
    }

    private void adaptViewsAndData() {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        DateTime dt = formatter.parseDateTime(cleaningModel.when);
        tvDate.setText(String.format("%s, %s hours", dt.toString("dd MMM yy, hh:mm"), Math.round(cleaningModel.displayingCleaningDuration)));

        tvAddress.setText(cleaningModel.address);

        tvPrice.setText("Rp --");

        tvHomeInfo.setText(String.format("%s, %s rooms, %s restrooms, %s",
                cleaningModel.homeType,
                cleaningModel.roomCnt,
                cleaningModel.restroomCnt,
                cleaningModel.homeSize)
        );

        tvPerson.setText(String.format("%s person", cleaningModel.person));

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.fragTabNextCleaning_map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onSelect() {
    }

    @Override
    public void onDeselect() {
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
        LatLng taskLocation = new LatLng(cleaningModel.lat, cleaningModel.lng);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(taskLocation , 11);
        googleMap.addMarker(new MarkerOptions()
                .position(taskLocation)
                .title(cleaningModel.address)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_blue))
        );
        googleMap.moveCamera(cameraUpdate);
    }

    @OnClick(R.id.fragTabNextCleaning_vbtnSeeDetail)
    public void onBtnSeeDetails(){
        Intent intent = new Intent(getContext(), CleaningDetailActivity.class);
        intent.putExtra("TASK_ACCEPTED", true);
        getContext().startActivity(intent);    }

}
