package id.co.okhome.consultant.lib.app;

import android.content.Context;
import android.content.ContextWrapper;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class OkHomeParentActivity extends AppCompatActivity {

    public final static int STATUS_ON       = 10001;
    public final static int STATUS_PAUSE    = 10002;
    public final static int STATUS_OFF      = 10003;

    public static String CurrentLocale = null;
    int status = STATUS_OFF;
    public int getStatus() {
        return status;
    }
    public static List<OkHomeParentActivity> listActivities = new ArrayList();
    public static Context lastContext = null;

    public static int getOpendActivityCount(){
        return listActivities.size();
    }

    public static void inputNewActivity(OkHomeParentActivity activity){
        listActivities.add(activity);
    }

    public static void destroyActivity(OkHomeParentActivity activity){
        listActivities.remove(activity);
    }

    public static void finishAllActivities(){
        finishAllActivitiesWithout(null);
    }

    public static void finishAllActivitiesWithout(OkHomeParentActivity activity){
        List<OkHomeParentActivity> listRemoved = new ArrayList<>();

        for(OkHomeParentActivity act : listActivities){
            if(activity == null){
                listRemoved.add(act);
            }else{
                if(act != activity){
                    listRemoved.add(act);
                }
            }
        }

        for(OkHomeParentActivity act : listRemoved){
            act.finish();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        inputNewActivity(this);
        super.onCreate(savedInstanceState);

        // Disable screenshots in all child activities
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
    }

    @Override
    protected void onResume() {
        lastContext = this;
        status = STATUS_ON;
        super.onResume();
    }

    @Override
    protected void onPause() {
        status = STATUS_PAUSE;
        super.onPause();
    }

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        inputNewActivity(this);
        super.onCreate(savedInstanceState, persistentState);
    }

    @Override
    protected void onDestroy() {
        status = STATUS_OFF;
        destroyActivity(this);
        super.onDestroy();
    }

    @Override
    protected void attachBaseContext(Context newBase) {

        //폰트, 스케일 설정
        ContextWrapper contextWrapper = CalligraphyContextWrapper.wrap(newBase);
        super.attachBaseContext(contextWrapper);
    }
}
