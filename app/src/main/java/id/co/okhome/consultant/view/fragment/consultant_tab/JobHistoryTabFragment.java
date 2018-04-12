package id.co.okhome.consultant.view.fragment.consultant_tab;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
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
        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(0, 0))
                .title("Marker"));
    }


//    private void getPreviousCleaningTasks() {
//        OkhomeRestApi.getCleaningTaskClient().getPrevCleaningTasks(ConsultantLoggedIn.id()).enqueue(new RetrofitCallback<List<CleaningInfoModel>>() {
//            @Override
//            public void onSuccess(List<CleaningInfoModel> cleaningList) {
//                adaptViews(cleaningList);
//            }
//
//            @Override
//            public void onFinish() {
//                super.onFinish();
//            }
//        });
//    }
//
//    private void adaptViews(List<CleaningInfoModel> cleaningList) {
//        for (CleaningInfoModel model : cleaningList) {
//
//        }
//    }

    @OnClick(R.id.fragTabJobs_vbtnSeeNextCleaning)
    public void onClickSeeNext() {
        startActivity(new Intent(getContext(), NextCleaningsActivity.class));
    }

    @OnClick(R.id.fragTabJobs_vbtnSeePreviousCleaning)
    public void onClickPreviousNext() {
        startActivity(new Intent(getContext(), PreviousCleaningsActivity.class));
    }
}
