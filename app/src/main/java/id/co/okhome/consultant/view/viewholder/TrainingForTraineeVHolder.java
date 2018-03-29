package id.co.okhome.consultant.view.viewholder;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;

import com.mrjodev.jorecyclermanager.JoViewHolder;
import com.mrjodev.jorecyclermanager.annotations.LayoutMatcher;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.okhome.consultant.R;
import id.co.okhome.consultant.model.TrainingModel;
import id.co.okhome.consultant.view.traininginfo.TraineeTrainingActivity;

/**
 * Created by josongmin on 2016-06-28.
 */

@LayoutMatcher(layoutId = R.layout.item_training_f_trainee_v2)
public class TrainingForTraineeVHolder extends JoViewHolder<TrainingModel> {

    @BindView(R.id.itemTraininigForTrainee_vgFailed)
    ViewGroup vgFailed;

    @BindView(R.id.itemTraininigForTrainee_vgSuccess)
    ViewGroup vgSuccess;

    @BindView(R.id.itemTraininigForTrainee_vgSchedule)
    ViewGroup vgSchedule;


    public TrainingForTraineeVHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void onViewCreated() {
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void onBind(TrainingModel m, int pos, int absPos) {
        super.onBind(m, pos, absPos);
        vgSchedule.setVisibility(View.GONE);
        vgFailed.setVisibility(View.GONE);
        vgSuccess.setVisibility(View.GONE);

        if(pos < 3){
            if(pos == 1){
                vgSuccess.setVisibility(View.VISIBLE);
            }else{
                vgFailed.setVisibility(View.VISIBLE);
            }
        }else{
            vgSchedule.setVisibility(View.VISIBLE);
        }


        getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getContext().startActivity(new Intent(getContext(), TraineeTrainingActivity.class));
            }
        });

    }

}
