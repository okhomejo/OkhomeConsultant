package id.co.okhome.consultant.view.main.trainee;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.FilterQueryProvider;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.mrjodev.jorecyclermanager.JoRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.okhome.consultant.R;
import id.co.okhome.consultant.lib.app.OkHomeParentActivity;
import id.co.okhome.consultant.lib.app.OkhomeUtil;
import id.co.okhome.consultant.lib.retrofit.RetrofitCallback;
import id.co.okhome.consultant.model.FaqModel;
import id.co.okhome.consultant.rest_apicall.retrofit_restapi.OkhomeRestApi;
import id.co.okhome.consultant.view.viewholder.FaqVHolder;

/**
 * Created by frizurd on 16/03/2018.
 */

public class TraineeFaqActivity extends OkHomeParentActivity {

    @BindView(R.id.actTraineeFAQ_fadeBackground)    View fadeBackground;
    @BindView(R.id.actTraineeFAQ_tvTitle)           TextView tvTitle;
    @BindView(R.id.actTraineeFAQ_ivSearchIcon)      ImageView ivSearchIcon;
    @BindView(R.id.actTraineeFAQ_vgNoResult)        FrameLayout tvNoResults;
    @BindView(R.id.actTraineeFAQ_vProgress)         ProgressBar progressBar;
    @BindView(R.id.actTraineeFAQ_rcv)               RecyclerView rcv;
    @BindView(R.id.actTraineeFAQ_search)            AutoCompleteTextView tvSearch;

    private Map<Integer, List<String>> faqKeywords;
    private List<String> faqItems;
    private JoRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainee_faq);
        OkhomeUtil.setWhiteSystembar(this);

        ButterKnife.bind(this);
        init();
    }

    private void init() {
        getAllFaqKeywords();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            getAllFaq(extras.getInt("FAQ_ID"));
            tvTitle.setText(extras.getString("FAQ_TITLE"));
        } else {
            getAllFaq(0);
        }
    }

    private void getAllFaq(final int faqId) {
        progressBar.setVisibility(View.VISIBLE);
        OkhomeRestApi.getCommonClient().getAllFaqs(faqId).enqueue(new RetrofitCallback<List<FaqModel>>() {

            @Override
            public void onSuccess(final List<FaqModel> faqs) {

                FaqVHolder.ItemClickListener faqClickListener
                        = new FaqVHolder.ItemClickListener() {
                    @Override
                    public void onItemClick(int pos, String value, String tag) {
                        if (faqs.get(pos).childCount == 0) {
                            Intent intent = new Intent(getBaseContext(), TraineeFaqSingleActivity.class);
                            intent.putExtra("FAQ_ID", faqs.get(pos).id);
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(getBaseContext(), TraineeFaqActivity.class);
                            intent.putExtra("FAQ_ID", faqs.get(pos).id);
                            intent.putExtra("FAQ_TITLE", faqs.get(pos).subject);
                            startActivity(intent);
                        }
                    }
                };

                faqItems = new ArrayList<>();
                for (FaqModel faq : faqs) {
                    faqItems.add(faq.subject);
                }
                JoRecyclerAdapter.Params params = new JoRecyclerAdapter.Params();
                params.setRecyclerView(rcv)
                        .addParam(FaqVHolder.TAG_ITEM_ACT, getBaseContext())
                        .addParam(FaqVHolder.TAG_ITEM_CLICK, faqClickListener)
                        .setItemViewHolderCls(FaqVHolder.class);
                adapter = new JoRecyclerAdapter(params);
                adapter.setListItems(faqItems);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void getAllFaqKeywords() {
        final ProgressDialog p = ProgressDialog.show(this, null, "Loading keywords...");
        OkhomeRestApi.getCommonClient().getAllFaqsKeywords().enqueue(new RetrofitCallback<Map<Integer, List<String>>>() {
            @Override
            public void onSuccess(Map<Integer, List<String>> result) {
                faqKeywords = result;
            }

            @Override
            public void onFinish() {
                super.onFinish();
                searchFunction();
                p.dismiss();
            }
        });
    }

    private void searchFunction() {
        String[] from   = { "name" };
        int[] to        = { R.id.itemFaqKw_tvTitle };

        SimpleCursorAdapter cursorAdapter = new SimpleCursorAdapter(this, R.layout.item_faq_keyword, null, from, to, 0);
        cursorAdapter.setStringConversionColumn(1);
        final FilterQueryProvider queryProvider = new FilterQueryProvider() {
            @Override
            public Cursor runQuery(CharSequence constraint) {
                if (constraint == null) {
                    showNoResults(false);
                    return null;
                }
                String[] columnNames = { BaseColumns._ID, "name" };
                MatrixCursor c = new MatrixCursor(columnNames);
                try {
                    int counter = 0;
                    for(List<String> valueList : faqKeywords.values()) {
                        for(String value : valueList) {
                            if (value.contains(constraint)) {
                                c.newRow().add(counter++).add(value);
                            }
                        }
                    }
                    if (constraint.length() > 1 && counter == 0) {
                        showNoResults(true);
                    } else {
                        showNoResults(false);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return c;
            }
        };
        cursorAdapter.setFilterQueryProvider(queryProvider);
        tvSearch.setAdapter(cursorAdapter);
        tvSearch.setOnItemClickListener(searchFaqClickListener);
    }

    private AdapterView.OnItemClickListener searchFaqClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            List<String> faqIds = new ArrayList<>();
            for (Map.Entry<Integer, List<String>> entry : faqKeywords.entrySet()) {
                for(String value : entry.getValue()) {
                    if (value.equals(tvSearch.getText().toString())) {
                        faqIds.add(entry.getKey().toString());
                    }
                }
            }
            if (!faqIds.isEmpty()) {
                Intent intent = new Intent(getBaseContext(), TraineeFaqSearchResultActivity.class);
                intent.putExtra("FAQ_SEARCH_IDS",   android.text.TextUtils.join(",", faqIds));
                intent.putExtra("FAQ_SEARCH_TITLE", tvSearch.getText().toString());
                startActivity(intent);
                onClickSearchFaq();
            }
        }
    };

    private void showNoResults(final boolean isTrue) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (isTrue) {
                    tvNoResults.setVisibility(View.VISIBLE);
                    tvNoResults.bringToFront();
                } else {
                    if (tvNoResults.getVisibility() == View.VISIBLE) {
                        tvNoResults.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });
    }

    private void checkIfSearchHidden() {
        if (tvSearch.getVisibility() == View.VISIBLE) {
            tvTitle.setVisibility(View.VISIBLE);
            tvSearch.getText().clear();
            tvSearch.setVisibility(View.GONE);
            OkhomeUtil.hideKeyboard(this);

            ivSearchIcon.setImageResource(R.drawable.ic_search);
            fadeBackground.animate().alpha(0f);
            fadeBackground.setVisibility(View.GONE);
        } else {
            tvTitle.setVisibility(View.GONE);
            tvSearch.setVisibility(View.VISIBLE);
            tvSearch.requestFocus();

            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(tvSearch, InputMethodManager.SHOW_IMPLICIT);

            ivSearchIcon.setImageResource(R.drawable.ic_x);
            fadeBackground.setVisibility(View.VISIBLE);
            fadeBackground.animate().alpha(0.5f);
        }
    }

    @OnClick(R.id.actTraineeFAQ_vbtnSearch)
    public void onClickSearchFaq() {
        checkIfSearchHidden();
    }

    @OnClick(R.id.actTraineeFAQ_vbtnX)
    public void onClickCloseActivity() {
        finish();
    }

    @OnClick(R.id.actTraineeFAQ_fadeBackground)
    public void onFadeBgClick() {
        onClickSearchFaq();
    }
}