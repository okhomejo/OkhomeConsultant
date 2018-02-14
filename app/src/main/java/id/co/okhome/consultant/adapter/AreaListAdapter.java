package id.co.okhome.consultant.adapter;

/**
 * Created by frizurd on 12/02/2018.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.okhome.consultant.R;
import id.co.okhome.consultant.model.WorkingRegionModel;

public class AreaListAdapter extends BaseAdapter {
    private Context context;
    private List<WorkingRegionModel> regions;

    public AreaListAdapter(Context context, List<WorkingRegionModel> items) {
        this.context = context;
        this.regions = items;
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
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_area_city_title, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        WorkingRegionModel region = (WorkingRegionModel) getItem(position);
        viewHolder.cityName.setText(region.address);
        viewHolder.selectedAreas.setText("No area selected");

        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.itemArea_tvCityTitle)       TextView cityName;
        @BindView(R.id.itemArea_tvSelectedAmt)     TextView selectedAreas;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}