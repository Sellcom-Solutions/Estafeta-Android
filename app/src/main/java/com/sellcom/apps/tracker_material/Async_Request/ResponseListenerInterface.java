package com.sellcom.apps.tracker_material.Async_Request;

import org.json.JSONObject;

import java.util.Map;

public interface ResponseListenerInterface {
    public void responseServiceToManager(JSONObject jsonResponse);
}
