package id.co.okhome.consultant.model.cleaning.order;

import java.io.Serializable;

/**
 * Created by jo on 2018-05-28.
 */

public class CleaningOrderDaySpecificationModel implements Serializable {
    public int id;
    public String name;
    public int durationMin;
    public int count; // 몇개?

    public final static String getCleaningItemName(String tagName){
        switch(tagName){
            case "BASIC":
                return "Basic Cleaning";
        }
        return "null";
    }
}
