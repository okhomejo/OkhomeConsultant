package com.mrjodev.jorecyclermanager.footerloading;

import android.support.v7.widget.RecyclerView;

/**
 * Created by josong on 2017-09-22.
 */

public class FooterLoadingInterceptor extends RecyclerView.OnScrollListener{

    private boolean nowLoading = false;
    private FooterLoadingListener listener, listener2;
    private boolean noMoreLoading = false;
    private int minPageSizeToLoad = 5;

    public FooterLoadingInterceptor(FooterLoadingListener listener, FooterLoadingListener listener2) {
        this.listener = listener;
        this.listener2 = listener2;
    }

    public void setNoMoreLoading(boolean noMoreLoading) {
        this.noMoreLoading = noMoreLoading;
    }

    public void setNowLoading(boolean nowLoading) {
        this.nowLoading = nowLoading;
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);

        //이건 나중에 설정가능하게해야겠다.
        if(recyclerView.getAdapter().getItemCount() < minPageSizeToLoad){
            return;
        }

        if (!recyclerView.canScrollVertically(1)) {
            if(noMoreLoading){
                return;
            }

            if(!nowLoading){
                nowLoading = true;
                listener2.onFooterLoading();
                listener.onFooterLoading();
            }
        }
    }
}
