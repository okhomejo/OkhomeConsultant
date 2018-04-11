package id.co.okhome.consultant.view.fragment.trainee_tab;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.joda.time.DateTime;
import org.joda.time.Years;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.okhome.consultant.R;
import id.co.okhome.consultant.lib.app.ConsultantLoggedIn;
import id.co.okhome.consultant.lib.app.OkhomeUtil;
import id.co.okhome.consultant.lib.fragment_pager.TabFragmentStatusListener;
import id.co.okhome.consultant.lib.retrofit.RetrofitCallback;
import id.co.okhome.consultant.model.TraineePageHomeModel;
import id.co.okhome.consultant.model.training.TrainingModel;
import id.co.okhome.consultant.rest_apicall.retrofit_restapi.OkhomeRestApi;

/**
 * Created by jo on 2018-01-23.
 */

public class HomeTabFragment extends Fragment implements TabFragmentStatusListener {

    @BindView(R.id.fragTabHomeForTrainee_vLoading)                  View vLoading;
    @BindView(R.id.fragTabHomeForTrainee_svItem)                    ScrollView svItem;

    @BindView(R.id.fragTabHomeForTrainee_tvName)                    TextView tvName;
    @BindView(R.id.fragTabHomeForTrainee_tvSubName)                 TextView tvSubName;
    @BindView(R.id.fragTabHomeForTrainee_ivPhoto)                   ImageView ivPhoto;

    @BindView(R.id.fragTabHomeForTrainee_tvTrainingTitle)           TextView tvTrainingTitle;
    @BindView(R.id.fragTabHomeForTrainee_tvTrainingDesc)            TextView tvTrainingDesc;
    @BindView(R.id.fragTabHomeForTrainee_tvTrainingTime)            TextView tvTrainingTime;
    @BindView(R.id.fragTabHomeForTrainee_tvAdvancedAmt)             TextView tvAdvancedAmt;
    @BindView(R.id.fragTabHomeForTrainee_tvBasicAmt)                TextView tvBasicAmt;

    @BindView(R.id.fragTabHomeForTrainee_vgTrainingProgressGraph)   ViewGroup vgTrainingProgressGraph;

    private HomeTabTrainingProgressManager progressManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        progressManager = new HomeTabTrainingProgressManager();
        return inflater.inflate(R.layout.fragment_tab_home_f_trainee, null);
    }

    @Override
    public void onResume() {
        super.onResume();
        getTraineeHomeInfo();
    }

    @Override
    public void onStart() {
        super.onStart();
        ButterKnife.bind(this, getView());
        svItem.setVisibility(View.GONE);
        progressManager.start();
    }

    @Override
    public void onSelect() {
        View v = null;
    }

    @Override
    public void onDeselect() {
        OkhomeUtil.Log(this.getClass().toString() + " onDeselect ");
    }

    @Override
    public void onPause() {
        super.onPause();
        OkhomeUtil.Log(this.getClass().toString() + " onPause ");
        progressManager.pause();
    }

    //match view and data
    private void adaptViews(TraineePageHomeModel traineePageHome) {
        // Update user information
        tvName.setText(traineePageHome.name);
        String accountType = "", gender = "", birthday = "";
        if (traineePageHome.type.equals("T")) {
            accountType = "Trainee";
        } else if (traineePageHome.type.equals("C")) {
            accountType = "Consultant";
        }
        if (traineePageHome.gender.equals("M")) {
            gender = "Male";
        } else if (traineePageHome.gender.equals("F")) {
            gender = "Female";
        }
        if (!TextUtils.isEmpty(traineePageHome.birthdate)) {
            DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd");
            DateTime birthDate = dtf.parseDateTime(traineePageHome.birthdate);
            DateTime today = DateTime.now();

            Years age = Years.yearsBetween(birthDate, today);
            birthday = String.valueOf(age.getYears());
        }

        tvSubName.setText(String.format("%s, %s, %s", accountType, birthday, gender));
        Glide.with(this).load(traineePageHome.photoUrl).thumbnail(0.5f).into(ivPhoto);

        // Update next job training
        adaptNextJobTraining(traineePageHome.trainingEarliest);

        // Update training progress
        progressManager.initDataAndViews(traineePageHome, vgTrainingProgressGraph);
    }

    private void adaptNextJobTraining(TrainingModel trainingEarliest) {
        tvTrainingTitle.setText(trainingEarliest.subject);
        tvTrainingDesc.setText(trainingEarliest.desc);

        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        DateTime dt = formatter.parseDateTime(trainingEarliest.trainingAttendanceForTrainee.trainingWhen);
        tvTrainingTime.setText(dt.toString("dd/MM/yyyy, hh:mm"));
    }

    //get trainee home info
    private void getTraineeHomeInfo() {
        vLoading.setVisibility(View.VISIBLE);
        OkhomeRestApi.getTraineeClient().getTraineePageHome(ConsultantLoggedIn.id()).enqueue(new RetrofitCallback<TraineePageHomeModel>() {
            @Override
            public void onSuccess(TraineePageHomeModel traineePageHome) {
                adaptViews(traineePageHome);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                vLoading.setVisibility(View.GONE);
                svItem.setVisibility(View.VISIBLE);
            }
        });
    }


    public class HomeTabTrainingProgressManager {

        private View activeBar;
        private boolean animationActive = false, progressDisplayed = false;

        public void start() {
            if (activeBar != null) {
                blinkAnimation(activeBar);
            }
        }

        public void pause() {
            stopAnimation(activeBar);
        }

        public void initDataAndViews(TraineePageHomeModel traineePageHome, ViewGroup view) {
            // Update training progress
            if (!progressDisplayed) {
                int bProgressCnt = traineePageHome.basicTrainingProgressCount,
                        bCnt = traineePageHome.basicTrainingCount,
                        aProgressCnt = traineePageHome.ojtTrainingProgressCount,
                        aCnt = traineePageHome.ojtTrainingCount,
                        activeBasic = -1, activeAdvanced = -1;

                tvBasicAmt.setText(String.format(Locale.ENGLISH, "%d/%d", bProgressCnt, bCnt));
                tvAdvancedAmt.setText(String.format(Locale.ENGLISH, "%d/%d", aProgressCnt, aCnt));

                if (traineePageHome.onGoingTrainingType.equals("BASIC")) {
                    activeBasic = traineePageHome.onGoingTrainingPos;
                } else if (traineePageHome.onGoingTrainingType.equals("ADVANCED")) {
                    activeAdvanced = traineePageHome.onGoingTrainingPos;
                }

                adaptTrainingProgressBar(
                        bProgressCnt,
                        bCnt,
                        activeBasic,
                        (ViewGroup) view.findViewById(R.id.fragTabHomeForTrainee_vgBasicTraining)
                );
                adaptTrainingProgressBar(
                        aProgressCnt,
                        aCnt,
                        activeAdvanced,
                        (ViewGroup) view.findViewById(R.id.fragTabHomeForTrainee_vgAdvancedTraining)
                );
                progressDisplayed = true;
            }
        }

        private void adaptTrainingProgressBar(int finishedAmt, int maxAmt, final int active, ViewGroup row) {
            row.removeAllViews();
            final List<View> viewList = new ArrayList<>();
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                    0, LinearLayout.LayoutParams.MATCH_PARENT, 1
            );
            for (int i = 0; i <= maxAmt; i++) {
                View bar = new View(getContext());
//                bar.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.graphbg_traning_off));
                viewList.add(bar);

                if (i != (maxAmt-1)) {
                    param.setMarginEnd(5);
                }
                bar.setLayoutParams(param);
                row.addView(bar);
            }

            int counter = finishedAmt;
            for (final View bar : viewList) {
                if (counter > 0) {
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            bar.setScaleX(0);
                            bar.setPivotX(0);
                            bar.setBackgroundColor(Color.parseColor("#16acf2"));
                            bar.animate().scaleX(1.0f).setDuration(600).start();

                            if (viewList.indexOf(bar) == active) {
                                activeBar = bar;
                                blinkAnimation(activeBar);
                            }
                        }
                    }, 600 * (viewList.indexOf(bar))+1);
                    counter--;
                } else {
                    break;
                }
            }
        }

        private void blinkAnimation(final View v) {
            if (!animationActive) {
                Animation anim = new AlphaAnimation(0.2f, 1f);
                anim.setDuration(600);
                anim.setStartOffset(20);
                anim.setRepeatMode(Animation.REVERSE);
                anim.setRepeatCount(Animation.INFINITE);
                v.startAnimation(anim);
                animationActive = true;
            }
        }

        private void stopAnimation(final View v) {
            if (animationActive) {
                v.clearAnimation();
                animationActive = false;
            }
        }
    }
}
