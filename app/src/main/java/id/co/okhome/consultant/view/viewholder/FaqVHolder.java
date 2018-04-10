package id.co.okhome.consultant.view.viewholder;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.mrjodev.jorecyclermanager.JoViewHolder;
import com.mrjodev.jorecyclermanager.annotations.LayoutMatcher;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.okhome.consultant.R;
import id.co.okhome.consultant.model.FaqModel;
import id.co.okhome.consultant.view.faq.FaqActivity;
import id.co.okhome.consultant.view.faq.FaqSingleActivity;

/**
 * Created by frizurd on 26/03/2018.
 */

@LayoutMatcher(layoutId = R.layout.item_faq_element)
public class FaqVHolder extends JoViewHolder<FaqModel> implements View.OnClickListener {

    @BindView(R.id.itemFaqs_tvTitle)    TextView tvTitle;

    public FaqVHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void onViewCreated() {
        ButterKnife.bind(this, getView());
    }

    @Override
    public void onBind(FaqModel m, int pos, int absPos) {
        super.onBind(m, pos, absPos);
        tvTitle.setText(m.subject);

        getView().setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        FaqModel faq = getModel();

        if (faq.childCount == 0) {
            getContext().startActivity(new Intent(getContext(), FaqSingleActivity.class)
                    .putExtra("FAQ_ID", faq.id));
        } else {
            getContext().startActivity(new Intent(getContext(), FaqActivity.class)
                    .putExtra("FAQ_CATEGORY", faq.category)
                    .putExtra("FAQ_ID", faq.id)
                    .putExtra("FAQ_TITLE", faq.subject));
        }
    }
}