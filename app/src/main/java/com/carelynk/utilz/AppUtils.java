package com.carelynk.utilz;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;


import com.carelynk.R;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Admin on 05-Apr-16.
 */
public class AppUtils {



    /**
     * Intent Extra and Bundles
     */
    public static String Extra_Goal_Id = "goal_id";
    public static String Extra_Group_Id = "group_id";
    public static String Extra_Is_From_Which_Group = "is_from_which_group";
    public static String Extra_Goal_Detail = "Goal_Detail";
    //public final static String IMAGE_CONCATE_PATH = "http://demo.carelynk.com/";
    public final static String IMAGE_CONCATE_PATH = "https://carelynk.com/";

    public static void closeKeyBoard(Activity context) {
        View view =  context.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Network[] networks = connectivityManager.getAllNetworks();
            NetworkInfo networkInfo;
            for (Network mNetwork : networks) {
                networkInfo = connectivityManager.getNetworkInfo(mNetwork);
                if (networkInfo.getState().equals(NetworkInfo.State.CONNECTED)) {
                    return true;
                }
            }
        }else {
            if (connectivityManager != null) {
                NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
                if (info != null) {
                    for (NetworkInfo anInfo : info) {
                        if (anInfo.getState() == NetworkInfo.State.CONNECTED) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public static void showSnakbar(View view, String msg){
        if(view != null) {
            Snackbar snackbar = Snackbar
                    .make(view, msg, Snackbar.LENGTH_LONG);
            snackbar.show();
        }
    }




    public static void showAlertDialog(Context context, String title, String msg){
        AlertDialog.Builder builder =
                new AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle);
        builder.setTitle(title);
        builder.setMessage(title);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
      /*  builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });*/
        builder.show();
    }

    public static String formattedDate(String inputFormat, String outputFormat, String inputDate){
        if(inputFormat.equals("")){
            inputFormat = "yyyy-MM-dd hh:mm:ss";
        }
        if(outputFormat.equals("")){
            outputFormat = "EEEE d 'de' MMMM 'del' yyyy";
        }
        Date parsed = null;
        String outputDate = "";

        SimpleDateFormat df_input = new SimpleDateFormat(inputFormat, java.util.Locale.getDefault());
        SimpleDateFormat df_output = new SimpleDateFormat(outputFormat, java.util.Locale.getDefault());
        try {
            parsed = df_input.parse(inputDate);
            outputDate = df_output.format(parsed);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return outputDate;
    }

    public static Date convertStringToDate(String mDate){
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
        try {
            Date date = format.parse(mDate);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getCurruntDate(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }

    public static String get12HourTime(String time24ime){
        String time="";
        final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        try {
            final Date dateObj = sdf.parse(time24ime);
            time = new SimpleDateFormat("hh:mm").format(dateObj);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }

    public static String getImagePath(String halfPath){
        Log.e("Path Full", "getImagePath: "+halfPath);
        String mImagePath = "";
        if(!TextUtils.isEmpty(halfPath) && halfPath.contains("Content")){
            mImagePath = IMAGE_CONCATE_PATH+"Content"+halfPath.split("Content")[1];
        }
        Log.e("IMAGE", "getImagePath: "+mImagePath);
        return mImagePath;
    }

    public static String getImagePathChat(String halfPath){
        //String mImagePath = "http://demo.carelynk.com/images/"+halfPath;
        String mImagePath = "https://carelynk.com/images/"+halfPath;
        return mImagePath;
    }

    public static File createImageFile() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "Carelync_" + timeStamp + "_";
        File sdCard = new File(Environment.getExternalStorageDirectory()+"/Carelync/Images");
        if(!sdCard.exists())
            sdCard.mkdirs();
        File image = null;
        try {
            image = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    sdCard      /* directory */
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    public static String getYoutubeUrl(String src){
        ////<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/PlgSC4YeBjY\" frameborder=\"0\" allowfullscreen></iframe>
        //String sf = "<div class=\"videoEmbed\"><iframe allowfullscreen=\"\" frameborder=\"0\" height=\"349\" mozallowfullscreen=\"\" src=\"https://www.youtube.com/embed/qIsOCoDEoO8\" webkitallowfullscreen=\"\" width=\"560\"></iframe></div>"
        return "<iframe width=\"100%\" height=\"100%\" src=\""+src+"\" frameborder=\"0\" allowfullscreen></iframe>";
    }

    public static String formatToYesterdayOrToday(String date) throws ParseException {
        Date dateTime = new SimpleDateFormat("MM/DD/yyyy hh:mm:ss a").parse(date);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateTime);
        Calendar today = Calendar.getInstance();
        Calendar yesterday = Calendar.getInstance();
        yesterday.add(Calendar.DATE, -1);
        DateFormat timeFormatter = new SimpleDateFormat("hh:mma");

        if (calendar.get(Calendar.YEAR) == today.get(Calendar.YEAR) && calendar.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR)) {
            return "Today " + timeFormatter.format(dateTime);
        } else if (calendar.get(Calendar.YEAR) == yesterday.get(Calendar.YEAR) && calendar.get(Calendar.DAY_OF_YEAR) == yesterday.get(Calendar.DAY_OF_YEAR)) {
            return "Yesterday " + timeFormatter.format(dateTime);
        } else {
            return date;
        }
    }


}
