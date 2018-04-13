package id.co.okhome.consultant.view.fragment.consultant_tab;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import org.joda.time.DateTime;

import java.util.Iterator;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.okhome.consultant.R;
import id.co.okhome.consultant.lib.app.ConsultantLoggedIn;
import id.co.okhome.consultant.lib.app.OkhomeUtil;
import id.co.okhome.consultant.lib.fragment_pager.TabFragmentStatusListener;
import id.co.okhome.consultant.lib.retrofit.RetrofitCallback;
import id.co.okhome.consultant.model.page.ConsultantPageProgressModel;
import id.co.okhome.consultant.rest_apicall.retrofit_restapi.OkhomeRestApi;
import id.co.okhome.consultant.view.activity.cleaning_review.CleaningReviewListActivity;
import id.co.okhome.consultant.view.activity.salary.ConsultantSalaryListActivity;

/**
 * Created by jo on 2018-04-07.
 */

public class ProgressTabFragment extends Fragment implements TabFragmentStatusListener {

    @BindView(R.id.fragTabProgress_svItem)              ScrollView svItem;
    @BindView(R.id.fragTabProgress_vProgress)           ProgressBar progressBar;

    @BindView(R.id.fragTabProgress_tvSalaryRevenue)     TextView tvSalaryRevenue;
    @BindView(R.id.fragTabProgress_tvSalaryMonth)       TextView tvSalaryMonth;
    @BindView(R.id.fragTabProgress_tvSalaryPaid)        TextView tvSalaryPaid;
    @BindView(R.id.fragTabProgress_tvSalaryBalance)     TextView tvSalaryBalance;

    @BindView(R.id.fragTabProgress_tvReviewLatest)      TextView tvReviewLatest;
    @BindView(R.id.fragTabProgress_tvReviewOverall)     TextView tvReviewOverall;
    @BindView(R.id.fragTabProgress_tvReviewOther)       TextView tvReviewOther;

    @BindView(R.id.fragTabProgress_tvWorkedAverage)     TextView tvWorkedAverage;
    @BindView(R.id.fragTabProgress_tvWorkedOverall)     TextView tvWorkedOverall;
    @BindView(R.id.fragTabProgress_tvWorkedNext)        TextView tvWorkedNext;
    @BindView(R.id.fragTabProgress_tvWorkedLast)        TextView tvWorkedLast;
    @BindView(R.id.fragTabProgress_tvWorkedOverallMonth)TextView tvWorkedOverallMonth;
    @BindView(R.id.fragTabProgress_tvWorkedLastMonth)   TextView tvWorkedLastMonth;
    @BindView(R.id.fragTabProgress_tvWorkedNextMonth)   TextView tvWorkedNextMonth;

    @BindView(R.id.fragTabProgress_vgWorked)            ViewGroup vgWorked;
    @BindView(R.id.fragTabProgress_vgWorkedOverall)     ViewGroup vgWorkedOverall;
    @BindView(R.id.fragTabProgress_vgWorkedNext)        ViewGroup vgWorkedNext;
    @BindView(R.id.fragTabProgress_vgWorkedLast)        ViewGroup vgWorkedLast;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tab_progress, null);
    }

    @Override
    public void onStart() {
        super.onStart();
        ButterKnife.bind(this, getView());
        svItem.setVisibility(View.GONE);
    }

    @Override
    public void onResume() {
        super.onResume();
        getConsultantProgressInfo();
    }

    @Override
    public void onSelect() {

    }

    @Override
    public void onDeselect() {

    }

    private void getConsultantProgressInfo() {
        progressBar.setVisibility(View.VISIBLE);
        OkhomeRestApi.getConsultantClient().getConsultantProgress(ConsultantLoggedIn.id()).enqueue(new RetrofitCallback<ConsultantPageProgressModel>() {
            @Override
            public void onSuccess(ConsultantPageProgressModel progressModel) {
                adaptViews(progressModel);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                progressBar.setVisibility(View.GONE);
                svItem.setVisibility(View.VISIBLE);
            }
        });
    }

    private void adaptViews(ConsultantPageProgressModel progressModel) {
        // Adapt salary view group
        tvSalaryRevenue.setText(String.format("Rp %s", OkhomeUtil.getPriceFormatValue(progressModel.salaryThisMonthTotal)));
        tvSalaryBalance.setText(String.format("Rp %s is paid", OkhomeUtil.getPriceFormatValue(progressModel.balance)));
        tvSalaryPaid.setText(String.format("Rp %s is your current balance", OkhomeUtil.getPriceFormatValue(progressModel.salaryThisMonthPaid)));
        tvSalaryMonth.setText(String.format("Total revenue in %s", DateTime.now().monthOfYear().getAsText()));

        // Adapt review view group
        tvReviewLatest.setText(String.valueOf(progressModel.reviewScore7days));
        tvReviewOverall.setText(String.valueOf(progressModel.reviewScoreOverall));
        tvReviewOther.setText(String.format("On average, Other consultants get %s score", progressModel.reviewScoreNear));

        // Adapt amount of days worked view group
        if (!progressModel.myWorkCnt.isEmpty()) {
            adaptAmountWorkedView(progressModel.myWorkCnt);
            tvWorkedAverage.setText(String.format("On average, Other consultants work %s times a month.", progressModel.othersWorkCnt));
        }
    }


    private void adaptAmountWorkedView(Map<String, Integer> myWorkCnt) {
        int counter = 0;
        Iterator it = myWorkCnt.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            switch (counter) {
                case 0:
                    tvWorkedOverall.setText(pair.getValue().toString());
                    tvWorkedOverallMonth.setText(pair.getKey().toString());
                    vgWorkedOverall.setVisibility(View.VISIBLE);
                    break;
                case 1:
                    tvWorkedNext.setText(pair.getValue().toString());
                    tvWorkedNextMonth.setText(pair.getKey().toString());
                    vgWorkedNext.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    tvWorkedLast.setText(pair.getValue().toString());
                    tvWorkedLastMonth.setText(pair.getKey().toString());
                    vgWorkedLast.setVisibility(View.VISIBLE);
                    break;
                default:
                    break;
            }
            it.remove();
            counter++;
        }
        vgWorked.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.fragTabProgress_vgbtnSalary)
    public void onSalaryClick(){
        startActivity(new Intent(getContext(), ConsultantSalaryListActivity.class));
    }

    @OnClick(R.id.fragTabProgress_vgbtnReview)
    public void onReviewClick(){
        startActivity(new Intent(getContext(), CleaningReviewListActivity.class));
    }

}
