package id.co.okhome.consultant.view.viewholder.prefered_area;

import android.view.View;
import android.widget.TextView;
import com.mrjodev.jorecyclermanager.JoViewHolder;
import com.mrjodev.jorecyclermanager.annotations.LayoutMatcher;

import java.util.List;
import java.util.Set;
import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.okhome.consultant.R;
import id.co.okhome.consultant.model.WorkingRegionModel;
import id.co.okhome.consultant.view.activity.account.profile.UpdateConsultantAreaActivity;

/**
 * Created by frizurd on 28/03/2018.
 */

@LayoutMatcher(layoutId = R.layout.item_area_city_title)
public class PreferredAreaVHolder extends JoViewHolder<WorkingRegionModel> implements View.OnClickListener {

    @BindView(R.id.itemArea_tvCityTitle)    TextView tvTitle;
    @BindView(R.id.itemArea_tvSelectedAmt)  TextView tvSelection;

    private Set<Integer> chosenRegions;
    private List<WorkingRegionModel> allRegions;

    public PreferredAreaVHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void onViewCreated() {
        ButterKnife.bind(this, getView());
    }

    @Override
    public void onBind(WorkingRegionModel region, int pos, int absPos) {
        super.onBind(region, pos, absPos);

        chosenRegions               = getAdapter().getParams().getParam("chosenRegions");
        allRegions                  = getAdapter().getParams().getParam("allRegions");

        tvTitle.setText(region.address);

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
            tvSelection.setText("0");
        } else if (selectedAreasNum == 1) {
            tvSelection.setText("1");
        } else if (selectedAreasNum > 1) {
            tvSelection.setText(selectedAreasNum + "");
        }

        getView().setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        UpdateConsultantAreaActivity areaActivity = (UpdateConsultantAreaActivity) getContext();
        areaActivity.callChildRegionDialog(getModel());

//        final List<WorkingRegionModel> childRegionList = new ArrayList<>();
//        for (WorkingRegionModel childRegion : allRegions) {
//            if (getModel().id == childRegion.parentId) {
//                childRegionList.add(childRegion);
//            }
//        }
//        final AreaListDialog areaDialog = new AreaListDialog(getContext());
//        areaDialog.setTitle(getModel().address)
//                .setListItems(childRegionList)
//                .setAllRegionItems(allRegions)
//                .setChosenRegions(chosenRegions)
//                .show();
    }
}