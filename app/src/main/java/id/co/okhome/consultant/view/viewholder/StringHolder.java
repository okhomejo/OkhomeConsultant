package id.co.okhome.consultant.view.viewholder;

import android.app.Dialog;
import android.view.View;
import android.widget.TextView;

import com.mrjodev.jorecyclermanager.JoViewHolder;
import com.mrjodev.jorecyclermanager.annotations.LayoutMatcher;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.okhome.consultant.R;

/**
 * Created by josongmin on 2016-08-17.
 */
@LayoutMatcher(layoutId = R.layout.item_string)
public class StringHolder extends JoViewHolder<String> implements View.OnClickListener{

    public final static String TAG_ITEM_CLICK = "TAG_ITEM_CLICK";
    public final static String TAG_ITEM_DIALOG = "TAG_ITEM_DIALOG";
    public final static String TAG_ITEM_LIST_REALVALUE = "TAG_ITEM_LIST_REALVALUE";

    @BindView(R.id.itemString_tvText)
    TextView tvText;

    public StringHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void onViewCreated() {
        ButterKnife.bind(this, getView());
    }

    @Override
    public void onBind(String s, int pos, int absPos) {
        super.onBind(s, pos, absPos);
        tvText.setText(s);

        getView().setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String item = getModel();
        int pos = getPos();

        Dialog dialog = getRcvParams().getParam(TAG_ITEM_DIALOG);
        ItemClickListener itemClickListener = getRcvParams().getParam(TAG_ITEM_CLICK);
        List<String> listRealValue = getRcvParams().getParam(TAG_ITEM_LIST_REALVALUE);
        if(itemClickListener != null){
            String tag = null;
            if(listRealValue != null){
                tag = listRealValue.get(pos);
            }
            itemClickListener.onItemClick(dialog, pos, item, tag);
        }
    }

    public interface ItemClickListener{
        void onItemClick(Dialog dialog, int pos, String value, String tag);
    }

}