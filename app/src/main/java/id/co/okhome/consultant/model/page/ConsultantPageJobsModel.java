package id.co.okhome.consultant.model.page;


import java.util.List;

import id.co.okhome.consultant.model.cleaning.CleaningDetailWithOrderInfoModel;
import id.co.okhome.consultant.model.cleaning.order.CleaningDayItemModel;

public class ConsultantPageJobsModel {
    public CleaningDetailWithOrderInfoModel nextJob = null;
    public List<CleaningDayItemModel> cleaningItems;
}
