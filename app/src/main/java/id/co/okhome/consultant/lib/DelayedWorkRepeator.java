package id.co.okhome.consultant.lib;

import android.os.Handler;
import android.os.Message;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by josong on 2017-06-17.
 */

public class DelayedWorkRepeator {

    static Map<Object, DelayedWorkRepeator> mapInstance = new HashMap<>();
    Object obj;
    int delay;
    Job job;
    int count = 1;
    Handler handler;
    int pos = 0;
    public DelayedWorkRepeator(Object obj) {
        this.obj = obj;
    }

    public static final DelayedWorkRepeator with(Object obj){
        DelayedWorkRepeator repeator = mapInstance.get(obj);
        if(repeator == null){
            repeator = new DelayedWorkRepeator(obj);
            mapInstance.put(obj, repeator);
            repeator.initHandler();
        }
        return repeator;

    }

    private void initHandler(){
        handler = new Handler(){
            @Override
            public void dispatchMessage(Message msg) {
                if(pos >= count && pos != -1){
                    return;
                }
                job.work();

                handler.removeMessages(0);
                handler.sendEmptyMessageDelayed(0, delay);

                pos++;
            }
        };
    }

    public DelayedWorkRepeator setDelay(int delay) {
        this.delay = delay;
        return this;
    }

    public DelayedWorkRepeator setJob(Job job) {
        this.job = job;
        return this;
    }

    public DelayedWorkRepeator setCount(int count) {
        this.count = count;
        return this;
    }

    public final void work(){
        this.pos = 0;
        handler.removeMessages(0);
        handler.sendEmptyMessageDelayed(0, delay);

    }

    public final void stop(){
        handler.removeMessages(0);
    }

    public interface Job{
        void work();
    }
}
