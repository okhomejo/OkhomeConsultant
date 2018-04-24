package id.co.okhome.consultant.lib;

import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by josongmin on 2016-08-18.
 */

public class AnimatedTooltipImageController {

    int tooltipDelay = 2200;
    ImageView ivToolTip;
    int[] arrTooltips;
    boolean isStarted = false;

    static Map<ImageView, AnimatedTooltipImageController> mapInstance = new HashMap<>();

    public static final AnimatedTooltipImageController with(ImageView ivToolTip){
        AnimatedTooltipImageController controller = mapInstance.get(ivToolTip);
        if(controller == null){
            controller = new AnimatedTooltipImageController(ivToolTip);
            mapInstance.put(ivToolTip, controller);
        }
        return controller;
    }


    public AnimatedTooltipImageController(ImageView ivToolTip) {
        this.ivToolTip = ivToolTip;
    }

    public AnimatedTooltipImageController setDelay(int tooltipDelay){
        this.tooltipDelay = tooltipDelay;
        return this;
    }

    public AnimatedTooltipImageController setArrTooltips(int[] arrTooltips) {
        this.arrTooltips = arrTooltips;
        return this;
    }

    public void start(){

        if(isStarted){
            ;
            stop();
        }
        isStarted = true;
        if(arrTooltips != null){
            ivToolTip.setImageResource(arrTooltips[0]);
        }
        handlerForTooltip.removeMessages(0);
        handlerForTooltip.removeCallbacks(null);
        handlerForTooltip.sendEmptyMessageDelayed(0, 140);

    }

    public void stop(){
        handlerForTooltip.removeMessages(0);
        isStarted = false;

    }

    Handler handlerForTooltip = new Handler(){
        int pos = 0;
        @Override
        public void dispatchMessage(Message msg) {
            if(!isStarted){
                return;
            }
            if(arrTooltips != null){
                ivToolTip.setImageResource(arrTooltips[pos % arrTooltips.length]);
            }


            ivToolTip.animate().translationX(20).alpha(0f).setDuration(0).start();
            ivToolTip.animate().translationX(0).alpha(1f).setDuration(100).start();
            pos++;
            sendEmptyMessageDelayed(0, tooltipDelay);
        }
    };
}
