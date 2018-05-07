package id.co.okhome.consultant.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.mrjodev.jorecyclermanager.JoRecyclerAdapter;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.okhome.consultant.R;
import id.co.okhome.consultant.lib.app.ConsultantLoggedIn;
import id.co.okhome.consultant.lib.app.OkhomeUtil;
import id.co.okhome.consultant.lib.dokuwallet.DokuWallet;
import id.co.okhome.consultant.lib.retrofit.RetrofitCallback;
import id.co.okhome.consultant.model.wallet.ActivitiesModel;
import id.co.okhome.consultant.model.wallet.MutationModel;
import id.co.okhome.consultant.model.wallet.TokenModel;
import id.co.okhome.consultant.rest_apicall.retrofit_restapi.OkhomeRestApi;
import id.co.okhome.consultant.view.viewholder.BlankVHolder;
import id.co.okhome.consultant.view.viewholder.ConsultantSalaryVHolder;

/**
 * Created by jo on 2018-04-07.
 */

public class ConsultantSalaryListFragment extends Fragment{

    @BindView(R.id.fragConsultantSalaryList_rcv)        RecyclerView rcv;
    @BindView(R.id.fragConsultantSalaryList_vLoading)   View vLoading;

    private JoRecyclerAdapter<MutationModel> adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_consultant_salarylist, null);
        ButterKnife.bind(this, v);

        String type = getArguments().getString("TYPE");

        adapter = new JoRecyclerAdapter<>(new JoRecyclerAdapter.Params()
                .setRecyclerView(rcv)
                .addParam("TYPE", type)
                .setFooterViewHolderCls(BlankVHolder.class)
                .setItemViewHolderCls(ConsultantSalaryVHolder.class)
                .setEmptyView(R.id.layerEmpty_vContents)
        );

        adapter.addFooterItem("A");
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        loadMutations(
                ConsultantLoggedIn.get().consultant.dokuToken,
                ConsultantLoggedIn.get().consultant.dokuSystrace,
                ConsultantLoggedIn.get().consultant.dokuId
        );
//        refreshToken(OkhomeUtil.getRandomString());
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    //get first
    private void loadMutations(String token, String systrace, String accountId){
        vLoading.setVisibility(View.VISIBLE);
        DokuWallet.getActivities(token, systrace, accountId).enqueue(new RetrofitCallback<ActivitiesModel>() {
            @Override
            public void onSuccess(ActivitiesModel result) {
                if ((result.responseCode.equals("3011") || result.responseCode.equals("3010"))) {
                    refreshToken(OkhomeUtil.getRandomString());
                } else if (result.mutasi != null) {
                    List<MutationModel> mutationList = new ArrayList<>();
                    for (MutationModel mutation : result.mutasi) {
                        if (Objects.equals(getArguments().getString("TYPE"), "COMPLETE")) {
                            if (mutation.type.equals("D")) {
                                mutationList.add(mutation);
                            }
                        } else {
                            if (mutation.type.equals("C")) {
                                mutationList.add(mutation);
                            }
                        }
                    }
                    adapter.setListItems(mutationList);
                    adapter.notifyDataSetChanged();
                } else {
                    System.out.println("Error " + result.responseCode + ": " + result.responseMessage);
                }
                System.out.println("Result: " + result.responseCode + " / " + result.responseMessage);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                vLoading.setVisibility(View.GONE);
            }
        });
    }

    private void refreshToken(final String systrace) {
        DokuWallet.signOn(systrace).enqueue(new RetrofitCallback<TokenModel>() {
            @Override
            public void onSuccess(TokenModel result) {
                System.out.println("DOKU APP --------------------- SUCCESSSSS");
                System.out.println(result.responseMessage);
                System.out.println(result.responseCode);
                System.out.println("ACCESSTOKEN: "+result.accessToken);

                if (result.responseCode.equals("3011") || result.responseCode.equals("3010")) {
                    refreshToken(OkhomeUtil.getRandomString());
                } else {
//                    loadMutations(result.accessToken, systrace, ConsultantLoggedIn.get().consultant.dokuId);
                    saveTokenAndSystrace(result.accessToken, systrace);
                }
            }
        });
    }

    private void saveTokenAndSystrace(final String token, final String systrace) {
        String jsonParam = new Gson().toJson(OkhomeUtil.makeMap("dokuToken", token, "dokuSystrace", systrace));
        OkhomeRestApi.getConsultantClient().update(ConsultantLoggedIn.get().id, jsonParam)
                .enqueue(new RetrofitCallback<String>() {
                    @Override
                    public void onSuccess(String account) {
                        System.out.println(account + "DOKU token and Systrace have been successfully saved to the database.");
                        loadMutations(token, systrace, ConsultantLoggedIn.get().consultant.dokuId);
                    }
                });
    }
}