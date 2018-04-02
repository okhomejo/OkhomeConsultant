package id.co.okhome.consultant.view.userinfo.trainee;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mrjodev.jorecyclermanager.JoRecyclerAdapter;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.okhome.consultant.R;
import id.co.okhome.consultant.exception.OkhomeException;
import id.co.okhome.consultant.lib.app.ConsultantLoggedIn;
import id.co.okhome.consultant.lib.app.OkHomeParentActivity;
import id.co.okhome.consultant.lib.app.OkhomeUtil;
import id.co.okhome.consultant.lib.dialog.DialogParent;
import id.co.okhome.consultant.lib.retrofit.RetrofitCallback;
import id.co.okhome.consultant.model.JobExperienceModel;
import id.co.okhome.consultant.model.v2.ProfileModel;
import id.co.okhome.consultant.view.common.dialog.JobExperienceDialog;
import id.co.okhome.consultant.view.viewholder.BlankHolder;
import id.co.okhome.consultant.view.viewholder.JobExperienceVHolder;

/**
 * Created by frizurd on 12/02/2018.
 */

public class UpdateJobExperienceActivity extends OkHomeParentActivity implements DialogParent.CommonDialogListener {

    @BindView(R.id.actJobExp_rcv)                 RecyclerView rcv;
    @BindView(R.id.actJobExp_placeholderWorkExp)  TextView placeHolderText;

    private JoRecyclerAdapter adapter;
    private List<JobExperienceModel> jobExperiences;
    private ProfileModel profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_job_experiences);
        OkhomeUtil.setSystemBarColor(this,
                ContextCompat.getColor(this, R.color.colorOkhome)
        );

        ButterKnife.bind(this);
        init();
    }

    private void init() {
        initRecyclerView();
        initJobExp();
    }

    private void initJobExp() {
        // Load saved consultant data
        profile = ConsultantLoggedIn.get().profile;
        if (Objects.equals(profile.pastCareers, "") || profile.pastCareers != null) {
            Type listType = new TypeToken<ArrayList<JobExperienceModel>>(){}.getType();
            jobExperiences = new Gson().fromJson(profile.pastCareers, listType);
        } else {
            jobExperiences = new ArrayList<>();
        }

        adapter.setListItems(jobExperiences);
        checkIfListEmpty();
    }

    private void initRecyclerView() {
        adapter = new JoRecyclerAdapter(new JoRecyclerAdapter.Params()
                .setRecyclerView(rcv)
                .setItemViewHolderCls(JobExperienceVHolder.class)
                .setFooterViewHolderCls(BlankHolder.class)
        );
        adapter.addFooterItem("");
    }

    public void checkIfListEmpty() {
        if (!jobExperiences.isEmpty()) {
            placeHolderText.setVisibility(View.GONE);
        } else {
            placeHolderText.setVisibility(View.VISIBLE);
        }
    }

    private void updateProfile() {
        final List<JobExperienceModel> jobs = jobExperiences;
        try {
            OkhomeException.chkException(jobs.size() < 1, "Please include at least one job before submitting");
        } catch (OkhomeException e) {
            finish();
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
            JobExperienceModel newJobExp = (JobExperienceModel) mapResult.get(JobExperienceDialog.RESULT_POSITION);
            jobExperiences.add(newJobExp);

            adapter.notifyDataSetChanged();
//            adapter.notifyItemInserted(jobExperiences.size());
        }
    }
}
