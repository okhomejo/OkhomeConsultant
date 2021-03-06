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
import id.co.okhome.consultant.model.wallet.MutationModel;

/**
 * Created by josongmin on 2016-06-28.
 */

@LayoutMatcher(layoutId = R.layout.item_consultant_salary)
public class ConsultantSalaryVHolder extends JoViewHolder<MutationModel> {

//    @BindView(R.id.itemConsultantSalary_vgPaymentNotyet)            ViewGroup vgPaymentNotyet;
//    @BindView(R.id.itemConsultantSalary_vgPaymentComplete)          ViewGroup vgPaymentCompl;

    @BindView(R.id.itemTrainingForTrainee_tvMoney)          TextView tvMoney;
    @BindView(R.id.itemConsultantSalary_tvTargetDateTime)   TextView tvDescription;
    @BindView(R.id.itemConsultantSalary_vgCheckBox)         ViewGroup vgCheckBox;
    @BindView(R.id.itemConsultantSalary_tvComment)          TextView tvComment;

    public ConsultantSalaryVHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void onViewCreated() {
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void onBind(MutationModel m, int pos, int absPos) {
        super.onBind(m, pos, absPos);

        tvMoney.setText(String.format("Rp %s", OkhomeUtil.getPriceFormatValue(m.amount)));

        vgCheckBox.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorGreen));
        //earning money
        if(m.type.equals("C")){
            tvMoney.setTextColor(ContextCompat.getColor(getContext(), R.color.colorOkhome));
            tvComment.setText(String.format("Deposited on %s", OkhomeDateTimeFormatUtil.printFullDate(m.date)));
        }
        //spending money
        else{
            tvMoney.setTextColor(Color.parseColor("#44000000"));
            tvComment.setText(String.format("Withdrawn on %s", OkhomeDateTimeFormatUtil.printFullDate(m.date)));
        }
        tvDescription.setText(m.title.get("id"));

//        tvDescription.setText(String.format("Cleaning on %s", OkhomeDateTimeFormatUtil.printFullDateTimeWithoutDayName(m.date)));

//        //paid
//        if(m.finishYN.equals("Y")){
//            vgCheckBox.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorGreen));
//
//            //earning money
//            if(m.type.equals("C")){
//                tvMoney.setTextColor(ContextCompat.getColor(getContext(), R.color.colorOkhome));
//                tvComment.setText(String.format("Deposited on %s", OkhomeDateTimeFormatUtil.printFullDate(m.date)));
//            }
//            //spending money
//            else{
//                tvMoney.setTextColor(Color.parseColor("#44000000"));
//                tvComment.setText(String.format("Withdrawn on %s", OkhomeDateTimeFormatUtil.printFullDate(m.date)));
//            }
//
//        }
//        //waiting
//        else{
//            vgCheckBox.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorLightBlueGray3));
//
//            if (m.payDateTime != null) {
//
//                //earning money
//                if(m.useType.equals("G")){
//                    tvMoney.setTextColor(ContextCompat.getColor(getContext(), R.color.colorOkhome));
//                    tvComment.setText(String.format("Deposited on %s", OkhomeDateTimeFormatUtil.printFullDate(m.date)));
//                }
//                //spending money
//                else{
//                    tvMoney.setTextColor(Color.parseColor("#44000000"));
//                    tvComment.setText(String.format("Withdrawn on %s", OkhomeDateTimeFormatUtil.printFullDate(m.date)));
//                }
//            }
//        }
//
//
//        //cleaning
//        if(m.tag.equals("C")) {
//            tvDescription.setText("Cleaning on " + OkhomeDateTimeFormatUtil.printFullDateTimeWithoutDayName((String)m.cleaning.get("cleaningWhen")));
//        }
//
//        //penalty
//        else if(m.tag.equals("P")){
//            tvDescription.setText("A late penalty of cleanining on " + OkhomeDateTimeFormatUtil.printFullDateTimeWithoutDayName((String)m.penalty.get("cleaningWhen")));
//        }
    }

}
