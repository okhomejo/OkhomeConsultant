package id.co.okhome.consultant.view.activity.account;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.okhome.consultant.R;
import id.co.okhome.consultant.lib.app.ConsultantLoggedIn;
import id.co.okhome.consultant.lib.app.OkHomeParentActivity;
import id.co.okhome.consultant.lib.app.OkhomeUtil;
import id.co.okhome.consultant.model.v2.AccountModel;

/**
 * Created by frizurd on 12/03/2018.
 */

public class BlockedActivity extends OkHomeParentActivity {

    @BindView(R.id.actBlocked_tvHeader)      TextView tvHeader;
    @BindView(R.id.actBlocked_tvDate)        TextView tvDate;
    @BindView(R.id.actBlocked_tvReason)      TextView tvReason;

    private AccountModel account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blocked);
        OkhomeUtil.setSystemBarColor(this, ContextCompat.getColor(this, R.color.colorOkhome));

        ButterKnife.bind(this);
        init();
    }

    private void init() {
        if (ConsultantLoggedIn.hasSavedData()) {
            account = ConsultantLoggedIn.get();
            if (account.blocked != null) {
                tvReason.setText(account.blocked.toString());
                tvDate.setText(account.blocked.get(0).toString());
            }
        }
    }

    @OnClick(R.id.actBlocked_vbtnX)
    public void closeActivity() {
        finishAllActivities();
        ConsultantLoggedIn.clear();
        startActivity(new Intent(this, AuthActivity.class));
    }

    @OnClick(R.id.actBlocked_btnContact)
    public void contactOkhome() {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", "ask@okhome.id", null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "My account got blocked!");
        this.startActivity(Intent.createChooser(emailIntent, null));
    }
}