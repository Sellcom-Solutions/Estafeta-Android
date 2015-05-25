package com.sellcom.apps.tracker_material.Utils;

import android.content.Context;

import com.sellcom.apps.tracker_material.R;

public class ErrorDecoder {

    public static String decodeErrorMessage(String error, Context context){
        String errorMessage = context.getString(R.string.req_man_error_contacting_service);
        if (error.toLowerCase().contains("has already started")){
            return context.getString(R.string.req_man_error_visit_already_starter_1)+error.split("[\\(\\)]")[1]
                    + context.getString(R.string.req_man_error_visit_already_starter_2)
                    + " ("+error.split("[\\(\\)]")[3]+")";
        }
        else if (error.toLowerCase().contains("error_cerrada")){
            return context.getString(R.string.req_man_error_visit_already_starter_1)+error.split("[\\(\\)]")[1]
                    + ") " +context.getString(R.string.req_man_error_visit_already_ended_2)
                    + " ("+error.split("[\\(\\)]")[3]+")";
        }
        else if (error.toLowerCase().contains("incomplete_data_new_client")){
            return context.getString(R.string.req_man_error_incomplete_data_new_client);
        }
        else if (error.contains("Invalid credentials")){
            return context.getString(R.string.req_man_error_credentials);
        }
            return error;
    }
}
