package com.estafeta.estafetamovilv1.Async_Request;

/**
 * Created by raymundo.piedra on 01/03/15.
 * Help with the response of a dialog decision.
 */
public interface DecisionDialogWithListener {
    public void responseFromDecisionDialog(String confirmMessage, String option);
}
