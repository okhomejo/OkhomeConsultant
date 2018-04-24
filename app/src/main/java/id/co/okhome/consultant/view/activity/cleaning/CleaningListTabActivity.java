package id.co.okhome.consultant.view.activity.cleaning;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.okhome.consultant.R;
import id.co.okhome.consultant.lib.app.OkHomeParentActivity;
import id.co.okhome.consultant.lib.app.OkhomeUtil;
import id.co.okhome.consultant.lib.fragment_pager.FragmentTabAdapter;
import id.co.okhome.consultant.view.fragment.CleaningListFragment;

public class CleaningListTabActivity extends OkHomeParentActivity {

    @BindView(R.id.actCleanings_vp)          ViewPager vp;
    @BindView(R.id.actCleanings_tvTab1)      TextView tvTab1;
    @BindView(R.id.actCleanings_tvTab2)      TextView tvTab2;
    @BindView(R.id.actCleanings_vTabLine1)   View vTabLine1;
    @BindView(R.id.actCleanings_vTabLine2)   View vTabLine2;

    CleaningTabAdapter tabAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cleanings);
        ButterKnife.bind(this);

        OkhomeUtil.setWhiteSystembar(this);

        init();
    }

    private void init(){
        setTab();
    }

    private void setTab(){
        tabAdapter = new CleaningTabAdapter(getSupportFragmentManager());
        tabAdapter.init(vp);
    }

    @OnClick(R.id.actCleanings_vbtnX)
    public void onButtonGoBack() {
        finish();
    }

    //tab adapter
    public class CleaningTabAdapter extends FragmentTabAdapter implements ViewPager.OnPageChangeListener{

        public CleaningTabAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment f = null;
            switch(position){
                case 0:
                    f = new CleaningListFragment();
                    f.setArguments(OkhomeUtil.makeBundle("TYPE", "NEXT"));
                    break;
                case 1:
                    f = new CleaningListFragment();
                    f.setArguments(OkhomeUtil.makeBundle("TYPE", "PREVIOUS"));
                    break;
            }
            return f;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public void onPageSelected(int position) {
            super.onPageSelected(position);

            tvTab1.setAlpha(0.4f);
            tvTab2.setAlpha(0.4f);
            vTabLine1.setVisibility(View.INVISIBLE);
            vTabLine2.setVisibility(View.INVISIBLE);

            switch (position){
                case 0:
                    tvTab1.setAlpha(1f);
                    vTabLine1.setVisibility(View.VISIBLE);

                    break;
                case 1:
                    tvTab2.setAlpha(1f);
                    vTabLine2.setVisibility(View.VISIBLE);

                    break;
            }


        }
    }
}
