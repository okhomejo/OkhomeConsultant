package id.co.okhome.consultant.view.salary;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.okhome.consultant.R;
import id.co.okhome.consultant.lib.app.OkHomeParentActivity;
import id.co.okhome.consultant.lib.app.OkhomeUtil;
import id.co.okhome.consultant.lib.fragment_pager.FragmentTabAdapter;

public class ConsultantSalaryListActivity extends OkHomeParentActivity {

    @BindView(R.id.actConsultantSalary_vp)          ViewPager vp;
    @BindView(R.id.actConsultantSalary_tvTab1)      TextView tvTab1;
    @BindView(R.id.actConsultantSalary_tvTab2)      TextView tvTab2;
    @BindView(R.id.actConsultantSalary_vTabLine1)   View vTabLine1;
    @BindView(R.id.actConsultantSalary_vTabLine2)   View vTabLine2;

    SalaryTabAdapter salaryTabAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultant_salary);
        ButterKnife.bind(this);

        OkhomeUtil.setWhiteSystembar(this);

        init();
    }

    private void init(){
        setTab();
    }

    private void setTab(){
        salaryTabAdapter = new SalaryTabAdapter(getSupportFragmentManager());
        salaryTabAdapter.init(vp);
    }

    //tab adapter
    public class SalaryTabAdapter extends FragmentTabAdapter implements ViewPager.OnPageChangeListener{

        public SalaryTabAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment f = null;
            switch(position){
                case 0:
                    f = new ConsultantSalaryListFragment();
                    f.setArguments(OkhomeUtil.makeBundle("TYPE", "COMPLETE"));
                    break;
                case 1:
                    f = new ConsultantSalaryListFragment();
                    f.setArguments(OkhomeUtil.makeBundle("TYPE", "WAITING"));
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
