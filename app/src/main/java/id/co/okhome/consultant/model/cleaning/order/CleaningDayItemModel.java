package id.co.okhome.consultant.model.cleaning.order;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jo on 2018-05-28.
 */

public class CleaningDayItemModel implements Serializable {
    public int cleaningId; //청소 번호
    public String notoolYN; //노툴YN
    public String commentFromCustomer, commentFromOkhome; //코멘트
    public String whenDateTime; //언제?
    public String whenDate; //언제?

    public int personRequired = 0; //컨설턴트 몇명?
    public int personOccupied = 0;

    public float grade;
    public String review;

    public int totalDurationMinute; //전체 청소 시간
    public int priceConsultant, priceCustomer; //전체 가격

    public String realBeginDateTime = null;
    public String realEndDateTime = null;

    public String homeAddress;

    public List<CleaningOrderDaySpecificationModel> cleaningOrderSpecifications = null;
}
