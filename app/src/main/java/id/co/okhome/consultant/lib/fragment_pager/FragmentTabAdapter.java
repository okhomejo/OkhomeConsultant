package id.co.okhome.consultant.lib.fragment_pager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

/**
 * Created by josong on 2017-10-04.
 */

public abstract class FragmentTabAdapter extends FragmentPagerAdapter implements ViewPager.OnPageChangeListener{

    FragmentManager fm;
    ViewPager viewPager;
    TabFragmentStatusListener lastFragment = null;
    public FragmentTabAdapter(FragmentManager fm) {
        super(fm);
        this.fm = fm;
    }

    /**연결*/

    private void notifyCurrentItemChange(int position){

        for(int i = 0; i < fm.getFragments().size(); i++){
            Fragment f = fm.getFragments().get(i);
            if(position == i){
                if(f instanceof TabFragmentStatusListener){
                    ((TabFragmentStatusListener) f).onSelect();
                    lastFragment = (TabFragmentStatusListener)f;
                }
            }else{

                if(lastFragment != null &&  f == lastFragment){
                    if(f instanceof TabFragmentStatusListener){
                        ((TabFragmentStatusListener) f).onDeselect();
                    }
                }
            }
        }


    }


    //페이지 셀렉되었을때
    @Override
    public void onPageSelected(int position) {
        //프래그먼트에 알림
        notifyCurrentItemChange(position);
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        ;
    }


    @Override
    public void onPageScrollStateChanged(int state) {
        ;
    }

    /**init*/
    public void setViewPager(ViewPager viewPager){
        this.viewPager = viewPager;

        viewPager.setOffscreenPageLimit(getCount());
        viewPager.post(new Runnable()
        {
            @Override
            public void run()
            {
                FragmentTabAdapter.this.onPageSelected(FragmentTabAdapter.this.viewPager.getCurrentItem());
            }
        });

        viewPager.addOnPageChangeListener(this);
        viewPager.setAdapter(this);
    }

    public void init(ViewPager vp){
        vp.setAdapter(this);
        vp.addOnPageChangeListener(this);
        onPageSelected(0);
    }
}
