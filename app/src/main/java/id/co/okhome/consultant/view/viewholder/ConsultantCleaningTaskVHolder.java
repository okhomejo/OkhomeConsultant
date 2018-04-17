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

    @BindView(R.id.itemCleaningTask_ivStar1)        ImageView ivStar1;
    @BindView(R.id.itemCleaningTask_ivStar2)        ImageView ivStar2;
    @BindView(R.id.itemCleaningTask_ivStar3)        ImageView ivStar3;
    @BindView(R.id.itemCleaningTask_ivStar4)        ImageView ivStar4;
    @BindView(R.id.itemCleaningTask_ivStar5)        ImageView ivStar5;

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
        printStarDrawable(Math.round(m.score));

        getView().setOnClickListener(this);
    }

    private void printStarDrawable(int starCnt) {
        switch (starCnt) {
            case 1:
                ivStar1.setImageResource(R.drawable.ic_star_on);
                ivStar2.setImageResource(R.drawable.ic_star_off);
                ivStar3.setImageResource(R.drawable.ic_star_off);
                ivStar4.setImageResource(R.drawable.ic_star_off);
                ivStar5.setImageResource(R.drawable.ic_star_off);
                break;
            case 2:
                ivStar1.setImageResource(R.drawable.ic_star_on);
                ivStar2.setImageResource(R.drawable.ic_star_on);
                ivStar3.setImageResource(R.drawable.ic_star_off);
                ivStar4.setImageResource(R.drawable.ic_star_off);
                ivStar5.setImageResource(R.drawable.ic_star_off);
                break;
            case 3:
                ivStar1.setImageResource(R.drawable.ic_star_on);
                ivStar2.setImageResource(R.drawable.ic_star_on);
                ivStar3.setImageResource(R.drawable.ic_star_on);
                ivStar4.setImageResource(R.drawable.ic_star_off);
                ivStar5.setImageResource(R.drawable.ic_star_off);
                break;
            case 4:
                ivStar1.setImageResource(R.drawable.ic_star_on);
                ivStar2.setImageResource(R.drawable.ic_star_on);
                ivStar3.setImageResource(R.drawable.ic_star_on);
                ivStar4.setImageResource(R.drawable.ic_star_on);
                ivStar5.setImageResource(R.drawable.ic_star_off);
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
    
    @Override
    public void onClick(View view) {
        Intent intent = new Intent(getContext(), CleaningDetailActivity.class);
        intent.putExtra("TASK_ACCEPTED", true);
        getContext().startActivity(intent);
    }
}