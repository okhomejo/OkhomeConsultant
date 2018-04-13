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
import id.co.okhome.consultant.view.activity.cleaning.CleaningDetailActivity;

/**
 * Created by frizurd on 12/04/2018.
 */

@LayoutMatcher(layoutId = R.layout.item_consultant_cleaning_task)
public class ConsultantCleaningTaskVHolder extends JoViewHolder<CleaningInfoModel> implements View.OnClickListener {

    @BindView(R.id.itemCleaningTask_tvTitle)        TextView tvTitle;
    @BindView(R.id.itemCleaningTask_tvAddress)      TextView tvAddress;
    @BindView(R.id.itemCleaningTask_tvDate)         TextView tvDate;
    @BindView(R.id.itemCleaningTask_vgRating)       ViewGroup vgRating;

    public ConsultantCleaningTaskVHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void onViewCreated() {
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void onBind(CleaningInfoModel m, int pos, int absPos) {

        super.onBind(m, pos, absPos);
        tvTitle.setText(m.title);
        tvAddress.setText(m.address);

        tvDate.setText(String.format("Cleaning on %s", OkhomeDateTimeFormatUtil.printFullDateTime(m.when)));

        vgRating.removeAllViews();
        int counter = Math.round(m.score);
        for (int i = 0; i < 5; i++) {
            ImageView star = new ImageView(getContext());
            star.setAdjustViewBounds(true);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(50, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMarginEnd(5);
            star.setLayoutParams(layoutParams);
            if (counter > 0) {
                star.setImageResource(R.drawable.ic_star_on);
            } else {
                star.setImageResource(R.drawable.ic_star_off);
            }
            vgRating.addView(star);
            counter--;
        }

        getView().setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(getContext(), CleaningDetailActivity.class);
        intent.putExtra("TASK_ACCEPTED", true);
        getContext().startActivity(intent);
    }
}