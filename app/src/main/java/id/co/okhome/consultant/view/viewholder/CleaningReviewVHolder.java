package id.co.okhome.consultant.view.viewholder;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mrjodev.jorecyclermanager.JoViewHolder;
import com.mrjodev.jorecyclermanager.annotations.LayoutMatcher;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.okhome.consultant.R;
import id.co.okhome.consultant.lib.app.OkhomeDateTimeFormatUtil;
import id.co.okhome.consultant.model.cleaning.CleaningInfoModel;
import id.co.okhome.consultant.model.cleaning.CleaningReviewModel;
import id.co.okhome.consultant.view.activity.cleaning.CleaningDetailActivity;

@LayoutMatcher(layoutId = R.layout.item_cleaning_review)
public class CleaningReviewVHolder extends JoViewHolder<CleaningReviewModel> {

    @BindView(R.id.itemReview_tvTitle)          TextView tvTitle;
    @BindView(R.id.itemReview_tvDate)           TextView tvDate;
    @BindView(R.id.itemReview_tvReviewText)     TextView tvReviewText;
    @BindView(R.id.itemReview_tvTextRating)     TextView tvTextRating;

    public CleaningReviewVHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void onViewCreated() {
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void onBind(CleaningReviewModel m, int pos, int absPos) {

        super.onBind(m, pos, absPos);
        tvTitle.setText(String.format("Review %s", m.id));
        tvTextRating.setText(String.valueOf(m.score));
        tvReviewText.setText(m.review);
        tvDate.setText(String.format("Cleaning on %s (%s hours)", OkhomeDateTimeFormatUtil.printFullDateTime(m.cleaningWhen), m.duration));
    }
}