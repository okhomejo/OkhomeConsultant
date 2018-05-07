package id.co.okhome.consultant.view.fragment;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.mrjodev.jorecyclermanager.JoRecyclerAdapter;

import java.sql.Array;
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
import id.co.okhome.consultant.model.v2.AccountModel;
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
        adaptDataAndViews();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void adaptDataAndViews() {

        List<MutationModel> mutationList = getArguments().getParcelableArrayList("MUTATION_LIST");

        if (mutationList != null) {
            List<MutationModel> specificMutations = new ArrayList<>();
            for (MutationModel mutation : mutationList) {
                if (Objects.equals(getArguments().getString("TYPE"), "COMPLETE")) {
                    if (mutation.type.equals("D")) {
                        specificMutations.add(mutation);
                    }
                } else {
                    if (mutation.type.equals("C")) {
                        specificMutations.add(mutation);
                    }
                }
            }
            adapter.setListItems(specificMutations);
            adapter.notifyDataSetChanged();
        }
    }
}