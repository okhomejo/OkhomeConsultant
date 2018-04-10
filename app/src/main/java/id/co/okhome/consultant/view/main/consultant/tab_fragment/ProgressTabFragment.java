package id.co.okhome.consultant.view.main.consultant.tab_fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.okhome.consultant.R;
import id.co.okhome.consultant.lib.fragment_pager.TabFragmentStatusListener;
import id.co.okhome.consultant.view.cleaning_review.CleaningReviewListActivity;
import id.co.okhome.consultant.view.salary.ConsultantSalaryListActivity;

/**
 * Created by jo on 2018-04-07.
 */

public class ProgressTabFragment extends Fragment implements TabFragmentStatusListener {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tab_progress, null);
    }

    @Override
    public void onStart() {
        super.onStart();
        ButterKnife.bind(this, getView());
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onSelect() {

    }

    @Override
    public void onDeselect() {

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
