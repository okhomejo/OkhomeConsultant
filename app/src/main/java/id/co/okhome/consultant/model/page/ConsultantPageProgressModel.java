package id.co.okhome.consultant.model.page;

import java.util.HashMap;
import java.util.Map;

public class ConsultantPageProgressModel {
    public int balance, salaryThisMonthTotal, salaryThisMonthPaid;
    public float reviewScore7days, reviewScoreOverall, reviewScoreNear;
    public Map<String, Integer> myWorkCnt = new HashMap<>();
    public int othersWorkCnt;
}
