package id.co.okhome.consultant.view.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mrjodev.jorecyclermanager.JoViewHolder;
import com.mrjodev.jorecyclermanager.annotations.LayoutMatcher;

import java.text.MessageFormat;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.okhome.consultant.R;
import id.co.okhome.consultant.model.WorkingRegionModel;
import id.co.okhome.consultant.view.userinfo.trainee.UpdateConsultantAreaActivity;

/**
 * Created by frizurd on 28/03/2018.
 */

@LayoutMatcher(layoutId = R.layout.item_area_child_city)
public class PreferredChildAreaVHolder extends JoViewHolder<WorkingRegionModel> implements View.OnClickListener {

    @BindView(R.id.itemAreaChild_tvCityTitle)     TextView cityName;
    @BindView(R.id.itemAreaChild_tvSubTitle)      TextView subTitle;
    @BindView(R.id.itemAreaChild_ivArrow)         ImageView arrowImage;
    @BindView(R.id.itemAreaChild_ivCheck)         ImageView checkImage;
    @BindView(R.id.itemAreaChild_vbtnArrow)       RelativeLayout btnArrow;

    private Set<Integer> chosenRegions;
    private List<WorkingRegionModel> allRegions;

    public PreferredChildAreaVHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void onViewCreated() {
        chosenRegions   = getAdapter().getParams().getParam("chosenRegions");
        allRegions      = getAdapter().getParams().getParam("allRegions");

        ButterKnife.bind(this, getView());
    }

    @Override
    public void onBind(final WorkingRegionModel region, int pos, int absPos) {
        super.onBind(region, pos, absPos);

        if (region.childCount > 0) {
            // Call new child region dialog
            btnArrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    listDialog.dismiss();
                    UpdateConsultantAreaActivity areaActivity = (UpdateConsultantAreaActivity) getContext();
                    areaActivity.callChildRegionDialog(region);
                }
            });
        }

        // Set child region arrow visible or gone
        cityName.setText(region.address);
        if (region.hasChild || region.childCount > 0) {
            arrowImage.setVisibility(View.VISIBLE);
        } else {
            arrowImage.setVisibility(View.GONE);
        }

        // Set region check mark image checked, gone or half checked
        if (region.childCount > 0) {
            subTitle.setVisibility(View.VISIBLE);
            int childRegionCount = 0, chosenChildCount = 0;
            for (WorkingRegionModel newRegion : allRegions) {
                if (newRegion.parentId == region.id) {
                    if (chosenRegions.contains(newRegion.id)) {
                        chosenChildCount++;
                    }
                    childRegionCount++;
                }
            }
            if (childRegionCount == chosenChildCount) {
                checkImage.setImageResource(R.drawable.ic_checked_sq);
                checkImage.setImageAlpha(255);
                subTitle.setText(MessageFormat.format("{0} of {0} selected", childRegionCount));
            } else if (chosenChildCount > 0 && chosenChildCount < childRegionCount) {
                checkImage.setImageResource(R.drawable.ic_checked_sq);
                checkImage.setImageAlpha(50);
                subTitle.setText(MessageFormat.format("{0} of {1} selected", chosenChildCount, childRegionCount));
            } else {
                checkImage.setImageResource(R.drawable.ic_check_not_deep);
                checkImage.setImageAlpha(255);
                subTitle.setText("Not selected");
            }
        } else {
            subTitle.setVisibility(View.GONE);
            if (chosenRegions.contains(region.id)) {
                checkImage.setImageResource(R.drawable.ic_checked_sq);
            } else {
                checkImage.setImageResource(R.drawable.ic_check_not_deep);
            }
        }
        getView().setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        final WorkingRegionModel region = getModel();

        if (region.childCount == 0) {
            // Add or remove a child region
            if (chosenRegions.contains(region.id)) {
                chosenRegions.remove(region.id);
            } else {
                chosenRegions.add(region.id);
            }
        } else {
            // Count how many child regions and how many of them are selected
            int childRegionCount = 0, chosenChildCount = 0;
            for (WorkingRegionModel newRegion : allRegions) {
                if (newRegion.parentId == region.id) {
                    if (chosenRegions.contains(newRegion.id)) {
                        chosenChildCount++;
                    }
                    childRegionCount++;
                }
            }
            // Add or remove all child regions of parent region
            if (childRegionCount == chosenChildCount) {
                for (WorkingRegionModel newRegion : allRegions) {
                    // Add all children
                    if (newRegion.parentId == region.id) {
                        chosenRegions.remove(newRegion.id);
                    }
                }
            } else if (chosenChildCount == 0 || chosenChildCount < childRegionCount) {
                for (WorkingRegionModel newRegion : allRegions) {
                    // Remove all children
                    if (newRegion.parentId == region.id) {
                        chosenRegions.add(newRegion.id);
                    }
                }
            }
        }
        getAdapter().notifyDataSetChanged();
    }
}