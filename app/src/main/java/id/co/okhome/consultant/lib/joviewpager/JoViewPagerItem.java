package id.co.okhome.consultant.lib.joviewpager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by josongmin on 2016-08-10.
 */

public abstract class JoViewPagerItem<T>{

    Context context;
    View view;
    int position;
    T model;
    JoViewPagerController pagerController;

    public void init(JoViewPagerController pagerController, Context context, T model, int position){
        this.context = context;
        this.position = position;
        this.model = model;
        this.pagerController = pagerController;
        view = getView(LayoutInflater.from(context));
    }

    public Context getContext() {
        return context;
    }

    public JoViewPagerController getPagerController() {
        return pagerController;
    }

    public <E> E getGlobalParam(String key){
        return (E)pagerController.getParam(key);
    }

    public void refresh(){
        onViewSelected(getModel(), getPosition());
    }
    public T getModel() {
        return model;
    }

    public int getPosition() {
        return position;
    }

    public View getDecorView(){
        return view;
    }

    public abstract View getView(LayoutInflater inflater);
    public abstract void onViewCreated();
    public abstract void onViewSelected(T model, int position);

}
