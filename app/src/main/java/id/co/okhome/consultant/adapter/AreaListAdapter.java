package id.co.okhome.consultant.adapter;

/**
 * Created by frizurd on 12/02/2018.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.okhome.consultant.R;
import id.co.okhome.consultant.lib.ViewHolderUtil;
import id.co.okhome.consultant.model.WorkingRegionModel;

public class AreaListAdapter extends BaseAdapter {
    private Context context;
    private List<WorkingRegionModel> parentRegions;
    private List<WorkingRegionModel> allRegions;
    private Set<Integer> chosenRegions;

    public AreaListAdapter(Context context, List<WorkingRegionModel> items, Set<Integer> chosenRegions, List<WorkingRegionModel> allRegions) {
        this.context = context;
        this.parentRegions = items;
        this.chosenRegions = chosenRegions;
        this.allRegions = allRegions;
    }

    @Override
    public int getCount() {
        return parentRegions.size(); //returns total item in the list
    }

    @Override
    public WorkingRegionModel getItem(int position) {
        return parentRegions.get(position); //returns the item at the specified position
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_area_city_title, parent, false);
        }

        TextView cityName        = ViewHolderUtil.getView(convertView, R.id.itemArea_tvCityTitle);
        TextView selectedAreas   = ViewHolderUtil.getView(convertView, R.id.itemArea_tvSelectedAmt);

        WorkingRegionModel region =  getItem(position);
        cityName.setText(region.address);

        int selectedAreasNum = 0;
        for (WorkingRegionModel childRegion : allRegions) {
            if (childRegion.parentId == region.id) {
                for (WorkingRegionModel thirdRegion : allRegions) {
                    if (thirdRegion.parentId == childRegion.id && chosenRegions.contains(thirdRegion.id)) {
                        selectedAreasNum++;
                    }
                }
            }
        }
        if (selectedAreasNum == 0) {
            selectedAreas.setText("No areas selected");
        } else if (selectedAreasNum == 1) {
            selectedAreas.setText("1 area selected");
        } else if (selectedAreasNum > 1) {
            selectedAreas.setText(selectedAreasNum + " areas selected");
        }
        return convertView;
    }
}