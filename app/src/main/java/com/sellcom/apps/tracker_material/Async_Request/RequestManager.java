package com.sellcom.apps.tracker_material.Async_Request;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.gc.materialdesign.widgets.Dialog;

public class RequestManager implements ResponseListenerInterface {

    public  final   boolean                                 TEST_MODE          = false;

    public  final   String 	                                LOG_TAG_MANAGER    = "requestManager";
    public  final   String 	                                LOG_TAG_REQUEST    = "asyncRequest";

    public  final   String 	                                API_URL 	       = "http://187.237.42.162:8880/alpura/api.php";
    //public  final   String 	                                API_URL 	       = "http://172.20.111.69:8880/alpura/api.php";

    private static 	RequestManager   						manager;
    private         Activity                                activity;
    private         UIResponseListenerInterface             listener;
    private         METHOD                                  method;

    private         ProgressDialog                          progressDialog;

    private RequestManager(){
    }
    public static synchronized RequestManager sharedInstance(){
        if (manager == null)
            manager = new RequestManager();
        return manager;
    }

    public void                         setActivity(Activity activity)  {this.activity    = activity;}
    public Activity                     getActivity()                   {return activity;}

    public void                         setListener(UIResponseListenerInterface listener){   this.listener = listener;}
    public UIResponseListenerInterface  getListener(){return listener;}

    public void showErrorDialog(String errorMessage, Context context){
        Log.d(LOG_TAG_MANAGER,"Error message: "+errorMessage);

        Dialog  dialog = new Dialog(context,"Error",errorMessage);
        dialog.show();
    }

    public void showConfirmationDialog(String confirmMessage, Context context){
        Log.d(LOG_TAG_MANAGER,"Error message: "+confirmMessage);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Atención");
        builder.setMessage(confirmMessage);
        builder.setNeutralButton("OK",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int id) {

            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void showConfirmationDialogWithListener(final String confirmMessage, Context context, final ConfirmationDialogListener listener){
        Log.d(LOG_TAG_MANAGER,"Error message: "+confirmMessage);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Atención");
        builder.setMessage(confirmMessage);
        builder.setNeutralButton("OK",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int id) {
                listener.okFromConfirmationDialog(confirmMessage);
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void showLoadingDialog(){

    }

    public void dismissProgressDialog(){
        progressDialog.dismiss();
    }

    public void makeRequestWithDataAndMethodIncludeCredentials(Map<String,String> reqData, METHOD method, boolean includeCredentials){
        this.method                 = method;

        Map<String,String> params = new HashMap<>();
        params  = reqData;

        params.put("request", method.toString());

        showLoadingDialog();
        final AsyncRequest req      = new AsyncRequest(activity, params, this);

        /* DUMMY request mode info */
        req.method                  = method;
        /***************************/
        req.execute(null,null,null);
    }

    public void makeRequestWithJSONDataAndMethod(JSONObject reqData, METHOD method){
        Log.d(LOG_TAG_MANAGER,"MakeRequestJSON");
        this.method                         = method;
        showLoadingDialog();
        final AsyncRequestWithJSON req      = new AsyncRequestWithJSON(activity, reqData, this);

        /* DUMMY request mode info */
        req.method                  = method;
        /***************************/
        req.execute(null,null,null);
    }

    @Override
    public void responseServiceToManager(JSONObject jsonResponse) {
        dismissProgressDialog();
        try {
            if(jsonResponse.getString("success").equalsIgnoreCase("true")){
                // Decode the json object
                Log.d(LOG_TAG_MANAGER, jsonResponse.toString());
                listener.decodeResponse(jsonResponse.toString());
            }
            else{
                showErrorDialog(jsonResponse.getString("resp"),activity);
            }
        } catch (JSONException e) {
            e.printStackTrace();

        }
    }

    public class AsyncRequest extends AsyncTask<Void,Void,JSONObject>{
        Activity 					activity;
        Map<String, String> 		requestData;
        ResponseListenerInterface   listener;

        /* DUMMY request mode info */
        METHOD                      method;

        public AsyncRequest(Activity activity, Map<String, String> requestData, ResponseListenerInterface listener){
            this.activity 	       	= activity;
            this.requestData        = requestData;
            this.listener    		= listener;
        }

        @Override
        protected JSONObject doInBackground(Void... voids) {
            Log.d(LOG_TAG_REQUEST,"Request data:"+requestData.toString());
            JSONObject jsonResponse = null;
            if (TEST_MODE){

            }
            else{
                HttpParams httpParameters   = new BasicHttpParams();
                int timeoutConnection       = 5000;
                HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
                int timeoutSocket           = 5000;
                HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

                HttpClient httpclient   = new DefaultHttpClient(httpParameters);
                HttpPost httppost       = new HttpPost(API_URL);

                try {
                    List<NameValuePair> params = new ArrayList<NameValuePair>(requestData.size());

                    for (Map.Entry<String, String> entry : requestData.entrySet())
                        params.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));

                    httppost.setEntity(new UrlEncodedFormEntity(params));

                    HttpResponse response       = httpclient.execute(httppost);
                    String       strResponse    = EntityUtils.toString(response.getEntity());
                    Log.d(LOG_TAG_REQUEST,"Response: "+strResponse);
                    try {
                        jsonResponse            = new JSONObject(strResponse);
                        jsonResponse.put("method",method.toString());
                        Log.d(LOG_TAG_REQUEST,"jsonResponse: "+jsonResponse.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } catch (ClientProtocolException e) {
                    try {
                        jsonResponse    = new JSONObject();
                        jsonResponse.put("error", "Network");
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                } catch (IOException e) {
                    try {
                        jsonResponse    = new JSONObject();
                        jsonResponse.put("error", "Info");
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                }
            }
            return jsonResponse;
        }

        protected void onPostExecute(JSONObject jsonResponse) {
            //Log.d(LOG_TAG_REQUEST,"jsonResponse_post: "+jsonResponse.toString());
            listener.responseServiceToManager(jsonResponse);
        }
    }

    public class AsyncRequestWithJSON extends AsyncTask<Void,Void,JSONObject>{
        Activity 					activity;
        JSONObject 		            requestData;
        ResponseListenerInterface   listener;

        /* DUMMY request mode info */
        METHOD                      method;

        public AsyncRequestWithJSON(Activity activity, JSONObject requestData, ResponseListenerInterface listener){
            this.activity 	       	= activity;
            this.requestData        = requestData;
            this.listener    		= listener;

        }

        @Override
        protected JSONObject doInBackground(Void... voids) {
            Log.d(LOG_TAG_REQUEST,"Request data (JSON):"+requestData.toString());
            JSONObject jsonResponse = null;
            if (TEST_MODE){
                try {
                    Thread.sleep(5000);
                    jsonResponse = new JSONObject();
                    try {
                        jsonResponse.put("method",method.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            else{
                HttpParams httpParameters   = new BasicHttpParams();
                int timeoutConnection       = 10000;
                HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
                int timeoutSocket = 10000;
                HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

                HttpClient httpclient   = new DefaultHttpClient(httpParameters);
                HttpPost httppost               = new HttpPost(API_URL);
                ArrayList<NameValuePair> params = new ArrayList<NameValuePair>(6);
                try {
                    Log.d(LOG_TAG_REQUEST,"Req: "+requestData.toString());

                    if ((requestData.toString().contains("{pdv_id:2"))
                    || (requestData.toString().contains("{pdv_id\":\"4\",\n" +
                            "\t\"products\":\"[\n" +
                            "\t\t{\\\"id_product_container\\\":\\\"2\\\",\\\"price\\\":\\\"5\\\",\\\"id_product\\\":\\\"47\\\"},\n" +
                            "\t\t{\\\"id_product_container\\\":\\\"1\\\",\\\"price\\\":\\\"6.70\\\",\\\"id_product\\\":\\\"67\\\"}]\",\n"))){
                        try {
                            Thread.sleep(5000);
                            jsonResponse = new JSONObject();
                            try {
                                jsonResponse.put("method",method.toString());
                                jsonResponse.put("success",true);
                                jsonResponse.put("resp","OK");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }


                    try {
                        params.add(new BasicNameValuePair("request", requestData.get("request").toString()));
                        params.add(new BasicNameValuePair("user", requestData.get("user").toString()));
                        params.add(new BasicNameValuePair("token", requestData.get("token").toString()));
                        params.add(new BasicNameValuePair("date", requestData.get("date").toString()));
                        params.add(new BasicNameValuePair("id_pdv", requestData.get("id_pdv").toString()));
                        String str = (requestData.get("products").toString());
                        params.add(new BasicNameValuePair("products", str));

                        httppost.setEntity(new UrlEncodedFormEntity(params,HTTP.UTF_8));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    HttpResponse response       = httpclient.execute(httppost);

                    String       strResponse    = EntityUtils.toString(response.getEntity());
                    Log.d(LOG_TAG_REQUEST,"Response: "+strResponse);
                    try {
                        jsonResponse            = new JSONObject(strResponse);
                        jsonResponse.put("method",method.toString());
                        Log.d(LOG_TAG_REQUEST,"jsonResponse: "+jsonResponse.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } catch (ClientProtocolException e) {
                    try {
                        jsonResponse    = new JSONObject();
                        jsonResponse.put("resp", "Network");
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                } catch (IOException e) {
                    try {
                        jsonResponse    = new JSONObject();
                        jsonResponse.put("resp", "Info");
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                }
            }
            return jsonResponse;
        }

        protected void onPostExecute(JSONObject jsonResponse) {
            //Log.d(LOG_TAG_REQUEST,"jsonResponse_post: "+jsonResponse.toString());
            listener.responseServiceToManager(jsonResponse);
        }
    }

    public interface ConfirmationDialogListener {
        public void okFromConfirmationDialog(String message);
    }
}