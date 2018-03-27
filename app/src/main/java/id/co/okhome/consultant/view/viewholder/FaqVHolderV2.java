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
import id.co.okhome.consultant.view.main.trainee.TraineeFaqActivity;

/**
 * Created by frizurd on 26/03/2018.
 */

@LayoutMatcher(layoutId = R.layout.item_faq_element)
public class FaqVHolderV2 extends JoViewHolder<FaqModel> implements View.OnClickListener {

    @BindView(R.id.itemFaqs_tvTitle)    TextView tvTitle;

    public FaqVHolderV2(View itemView) {
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
//            Intent intent = new Intent(getContext(), TraineeFaqSingleActivity.class);
//            intent.putExtra("FAQ_ID", faq.id);
//            getContext().startActivity(intent);

            getContext().startActivity(new Intent(getContext(), TraineeFaqActivity.class)
                    .putExtra("FAQ_ID", faq.id));
        } else {

            getContext().startActivity(new Intent(getContext(), TraineeFaqActivity.class)
                    .putExtra("FAQ_ID", faq.id)
                    .putExtra("FAQ_TITLE", faq.subject));
        }
    }

}
