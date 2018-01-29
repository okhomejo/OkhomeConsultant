package id.co.okhome.consultant.view.viewholder;

import android.view.View;

import com.mrjodev.jorecyclermanager.JoViewHolder;
import com.mrjodev.jorecyclermanager.annotations.LayoutMatcher;

import butterknife.ButterKnife;
import id.co.okhome.consultant.R;
import id.co.okhome.consultant.model.TrainingModel;

/**
 * Created by josongmin on 2016-06-28.
 */

@LayoutMatcher(layoutId = R.layout.item_training_f_trainee)
public class TrainingForTraineeVHolder extends JoViewHolder<TrainingModel> {



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
    }

}
