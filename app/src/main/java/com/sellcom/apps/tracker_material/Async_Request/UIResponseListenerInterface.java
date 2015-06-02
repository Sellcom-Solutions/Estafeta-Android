package com.sellcom.apps.tracker_material.Async_Request;

import java.util.ArrayList;
import java.util.Map;

public interface UIResponseListenerInterface {
    public void prepareRequest(METHOD method, Map<String, String> params, boolean includeCredentials);
    public void decodeResponse(String stringResponse);

}
