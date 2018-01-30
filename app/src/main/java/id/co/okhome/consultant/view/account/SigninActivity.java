package id.co.okhome.consultant.view.account;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.okhome.consultant.R;
import id.co.okhome.consultant.exception.OkhomeException;
import id.co.okhome.consultant.lib.OkHomeParentActivity;
import id.co.okhome.consultant.lib.OkhomeUtil;
import id.co.okhome.consultant.model.ConsultantModel;
import id.co.okhome.consultant.rest_apicall.retrofit_restapi.AccountClient;
import id.co.okhome.consultant.rest_apicall.retrofit_restapi.OkhomeRestApi;
import id.co.okhome.consultant.view.main.trainee.TraineeMainActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SigninActivity extends OkHomeParentActivity {

    @BindView(R.id.actSignIn_etEmail)           EditText etEmail;
    @BindView(R.id.actSignIn_etPassword)        EditText etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        ButterKnife.bind(this);
    }

    private void loginProcessWithRetrofit(final String email, String password){

        AccountClient mApiService = OkhomeRestApi.getAccountClient();

        Call<ConsultantModel> mService = mApiService.signin(email, password);
        mService.enqueue(new Callback<ConsultantModel>() {
            @Override
            public void onResponse(Call<ConsultantModel> call, Response<ConsultantModel> response) {

                if(response.isSuccessful()){

                    int consultantId = Integer.parseInt(response.body().id);

                    Toast.makeText(SigninActivity.this, "Login Success", Toast.LENGTH_SHORT).show();

                    Intent loginIntent = new Intent(SigninActivity.this, TraineeMainActivity.class);
                    loginIntent.putExtra("EMAIL", email);
                    loginIntent.putExtra("ID", consultantId);
                    startActivity(loginIntent);
                } else {

                    Toast.makeText(SigninActivity.this, "Login fail", Toast.LENGTH_SHORT).show();

                    etPassword.setText("");

                    Log.e("Error Code", String.valueOf(response.code()));
                    Log.e("Error Body", response.errorBody().toString());
                }
            }
            @Override
            public void onFailure(Call<ConsultantModel> call, Throwable t) {
                call.cancel();
                Toast.makeText(SigninActivity.this, "Please check your network connection and internet permission", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void checkBeforeSignup(){

        final String email = etEmail.getText().toString();
        final String password = etPassword.getText().toString();

//        try {
//            OkhomeUtil.chkException(!OkhomeUtil.isValidEmail(email), "Check your email.");
//            OkhomeUtil.isValidPassword(password);
//
//        } catch(OkhomeException e) {
//            OkhomeUtil.showToast(this, e.getMessage());
//            return;
//        }

        loginProcessWithRetrofit(email, password);
    }

    @OnClick(R.id.actSignIn_vbtnClose)
    public void onCloseWindow() {
        finish();
    }

    @OnClick(R.id.actSignIn_vbtnSignin)
    public void onSignin(View v){

        checkBeforeSignup();

//        startActivity(new Intent(this, TraineeMainActivity.class));
    }
}
