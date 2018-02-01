package id.co.okhome.consultant.view.userinfo.trainee;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.okhome.consultant.R;
import id.co.okhome.consultant.lib.app.OkHomeParentActivity;
import id.co.okhome.consultant.lib.app.OkhomeUtil;
import id.co.okhome.consultant.view.common.dialog.CommonListDialog;
import id.co.okhome.consultant.view.viewholder.StringHolder;

public class UpdateExtraUserDocumentActivity extends OkHomeParentActivity {

    @BindView(R.id.actUpdateUserExtraDoc_etNIK)         EditText etNIK;
    @BindView(R.id.actUpdateUserExtraDoc_tvMarried)     TextView tvMarried;
    @BindView(R.id.actUpdateUserExtraDoc_tvReligion)    TextView tvReligion;



    String marriedYN, nik, religion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user_extra_document);
        ButterKnife.bind(this);
        OkhomeUtil.setSystemBarColor(this,

//                Color.parseColor("#29313a"));
                ContextCompat.getColor(this, R.color.colorOkhome));

        init();
    }

    private void init(){
        ;
    }


    //-- onclick mothods---------------------------------
    @OnClick(R.id.actUpdateUserExtraDoc_vgMarried)
    public void onClickMarried(){

        new CommonListDialog(this)
                .setTitle("Married?")
                .setArrItems("Married", "Not yet")
                .setArrItemTag("Y", "N")
                .setItemClickListener(new StringHolder.ItemClickListener() {
                    @Override
                    public void onItemClick(Dialog dialog, int pos, String value, String tag) {
                        dialog.dismiss();
                        tvMarried.setText(value);
                        marriedYN = tag;
                    }
                })
                .show();
    }


    @OnClick(R.id.actUpdateUserExtraDoc_vgReligion)
    public void onClickReligion(){

    }
}
