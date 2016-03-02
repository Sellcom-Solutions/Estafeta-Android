package com.estafeta.estafetamovilv1.communication;

import java.util.Map;

/**
 * Created by hugo.figueroa on 09/11/15.
 */
public interface CommunicationBetweenFragments {

    void setDataSender(Map<String,String> dataSender);
    Map<String,String> getDataSender();

    void setDataSenderQuotation(Map<String,String> dataSender);
    Map<String,String> getDataSenderQuotation();

//    void showMenu(boolean visible);


}
