package id.co.okhome.consultant.view.userinfo.trainee;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.okhome.consultant.R;
import id.co.okhome.consultant.adapter.JobExperienceListAdapter;
import id.co.okhome.consultant.adapter.RegionListAdapter;
import id.co.okhome.consultant.lib.app.OkHomeParentActivity;
import id.co.okhome.consultant.lib.app.OkhomeUtil;
import id.co.okhome.consultant.lib.dialog.DialogParent;
import id.co.okhome.consultant.model.JobExperienceModel;
import id.co.okhome.consultant.model.WorkingRegionModel;
import id.co.okhome.consultant.view.common.dialog.JobExperienceDialog;
import id.co.okhome.consultant.view.common.dialog.PhoneVerificationDialog;

/**
 * Created by frizurd on 12/02/2018.
 */

public class UpdateJobExperienceActivity extends OkHomeParentActivity implements DialogParent.CommonDialogListener {

    @BindView(R.id.actJobExp_list)                ListView itemsListView;
    @BindView(R.id.actJobExp_placeholderWorkExp)  TextView placeHolderText;

    private JobExperienceListAdapter jobExperienceAdapter;
    private List<JobExperienceModel> jobExperiences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_job_experiences);
        ButterKnife.bind(this);

        init();
    }

    private void init() {
        jobExperiences = new ArrayList<>();

        jobExperienceAdapter = new JobExperienceListAdapter(this, jobExperiences);
        itemsListView.setAdapter(jobExperienceAdapter);
    }

    public void checkIfListEmpty() {
        if (!jobExperiences.isEmpty()) {
            placeHolderText.setVisibility(View.GONE);
        } else {
            placeHolderText.setVisibility(View.VISIBLE);
        }
        jobExperienceAdapter.notifyDataSetChanged();
    }

    @OnClick({R.id.actJobExp_btnAddExp, R.id.actJobExp_vbtnAdd})
    public void onButtonAddExperience() {
        JobExperienceDialog expDialog = new JobExperienceDialog(this, this);
        expDialog.show();
    }

    @OnClick(R.id.actJobExp_vbtnX)
    public void onGoBackClick() {
        finish();
    }

    @OnClick(R.id.actJobExp_vbtnOk)
    public void onButtonSubmit() {
        finish();
    }

    @Override
    public void onCommonDialogWorkDone(Dialog dialog, int actionCode, Map<String, Object> mapResult) {
        if(actionCode == ACTIONCODE_OK){
            JobExperienceModel newJobExp = (JobExperienceModel)mapResult.get(JobExperienceDialog.RESULT_POSITION);
            jobExperiences.add(newJobExp);

            checkIfListEmpty();
        }
    }
}
