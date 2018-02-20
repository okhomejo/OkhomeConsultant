package id.co.okhome.consultant.view.userinfo.trainee;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.okhome.consultant.R;
import id.co.okhome.consultant.adapter.JobExperienceListAdapter;
import id.co.okhome.consultant.exception.OkhomeException;
import id.co.okhome.consultant.lib.ToastUtil;
import id.co.okhome.consultant.lib.app.ConsultantLoggedIn;
import id.co.okhome.consultant.lib.app.OkHomeParentActivity;
import id.co.okhome.consultant.lib.app.OkhomeUtil;
import id.co.okhome.consultant.lib.dialog.DialogParent;
import id.co.okhome.consultant.lib.retrofit.RetrofitCallback;
import id.co.okhome.consultant.model.ConsultantModel;
import id.co.okhome.consultant.model.JobExperienceModel;
import id.co.okhome.consultant.view.common.dialog.JobExperienceDialog;

/**
 * Created by frizurd on 12/02/2018.
 */

public class UpdateJobExperienceActivity extends OkHomeParentActivity implements DialogParent.CommonDialogListener {

    @BindView(R.id.actJobExp_list)                ListView itemsListView;
    @BindView(R.id.actJobExp_placeholderWorkExp)  TextView placeHolderText;

    private JobExperienceListAdapter jobExperienceAdapter;
    private List<JobExperienceModel> jobExperiences;
    private ConsultantModel consultant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_job_experiences);
        OkhomeUtil.setSystemBarColor(this,

//                Color.parseColor("#29313a"));
                ContextCompat.getColor(this, R.color.colorOkhome));

        ButterKnife.bind(this);
        init();
    }

    private void init() {
        // Load saved consultant data
        if (ConsultantLoggedIn.hasSavedData()) {
            consultant = ConsultantLoggedIn.get();
            if (!consultant.pastCareers.isEmpty()) {
                Type listType = new TypeToken<ArrayList<JobExperienceModel>>(){}.getType();
                jobExperiences = new Gson().fromJson(consultant.pastCareers, listType);
            } else {
                jobExperiences = new ArrayList<>();
            }
        } else {
            jobExperiences = new ArrayList<>();
        }

        jobExperienceAdapter = new JobExperienceListAdapter(this, jobExperiences);
        itemsListView.setAdapter(jobExperienceAdapter);

        checkIfListEmpty();
    }

    public void checkIfListEmpty() {
        if (!jobExperiences.isEmpty()) {
            placeHolderText.setVisibility(View.GONE);
        } else {
            placeHolderText.setVisibility(View.VISIBLE);
        }
        jobExperienceAdapter.notifyDataSetChanged();
    }

    private void updateProfile() {
        final List<JobExperienceModel> jobs = jobExperiences;
        try {
            OkhomeException.chkException(jobs.size() < 1, "Please include at least one job before submitting");

        } catch (OkhomeException e) {
            ToastUtil.showToast(e.getMessage());
            return;
        }

        final ProgressDialog p = ProgressDialog.show(this, "", "Loading");
        final RetrofitCallback retrofitCallback = new RetrofitCallback<String>() {

            @Override
            public void onSuccess(String result) {
                finish();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                p.dismiss();
            }
        };

        String jobGson = new Gson().toJson(jobs);
        ConsultantLoggedIn.updateUserInfo(
                OkhomeUtil.makeMap("past_careers", jobGson),
                retrofitCallback
        );
    }

    @OnClick(R.id.actJobExp_vbtnAdd)
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
        updateProfile();
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
