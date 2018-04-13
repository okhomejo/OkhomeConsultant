package id.co.okhome.consultant.view.viewholder;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mrjodev.jorecyclermanager.JoViewHolder;
import com.mrjodev.jorecyclermanager.annotations.LayoutMatcher;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.okhome.consultant.R;
import id.co.okhome.consultant.lib.joviewrepeator.JoRepeatorAdapter;
import id.co.okhome.consultant.lib.joviewrepeator.JoViewRepeator;
import id.co.okhome.consultant.model.cleaning.CleaningReviewSummaryModel;

/**
 * Created by josongmin on 2016-06-28.
 */

@LayoutMatcher(layoutId = R.layout.item_cleaning_review_summary)
public class ReviewSummaryVHolder extends JoViewHolder<CleaningReviewSummaryModel> {

    @BindView(R.id.itemCleaningSummary_vgStars)     ViewGroup vgStars;

    JoViewRepeator<Integer> starReviewRepeater = null;

    public ReviewSummaryVHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void onViewCreated() {
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void onBind(CleaningReviewSummaryModel m, int pos, int absPos) {
        super.onBind(m, pos, absPos);

//        starReviewRepeater = new JoViewRepeator<Integer>(getContext())
//                .setContainer(vgStars)
//                .setItemLayoutId(R.layout.item_cleaning_review_summary_star_graph)
//                .setCallBack(new StarRatingBarAdapter("C"));
//
//        starReviewRepeater.addItem(m.star1Cnt);
//        starReviewRepeater.notifyDataSetChanged();
    }

    class StarRatingBarAdapter extends JoRepeatorAdapter<Integer> {

        @BindView(R.id.itemTrainingPageItem_tvName)     TextView tvName;

        String type;

        public StarRatingBarAdapter(String type) {
            this.type = type;
        }

        @Override
        public void onBind(View v, int pos, Integer model) {
            ButterKnife.bind(this, v);
        }
    }
}
