package id.co.okhome.consultant.view.viewholder;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.TextView;
import com.mrjodev.jorecyclermanager.JoViewHolder;
import com.mrjodev.jorecyclermanager.annotations.LayoutMatcher;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.okhome.consultant.R;

/**
 * Created by frizurd on 26/03/2018.
 */

@LayoutMatcher(layoutId = R.layout.item_faq_element)
public class FaqVHolder extends JoViewHolder<String> implements View.OnClickListener {

    public final static String TAG_ITEM_CLICK = "TAG_ITEM_CLICK";
    public final static String TAG_ITEM_ACT = "TAG_ITEM_ACTVITY";
    public final static String TAG_ITEM_LIST_REALVALUE = "TAG_ITEM_LIST_REALVALUE";

    @BindView(R.id.itemFaqs_tvTitle)    TextView tvTitle;

    public FaqVHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void onViewCreated() {
        ButterKnife.bind(this, getView());
    }

    @Override
    public void onBind(String s, int pos, int absPos) {
        super.onBind(s, pos, absPos);
        tvTitle.setText(s);

        getView().setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String item = getModel();
        int pos = getPos();

        FaqVHolder.ItemClickListener itemClickListener = getRcvParams().getParam(TAG_ITEM_CLICK);
        List<String> listRealValue = getRcvParams().getParam(TAG_ITEM_LIST_REALVALUE);
        if(itemClickListener != null) {
            String tag = null;
            if(listRealValue != null) {
                tag = listRealValue.get(pos);
            }
            itemClickListener.onItemClick(pos, item, tag);
        }
    }

    public interface ItemClickListener{
        void onItemClick(int pos, String value, String tag);
    }
}
