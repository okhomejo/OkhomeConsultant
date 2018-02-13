package id.co.okhome.consultant.view.userinfo.trainee;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.okhome.consultant.R;
import id.co.okhome.consultant.adapter.RegionListAdapter;
import id.co.okhome.consultant.lib.app.OkHomeParentActivity;
import id.co.okhome.consultant.lib.app.OkhomeUtil;
import id.co.okhome.consultant.lib.retrofit.RetrofitCallback;
import id.co.okhome.consultant.model.WorkingRegionModel;
import id.co.okhome.consultant.rest_apicall.retrofit_restapi.OkhomeRestApi;
import id.co.okhome.consultant.view.common.dialog.CommonListDialog;
import id.co.okhome.consultant.view.viewholder.StringHolder;

/**
 * Created by frizurd on 07/02/2018.
 */

public class UpdateConsultantAreaActivity extends OkHomeParentActivity {

    @BindView(R.id.actArea_lvRegions)     ListView itemsListView;
    private RegionListAdapter regionAdapter;
    private List<WorkingRegionModel> allRegions;
    private Set<Integer> chosenRegions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_prefered_area);
        ButterKnife.bind(this);

        init();
    }

    private void init(){
        chosenRegions = new HashSet<>();
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
                regionAdapter = new RegionListAdapter(UpdateConsultantAreaActivity.this, parentRegions);
                itemsListView.setAdapter(regionAdapter);

                itemsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                        callRegionDialog(regionAdapter.getItem(pos));
                    }
                });
            }
        });
    }

    private void callRegionDialog(WorkingRegionModel region) {

        final List<WorkingRegionModel> childRegionList = new ArrayList<>();
        List<String> childRegionStrings = new ArrayList<>();

        for (WorkingRegionModel childRegion : allRegions) {
            if (region.id == childRegion.parentId) {
                childRegionList.add(childRegion);
                childRegionStrings.add(childRegion.address);
            }
        }
        new CommonListDialog(UpdateConsultantAreaActivity.this)
                .setTitle(region.address)
                .setListItems(childRegionStrings)
                .setColumnCount(1)
                .setItemClickListener(new StringHolder.ItemClickListener() {
                    @Override
                    public void onItemClick(Dialog dialog, int pos, String value, String tag) {

                        chosenRegions.add(childRegionList.get(pos).id);
                        if (childRegionList.get(pos).childCount != 0) {
                            callRegionDialog(childRegionList.get(pos));
                        }
                    }
                })
                .show();
    }

//    private void getParentRegions() {
//        OkhomeRestApi.getCommonClient().getAllWorkingRegion().enqueue(new RetrofitCallback<List<WorkingRegionModel>>() {
//
//            @Override
//            public void onSuccess(List<WorkingRegionModel> result) {
//
//                List<WorkingRegionModel> parentRegions = new ArrayList<>();
//
//                for (WorkingRegionModel region : result) {
//                    if (region.parentId == 0) {
//                        parentRegions.add(region);
//                    }
//                }
//                regionAdapter = new RegionListAdapter(UpdateConsultantAreaActivity.this, parentRegions);
//                itemsListView.setAdapter(regionAdapter);
//
//                itemsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
//                        getChildRegions(regionAdapter.getItem(pos).id, regionAdapter.getItem(pos).address);
//                    }
//                });
//            }
//        });
//    }

//    private void getChildRegions(int id, final String address) {
//        OkhomeRestApi.getCommonClient().getWorkingRegion(id).enqueue(new RetrofitCallback<List<WorkingRegionModel>>() {
//
//            @Override
//            public void onSuccess(final List<WorkingRegionModel> result) {
//
//                final List<String> childRegionStrings = new ArrayList<>();
//                for (WorkingRegionModel region:result) {
//                    childRegionStrings.add(region.address);
//                }
//
//                new CommonListDialog(UpdateConsultantAreaActivity.this)
//                        .setTitle(address)
//                        .setListItems(childRegionStrings)
//                        .setColumnCount(1)
//                        .setItemClickListener(new StringHolder.ItemClickListener() {
//                            @Override
//                            public void onItemClick(Dialog dialog, int pos, String value, String tag) {
//                                getChildRegions(dialog., regionAdapter.getItem(pos).address);
//                                dialog.dismiss();
//                            }
//                        })
//                        .show();
//            }
//        });
//    }

    @OnClick(R.id.actArea_vbtnX)
    public void onGoBackClick() {
        finish();
    }

    @OnClick(R.id.actArea_vbtnOk)
    public void onButtonSubmit() {
        finish();
    }
}