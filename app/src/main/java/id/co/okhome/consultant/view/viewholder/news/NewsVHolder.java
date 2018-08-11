package id.co.okhome.consultant.view.viewholder.news;

import android.view.View;
import android.widget.TextView;

import com.mrjodev.jorecyclermanager.JoViewHolder;
import com.mrjodev.jorecyclermanager.annotations.LayoutMatcher;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.okhome.consultant.R;
import id.co.okhome.consultant.lib.app.OkhomeDateTimeFormatUtil;
import id.co.okhome.consultant.model.NewsModel;
import id.co.okhome.consultant.view.activity.news.NewsSingleActivity;

/**
 * Created by frizurd on 27/03/2018.
 */

@LayoutMatcher(layoutId = R.layout.item_news_element)
public class NewsVHolder extends JoViewHolder<NewsModel> implements View.OnClickListener {

    @BindView(R.id.itemNews_tvTitle)    TextView tvTitle;
    @BindView(R.id.itemNews_tvDate)     TextView tvDate;

    public NewsVHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void onViewCreated() {
        ButterKnife.bind(this, getView());
    }

    @Override
    public void onBind(NewsModel m, int pos, int absPos) {
        super.onBind(m, pos, absPos);

        tvTitle.setText(m.subject);

        String datetime = OkhomeDateTimeFormatUtil.printOkhomeType(m.insertDateTime, "yyyy-MM-dd HH:mm:ss.S", "(E) d MMM yy");
        tvDate.setText(datetime);

        getView().setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        NewsModel news = getModel();
        NewsSingleActivity.start(getContext(), Integer.parseInt(news.id));
    }
}