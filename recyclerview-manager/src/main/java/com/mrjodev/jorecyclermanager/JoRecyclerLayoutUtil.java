package com.mrjodev.jorecyclermanager;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by josongmin on 2016-03-23.
 */
public class JoRecyclerLayoutUtil {

    /**그리드매니저 리턴*/
    public static GridLayoutManager buildGridLayoutManager(final RecyclerView.Adapter adapter, final Context context, final int spanCount){
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(context, spanCount);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup(){
            @Override
            public int getSpanSize(int position) {
                switch(adapter.getItemViewType(position)){
                    case JoRecyclerAdapter.VIEWTYPE_HEADER:
                        return gridLayoutManager.getSpanCount();
                    case JoRecyclerAdapter.VIEWTYPE_FOOTER:
                        return gridLayoutManager.getSpanCount();
                    default:
                        return 1;
                }
            }
        });

        return gridLayoutManager;
    }

    /**리니어 매니저 리턴*/
    public static LinearLayoutManager buildLinearLayoutManager(final RecyclerView.Adapter adapter, final Context context, final int orientation){
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(orientation);

        return linearLayoutManager;
    }
}
