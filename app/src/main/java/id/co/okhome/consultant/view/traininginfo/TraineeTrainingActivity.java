package id.co.okhome.consultant.view.traininginfo;

import android.os.Bundle;

import id.co.okhome.consultant.R;
import id.co.okhome.consultant.lib.app.OkHomeParentActivity;
import id.co.okhome.consultant.lib.app.OkhomeUtil;

public class TraineeTrainingActivity extends OkHomeParentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traininginfo_type1);
        OkhomeUtil.setWhiteSystembar(this);
    }

    private void init(){

    }
}
