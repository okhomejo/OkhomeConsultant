package id.co.okhome.consultant.model.v2;

import java.util.Map;

public class AccountModel {
    public String
            id,
            email,
//            password,
            type,  //(C)onsultant (T)rainee. There are only 2 types.
            signupBy,
            logoutYN,
            joinDateTime;

    public ProfileModel profile = null;
    public ConsultantModel consultant = null;
    public TraineeModel trainee = null;
    public Map blocked = null;
}
