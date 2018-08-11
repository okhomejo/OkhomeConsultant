package id.co.okhome.consultant.lib.cached_viewpager;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import id.co.okhome.consultant.lib.calendar_manager.model.YearMonthDay;

/**
 * Created by jo on 2018-04-17.
 */

public abstract class CachedPagerAdapter<T> extends PagerAdapter {

    private static String tag = "CachedPagerAdapter";

    List<T> listItems = new ArrayList<>();
    List<View> listCachedView = new ArrayList<>();
    LayoutInflater inflater;
    Map<Integer, View> mapPosMonth = new HashMap<>();
    Context context;

    public CachedPagerAdapter(Context context){
        inflater = LayoutInflater.from(context);
        this.context = context;
    }

    public abstract View makeViewItem(LayoutInflater inflater);
    public abstract void bindViewHolder(ViewHolder viewHolder);
    public abstract void adaptDataAndViews(ViewHolder viewHolder, T data, int position);

    public void setListItems(List<T> listItems) {
        this.listItems = listItems;
    }

    /**미리 프리셋 로딩하고 아이템 붙이기*/
    public void asyncAdaptView(final ViewPager vp, final List<T> items, final OnInitiatingFinishListener onInitiatingFinishListener){
        new SimpleAsyncTask(){
            @Override
            public Object doBackground() {
                initiateFirstItems(4);
                return null;
            }

            @Override
            public void onFinish(Object result) {
                setListItems(items);
                vp.setAdapter(CachedPagerAdapter.this);

                onInitiatingFinishListener.onFinish();
            }
        }.start();
    }

    public Context getContext() {
        return context;
    }

    @Override
    public int getCount() {
        return listItems.size();
    }

    /**초기에 프리셋 준비*/
    public void initiateFirstItems(int size){
        listCachedView.removeAll(listCachedView);
        for(int i = 0; i < size; i++){
            View v = makeViewItem();
            listCachedView.add(v);
        }
    }

    //detached된 아이템 가져오기
    protected View getCachedView(){
        Log.d(tag, "getCachedView");
        for(View v : listCachedView){
            ViewHolder viewHolder = getViewHolderFromView(v);
            if(!viewHolder.isAttached){
                Log.d(tag, "getCachedView hitted");
                return v;
            }
        }

        //없으면
        Log.d(tag, "getCachedView no item");
        View v = makeViewItem();
        return v;
    }

    //뷰 만들기
    private View makeViewItem(){
        Log.d(tag, "makeViewItem");
        long from = System.currentTimeMillis();
        View v = makeViewItem(inflater);

        long end = System.currentTimeMillis();

        Log.d(tag, "makeViewItem duration  : " + (end - from) + "ms");

        ViewHolder viewHolder = new ViewHolder(v);
        bindViewHolder(viewHolder);
        viewHolder.isAttached = false;
        v.setTag(viewHolder);
        return v;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Log.d(tag, "instantiateItem");
        View v = getCachedView();
        container.addView(v);

        ViewHolder viewHolder = getViewHolderFromView(v);
        viewHolder.isAttached = true;

        T data = listItems.get(position);
        adaptDataAndViews(viewHolder, data, position);

        mapPosMonth.put(position, v);
        return v;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        Log.d(tag, "destroyItem");

        View v = (View)object;
        ViewHolder viewHolder = getViewHolderFromView(v);
        viewHolder.isAttached = false;

        container.removeView((View)object);
        mapPosMonth.remove(position);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    //get view holder from view
    public ViewHolder getViewHolderFromView(View view){
        return (ViewHolder)view.getTag();
    }

    public View getViewInPosition(int position){
        return mapPosMonth.get(position);
    }

    //view holder
    public class ViewHolder{
        public boolean isAttached = false;
        public View view;
        SparseArray<View> viewHolders = null;
        Object tag = null;
        public List<YearMonthDay> days = null;

        public ViewHolder(View view) {
            this.view = view;
        }

        /**init views.*/
        public void initViews(int... ids){
            if (viewHolders == null) {
                viewHolders = new SparseArray<View>();
            }

            for(int id : ids){
                View childView = viewHolders.get(id);
                if (childView == null) {
                    childView = view.findViewById(id);
                    viewHolders.put(id, childView);
                }
            }
        }

        public <T extends View> T findViewById(int id) {

            if (viewHolders == null) {
                viewHolders = new SparseArray<View>();
            }

            View childView = viewHolders.get(id);
            if (childView == null) {
                childView = view.findViewById(id);
                viewHolders.put(id, childView);
            }
            return (T) childView;
        }

        public void setTag(Object tag) {
            this.tag = tag;
        }

        public Object getTag() {
            return tag;
        }

        public void setDays(List<YearMonthDay> days) {
            this.days = days;
        }

        public List<YearMonthDay> getDays() {
            return days;
        }
    }

    public interface OnInitiatingFinishListener{
        void onFinish();
    }
}
