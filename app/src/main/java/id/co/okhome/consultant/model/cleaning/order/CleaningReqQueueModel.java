package id.co.okhome.consultant.model.cleaning.order;

import java.io.Serializable;

/**
 * Created by jo on 2018-05-28.
 */

public class CleaningReqQueueModel implements Serializable{
    public int queueId, orderId;
    public String type;
    public String payType;
    public int personOccupied, personRequired;
    public long firstCleaningDateTime;


    public CleaningOrderModel cleaningOrderInfo;
}
