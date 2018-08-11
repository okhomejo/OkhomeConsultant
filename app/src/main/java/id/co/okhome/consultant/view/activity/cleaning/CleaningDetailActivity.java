package id.co.okhome.consultant.view.activity.cleaning;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

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
import id.co.okhome.consultant.lib.retrofit.restmodel.ErrorModel;
import id.co.okhome.consultant.model.cleaning.CleaningDetailWithOrderInfoModel;
import id.co.okhome.consultant.model.cleaning.order.CleaningDayItemModel;
import id.co.okhome.consultant.model.cleaning.order.CleaningOrderDaySpecificationModel;
import id.co.okhome.consultant.model.cleaning.order.CleaningOrderModel;
import id.co.okhome.consultant.rest_apicall.retrofit_restapi.OkhomeRestApi;
import id.co.okhome.consultant.view.dialog.CommonAlertDialog;
import id.co.okhome.consultant.view.fragment.cleaning_queue.CleaningQueueItemFragment;

public class CleaningDetailActivity extends OkHomeParentActivity implements OnMapReadyCallback {

    @BindView(R.id.actCleaningDetail_svItem)            ScrollView svItem;
    @BindView(R.id.actCleaningDetail_vLoading)          View vLoading;

    @BindView(R.id.actCleaningDetail_vgMap)                     ViewGroup vgMap;
    @BindView(R.id.actCleaningDetail_vgPrice2)                  ViewGroup vgPrice2;
    @BindView(R.id.actCleaningDetail_tvAddress)                 TextView tvAddress;
    @BindView(R.id.actCleaningDetail_tvPerson)                  TextView tvPerson;
    @BindView(R.id.actCleaningDetail_tvPrice)                   TextView tvPrice;
    @BindView(R.id.actCleaningDetail_tvPrice2)                   TextView tvPrice2;

    @BindView(R.id.actCleaningDetail_tvTags)                    TextView tvTags;
    @BindView(R.id.actCleaningDetail_tvWhen)                    TextView tvWhen;
    @BindView(R.id.actCleaningDetail_vgCleaningSpecification)   ViewGroup vgCleaningSpecification;
    @BindView(R.id.actCleaningDetail_tvCleanigType)             TextView tvCleanigType;
    @BindView(R.id.actCleaningDetail_tvCommentFromCustomer)     TextView tvCommentFromCustomer;
    @BindView(R.id.actCleaningDetail_tvCommentFromOkhome)       TextView tvCommentFromOKhome;
    @BindView(R.id.actCleaningDetail_tvTotalDuration)           TextView tvTotalDuration;

    @BindView(R.id.actCleaningDetail_vgMornitor)                ViewGroup vgMornitor;
    @BindView(R.id.actCleaningDetail_tvMornitorHour)            TextView tvMornitorHour;
    @BindView(R.id.actCleaningDetail_tvMornitorMin)             TextView tvMornitorMin;
    @BindView(R.id.actCleaningDetail_tvMornitorSec)             TextView tvMornitorSec;
    @BindView(R.id.actCleaningDetail_tvMornitorTail)            TextView tvMornitorTail;
    @BindView(R.id.actCleaningDetail_tvId)                      TextView tvId;

    @BindView(R.id.actCleaningDetail_tvReview)                  TextView tvReview;
    @BindView(R.id.actCleaningDetail_tvReviewScore)             TextView tvReviewScore;
    @BindView(R.id.actCleaningDetail_vgReviewItem)              ViewGroup vgReviewItem;
    @BindView(R.id.actCleaningDetail_vgStars)                   ViewGroup vgReviewStars;
    @BindView(R.id.actCleaningDetail_vgStarItems)               ViewGroup vgStarItems;

    @BindView(R.id.actCleaningDetail_vgBeginCleaning)           ViewGroup vgBeginCleaning;
    @BindView(R.id.actCleaningDetail_vgFinishCleaning)          ViewGroup vgFinishCleaning;
    @BindView(R.id.actCleaningDetail_vPaddingBelowMap)          View vPaddingBelowMap;


    String hourBefore = "", minBefore = "", secBefore = "";

    Timer timer = null;

    public final static void start(Context context, int cleaningId){
        context.startActivity(new Intent(context, CleaningDetailActivity.class).putExtra("cleaningId", cleaningId));
    }

    CleaningOrderModel cleaningOrder = null;
    CleaningDayItemModel cleaning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cleaning_detail_info);
        OkhomeUtil.setWhiteSystembar(this);

        ButterKnife.bind(this);
        setActionButtonDefault();
    }

    @Override
    protected void onResume() {
        super.onResume();

        int cleaningId = getIntent().getIntExtra("cleaningId", 0);
        if(cleaningId == 0){
            finish();
            return;
        }

        getCleaningInfo(cleaningId);
    }

    //로딩
    private void loading(boolean loading){
        if(loading){
            vgMornitor.setVisibility(View.GONE);
            vLoading.setVisibility(View.VISIBLE);
            svItem.setVisibility(View.INVISIBLE);
        }else{
            vgMornitor.setVisibility(View.VISIBLE);
            vLoading.setVisibility(View.GONE);
            svItem.setVisibility(View.VISIBLE);
        }

    }

    //타이머 시작
    private void startTimer(){
        if(timer == null){
            timer = new Timer();
        }else{
            timer.cancel();
            timer.purge();
            timer = new Timer();
        }

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                CleaningDetailActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        boolean continueWork = adaptDataByTimer();
                        if(!continueWork){
                            timer.purge();
                            timer.cancel();
                        }

                        loading(false);
                    }
                });

            }
        }, 0, 1000);
    }

    //새로고침
    private void refresh(){
        getCleaningInfo(cleaning.cleaningId);
    }

    //청소 정보 가져오기
    private void getCleaningInfo(int cleaningId){
        loading(true);
        OkhomeRestApi.getCleaningTaskClient().getCleaningDetailWithOrderDetail(cleaningId, ConsultantLoggedIn.id()).enqueue(new RetrofitCallback<CleaningDetailWithOrderInfoModel>() {
            @Override
            public void onSuccess(CleaningDetailWithOrderInfoModel cleaningDetail) {
                cleaning = cleaningDetail.cleaning;
                cleaningOrder =  cleaningDetail.cleaningOrder;
                startTimer();
                adaptData();


            }

            @Override
            public void onJodevError(ErrorModel jodevErrorModel) {
                super.onJodevError(jodevErrorModel);

                vLoading.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }
        });
    }

    //타이머에 따른 버튼액션 기본ㄱ밧
    private void setActionButtonDefault(){
        vgReviewItem.setVisibility(View.GONE);
        vPaddingBelowMap.setVisibility(View.GONE);
        vgBeginCleaning.setVisibility(View.GONE);
        vgFinishCleaning.setVisibility(View.GONE);
        vgMornitor.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPurpleBlack));
        vgPrice2.setVisibility(View.GONE);
    }

    private boolean adaptDataByTimer(){
        setActionButtonDefault();

        tvMornitorHour.setText("--");
        tvMornitorMin.setText("--");
        tvMornitorSec.setText("--");

        DateTime nowDateTime = new DateTime(OkhomeUtil.getCurrentJarartaTimeMills());
        DateTime cleaningScheduledDateTime = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.s").parseDateTime(cleaning.whenDateTime);
        DateTime cleaningRealBeginDateTime = null;
        if(cleaning.realBeginDateTime != null){
            cleaningRealBeginDateTime = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.s").parseDateTime(cleaning.realBeginDateTime);
        }


        //0 청소 완료
        if(cleaning.realEndDateTime != null){
            //청소 완료됬음.
            onCleaningFinish();
            return false;
        }

        //1. 청소 시작전.
        else if(cleaning.realBeginDateTime == null){

            //1-1청소 예정 시간보다 남았을경우.
            if(nowDateTime.getMillis() < cleaningScheduledDateTime.getMillis()){
                //걍 타이머 처리
                showSecDiff(nowDateTime.getMillis() - cleaningScheduledDateTime.getMillis());
                onCleaningReadyNormalEverySec();

                //30분전부터 시작버튼 활성화됨.
                long diff = Math.abs(cleaningScheduledDateTime.getMillis() - nowDateTime.getMillis());
                if(diff < 1000 * 60 * 30){
                    on30MinBeforeCleaning();
                }
            }
            //1-2청소 예정 시간 넘었을 경우
            else{
                //청소 시작 버튼을 누르세요 버튼 활성화.
                showSecDiff(nowDateTime.getMillis() - cleaningScheduledDateTime.getMillis());
                onCleaningReadyButOverSchedule();
            }
            return true;
        }
        //2. 청소 시작후
        else if(cleaning.realBeginDateTime != null){
            //청소 지난 타이머 보이기.
            DateTime cleaningEndTime = new DateTime(cleaningRealBeginDateTime.getMillis()).plusMinutes(cleaning.totalDurationMinute);

            if(nowDateTime.getMillis() > cleaningEndTime.getMillis()){
                showSecDiff(nowDateTime.getMillis() - cleaningEndTime.getMillis());
                onCleaningButOverSchedule();
            }else{
                showSecDiff(cleaningEndTime.getMillis() - nowDateTime.getMillis());
                onCleaning();
                //청소 완료 30분전이면
                long diff = Math.abs(cleaningEndTime.getMillis() - nowDateTime.getMillis());
                if(diff < 1000 * 60 * 30){
                    on30MinBeforeFinish();
                }

            }
            return true;
        }else{
            onCleaningElse();
            return false;
        }
    }

    //예외상황.
    private void onCleaningElse(){
        tvMornitorTail.setText("Err");
        //
    }

    //청소 완료상태.
    private void onCleaningFinish(){
        tvMornitorTail.setText("Cleaning finished");
        vgMornitor.setBackgroundColor(ContextCompat.getColor(this, R.color.colorCalendarRed));

        vgMap.setVisibility(View.GONE);
        vgPrice2.setVisibility(View.VISIBLE);
        vPaddingBelowMap.setVisibility(View.VISIBLE);
        vgReviewItem.setVisibility(View.VISIBLE);

        if(cleaning.grade > 0){
            //리뷰완료됨.
            tvReview.setText(cleaning.review);
            tvReviewScore.setText(cleaning.grade +"");
            //별점..
            for(int i = 0; i < vgStarItems.getChildCount(); i++){
                View v = vgStarItems.getChildAt(i);
                ImageView ivStar = (ImageView)v;

                ivStar.setImageResource(R.drawable.ic_star_off);
                if(cleaning.grade > i){
                    ivStar.setImageResource(R.drawable.ic_star_on);
                }
            }
        }else{
            //리뷰 안됨.
            tvReview.setText("(Review has not been written yet)");
            tvReviewScore.setText("0.0");
        }

    }

    //청소 30분전
    private void on30MinBeforeCleaning(){
        tvMornitorTail.setText("Before cleaning");
        vgBeginCleaning.setVisibility(View.VISIBLE);
    }

    //클리닝 대기중.
    private void onCleaningReadyNormalEverySec(){
        tvMornitorTail.setText("Before cleaning");
    }

    //클리닝 대기중. 근데 예정시간 초과되었음.
    private void onCleaningReadyButOverSchedule(){
        vgBeginCleaning.setVisibility(View.VISIBLE);
        tvMornitorTail.setText("Behind schedule");
        //주의사항 보여줘야겠네.
    }

    //청소중
    private void onCleaning(){
        vgMornitor.setBackgroundColor(ContextCompat.getColor(this, R.color.colorOkhomeLight));
        tvMornitorTail.setText("Remaining");
    }

    //청소중 근데 스케쥴 오버댐.
    private void onCleaningButOverSchedule(){
        vgMornitor.setBackgroundColor(ContextCompat.getColor(this, R.color.colorCalendarRed));
        tvMornitorTail.setText("Overtime work");
        vgFinishCleaning.setVisibility(View.VISIBLE);
    }

    //청소중
    private void on30MinBeforeFinish(){
        vgFinishCleaning.setVisibility(View.VISIBLE);
    }

    //시간 차이 모니터
    private void showSecDiff(long millDiff){
        long diff = Math.abs(millDiff);
        int sec = (int)(diff / 1000) % 60;
        int minute = (int)(diff / 1000 / 60) % 60;
        int hour = (int)(diff / 1000 / 60 / 60);

        String sHour = OkhomeUtil.getFull2Decimal(hour);
        String sMin = OkhomeUtil.getFull2Decimal(minute);
        String sSec = OkhomeUtil.getFull2Decimal(sec);

        tvMornitorHour.setText(sHour);
        tvMornitorMin.setText(sMin);
        tvMornitorSec.setText(sSec);

        //애니메이션 처리
        if(!hourBefore.equals(sHour)){
            startDecimalTranslateAnim(tvMornitorHour);
        }
        if(!minBefore.equals(sMin)){
            startDecimalTranslateAnim(tvMornitorMin);
        }
        if(!secBefore.equals(sSec)){
            startDecimalTranslateAnim(tvMornitorSec);
        }

        hourBefore = sHour;
        minBefore = sMin;
        secBefore = sSec;
    }

    //데이터 연결
    private void adaptData() {
        initMap();
        //처리하자...

        int price = cleaning.priceConsultant;
        int cleaningCount = cleaningOrder.cleaningCount;
        int personRequired = cleaningOrder.personRequired;
        String notoolYN = cleaning.notoolYN;
        String address = cleaningOrder.homeAddress;

        int durationMin = cleaning.totalDurationMinute;
        String durationHour = durationMin / 60 + "." + durationMin % 60 + " Hours";

        DateTime dtFirstCleaningDateTime = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.s").parseDateTime(cleaning.whenDateTime);
        DateTime dtFirstCleaningDateTimeEnd = dtFirstCleaningDateTime.plusMinutes(durationMin);

        String whenString = OkhomeDateTimeFormatUtil.printOkhomeType(dtFirstCleaningDateTime.getMillis(), "(E) d MMM yy HH:mm");
        String whenStringTail = DateTimeFormat.forPattern("HH:mm").print(dtFirstCleaningDateTimeEnd);
        String when = whenString + "-" + whenStringTail;
        String priceString = OkhomeUtil.getPriceFormatValue(price) + " Rupiah";

//        리뷰

        //뷰랑 대입

        tvTotalDuration.setText(durationHour);
        tvWhen.setText(when);
        tvAddress.setText(address);
        tvCleanigType.setText(notoolYN.equals("Y") ? "No tool cleaning" : "With tool cleaning");

        tvPerson.setText(String.format("%d Persons", personRequired));

        tvPrice2.setText(priceString);
        tvPrice.setText(priceString);
        tvTags.setText(CleaningQueueItemFragment.getTagStrings(this.getLayoutInflater() ,cleaningOrder));
        tvId.setText("OKC_" + cleaning.cleaningId);

        tvCommentFromCustomer.setText(cleaningOrder.commentCustomer);
        tvCommentFromOKhome.setText(cleaningOrder.commentOkhome);

        //남은시간 처리

        //클리닝 아이템 처리
        final int cleaningSize = cleaning.cleaningOrderSpecifications.size();
        JoViewRepeator cleaningSpecificationViewRepeator = new JoViewRepeator<CleaningDayItemModel>(this)
                .setContainer(vgCleaningSpecification)
                .setItemLayoutId(R.layout.item_cleaning_specification)
                .setCallBack(new JoRepeatorAdapter<CleaningOrderDaySpecificationModel>() {
                    @Override
                    public void onBind(View v, int pos, CleaningOrderDaySpecificationModel cleaningSpecification) {
                        TextView tvDuration = v.findViewById(R.id.itemCleaningSpecification_tvDuration);
                        TextView tvNumber = v.findViewById(R.id.itemCleaningSpecification_tvNumber);
                        TextView tvSubject = v.findViewById(R.id.itemCleaningSpecification_tvSubject);
                        View vPaddingBottom = v.findViewById(R.id.itemCleaningSpecification_vPaddingBottom);

                        int durationMin = cleaningSpecification.durationMin;
                        String durationHour = durationMin / 60 + "." + durationMin % 60 + " Hours";

                        //
                        tvDuration.setText(durationHour);
                        tvSubject.setText(CleaningOrderDaySpecificationModel.getCleaningItemName(cleaningSpecification.name));
                        tvNumber.setText(pos+1+"");

                        if(pos == cleaningSize - 1){
                            vPaddingBottom.setVisibility(View.GONE);
                        }
                    }
                });

        cleaningSpecificationViewRepeator.setList(cleaning.cleaningOrderSpecifications);
        cleaningSpecificationViewRepeator.notifyDataSetChanged();
    }

    //청소 시작
    private void notifyCleaningBegin(){
        final ProgressDialog p = OkhomeUtil.showLoadingDialog(this);
        OkhomeRestApi.getCleaningTaskClient().notifyBeginCleaning(ConsultantLoggedIn.id(), cleaning.cleaningId).enqueue(new RetrofitCallback<String>() {
            @Override
            public void onSuccess(String result) {
                refresh();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                p.dismiss();
            }
        });
    }

    //청소 종료
    private void notifyCleaningEnd(){
        final ProgressDialog p = OkhomeUtil.showLoadingDialog(this);
        OkhomeRestApi.getCleaningTaskClient().notifyEndCleaning(ConsultantLoggedIn.id(), cleaning.cleaningId).enqueue(new RetrofitCallback<String>() {
            @Override
            public void onSuccess(String result) {
                refresh();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                p.dismiss();
            }
        });
    }

    private void initMap(){
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.actCleaningDetail_fragMap);
        mapFragment.getMapAsync(this);
    }

    private void startDecimalTranslateAnim(View v){
        v.animate().translationY(-30).alpha(0f).setDuration(0).start();
        v.animate().translationY(0).alpha(1f).setDuration(100).start();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Adjust map style
        boolean success = googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style_json));

        // Setup camera and pinpoint icon
        LatLng taskLocation = new LatLng(cleaningOrder.homeLat, cleaningOrder.homeLng);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(taskLocation , 16);
        googleMap.addMarker(new MarkerOptions()
                .position(taskLocation)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_blue))
        );

        googleMap.moveCamera(cameraUpdate);
        googleMap.getUiSettings().setScrollGesturesEnabled(false);
        googleMap.getUiSettings().setZoomGesturesEnabled(false);
    }

    @OnClick(R.id.actCleaningDetail_vbtnX)
    public void onButtonGoBack() {
        finish();
    }

    @OnClick(R.id.actCleaningDetail_vgBeginCleaning)
    public void onClickBeginCleaning(View v){
        new CommonAlertDialog(this, true).setTitle("OKHOME").setSubTitle("Are you ready to start cleaning?").setCommonDialogListener(new DialogParent.CommonDialogListener() {
            @Override
            public void onCommonDialogWorkDone(Dialog dialog, int actionCode, Map<String, Object> mapResult) {
                if(actionCode == DialogParent.CommonDialogListener.ACTIONCODE_OK){
                    notifyCleaningBegin();
                }

            }
        }).show();
    }

    @OnClick(R.id.actCleaningDetail_vgFinishCleaning)
    public void onClickFinishCleaning(View v){
        new CommonAlertDialog(this, true).setTitle("OKHOME").setSubTitle("Is the cleaning complete?").setCommonDialogListener(new DialogParent.CommonDialogListener() {
            @Override
            public void onCommonDialogWorkDone(Dialog dialog, int actionCode, Map<String, Object> mapResult) {
                if(actionCode == DialogParent.CommonDialogListener.ACTIONCODE_OK){
                    notifyCleaningEnd();
                }

            }
        }).show();
    }


}
