package com.mrjodev.jorecyclermanager;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;

import com.mrjodev.jorecyclermanager.annotations.LayoutMatcher;
import com.mrjodev.jorecyclermanager.footerloading.FooterLoadingInterceptor;
import com.mrjodev.jorecyclermanager.footerloading.FooterLoadingListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by josongmin on 2016-03-23.
 */

public class JoRecyclerAdapter<ITEM> extends RecyclerView.Adapter{

    private static final String LOGTAG = "JoDevRecyclerAdapter";

    public static final int VIEWTYPE_FOOTERLOADING = 21312;
    public static final int VIEWTYPE_QUICKHEADER = 21313;
    public static final int VIEWTYPE_HEADER = Integer.MIN_VALUE;
    public static final int VIEWTYPE_FOOTER = Integer.MIN_VALUE + 1;
    public static final int VIEWTYPE_ITEM = Integer.MIN_VALUE + 2;

    private View vQuickHeader = null;
    private View vFooterLoading = null;

    private List<ITEM> listItems = new ArrayList<ITEM>();
    private List<Object> listHeader = new ArrayList<>();
    private List<Object> listFooter = new ArrayList<>();

    private int itemSize = 0, headerSize = 0, footerSize = 0, totalSize = 0, quickHeaderViewSize = 0, footerLoadingViewSize = 0;
    private Params params;
    private Context context;
    boolean noMoreFooterResult;
    private LayoutInflater inflater = null;
    FooterLoadingInterceptor footerLoadingInterceptor = null;

    public JoRecyclerAdapter() {
        init();
    }

    public JoRecyclerAdapter(Params params) {
        this.params = params;
        init();
    }

    public JoRecyclerAdapter setParams(Params params) {
        this.params = params;
        init();
        return this;
    }

    public Params getParams() {
        return params;
    }

    //기본세팅
    private void init(){
        //do something
        context = params.context;

        params.recyclerView.setAdapter(this);
        switch(params.recyclerLayoutManagerType){
            case Params.GRID: {
                RecyclerView.LayoutManager manager = JoRecyclerLayoutUtil.buildGridLayoutManager(this, params.context, params.spanCount);
                params.recyclerView.setLayoutManager(manager);
                break;
            }
            case Params.LINEAR:{
                params.recyclerView.setLayoutManager(JoRecyclerLayoutUtil.buildLinearLayoutManager(this, params.context, params.orientation));
                break;
            }
        }

        initQuickHeader();
        initFooterLoading();

        if(params.vEmpty != null){
            params.vEmpty.setVisibility(View.GONE);
        }
    }

    //퀵 헤더 설정
    private void initQuickHeader(){
        if(params.vQuickHeader != null){
            vQuickHeader = params.vQuickHeader;
            vQuickHeader.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            int width = vQuickHeader.getMeasuredWidth();
            int height = vQuickHeader.getMeasuredHeight();
            View vQuickHeaderPadding = new View(context);
            vQuickHeaderPadding.setLayoutParams(new ViewGroup.LayoutParams(width, height));
            this.vQuickHeader = vQuickHeaderPadding;
            quickHeaderViewSize = 1;

            QuickReturnViewInitializor.init(params.recyclerView, params.vQuickHeader);

        }
    }

    private void initFooterLoading(){

        if(params.hasFooterLoading){
            FrameLayout vg = new FrameLayout(context);
            vg.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//        vg.setGravity(Gravity.CENTER);
            vg.addView(params.vLoading);
            vFooterLoading = vg;
            footerLoadingViewSize = 1;
            params.vLoading.setVisibility(View.INVISIBLE);
            footerLoadingInterceptor = new FooterLoadingInterceptor(params.footerLoadingListener, new FooterLoadingListener() {
                @Override
                public void onFooterLoading() {
                    params.vLoading.setVisibility(View.VISIBLE);
                }
            });
            params.recyclerView.addOnScrollListener(footerLoadingInterceptor);
        }
    }

    /**푸터 더 있능가 없능가 변경*/
    public void setFooterNoMoreResult(boolean noMoreFooterResult){
        this.noMoreFooterResult = noMoreFooterResult;
        if(footerLoadingInterceptor != null){
            footerLoadingInterceptor.setNoMoreLoading(noMoreFooterResult);
        }

        if(noMoreFooterResult){
            params.vLoading.setVisibility(View.GONE);
        }

    }

    public void notifyFooterLoadingComplete(){
        if(footerLoadingInterceptor != null){
            footerLoadingInterceptor.setNowLoading(false);
            params.vLoading.setVisibility(View.INVISIBLE);
        }
    }

    public List<ITEM> getListItems() {
        return listItems;
    }

    public void removeItem(int pos){
        listItems.remove(pos);

        calcItemsSize();
    }

    //리스트 세팅
    public void setListItems(List<ITEM> listItems) {
        this.listItems = listItems;

        calcItemsSize();
        toggleEmptyView();
    }


    public void addListItems(List<ITEM> listAddItems){
        this.listItems.addAll(listAddItems);

        calcItemsSize();
        toggleEmptyView();
    }

    private void toggleEmptyView(){
        if(listItems.size() <= 0){
            if(params.vEmpty != null){
                params.vEmpty.setVisibility(View.VISIBLE);
            }
        }else{
            if(params.vEmpty != null){
                params.vEmpty.setVisibility(View.GONE);
            }
        }
    }

    public ITEM getLastItem(){
        return listItems.get(listItems.size() - 1);
    }

    /**it used for only one header*/
    public void setHeader(Object header){
        clearAllHeader();
        addHeaderItem(header);
    }

    public void clearAllHeader(){
        this.listHeader.removeAll(listHeader);
        this.headerSize = this.listHeader.size();

        calcItemsSize();
        //dd
    }
    /**헤더아이템 추가*/
    public void addHeaderItem(Object header){
        this.listHeader.add(header);
        this.headerSize = this.listHeader.size();

        calcItemsSize();
    }

    public void addHeaderItemList(List<Object> listHeader){
        this.listHeader.addAll(listHeader);
        this.headerSize = this.listHeader.size();
        calcItemsSize();
    }

    /**푸터아이템 추가*/
    public void addFooterItem(Object footer){
        this.listFooter.add(footer);
        this.footerSize = this.listFooter.size();

        calcItemsSize();
    }

    public void addFooterItemList(List<Object> listFooter){
        this.listFooter.addAll(listFooter);
        this.footerSize = this.listFooter.size();
        calcItemsSize();
    }

    //전체 개수합계
    private void calcItemsSize(){
        this.itemSize = listItems.size();
        this.totalSize = itemSize + headerSize + footerSize + quickHeaderViewSize + footerLoadingViewSize;
    }

    /**header change*/
    public void notifyHeaderChanged(){
        for(int i = 0; i < listHeader.size(); i ++){
            notifyItemChanged(i);
        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if(inflater == null){
            inflater = LayoutInflater.from(parent.getContext());
        }

        try{

            JoViewHolder viewHolder = null;
            switch(viewType){
                case VIEWTYPE_QUICKHEADER:
                    //퀵 헤더일때
                    viewHolder = new JoViewHolder<String>(vQuickHeader) {
                        @Override
                        public void onViewCreated() {
                            ;
                        }
                    };
                    break;

                case VIEWTYPE_HEADER:
                    Log.d(LOGTAG, "onCreateViewHolder VIEWTYPE_HEADER");
                    viewHolder = getTargetViewHolder(onCreateHeaderViewHolder(), params.headerViewHolderCls);

                    break;
                case VIEWTYPE_FOOTER:
                    Log.d(LOGTAG, "onCreateViewHolder VIEWTYPE_FOOTER");
                    viewHolder = getTargetViewHolder(onCreateFooterViewHolder(), params.footerViewHolderCls);

                    break;

                case VIEWTYPE_FOOTERLOADING:
                    //퀵 헤더일때
                    viewHolder = new JoViewHolder<String>(vFooterLoading) {
                        @Override
                        public void onViewCreated() {
                            ;
                        }


                    };
                    break;

                default:
                    Log.d(LOGTAG, "onCreateViewHolder VIEWTYPE_ITEM");
                    viewHolder = getTargetViewHolder(onCreateItemViewHolder(), params.itemViewHolderCls);
                    break;
            }

            return viewHolder;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    //뷰홀더 가져오기
    private JoViewHolder getTargetViewHolder(JoViewHolder userViewHolder, Class<?> cls) throws Exception{
        JoViewHolder viewHolder = null;
        if(userViewHolder != null){
            viewHolder = userViewHolder;
        }

        //리플랙션(디폴트)이용하면 이쪽으로
        else if(params.itemViewHolderCls != null){

            LayoutMatcher layoutMatcher = cls.getAnnotation(LayoutMatcher.class);
            View view = inflater.inflate(layoutMatcher.layoutId(), null, false);
            viewHolder  = (JoViewHolder)(cls.getConstructor(View.class).newInstance(view));
        }
        else{
            throw new Exception();
        }

        viewHolder.initHolder(params.recyclerView);
        viewHolder.onViewCreated();

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if(vQuickHeader != null && position == 0){
            //퀵뷰시다
            ;

        }else if(position < headerSize + quickHeaderViewSize){ //헤더진입
            int headerPosition = position + quickHeaderViewSize;
            int itemPosition = position - quickHeaderViewSize;
            JoViewHolder<Object> headerViewHolder = (JoViewHolder<Object>)holder;
            headerViewHolder.onBind(listHeader.get(itemPosition), headerPosition, position);

        }else if(position >= headerSize + quickHeaderViewSize && position < headerSize + quickHeaderViewSize + itemSize){
            int itemPosition = position - headerSize - quickHeaderViewSize;

            JoViewHolder<ITEM> itemViewHolder = (JoViewHolder<ITEM>)holder;
            itemViewHolder.onBind(listItems.get(itemPosition), itemPosition, position);

        }else{
            if(footerLoadingViewSize == 1 && position == getItemCount()-1){
                //푸터 로딩
                ;
            }else{
                int footerPosition = position - headerSize - itemSize- quickHeaderViewSize;
                int itemPosition = position - headerSize - itemSize - quickHeaderViewSize;
                JoViewHolder<Object> footerViewHolder = (JoViewHolder<Object>)holder;
                footerViewHolder.onBind(listFooter.get(itemPosition), footerPosition, position);
            }
        }
    }

    @Override
    public int getItemCount() {
        return this.totalSize;
    }

    @Override
    public int getItemViewType(int position) {
        if(vQuickHeader != null && position == 0){
            return VIEWTYPE_QUICKHEADER;
        }else if(position < headerSize + quickHeaderViewSize){ //헤더진입
            return VIEWTYPE_HEADER;
        }else if(position >= headerSize + quickHeaderViewSize && position < headerSize + itemSize + quickHeaderViewSize ){
            return VIEWTYPE_ITEM;
        }else{
            if(footerLoadingViewSize == 1 && position == getItemCount()-1){
                return VIEWTYPE_FOOTERLOADING;
            }else{
                return VIEWTYPE_FOOTER;
            }

        }
    }



    public JoViewHolder<Object> onCreateHeaderViewHolder(){
        return null;
    }
    /**푸터 필요하면 오버라이딩*/
    public JoViewHolder<Object> onCreateFooterViewHolder(){
        return null;
    }
    /**아이템 뷰홀더 생성*/
    public JoViewHolder<ITEM> onCreateItemViewHolder(){ return null; };


    public static JoRecyclerAdapter buildInstance(Params params){
        return new JoRecyclerAdapter(params);
    }

    /**파람*/
    public static class Params{
        public final static int LINEAR = 101012;
        public final static int GRID = 101013;

        public Context context;
        //        public int itemViewId, headerViewId, footerViewId;
        public View vQuickHeader = null;
        public Class itemViewHolderCls, headerViewHolderCls, footerViewHolderCls;
        public RecyclerView recyclerView;

        public int recyclerLayoutManagerType = LINEAR;
        public int orientation = LinearLayoutManager.VERTICAL;
        public int spanCount = 3;

        public View vLoading;
        public FooterLoadingListener footerLoadingListener;
        public boolean hasFooterLoading = false;

        public View vEmpty = null;

        public Map<String, Object> globalParams = new HashMap<String, Object>();

        public Params setBottomLoading(View vLoading, FooterLoadingListener footerLoadingListener) {
            this.vLoading = vLoading;
            this.hasFooterLoading = true;
            this.footerLoadingListener = footerLoadingListener;
            return this;
        }

        public Params setQuickHeaderLayoutId(View vQuickHeader) {
            this.vQuickHeader = vQuickHeader;
            return this;
        }

        public Params setEmptyView(View vEmpty){
            this.vEmpty = vEmpty;
            return this;
        }

        public Params setEmptyView(int id){
            ViewParent vParent = recyclerView.getParent();
            while(vParent == null){
                vParent = vParent.getParent();
            }

            View v = ((View)vParent).findViewById(id);
            this.vEmpty = v;

            return this;
        }

        public Params setItemViewHolderCls(Class itemViewHolderCls) {
            this.itemViewHolderCls = itemViewHolderCls;
            return this;
        }

        public Params setFooterViewHolderCls(Class footerViewHolderCls) {
            this.footerViewHolderCls = footerViewHolderCls;
            return this;
        }

        public Params setHeaderViewHolderCls(Class headerViewHolderCls) {
            this.headerViewHolderCls = headerViewHolderCls;
            return this;
        }

//        public Params setItemViewId(int itemViewId){
//            this.itemViewId = itemViewId;
//            return this;
//        }
//
//        public Params setHeaderViewId(int headerViewId){
//            this.headerViewId = headerViewId;
//            return this;
//        }
//
//        public Params setFooterViewId(int footerViewId){
//            this.footerViewId = footerViewId;
//            return this;
//        }

        public Params setGridLayoutManager(int spanCount){
            this.recyclerLayoutManagerType = GRID;
            this.spanCount = spanCount;
            return this;
        }

        public Params setLinearLayoutManager(int orientation){
            this.recyclerLayoutManagerType = LINEAR;
            this.orientation = orientation;
            return this;
        }

        public Params setRecyclerView(RecyclerView recyclerView) {
            this.context = recyclerView.getContext();
            this.recyclerView = recyclerView;
            return this;
        }

        public Params setRecyclerView(Context context, RecyclerView recyclerView) {
            this.context = context;
            this.recyclerView = recyclerView;
            return this;
        }

        public Params addParam(String key, Object value){
            globalParams.put(key, value);
            return this;
        }

        public <T> T getParam(String key){
            T t = (T)globalParams.get(key);
            return t;
        }

        public Map getGlobalParams(){
            return globalParams;
        }

    }
}
