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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.okhome.consultant.R;
import id.co.okhome.consultant.lib.ViewHolderUtil;
import id.co.okhome.consultant.model.FaqModel;
import id.co.okhome.consultant.model.NewsModel;

public class FaqListAdapter extends BaseAdapter {

    private Context context;
    private List<FaqModel> faqList;

    public FaqListAdapter(Context context, List<FaqModel> faqs) {
        this.context = context;
        this.faqList = faqs;
    }

    @Override
    public int getCount() {
        return faqList.size();
    }

    @Override
    public FaqModel getItem(int position) {
        return faqList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_faq_element, parent, false);
        }

        TextView faqTitle = ViewHolderUtil.getView(convertView, R.id.itemFaqs_tvTitle);

        FaqModel faq =  getItem(position);
        faqTitle.setText(faq.subject);

        return convertView;
    }
}