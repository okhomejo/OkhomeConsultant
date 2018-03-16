package id.co.okhome.consultant.view.main.trainee.fragment;

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
import id.co.okhome.consultant.view.main.trainee.TraineeNewsActivity;
import id.co.okhome.consultant.view.userinfo.trainee.FillupUserInfoActivity;
import id.co.okhome.consultant.view.etc.AboutOkhomeActivity;

/**
 * Created by jo on 2018-01-23.
 */

public class SettingTabFragment extends Fragment implements TabFragmentStatusListener {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab_setting_f_trainee, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        ButterKnife.bind(this, getView());

    }

    @Override
    public void onSelect() {

    }

    @Override
    public void onDeselect() {

    }

    @OnClick(R.id.fragTabSettingsForTrainee_vbtnProfile)
    public void onClickProfile() {
        startActivity(new Intent(getActivity(), FillupUserInfoActivity.class));
    }

    @OnClick(R.id.fragTabSettingsForTrainee_vbtnNews)
    public void onClickNews() {
        startActivity(new Intent(getActivity(), TraineeNewsActivity.class));
    }
    @OnClick(R.id.fragTabSettingForTrainee_vbtnAbout)
    public void onAboutClick(View v){
        startActivity(new Intent(getActivity(), AboutOkhomeActivity.class));
    }

}
