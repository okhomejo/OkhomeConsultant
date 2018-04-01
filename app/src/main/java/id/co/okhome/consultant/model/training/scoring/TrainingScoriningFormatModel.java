package id.co.okhome.consultant.model.training.scoring;



import java.util.List;

public class TrainingScoriningFormatModel {
    public int trainingGoingonId, trainingId, traineeId;
    public TrainingScoriningModel trainingLevel1Score = null;
    public List<TrainingScoriningModel> listTrainingLevel2Score = null;
    public List<TrainingScoriningModel> listTrainingLevel3Score = null;
}
