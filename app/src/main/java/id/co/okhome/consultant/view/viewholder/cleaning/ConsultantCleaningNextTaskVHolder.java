package id.co.okhome.consultant.view.viewholder.cleaning;

import android.content.Intent;
import android.view.View;
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

@LayoutMatcher(layoutId = R.layout.item_consultant_next_cleaning_thumb)
public class ConsultantCleaningNextTaskVHolder extends JoViewHolder<CleaningInfoModel> implements View.OnClickListener {

    @BindView(R.id.itemConsultantCleaningNextThumb_tvTitle)        TextView tvTitle;
    @BindView(R.id.itemConsultantCleaningNextThumb_tvAddress)      TextView tvAddress;
    @BindView(R.id.itemConsultantCleaningNextThumb_tvDate)         TextView tvDate;
    @BindView(R.id.itemConsultantCleaningNextThumb_tvPrice)         TextView tvPrice;

    public ConsultantCleaningNextTaskVHolder(View itemView) {
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
//        tvPrice.setText(m.);
        getView().setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        Intent intent = new Intent(getContext(), CleaningDetailActivity.class);
        intent.putExtra("TASK_ACCEPTED", true);
        getContext().startActivity(intent);
    }
}