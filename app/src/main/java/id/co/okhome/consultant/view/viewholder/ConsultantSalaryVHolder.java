package id.co.okhome.consultant.view.viewholder;

import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mrjodev.jorecyclermanager.JoViewHolder;
import com.mrjodev.jorecyclermanager.annotations.LayoutMatcher;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.okhome.consultant.R;
import id.co.okhome.consultant.lib.app.OkhomeDateTimeFormatUtil;
import id.co.okhome.consultant.lib.app.OkhomeUtil;
import id.co.okhome.consultant.model.MoneyHistoryModel;

/**
 * Created by josongmin on 2016-06-28.
 */

@LayoutMatcher(layoutId = R.layout.item_consultant_salary)
public class ConsultantSalaryVHolder extends JoViewHolder<MoneyHistoryModel> {

//    @BindView(R.id.itemConsultantSalary_vgPaymentNotyet)            ViewGroup vgPaymentNotyet;
//    @BindView(R.id.itemConsultantSalary_vgPaymentComplete)          ViewGroup vgPaymentCompl;


    @BindView(R.id.itemTrainingForTrainee_tvMoney)
    TextView tvMoney;

    @BindView(R.id.itemConsultantSalary_tvTargetDateTime)
    TextView tvTargetDateTime;

    @BindView(R.id.itemConsultantSalary_vgCheckBox)
    ViewGroup vgCheckBox;

    @BindView(R.id.itemConsultantSalary_tvComment)
    TextView tvComment;


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

        tvMoney.setText("Rp " + OkhomeUtil.getPriceFormatValue(m.money));

        //paid
        if(m.finishYN.equals("Y")){
            vgCheckBox.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorGreen));

            //earning money
            if(m.useType.equals("G")){
                tvMoney.setTextColor(ContextCompat.getColor(getContext(), R.color.colorOkhome));
                tvComment.setText("Deposited on " + OkhomeDateTimeFormatUtil.printFullDate(m.payDateTime));
            }
            //spending money
            else{
                tvMoney.setTextColor(Color.parseColor("#44000000"));
                tvComment.setText("Withdrawn on " + OkhomeDateTimeFormatUtil.printFullDate(m.payDateTime));
            }

        }
        //waiting
        else{
            vgCheckBox.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorLightBlueGray3));

            //earning money
            if(m.useType.equals("G")){
                tvMoney.setTextColor(ContextCompat.getColor(getContext(), R.color.colorOkhome));
                tvComment.setText("Will be deposited on " + OkhomeDateTimeFormatUtil.printFullDate(m.payDateTime));
            }
            //spending money
            else{
                tvMoney.setTextColor(Color.parseColor("#44000000"));
                tvComment.setText("Will be withdrawn on "  + OkhomeDateTimeFormatUtil.printFullDate(m.payDateTime));
            }

        }


        //cleaning
        if(m.tag.equals("C")) {
            tvTargetDateTime.setText("Cleaning on " + OkhomeDateTimeFormatUtil.printFullDateTimeWithoutDayName((String)m.cleaning.get("cleaningWhen")));
        }

        //penalty
        else if(m.tag.equals("P")){
            tvTargetDateTime.setText("A late penalty of cleanining on " + OkhomeDateTimeFormatUtil.printFullDateTimeWithoutDayName((String)m.penalty.get("cleaningWhen")));
        }
    }

}
