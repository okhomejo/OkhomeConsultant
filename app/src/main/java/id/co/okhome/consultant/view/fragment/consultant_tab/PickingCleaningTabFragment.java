package id.co.okhome.consultant.view.fragment.consultant_tab;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.okhome.consultant.R;
import id.co.okhome.consultant.lib.SimplePagerDotManager;
import id.co.okhome.consultant.lib.app.ConsultantLoggedIn;
import id.co.okhome.consultant.lib.app.OkhomeDateTimeFormatUtil;
import id.co.okhome.consultant.lib.app.OkhomeUtil;
import id.co.okhome.consultant.lib.dialog.DialogParent;
import id.co.okhome.consultant.lib.fragment_pager.TabFragmentStatusListener;
import id.co.okhome.consultant.lib.retrofit.RetrofitCallback;
import id.co.okhome.consultant.model.cleaning.order.CleaningReqQueueModel;
import id.co.okhome.consultant.rest_apicall.retrofit_restapi.OkhomeRestApi;
import id.co.okhome.consultant.view.dialog.CommonAlertDialog;
import id.co.okhome.consultant.view.dialog.CommonOkDialog;
import id.co.okhome.consultant.view.dialog.ConsultantCleaningQueueSettingDialog;
import id.co.okhome.consultant.view.fragment.cleaning_queue.CleaningQueueItemFragment;

/**
 * Created by jo on 2018-04-07.
 */

public class PickingCleaningTabFragment extends Fragment implements TabFragmentStatusListener {

    @BindView(R.id.fragTabPickingCleaning_vpCleaningRequest)    ViewPager vpCleaningReq;
    @BindView(R.id.fragTabPickingCleaning_tvPage)               TextView tvPage;
    @BindView(R.id.fragTabPickingCleaning_vgDots)               ViewGroup vgDots;
    @BindView(R.id.fragTabPickingCleaning_vLoading)             View vLoading;
    @BindView(R.id.fragTabPickingCleaning_vgContents)           ViewGroup vgContents;
    @BindView(R.id.fragTabPickingCleaning_tvDate)               TextView tvDate;
    @BindView(R.id.fragTabPickingCleaning_vgEmpty)              ViewGroup vgEmpty;

    SimplePagerDotManager simplePagerDotManager;
    CleaningQueueFragmentAdapter cleaningQueueAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tab_picking_cleaning, null);
        ButterKnife.bind(this, v);
        init();
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onResume() {
        super.onResume();
        getQuratedCleaningQueues();
    }

    @Override
    public void onSelect() {
        getQuratedCleaningQueues();
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

    private void init(){
        vpCleaningReq.setOffscreenPageLimit(10);
    }

    //로딩 시작
    private void loading(boolean loading){
        if(loading){
            vgEmpty.setVisibility(View.GONE);
            vLoading.setVisibility(View.VISIBLE);
            vgContents.setVisibility(View.INVISIBLE);
        }else{
            vLoading.setVisibility(View.INVISIBLE);
            vgContents.setVisibility(View.VISIBLE);
        }

    }

    //청소 ㅐ역 없을때
    private void onCleaningNothing(){
        vgEmpty.setVisibility(View.VISIBLE);
        vgContents.setVisibility(View.GONE);
    }

    //아이템 받아왔을때 처리
    private void onQuratedItemRetrived(Map<Integer, List<CleaningReqQueueModel>> cleaningReqQueueMap){

        //무저건 하나만 옴.
        int targetDate = 0;
        for(int date : cleaningReqQueueMap.keySet()){
            targetDate = date;
        }
        List<CleaningReqQueueModel> cleaningReqQueues = cleaningReqQueueMap.get(targetDate);

        //아이템 목록 처리
        cleaningQueueAdapter = new CleaningQueueFragmentAdapter(getFragmentManager(), cleaningReqQueues);
        vpCleaningReq.setAdapter(cleaningQueueAdapter);

        //도트 처리
        simplePagerDotManager = new SimplePagerDotManager(vgDots, vpCleaningReq, R.drawable.circle_lightblue, R.drawable.circle_gray);
        simplePagerDotManager.setOnPageChangeListener(new SimplePagerDotManager.OnPageChangeListener() {
            @Override
            public void onPageChanged(int pos, int maxSize) {
                String text = (pos + 1) + "/" + maxSize;
                tvPage.setText(text);
            }
        });
        simplePagerDotManager.build();

        //상단 날짜 처리
        String when = OkhomeDateTimeFormatUtil.printOkhomeType(cleaningReqQueues.get(0).firstCleaningDateTime, "(E) d MMM yy");
        tvDate.setText(when.toUpperCase());

    }

    //아이템 불러오기
    private void getQuratedCleaningQueues(){
        loading(true);
        OkhomeRestApi.getCleaningOrderQueueClient().getQuratedCleaningQueueList(ConsultantLoggedIn.id()).enqueue(new RetrofitCallback<Map<Integer, List<CleaningReqQueueModel>>>() {
            @Override
            public void onSuccess(Map<Integer, List<CleaningReqQueueModel>> map) {
                if(map.size() > 0){
                    onQuratedItemRetrived(map);
                }else{
                    onCleaningNothing();
                }

            }

            @Override
            public void onFinish() {
                super.onFinish();
                loading(false);
            }
        });
    }

    private void refresh(){
        getQuratedCleaningQueues();
    }

    //acceept
    private void accept(){
        final ProgressDialog p = OkhomeUtil.showLoadingDialog(getContext());
        CleaningReqQueueModel cleaningReqQueue = cleaningQueueAdapter.getCLeaningReqQueuItem(vpCleaningReq.getCurrentItem());
        OkhomeRestApi.getCleaningOrderQueueClient().acceptCleaningOrderInQueue(cleaningReqQueue.queueId, ConsultantLoggedIn.id()).enqueue(new RetrofitCallback<String>() {
            @Override
            public void onSuccess(String result) {
                //ok 팝업
                //refresh
                new CommonOkDialog(getContext()).setTitle("OKHOME").setSubTitle("Complete!").setCommonDialogListener(new DialogParent.CommonDialogListener() {
                    @Override
                    public void onCommonDialogWorkDone(Dialog dialog, int actionCode, Map<String, Object> mapResult) {
                        refresh();
                    }
                }).show();

            }

            @Override
            public void onFinish() {
                p.dismiss();
            }
        });
    }

    //decline
    private void decline(){
        final ProgressDialog p = OkhomeUtil.showLoadingDialog(getContext());
        CleaningReqQueueModel cleaningReqQueue = cleaningQueueAdapter.getCLeaningReqQueuItem(vpCleaningReq.getCurrentItem());
        OkhomeRestApi.getCleaningOrderQueueClient().rejectCleaningOrderInQueue(cleaningReqQueue.queueId, ConsultantLoggedIn.id()).enqueue(new RetrofitCallback<String>() {
            @Override
            public void onSuccess(String result) {
                refresh();
            }


            @Override
            public void onFinish() {
                p.dismiss();
            }
        });
    }

    @OnClick(R.id.fragmentTabPickingCleaning_vbtnAccept)
    public void onAccept(View v){
        new CommonAlertDialog(getContext(), true)
                .setTitle("ACCEPT")
                .setSubTitle("Do you want to accept this order?")
                .setCommonDialogListener(new DialogParent.CommonDialogListener() {
                    @Override
                    public void onCommonDialogWorkDone(Dialog dialog, int actionCode, Map<String, Object> mapResult) {
                        if(actionCode == DialogParent.CommonDialogListener.ACTIONCODE_OK){
                            dialog.dismiss();
                            accept();

                        }
                    }
                }).show();
    }

    @OnClick(R.id.fragmentTabPickingCleaning_vbtnDecline)
    public void onDeny(View v){
        new CommonAlertDialog(getContext(), true)
                .setTitle("DECLINE")
                .setSubTitle("Do you want to decline this order?")
                .setCommonDialogListener(new DialogParent.CommonDialogListener() {
                    @Override
                    public void onCommonDialogWorkDone(Dialog dialog, int actionCode, Map<String, Object> mapResult) {
                        if(actionCode == DialogParent.CommonDialogListener.ACTIONCODE_OK){
                            dialog.dismiss();
                            decline();
                        }
                    }
                }).show();
    }

    //어댑터
    class CleaningQueueFragmentAdapter extends FragmentStatePagerAdapter {

        List<CleaningReqQueueModel> cleaningReqQueues;

        public CleaningQueueFragmentAdapter(FragmentManager fm, List<CleaningReqQueueModel> cleaningReqQueues) {
            super(fm);

            this.cleaningReqQueues = cleaningReqQueues;
        }

        @Override
        public Fragment getItem(int position) {
            CleaningReqQueueModel cleaningReqQueue = cleaningReqQueues.get(position);
            Fragment f = new CleaningQueueItemFragment();
            Bundle bundle = new Bundle();

            bundle.putSerializable("cleaningReqQueue", cleaningReqQueue);
            f.setArguments(bundle);
            return f;
        }

        public CleaningReqQueueModel getCLeaningReqQueuItem(int position){
            CleaningReqQueueModel cleaningReqQueue = cleaningReqQueues.get(position);
            return cleaningReqQueue;
        }

        @Override
        public int getCount() {
            return cleaningReqQueues.size();
        }
    }
}
