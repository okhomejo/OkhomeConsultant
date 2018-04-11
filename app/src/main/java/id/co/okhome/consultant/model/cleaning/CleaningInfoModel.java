package id.co.okhome.consultant.model.cleaning;

import java.util.List;

public class CleaningInfoModel {
    public int id;
    public String address, homeType, homeSize;
    public String petType; // CAT,DOG | CAT | NONE
    public int roomCnt, restroomCnt;
    public double lat, lng;
    public int person;
    public String when;
    public String commentFromOkhome, commentFromCustomer;
    public float totalCleaningDuration;
    public float displayingCleaningDuration;

    public List<CleaningItemModel> cleanings = null;

    public float score;

}