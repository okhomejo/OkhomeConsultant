package id.co.okhome.consultant.lib.joviewpager;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by josongmin on 2016-08-09.
 */

public class JoViewPagerController<T> implements ViewPager.OnPageChangeListener{

    //빌더
    public static final <T> JoViewPagerController<T> with(Context context, ViewPager vp){
        return new <T>JoViewPagerController(context, vp);
    }

    ViewPager vp;
    ViewPagerAdapter adapter;
    Context context;
    int size = 0;
    List<T> listModel = null;
    Class itemClass;
    Object parentInstance;
    Map<String, Object> mapParams = new HashMap<String, Object>();


    //////시작

    public JoViewPagerController(Context context, ViewPager vp){
        this.context = context;
        adapter = new ViewPagerAdapter();
        this.vp = vp;
    }

    public JoViewPagerController setViewPageItemClass(Class itemClass){
        this.itemClass = itemClass;
        return this;
    }

    public JoViewPagerController setViewPageItemClass(Object parentInstance, Class itemClass){
        this.parentInstance = parentInstance;
        this.itemClass = itemClass;
        return this;
    }

    public JoViewPagerController putParam(String key, Object obj){
        mapParams.put(key, obj);
        return this;
    }

    public <E> E getParam(String key){
        return (E)(mapParams.get(key));
    }

    public JoViewPagerController remove(String key){
        mapParams.remove(key);
        return this;
    }


    public JoViewPagerController setListModel(List<T> listModel) {
        this.listModel = listModel;
        return this;
    }

    public List<T> getListModel() {
        return listModel;
    }

    //시작
    public JoViewPagerController build(){
        size = listModel.size();
        vp.setAdapter(adapter);
        vp.addOnPageChangeListener(this);
        onPageSelected(0);
        return this;
    }

    //
    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private Object getInstance(final Class itemClass) throws Exception {
        if(itemClass.isMemberClass()){
            Constructor innerConstructor = itemClass.getDeclaredConstructor(parentInstance.getClass());
            return innerConstructor.newInstance(parentInstance);
        }else{
            return itemClass.newInstance();
        }
    }

    //어댑터
    private class ViewPagerAdapter extends PagerAdapter {

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            T model = listModel.get(position);
            try{
                JoViewPagerItem item = (JoViewPagerItem)getInstance(itemClass);

                item.init(JoViewPagerController.this, context, model, position);
                View v = item.getDecorView();
                container.addView(v);

                //뷰 완성되었음을 알림
                item.onViewCreated();
                item.onViewSelected(model, position);
                return v;
            }catch(Exception e){
                e.printStackTrace();
                Log.d("JO", "instantiateItem err" + e.toString());
                return null;
            }
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager)container).removeView((View)object);
        }

        @Override
        public int getCount() {
            return size;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }



}
