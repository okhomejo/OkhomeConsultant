package id.co.okhome.consultant.view.activity.etc;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;

import com.mrjodev.jorecyclermanager.JoRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.okhome.consultant.R;
import id.co.okhome.consultant.lib.app.OkHomeParentActivity;
import id.co.okhome.consultant.view.viewholder.BlankHeaderTestVHolder;

public class TestActivity extends OkHomeParentActivity{

    @BindView(R.id.actTest_rcv)
    RecyclerView rcv;

    JoRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.bind(this);

        adapter = new JoRecyclerAdapter(new JoRecyclerAdapter.Params()
                .setRecyclerView(rcv)
                .setItemViewHolderCls(BlankHeaderTestVHolder.class)
                .setHeaderViewHolderCls(BlankHeaderTestVHolder.class)
        );

        loadList();

        new Handler(){
            @Override
            public void dispatchMessage(Message msg) {
                super.dispatchMessage(msg);

                setHeader("test " + new Random().nextInt(10) +"");
                sendEmptyMessageDelayed(0, 1000);
            }
        }.sendEmptyMessageDelayed(0, 1000);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void setHeader(String name){
        adapter.setHeader(name+ " " + System.currentTimeMillis());
        adapter.notifyHeaderChanged();
    }

    private void loadList(){
        List<String> item = new ArrayList<>();
        for(int i = 0; i < 10; i ++){
            item.add(i+"");
        }

        adapter.setListItems(item);
        adapter.notifyDataSetChanged();
    }

}
