package id.co.okhome.consultant.view.fragment.consultant_tab;

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
import id.co.okhome.consultant.view.activity.cleaning.NextCleaningsActivity;
import id.co.okhome.consultant.view.activity.cleaning.PreviousCleaningsActivity;

/**
 * Created by jo on 2018-04-07.
 */

public class JobHistoryTabFragment extends Fragment implements TabFragmentStatusListener {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tab_jobs, null);
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

    @OnClick(R.id.fragTabJobs_vbtnSeeNextCleaning)
    public void onClickSeeNext(View v){
        startActivity(new Intent(getContext(), NextCleaningsActivity.class));

    }

    @OnClick(R.id.fragTabJobs_vbtnSeePreviousCleaning)
    public void onClickPreviousNext(View v){
        startActivity(new Intent(getContext(), PreviousCleaningsActivity.class));

    }

}
