package id.co.okhome.consultant.lib;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

/**
 * Created by josongmin on 2016-08-30.
 */

public class DelayedFinish {
    private final static int DELAY = 1000;
    private static boolean ready = false;
    public final static void delayedFinish(Activity act, String message){
        if(ready == false){
            Toast.makeText(act, message, Toast.LENGTH_SHORT).show();
            ready = true;
            handler.sendEmptyMessageDelayed(0, DELAY);
            return;
        }else{
            act.finish();
        }

    }

    private static Handler handler = new Handler(){
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            ready = false;
        }
    };
}
