package id.co.okhome.consultant.view.userinfo.trainee;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.okhome.consultant.R;
import id.co.okhome.consultant.adapter.ExpandableListAdapter;
import id.co.okhome.consultant.lib.app.OkHomeParentActivity;
import id.co.okhome.consultant.lib.app.OkhomeUtil;
import id.co.okhome.consultant.lib.retrofit.RetrofitCallback;
import id.co.okhome.consultant.lib.retrofit.restmodel.ErrorModel;
import id.co.okhome.consultant.model.WorkingRegionModel;
import id.co.okhome.consultant.rest_apicall.retrofit_restapi.OkhomeRestApi;

/**
 * Created by frizurd on 07/02/2018.
 */

public class UpdateConsultantAreaActivity extends OkHomeParentActivity {

    @BindView(R.id.actArea_lvExp)      ExpandableListView expListView;

    private ExpandableListAdapter listAdapter;
    private List<String> listDataHeader;
    private HashMap<String, List<String>> listDataChild;
    private List<WorkingRegionModel> workingRegions = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_prefered_area);
        ButterKnife.bind(this);

        init();
    }

    private void init(){

        // preparing list data
        prepareListData();

        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);

        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                Toast.makeText(
                        getApplicationContext(),
                        listDataHeader.get(groupPosition)
                                + " : "
                                + listDataChild.get(
                                listDataHeader.get(groupPosition)).get(
                                childPosition), Toast.LENGTH_SHORT)
                        .show();
                return false;
            }
        });

        // Listview Group expanded listener
        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        listDataHeader.get(groupPosition) + " Expanded",
                        Toast.LENGTH_SHORT).show();
            }
        });

        // Listview Group collasped listener
        expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        listDataHeader.get(groupPosition) + " Collapsed",
                        Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void prepareListData() {


        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        fetchChildRegions(0);

        listDataHeader.add("Jakarta1");
        listDataHeader.add("Jakarta2");
        listDataHeader.add("Jakarta3");

        List<String> Jakartax1 = new ArrayList<String>();
        Jakartax1.add("kemang 1");
        Jakartax1.add("kemang 2");

        List<String> Jakartax2 = new ArrayList<String>();
        Jakartax2.add("kemang 3");
        Jakartax2.add("kemang 4");

        List<String> Jakartax3 = new ArrayList<String>();
        Jakartax3.add("kemang 5");
        Jakartax3.add("kemang 6");

        listDataChild.put(listDataHeader.get(0), Jakartax1);
        listDataChild.put(listDataHeader.get(1), Jakartax2);
        listDataChild.put(listDataHeader.get(2), Jakartax3);
    }

    public void fetchChildRegions(int id) {
        OkhomeRestApi.getCommonClient().getWorkingRegion(id).enqueue(new RetrofitCallback<List<WorkingRegionModel>>() {
            @Override
            public void onFinish() {
                super.onFinish();
            }

            @Override
            public void onSuccess(List<WorkingRegionModel> result) {
                workingRegions.addAll(result);

//                int counter = 0;
//                for (WorkingRegionModel region : result) {
//                    listDataHeader.add(region.address);
//                    counter ++;
//                }
//                OkhomeUtil.showToast(UpdateConsultantAreaActivity.this, "Amount is: " + counter);
            }

            @Override
            public void onJodevError(ErrorModel jodevErrorModel) {
                super.onJodevError(jodevErrorModel);
            }
        });
    }

    @OnClick(R.id.actArea_vbtnX)
    public void onGoBackClick() {
        finish();
    }

    @OnClick(R.id.actArea_vbtnOk)
    public void onButtonSubmit() {
        finish();
    }
}