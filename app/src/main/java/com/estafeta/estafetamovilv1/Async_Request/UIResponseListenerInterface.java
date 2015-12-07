package com.estafeta.estafetamovilv1.Async_Request;

import java.util.Map;

/**
 * It allows communication between a Fragment or Activity and RequestManager.
 */
public interface UIResponseListenerInterface {
    public void prepareRequest(METHOD method, Map<String, String> params, boolean includeCredentials);
    public void decodeResponse(String stringResponse);

}
