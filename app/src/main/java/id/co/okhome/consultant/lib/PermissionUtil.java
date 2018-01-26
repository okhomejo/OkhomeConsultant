package id.co.okhome.consultant.lib;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by jo on 2018-01-25.
 */

public class PermissionUtil {

    //퍼미션 체크 0, 1, 2에 따라 분기
    public static final int chkPermission(Activity activity, String[] permissions){

        boolean hasPermission = true;
        for(String permission : permissions){
            if(ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED){
                hasPermission = false;
                break;
            }
        }

        boolean notAskedYet = true;
        for(String permission : permissions){
            if(!ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)){
                notAskedYet = false;
                break;
            }
        }

        if(notAskedYet){
            return 2;
        }

        if(!hasPermission){
            return 0;
        }else{
            return 1;
        }
    }

    public static final void requestPermission(Activity activity, String[] permissions){
        int status = chkPermission(activity, permissions);
        switch(status){
            case 0:
            case 2:
                ActivityCompat.requestPermissions(activity, permissions, 1);
                break;
            case 1:
                //성공
                break;
        }

    }
}
