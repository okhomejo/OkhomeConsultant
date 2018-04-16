package id.co.okhome.consultant.view.viewholder;

import android.app.Dialog;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mrjodev.jorecyclermanager.JoViewHolder;
import com.mrjodev.jorecyclermanager.annotations.LayoutMatcher;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.okhome.consultant.R;
import id.co.okhome.consultant.lib.dialog.DialogParent;
import id.co.okhome.consultant.model.JobExperienceModel;
import id.co.okhome.consultant.view.dialog.CommonAlertDialog;

/**
 * Created by frizurd on 27/03/2018.
 */
@LayoutMatcher(layoutId = R.layout.item_work_experience)
public class JobExperienceVHolder extends JoViewHolder<JobExperienceModel> implements View.OnClickListener {

    @BindView(R.id.itemJob_vbtnRemoveExp)     ImageView btnRemove;
    @BindView(R.id.itemJob_tvPosition)        TextView numPosition;
    @BindView(R.id.itemJob_tvDate)            TextView date;

    public JobExperienceVHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void onViewCreated() {
        ButterKnife.bind(this, getView());
    }

    @Override
    public void onBind(JobExperienceModel m, int pos, int absPos) {
        super.onBind(m, pos, absPos);

        numPosition.setText(m.position);

        String startDate = m.workPeriod.substring(0,7);
        String endDate   = m.workPeriod.substring(8,15);

        DateTimeFormatter dtf1 = DateTimeFormat.forPattern("MMMM yyyy");
        DateTimeFormatter dtf2 = DateTimeFormat.forPattern("yyyy-MM");

        DateTime start  = dtf2.parseDateTime(startDate);
        DateTime end    = dtf2.parseDateTime(endDate);

        date.setText(String.format("%s, %s", start.toString(dtf1), end.toString(dtf1)));

        btnRemove.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        new CommonAlertDialog(getContext())
                .setSubTitle("Removing job experience")
                .setTitle("Are you sure?")
                .setCommonDialogListener(new DialogParent.CommonDialogListener() {
                    @Override
                    public void onCommonDialogWorkDone(Dialog dialog, int actionCode, Map<String, Object> mapResult) {
                        if(actionCode == ACTIONCODE_OK){
                            getAdapter().removeItem(getPos());
                            getAdapter().notifyItemRemoved(getPos());
                        }
                        dialog.dismiss();
                    }
                })
                .show();
    }
}