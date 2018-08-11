package id.co.okhome.consultant.view.fragment.cleaning_queue;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.okhome.consultant.R;
import id.co.okhome.consultant.lib.app.OkhomeDateTimeFormatUtil;
import id.co.okhome.consultant.lib.app.OkhomeUtil;
import id.co.okhome.consultant.model.cleaning.order.CleaningOrderModel;
import id.co.okhome.consultant.model.cleaning.order.CleaningReqQueueModel;
import id.co.okhome.consultant.view.activity.cleaning_order.CleaningOrderDetailActivity;

/**
 * Created by jo on 2018-04-07.
 */

public class CleaningQueueItemFragment extends Fragment implements OnMapReadyCallback {


    private static View vTag = null;

    @BindView(R.id.fragCleaningQueueItem_tvCleanigType)           TextView tvCleaningType;
    @BindView(R.id.fragCleaningQueueItem_tvAddress)               TextView tvAddress;
    @BindView(R.id.fragCleaningQueueItem_tvPerson)                TextView tvPerson;
    @BindView(R.id.fragCleaningQueueItem_tvWhen)                  TextView tvWhen;
    @BindView(R.id.fragCleaningQueueItem_tvTags)                  TextView tvTags;
    @BindView(R.id.fragCleaningQueueItem_vgHowMany)               ViewGroup vgHowMany;
    @BindView(R.id.fragCleaningQueueItem_tvPrice)                 TextView tvPrice;
    @BindView(R.id.fragCleaningQueueItem_tvHowMany)               TextView tvHowMany;


    CleaningReqQueueModel cleaningReqQueue;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cleaning_queue_item, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Bundle bundle = getArguments();
        cleaningReqQueue = (CleaningReqQueueModel) getArguments().get("cleaningReqQueue");
    }



    @Override
    public void onResume() {
        super.onResume();
        init();
    }

    private void init(){
        int price = cleaningReqQueue.cleaningOrderInfo.cleaningOrderDayItemModels.get(0).priceConsultant;
        int cleaningCount = cleaningReqQueue.cleaningOrderInfo.cleaningCount;
        int personOccupied = cleaningReqQueue.personOccupied;
        int personRequired = cleaningReqQueue.personRequired;
        String notoolYN = cleaningReqQueue.cleaningOrderInfo.cleaningOrderDayItemModels.get(0).notoolYN;
        String address = cleaningReqQueue.cleaningOrderInfo.homeAddress;
        DateTime dtFirstCleaningDateTime = new DateTime(cleaningReqQueue.firstCleaningDateTime);
        int durationMin = cleaningReqQueue.cleaningOrderInfo.cleaningOrderDayItemModels.get(0).totalDurationMinute;
        DateTime dtFirstCleaningDateTimeEnd = dtFirstCleaningDateTime.plusMinutes(durationMin);

        String whenString = OkhomeDateTimeFormatUtil.printOkhomeType(dtFirstCleaningDateTime.getMillis(), "(E) d MMM yy HH:mm");
        String whenStringTail = DateTimeFormat.forPattern("HH:mm").print(dtFirstCleaningDateTimeEnd);

        whenString += "-" + whenStringTail;
        if(cleaningCount > 1){
            whenString += String.format("(%d Times)", cleaningCount);
        }

        //뷰랑 대입
        tvWhen.setText(whenString);
        tvAddress.setText(address);
        tvPerson.setText(String.format("%d Persons (%d/%d)", personRequired, personOccupied, personRequired));
        tvCleaningType.setText(notoolYN.equals("Y") ? "No tool cleaning" : "With tool cleaning");

        if(cleaningCount > 0){
            vgHowMany.setVisibility(View.VISIBLE);
            tvHowMany.setText(cleaningCount + " Kali");
        }else{
            vgHowMany.setVisibility(View.GONE);
        }

        tvPrice.setText(OkhomeUtil.getPriceFormatValue(price) + " Rupiah");
        tvTags.setText(getTagStrings(getActivity().getLayoutInflater() ,cleaningReqQueue.cleaningOrderInfo));

        initMap();
    }


    //맵 정리
    private void initMap(){
//        MapFragment mapFragment = (MapFragment) getFragmentManager()
//                .findFragmentById(R.id.fragTabPickingCleaning_map);
//        mapFragment.getMapAsync(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.fragCleaningQueueItem_fragMap);
        mapFragment.getMapAsync(this);

    }

    @OnClick(R.id.fragCleaningQueueItem_vbtnSeeDetail)
    public void onBtnSeeDetail(View v){
        getActivity().startActivity(new Intent(getActivity(), CleaningOrderDetailActivity.class).putExtra("cleaningOrderId", cleaningReqQueue.orderId));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getContext(), R.raw.map_style_json));

        // Setup camera and pinpoint icon
        LatLng taskLocation = new LatLng(cleaningReqQueue.cleaningOrderInfo.homeLat, cleaningReqQueue.cleaningOrderInfo.homeLng);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(taskLocation , 16);
        googleMap.addMarker(new MarkerOptions()
                .position(taskLocation)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_blue))
        );
        googleMap.moveCamera(cameraUpdate);
        googleMap.getUiSettings().setScrollGesturesEnabled(false);
        googleMap.getUiSettings().setZoomGesturesEnabled(false);
    }


    //util
    //태그정리
    public static final SpannableString getTagStrings(LayoutInflater layoutInflater, CleaningOrderModel cleaningOrder){
        //태그 정리
        String homeSize = null, homeType = null, homeRoomCount = null, homeRestRoomCount = null, homePets = null;
        switch(cleaningOrder.homeType){
            case "APT":
                homeType = "Apartment";
                break;
            case "STUDIO":
                homeType = "Studio";
                break;
            case "HOUSE":
                homeType = "House";
                break;
        }

        homeSize = cleaningOrder.homeSize + "sqm";
        homeRoomCount = "Room " + cleaningOrder.homeRoomCnt;
        homeRestRoomCount = "Restroom " + cleaningOrder.homeRestroomCnt;

        List<String> tags = new ArrayList<>();
        tags.add(homeSize); tags.add(homeType); tags.add(homeRoomCount); tags.add(homeRestRoomCount);
        for(String pet : cleaningOrder.homePets){
            String petParsed = null;
            switch(pet){
                case "DOG":
                    petParsed = "Dog";
                    break;
                case "CAT":
                    petParsed = "Cat";
                    break;
                case "ETC":
                    petParsed = "Etc";
                    break;
            }
            tags.add(petParsed);
        }


        String tagFrame = "";
        for(String tag : tags){
            if(tag != null){
                tagFrame += "-";
            }
        }

        SpannableString spannableString = new SpannableString(tagFrame);
        int pos = 0;
        for(String tag : tags){
            if(tag != null){
                View v = makeTagView(layoutInflater, tag);
                BitmapDrawable bd = OkhomeUtil.getBitmapDrawableFromView(v);
                ImageSpan span = new ImageSpan(bd, ImageSpan.ALIGN_BASELINE);
                spannableString.setSpan(span, pos, pos+1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                pos ++;
            }
        }

        return spannableString;

    }

    //태그뷰만들기
    public static View makeTagView(LayoutInflater layoutInflater, String tag){
        if(vTag == null){
            vTag = layoutInflater.inflate(R.layout.item_cleaninginfo_tag, null);
        }

        TextView tvTag = vTag.findViewById(R.id.itemCleaningInfoTag_tvTitle);
        tvTag.setText(tag);

        return vTag;
    }

}
