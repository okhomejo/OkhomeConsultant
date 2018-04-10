package id.co.okhome.consultant.view.userinfo.trainee;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;

import com.mrjodev.jorecyclermanager.JoRecyclerAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.okhome.consultant.R;
import id.co.okhome.consultant.lib.app.ConsultantLoggedIn;
import id.co.okhome.consultant.lib.app.OkHomeParentActivity;
import id.co.okhome.consultant.lib.app.OkhomeUtil;
import id.co.okhome.consultant.lib.retrofit.RetrofitCallback;
import id.co.okhome.consultant.model.WorkingRegionModel;
import id.co.okhome.consultant.model.v2.ProfileModel;
import id.co.okhome.consultant.rest_apicall.retrofit_restapi.OkhomeRestApi;
import id.co.okhome.consultant.view.common.dialog.AreaListDialog;
import id.co.okhome.consultant.view.viewholder.BlankVHolder;
import id.co.okhome.consultant.view.viewholder.PreferredAreaVHolder;

/**
 * Created by frizurd on 07/02/2018.
 */

public class UpdateConsultantAreaActivity extends OkHomeParentActivity {

    @BindView(R.id.actArea_rcv)     RecyclerView rcv;

    private List<WorkingRegionModel> allRegions;
    private Set<Integer> chosenRegions, finalRegions;
    private AreaListDialog parentDialog, childDialog;
    private boolean savedSelection = false;
    private JoRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_prefered_area);
        OkhomeUtil.setSystemBarColor(this,
                ContextCompat.getColor(this, R.color.colorOkhome)
        );
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        ProfileModel profile = ConsultantLoggedIn.get().profile;
        if (profile.workingRegions != null && !Objects.equals(profile.workingRegions, "")) {
            List<String> regionStringList = Arrays.asList(profile.workingRegions.split(","));
            chosenRegions = new HashSet<>(regionStringList.size());
            for (String current : regionStringList) {
                chosenRegions.add(Integer.parseInt(current));
            }
        } else {
            chosenRegions = new HashSet<>();
        }
        allRegions      = new ArrayList<>();
        finalRegions    = new HashSet<>();

        if (!chosenRegions.isEmpty()) {
            finalRegions.addAll(chosenRegions);
        }
        getAndSaveAllRegions();
    }

    private void initAdapter() {
        adapter = new JoRecyclerAdapter(new JoRecyclerAdapter.Params()
                .setRecyclerView(rcv)
                .addParam("allRegions", allRegions)
                .addParam("chosenRegions", chosenRegions)
                .setItemViewHolderCls(PreferredAreaVHolder.class)
                .setFooterViewHolderCls(BlankVHolder.class)
        );
        adapter.addFooterItem("");
    }

    private void getAndSaveAllRegions() {
        final ProgressDialog p = ProgressDialog.show(this, null, "Loading regions...");
        OkhomeRestApi.getCommonClient().getAllWorkingRegion().enqueue(new RetrofitCallback<List<WorkingRegionModel>>() {

            @Override
            public void onSuccess(List<WorkingRegionModel> result) {
                allRegions = result;
                List<WorkingRegionModel> parentRegions = new ArrayList<>();

                for (WorkingRegionModel region : result) {
                    if (region.parentId == 0) {
                        parentRegions.add(region);
                    }
                }
                initAdapter();
                adapter.setListItems(parentRegions);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                p.dismiss();
            }
        });
    }

    public void callChildRegionDialog(final WorkingRegionModel region) {

        if(childDialog != null && childDialog.isShowing()) return;

        final List<WorkingRegionModel> childRegionList = new ArrayList<>();
        for (WorkingRegionModel childRegion : allRegions) {
            if (region.id == childRegion.parentId) {
                childRegionList.add(childRegion);
            }
        }
        final AreaListDialog areaDialog = new AreaListDialog(UpdateConsultantAreaActivity.this);
        areaDialog.setTitle(region.address)
                .setListItems(childRegionList)
                .setAllRegionItems(allRegions)
                .setChosenRegions(chosenRegions);

        if (parentDialog != null && parentDialog.isShowing()) {
            childDialog = areaDialog;
            childDialog.show();
        } else {
            parentDialog = areaDialog;
            parentDialog.show();
        }

        parentDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                if (!savedSelection) {
                    chosenRegions.clear();
                    chosenRegions.addAll(finalRegions);
                } else {
                    savedSelection = false;
                }
            }
        });
    }

    public void saveAndCloseDialog() {
        savedSelection = true;

        finalRegions.clear();
        finalRegions.addAll(chosenRegions);

        if (childDialog != null && childDialog.isShowing())
            childDialog.dismiss();
        if (parentDialog != null && parentDialog.isShowing())
            parentDialog.dismiss();

        adapter.notifyDataSetChanged();
    }

    private void updateWorkingRegions(){

        final ProgressDialog p = ProgressDialog.show(this, null, "Updating regions...");

        String regionString = "";
        for (Integer id : chosenRegions) {
            if (Objects.equals(regionString, "")) {
                regionString = Integer.toString(id);
            } else {
                regionString = regionString + "," + id;
            }
        }
        ConsultantLoggedIn.updateUserInfo(
                OkhomeUtil.makeMap(
                        "working_regions", regionString),
                new RetrofitCallback<String>() {

                    @Override
                    public void onSuccess(String result) {
                        finish();
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        p.dismiss();
                    }
                }
        );
    }

    @OnClick(R.id.actArea_vbtnX)
    public void onGoBackClick() {
        finish();
    }

    @OnClick(R.id.actArea_vbtnOk)
    public void onButtonSubmit() {
        updateWorkingRegions();
    }
}