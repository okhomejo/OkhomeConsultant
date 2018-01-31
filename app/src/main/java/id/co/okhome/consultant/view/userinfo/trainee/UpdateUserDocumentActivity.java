package id.co.okhome.consultant.view.userinfo.trainee;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import butterknife.BindView;
import butterknife.OnClick;
import butterknife.ButterKnife;
import id.co.okhome.consultant.R;
import id.co.okhome.consultant.lib.ToastUtil;
import id.co.okhome.consultant.lib.app.OkHomeParentActivity;
import id.co.okhome.consultant.lib.app.OkhomeUtil;
import id.co.okhome.consultant.lib.retrofit.RetrofitCallback;
import id.co.okhome.consultant.lib.retrofit.restmodel.ErrorModel;
import id.co.okhome.consultant.rest_apicall.retrofit_restapi.OkhomeRestApi;
import id.co.okhome.consultant.view.etc.LocationActivity;

public class UpdateUserDocumentActivity extends OkHomeParentActivity {

    @BindView(R.id.actUpdateUserDocument_tvAddress)      TextView tvAddress;

    private String address;
    private Bundle previousBundle = null;
    private boolean isActive = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user_document);
        OkhomeUtil.setSystemBarColor(this,

//                Color.parseColor("#29313a"));
        ContextCompat.getColor(this, R.color.colorOkhome));

        ButterKnife.bind(this);
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

//                String changes = "{\"gender\":\"F\", \"address\":\"" + address + "\"}";
//                updateProfile("7", changes);
            }
            isActive = false;
        }
    }

    @OnClick(R.id.actUpdateUserDocument_vgbtnAddress)
    public void onAddressClick(View v){
        if (!isActive) {
            if (previousBundle != null) {
                Intent editAddressActivity = new Intent(this, LocationActivity.class);
                editAddressActivity.putExtras(previousBundle);
                startActivityForResult(editAddressActivity, 1);
            } else {
                startActivityForResult(new Intent(this, LocationActivity.class), 1);
            }
        }
        isActive = true;
    }

    private void updateProfile(final String consultantId, String jsonParams) {

        OkhomeRestApi.getAccountClient().updateProfile(consultantId, jsonParams).
                enqueue(new RetrofitCallback<String>() {

            @Override
            public void onSuccess(String result) {
                Toast.makeText(UpdateUserDocumentActivity.this, "Result: " + result, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onJodevError(ErrorModel jodevErrorModel) {
                super.onJodevError(jodevErrorModel);
                ToastUtil.showToast(jodevErrorModel.message);
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }
        });
    }

}
