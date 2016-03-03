package com.estafeta.estafetamovilv1.communication;

import android.os.Bundle;

import java.util.List;
import java.util.Map;

/**
 * Created by hugo.figueroa on 09/11/15.
 */
public interface CommunicationBetweenFragments {

    void setDataSender(Map<String,String> dataSender);
    Map<String,String> getDataSender();

    void setDataSenderQuotation(Map<String,String> dataSender);
    Map<String,String> getDataSenderQuotation();

    //Metodos para guardar datos en prefilledSender
    void temporarilySaveDataPrefilledSender(List<String> listColony, List<String> listCity, List<String> listState);
    Bundle getDataPrefilledSender();
    void clearBundlePrefilledSender();

//    void showMenu(boolean visible);


}
