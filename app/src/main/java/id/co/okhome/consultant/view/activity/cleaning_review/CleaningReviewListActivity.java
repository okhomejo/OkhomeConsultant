package id.co.okhome.consultant.view.activity.cleaning_review;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

import com.mrjodev.jorecyclermanager.JoRecyclerAdapter;
import com.mrjodev.jorecyclermanager.footerloading.FooterLoadingListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.okhome.consultant.R;
import id.co.okhome.consultant.lib.app.ConsultantLoggedIn;
import id.co.okhome.consultant.lib.app.OkHomeParentActivity;
import id.co.okhome.consultant.lib.app.OkhomeUtil;
import id.co.okhome.consultant.lib.retrofit.RetrofitCallback;
import id.co.okhome.consultant.model.cleaning.CleaningInfoModel;
import id.co.okhome.consultant.model.cleaning.CleaningReviewModel;
import id.co.okhome.consultant.model.cleaning.CleaningReviewPageModel;
import id.co.okhome.consultant.model.cleaning.CleaningReviewSummaryModel;
import id.co.okhome.consultant.rest_apicall.retrofit_restapi.OkhomeRestApi;
import id.co.okhome.consultant.view.viewholder.BlankVHolder;
import id.co.okhome.consultant.view.viewholder.CleaningReviewVHolder;
import id.co.okhome.consultant.view.viewholder.ConsultantCleaningTaskVHolder;
import id.co.okhome.consultant.view.viewholder.ManualParentVHolder;
import id.co.okhome.consultant.view.viewholder.ReviewSummaryVHolder;

public class CleaningReviewListActivity extends OkHomeParentActivity {

    @BindView(R.id.actCleaningReview_rcv)       RecyclerView rcv;

    private JoRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cleaning_review_list);
        OkhomeUtil.setWhiteSystembar(this);

        ButterKnife.bind(this);
        init();
    }

    private void init() {
        initRecyclerView();
        loadFirstList();
    }

    private void initRecyclerView() {
        adapter = new JoRecyclerAdapter(new JoRecyclerAdapter.Params()
                .setRecyclerView(rcv)
                .setItemViewHolderCls(CleaningReviewVHolder.class)
                .setFooterViewHolderCls(BlankVHolder.class)
                .setHeaderViewHolderCls(ReviewSummaryVHolder.class)
                .setEmptyView(R.id.layerEmpty_vContents)
                .setBottomLoading(getLayoutInflater().inflate(R.layout.layer_footer_loading, null), new FooterLoadingListener() {
                    @Override
                    public void onFooterLoading() {
                        CleaningReviewModel cleaningInfoModel = (CleaningReviewModel) adapter.getLastItem();
                        int rowNum = cleaningInfoModel.rownum;
                        getListMore(rowNum);
                    }
                })
        );
        adapter.addFooterItem("A");
    }

    private void setHeaderViewItem(CleaningReviewSummaryModel summaryModel){
        adapter.addHeaderItem(summaryModel);
    }

    private void loadFirstList() {
        OkhomeRestApi.getCleaningReviewClient().getReviewPageModel(ConsultantLoggedIn.id()).enqueue(new RetrofitCallback<CleaningReviewPageModel>() {
            @Override
            public void onSuccess(CleaningReviewPageModel result) {
                adapter.setListItems(result.reviews);
                setHeaderViewItem(result.summaryModel);
            }

            @Override
            public void onFinish() {
                adapter.notifyDataSetChanged();
                super.onFinish();
            }
        });
    }

    private void getListMore(int lastRowNum){
        OkhomeRestApi.getCleaningReviewClient().getCleaningReviews(ConsultantLoggedIn.id(), lastRowNum).enqueue(new RetrofitCallback<List<CleaningReviewModel>>() {
            @Override
            public void onSuccess(List<CleaningReviewModel> result) {
                if (result.size() > 0) {
                    adapter.addListItems(result);
                    adapter.notifyDataSetChanged();
                    adapter.notifyFooterLoadingComplete();
                } else {
                    adapter.setFooterNoMoreResult(true);
                }
            }
        });
    }

    @OnClick(R.id.actCleaningReview_vbtnX)
    public void onButtonGoBack() {
        finish();
    }
}
