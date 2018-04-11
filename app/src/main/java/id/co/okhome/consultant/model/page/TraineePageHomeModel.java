package id.co.okhome.consultant.model.page;


import id.co.okhome.consultant.model.training.TrainingModel;

public class TraineePageHomeModel {
    public String name, birthdate, gender, photoUrl, type, onGoingTrainingType;
    public int basicTrainingCount = 0, basicTrainingProgressCount = 0;
    public int ojtTrainingCount = 0, ojtTrainingProgressCount = 0;
    public int onGoingTrainingPos = 0, onGoingTrainingAbsPos = 0;

    public TrainingModel trainingEarliest = null;
}
