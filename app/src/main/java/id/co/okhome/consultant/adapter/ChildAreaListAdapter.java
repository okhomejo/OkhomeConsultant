package id.co.okhome.consultant.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.okhome.consultant.R;
import id.co.okhome.consultant.lib.app.OkhomeUtil;
import id.co.okhome.consultant.model.WorkingRegionModel;
import id.co.okhome.consultant.view.userinfo.trainee.UpdateConsultantAreaActivity;

/**
 * Created by frizurd on 14/02/2018.
 */

public class ChildAreaListAdapter extends BaseAdapter {

    private Context context;
    private List<WorkingRegionModel> regions;
    private List<WorkingRegionModel> allRegions;
    private Set<Integer> chosenRegions;

    public ChildAreaListAdapter(Context context, List<WorkingRegionModel> items, Set<Integer> chosenRegions, List<WorkingRegionModel> allRegions) {
        this.context = context;
        this.regions = items;
        this.allRegions = allRegions;
        this.chosenRegions = chosenRegions;
    }

    public void setRegionList(List<WorkingRegionModel> regions) {
        this.regions = regions;
    }

    @Override
    public int getCount() {
        return regions.size(); //returns total item in the list
    }

    @Override
    public WorkingRegionModel getItem(int position) {
        return regions.get(position); //returns the item at the specified position
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        final ViewHolder viewHolder;
        final WorkingRegionModel region = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_area_child_city, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.btnArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateConsultantAreaActivity areaActivity = (UpdateConsultantAreaActivity) context;
                areaActivity.callChildRegionDialog(region);
            }
        });

        viewHolder.cityName.setText(region.address);
        if (region.hasChild || region.childCount > 0) {
            viewHolder.arrowImage.setVisibility(View.VISIBLE);
        } else {
            viewHolder.arrowImage.setVisibility(View.GONE);
        }

        if (region.childCount > 0) {
            int childRegionCount = 0, chosenChildCount = 0;
            for(WorkingRegionModel newRegion : allRegions) {
                if (newRegion.parentId == region.id) {
                    if (chosenRegions.contains(newRegion.id)) {
                        chosenChildCount++;
                    }
                    childRegionCount++;
                }
            }
            if (childRegionCount == chosenChildCount) {
                viewHolder.checkImage.setImageResource(R.drawable.ic_checked_sq);
            } else {
                viewHolder.checkImage.setImageResource(R.drawable.ic_check_not_deep);
            }
        } else {
            if (chosenRegions.contains(region.id)){
                viewHolder.checkImage.setImageResource(R.drawable.ic_checked_sq);
            } else {
                viewHolder.checkImage.setImageResource(R.drawable.ic_check_not_deep);
            }
        }

        return convertView;
    }



    static class ViewHolder {
        @BindView(R.id.itemAreaChild_tvCityTitle)   TextView cityName;
        @BindView(R.id.itemAreaChild_ivArrow)       ImageView arrowImage;
        @BindView(R.id.itemAreaChild_ivCheck)       ImageView checkImage;
        @BindView(R.id.itemAreaChild_vbtnArrow)     LinearLayout btnArrow;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}