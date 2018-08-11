package id.co.okhome.consultant.lib;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by jo on 2018-04-23.
 */

public class SimplePagerDotManager {
    private ViewGroup vg;
    private ViewPager vp;
    private int maxSize = 0;
    private int resOnId, resOffId;
    private OnPageChangeListener onPageChangeListener;

    public SimplePagerDotManager(ViewGroup vgDots, ViewPager vp, int resOnId, int resOffId) {
        this.vg = vgDots;
        this.vp = vp;
        this.resOffId = resOffId;
        this.resOnId = resOnId;

        maxSize = vp.getAdapter().getCount();
    }

    public void setOnPageChangeListener(OnPageChangeListener onPageChangeListener) {
        this.onPageChangeListener = onPageChangeListener;
    }

    public void build(){
        maxSize = vp.getAdapter().getCount();
        addChangeListener();
        init();
        setPosition(0);


    }


    private void addChangeListener(){
        vp.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                setPosition(position);

                if(onPageChangeListener != null){
                    onPageChangeListener.onPageChanged(position, maxSize);
                }
            }
        });
    }

    private void init(){
        for(int i = 0; i < vg.getChildCount(); i++){
            View v = vg.getChildAt(i);
            v.setBackgroundResource(resOffId);

            if(i < maxSize){
                v.setVisibility(View.VISIBLE);
            }else{
                v.setVisibility(View.GONE);
            }
        }
    }

    public void refresh(){
        maxSize = vp.getAdapter().getCount();
        init();
    }

    public void setPosition(int pos){
        init();
        vg.getChildAt(pos).setBackgroundResource(resOnId);

        if(onPageChangeListener != null){
            onPageChangeListener.onPageChanged(0, maxSize);
        }
    }

    public interface OnPageChangeListener{
        void onPageChanged(int pos, int maxSize);
    }
}
