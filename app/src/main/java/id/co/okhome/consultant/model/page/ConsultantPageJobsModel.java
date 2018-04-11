package id.co.okhome.consultant.model.page;


import java.util.List;

import id.co.okhome.consultant.model.cleaning.CleaningInfoModel;

public class ConsultantPageJobsModel {
    public List<CleaningInfoModel> nextCleanings = null;
    public List<CleaningInfoModel> prevCleanings = null;

    public int nextCleaningCnt, prevCleaningCnt;


}
