package com.sellcom.apps.tracker_material.Async_Request;

import java.io.IOException;
import java.io.InputStream;
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

import org.apache.http.HttpEntity;
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
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.gc.materialdesign.widgets.Dialog;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class RequestManager implements ResponseListenerInterface {

    public  final   boolean                                 TEST_MODE          = false;

    public  final   String 	                                LOG_TAG_MANAGER    = "requestManager";
    public  final   String 	                                LOG_TAG_REQUEST    = "asyncRequest";

    public final String API_REQUEST_ZIPCODE = "http://validacpscs.estafeta.com/RestService.svc/ConsultaCP";

    /*
    public static final int METHOD_REQUEST_ZIPCODE = 0;
    public static final int METHOD_REQUEST_ZIPCODE_ADDRESSES = 1;
    public static final int METHOD_REQUEST_ = 2;
    public static final int METHOD_REQUEST_ZIPCODE = 3;
    public static final int METHOD_REQUEST_ZIPCODE = 4;
    */


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
        Log.d(LOG_TAG_MANAGER, "Error message: " + errorMessage);

        Dialog  dialog = new Dialog(context,"Error",errorMessage);
        dialog.show();
    }

    public void showConfirmationDialog(String confirmMessage, Context context){
        Log.d(LOG_TAG_MANAGER,"Error message: "+confirmMessage);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Atención");
        builder.setMessage(confirmMessage);
        builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

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

    }

    //created by jose luis at 27/05/2015
    public void makeRequest(METHOD type_request, Map<String, String> params){
        this.method = type_request;
        final AsyncRequest asyncRequest =  new AsyncRequest(activity, params, this, method);
        asyncRequest.execute();
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

    @Override
    public void responseServiceToManager(String stringReponse){
        listener.decodeResponse(stringReponse);
    }

    /**
     *Created by Jose Luis at 27/05/2015
     * Generic AsyncRequest
     */
    public class AsyncRequest extends AsyncTask<Void, Void, String>{

        Activity activity;
        Map<String, String> requestData;
        ResponseListenerInterface listener;
        METHOD method;

        public AsyncRequest(Activity activity, Map<String, String> requestData,
                                   ResponseListenerInterface listener, METHOD method){
            this.activity =  activity;
            this.requestData = requestData;
            this.listener = listener;
            this.method = method;
        }

        @Override
        protected String doInBackground(Void... voids){

            String stringResponse = null;

            HttpParams httpParameters = new BasicHttpParams();
            int timeoutConnection = 5000;
            HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
            int timeoutSocket = 5000;
            HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

            //create a new instance of HttpClient request POST
            HttpClient httpclient = new DefaultHttpClient(httpParameters);
            //especify the method to connect to web server
            HttpPost httppost = new HttpPost(getRequestURL(this.method));

            try {
                List<NameValuePair> params = new ArrayList<NameValuePair>(requestData.size());

                for (Map.Entry<String, String> entry : requestData.entrySet())
                    params.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));

                httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
                httppost.setHeader("Content-Type","application/x-www-form-urlencoded");

                HttpResponse response = httpclient.execute(httppost);

                HttpEntity entity = response.getEntity();
                if(entity != null) {
                    InputStream streamZipCodes = entity.getContent();
                    try {
                        stringResponse = parseToStringZipCodes(streamZipCodes);
                    } catch (SAXException e) {
                        e.printStackTrace();
                    } catch (ParserConfigurationException e) {
                        e.printStackTrace();
                    }
                }

            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return stringResponse;
        }

        @Override
        protected void onPostExecute(String stringResponse){
            listener.responseServiceToManager(stringResponse);
        }
    }

    /**
     * @param zipcodes
     * @return
     * @throws SAXException
     * @throws IOException
     * @throws ParserConfigurationException
     */
    public String parseToStringZipCodes(InputStream zipcodes) throws SAXException, IOException, ParserConfigurationException{
        Document doc;
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        documentBuilder.isValidating();
        doc = documentBuilder.parse(zipcodes);
        zipcodes.close();
        doc.getDocumentElement().normalize();
        String contentDocument = doc.getDocumentElement().getTextContent();
        return contentDocument;
    }

    public interface ConfirmationDialogListener {
        public void okFromConfirmationDialog(String message);
    }

    private String getRequestURL(METHOD type_request){
        String url = null;
        switch (type_request){
            case REQUEST_ZIPCODE:
                url=  "http://validacpscs.estafeta.com/RestService.svc/ConsultaCP";
                Log.v(METHOD.REQUEST_ZIPCODE.toString(), url);
            break;
            case REQUEST_ZIPCODE_ADDRESSES:
                url =  "http://validacpscs.estafeta.com/RestService.svc/ConsultaDatosCP";
                Log.v(METHOD.REQUEST_ZIPCODE_ADDRESSES.toString(), url);
            break;
            case REQUEST_TRACKING_LIST_CODES:
                url =  "http://trackingcs.estafeta.com/RestService.svc/ExecuteQueryPlano";
                Log.v( METHOD.REQUEST_TRACKING_LIST_CODES.toString(), url);
            break;
            case REQUEST_TRACKING_LIST_GUIDES:
                url =  "http://trackingcs.estafeta.com/RestService.svc/ExecuteQueryPlano";
                Log.v( METHOD.REQUEST_TRACKING_LIST_GUIDES.toString(), url);
            break;
            case REQUEST_NATIONAL_DELIVERY:
                url =  "http://frecuenciacotizadorcs.estafeta.com/RestService.svc/FrecuenciaCotizadorPlano";
                Log.v( METHOD.REQUEST_NATIONAL_DELIVERY.toString(), url);
            break;
            case REQUEST_INTERNATIONAL_DELIVERY:
                url =  "http://cotizadorintcs.estafeta.com/RestService.svc/CotizaPlano";
                Log.v( METHOD.REQUEST_INTERNATIONAL_DELIVERY.toString(), url);
                break;
            case REQUEST_OFFICES:
                url = "http://sucursalescs.estafeta.com/RestService.svc/consultaConFechaPlano";
                Log.v( METHOD.REQUEST_OFFICES.toString(), url);
            case REQUEST_EXCEPTION_CODES:
                url =  "http://clavexcs.estafeta.com/RestService.svc/consultaConFechaMovilidadPlano";
                Log.v( METHOD.REQUEST_EXCEPTION_CODES.toString(), url);
            break;
            default:
                break;
        }
        return url;
    }
}