package com.estafeta.estafetamovilv1.Async_Request;

import org.json.JSONObject;

public interface ResponseListenerInterface {
    public void responseServiceToManager(JSONObject jsonResponse, UIResponseListenerInterface responseTo);
    public void responseServiceToManager(String stringResponse,UIResponseListenerInterface responseTo);

}
