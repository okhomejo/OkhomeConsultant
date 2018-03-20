package id.co.okhome.consultant.adapter;

/**
 * Created by frizurd on 12/02/2018.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.okhome.consultant.R;
import id.co.okhome.consultant.model.NewsModel;

public class NewsListAdapter extends BaseAdapter {
    private Context context;
    private List<NewsModel> newsList;
    private ViewHolder viewHolder;

    public NewsListAdapter(Context context, List<NewsModel> news) {
        this.context = context;
        this.newsList = news;
    }

    @Override
    public int getCount() {
        return newsList.size();
    }

    @Override
    public NewsModel getItem(int position) {
        return newsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_news_element, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        NewsModel news =  getItem(position);

        viewHolder.newsTitle.setText(news.subject);
        viewHolder.newsDate.setText(news.insertDateTime);

        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.itemNews_tvTitle)    TextView newsTitle;
        @BindView(R.id.itemNews_tvDate)     TextView newsDate;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}