package com.ijudge.sacijudge;

import android.app.Activity;
import android.content.Context;
import android.os.Message;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class Utils {
    public static void callToast(Context context, String message){

    }
    public static void popup(ConstraintLayout constraintLayout,String msg){
        Snackbar.make(constraintLayout, msg,
                Snackbar.LENGTH_SHORT)
                .show();
    }
    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    public static String candidates(){
        return "candidates";
    }
    public static String critiria(){
        return "criteria";
    }
    public static String ratings(){
        return "ratings";
    }
}
