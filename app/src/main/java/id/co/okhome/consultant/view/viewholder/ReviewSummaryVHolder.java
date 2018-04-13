package id.co.okhome.consultant.view.viewholder;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mrjodev.jorecyclermanager.JoViewHolder;
import com.mrjodev.jorecyclermanager.annotations.LayoutMatcher;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.okhome.consultant.R;
import id.co.okhome.consultant.lib.joviewrepeator.JoRepeatorAdapter;
import id.co.okhome.consultant.lib.joviewrepeator.JoViewRepeator;
import id.co.okhome.consultant.model.cleaning.CleaningReviewSummaryModel;
import id.co.okhome.consultant.model.training.TrainingItemChildModel;
import id.co.okhome.consultant.model.training.TrainingItemModel;
import id.co.okhome.consultant.view.activity.traininginfo.TraineeTrainingActivity;
import id.co.okhome.consultant.view.activity.traininginfo.TraineeTrainingItemInfoActivity;

/**
 * Created by josongmin on 2016-06-28.
 */

@LayoutMatcher(layoutId = R.layout.item_cleaning_review_summary)
public class ReviewSummaryVHolder extends JoViewHolder<CleaningReviewSummaryModel> {

    @BindView(R.id.itemCleaningSummary_vgStars)     ViewGroup vgStars;

    JoViewRepeator<Integer> starReviewRepeator = null;

    public ReviewSummaryVHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void onViewCreated() {
        ButterKnife.bind(this, itemView);
        starReviewRepeator = new JoViewRepeator<Integer>(getContext())
                .setContainer(vgStars)
                .setItemLayoutId(R.layout.item_cleaning_review_summary_star_graph);
    }

    @Override
    public void onBind(CleaningReviewSummaryModel m, int pos, int absPos) {
        super.onBind(m, pos, absPos);

    }

    class TraineeTrainingItemTypeAdapter extends JoRepeatorAdapter<TrainingItemModel> {

        @BindView(R.id.itemTrainingPageItem_tvName)
        TextView tvName;

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
