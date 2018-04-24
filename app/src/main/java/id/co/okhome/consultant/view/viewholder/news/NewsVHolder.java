package id.co.okhome.consultant.view.viewholder.news;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.mrjodev.jorecyclermanager.JoViewHolder;
import com.mrjodev.jorecyclermanager.annotations.LayoutMatcher;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.okhome.consultant.R;
import id.co.okhome.consultant.model.NewsModel;
import id.co.okhome.consultant.view.activity.news.NewsActivity;

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

        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.S");
        DateTime dt = formatter.parseDateTime(m.insertDateTime);
        tvDate.setText(dt.toString(DateTimeFormat.mediumDate()));

        getView().setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        NewsModel news = getModel();
        getContext().startActivity(new Intent(getContext(), NewsActivity.class)
                .putExtra("NEWS_TITLE", news.subject)
                .putExtra("NEWS_CONTENTS", news.contents)
        );
    }
}