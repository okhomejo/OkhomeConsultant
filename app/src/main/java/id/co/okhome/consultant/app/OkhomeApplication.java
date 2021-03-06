package id.co.okhome.consultant.app;

import android.app.Application;
import android.content.Context;

import id.co.okhome.consultant.R;
import id.co.okhome.consultant.lib.JoSharedPreference;
import id.co.okhome.consultant.lib.ToastUtil;
import id.co.okhome.consultant.lib.retrofit.RetrofitCallback;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by jo on 2018-01-02.
 */

public class OkhomeApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        RetrofitCallback.setApplicationContext(this);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
//                .setDefaultFontPath("fonts/Raleway-Medium.ttf")
                        .setFontAttrId(R.attr.fontPath)
                        .build()
        );

        int i = 0;
        i++;
        ToastUtil.setContext(this);
    }


    @Override
    protected void attachBaseContext(Context newBase) {
        JoSharedPreference.setContext(newBase);
        super.attachBaseContext(newBase);
    }
}
