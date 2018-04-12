package id.co.okhome.consultant.view.viewholder;

import android.view.View;
import android.view.ViewGroup;

import com.mrjodev.jorecyclermanager.JoViewHolder;
import com.mrjodev.jorecyclermanager.annotations.LayoutMatcher;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.okhome.consultant.R;
import id.co.okhome.consultant.model.MoneyHistoryModel;

/**
 * Created by josongmin on 2016-06-28.
 */

@LayoutMatcher(layoutId = R.layout.item_consultant_salary)
public class ConsultantSalaryVHolder extends JoViewHolder<MoneyHistoryModel> {

    @BindView(R.id.itemConsultantSalary_vgPaymentNotyet)
    ViewGroup vgPaymentNotyet;

    @BindView(R.id.itemConsultantSalary_vgPaymentComplete)
    ViewGroup vgPaymentCompl;


    public ConsultantSalaryVHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void onViewCreated() {
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void onBind(MoneyHistoryModel m, int pos, int absPos) {
        super.onBind(m, pos, absPos);

        vgPaymentNotyet.setVisibility(View.GONE);
        vgPaymentCompl.setVisibility(View.GONE);

        String type = getRcvParams().getParam("TYPE");
        if(type.equals("COMPLETE")){
            vgPaymentCompl.setVisibility(View.VISIBLE);
        }else{
            vgPaymentNotyet.setVisibility(View.VISIBLE);
        }
    }

}
