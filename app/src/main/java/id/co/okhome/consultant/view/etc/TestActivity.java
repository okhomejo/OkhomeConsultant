package id.co.okhome.consultant.view.etc;

import android.os.Bundle;

import id.co.okhome.consultant.R;
import id.co.okhome.consultant.lib.app.OkHomeParentActivity;

public class TestActivity extends OkHomeParentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

//        new CommonListDialog(this)
//                .setTitle("HELLO").setArrItems(new String[]{"Number 1", "Bumbu", "Bamsoo2"}).setItemClickListener(new StringHolder.ItemClickListener() {
//            @Override
//            public void onItemClick(int pos, String item) {
//
//            }
//        }).show();



    }
}
