package id.co.okhome.consultant.view.activity.cleaning_order;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

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

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.okhome.consultant.R;
import id.co.okhome.consultant.lib.app.ConsultantLoggedIn;
import id.co.okhome.consultant.lib.app.OkHomeParentActivity;
import id.co.okhome.consultant.lib.app.OkhomeDateTimeFormatUtil;
import id.co.okhome.consultant.lib.app.OkhomeUtil;
import id.co.okhome.consultant.lib.dialog.DialogParent;
import id.co.okhome.consultant.lib.joviewrepeator.JoRepeatorAdapter;
import id.co.okhome.consultant.lib.joviewrepeator.JoViewRepeator;
import id.co.okhome.consultant.lib.retrofit.RetrofitCallback;
import id.co.okhome.consultant.model.cleaning.order.CleaningDayItemModel;
import id.co.okhome.consultant.model.cleaning.order.CleaningOrderDaySpecificationModel;
import id.co.okhome.consultant.model.cleaning.order.CleaningOrderInfoDetailModel;
import id.co.okhome.consultant.rest_apicall.retrofit_restapi.OkhomeRestApi;
import id.co.okhome.consultant.view.dialog.CommonAlertDialog;
import id.co.okhome.consultant.view.fragment.cleaning_queue.CleaningQueueItemFragment;

public class CleaningOrderDetailActivity extends OkHomeParentActivity implements OnMapReadyCallback {

    @BindView(R.id.actCleaningOrderDetail_svItem)            ScrollView svItem;
    @BindView(R.id.actCleaningOrderDetail_vgActions)         ViewGroup vgActions;
    @BindView(R.id.actCleaningOrderDetail_vLoading)          View vLoading;
    @BindView(R.id.actCleaningOrderDetail_vgCleaningThumbs)  ViewGroup vgCleaningThumbs;

    //
    @BindView(R.id.actCleaningOrderDetail_tvAddress)             TextView tvAddress;
    @BindView(R.id.actCleaningOrderDetail_tvPerson)              TextView tvPerson;
    @BindView(R.id.actCleaningOrderDetail_tvPrice)               TextView tvPrice;
    @BindView(R.id.actCleaningOrderDetail_tvTags)                TextView tvTags;
    @BindView(R.id.actCleaningOrderDetail_vgHowMany)             ViewGroup vgHowMany;
    @BindView(R.id.actCleaningOrderDetail_tvHowMany)             TextView tvHowMany;
    @BindView(R.id.actCleaningOrderDetail_tvCleanigType)         TextView tvCleanigType;
    @BindView(R.id.actCleaningOrderDetail_tvCommentFromCustomer) TextView tvCommentFromCustomer;
    @BindView(R.id.actCleaningOrderDetail_tvCommentFromOkhome)   TextView tvCommentFromOKhome;

    CleaningOrderInfoDetailModel cleaningOrderInfoDetail = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cleaning_order_detail_info);
        OkhomeUtil.setWhiteSystembar(this);

        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        int cleaningOrderId = getIntent().getIntExtra("cleaningOrderId", 0);
        if(cleaningOrderId == 0){
            finish();
            return;
        }
        getCleaningInfo(cleaningOrderId);
    }

    //청소 정보 가져오기
    private void getCleaningInfo(int cleaningOrderId){
        vLoading.setVisibility(View.VISIBLE);
        vgActions.setVisibility(View.GONE);
        svItem.setVisibility(View.INVISIBLE);
        OkhomeRestApi.getCleaningOrderClient().getCleaningDetail(cleaningOrderId, ConsultantLoggedIn.id()).enqueue(new RetrofitCallback<CleaningOrderInfoDetailModel>() {
            @Override
            public void onSuccess(CleaningOrderInfoDetailModel cleaningOrderInfoDetail) {
                CleaningOrderDetailActivity.this.cleaningOrderInfoDetail = cleaningOrderInfoDetail;
                adaptData();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                vLoading.setVisibility(View.GONE);
                svItem.setVisibility(View.VISIBLE);
            }
        });
    }

    //데이터 연결
    private void adaptData() {
        initMap();

        if(cleaningOrderInfoDetail.cleaningOrder != null && cleaningOrderInfoDetail.joinYN.equals("N")){
            vgActions.setVisibility(View.VISIBLE);
            QuickReturnViewInitializor.init(svItem, vgActions);
        }else{
            vgActions.setVisibility(View.GONE);
        }
        //처리하자...

        int price = cleaningOrderInfoDetail.cleaningOrder.cleaningOrderDayItemModels.get(0).priceConsultant;
        int cleaningCount = cleaningOrderInfoDetail.cleaningOrder.cleaningCount;
        int personRequired = cleaningOrderInfoDetail.cleaningOrder.personRequired;
        String notoolYN = cleaningOrderInfoDetail.cleaningOrder.cleaningOrderDayItemModels.get(0).notoolYN;
        String address = cleaningOrderInfoDetail.cleaningOrder.homeAddress;


        //뷰랑 대입
        tvAddress.setText(address);
        tvCleanigType.setText(notoolYN.equals("Y") ? "No tool cleaning" : "With tool cleaning");

        tvPerson.setText(String.format("%d Persons", personRequired));

        if(cleaningOrderInfoDetail.cleaningReqQueue != null){
            tvPerson.setText(String.format("%d/%d Persons", cleaningOrderInfoDetail.cleaningReqQueue.personOccupied, cleaningOrderInfoDetail.cleaningReqQueue.personRequired));
        }

        if(cleaningCount > 0){
            vgHowMany.setVisibility(View.VISIBLE);
            tvHowMany.setText(cleaningCount + " Kali");
        }else{
            vgHowMany.setVisibility(View.GONE);
        }

        tvPrice.setText(OkhomeUtil.getPriceFormatValue(price) + " Rupiah");
        tvTags.setText(CleaningQueueItemFragment.getTagStrings(this.getLayoutInflater() ,cleaningOrderInfoDetail.cleaningOrder));

        tvCommentFromCustomer.setText(cleaningOrderInfoDetail.cleaningOrder.commentCustomer);
        tvCommentFromOKhome.setText(cleaningOrderInfoDetail.cleaningOrder.commentOkhome);


        //클리닝 아이템 처리
        final int cleaningSize = cleaningOrderInfoDetail.cleaningOrder.cleaningOrderDayItemModels.size();
        JoViewRepeator cleaningTumbs = new JoViewRepeator<CleaningDayItemModel>(this)
                .setContainer(vgCleaningThumbs)
                .setItemLayoutId(R.layout.item_cleaning_thumb_in_order)
                .setCallBack(new JoRepeatorAdapter<CleaningDayItemModel>() {
                    @Override
                    public void onBind(View v, int pos, final CleaningDayItemModel cleaningOrderDayItem) {
                        TextView tvCleaningOrders = v.findViewById(R.id.itemCleaningThumbInOrder_tvCleaningOrders);
                        TextView tvDuration = v.findViewById(R.id.itemCleaningThumbInOrder_tvDuration);
                        TextView tvNumber = v.findViewById(R.id.itemCleaningThumbInOrder_tvNumber);
                        TextView tvWhen = v.findViewById(R.id.itemCleaningThumbInOrder_tvWhen);
                        View vPaddingBottom = v.findViewById(R.id.itemCleaningThumbInOrder_vPaddingBottom);

                        int durationMin = cleaningOrderDayItem.totalDurationMinute;
                        DateTime dtFirstCleaningDateTime = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.s").parseDateTime(cleaningOrderDayItem.whenDateTime);
                        DateTime dtFirstCleaningDateTimeEnd = dtFirstCleaningDateTime.plusMinutes(durationMin);

                        String whenString = OkhomeDateTimeFormatUtil.printOkhomeType(dtFirstCleaningDateTime.getMillis(), "(E) d MMM yy HH:mm");
                        String whenStringTail = DateTimeFormat.forPattern("HH:mm").print(dtFirstCleaningDateTimeEnd);
                        String when = whenString + "-" + whenStringTail;

                        String durationHour = durationMin / 60 + "." + durationMin % 60 + " Hours";
                        String cleaningDetails = "";

                        if(cleaningOrderDayItem.cleaningOrderSpecifications != null){
                            for(CleaningOrderDaySpecificationModel s : cleaningOrderDayItem.cleaningOrderSpecifications){
                                String nameParsed = CleaningOrderDaySpecificationModel.getCleaningItemName(s.name);
                                cleaningDetails += ", " + nameParsed;
                            }

                            cleaningDetails = cleaningDetails.substring(2);
                        }

                        //세팅
                        tvNumber.setText(pos + 1 + "");
                        tvWhen.setText(when);
                        tvDuration.setText(durationHour);
                        tvCleaningOrders.setText(cleaningDetails);

                        //마지막에는 패딩없음
                        if(pos == cleaningSize - 1){
                            vPaddingBottom.setVisibility(View.GONE);
                        }


                    }
                });

        cleaningTumbs.setList(cleaningOrderInfoDetail.cleaningOrder.cleaningOrderDayItemModels);
        cleaningTumbs.notifyDataSetChanged();
    }



    //acceept
    private void accept(){
        final ProgressDialog p = OkhomeUtil.showLoadingDialog(this);
        OkhomeRestApi.getCleaningOrderQueueClient().acceptCleaningOrderInQueue(cleaningOrderInfoDetail.cleaningReqQueue.queueId, ConsultantLoggedIn.id()).enqueue(new RetrofitCallback<String>() {
            @Override
            public void onSuccess(String result) {
                //ok 팝업
                getCleaningInfo(cleaningOrderInfoDetail.cleaningOrder.orderId);
            }

            @Override
            public void onFinish() {
                p.dismiss();
            }
        });
    }

    //decline
    private void decline(){
        final ProgressDialog p = OkhomeUtil.showLoadingDialog(this);
        OkhomeRestApi.getCleaningOrderQueueClient().rejectCleaningOrderInQueue(cleaningOrderInfoDetail.cleaningReqQueue.queueId, ConsultantLoggedIn.id()).enqueue(new RetrofitCallback<String>() {
            @Override
            public void onSuccess(String result) {
                //ok 팝업
//                OkhomeUtil.showToast(CleaningOrderDetailActivity.this, "Declined");
                finish();
            }

            @Override
            public void onFinish() {
                p.dismiss();
            }
        });
    }

    private void initMap(){
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.actCleaningOrderDetail_fragMap);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Adjust map style
        boolean success = googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style_json));

        // Setup camera and pinpoint icon
        LatLng taskLocation = new LatLng(cleaningOrderInfoDetail.cleaningOrder.homeLat, cleaningOrderInfoDetail.cleaningOrder.homeLng);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(taskLocation , 16);
        googleMap.addMarker(new MarkerOptions()
                .position(taskLocation)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_blue))
        );

        googleMap.moveCamera(cameraUpdate);
        googleMap.getUiSettings().setScrollGesturesEnabled(false);
        googleMap.getUiSettings().setZoomGesturesEnabled(false);
    }

    @OnClick(R.id.actCleaningOrderDetail_vbtnX)
    public void onButtonGoBack() {
        finish();
    }

    @OnClick(R.id.actCleaningOrderDetail_vbtnAccept)
    public void onAccept(){
        new CommonAlertDialog(this, true)
                .setTitle("ACCEPT")
                .setSubTitle("Do you want to accept this order?")
                .setCommonDialogListener(new DialogParent.CommonDialogListener() {
                    @Override
                    public void onCommonDialogWorkDone(Dialog dialog, int actionCode, Map<String, Object> mapResult) {
                        if(actionCode == DialogParent.CommonDialogListener.ACTIONCODE_OK){
                            dialog.dismiss();
                            accept();

                        }
                    }
                }).show();
    }

    @OnClick(R.id.actCleaningOrderDetail_vbtnDecline)
    public void onDecline(){
        new CommonAlertDialog(this, true)
                .setTitle("DECLINE")
                .setSubTitle("Do you want to decline this order?")
                .setCommonDialogListener(new DialogParent.CommonDialogListener() {
                    @Override
                    public void onCommonDialogWorkDone(Dialog dialog, int actionCode, Map<String, Object> mapResult) {
                        if(actionCode == DialogParent.CommonDialogListener.ACTIONCODE_OK){
                            dialog.dismiss();
                            decline();
                        }
                    }
                }).show();
    }

}
