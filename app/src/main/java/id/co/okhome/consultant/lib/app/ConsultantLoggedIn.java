package id.co.okhome.consultant.lib.app;

import id.co.okhome.consultant.config.OkhomeRegistryKey;
import id.co.okhome.consultant.lib.JoSharedPreference;
import id.co.okhome.consultant.model.ConsultantModel;

/**
 * Created by jo on 2018-01-28.
 */

public class ConsultantLoggedIn {
    /**set consultant information on sdcard*/
    public final static void set(ConsultantModel consultant){
        JoSharedPreference.with().push(OkhomeRegistryKey.LOGIN_CONSULTANT, consultant);
    }

    /**get consultant information from sdcard*/
    public final static ConsultantModel get(){
        return JoSharedPreference.with().get(OkhomeRegistryKey.LOGIN_CONSULTANT);
    }

    /**if there is saved consultant data logged-in, return true.*/
    public static boolean hasSavedData(){
        return JoSharedPreference.with().get(OkhomeRegistryKey.LOGIN_CONSULTANT) == null ? false : true;
    }

    /**clear all data. It may be called on logout*/
    public final static void clear(){
        JoSharedPreference.with().push(OkhomeRegistryKey.LOGIN_CONSULTANT, null);
    }

}
