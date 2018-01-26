package id.co.okhome.consultant.view.userinfo.trainee;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.okhome.consultant.R;
import id.co.okhome.consultant.lib.OkHomeParentActivity;
import id.co.okhome.consultant.lib.OkhomeUtil;
import id.co.okhome.consultant.view.etc.LocationActivity;

public class UpdateUserDocumentActivity extends OkHomeParentActivity {

    @BindView(R.id.actUpdateUserDocument_tvAddress)      TextView tvAddress;

    private String address;
    private Bundle previousBundle = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user_document);
        OkhomeUtil.setSystemBarColor(this,

//                Color.parseColor("#29313a"));
        ContextCompat.getColor(this, R.color.colorOkhome));

        ButterKnife.bind(this);
    }

    @OnClick(R.id.actUpdateUserDocument_vgbtnAddress)
    public void onAddressClick(View v){
        if (previousBundle != null) {
            Intent editAddressActivity = new Intent(this, LocationActivity.class);
            editAddressActivity.putExtras(previousBundle);
            startActivityForResult(editAddressActivity, 1);
        } else {
            startActivityForResult(new Intent(this, LocationActivity.class), 1);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                if(data.getExtras() == null) {
                    address = "No address";
                } else {
                    address = data.getStringExtra("address");
//                    latitude = data.getDoubleExtra("latitude",0);
//                    longitude = data.getDoubleExtra("longitude", 0);
                    previousBundle = data.getExtras();
                }
                tvAddress.setText(address);
            }
        }
    }
}
