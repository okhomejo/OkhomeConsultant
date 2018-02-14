package id.co.okhome.consultant.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.okhome.consultant.R;
import id.co.okhome.consultant.lib.app.OkhomeUtil;
import id.co.okhome.consultant.model.WorkingRegionModel;

/**
 * Created by frizurd on 14/02/2018.
 */

public class ChildAreaListAdapter extends BaseAdapter {

    private Context context;
    private List<WorkingRegionModel> regions;
    private Set<Integer> chosenRegions;

    public ChildAreaListAdapter(Context context, List<WorkingRegionModel> items) {
        this.context = context;
        this.regions = items;
        chosenRegions = new HashSet<>();
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
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_area_child_city, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final WorkingRegionModel region = getItem(position);
        viewHolder.cityName.setText(region.address);
        if (region.hasChild || region.childCount > 0) {
            viewHolder.arrowImage.setVisibility(View.VISIBLE);
        } else {
            viewHolder.arrowImage.setVisibility(View.GONE);
        }

        viewHolder.btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (region.childCount > 0) {
                    for (WorkingRegionModel childReg : regions) {
                        if (region.parentId == childReg.id && childReg.childCount == 0) {
                            chosenRegions.add(childReg.id);
                        } else {
                            for (WorkingRegionModel secondChildReg : regions) {
                                if (childReg.parentId == secondChildReg.id && secondChildReg.childCount == 0) {
                                    chosenRegions.add(childReg.id);
                                }
                            }
                        }
                    }
                    viewHolder.checkImage.setImageResource(R.drawable.ic_checked_sq);
                } else {
                    chosenRegions.add(region.id);
                    viewHolder.checkImage.setImageResource(R.drawable.ic_checked_sq);
                }
            }
        });
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.itemAreaChild_tvCityTitle)   TextView cityName;
        @BindView(R.id.itemAreaChild_ivCheck)       ImageView checkImage;
        @BindView(R.id.itemAreaChild_ivArrow)       ImageView arrowImage;
        @BindView(R.id.itemAreaChild_vbtnCheck)     LinearLayout btnCheck;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}