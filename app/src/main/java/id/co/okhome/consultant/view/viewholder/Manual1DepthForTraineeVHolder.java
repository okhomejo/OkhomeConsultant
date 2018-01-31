package id.co.okhome.consultant.view.viewholder;

import android.view.View;
import android.widget.TextView;

import com.mrjodev.jorecyclermanager.JoViewHolder;
import com.mrjodev.jorecyclermanager.annotations.LayoutMatcher;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.okhome.consultant.R;

/**
 * Created by josongmin on 2016-06-28.
 */

@LayoutMatcher(layoutId = R.layout.item_manual_1depth_f_trainee)
public class Manual1DepthForTraineeVHolder extends JoViewHolder<String> {

    @BindView(R.id.itemManual1DepthForTrainee_tvTitle)
    TextView tvTitle;

    public Manual1DepthForTraineeVHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void onViewCreated() {
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void onBind(String m, int pos, int absPos) {
        super.onBind(m, pos, absPos);

        tvTitle.setText(m);
    }

}
