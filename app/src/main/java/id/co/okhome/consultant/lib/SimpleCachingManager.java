package id.co.okhome.consultant.lib;

import java.util.HashMap;
import java.util.Map;

public class SimpleCachingManager {

    private static Map<String, Object> mapInstance = new HashMap<>();

    /**set data. it will be stored at mem or mem and sdcard*/
    public static <T> void setData(String key, T data, boolean saveSdcardAlso){
        getInstance().put(key, data);
        if(saveSdcardAlso){
            JoSharedPreference.with().push(key, data);
        }
    }

    /**get data which stored at mem and sdcard*/
    public static <T> T getData(String key){
        //check is there data at Mem
        Object o = mapInstance.get(key);
        if(o == null){
            o = JoSharedPreference.with().get(key);
            //check is there data at Sdcard
            if(o == null){
                return null;
            }else{
                setData(key, o, false);
            }
        }

        return (T)o;
    }

    private static Map<String, Object> getInstance(){
        if(mapInstance == null){
            mapInstance = new HashMap<>();
        }
        return mapInstance;
    }
}
