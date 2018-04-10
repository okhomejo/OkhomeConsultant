package id.co.okhome.consultant.view.activity.traininginfo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

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
import id.co.okhome.consultant.model.training.TrainingItemChildModel;
import id.co.okhome.consultant.model.training.TrainingItemModel;
import id.co.okhome.consultant.model.training.TrainingModel;
import id.co.okhome.consultant.rest_apicall.retrofit_restapi.OkhomeRestApi;
import id.co.okhome.consultant.view.activity.faq.FaqSearchResultActivity;
import id.co.okhome.consultant.view.activity.faq.FaqSingleActivity;

public class TraineeTrainingItemInfoActivity extends OkHomeParentActivity {

    @BindView(R.id.actTrainingItemInfo_vLoading)            View vLoading;
    @BindView(R.id.actTrainingInfo_tvEvalTitle)             TextView tvEvalTitle;
    @BindView(R.id.actTrainingInfo_tvCommentTrainer)        TextView tvCommentTrainer;
    @BindView(R.id.actTrainingInfo_tvEvalTrainerName)       TextView tvEvalTrainerName;
    @BindView(R.id.actTrainingInfo_tvTitle)                 TextView tvTitle;
    @BindView(R.id.actTrainingInfo_vbtnManual)              ViewGroup vbtnManual;
    @BindView(R.id.actTrainingInfo_vgTrainingTypeBItems)    ViewGroup vgTrainingTypeB;
    @BindView(R.id.actTrainingInfo_vgTrainingTypeCItems)    ViewGroup vgTrainingTypeC;
    @BindView(R.id.actTrainingInfo_vgComment)               LinearLayout vgComment;
    @BindView(R.id.actTrainingInfo_svItem)                  ScrollView svItem;

    @BindView(R.id.fragmentIntro1_ivUserPhoto)              ImageView ivUserPhoto;
    @BindView(R.id.itemChat_tvChat)                         TextView tvComment;

    private String trainingId = null, itemId = null, listType = null;
    JoViewRepeator<TrainingItemChildModel> trainingItemTypeBRepeater = null;
    JoViewRepeator<TrainingItemChildModel> trainingItemTypeCRepeater = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainee_training_item_info);
        ButterKnife.bind(this);
        OkhomeUtil.setWhiteSystembar(this);

        listType    = getIntent().getStringExtra("type");
        trainingId  = String.valueOf(getIntent().getLongExtra("trainingId", 0));
        itemId      = String.valueOf(getIntent().getLongExtra("itemId", 0));

        init();

        svItem.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getTrainingInfo();
    }

    private void init(){
        trainingItemTypeBRepeater = new JoViewRepeator<TrainingItemChildModel>(this)
                .setContainer(vgTrainingTypeB)
                .setItemLayoutId(R.layout.item_trainingpage_item_child)
                .setCallBack(new TraineeTrainingItemInfoActivity.TraineeTrainingChildItemTypeAdapter("B"));

        trainingItemTypeCRepeater = new JoViewRepeator<TrainingItemChildModel>(this)
                .setContainer(vgTrainingTypeC)
                .setItemLayoutId(R.layout.item_trainingpage_item_child)
                .setCallBack(new TraineeTrainingItemInfoActivity.TraineeTrainingChildItemTypeAdapter("C"));
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

    private void adaptTrainingViewAndData(final TrainingModel training){

        TrainingAttendanceForTraineeModel attendance = training.trainingAttendanceForTrainee;
        Glide.with(this).load(attendance.trainerPhotoUrl).thumbnail(0.5f).into(ivUserPhoto);
        tvCommentTrainer.setText(String.format("Comment from trainer %s", attendance.trainerName));
        tvEvalTrainerName.setText(String.format("Your score is evaluated by trainer %s.", attendance.trainerName));

        if (training.faqHotkey != null) {
            vbtnManual.setVisibility(View.VISIBLE);
            vbtnManual.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getFaqByHotKey(training.faqHotkey);
                }
            });
        }

        for (TrainingItemModel item : training.listTrainingItemModel) {
            if (Objects.equals(String.valueOf(item.id), itemId)) {
                tvTitle.setText(String.format("%s : %s", training.subject, item.subject));

                tvEvalTitle.setText(String.format("Evaluation : %s", item.subject));

                if (item.trainingResult != null) {
                    tvComment.setText(item.trainingResult.trainerComment);
                    vgComment.setVisibility(View.VISIBLE);
                }
                adaptTrainingItems(item.listTrainingItemChild);

                break;
            }
        }
    }

    private void adaptTrainingItems(List<TrainingItemChildModel> listTrainingItem) {
        vgTrainingTypeB.setVisibility(View.GONE);
        vgTrainingTypeC.setVisibility(View.GONE);
        if (listType.equals("B")) {
            vgTrainingTypeB.setVisibility(View.VISIBLE);
            trainingItemTypeBRepeater.setList(listTrainingItem);
            trainingItemTypeBRepeater.notifyDataSetChanged();
        } else if (listType.equals("C")) {
            vgTrainingTypeC.setVisibility(View.VISIBLE);
            trainingItemTypeCRepeater.setList(listTrainingItem);
            trainingItemTypeCRepeater.notifyDataSetChanged();
        }
    }

    // fetch faq detail info
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
    class TraineeTrainingChildItemTypeAdapter extends JoRepeatorAdapter<TrainingItemChildModel> {

        @BindView(R.id.itemTrainingPageItemChild_tvNo)          TextView tvNo;
        @BindView(R.id.itemTrainingPageItemChild_tvItem)        TextView tvTitle;
        @BindView(R.id.itemTrainingPageItemChild_tvStatusLMH)   TextView tvStatus;

        @BindView(R.id.itemTrainingPageItemChild_vgSuccecss)    View vSuccess;
        @BindView(R.id.itemTrainingPageItemChild_vgFailed)      View vFailed;
        @BindView(R.id.itemTrainingPageItemChild_vgRightResult) View vRightResult;

        private String type;

        public TraineeTrainingChildItemTypeAdapter(String type) {
            this.type = type;
        }

        @Override
        public void onBind(View v, final int pos, final TrainingItemChildModel trainingItem) {
            ButterKnife.bind(this, v);
            tvTitle.setText(trainingItem.contents);

            tvNo.setText(String.format(Locale.ENGLISH, "%d", pos + 1));
            vRightResult.setVisibility(View.GONE);
            vSuccess.setVisibility(View.GONE);
            vFailed.setVisibility(View.GONE);

            if (type.equals("B")) {
                if (trainingItem.trainingResult != null) {
                    String result = trainingItem.trainingResult.traininigResult;
                    if (result.equals("S")) {
                        vSuccess.setVisibility(View.VISIBLE);
                    } else if (result.equals("F")) {
                        vFailed.setVisibility(View.VISIBLE);
                    }

                    vRightResult.setVisibility(View.VISIBLE);
                }
            } else if (type.equals("C")) {
                v.setVisibility(View.VISIBLE);
                if (trainingItem.trainingResult != null) {
                    tvStatus.setText(trainingItem.trainingResult.traininigResult);
                    tvStatus.setVisibility(View.VISIBLE);

                    vRightResult.setVisibility(View.VISIBLE);
                }
            }
        }
    }
}