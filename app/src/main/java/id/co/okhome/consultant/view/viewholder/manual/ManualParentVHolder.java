package id.co.okhome.consultant.view.viewholder.manual;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.mrjodev.jorecyclermanager.JoViewHolder;
import com.mrjodev.jorecyclermanager.annotations.LayoutMatcher;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.okhome.consultant.R;
import id.co.okhome.consultant.model.FaqModel;
import id.co.okhome.consultant.view.activity.faq.FaqActivity;
import id.co.okhome.consultant.view.activity.faq.FaqSingleActivity;

/**
 * Created by josongmin on 2016-06-28.
 */

@LayoutMatcher(layoutId = R.layout.item_manual_parentitem)
public class ManualParentVHolder extends JoViewHolder<FaqModel> implements View.OnClickListener{

    @BindView(R.id.itemManualParentItem_tvTitle)
    TextView tvTitle;

    public ManualParentVHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void onViewCreated() {
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void onBind(final FaqModel faq, int pos, int absPos) {
        super.onBind(faq, pos, absPos);

        tvTitle.setText(faq.subject);
        getView().setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        FaqModel faq = getModel();
        if(faq.childCount > 0){
            FaqActivity.startFaqActivity(getContext(), faq.subject, faq.category, faq.id);
        }else{
            getContext().startActivity(new Intent(getContext(), FaqSingleActivity.class)
                    .putExtra("FAQ_ID", faq.id));
        }
    }
}
