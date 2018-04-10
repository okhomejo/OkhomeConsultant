package id.co.okhome.consultant.view.fragment.trainee_tab;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.okhome.consultant.R;
import id.co.okhome.consultant.lib.app.ConsultantLoggedIn;
import id.co.okhome.consultant.lib.fragment_pager.TabFragmentStatusListener;
import id.co.okhome.consultant.model.v2.AccountModel;
import id.co.okhome.consultant.view.activity.account.AccountSettingsActivity;
import id.co.okhome.consultant.view.activity.faq.FaqActivity;
import id.co.okhome.consultant.view.activity.news.NewsActivity;
import id.co.okhome.consultant.view.activity.account.profile.FillupUserInfoActivity;
import id.co.okhome.consultant.view.activity.etc.AboutOkhomeActivity;

/**
 * Created by jo on 2018-01-23.
 */

public class SettingTabFragment extends Fragment implements TabFragmentStatusListener {

    @BindView(R.id.fragTabSettingForTrainee_tvAccountEmail)     TextView tvEmail;
    @BindView(R.id.fragTabSettingForTrainee_tvProfileInfo)      TextView tvPersonInfo;
    @BindView(R.id.fragTabSettingForTrainee_ivProfileImage)     ImageView ivProfileImage;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab_setting_f_trainee, null);
        ButterKnife.bind(this, view);
        return view;
    }

    private void init() {
        AccountModel account = ConsultantLoggedIn.get();
        String accountType = "", gender = "";
        if (account.type.equals("T")) {
            accountType = "Trainee";
        } else if (account.type.equals("C")) {
            accountType = "Consultant";
        }
        if (account.profile.gender.equals("M")) {
            gender = "Male";
        } else if (account.profile.gender.equals("F")) {
            gender = "Female";
        }
        String personInfo = account.profile.name + ", " + accountType + ", " + gender;
        tvEmail.setText(account.email);
        tvPersonInfo.setText(personInfo);
        Glide.with(this).load(account.profile.photoUrl).thumbnail(0.5f).into(ivProfileImage);
    }

    @Override
    public void onStart() {
        super.onStart();
        ButterKnife.bind(this, getView());
    }

    @Override
    public void onSelect() {

    }

    @Override
    public void onDeselect() {

    }

    @Override
    public void onResume() {
        super.onResume();
        init();
    }

    @OnClick(R.id.fragTabSettingsForTrainee_vbtnProfile)
    public void onClickProfile() {
        startActivity(new Intent(getActivity(), FillupUserInfoActivity.class));
    }

    @OnClick(R.id.fragTabSettingsForTrainee_vbtnNews)
    public void onClickNews() {
        startActivity(new Intent(getActivity(), NewsActivity.class));
    }

    @OnClick(R.id.fragTabSettingForTrainee_vbtnAbout)
    public void onAboutClick(){
        startActivity(new Intent(getActivity(), AboutOkhomeActivity.class));
    }

    @OnClick(R.id.fragTabSettingsForTrainee_vbtnFaqs)
    public void onFaqClick(){
        FaqActivity.startFaqActivity(getActivity(), "Faq", "FAQ_TRAINEE", 0);
//        startActivity(new Intent(getActivity(), FaqActivity.class).putExtra("FAQ_CATEGORY", "FAQ_TRAINEE"));
    }

    @OnClick(R.id.fragTabSettingForTrainee_vbtnAccount)
    public void onAccountClick(){
        startActivity(new Intent(getActivity(), AccountSettingsActivity.class));
    }
}
