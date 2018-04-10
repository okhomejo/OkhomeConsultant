package id.co.okhome.consultant.lib.app;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.telephony.PhoneNumberUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.lang.reflect.Constructor;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import id.co.okhome.consultant.config.OkhomeConstant;
import id.co.okhome.consultant.exception.OkhomeException;


/**
 * Created by josongmin on 2016-07-29.
 */

public class OkhomeUtil {

    public final static Bundle makeBundle(Object ... params){
        Bundle b = new Bundle();
        for(int i = 0; i < params.length; i +=2){
            String key = (String)params[i];
            Object o = params[i+1];

            if(o instanceof Integer){
                b.putInt(key, (int)o);
            }
            else if(o instanceof String){
                b.putString(key, (String)o);
            }
        }

        return b;
    }

    public final static void initTopPadding(View vTarget){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)vTarget.getLayoutParams();
            layoutParams.topMargin = layoutParams.topMargin - OkhomeUtil.getPixelByDp(vTarget.getContext(), 3);
            vTarget.setLayoutParams(layoutParams);
        }
    }


    public static final boolean isEmptyString(String str){
        if(str == null){
            return true;
        }

        if(str.trim().length() == 0){
            return true;
        }

        return false;
    }

    public static final void Log(Object msg){
        Log.d(OkhomeConstant.LOGTAG, msg.toString());
    }

    public final static void isValidPassword(String password) throws OkhomeException{
        if(password.length() < OkhomeConstant.PASSWORD_MIN_LENGTH){
            throw new OkhomeException(-100,
                    "Password must be more then #{passwordMinLength}".replace("#{passwordMinLength}", OkhomeConstant.PASSWORD_MIN_LENGTH+""));
        }

        if(password.length() > OkhomeConstant.PASSWORD_MAX_LENGTH){
            throw new OkhomeException(-100,
                    "Password must be less then #{passwordMaxLength}".replace("#{passwordMaxLength}", OkhomeConstant.PASSWORD_MAX_LENGTH+""));
        }
    }

    /**check exception*/
    public final static void chkException(boolean expression, String err) throws OkhomeException{
        if(expression){
            throw new OkhomeException(-100, err);
        }

    }

    final public static void setWhiteSystembar(Activity act){
        View decor = act.getWindow().getDecorView();
        decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        OkhomeUtil.setSystemBarColor(act, Color.parseColor("#ffffff"));
    }

    final public static void setSystemBarColor(Activity activity, int color){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(color);
        }
    }

    final public static Message makeHandlerMessage(int what, Object obj){
        Message m = new Message();
        m.obj = obj;
        m.what = what;
        return m;
    }

    public static final long getCurrentJarartaTimeMills(){
        DateTime dt = new DateTime();
        DateTime dt2 = dt.toDateTime(DateTimeZone.forID("Asia/Jakarta"));

        String currentDateTime = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss").print(dt2);
        long mills = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss").parseDateTime(currentDateTime).getMillis();
        return mills;
    }


    public final static DateTimeFormatter forPattern(Context context, String pattern){
        return DateTimeFormat.forPattern(pattern).withLocale(OkhomeUtil.getNowLocale(context));
    }

    public final static void delayedStart(final Runnable runnable, int delay){
        new Handler(){
            @Override
            public void dispatchMessage(Message msg) {
                runnable.run();
            }
        }.sendEmptyMessageDelayed(0, delay);

    }

    public final static String getString(Context context, int resourceId){
        return context.getResources().getString(resourceId);
    }

    public final static String getLocaleCode(Context context){
        Locale locale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            locale = context.getResources().getConfiguration().getLocales().get(0);
        } else {
            locale = context.getResources().getConfiguration().locale;
        }
        return locale.getLanguage();
    }

    public final static Locale getNowLocale(Context context){
        Locale locale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            locale = context.getResources().getConfiguration().getLocales().get(0);
        } else {
            locale = context.getResources().getConfiguration().locale;
        }
        return locale;
    }


    public final static String getTopAppName(Context context) {
        ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        String strName = "";
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                strName = getLollipopFGAppPackageName(context);
            } else {
                strName = mActivityManager.getRunningTasks(1).get(0).topActivity.getClassName();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strName;
    }



    @TargetApi(21)
    private final static String getLollipopFGAppPackageName(Context ctx) {

        try {
            UsageStatsManager usageStatsManager = (UsageStatsManager) ctx.getSystemService(Context.USAGE_STATS_SERVICE);
            long milliSecs = 60 * 1000;
            Date date = new Date();
            List<UsageStats> queryUsageStats = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, date.getTime() - milliSecs, date.getTime());
            if (queryUsageStats.size() > 0) {
                Log.i("LPU", "queryUsageStats size: " + queryUsageStats.size());
            }
            long recentTime = 0;
            String recentPkg = "";
            for (int i = 0; i < queryUsageStats.size(); i++) {
                UsageStats stats = queryUsageStats.get(i);
                if (i == 0 && !"org.pervacio.pvadiag".equals(stats.getPackageName())) {
                    Log.i("LPU", "PackageName: " + stats.getPackageName() + " " + stats.getLastTimeStamp());
                }
                if (stats.getLastTimeStamp() > recentTime) {
                    recentTime = stats.getLastTimeStamp();
                    recentPkg = stats.getPackageName();
                }
            }
            return recentPkg;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    //앱이 백그라운드인지 아닌지
    public final static boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }

        return isInBackground;
    }

    public final static Map getServiceDurationAndPerson(float duration){

        Map<String, Object> mapResult = new HashMap<>();

//        float fAddTime = duration;
//        int person = 1;
//
//        //addTime, 사람 추가 되는거 계산
//        while(fAddTime >= 6){
//            fAddTime = (fAddTime + 1) / 2;
//            person ++;
//        }
//
//        int hour = (int)(fAddTime / 1);
//        float remainder = fAddTime % 1;
//        remainder = Float.parseFloat(String.format("%.1f", remainder));
//        String addHour = "";
//
//        if(remainder <= 0){
//            addHour = hour+"";
//        }else if(remainder <= 0.5f){
//            addHour = hour + ".5";
//        }else{
//            addHour = (hour + 1) + "";
//        }

        int person = (int)((duration + 6) / 6);
        float durationPer = (duration + (person > 1 ?  person * 0.5f : 0)) / person;

        int hour = (int)(durationPer / 1);
        float remainder = durationPer % 1;
        String midFormat = String.format("%.1f", remainder);
        midFormat = midFormat.replace(",", ".");
        remainder = Float.parseFloat(midFormat);
//        try{
//
//        }catch(Exception e){
//            remainder = Float.parseFloat(String.format("%,1f", remainder));
//        }

        String addHour = "";

        if(remainder <= 0){
            addHour = hour+"";
        }else if(remainder <= 0.5f){
            addHour = hour + ".5";
        }else{
            addHour = (hour + 1) + "";
        }

        mapResult.put("person", person);
        mapResult.put("duration", addHour);
        return mapResult;
    }

    public final static String getDateForExpiry(){
        //2일더함
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, 2);

        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        String datetime = format.format(c.getTimeInMillis());
        return datetime;
    }

    public final static String getPhoneCodeWithoutNationalCode(String phone, String nationalPhoneCode) throws OkhomeException {
        phone = phone.replace("-", "");
        phone = phone.replace(" ", "");

        if(phone.length() < 5){
            throw new OkhomeException(-100, "Check your phone number");
        }

        if(nationalPhoneCode.equals("+62")){
            //인도네시아면
            if(phone.startsWith("081")){
                phone = phone.substring(1);
            }
            else if(phone.startsWith("+62")){
                phone = phone.substring(3);
            }
            else if(phone.startsWith("81")){
//                phone = "0" + phone;
            }

            //0812입력할경우 0을 +62로 변경
            //812로 입력할경우 +62추가
            //+62812하면 냅둠
        }else if(nationalPhoneCode.equals("+82")){
            //코리아면
            if(phone.startsWith("01")){
                phone = phone.substring(1);
            }
            else if(phone.startsWith("+82")){
                phone = phone.substring(3);
            }
            else if(phone.startsWith("10")){
                phone = phone;
            }
        }
        return phone;
    }

    public final static String getCurrentLang(Context context){
        Locale systemLocale = context.getResources().getConfiguration().locale;
        String strLanguage = systemLocale.getLanguage();
        return strLanguage;
    }

    public static int getScreenHeight(Activity activity){
        try{
            DisplayMetrics metrics = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);

            int height = metrics.heightPixels;
            int width = metrics.widthPixels;

            return height;
        }catch(Exception e){

            return 400;
        }



    }
    public static int getScreenWidth(Activity activity){
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);

        int height = metrics.heightPixels;
        int width = metrics.widthPixels;

        return width;
    }


    /**포맷형태로 날짜 더해서 변환*/
    public static final String getCurrentDateSimpleTypeWithAdd(int calendarField, int value){
        try{
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(OkhomeUtil.getCurrentJarartaTimeMills());
            c.add(calendarField, value);

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String date = format.format(c.getTime());
            return date;
        }catch(Exception e){
            return null;
        }
    }

    /**포맷형태로 날짜 더해서 변환*/
    public static final String getCurrentDateWithAdd(String formatString, int calendarField, int value){
        try{
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(OkhomeUtil.getCurrentJarartaTimeMills());
            c.add(calendarField, value);

            SimpleDateFormat format = new SimpleDateFormat(formatString);
            String date = format.format(c.getTime());
            return date;
        }catch(Exception e){
            return null;
        }
    }

    /**포맷형태로 날짜 변환*/
    public static final String getCurrentDate(String formatString){
        try{
            SimpleDateFormat format = new SimpleDateFormat(formatString);
            String date = format.format(OkhomeUtil.getCurrentJarartaTimeMills());
            return date;
        }catch(Exception e){
            return null;
        }
    }

    /**yyyy-MM-dd 형태로 날짜 변환*/
    public static final String getCurrentDateSimpleType(){
        return getCurrentDate("yyyy-MM-dd");
    }

    /**날짜형식 변경*/
    public static final String getPatternedTimeString(String dateString, String patternFrom, String patternDest){

        try{
            SimpleDateFormat formatFrom = new SimpleDateFormat(patternFrom);
            SimpleDateFormat formatDest = new SimpleDateFormat(patternDest);

            return formatDest.format(formatFrom.parse(dateString).getTime());
        }catch(Exception e){
            return "err";
        }
    }

    /**pivot데이트 기준으로 targetDate가 몇번재 주인지 가져오기*/
    public static final int getWeekFromSpecificDate(String pivotDate, String targetDate) throws ParseException {


        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date datePivot = dateFormat.parse(pivotDate);
        Date dateTarget = dateFormat.parse(targetDate);

        Calendar cPivot = Calendar.getInstance();
        cPivot.setTime(datePivot);
        cPivot.set(Calendar.HOUR, 0);
        cPivot.set(Calendar.MINUTE, 0);
        cPivot.set(Calendar.SECOND, 0);

        Calendar cTarget = Calendar.getInstance();
        cTarget.setTime(dateTarget);
        cTarget.set(Calendar.HOUR, 0);
        cTarget.set(Calendar.MINUTE, 0);
        cTarget.set(Calendar.SECOND, 0);


        //시작일은 무슨요일
        int dayOfWeekPivot = cPivot.get(Calendar.DAY_OF_WEEK);
        int dayOfWeekTarget = cTarget.get(Calendar.DAY_OF_WEEK);

        long diff = cTarget.getTimeInMillis() - cPivot.getTimeInMillis();

        Calendar cDiff = Calendar.getInstance();
        cDiff.setTimeInMillis(diff);

        int diffDay = (int)(diff / 1000 / 60 / 60 / 24);
        int weekFromDay = ((dayOfWeekPivot-1) + diffDay) / 7;

        return weekFromDay;
    }

    public static String getFull2Decimal(int decimal){
        if(decimal < 10){
            return "0" + decimal;
        }else{
            return decimal + "";
        }
    }

    /**1개월을 42일로 나누어서 전, 후 월 일부도 다 가져옴*/
    public static final Map<String, String> getMonthRange42(int year, int month){
        String startDate = "", endDate = "";

        Calendar cal = Calendar.getInstance();
        cal.set(year, month, 1);
        cal.setFirstDayOfWeek(Calendar.SUNDAY);//Sunday is first day of week in this sample

        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);//Get day of the week in first day of this month

        cal.add(Calendar.DAY_OF_MONTH, Calendar.SUNDAY - dayOfWeek);//Move to first day of first week

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        startDate = dateFormat.format(cal.getTime());
        cal.add(Calendar.DAY_OF_MONTH, 41);
        endDate = dateFormat.format(cal.getTime());
        //
        return OkhomeUtil.makeStringMap("startDate", startDate, "endDate", endDate);
    }

    /**프래그먼트 파라미터랑 같이 생성*/
    public final static<T> T makeFragmentInstance(Class<T> fragmentClass, Map<String, Object> params){
        try{

            Constructor[] allConstructors = fragmentClass.getDeclaredConstructors();
            T f = (T)allConstructors[0].newInstance();


            Bundle bundle = new Bundle();
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();

                if(value instanceof Integer){
                    bundle.putInt(key, (int)value);
                }
                else if(value instanceof String){
                    bundle.putString(key, (String)value);
                }
                else if(value instanceof Float){
                    bundle.putFloat(key, (Float)value);
                }
            }

            ((Fragment)f).setArguments(bundle);
            return f;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }


    public static final boolean isEmpty(String value){
        if(value == null || value.equals("")){
            return true;
        }else{
            return false;
        }
    }

    /**앱 버전 갖고오기*/
    public final static String getAppVersionName(Activity context) {

        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo packageInfo = pm.getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionName;

        } catch (PackageManager.NameNotFoundException e) {
            return "Unknown";
        }

    }


    public final static void openMarketIntent(Context context, String packageName){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("market://details?id=" + packageName));
        context.startActivity(intent);
    }


    public final static void openThisMarketIntent(Context context){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("market://details?id=" + context.getPackageName()));
        context.startActivity(intent);
    }

    public static void openWhatsApp(Context context, String phone) {
        String smsNumber = phone;
        //"91XXXXXXXX20";
        boolean isWhatsappInstalled = whatsappInstalledOrNot(context, "com.whatsapp");
        if (isWhatsappInstalled) {

            Intent sendIntent = new Intent("android.intent.action.MAIN");
            sendIntent.setComponent(new ComponentName("com.whatsapp", "com.whatsapp.Conversation"));
            sendIntent.putExtra("jid", PhoneNumberUtils.stripSeparators(smsNumber) + "@s.whatsapp.net");//phone number without "+" prefix

            context.startActivity(sendIntent);
        } else {
            Uri uri = Uri.parse("market://details?id=com.whatsapp");
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            Toast.makeText(context, "WhatsApp not Installed",
                    Toast.LENGTH_SHORT).show();
            context.startActivity(goToMarket);
        }
    }

    private static boolean whatsappInstalledOrNot(Context context, String uri) {
        PackageManager pm = context.getPackageManager();
        boolean app_installed = false;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }

    public final static void openWhatsAppMessageIntent(Context context, String phone, String subject, String text){
        try{
//            Intent waIntent = new Intent(Intent.ACTION_SEND);
//            waIntent.setType("text/plain");
//            PackageInfo info= context.getPackageManager().getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);
//            waIntent.setPackage("com.whatsapp");
//            waIntent.putExtra(Intent.EXTRA_TEXT, text);
//            context.startActivity(Intent.createChooser(waIntent, "Share with"));

            Uri uri = Uri.parse("smsto:" + phone);
            Intent i = new Intent(Intent.ACTION_SENDTO, uri);
            i.setPackage("com.whatsapp");
            context.startActivity(Intent.createChooser(i, ""));
        }catch(Exception e){
            openMarketIntent(context, "com.whatsapp");
        }
    }

    public final static void openLineMessageIntent(Context context, String phone, String subject, String text){
        try{
//            Intent waIntent = new Intent(Intent.ACTION_SEND);
//            waIntent.setType("text/plain");
//            PackageInfo info= context.getPackageManager().getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);
//            waIntent.setPackage("com.whatsapp");
//            waIntent.putExtra(Intent.EXTRA_TEXT, text);
//            context.startActivity(Intent.createChooser(waIntent, "Share with"));
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_SUBJECT, subject);
            intent.putExtra(Intent.EXTRA_TEXT, text);
            intent.setPackage("jp.naver.line.android");

            context.startActivity(intent);
        }catch(Exception e){
            openMarketIntent(context, "jp.naver.line.android");
        }
    }
    public final static void openKakaoTalkMessageIntent(Context context, String phone, String subject, String text){
        try{
//            Intent waIntent = new Intent(Intent.ACTION_SEND);
//            waIntent.setType("text/plain");
//            PackageInfo info= context.getPackageManager().getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);
//            waIntent.setPackage("com.whatsapp");
//            waIntent.putExtra(Intent.EXTRA_TEXT, text);
//            context.startActivity(Intent.createChooser(waIntent, "Share with"));
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_SUBJECT, subject);
            intent.putExtra(Intent.EXTRA_TEXT, text);
            intent.setPackage("com.kakao.talk");

            context.startActivity(intent);
        }catch(Exception e){
            openMarketIntent(context, "com.kakao.talk");
        }
    }

    public final static void openEmailIntent(Context context, String email, String subject, String text){
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:"+email));
        i.putExtra(Intent.EXTRA_SUBJECT, subject);
        i.putExtra(Intent.EXTRA_TEXT, text);
        context.startActivity(i);
    }

    public final static void openWebIntent(Context context, String url){
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        context.startActivity(i);
    }

    public static void openPhoneDialIntent(Context context, String phone){
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
        context.startActivity(intent);
    }

    public final static String getFormattedDateString(String date, String format){
        try{
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            long time = dateFormat.parse(date).getTime();

            SimpleDateFormat dateFormat2 = new SimpleDateFormat(format, Locale.KOREAN);
            String s = dateFormat2.format(time);
            return s;
        }catch(Exception e){
            return e.toString();
        }
    }

    public final static String changeDatetimeFormat(String date, String formatFrom, String formatTo, Locale locale){
        try{
            SimpleDateFormat dateFormat = new SimpleDateFormat(formatFrom);
            long time = dateFormat.parse(date).getTime();

            SimpleDateFormat dateFormat2 = new SimpleDateFormat(formatTo, locale);
            String s = dateFormat2.format(time);
            return s;
        }catch(Exception e){
            return e.toString();
        }
    }

    public final static String getCurrentDateTime(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(new Date(OkhomeUtil.getCurrentJarartaTimeMills()));
    }

    //**이메일 정규식체크*/
    public final static boolean isValidEmail(String email) {
        boolean err = false;
        String regex = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$";

        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(email);
        if( m.matches() ) {
            err = true;
        }
        return err;
    }

    /**맵값을 캐스팅없이 가져옴*/
    public static <T> T getMapValue(Map map, String key){
        return (T)map.get(key);
    }


    /**etbox 키보드 컨트롤*/
    public static void setSoftKeyboardVisiblity(EditText etTarget, boolean on){
        if(on == true){
            InputMethodManager imm = (InputMethodManager)etTarget.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(etTarget, InputMethodManager.SHOW_FORCED);
            //imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        }else{
            InputMethodManager imm = (InputMethodManager)etTarget.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(etTarget.getWindowToken(), 0);
        }
    }

    /**키보드 숨기기*/
    public static final void hideKeyboard(Activity act){
        try{
            InputMethodManager inputManager = (InputMethodManager) act.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(act.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }catch(Exception e){
            ;
        }

    }

    /**dp를 픽셀로받기*/
    public static int getPixelByDp(Context context, int dp){
        Resources r = context.getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());

        return (int)px;
    }

    public static int getPixelBySp(Context context, float sp){
        Resources r = context.getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, r.getDisplayMetrics());

        return (int)px;
    }

    /**메모리 주소*/
    public static final String getMemoryAddress(Object obj){
        return obj.getClass().getName() + '@' + Integer.toHexString(obj.hashCode());
    }

    /**숫자받아서 format카운트만큼 0으로 채워넣기*/
    public static final String fillupWithZero(int decimal, String format){
        return fillupWith(decimal, format, "0");
    }

    public static final String fillupWith2Zero(String decimal){
        return fillupWith(Integer.parseInt(decimal), "XX", "0");
    }

    /**숫자받아서 format카운트만큼 character로 채워넣기*/
    public static final String fillupWith(int decimal, String format, String character){
        String target = decimal + "";

        while(target.length() < format.length()){
            target = character + target;
        }

        return target;
    }

    /**맵 만들기. 짝수로 넘겨야함*/
    public static Map<String, Object> makeMap(Object... objs){
        Map<String, Object> params = new HashMap<String, Object>();

        for(int i = 0; i < objs.length; i+=2){
            params.put((String)objs[i], objs[i+1]);
        }

        return params;
    }

    public static Map<String, String> makeStringMap(String... objs){
        Map<String, String> params = new HashMap<String, String>();

        for(int i = 0; i < objs.length; i+=2){
            params.put((String)objs[i], objs[i+1]);
        }

        return params;
    }

    /**현재 년도 가져오기*/
    public static final int getCurrentYear(){
        return Calendar.getInstance().get(Calendar.YEAR);
    }

    /**현재 월 가져오기*/
    public static final int getCurrentMonth(){
        return Calendar.getInstance().get(Calendar.MONTH)+1;
    }

    /**토스트 띄우기*/
    public static final void showToast(Context context, String msg){
        if(context != null) Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    /** 뷰페이저 스와이핑 막기*/
    public static final void disableViewPagerSwiping(ViewPager vp){
        vp.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
    }

    /** 뷰페이저 스와이핑 막기*/
    public static final void ableViewPagerSwiping(ViewPager vp){
        vp.setOnTouchListener(null);
    }

    /**화폐숫자 가져오기*/
    public static final String getMoneyString(String str){
        return getMoneyString(str, ',');
    }

    public static final String getMoneyString(int money, char ch){
        return getMoneyString(money + "", ch);
    }

    /**화폐숫자 가져오기*/
    public static final String getMoneyString(String str, char ch){

        NumberFormat numberFormat = null;
        numberFormat = NumberFormat.getInstance();

        String v = numberFormat.format(Long.parseLong(str));
        return v;
    }
}
