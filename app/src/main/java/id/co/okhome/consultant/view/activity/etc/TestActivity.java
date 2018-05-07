package id.co.okhome.consultant.view.activity.etc;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import id.co.okhome.consultant.R;
import id.co.okhome.consultant.lib.app.OkHomeParentActivity;
import id.co.okhome.consultant.lib.app.OkhomeUtil;

public class TestActivity extends OkHomeParentActivity{

    @BindView(R.id.actTest_rcv)
    RecyclerView rcv;

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        mAuth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser == null){
            updateUI(null);
            signup();
        }else{
            signin();
        }

    }

    private void updateUI(FirebaseUser user){
        if(user == null){
        }else{

            OkhomeUtil.Log(TestActivity.this, user);

            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    OkhomeUtil.Log(TestActivity.this, "Email sent");
                }
            });
        }

    }

    private void signup(){
        mAuth.createUserWithEmailAndPassword("944899@gmail.com", "whthdals")
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            OkhomeUtil.Log(TestActivity.this, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            OkhomeUtil.Log(TestActivity.this, "createUserWithEmail:failure " +  task.getException());
                            updateUI(null);
                        }
                    }
                });
    }

    private void signin(){
        mAuth.signInWithEmailAndPassword("944899@gmail.com", "whthdals")
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            OkhomeUtil.Log(TestActivity.this, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            OkhomeUtil.Log(TestActivity.this, "signInWithEmail:failure");
                            Toast.makeText(TestActivity.this, "Authenticatison failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


}
