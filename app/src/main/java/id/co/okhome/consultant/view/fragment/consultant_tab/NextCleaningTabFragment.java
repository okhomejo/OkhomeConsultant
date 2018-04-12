package id.co.okhome.consultant.view.fragment.consultant_tab;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.okhome.consultant.R;
import id.co.okhome.consultant.lib.fragment_pager.TabFragmentStatusListener;
import id.co.okhome.consultant.model.cleaning.CleaningInfoModel;
import id.co.okhome.consultant.view.activity.cleaning.CleaningDetailActivity;

public class NextCleaningTabFragment extends Fragment implements TabFragmentStatusListener {

    @BindView(R.id.fragTabNextCleaning_tvAddress)     TextView tvAddress;

    private static final String TASK_KEY = "cleaning_model_key";
    private CleaningInfoModel cleaningModel;

    public static NextCleaningTabFragment newInstance(CleaningInfoModel model) {
        NextCleaningTabFragment fragment = new NextCleaningTabFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(TASK_KEY, model);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        cleaningModel = (CleaningInfoModel) getArguments().getSerializable(TASK_KEY);
        return inflater.inflate(R.layout.fragment_tab_next_cleaning, null);
    }

    @Override
    public void onStart() {
        super.onStart();
        ButterKnife.bind(this, getView());
    }

    @Override
    public void onResume() {
        super.onResume();
        adaptViewsAndData();
    }

    private void adaptViewsAndData() {
        tvAddress.setText(cleaningModel.address);
    }

    @Override
    public void onSelect() {
    }

    @Override
    public void onDeselect() {
    }

    @OnClick(R.id.fragTabNextCleaning_vbtnSeeDetail)
    public void onBtnSeeDetails(){
        Intent intent = new Intent(getContext(), CleaningDetailActivity.class);
        intent.putExtra("TASK_ACCEPTED", true);
        getContext().startActivity(intent);    }
}
