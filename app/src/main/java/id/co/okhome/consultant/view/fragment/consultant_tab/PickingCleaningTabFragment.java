package id.co.okhome.consultant.view.fragment.consultant_tab;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.okhome.consultant.R;
import id.co.okhome.consultant.lib.SimplePagerDotManager;
import id.co.okhome.consultant.lib.fragment_pager.TabFragmentStatusListener;
import id.co.okhome.consultant.lib.joviewpager.JoViewPagerController;
import id.co.okhome.consultant.view.dialog.ConsultantCleaningQueueSettingDialog;
import id.co.okhome.consultant.view.jo_pageitem.CleaningRequestPageItem;

/**
 * Created by jo on 2018-04-07.
 */

public class PickingCleaningTabFragment extends Fragment implements TabFragmentStatusListener {

    @BindView(R.id.fragTabPickingCleaning_vpCleaningRequest)    ViewPager vpCleaningReq;
    @BindView(R.id.fragTabPickingCleaning_tvPage)               TextView tvPage;
    @BindView(R.id.fragTabPickingCleaning_vgDots)               ViewGroup vgDots;

    SimplePagerDotManager simplePagerDotManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tab_picking_cleaning, null);
        ButterKnife.bind(this, v);
        init();
        return v;
    }

    private void init(){
        List list = new ArrayList();
        list.add("a");
        list.add("a");
        list.add("a");
        list.add("a");

        JoViewPagerController.with(getContext(), vpCleaningReq)
                .setViewPageItemClass(CleaningRequestPageItem.class)
                .setListModel(list)
//                .putParam("photoFrameWidthHeight", photoFrameWidthHeight)
                .build();

        simplePagerDotManager = new SimplePagerDotManager(vgDots, vpCleaningReq, R.drawable.circle_lightblue, R.drawable.circle_gray);
        simplePagerDotManager.setOnPageChangeListener(new SimplePagerDotManager.OnPageChangeListener() {
            @Override
            public void onPageChanged(int pos, int maxSize) {
                String text = (pos + 1) + "/" + vpCleaningReq.getAdapter().getCount();
                tvPage.setText(text);
            }
        });
        simplePagerDotManager.build();


    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onSelect() {
    }

    @Override
    public void onSelectWithData(Map<String, Object> param) {
        View vbtnSetting = (View)param.get("vSetting");
        vbtnSetting.setVisibility(View.VISIBLE);
        vbtnSetting.animate().translationX(20).alpha(0f).setDuration(0).start();
        vbtnSetting.animate().translationX(0).alpha(1f).setDuration(200).start();

        vbtnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ConsultantCleaningQueueSettingDialog(getContext()).show();
            }
        });
    }

    @Override
    public void onDeselect() {

    }

}
