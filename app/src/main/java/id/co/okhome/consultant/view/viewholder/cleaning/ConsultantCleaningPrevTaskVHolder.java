package id.co.okhome.consultant.view.viewholder.cleaning;

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
import id.co.okhome.consultant.lib.app.OkhomeUtil;
import id.co.okhome.consultant.model.cleaning.order.CleaningDayItemModel;
import id.co.okhome.consultant.view.activity.cleaning.CleaningDetailActivity;

/**
 * Created by frizurd on 12/04/2018.
 */

@LayoutMatcher(layoutId = R.layout.item_consultant_prev_cleaning_task)
public class ConsultantCleaningPrevTaskVHolder extends JoViewHolder<CleaningDayItemModel> implements View.OnClickListener {

    @BindView(R.id.itemConsultantPrevCleaningTask_tvPrice)        TextView tvPrice;
    @BindView(R.id.itemConsultantPrevCleaningTask_tvAddress)      TextView tvAddress;
    @BindView(R.id.itemConsultantPrevCleaningTask_tvReviewScore)  TextView tvReviewScore;
    @BindView(R.id.itemConsultantPrevCleaningTask_tvDate)         TextView tvDate;
    @BindView(R.id.itemConsultantPrevCleaningTask_vgStarItems)    ViewGroup vgRating;

    public ConsultantCleaningPrevTaskVHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void onViewCreated() {
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void onBind(CleaningDayItemModel m, int pos, int absPos) {

        super.onBind(m, pos, absPos);
        String money = OkhomeUtil.getPriceFormatValue(m.priceConsultant) + " Rupiah";

        tvPrice.setText(money);
        tvAddress.setText(m.homeAddress);
        tvDate.setText(String.format("Cleaning on %s", OkhomeDateTimeFormatUtil.printFullDateTime(m.whenDateTime)));
        tvReviewScore.setText(m.grade+"");
        printStarDrawable(m.grade);

        getView().setOnClickListener(this);
    }

    private void printStarDrawable(float starCnt) {
        for(int i = 0; i < 5; i++){
            ImageView ivStar = (ImageView)vgRating.getChildAt(i);
            if(i < starCnt){
                ivStar.setImageResource(R.drawable.ic_star_on);
            }else{
                ivStar.setImageResource(R.drawable.ic_star_off);
            }
        }
    }
    
    @Override
    public void onClick(View view) {
        CleaningDetailActivity.start(getContext(), getModel().cleaningId);
    }
}