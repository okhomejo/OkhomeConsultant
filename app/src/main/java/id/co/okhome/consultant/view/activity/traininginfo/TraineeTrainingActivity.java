package id.co.okhome.consultant.view.activity.traininginfo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.okhome.consultant.R;
import id.co.okhome.consultant.lib.app.ConsultantLoggedIn;
import id.co.okhome.consultant.lib.app.OkHomeParentActivity;
import id.co.okhome.consultant.lib.app.OkhomeUtil;
import id.co.okhome.consultant.lib.joviewrepeator.JoRepeatorAdapter;
import id.co.okhome.consultant.lib.joviewrepeator.JoViewRepeator;
import id.co.okhome.consultant.lib.retrofit.RetrofitCallback;
import id.co.okhome.consultant.model.FaqModel;
import id.co.okhome.consultant.model.training.TrainingAttendanceForTraineeModel;
import id.co.okhome.consultant.model.training.TrainingItemModel;
import id.co.okhome.consultant.model.training.TrainingModel;
import id.co.okhome.consultant.rest_apicall.retrofit_restapi.OkhomeRestApi;
import id.co.okhome.consultant.view.activity.faq.FaqSearchResultActivity;
import id.co.okhome.consultant.view.activity.faq.FaqSingleActivity;

public class TraineeTrainingActivity extends OkHomeParentActivity {

    @BindView(R.id.actTrainingInfo_vLoading)                View vLoading;
    @BindView(R.id.actTrainingInfo_svItem)                  ScrollView svItem;
    @BindView(R.id.actTrainingInfo_tvTrainingWhere)         TextView tvTrainingWhere;
    @BindView(R.id.actTrainingInfo_tvTrainingWhen)          TextView tvTrainingWhen;
    @BindView(R.id.actTrainingInfo_tvTrainerName)           TextView tvTrainerName;
    @BindView(R.id.actTrainingInfo_tvTrainerInfo)           TextView tvTrainerInfo;
    @BindView(R.id.actTrainingInfo_tvTrainingInfo)          TextView tvTrainingInfo;
    @BindView(R.id.actTrainingInfo_ivTrainerPhoto)          ImageView ivProfileImage;

    @BindView(R.id.actTrainingInfo_vbtnManual)              ViewGroup vbtnManual;
    @BindView(R.id.actTrainingInfo_vgTrainerInfo)           ViewGroup vgTrainerInfo;
    @BindView(R.id.actTrainingInfo_vgTrainingTypeB)         ViewGroup vgTrainingTypeB;
    @BindView(R.id.actTrainingInfo_vgTrainingTypeC)         ViewGroup vgTrainingTypeC;
    @BindView(R.id.actTrainingInfo_vgTrainingTypeBItems)    ViewGroup vgTrainingTypeBItems;
    @BindView(R.id.actTrainingInfo_vgTrainingTypeCItems)    ViewGroup vgTrainingTypeCItems;

    JoViewRepeator<TrainingItemModel> trainingItemTypeBRepeator = null;
    JoViewRepeator<TrainingItemModel> trainingItemTypeCRepeator = null;

    String trainingId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traininginfo);
        ButterKnife.bind(this);
        OkhomeUtil.setWhiteSystembar(this);

        trainingId = getIntent().getStringExtra("trainingId");

        init();

        svItem.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getTrainingInfo();
    }

    private void init(){
        trainingItemTypeBRepeator = new JoViewRepeator<TrainingItemModel>(this)
                .setContainer(vgTrainingTypeBItems)
                .setItemLayoutId(R.layout.item_trainingpage_item)
                .setCallBack(new TraineeTrainingItemTypeAdapter("B"));

        trainingItemTypeCRepeator = new JoViewRepeator<TrainingItemModel>(this)
                .setContainer(vgTrainingTypeCItems)
                .setItemLayoutId(R.layout.item_trainingpage_item)
                .setCallBack(new TraineeTrainingItemTypeAdapter("C"));
    }

    //view <-> model async
    private void adaptTrainingViewAndData(final TrainingModel training){
        //init data.
        final TrainingAttendanceForTraineeModel attendance = training.trainingAttendanceForTrainee;
        tvTrainingInfo.setText(training.desc);

        if (attendance != null) {
            Glide.with(TraineeTrainingActivity.this).load(attendance.trainerPhotoUrl).thumbnail(0.5f).into(ivProfileImage);
            tvTrainerName.setText(attendance.trainerName);

            String gender = "Unknown";
            if (attendance.trainerGender != null) {
                if (attendance.trainerGender.equals("M")) {
                    gender = "Male";
                } else if (attendance.trainerGender.equals("F")) {
                    gender = "Female";
                }
            }
            tvTrainerInfo.setText(String.format("Consultant, %s", gender));

            DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
            DateTime dt = formatter.parseDateTime(attendance.trainingWhen);
            tvTrainingWhen.setText(String.format("Training on %s", dt.toString("dd MMM yy, hh:mm")));
            tvTrainingWhere.setText(attendance.placeName);
            tvTrainingWhere.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Uri gmmIntentUri = Uri.parse(
                            String.format("geo:%s,%s?q=%s",
                                    attendance.placeGpsLat,
                                    attendance.placeGptLng,
                                    attendance.placeName
                            )
                    );
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    startActivity(mapIntent);
                }
            });
        }
        if (training.faqHotkey != null) {
            vbtnManual.setVisibility(View.VISIBLE);
            vbtnManual.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getFaqByHotKey(training.faqHotkey);
                }
            });
        }
        //adapt training items
        adaptTrainingItems(training.type, training.listTrainingItemModel);
    }

    //adapt typeB trainings
    private void adaptTrainingItems(String type, List<TrainingItemModel> listTrainingItem){
        vgTrainingTypeB.setVisibility(View.GONE);
        vgTrainingTypeC.setVisibility(View.GONE);
        if(type.equals("B")){
            vgTrainingTypeB.setVisibility(View.VISIBLE);
            trainingItemTypeBRepeator.setList(listTrainingItem);
            trainingItemTypeBRepeator.notifyDataSetChanged();
        }else if(type.equals("C")){
            vgTrainingTypeC.setVisibility(View.VISIBLE);
            trainingItemTypeCRepeator.setList(listTrainingItem);
            trainingItemTypeCRepeator.notifyDataSetChanged();
        }
    }

    //pull training detail info
    private void getTrainingInfo(){
        vLoading.setVisibility(View.VISIBLE);

        OkhomeRestApi.getTrainingForTraineeClient().getTrainingDetail(ConsultantLoggedIn.id(), trainingId).enqueue(new RetrofitCallback<TrainingModel>() {
            @Override
            public void onSuccess(TrainingModel training) {
                adaptTrainingViewAndData(training);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                svItem.setVisibility(View.VISIBLE);
                vLoading.setVisibility(View.GONE);
            }
        });
    }

    //pull faq detail info
    private void getFaqByHotKey(final String faqKey){
        vLoading.setVisibility(View.VISIBLE);
        OkhomeRestApi.getCommonClient().getFaqByHotkey(faqKey).enqueue(new RetrofitCallback<List<FaqModel>>() {
            @Override
            public void onSuccess(List<FaqModel> faqModels) {
                openFaqResults(faqModels, faqKey);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                vLoading.setVisibility(View.GONE);
            }
        });
    }

    private void openFaqResults(List<FaqModel> faqModels, String faqKey) {
        if (faqModels.size() > 1) {
            Intent faqActivity = new Intent(this, FaqSearchResultActivity.class);
            faqActivity.putExtra("FAQ_HOT_KEY", faqKey);
            startActivity(faqActivity);
        } else {
            Intent singleFaq = new Intent(this, FaqSingleActivity.class);
            singleFaq.putExtra("FAQ_ID", faqModels.get(0).id);
            startActivity(singleFaq);
        }
    }

    @OnClick(R.id.actTrainingInfo_vbtnX)
    public void onBackButtonClicked() {
        finish();
    }

    //training item adapter
    class TraineeTrainingItemTypeAdapter extends JoRepeatorAdapter<TrainingItemModel> {

        @BindView(R.id.itemTrainingPageItem_tvName) TextView tvName;

        String type;

        public TraineeTrainingItemTypeAdapter(String type) {
            this.type = type;
        }

        @Override
        public void onBind(View v, final int pos, final TrainingItemModel trainingItem) {
            ButterKnife.bind(this, v);
            tvName.setText(trainingItem.subject);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(TraineeTrainingActivity.this, TraineeTrainingItemInfoActivity.class)
                            .putExtra("type", type)
                            .putExtra("itemId", trainingItem.id)
                            .putExtra("trainingId", trainingItem.trainingId)
                    );
                }
            });
        }
    }
}