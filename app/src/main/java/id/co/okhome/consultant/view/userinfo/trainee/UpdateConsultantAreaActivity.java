package id.co.okhome.consultant.view.userinfo.trainee;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

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
import id.co.okhome.consultant.adapter.AreaListAdapter;
import id.co.okhome.consultant.lib.app.ConsultantLoggedIn;
import id.co.okhome.consultant.lib.app.OkHomeParentActivity;
import id.co.okhome.consultant.lib.app.OkhomeUtil;
import id.co.okhome.consultant.lib.retrofit.RetrofitCallback;
import id.co.okhome.consultant.model.ConsultantModel;
import id.co.okhome.consultant.model.WorkingRegionModel;
import id.co.okhome.consultant.rest_apicall.retrofit_restapi.OkhomeRestApi;
import id.co.okhome.consultant.view.common.dialog.AreaListDialog;

/**
 * Created by frizurd on 07/02/2018.
 */

public class UpdateConsultantAreaActivity extends OkHomeParentActivity {

    @BindView(R.id.actArea_lvRegions)     ListView itemsListView;

    private List<WorkingRegionModel> allRegions;
    private AreaListAdapter regionAdapter;
    private Set<Integer> chosenRegions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_prefered_area);
        ButterKnife.bind(this);

        init();
    }

    private void init(){
        if (ConsultantLoggedIn.hasSavedData()) {
            ConsultantModel consultant = ConsultantLoggedIn.get();
            if (!Objects.equals(consultant.workingRegions, "")) {
                List<String> regionStringList = Arrays.asList(consultant.workingRegions.split(","));
                chosenRegions = new HashSet<>(regionStringList.size());
                for (String current : regionStringList){
                    chosenRegions.add(Integer.parseInt(current));
                }
            } else {
                chosenRegions = new HashSet<>();
            }
        } else {
            chosenRegions = new HashSet<>();
        }
        getAllRegions();
    }

    private void getAllRegions() {
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
                regionAdapter = new AreaListAdapter(UpdateConsultantAreaActivity.this, parentRegions, chosenRegions, allRegions);
                itemsListView.setAdapter(regionAdapter);

                itemsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                        callChildRegionDialog(regionAdapter.getItem(pos));
                    }
                });
            }
        });
    }

    private void callChildRegionDialog(final WorkingRegionModel region) {

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
                .setChosenRegions(chosenRegions)
                .setItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        final WorkingRegionModel region = childRegionList.get(i);

                        if (region.childCount != 0) {
                            callChildRegionDialog(region);
                        }

                        final LinearLayout btnCheck = view.findViewById(R.id.itemAreaChild_vbtnCheck);
                        btnCheck.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                ImageView imageCheck = view.findViewById(R.id.itemAreaChild_ivCheck);
                                if (region.childCount == 0) {
                                    if (chosenRegions.contains(region.id)) {
                                        chosenRegions.remove(region.id);
                                    } else {
                                        chosenRegions.add(region.id);
                                        OkhomeUtil.showToast(UpdateConsultantAreaActivity.this, "Added child");
                                    }
                                } else {
                                    if (imageCheck.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.ic_checked_sq).getConstantState()) {
                                        for (WorkingRegionModel newRegion : allRegions) {
                                            if (newRegion.parentId == region.id) {
                                                chosenRegions.remove(newRegion.id);
                                                OkhomeUtil.showToast(UpdateConsultantAreaActivity.this, "All children removed");
                                            }
                                        }
                                    } else {
                                        for (WorkingRegionModel newRegion : allRegions) {
                                            if (newRegion.parentId == region.id) {
                                                chosenRegions.add(newRegion.id);
                                                OkhomeUtil.showToast(UpdateConsultantAreaActivity.this, "Added all children of root");
                                            }
                                        }
                                    }
                                }
                                areaDialog.updateChildRegionList();
                                regionAdapter.notifyDataSetChanged();
                            }
                        });
                    }
                });
        areaDialog.show();
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