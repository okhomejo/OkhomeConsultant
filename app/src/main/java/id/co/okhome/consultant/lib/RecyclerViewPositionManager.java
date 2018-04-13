package id.co.okhome.consultant.lib;

import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jo on 2018-04-11.
 */

public class RecyclerViewPositionManager{

    final static String KEY = "RCV_STATE";
    final static Map<RecyclerView, Parcelable> mapInstance = new HashMap<>();

    public final static void save(RecyclerView rcv){
        mapInstance.put(rcv, rcv.getLayoutManager().onSaveInstanceState());
    }

    public final static void restore(RecyclerView rcv) {
        if(mapInstance.containsKey(rcv)){
            Parcelable savedRecyclerLayoutState = mapInstance.get(rcv);
            rcv.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState);
            clear(rcv);
        }

    }

    public final static void clear(RecyclerView rcv){
        mapInstance.remove(rcv);
    }
}
