package id.co.okhome.consultant.adapter;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.okhome.consultant.R;
import id.co.okhome.consultant.lib.ViewHolderUtil;
import id.co.okhome.consultant.lib.app.OkhomeUtil;
import id.co.okhome.consultant.lib.dialog.DialogParent;
import id.co.okhome.consultant.model.JobExperienceModel;
import id.co.okhome.consultant.view.common.dialog.CommonAlertDialog;
import id.co.okhome.consultant.view.common.dialog.CommonInputDialog;
import id.co.okhome.consultant.view.common.dialog.CommonListDialog;
import id.co.okhome.consultant.view.viewholder.StringHolder;

/**
 * Created by frizurd on 12/02/2018.
 */

public class JobExperienceListAdapter extends BaseAdapter {
    private Context context;
    private List<JobExperienceModel> jobExperiences;

    public JobExperienceListAdapter(Context context, List<JobExperienceModel> items) {
        this.context = context;
        this.jobExperiences = items;
    }

    @Override
    public int getCount() {
        return jobExperiences.size();
    }

    @Override
    public JobExperienceModel getItem(int position) {
        return jobExperiences.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_work_experience, parent, false);
        }

        TextView numPosition = ViewHolderUtil.getView(convertView, R.id.itemJob_tvPosition);
        TextView date        = ViewHolderUtil.getView(convertView, R.id.itemJob_tvDate);

        final JobExperienceModel jobExp = getItem(position);
        numPosition.setText(jobExp.position);
        date.setText(jobExp.workPeriod);

        ImageView rmExp = convertView.findViewById(R.id.itemJob_vbtnRemoveExp);
        rmExp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new CommonAlertDialog(context)
                        .setSubTitle("Removing job experience")
                        .setTitle("Are you sure?")
                        .setCommonDialogListener(new DialogParent.CommonDialogListener() {
                            @Override
                            public void onCommonDialogWorkDone(Dialog dialog, int actionCode, Map<String, Object> mapResult) {
                                if(actionCode == ACTIONCODE_OK){
                                    jobExperiences.remove(jobExp);
                                    notifyDataSetChanged();
                                }
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        });
        return convertView;
    }
}