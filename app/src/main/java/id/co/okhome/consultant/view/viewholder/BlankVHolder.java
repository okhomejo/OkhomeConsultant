package id.co.okhome.consultant.view.viewholder;

import android.view.View;

import com.mrjodev.jorecyclermanager.JoViewHolder;
import com.mrjodev.jorecyclermanager.annotations.LayoutMatcher;

import butterknife.ButterKnife;
import id.co.okhome.consultant.R;

/**
 * Created by josongmin on 2016-06-28.
 */

@LayoutMatcher(layoutId = R.layout.item_padding)
public class BlankVHolder extends JoViewHolder<String> {

    public BlankVHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void onViewCreated() {
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void onBind(String m, int pos, int absPos) {

        super.onBind(m, pos, absPos);
    }

}
