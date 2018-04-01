package id.co.okhome.consultant.view.traininginfo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.okhome.consultant.R;
import id.co.okhome.consultant.lib.app.ConsultantLoggedIn;
import id.co.okhome.consultant.lib.app.OkHomeParentActivity;
import id.co.okhome.consultant.lib.app.OkhomeUtil;
import id.co.okhome.consultant.lib.joviewrepeator.JoRepeatorAdapter;
import id.co.okhome.consultant.lib.joviewrepeator.JoViewRepeator;
import id.co.okhome.consultant.lib.retrofit.RetrofitCallback;
import id.co.okhome.consultant.model.training.TrainingItemModel;
import id.co.okhome.consultant.model.training.TrainingModel;
import id.co.okhome.consultant.rest_apicall.retrofit_restapi.OkhomeRestApi;

public class TraineeTrainingActivity extends OkHomeParentActivity {

    @BindView(R.id.actTrainingInfo_vLoading)                View vLoading;
    @BindView(R.id.actTrainingInfo_svItem)                  ScrollView svItem;
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
        setContentView(R.layout.activity_traininginfo_type1);
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
                .setContainer(vgTrainingTypeBItems)
                .setItemLayoutId(R.layout.item_trainingpage_item)
                .setCallBack(new TraineeTrainingItemTypeAdapter("C"));
    }

    //view <-> model async
    private void adaptTrainingViewAndData(TrainingModel training){
        //init datas.

        //adapt trainng items
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
                svItem.setVisibility(View.VISIBLE);
                adaptTrainingViewAndData(training);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                vLoading.setVisibility(View.GONE);
            }
        });
    }


    //training item adapter
    class TraineeTrainingItemTypeAdapter extends JoRepeatorAdapter<TrainingItemModel> {

        @BindView(R.id.itemTrainingPageItem_tvName) TextView tvName;

        String type;

        public TraineeTrainingItemTypeAdapter(String type) {
            this.type = type;
        }

        @Override
        public void onBind(View v, final TrainingItemModel trainingItem) {
            ButterKnife.bind(this, v);
            tvName.setText(trainingItem.subject);


            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(TraineeTrainingActivity.this, TraineeTrainingItemInfoActivity.class)
                            .putExtra("trainingId", trainingItem.id));
                }
            });
        }


    }
}
