package id.co.okhome.consultant.view.activity.salary;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.okhome.consultant.R;
import id.co.okhome.consultant.lib.app.ConsultantLoggedIn;
import id.co.okhome.consultant.lib.app.OkHomeParentActivity;
import id.co.okhome.consultant.lib.app.OkhomeUtil;
import id.co.okhome.consultant.lib.dokuwallet.DokuWallet;
import id.co.okhome.consultant.lib.fragment_pager.FragmentTabAdapter;
import id.co.okhome.consultant.lib.retrofit.RetrofitCallback;
import id.co.okhome.consultant.model.v2.AccountModel;
import id.co.okhome.consultant.model.wallet.ActivitiesModel;
import id.co.okhome.consultant.model.wallet.MutationModel;
import id.co.okhome.consultant.model.wallet.TokenModel;
import id.co.okhome.consultant.rest_apicall.retrofit_restapi.OkhomeRestApi;
import id.co.okhome.consultant.view.fragment.ConsultantSalaryListFragment;

public class ConsultantSalaryListActivity extends OkHomeParentActivity {

    @BindView(R.id.actTrainingInfo_vLoading)        ProgressBar progressBar;
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
        loadMutations(
                ConsultantLoggedIn.get().consultant.dokuToken,
                ConsultantLoggedIn.get().consultant.dokuSystrace,
                ConsultantLoggedIn.get().consultant.dokuId
        );
    }

    private void loadMutations(String token, String systrace, String accountId) {
        DokuWallet.getActivities(token, systrace, accountId).enqueue(new RetrofitCallback<ActivitiesModel>() {
            @Override
            public void onSuccess(ActivitiesModel result) {
                if (!tokenRetrieved(result.responseCode)) {
                    refreshToken(OkhomeUtil.getRandomString());
                } else if (result.mutasi != null) {
                    salaryTabAdapter = new SalaryTabAdapter(getSupportFragmentManager(), result.mutasi);
                    salaryTabAdapter.init(vp);

                    progressBar.setVisibility(View.INVISIBLE);
                } else {
                    System.out.println("Error " + result.responseCode + ": " + result.responseMessage);
                }
                System.out.println("Result: " + result.responseCode + " / " + result.responseMessage);
            }
        });
    }

    private void refreshToken(final String systrace) {
        DokuWallet.signOn(systrace).enqueue(new RetrofitCallback<TokenModel>() {
            @Override
            public void onSuccess(TokenModel result) {
                if (!tokenRetrieved(result.responseCode)) {
                    refreshToken(OkhomeUtil.getRandomString());
                } else {
                    saveTokenAndSystrace(result.accessToken, systrace);
                }
            }
        });
    }

    private void saveTokenAndSystrace(final String token, final String systrace) {
        String jsonParam = new Gson().toJson(OkhomeUtil.makeMap("dokuToken", token, "dokuSystrace", systrace));
        OkhomeRestApi.getConsultantClient().update(ConsultantLoggedIn.get().id, jsonParam)
                .enqueue(new RetrofitCallback<String>() {
                    @Override
                    public void onSuccess(String account) {
                        ConsultantLoggedIn.reload(new RetrofitCallback<AccountModel>() {
                            @Override
                            public void onSuccess(AccountModel result) {
                                loadMutations(
                                        result.consultant.dokuToken,
                                        result.consultant.dokuSystrace,
                                        result.consultant.dokuId
                                );
                            }
                        });
                    }
                });
    }

    private boolean tokenRetrieved(String code) {
        return !code.equals("3011") && !code.equals("3010") && !code.equals("3009");
    }

    @OnClick(R.id.actTrainingInfo_vbtnX)
    public void onButtonGoBack() {
        finish();
    }

    @OnClick(R.id.actConsultantSalary_vbTab1)
    public void onCompletedSalary() {
        vp.setCurrentItem(0, true);
    }

    @OnClick(R.id.actConsultantSalary_vbTab2)
    public void onDepositSalary() {
        vp.setCurrentItem(1, true);
    }

    //tab adapter
    public class SalaryTabAdapter extends FragmentTabAdapter implements ViewPager.OnPageChangeListener{

        private List<MutationModel> mutationList;

        public SalaryTabAdapter(FragmentManager fm, List<MutationModel> mutationList) {
            super(fm);
            this.mutationList = mutationList;
        }

        @Override
        public Fragment getItem(int position) {
            Fragment f = null;
            Bundle b;

            switch (position) {
                case 0:
                    b = OkhomeUtil.makeBundle("TYPE", "COMPLETE");
                    b.putParcelableArrayList("MUTATION_LIST", (ArrayList<MutationModel>) mutationList);

                    f = new ConsultantSalaryListFragment();
                    f.setArguments(b);
                    break;
                case 1:
                    b = OkhomeUtil.makeBundle("TYPE", "WAITING");
                    b.putParcelableArrayList("MUTATION_LIST", (ArrayList<MutationModel>) mutationList);

                    f = new ConsultantSalaryListFragment();
                    f.setArguments(b);
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
