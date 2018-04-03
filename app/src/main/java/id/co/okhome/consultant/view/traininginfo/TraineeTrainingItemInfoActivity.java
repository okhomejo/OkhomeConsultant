package id.co.okhome.consultant.view.traininginfo;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.okhome.consultant.R;
import id.co.okhome.consultant.lib.app.ConsultantLoggedIn;
import id.co.okhome.consultant.lib.app.OkHomeParentActivity;
import id.co.okhome.consultant.lib.joviewrepeator.JoRepeatorAdapter;
import id.co.okhome.consultant.lib.joviewrepeator.JoViewRepeator;
import id.co.okhome.consultant.lib.retrofit.RetrofitCallback;
import id.co.okhome.consultant.model.training.TrainingItemChildModel;
import id.co.okhome.consultant.model.training.TrainingItemModel;
import id.co.okhome.consultant.model.training.TrainingModel;
import id.co.okhome.consultant.model.v2.AccountModel;
import id.co.okhome.consultant.rest_apicall.retrofit_restapi.OkhomeRestApi;

public class TraineeTrainingItemInfoActivity extends OkHomeParentActivity {

    @BindView(R.id.actTrainingItemInfo_vLoading)            View vLoading;
    @BindView(R.id.actTrainingInfo_tvEvalTitle)             TextView tvEvalTitle;
    @BindView(R.id.actTrainingInfo_tvCommentTrainer)        TextView tvCommentTrainer;
    @BindView(R.id.actTrainingInfo_tvEvalTrainerName)       TextView tvEvalTrainerName;
    @BindView(R.id.actTrainingInfo_tvTitle)                 TextView tvTitle;
    @BindView(R.id.actTrainingInfo_vgComment)               LinearLayout vgComment;
    @BindView(R.id.actTrainingInfo_vgTrainingTypeBItems)    ViewGroup vgTrainingTypeB;
    @BindView(R.id.actTrainingInfo_svItem)                  ScrollView svItem;
    @BindView(R.id.fragmentIntro1_ivUserPhoto)              ImageView ivUserPhoto;
    @BindView(R.id.itemChat_tvChat)                         TextView tvComment;

    private String trainingId = null, itemId = null, listType = null;
    JoViewRepeator<TrainingItemChildModel> trainingItemTypeRepeater = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainee_training_item_info);

        ButterKnife.bind(this);

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
        trainingItemTypeRepeater = new JoViewRepeator<TrainingItemChildModel>(this)
                .setContainer(vgTrainingTypeB)
                .setItemLayoutId(R.layout.item_trainingpage_item_child)
                .setCallBack(new TraineeTrainingItemInfoActivity.TraineeTrainingChildItemTypeAdapter(listType));
    }

    //pull training detail info
    private void getTrainingInfo(){
        vLoading.setVisibility(View.VISIBLE);

        OkhomeRestApi.getTrainingForTraineeClient().getTrainingDetail(ConsultantLoggedIn.id(), trainingId).enqueue(new RetrofitCallback<TrainingModel>() {
            @Override
            public void onSuccess(TrainingModel training) {
                for (TrainingItemModel item : training.listTrainingItemModel) {
                    if (Objects.equals(String.valueOf(item.id), itemId)) {
                        tvTitle.setText(String.format("%s : %s", training.subject, item.subject));
                        adaptTrainingViewAndData(item);
                        getTrainerAccountInfo(training.trainingAttendanceForTrainee.trainerId);
                        break;
                    }
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                vLoading.setVisibility(View.GONE);
            }
        });
    }

    private void adaptTrainingViewAndData(TrainingItemModel item){
        tvEvalTitle.setText(String.format("Evaluation : %s", item.subject));
        if (item.trainingResult != null) {
            tvComment.setText(item.trainingResult.trainerComment);
            vgComment.setVisibility(View.VISIBLE);
        }
        adaptTrainingItems(item.listTrainingItemChild);
    }

    private void getTrainerAccountInfo(int trainerId){
        vLoading.setVisibility(View.VISIBLE);

        OkhomeRestApi.getAccountClient().getInfo(trainerId).enqueue(new RetrofitCallback<AccountModel>() {
            @Override
            public void onSuccess(AccountModel trainer) {
                svItem.setVisibility(View.VISIBLE);
                adaptTrainerAccountView(trainer);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                vLoading.setVisibility(View.GONE);
            }
        });
    }

    private void adaptTrainerAccountView(AccountModel trainer) {
        Glide.with(this).load(trainer.profile.photoUrl).thumbnail(0.5f).into(ivUserPhoto);
        tvCommentTrainer.setText(String.format("Comment from trainer %s", trainer.profile.name));
        tvEvalTrainerName.setText(String.format("Your score is evaluated by trainer %s.", trainer.profile.name));
    }

    private void adaptTrainingItems(List<TrainingItemChildModel> listTrainingItem){
        vgTrainingTypeB.setVisibility(View.VISIBLE);
        trainingItemTypeRepeater.setList(listTrainingItem);
        trainingItemTypeRepeater.notifyDataSetChanged();
    }

    @OnClick(R.id.actTrainingInfo_vbtnX)
    public void onBackButtonClicked() {
        finish();
    }

    //training item adapter
    class TraineeTrainingChildItemTypeAdapter extends JoRepeatorAdapter<TrainingItemChildModel> {

        @BindView(R.id.itemTrainingPageItemChild_tvItem)        TextView tvTitle;
        @BindView(R.id.itemTrainingPageItemChild_tvStatusLMH)   TextView tvStatus;
        @BindView(R.id.itemTrainingPageItemChild_ivSuccess)     ImageView ivSuccess;
        @BindView(R.id.itemTrainingPageItemChild_ivFail)        ImageView ivFail;

        String type;

        public TraineeTrainingChildItemTypeAdapter(String type) {
            this.type = type;
        }

        @Override
        public void onBind(View v, final TrainingItemChildModel trainingItem) {
            ButterKnife.bind(this, v);
            tvTitle.setText(trainingItem.contents);

            if (type.equals("B")) {
                if (trainingItem.trainingResult != null) {
                    String result = trainingItem.trainingResult.traininigResult;
                    if (result.equals("S")) {
                        ivSuccess.setVisibility(View.VISIBLE);
                    } else if (result.equals("F")) {
                        ivFail.setVisibility(View.VISIBLE);
                    }
                }
            } else if (type.equals("C")) {
                tvStatus.setText(trainingItem.trainingResult.traininigResult);
                tvStatus.setVisibility(View.VISIBLE);
            }
        }
    }
}