package id.co.okhome.consultant.view.viewholder;

import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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

        starReviewRepeater = new JoViewRepeator<Integer>(getContext())
                .setContainer(vgStars)
                .setItemLayoutId(R.layout.item_cleaning_review_summary_star_graph)
                .setCallBack(new StarRatingBarAdapter(m.totalReviewCount));

        starReviewRepeater.addItem(m.star5Cnt);
        starReviewRepeater.addItem(m.star4Cnt);
        starReviewRepeater.addItem(m.star3Cnt);
        starReviewRepeater.addItem(m.star2Cnt);
        starReviewRepeater.addItem(m.star1Cnt);

        starReviewRepeater.notifyDataSetChanged();
    }

    class StarRatingBarAdapter extends JoRepeatorAdapter<Integer> {

        @BindView(R.id.itemReviewSummary_vRatingBar)            View vRatingBar;
        @BindView(R.id.itemReviewSummary_tvRating)              TextView tvRating;
        @BindView(R.id.itemReviewSummary_tvTotalPercentage)     TextView tvTotalPercentage;

        int totalReviewCount;

        public StarRatingBarAdapter(int count) {
            this.totalReviewCount = count;
        }

        @Override
        public void onBind(View v, int pos, Integer reviewCount) {
            ButterKnife.bind(this, v);
            tvRating.setText(String.valueOf(5-pos));

            float percentage = (float) reviewCount*100/totalReviewCount;
            LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT);
            p.weight = percentage;

            vRatingBar.setLayoutParams(p);
            tvTotalPercentage.setText(String.format("%s%%", (double) Math.round((percentage) * 10) / 10));
        }
    }
}