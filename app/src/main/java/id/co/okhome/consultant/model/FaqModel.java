package id.co.okhome.consultant.model;

public class FaqModel {
    public final static String CATEGORY_MANUAL_TRAINEE = "MANUAL_TRAINEE";
    public final static String CATEGORY_MANUAL_TRAINER = "MANUAL_TRAINER";

    public int id;
    public int childCount = 0;
    public String category;
    public String orderNo, parentId, subject, contents, insertDateTime;
}