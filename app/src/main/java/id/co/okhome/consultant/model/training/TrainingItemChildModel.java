package id.co.okhome.consultant.model.training;


import java.io.Serializable;

public class TrainingItemChildModel implements Serializable {
    public int id;
    public int trainingItemId;
    public String  contents;

    public TrainingCommonResultModel trainingResult = null;
}
