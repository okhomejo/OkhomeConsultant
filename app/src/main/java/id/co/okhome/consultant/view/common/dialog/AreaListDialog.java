package id.co.okhome.consultant.view.common.dialog;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.okhome.consultant.R;
import id.co.okhome.consultant.adapter.ChildAreaListAdapter;
import id.co.okhome.consultant.lib.app.OkhomeUtil;
import id.co.okhome.consultant.lib.dialog.DialogParent;
import id.co.okhome.consultant.model.WorkingRegionModel;

import static id.co.okhome.consultant.lib.dialog.DialogParent.CommonDialogListener.ACTIONCODE_NO;

/**
 * Created by frizurd on 14/02/2018.
 */

public class AreaListDialog extends DialogParent {

    @BindView(R.id.dialogAlertList_tvSubTitle)    TextView tvSubTitle;
    @BindView(R.id.dialogAlertList_tvTitle)       TextView tvTitle;
    @BindView(R.id.dialogAlertList_list)          ListView listView;

    private Context context;
    private ChildAreaListAdapter regionAdapter;
    private List<WorkingRegionModel> listItems;
    private List<WorkingRegionModel> allRegions;
    private Set<Integer> chosenRegions;
    private String title, subTitle;
    private AdapterView.OnItemClickListener itemClickListener;

    public AreaListDialog(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public int onInit() {
        return R.layout.dialog_area_list;
    }

    @Override
    public void onCreate() {
        ButterKnife.bind(this, getDecorView());
        //view control
        tvTitle.setVisibility(View.GONE);
        tvSubTitle.setVisibility(View.GONE);
        tvSubTitle.setVisibility(View.GONE);
        if(title != null){
            tvTitle.setText(title);
            tvTitle.setVisibility(View.VISIBLE);
        }

        if(subTitle != null){
            tvSubTitle.setText(subTitle);
            tvSubTitle.setVisibility(View.VISIBLE);
        }

        //set list
        adaptList();
    }

    @Override
    public void onShow() {
    }

    private void adaptList() {
        regionAdapter = new ChildAreaListAdapter(context, listItems, chosenRegions, allRegions);
        listView.setAdapter(regionAdapter);
        listView.setOnItemClickListener(itemClickListener);

    }

    public void updateChildRegionList() {
        regionAdapter.notifyDataSetChanged();
    }

    @OnClick(R.id.dialogAlertList_vbtnBack)
    public void x(){
        dismiss();
    }


    //-----------begin setting params
    public AreaListDialog setTitle(String title) {
        this.title = title;
        return this;
    }

    public AreaListDialog setSubTitle(String subTitle) {
        this.subTitle = subTitle;
        return this;
    }

    @Override
    public AreaListDialog setCommonDialogListener(CommonDialogListener commonDialogListener) {
        super.setCommonDialogListener(commonDialogListener);
        return this;
    }

    public AreaListDialog setListItems(List<WorkingRegionModel> listItems) {
        this.listItems = listItems;
        return this;
    }

    public AreaListDialog setAllRegionItems(List<WorkingRegionModel> allRegions) {
        this.allRegions = allRegions;
        return this;
    }

    public AreaListDialog setChosenRegions(Set<Integer> chosenRegions) {
        this.chosenRegions = chosenRegions;
        return this;
    }

    public AreaListDialog setItemClickListener(AdapterView.OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
        return this;
    }

    //-----------end setting params

}
