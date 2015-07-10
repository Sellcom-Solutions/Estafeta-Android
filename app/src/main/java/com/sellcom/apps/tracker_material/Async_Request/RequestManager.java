package com.sellcom.apps.tracker_material.Async_Request;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.gc.materialdesign.widgets.Dialog;
import com.sellcom.apps.tracker_material.R;
import com.sellcom.apps.tracker_material.Utils.DialogManager;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class RequestManager implements ResponseListenerInterface {

    public  final   boolean                                 TEST_MODE          = false;

    public  final   String 	                                LOG_TAG_MANAGER    = "requestManager";
    public  final   String 	                                LOG_TAG_REQUEST    = "asyncRequest";

    private static 	RequestManager   						manager;
    private         Activity                                activity;
    private         UIResponseListenerInterface             listener;
    private         METHOD                                  method;
    private ArrayList<Map<String, String>> responseArray1;

    public ArrayList<Map<String, String>> getResponseArray() {
        return responseArray;
    }

    public void setResponseArray(ArrayList<Map<String, String>> responseArray) {
        this.responseArray = responseArray;
    }

    private static ArrayList<Map<String,String>> responseArray = new ArrayList<>();

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

    public void showDecisionDialogWithListener(final String confirmMessage, Context context, final DecisionDialogWithListener listener){
        Log.d(LOG_TAG_MANAGER,"Error message: "+confirmMessage);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Atención");
        builder.setMessage(confirmMessage);
        builder.setPositiveButton(context.getString(R.string.done), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                listener.responseFromDecisionDialog(confirmMessage, "OK");
            }
        });
        builder.setNegativeButton(context.getString(R.string.cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                listener.responseFromDecisionDialog(confirmMessage, "CANCEL");
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
        Map<String, String> credentials;
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

        protected void onPreExecute(){

        }

        @Override
        protected String doInBackground(Void... voids) {

            String stringResponse = null;
            JSONObject jsonResponse = null;

            HttpParams httpParameters = new BasicHttpParams();
            int timeoutConnection = 5000;
            HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
            int timeoutSocket = 5000;
            HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

            //create a new instance of HttpClient request POST
            HttpClient httpclient = new DefaultHttpClient(httpParameters);
            //especify the method to connect to web server
            HttpPost httppost = new HttpPost(getRequestURL(this.method));
            credentials = getCredentials(this.method);

            if (this.method == METHOD.TEST) {

                try {
                    Thread.sleep(5000);
                    jsonResponse = new JSONObject();
                    try {
                        jsonResponse.put("method",method.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                        try {
                            jsonResponse.put("success",true);
                            jsonResponse.put("resp","OK");
                            jsonResponse.put(Parameter.BUY_RESPONSE.COSTO.toString(),"300.00");
                            jsonResponse.put(Parameter.BUY_RESPONSE.CP_DESTINO.toString(),"11500");
                            jsonResponse.put(Parameter.BUY_RESPONSE.CP_ORIGEN.toString(),"56736");
                            jsonResponse.put(Parameter.BUY_RESPONSE.DESTINATARIO.toString(),"Usuario demo 2");
                            jsonResponse.put(Parameter.BUY_RESPONSE.DESTINO.toString(),"Ciudad de México, Miguel Hidalgo");
                            jsonResponse.put(Parameter.BUY_RESPONSE.GARANTIA.toString(),"Zona 3, Tercer día hábil");
                            jsonResponse.put(Parameter.BUY_RESPONSE.ORIGEN.toString(),"Edo. de México, Huixquilucan");
                            jsonResponse.put(Parameter.BUY_RESPONSE.REFERENCIA.toString(),"12345678");
                            jsonResponse.put(Parameter.BUY_RESPONSE.REMITENTE.toString(),"Usuario demo 1");
                            jsonResponse.put(Parameter.BUY_RESPONSE.TIPO_SERVICIO.toString(),"2 Días");


                            Map<String,String> response = new HashMap<String,String>();
                            Iterator<?> keys = jsonResponse.keys();
                            while( keys.hasNext() ) {
                                String key = (String)keys.next();
                                response.put(key,jsonResponse.getString(key));
                            }
                            ArrayList<Map<String, String>> responseArray = new ArrayList<>();
                            responseArray.add(response);
                            setResponseArray(responseArray);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                return jsonResponse.toString();

            }


            else {
                try {
                    // List<NameValuePair> credentialsParams = new ArrayList<NameValuePair>(credentials.size());
                    List<NameValuePair> params = new ArrayList<NameValuePair>(requestData.size());

                for (Map.Entry<String, String> entry : credentials.entrySet()) {
                    Log.d("credentials: ",""+entry.getKey()+" ----- "+ entry.getValue().toString());
                    params.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
                }

                for (Map.Entry<String, String> entry : requestData.entrySet())
                    params.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));

                httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
                httppost.setHeader("Content-Type","application/x-www-form-urlencoded");
                //httppost.setHeader("Host",getRequestURL(this.method) );

                //Log.v("Request",httppost.toString());

                HttpResponse response = httpclient.execute(httppost);
                Log.d(LOG_TAG_REQUEST+"response",response.toString());

                    HttpEntity entity = response.getEntity();
                    if (entity != null) {
                        InputStream streamZipCodes = entity.getContent();
                        try {
                            stringResponse = parseToStringZipCodes(streamZipCodes);
                            //responseArray = responseParse(streamZipCodes,this.method);
                            Log.d(LOG_TAG_REQUEST + "Method", ":" + this.method);
                            Log.v(LOG_TAG_REQUEST + "Response", stringResponse);
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
        }

        @Override
        protected void onPostExecute(String stringResponse){
            listener.responseServiceToManager(stringResponse);

        }
    }


    /*Process response*/
    protected ArrayList<Map<String,String>> responseParse(Document doc,METHOD method){
        ArrayList<Map<String,String>> responseArray= new ArrayList<>();
        Log.d("responseParse","inicial");
        switch (method){
            case REQUEST_ZIPCODE:
                try {
                    responseArray = ResponseManager.sharedInstance().parseZipCodes(doc,"1");
                    setResponseArray(responseArray);
                    Log.d("responseParse","ok"+responseArray.size());
                } catch (SAXException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                }
                Log.v(METHOD.REQUEST_ZIPCODE.toString(), "parse");
                break;

            case REQUEST_ZIPCODE_ADDRESSES:
                try {
                    responseArray = ResponseManager.sharedInstance().parseZipCodes(doc,"0");
                    setResponseArray(responseArray);
                    Log.d("responseParse","ok"+responseArray.size());
                } catch (SAXException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                }
                Log.v(METHOD.REQUEST_ZIPCODE_ADDRESSES.toString(), "parse");
                break;

            case REQUEST_TRACKING_LIST_CODES:
                try {
                    responseArray = ResponseManager.sharedInstance().parseGuide(doc);
                    setResponseArray(responseArray);
                    Log.d("responseParse","ok size: "+responseArray.size());
                } catch (SAXException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                }
                Log.v( METHOD.REQUEST_TRACKING_LIST_CODES.toString(), "");
                break;
            case REQUEST_TRACKING_LIST_GUIDES:

                Log.v( METHOD.REQUEST_TRACKING_LIST_GUIDES.toString(), "");
                break;
            case REQUEST_NATIONAL_DELIVERY:
                try {
                    responseArray = ResponseManager.sharedInstance().parseCotizador(doc);
                    setResponseArray(responseArray);
                    Log.d("responseParse", "NATIONAL_DELIVERY");

                    //Log.d("responseParse","ok size: "+responseArray.size());
                } catch (SAXException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                }

                Log.v( METHOD.REQUEST_NATIONAL_DELIVERY.toString(), "");
                break;
            case REQUEST_INTERNATIONAL_DELIVERY:

                try {
                    responseArray = ResponseManager.sharedInstance().parseCotizadorInternational(doc);
                    setResponseArray(responseArray);
                    Log.d("responseParse", "INTERNATIONAL_DELIVERY");

                } catch (SAXException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                }

                //Log.v( METHOD.REQUEST_INTERNATIONAL_DELIVERY.toString(),"");
                break;
            case REQUEST_OFFICES:
                try {
                    responseArray = ResponseManager.sharedInstance().parseOffices(doc);
                    setResponseArray(responseArray);
                    Log.d("responseParse","ok size: "+responseArray.size());
                } catch (SAXException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                }
                Log.v( METHOD.REQUEST_OFFICES.toString(), "");
            case REQUEST_EXCEPTION_CODES:

                Log.v( METHOD.REQUEST_EXCEPTION_CODES.toString(), "");
                break;
            default:
                break;
        }

        return responseArray;
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
        responseArray1 = responseParse(doc,this.method);
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
                Log.v( "Request URL"+METHOD.REQUEST_OFFICES.toString(), url);
                break;


            case REQUEST_EXCEPTION_CODES:
                url =  "http://clavexcs.estafeta.com/RestService.svc/consultaConFechaMovilidadPlano";
                Log.v( "Request URL"+METHOD.REQUEST_EXCEPTION_CODES.toString(), url);
            break;

            default:
                url =  "http://clavexcs.estafeta.com/RestService.svc/consultaConFechaMovilidadPlano";
                break;
        }
        return url;
    }

    private Map<String, String> getCredentials(METHOD type_request){
        Map<String, String> requestData = new HashMap<>();
        switch (type_request){
            case REQUEST_OFFICES:
                requestData.put("idUsuario","1");
                requestData.put("usuario","AdminUser");
                requestData.put("password","zi+Eybk8");

                Log.v( METHOD.REQUEST_OFFICES.toString(), "");

            case REQUEST_EXCEPTION_CODES:
                requestData.put("idUsuario","1");
                requestData.put("usuario","AdminUser");
                requestData.put("password","zi+Eybk8");
                Log.v( METHOD.REQUEST_EXCEPTION_CODES.toString(), "");
                break;

            case REQUEST_ZIPCODE:
                requestData.put("suscriberId", "1");
                requestData.put("login", "AdminUser");
                requestData.put("password", "zi+Eybk8");

                Log.v(METHOD.REQUEST_ZIPCODE.toString(), "");
                break;
            case REQUEST_ZIPCODE_ADDRESSES:
                requestData.put("suscriberId", "1");
                requestData.put("login", "AdminUser");
                requestData.put("password", "zi+Eybk8");
                Log.v(METHOD.REQUEST_ZIPCODE_ADDRESSES.toString(), "");
                break;
            case REQUEST_TRACKING_LIST_CODES:
                requestData.put("suscriberId", "1");
                requestData.put("login", "AdminUser");
                requestData.put("password", "zi+Eybk8");
                Log.v( METHOD.REQUEST_TRACKING_LIST_CODES.toString(), "");
                break;
            case REQUEST_TRACKING_LIST_GUIDES:
                requestData.put("suscriberId", "1");
                requestData.put("login", "AdminUser");
                requestData.put("password", "zi+Eybk8");
                Log.v( METHOD.REQUEST_TRACKING_LIST_GUIDES.toString(), "");
                break;
            case REQUEST_NATIONAL_DELIVERY:
                requestData.put("idUsuario","1");
                requestData.put("usuario","AdminUser");
                requestData.put("contra",",1,B(vVi");
                requestData.put("esFrecuencia","false");
                requestData.put("esLista","true");

                Log.v( METHOD.REQUEST_NATIONAL_DELIVERY.toString(), "");
                break;
            case REQUEST_INTERNATIONAL_DELIVERY:
                requestData.put("idUsuario","1");
                requestData.put("usuario","AdminUser");
                requestData.put("pass","zi+Eybk8");
                requestData.put("modalidad","0");
                requestData.put("medicion","1");
                Log.v( METHOD.REQUEST_INTERNATIONAL_DELIVERY.toString(), "");
                break;


            default:
                requestData.put("idUsuario","1");
                requestData.put("usuario","AdminUser");
                requestData.put("pass","zi+Eybk8");
                requestData.put("modalidad","0");
                requestData.put("medicion","1");
                break;
        }
    return requestData;
    }
}