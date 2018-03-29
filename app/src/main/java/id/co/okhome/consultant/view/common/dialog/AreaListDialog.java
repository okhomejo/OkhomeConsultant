package id.co.okhome.consultant.view.common.dialog;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mrjodev.jorecyclermanager.JoRecyclerAdapter;

import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.okhome.consultant.R;
import id.co.okhome.consultant.lib.app.OkhomeUtil;
import id.co.okhome.consultant.lib.dialog.DialogParent;
import id.co.okhome.consultant.model.WorkingRegionModel;
import id.co.okhome.consultant.view.userinfo.trainee.UpdateConsultantAreaActivity;
import id.co.okhome.consultant.view.viewholder.BlankHolder;
import id.co.okhome.consultant.view.viewholder.PreferredChildAreaVHolder;

/**
 * Created by frizurd on 14/02/2018.
 */

public class AreaListDialog extends DialogParent {

    @BindView(R.id.dialogAreaList_btnConfirm)    Button btnConfirm;
    @BindView(R.id.dialogAreaList_tvTitle)       TextView tvTitle;
    @BindView(R.id.dialogAreaList_rcv)           RecyclerView rcv;

    private String title;
    private Context context;
    private Set<Integer> chosenRegions;
    private List<WorkingRegionModel> listItems, allRegions;
    private JoRecyclerAdapter adapter;

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
        if(title != null){
            tvTitle.setText(title);
            tvTitle.setVisibility(View.VISIBLE);
        }
        initAdapter();
    }

    @Override
    public void onShow() {
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (hasFocus) {
            adapter.notifyDataSetChanged();
        }
        super.onWindowFocusChanged(hasFocus);
    }

    private void initAdapter() {
        adapter = new JoRecyclerAdapter(new JoRecyclerAdapter.Params()
                .setRecyclerView(rcv)
                .addParam("allRegions", allRegions)
                .addParam("chosenRegions", chosenRegions)
                .setItemViewHolderCls(PreferredChildAreaVHolder.class)
                .setFooterViewHolderCls(BlankHolder.class)
        );
        adapter.addFooterItem("");
        adapter.setListItems(listItems);
    }

    @OnClick(R.id.dialogAreaList_vbtnBack)
    public void x(){
        dismiss();
    }

    @OnClick(R.id.dialogAreaList_btnConfirm)
    public void confirmSelection(){
        UpdateConsultantAreaActivity areaActivity = (UpdateConsultantAreaActivity) context;
        areaActivity.saveAndCloseDialog();
    }

    //-----------begin setting params
    public AreaListDialog setTitle(String title) {
        this.title = title;
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
    //-----------end setting params
}
