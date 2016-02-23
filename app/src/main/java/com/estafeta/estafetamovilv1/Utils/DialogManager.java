package com.estafeta.estafetamovilv1.Utils;

import android.app.Activity;
import android.app.Dialog;
import android.os.Handler;
import android.text.Spannable;
import android.text.Spanned;
import android.view.Window;
import android.widget.TextView;
import android.widget.ProgressBar;

import com.estafeta.estafetamovilv1.R;

/**
 * Created by hugo.figueroa on 01/06/15.
 * This class is responsible for displaying dialogs Loading, Error and Success of the application.
 */
public class DialogManager {


    private static DialogManager manager;
    private         Activity                                        activity;
    private         TYPE_DIALOG                                     dialogType;
    private         Dialog                                          dialogSplahs = null,
                                                                    dialogLoadig = null;

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

    private static boolean flag = true;


    /**
     * This method is responsible for displaying the dialog.
     * @param type Dialog type.
     * @param message Dialog message.
     * @param time Dialog duration.
     */
   public void showDialog(TYPE_DIALOG type ,String message, int time){


       dialogType = type;

       if(dialogLoadig != null){
           dismissDialog();
       }

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

               if(!isShowingDialog()) {
                   if(flag) {
                       flag = false;
                       dialogLoadig.show();
                       if (time != 0) {

                            Handler handler = new Handler ();//Para dar un tiempo al dialog

                           handler.postDelayed(new Runnable() {
                               public void run() {
                                   dismissDialog();
                               }
                           }, time);

                       }

                   }

               }


       }


   }

    /**
     * This method is responsible for displaying the dialog.
     * @param type Dialog type.
     * @param message Dialog message with style.
     * @param time Dialog duration.
     */
    public void showDialog(TYPE_DIALOG type ,Spanned message, int time){


        dialogType = type;

        if(dialogLoadig != null){
            dismissDialog();
        }

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

            if(!isShowingDialog()) {
                if(flag) {
                    flag = false;
                    dialogLoadig.show();
                    if (time != 0) {

                        Handler handler = new Handler ();//Para dar un tiempo al dialog

                        handler.postDelayed(new Runnable() {
                            public void run() {
                                dismissDialog();
                            }
                        }, time);

                    }

                }

            }


        }


    }

    /**
     * This method closes a dialog visible.
     */
    public void dismissDialog(){
        flag = true;
        if (dialogType == TYPE_DIALOG.SPLASH) {
            dialogSplahs.dismiss();
        } else if (dialogType == TYPE_DIALOG.LOADING || dialogType == TYPE_DIALOG.ERROR || dialogType == TYPE_DIALOG.SUCCESS) {
            dialogLoadig.dismiss();
        }

    }

    /**
     * This method checks that if a dialog is visible.
     * @return
     */
    public boolean isShowingDialog(){
        if(dialogType == TYPE_DIALOG.SPLASH){
            return dialogSplahs.isShowing();
        }else if(dialogType == TYPE_DIALOG.LOADING || dialogType == TYPE_DIALOG.ERROR || dialogType == TYPE_DIALOG.SUCCESS){
            return dialogLoadig.isShowing();
        }
        return false;
    }


}
