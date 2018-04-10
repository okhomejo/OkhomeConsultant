package id.co.okhome.consultant.view.dialog;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.mrjodev.jorecyclermanager.JoRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.okhome.consultant.R;
import id.co.okhome.consultant.lib.dialog.DialogParent;
import id.co.okhome.consultant.view.viewholder.StringHolder;

import static id.co.okhome.consultant.lib.dialog.DialogParent.CommonDialogListener.ACTIONCODE_NO;

/**
 * Created by jo on 2018-01-24.
 */

public class CommonListDialog extends DialogParent{

    @BindView(R.id.dialogCommonList_tvSubTitle)    TextView tvSubTitle;
    @BindView(R.id.dialogCommonList_tvTitle)       TextView tvTitle;
    @BindView(R.id.dialogCommonList_rcv)             RecyclerView rcv;

    List<String> listItems;
    List<String> listItemsValue;
    String title, subTitle;
    int columnCount = 1;

    JoRecyclerAdapter adapter;
    StringHolder.ItemClickListener itemClickListener;

    public CommonListDialog(Context context) {
        super(context);
    }

    @Override
    public int onInit() {
        return R.layout.dialog_common_list;
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

    private void adaptList(){
        JoRecyclerAdapter.Params params = new JoRecyclerAdapter.Params();
        params.setRecyclerView(rcv)
                .addParam(StringHolder.TAG_ITEM_DIALOG, this)
                .addParam(StringHolder.TAG_ITEM_CLICK, itemClickListener)
                .addParam(StringHolder.TAG_ITEM_LIST_REALVALUE, listItemsValue)
                .setItemViewHolderCls(StringHolder.class);

        if(columnCount > 0){
            params.setGridLayoutManager(columnCount);
        }

        adapter = new JoRecyclerAdapter(params);
        adapter.setListItems(listItems);
    }

    @Override
    public void onShow() {

    }


    @OnClick(R.id.dialogCommonList_vbtnX)
    public void x(){
        dismiss();
        if(commonDialogListener!= null){
            commonDialogListener.onCommonDialogWorkDone(this, ACTIONCODE_NO, null);
        }
    }

    //-----------begin setting params
    public CommonListDialog setTitle(String title) {
        this.title = title;
        return this;
    }

    public CommonListDialog setSubTitle(String subTitle) {
        this.subTitle = subTitle;
        return this;
    }

    @Override
    public CommonListDialog setCommonDialogListener(CommonDialogListener commonDialogListener) {
        super.setCommonDialogListener(commonDialogListener);
        return this;
    }

    public CommonListDialog setListItems(List<String> listItems) {
        this.listItems = listItems;
        return this;
    }


    public CommonListDialog setArrItems(String... items) {
        listItems = new ArrayList<>();
        for(String s : items){
            listItems.add(s);
        }
        return this;
    }

    public CommonListDialog setColumnCount(int columnCount) {
        this.columnCount = columnCount;
        return this;
    }

    public CommonListDialog setArrItemTag(String ... values) {
        listItemsValue = new ArrayList<>();
        for(String s : values){
            listItemsValue.add(s);
        }
        return this;
    }


    public CommonListDialog setItemClickListener(StringHolder.ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
        return this;
    }

    //-----------end setting params

}
