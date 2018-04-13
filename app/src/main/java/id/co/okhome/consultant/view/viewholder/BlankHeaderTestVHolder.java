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

@LayoutMatcher(layoutId = R.layout.item_temp)
public class BlankHeaderTestVHolder extends JoViewHolder<String> {


    @BindView(R.id.itemTemp_tv)
    TextView tvTemp;
    public BlankHeaderTestVHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void onViewCreated() {
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void onBind(String m, int pos, int absPos) {
        super.onBind(m, pos, absPos);
        tvTemp.setText(m);
    }

}
