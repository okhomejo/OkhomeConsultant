package id.co.okhome.consultant.lib.joviewrepeator;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by josongmin on 2016-08-10.
 */

public class JoViewRepeator<E> {

    //instance
    List<E> list = new ArrayList<E>();
    Context context;
    ViewGroup vg;
    int itemLayoutId;
    JoRepeatorAdapter callBack;
    int spanSize = 1;

    //
    Map<View, E> mapViewModel = new HashMap<>();
    Map<E, View> mapModelView = new HashMap<>();
    Map<Integer, View> mapPosView = new HashMap<>();

    //
    boolean enableSquare = false;
    int itemWidth  = 0;

    //
    public Context getContext() {
        return context;
    }

    //constructor
    public JoViewRepeator(Context context) {
        this.context = context;
    }

    //
    public JoRepeatorAdapter getCallBack() {
        return callBack;
    }

    //동작
    public void notifyDataSetChanged2(){
        vg.removeAllViews();
        //헤더

        if(callBack != null){
            View vHeader = callBack.getHeaderView();
            if(vHeader != null) vg.addView(vHeader);
        }
        //아이템
        for(E model : list){
            View vItem = getView(model);
            vg.addView(vItem);
        }

        //푸터
        if(callBack != null){
            View vFooter = callBack.getFooterView();
            if(vFooter!= null) vg.addView(vFooter);
        }
    }


    public JoViewRepeator setSpanSize(int spanSize) {
        this.spanSize = spanSize;
        return this;
    }

    public void enableSquare(int height){
        JoViewRepeator.this.itemWidth = height;
        for(E item : list){
            View vItem = mapModelView.get(item);
            vItem.getLayoutParams().height = itemWidth;
        }
    }

    public JoViewRepeator enableSquare(){
        this.enableSquare = true;
        vg.post(new Runnable() {
            @Override
            public void run() {
                int itemWidth = vg.getWidth() / spanSize;
                JoViewRepeator.this.itemWidth = itemWidth;
                for(E item : list){
                    View vItem = mapModelView.get(item);
                    vItem.getLayoutParams().height = itemWidth;
                }
            }
        });

        return this;
    }

    public void notifyDataChange(E model){
        View vItem = mapModelView.get(model);

        if(callBack != null){
            callBack.onBind(vItem, model);
        }
    }

    public void notifyDataChange(int pos){
        View vItem = mapPosView.get(pos);
        E model  = mapViewModel.get(vItem);

        if(callBack != null){
            callBack.onBind(vItem, model);
        }
    }

    public void notifyAllDataChange(){
        for(E model : list){
            notifyDataChange(model);
        }
    }


    /**최종 만들기*/
    public JoViewRepeator notifyDataSetChanged(){
        vg.removeAllViews();
        int size = list.size();

        final ViewGroup vgVertical = makeVerticalViewContainer();
        ViewGroup vgHorizontal = null;

        //헤더
        if(callBack != null){
            View vHeader = callBack.getHeaderView();
            if(vHeader != null) vgVertical.addView(vHeader);
        }

        int width = vgVertical.getWidth();

        for(int i = 0; i < size; i++){
            E model = list.get(i);
            if(i % spanSize == 0){
                Log.d("JO", "NewVertical");
                vgHorizontal = makeHorizontalViewContainer();
                vgVertical.addView(vgHorizontal);
            }

            final ViewGroup vItemContainer = makeItemViewContainer();
            View vItem = getView(model);

            vItemContainer.addView(vItem);

            mapViewModel.put(vItem, model);
            mapModelView.put(model, vItem);
            mapPosView.put(i, vItem);

            //컨테이너에 넣는다
            vgHorizontal.addView(vItemContainer);
        }

        //푸터
        if(callBack != null){
            View vFooter = callBack.getFooterView();
            if(vFooter!= null) vgVertical.addView(vFooter);
        }


        Log.d("JO", vgVertical.getChildCount() + "개");
        vg.addView(vgVertical);


        if(enableSquare == true && itemWidth > 0){
            for(E item : list){
                View vItem = mapModelView.get(item);
                vItem.getLayoutParams().height = itemWidth;
            }
        }

        return this;
    }


    //버티컬 컨테이너
    private ViewGroup makeVerticalViewContainer(){
        LinearLayout llContainer = new LinearLayout(context);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        llContainer.setLayoutParams(lp);
        llContainer.setOrientation(LinearLayout.VERTICAL);


        return llContainer;
    }

    //한줄 컨테이너 만들기
    private ViewGroup makeHorizontalViewContainer(){
        LinearLayout llContainer = new LinearLayout(context);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        llContainer.setWeightSum(spanSize);
        llContainer.setLayoutParams(lp);

        return llContainer;
    }

    //아이템뷰컨테이너 만들어지는곳
    private ViewGroup makeItemViewContainer(){
        LinearLayout llItem = new LinearLayout(context);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        lp.weight = 1;
        llItem.setLayoutParams(lp);
//        llItem.setGravity(Gravity.CENTER);

        return llItem;
    }

    private View getView(E model){
        View vItem = LayoutInflater.from(context).inflate(itemLayoutId, null);

        if(callBack != null){
            callBack.onBind(vItem, model);
        }
        return vItem;
    }

    public void notifyDataInserted(int pos){
        E model = getList().get(pos);
        View v = getView(model);
        vg.addView(v, pos);
    }

    public void notifyDataInsertedAtLast(){
        notifyDataInserted(getList().size()-1);
    }

    public void notifyDataRemoved(int pos){
        vg.removeView(vg.getChildAt(pos));
    }

    //컨테이너 설정
    public JoViewRepeator<E> setContainer(ViewGroup vg){
        this.vg = vg;
        return this;
    }

    public JoViewRepeator<E> setContainer(int viewGroupId){
        vg = (ViewGroup) LayoutInflater.from(context).inflate(viewGroupId, null);
        return this;
    }

    //반복뷰설정
    public JoViewRepeator<E> setItemLayoutId(int itemLayoutId){
        this.itemLayoutId = itemLayoutId;
        return this;
    }

    //setter


    public List<E> getList() {
        return list;
    }

    public JoViewRepeator<E> setCallBack(JoRepeatorAdapter callBack) {
        this.callBack = callBack;
        callBack.setViewRepeator(this);
        return this;
    }

    public void setList(List<E> list) {
        this.list = list;
    }
    public void addItem(E item){
        list.add(item);
    }

    //추가
    public void insertItem(E model){
        list.add(model);
    }

    public void insertItem(E model, int pos){
        list.add(pos, model);
    }

    public void insertItemAtLast(E model){
        list.add(list.size(), model);
    }

    //제거
    public void deleteItem(int pos){
        list.remove(pos);
    }

    public int deleteItem(E model){
        for(int i = 0; i < list.size(); i++){
            if(list.get(i) == model){
                list.remove(model);
                return i;
            }
        }

        return -1;
    }




}
