package com.estafeta.estafetamovilv1.Async_Request;

import java.io.IOException;
import java.io.InputStream;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.gc.materialdesign.widgets.Dialog;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


/**
 * This class handles calls to the server.
 */
public class RequestManager implements ResponseListenerInterface {

    public  final   boolean                                 TEST_MODE          = true;

    public  final   String 	                                LOG_TAG_MANAGER    = "requestManager";
    public  final   String 	                                LOG_TAG_REQUEST    = "asyncRequest";

    private static 	RequestManager   						manager;
    private         Activity                                activity;
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


    /**
     * Send an error message on a dialog on the screen.
     *
     */
    public void showErrorDialog(String errorMessage, Context context){
        if(TEST_MODE)
            Log.d(LOG_TAG_MANAGER, "Error message: " + errorMessage);

        Dialog  dialog = new Dialog(context,"Error",errorMessage);
        dialog.show();
    }

    //created by jose luis at 27/05/2015
    public void makeRequest(METHOD type_request, Map<String, String> params, UIResponseListenerInterface responseTo){
        this.method = type_request;
        final AsyncRequest asyncRequest =  new AsyncRequest(activity, params, this, method, responseTo);
        //El AsycTask es serializado, esta instrucción es para que ejecute en paralelo si se requiere.
        //asyncRequest.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        asyncRequest.execute();
    }

    /**
     * Receive the response from the server that gets the AsyncRequest.
     */
    @Override
    public void responseServiceToManager(JSONObject jsonResponse, UIResponseListenerInterface responseTo) {
        try {
            if(jsonResponse.getString("success").equalsIgnoreCase("true")){
                // Decode the json object
                if(TEST_MODE)
                    Log.d(LOG_TAG_MANAGER, jsonResponse.toString());
                responseTo.decodeResponse(jsonResponse.toString());
            }
            else{
                showErrorDialog(jsonResponse.getString("resp"),activity);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void responseServiceToManager(String stringReponse, UIResponseListenerInterface responseTo){
        responseTo.decodeResponse(stringReponse);
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
        UIResponseListenerInterface responseTo;
        METHOD method;

        public AsyncRequest(Activity activity, Map<String, String> requestData,
                                   ResponseListenerInterface listener, METHOD method, UIResponseListenerInterface responseTo){
            this.activity =  activity;
            this.requestData = requestData;
            this.listener = listener;
            this.method = method;
            this.responseTo = responseTo;
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

            if(this.method != METHOD.TEST){

                try {

                    try {
                            try {
                                // List<NameValuePair> credentialsParams = new ArrayList<NameValuePair>(credentials.size());
                                List<NameValuePair> params = new ArrayList<NameValuePair>(requestData.size());

                                for (Map.Entry<String, String> entry : credentials.entrySet()) {
                                    params.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
                                }

                                for (Map.Entry<String, String> entry : requestData.entrySet())
                                    params.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));

                                httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
                                httppost.setHeader("Content-Type", "application/x-www-form-urlencoded");
                                //httppost.setHeader("Host",getRequestURL(this.method) );


                                HttpResponse response = httpclient.execute(httppost);
                                if(TEST_MODE)
                                    Log.d(LOG_TAG_REQUEST + "response", response.toString());


                                HttpEntity entity = response.getEntity();

                                if (entity != null) {
                                    InputStream streamZipCodes = entity.getContent();
                                    try {
                                        stringResponse = parseToStringZipCodes(streamZipCodes);
                                        if(TEST_MODE) {
                                            Log.d(LOG_TAG_REQUEST + "Method", ":" + this.method);
                                            Log.v(LOG_TAG_REQUEST + "Response", stringResponse);
                                        }
                                    } catch (SAXException e) {
                                        e.printStackTrace();
                                    } catch (ParserConfigurationException e) {
                                        e.printStackTrace();
                                    }

                                }

                            } catch (ConnectTimeoutException cte) {
                                if (this.method == METHOD.REQUEST_TRACKING_LIST_CODES || this.method == METHOD.REQUEST_TRACKING_LIST_GUIDES) {
                                    stringResponse = "falla_timeOut";
                                    return stringResponse;
                                }
                            }

                        } catch (SocketTimeoutException ste) {
                            if (this.method == METHOD.REQUEST_TRACKING_LIST_CODES || this.method == METHOD.REQUEST_TRACKING_LIST_GUIDES) {
                                stringResponse = "falla_timeOut";
                                return stringResponse;
                            }
                        }


                    } catch (ClientProtocolException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                return stringResponse;
            }
            return stringResponse;
        }

        @Override
        protected void onPostExecute(String stringResponse){
            listener.responseServiceToManager(stringResponse,responseTo);

        }
    }


    /**
     *
     * The server response is processed according to the service.
     *
     * @return
     */
    protected ArrayList<Map<String,String>> responseParse(Document doc,METHOD method){
        ArrayList<Map<String,String>> responseArray= new ArrayList<>();
        if(TEST_MODE)
            Log.d("responseParse","inicial");
        switch (method){
            case REQUEST_ZIPCODE:
                try {
                    responseArray = ResponseManager.sharedInstance().parseZipCodes(doc,"1");
                    setResponseArray(responseArray);
                } catch (SAXException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                }
                if(TEST_MODE)
                    Log.v(METHOD.REQUEST_ZIPCODE.toString(), "parse");
                break;

            case REQUEST_ZIPCODE_ADDRESSES:
                try {
                    responseArray = ResponseManager.sharedInstance().parseZipCodes(doc,"0");
                    setResponseArray(responseArray);
                } catch (SAXException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                }
                if(TEST_MODE)
                    Log.v(METHOD.REQUEST_ZIPCODE_ADDRESSES.toString(), "parse");
                break;

            case REQUEST_TRACKING_LIST_CODES:
                try {
                    responseArray = ResponseManager.sharedInstance().parseGuide(doc);
                    setResponseArray(responseArray);
                } catch (SAXException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                }
                if(TEST_MODE)
                    Log.v( METHOD.REQUEST_TRACKING_LIST_CODES.toString(), "");
                break;
            case REQUEST_TRACKING_LIST_GUIDES:
                if(TEST_MODE)
                    Log.v( METHOD.REQUEST_TRACKING_LIST_GUIDES.toString(), "");
                break;
            case REQUEST_NATIONAL_DELIVERY:
                try {
                    responseArray = ResponseManager.sharedInstance().parseCotizador(doc);
                    setResponseArray(responseArray);

                } catch (SAXException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                }
                if(TEST_MODE)
                    Log.v( METHOD.REQUEST_NATIONAL_DELIVERY.toString(), "");
                break;
            case REQUEST_INTERNATIONAL_DELIVERY:

                try {
                    responseArray = ResponseManager.sharedInstance().parseCotizadorInternational(doc);
                    setResponseArray(responseArray);

                } catch (SAXException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                }

                break;
            case REQUEST_OFFICES:
                try {

                    responseArray = ResponseManager.sharedInstance().parseOffices(doc);
                    setResponseArray(responseArray);
                    setResponseArray(responseArray);;
                } catch (SAXException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                }
                break;
            case REQUEST_EXCEPTION_CODES:
                try {

                    responseArray = ResponseManager.sharedInstance().ExceptionCodes(doc);
                    setResponseArray(responseArray);

                } catch (SAXException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                }

                break;
            default:
                break;
        }

        return responseArray;
    }
    /**
     * This method sends a doc with the SOAP response service for processing.
     *
     * @param
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

    /**
     * Addresses services Estafeta.
     * @param type_request Type of service that summons.
     * @return
     */
    private String getRequestURL(METHOD type_request){
        String url = null;
        switch (type_request){
            case REQUEST_ZIPCODE:
                url=  "http://validacpscs.estafeta.com/RestService.svc/ConsultaCP";
                if(TEST_MODE)
                    Log.v(METHOD.REQUEST_ZIPCODE.toString(), url);
            break;

            case REQUEST_ZIPCODE_ADDRESSES:
                url =  "http://validacpscs.estafeta.com/RestService.svc/ConsultaDatosCP";
                if(TEST_MODE)
                    Log.v(METHOD.REQUEST_ZIPCODE_ADDRESSES.toString(), url);
            break;

            case REQUEST_TRACKING_LIST_CODES:
                url =  "http://trackingcs.estafeta.com/RestService.svc/ExecuteQueryPlano";
                if(TEST_MODE)
                    Log.v( METHOD.REQUEST_TRACKING_LIST_CODES.toString(), url);
            break;

            case REQUEST_TRACKING_LIST_GUIDES:
                url =  "http://trackingcs.estafeta.com/RestService.svc/ExecuteQueryPlano";
                if(TEST_MODE)
                    Log.v( METHOD.REQUEST_TRACKING_LIST_GUIDES.toString(), url);
            break;

            case REQUEST_NATIONAL_DELIVERY:
                url =  "http://frecuenciacotizadorcs.estafeta.com/RestService.svc/FrecuenciaCotizadorPlano";
                if(TEST_MODE)
                    Log.v( METHOD.REQUEST_NATIONAL_DELIVERY.toString(), url);
            break;

            case REQUEST_INTERNATIONAL_DELIVERY:
                url =  "http://cotizadorintcs.estafeta.com/RestService.svc/CotizaPlano";
                if(TEST_MODE)
                    Log.v( METHOD.REQUEST_INTERNATIONAL_DELIVERY.toString(), url);
                break;

            case REQUEST_OFFICES:
                url = "http://sucursalescs.estafeta.com/RestService.svc/consultaConFechaPlano";
                if(TEST_MODE)
                    Log.v( "Request URL"+METHOD.REQUEST_OFFICES.toString(), url);
                break;


            case REQUEST_EXCEPTION_CODES:
                url =  "http://clavexcs.estafeta.com/RestService.svc/consultaConFechaMovilidadPlano";
                if(TEST_MODE)
                    Log.v( "Request URL"+METHOD.REQUEST_EXCEPTION_CODES.toString(), url);
            break;

            default:
                url =  "http://clavexcs.estafeta.com/RestService.svc/consultaConFechaMovilidadPlano";
                break;
        }
        return url;
    }

    /**
     * This method assigns the default credentials required by each service.
     * @param type_request Type of service that summons.
     * @return
     */
    private Map<String, String> getCredentials(METHOD type_request){
        Map<String, String> requestData = new HashMap<>();
        switch (type_request){
            case REQUEST_OFFICES:
                requestData.put("idUsuario","1");
                requestData.put("usuario","AdminUser");
                requestData.put("password", "zi+Eybk8");

                if(TEST_MODE)
                    Log.v(METHOD.REQUEST_OFFICES.toString(), "");

            case REQUEST_EXCEPTION_CODES:
                requestData.put("idUsuario","1");
                requestData.put("usuario","AdminUser");
                requestData.put("password", "zi+Eybk8");
                if(TEST_MODE)
                    Log.v(METHOD.REQUEST_EXCEPTION_CODES.toString(), "");
                break;

            case REQUEST_ZIPCODE:
                requestData.put("suscriberId", "1");
                requestData.put("login", "AdminUser");
                requestData.put("password", "zi+Eybk8");

                if(TEST_MODE)
                    Log.v(METHOD.REQUEST_ZIPCODE.toString(), "");
                break;
            case REQUEST_ZIPCODE_ADDRESSES:
                requestData.put("suscriberId", "1");
                requestData.put("login", "AdminUser");
                requestData.put("password", "zi+Eybk8");
                if(TEST_MODE)
                    Log.v(METHOD.REQUEST_ZIPCODE_ADDRESSES.toString(), "");
                break;
            case REQUEST_TRACKING_LIST_CODES:
                requestData.put("suscriberId", "1");
                requestData.put("login", "AdminUser");
                requestData.put("password", "zi+Eybk8");
                if(TEST_MODE)
                    Log.v(METHOD.REQUEST_TRACKING_LIST_CODES.toString(), "");
                break;
            case REQUEST_TRACKING_LIST_GUIDES:
                requestData.put("suscriberId", "1");
                requestData.put("login", "AdminUser");
                requestData.put("password", "zi+Eybk8");
                if(TEST_MODE)
                    Log.v(METHOD.REQUEST_TRACKING_LIST_GUIDES.toString(), "");
                break;
            case REQUEST_NATIONAL_DELIVERY:
                requestData.put("idUsuario","1");
                requestData.put("usuario","AdminUser");
                requestData.put("contra",",1,B(vVi");
                requestData.put("esFrecuencia","false");
                requestData.put("esLista", "true");

                if(TEST_MODE)
                    Log.v(METHOD.REQUEST_NATIONAL_DELIVERY.toString(), "");
                break;
            case REQUEST_INTERNATIONAL_DELIVERY:
                requestData.put("idUsuario","1");
                requestData.put("usuario","AdminUser");
                requestData.put("pass","zi+Eybk8");
                requestData.put("modalidad","0");
                requestData.put("medicion", "0");
                if(TEST_MODE)
                    Log.v(METHOD.REQUEST_INTERNATIONAL_DELIVERY.toString(), "");
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