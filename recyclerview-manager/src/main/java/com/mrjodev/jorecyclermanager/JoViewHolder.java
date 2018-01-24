package com.mrjodev.jorecyclermanager;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by josongmin on 2016-03-23.
 */
public abstract class JoViewHolder<MODEL> extends RecyclerView.ViewHolder {
    View view;
    private Context context;
    private RecyclerView rcv;
    private JoRecyclerAdapter.Params rcvParams;
    private JoRecyclerAdapter adapter;

    MODEL model;
    int pos, absPos;

    public JoViewHolder(View itemView) {
        super(itemView);
        this.view = itemView;
        this.context = itemView.getContext();
    }

    public void initHolder(RecyclerView rcv){
        this.rcv = rcv;
        this.adapter = (JoRecyclerAdapter)rcv.getAdapter();
        this.rcvParams = adapter.getParams();
    }

    public void onBind(MODEL model, int pos, int absPos){
        this.model = model;
        this.pos = pos;
        this.absPos = absPos;
    }


    public RecyclerView getRecyclerView(){
        return rcv;
    }

    public JoRecyclerAdapter getAdapter() {
        return adapter;
    }

    public JoRecyclerAdapter.Params getRcvParams() {
        return rcvParams;
    }

    public Context getContext() {
        return context;
    }

    public View getView() {
        return view;
    }

    public MODEL getModel() {
        return model;
    }

    public int getPos() {
        return pos;
    }

    public int getAbsPos() {
        return absPos;
    }

    public abstract void onViewCreated();

    public String getString(int resourceId){
        return getContext().getString(resourceId);
    }
}
