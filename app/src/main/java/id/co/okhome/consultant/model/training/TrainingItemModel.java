package id.co.okhome.consultant.model.training;

import java.io.Serializable;
import java.util.List;

public class TrainingItemModel implements Serializable{
    public long id;
    public long trainingId;
    public String subject, faqHotkey;
    public TrainingCommonResultModel trainingResult = null;

    public List<TrainingItemChildModel> listTrainingItemChild = null;
}
