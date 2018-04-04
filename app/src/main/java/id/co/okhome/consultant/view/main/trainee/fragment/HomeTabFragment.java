package id.co.okhome.consultant.view.main.trainee.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.okhome.consultant.R;
import id.co.okhome.consultant.lib.app.ConsultantLoggedIn;
import id.co.okhome.consultant.lib.fragment_pager.TabFragmentStatusListener;
import id.co.okhome.consultant.lib.retrofit.RetrofitCallback;
import id.co.okhome.consultant.model.TraineePageHomeModel;
import id.co.okhome.consultant.rest_apicall.retrofit_restapi.OkhomeRestApi;

/**
 * Created by jo on 2018-01-23.
 */

public class HomeTabFragment extends Fragment implements TabFragmentStatusListener {

    @BindView(R.id.fragTabHomeForTrainee_vLoading)      View vLoading;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tab_home_f_trainee, null);
    }


    @Override
    public void onResume() {
        super.onResume();
        getTraineeHomeInfo();
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

    //match view and data
    private void adaptViews(TraineePageHomeModel traineePageHome){
        //

    }


    //get trainnee home info
    private void getTraineeHomeInfo(){
        vLoading.setVisibility(View.VISIBLE);
        OkhomeRestApi.getTraineeClient().getTraineePageHome(ConsultantLoggedIn.id()).enqueue(new RetrofitCallback<TraineePageHomeModel>() {
            @Override
            public void onSuccess(TraineePageHomeModel traineePageHome) {

                adaptViews(traineePageHome);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                vLoading.setVisibility(View.GONE);
            }
        });

    }



}
