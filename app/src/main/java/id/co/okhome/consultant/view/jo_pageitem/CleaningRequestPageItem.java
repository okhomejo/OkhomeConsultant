package id.co.okhome.consultant.view.jo_pageitem;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.okhome.consultant.R;
import id.co.okhome.consultant.lib.joviewpager.JoViewPagerItem;
import id.co.okhome.consultant.view.activity.cleaning.CleaningDetailActivity;

/**
 * Created by jo on 2018-04-23.
 */

public class CleaningRequestPageItem extends JoViewPagerItem<String>{

    @Override
    public View getView(LayoutInflater inflater) {
        View v = inflater.inflate(R.layout.page_cleaning_request, null);
        ButterKnife.bind(this, v);
        int i = 0;
        return v;
    }

    @Override
    public void onViewCreated() {

    }

    @Override
    public void onViewSelected(String model, int position) {

    }

    @OnClick(R.id.pageCleaningRequest_vbtnSeeDetail)
    public void onBtnSeeDetail(View v){
        getContext().startActivity(new Intent(getContext(), CleaningDetailActivity.class));
    }
}
