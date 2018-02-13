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
import id.co.okhome.consultant.R;
import id.co.okhome.consultant.lib.app.OkhomeUtil;
import id.co.okhome.consultant.model.JobExperienceModel;
import id.co.okhome.consultant.view.common.dialog.CommonAlertDialog;
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
        return jobExperiences.size(); //returns total item in the list
    }

    @Override
    public JobExperienceModel getItem(int position) {
        return jobExperiences.get(position); //returns the item at the specified position
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_work_experience, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        JobExperienceModel jobExp = getItem(position);
        viewHolder.position.setText(jobExp.position);
        viewHolder.date.setText(jobExp.workPeriod);


        ImageView rmExp = convertView.findViewById(R.id.itemJob_vbtnRemoveExp);
        rmExp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new CommonListDialog(context)
                        .setTitle("Are you sure?")
                        .setArrItems("Yes", "No") //value displayed on list
                        .setArrItemTag("Y", "N") // value will be sent to server
                        .setColumnCount(2)
                        .setItemClickListener(new StringHolder.ItemClickListener() {
                            @Override
                            public void onItemClick(Dialog dialog, int pos, String item, String tag) {
                                if (tag.equals("Y")) {
                                    jobExperiences.remove(pos);
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

    //ViewHolder inner class
    private class ViewHolder {
        TextView position;
        TextView date;

        public ViewHolder(View view) {
            position    = view.findViewById(R.id.itemJob_tvPosition);
            date        = view.findViewById(R.id.itemJob_tvDate);
        }
    }
}