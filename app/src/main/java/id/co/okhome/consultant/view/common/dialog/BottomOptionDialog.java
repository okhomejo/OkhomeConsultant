package id.co.okhome.consultant.view.common.dialog;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.mrjodev.jorecyclermanager.JoRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.okhome.consultant.R;
import id.co.okhome.consultant.lib.dialog.DialogParent;
import id.co.okhome.consultant.view.viewholder.StringHolder;

/**
 * Created by frizurd on 14/03/2018.
 */

public class BottomOptionDialog extends DialogParent {

    @BindView(R.id.dialogBottomOptions_rcv)    RecyclerView rcv;

    private List<String> listItemsValue, listItems;
    private StringHolder.ItemClickListener itemClickListener;
    private JoRecyclerAdapter adapter;

    public BottomOptionDialog(Context context) {
        super(context);
    }

    @Override
    public int onInit() {
        return R.layout.dialog_bottom_options;
    }

    @Override
    public void onCreate() {
        ButterKnife.bind(this, getDecorView());
        adaptList();
    }

    @Override
    public void onShow() {
    }

    private void adaptList(){
        JoRecyclerAdapter.Params params = new JoRecyclerAdapter.Params();
        params.setRecyclerView(rcv)
                .addParam(StringHolder.TAG_ITEM_DIALOG, this)
                .addParam(StringHolder.TAG_ITEM_CLICK, itemClickListener)
                .addParam(StringHolder.TAG_ITEM_LIST_REALVALUE, listItemsValue)
                .setItemViewHolderCls(StringHolder.class);

        adapter = new JoRecyclerAdapter(params);
        adapter.setListItems(listItems);
    }


    public BottomOptionDialog setItemClickListener(StringHolder.ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
        return this;
    }

    public BottomOptionDialog setListItems(List<String> listItems) {
        this.listItems = listItems;
        return this;
    }


    public BottomOptionDialog setArrItems(String... items) {
        listItems = new ArrayList<>();
        for(String s : items){
            listItems.add(s);
        }
        return this;
    }

    public BottomOptionDialog setArrItemTag(String ... values) {
        listItemsValue = new ArrayList<>();
        for(String s : values){
            listItemsValue.add(s);
        }
        return this;
    }
}
