package id.co.okhome.consultant.view.viewholder;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mrjodev.jorecyclermanager.JoViewHolder;
import com.mrjodev.jorecyclermanager.annotations.LayoutMatcher;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.okhome.consultant.R;
import id.co.okhome.consultant.model.training.TrainingAttendanceForTraineeModel;
import id.co.okhome.consultant.model.training.TrainingModel;
import id.co.okhome.consultant.view.traininginfo.TraineeTrainingActivity;

/**
 * Created by josongmin on 2016-06-28.
 */

@LayoutMatcher(layoutId = R.layout.item_training_f_trainee_v2)
public class TrainingForTraineeVHolder extends JoViewHolder<TrainingModel> {

    @BindView(R.id.itemTraininigForTrainee_vgFailed)            ViewGroup vgFailed;
    @BindView(R.id.itemTraininigForTrainee_vgSuccess)           ViewGroup vgSuccess;
    @BindView(R.id.itemTraininigForTrainee_vgSchedule)          ViewGroup vgSchedule;

    @BindView(R.id.itemTrainingForTrainee_tvDesc)               TextView tvDesc;
    @BindView(R.id.itemTrainingForTrainee_tvSubject)            TextView tvSubject;
    @BindView(R.id.itemTrainingForTrainee_tvWhen)               TextView tvWhen;

    public TrainingForTraineeVHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void onViewCreated() {
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void onBind(TrainingModel training, int pos, int absPos) {
        super.onBind(training, pos, absPos);
        TrainingAttendanceForTraineeModel attendance = training.trainingAttendanceForTrainee;

        vgSchedule.setVisibility(View.GONE);
        vgFailed.setVisibility(View.GONE);
        vgSuccess.setVisibility(View.GONE);

        tvDesc.setText(training.desc);
        tvSubject.setText(training.subject);


        //트레이닝 일정 뷰 처리
        if(attendance == null){
            tvWhen.setText("Training schedule is not ready");
        }else{

            if(attendance.joinYN != null){
                DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.S");
                DateTime dt = formatter.parseDateTime(attendance.trainingWhen);
                tvWhen.setText("Training on " + dt.toString("dd MMM yy, hh:mm"));

                // 5 Aug 18, 15:00
            }else{
                if(attendance.joinYN.equals("Y")){
                    tvWhen.setText("Training on progress");
                }else{
                    tvWhen.setText("You missed the training");
                }
            }
        }

        //합격, 불합격 처리
        if(attendance != null){
            if(attendance.passYN == null){
                vgSchedule.setVisibility(View.VISIBLE);
            }else if(attendance.passYN.equals("Y")){
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
                getContext().startActivity(new Intent(getContext(), TraineeTrainingActivity.class).putExtra("trainingId", getModel().trainingId+""));
            }
        });

    }

}
