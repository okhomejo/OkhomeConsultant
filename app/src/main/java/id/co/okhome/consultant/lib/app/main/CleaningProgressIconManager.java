package id.co.okhome.consultant.lib.app.main;

import android.view.View;
import android.widget.ImageView;

import id.co.okhome.consultant.lib.AnimatedTooltipImageController;
import id.co.okhome.consultant.model.page.ConsultantPageMainModel;

/**
 * Created by jo on 2018-04-23.
 */

public class CleaningProgressIconManager {

    private ImageView ivCleaningIcon;
    private View vNewIcon;
    private View[] vEtc;

    public CleaningProgressIconManager(ImageView ivCleaningIcon, View vNewIcon, View ... vEtc) {
        this.ivCleaningIcon = ivCleaningIcon;
        this.vNewIcon = vNewIcon;
        this.vEtc = vEtc;

        for(View v:  vEtc){
            v.setVisibility(View.GONE);
        }
    }

    public void check(ConsultantPageMainModel.OnCleaning onCleaning){

        ivCleaningIcon.setVisibility(View.GONE);
        vNewIcon.setVisibility(View.GONE);
        AnimatedTooltipImageController.with(ivCleaningIcon).stop();

        for(View v:  vEtc){
            v.setVisibility(View.GONE);
        }


        if (onCleaning.showYN.equals("Y")) {
            ivCleaningIcon.setVisibility(View.VISIBLE);

            for(View v:  vEtc){
                v.setVisibility(View.VISIBLE);
            }

            if(onCleaning.checkedYN.equals("N")){
                vNewIcon.setVisibility(View.VISIBLE);
            }

            if(onCleaning.animateYN.equals("Y")){
                AnimatedTooltipImageController.with(ivCleaningIcon).setArrTooltips(null).setDelay(1000).start();
            }
        }
    }

}
