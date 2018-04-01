package id.co.okhome.consultant.model.training;

import java.util.List;

public class TrainingModel {

    public int trainingId;
    public String type, subject, desc;

    public TrainingAttendanceForTraineeModel trainingAttendanceForTrainee = null;
    public TrainingCommonResultModel trainingResult = null;
    public List<TrainingItemModel> listTrainingItemModel = null;
}
