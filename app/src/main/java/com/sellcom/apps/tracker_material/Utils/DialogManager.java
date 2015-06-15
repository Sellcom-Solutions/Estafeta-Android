package com.sellcom.apps.tracker_material.Utils;

import android.app.Activity;
import android.app.Dialog;
import android.os.Handler;
import android.view.Window;
import android.widget.TextView;
import android.widget.ProgressBar;

import com.sellcom.apps.tracker_material.R;

/**
 * Created by hugo.figueroa on 01/06/15.
 */
public class DialogManager {


    private static DialogManager manager;
    private         Activity                                        activity;
    private         TYPE_DIALOG                                     dialogType;
    private         Dialog                                          dialogSplahs,
                                                                    dialogLoadig;

    public static final int  SPLASH  = 1;
    public static final int  LOADING = 2;
    public static final int  ERROR   = 3;
    public static final int  SUCCESS = 4;


    public enum TYPE_DIALOG {
        SPLASH("splash"),
        LOADING("loading"),
        ERROR("error"),
        SUCCESS("success");


        private final String name;

        private TYPE_DIALOG(String s) {
            name = s;
        }

        public boolean equalsName(String otherName){
            return (otherName == null)? false:name.equals(otherName);
        }

        public String toString(){
            return name;
        }
    }



    public static synchronized DialogManager sharedInstance(){
        if (manager == null)
            manager = new DialogManager();
        return manager;
    }

    public void                         setActivity(Activity activity)  {this.activity    = activity;}

    public Activity                     getActivity()                   {return activity;}


   public void showDialog(TYPE_DIALOG type ,String message, int time){


       dialogType = type;

           if(type == TYPE_DIALOG.SPLASH) {

               dialogSplahs = new Dialog(activity, android.R.style.Theme_Translucent_NoTitleBar);
               dialogSplahs.setContentView(R.layout.dialog_message_splash);
               dialogSplahs.setCancelable(false);

               TextView textDialogSplash = (TextView) dialogSplahs.findViewById(R.id.txv_transparent_name_dialog);
               textDialogSplash.setText(message);

               dialogSplahs.show();

            }else if(type == TYPE_DIALOG.LOADING || type == TYPE_DIALOG.ERROR || type == TYPE_DIALOG.SUCCESS) {
               dialogLoadig = new Dialog(activity);
               dialogLoadig.requestWindowFeature(Window.FEATURE_NO_TITLE);
               dialogLoadig.setContentView(R.layout.dialog_message_loading);
               dialogLoadig.setCancelable(false);


               TextView textDialogLoading = (TextView) dialogLoadig.findViewById(R.id.txv_general_name_dialog);
               textDialogLoading.setText(message);

               ProgressBar pgb = (ProgressBar) dialogLoadig.findViewById(R.id.pgb_general_dialog);


                if(type == TYPE_DIALOG.LOADING){
                    pgb.setIndeterminateDrawable(activity.getResources().getDrawable(R.drawable.progress_anim_gray));
                }else if(type == TYPE_DIALOG.ERROR){
                    pgb.setIndeterminateDrawable(activity.getResources().getDrawable(R.drawable.ic_error));
                }else if(type == TYPE_DIALOG.SUCCESS){
                    pgb.setIndeterminateDrawable(activity.getResources().getDrawable(R.drawable.success_drawable_dialog));
                }
               dialogLoadig.show();

                if(time != 0) {

                    Handler handler = new Handler();//Para dar un tiempo al dialog
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            dismissDialog();
                        }
                    }, time);
                }


       }


   }


    public void dismissDialog(){

        if(dialogType == TYPE_DIALOG.SPLASH){
            dialogSplahs.dismiss();
        }else if(dialogType == TYPE_DIALOG.LOADING || dialogType == TYPE_DIALOG.ERROR || dialogType == TYPE_DIALOG.SUCCESS){
            dialogLoadig.dismiss();
        }

    }


}
