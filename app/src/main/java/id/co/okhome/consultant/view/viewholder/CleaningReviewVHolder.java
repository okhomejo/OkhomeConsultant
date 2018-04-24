package id.co.okhome.consultant.view.viewholder;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mrjodev.jorecyclermanager.JoViewHolder;
import com.mrjodev.jorecyclermanager.annotations.LayoutMatcher;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.okhome.consultant.R;
import id.co.okhome.consultant.lib.app.OkhomeDateTimeFormatUtil;
import id.co.okhome.consultant.model.cleaning.CleaningReviewModel;

@LayoutMatcher(layoutId = R.layout.item_cleaning_review)
public class CleaningReviewVHolder extends JoViewHolder<CleaningReviewModel> {

    @BindView(R.id.itemReview_vgReview)         ViewGroup vgReview;

    @BindView(R.id.itemReview_tvTitle)          TextView tvTitle;
    @BindView(R.id.itemReview_tvDate)           TextView tvDate;
    @BindView(R.id.itemReview_tvReviewText)     TextView tvReviewText;
    @BindView(R.id.itemReview_tvTextRating)     TextView tvTextRating;
    @BindView(R.id.itemReview_tvTags)           TextView tvTags;

    @BindView(R.id.itemReview_ivStar1)          ImageView ivStar1;
    @BindView(R.id.itemReview_ivStar2)          ImageView ivStar2;
    @BindView(R.id.itemReview_ivStar3)          ImageView ivStar3;
    @BindView(R.id.itemReview_ivStar4)          ImageView ivStar4;
    @BindView(R.id.itemReview_ivStar5)          ImageView ivStar5;

    private static View tagLayout;

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
        int reviewScore = Math.round(m.score);
        tvTitle.setText(String.format("Review %s", m.id));
        tvTextRating.setText(String.valueOf(reviewScore));
        tvReviewText.setText(m.review);
        tvDate.setText(String.format("Cleaning on %s (%s hours)", OkhomeDateTimeFormatUtil.printFullDateTime(m.cleaningWhen), Math.round(m.duration)));
        printStarDrawable(reviewScore);
        printTags(m.tags);
    }

    private void printStarDrawable(int starCnt) {
        ivStar2.setImageResource(R.drawable.ic_star_off);
        ivStar3.setImageResource(R.drawable.ic_star_off);
        ivStar4.setImageResource(R.drawable.ic_star_off);
        ivStar5.setImageResource(R.drawable.ic_star_off);

        switch (starCnt) {
            case 1:
                ivStar1.setImageResource(R.drawable.ic_star_on);
                break;
            case 2:
                ivStar1.setImageResource(R.drawable.ic_star_on);
                ivStar2.setImageResource(R.drawable.ic_star_on);
                break;
            case 3:
                ivStar1.setImageResource(R.drawable.ic_star_on);
                ivStar2.setImageResource(R.drawable.ic_star_on);
                ivStar3.setImageResource(R.drawable.ic_star_on);
                break;
            case 4:
                ivStar1.setImageResource(R.drawable.ic_star_on);
                ivStar2.setImageResource(R.drawable.ic_star_on);
                ivStar3.setImageResource(R.drawable.ic_star_on);
                ivStar4.setImageResource(R.drawable.ic_star_on);
                break;
            case 5:
                ivStar1.setImageResource(R.drawable.ic_star_on);
                ivStar2.setImageResource(R.drawable.ic_star_on);
                ivStar3.setImageResource(R.drawable.ic_star_on);
                ivStar4.setImageResource(R.drawable.ic_star_on);
                ivStar5.setImageResource(R.drawable.ic_star_on);
                break;
        }
    }

    private void printTags(String[] tags) {
        tvTags.setText("");
        for (int i = 0; i < tags.length; i++) {
            tvTags.append("#"+i);
        }

        if(tagLayout == null){
            LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            tagLayout = li.inflate(R.layout.item_cleaning_review_tag, vgReview, false);
        }
        SpannableString ss = new SpannableString(tvTags.getText());

        int counter = 0;
        for (String tag : tags) {
            TextView tagTitle = tagLayout.findViewById(R.id.itemCleaningTag_tvTitle);
            tagTitle.setText(tag);
            tagLayout.setDrawingCacheEnabled(true);
            tagLayout.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

            tagLayout.layout(0, 0, tagLayout.getMeasuredWidth(), tagLayout.getMeasuredHeight());
            tagLayout.buildDrawingCache(true);
            Bitmap b = Bitmap.createBitmap(tagLayout.getDrawingCache());
            tagLayout.setDrawingCacheEnabled(false);

            Drawable d = new BitmapDrawable(getContext().getResources(), b);
            d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());

            ImageSpan span = new ImageSpan(d, ImageSpan.ALIGN_BASELINE);
            ss.setSpan(span, counter, counter+2, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);

            counter+=2;
        }
        tvTags.setText(ss);
    }
}