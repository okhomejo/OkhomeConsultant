package id.co.okhome.consultant.model.cleaning.order;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by jo on 2018-05-28.
 */

public class CleaningOrderModel implements Serializable {
    //주문자, 집정보
    public int orderId; //오더번호
    public int cleaningCount;
    public int requesterId;
    public String requesterName;

    public int personRequired = 0;
    public String homeAddress, homeType, homeSize;
    public int homeRoomCnt, homeRestroomCnt;

    public String[] homePets = new String[]{};
    public double homeLng, homeLat;
    public int homeAreaCode; //tebet등 지역 코드.


    public String commentCustomer, commentOkhome;

    //선호 정보
    public int[] favoConsultantIds;

    //필터링정보
    public String prefGender;
    public Map<String, String> prefs;



    public List<CleaningDayItemModel> cleaningOrderDayItemModels;

}
