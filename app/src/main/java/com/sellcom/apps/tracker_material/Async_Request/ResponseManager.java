package com.sellcom.apps.tracker_material.Async_Request;

import android.app.Activity;
import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import database.model.Codes;

/**
 * Created by rebecalopezmartinez on 02/06/15.
 */
public class ResponseManager {

    private static ResponseManager manager;
    private Activity activity;

    private ResponseManager(){

    }

    public static synchronized ResponseManager sharedInstance(){
        if (manager == null)
            manager = new ResponseManager();
        return manager;
    }

    public void                         setActivity(Activity activity)  {this.activity    = activity;}
    public Activity                     getActivity()                   {return activity;}


    public ArrayList<Map<String,String>> parseZipCodes(Document doc, String tipo) throws SAXException, IOException, ParserConfigurationException {
        ArrayList<Map<String ,String>> codigoMap =new ArrayList<>();
        Map<String,String> map  = new HashMap<String, String>();

        doc.getDocumentElement().normalize();
        NodeList respDoc;
        if(tipo.equals("0"))
            respDoc = doc.getElementsByTagName("ConsultaDatosCPResult");
        else
            respDoc = doc.getElementsByTagName("ConsultaCPResult");

        if(respDoc == null || respDoc.getLength() == 0 ){
            return null;
        }
        Node node = null;
        node = respDoc.item(0);
        NodeList all = node.getChildNodes();
        //Log.d("Node size",":"+all.getLength());
        Node nodeitem;
        String nodeName;
        for (int i = 0; i < all.getLength(); i++) {
            nodeitem = all.item(i);
            nodeName = nodeitem.getNodeName();
            //Log.d("Node name", nodeName);
            if (nodeitem.hasChildNodes()) {
                Node subNode = nodeitem.getFirstChild();
               // Log.d("Node child", "true");
                //Log.d("Node name sub", subNode.getNodeName());
                String nodeValue = "";
                if (subNode.getNodeValue() != null)
                    nodeValue = subNode.getNodeValue();
                if ("HasError".equals(nodeName)) {
                    if (nodeValue.equals("true"))
                        map.put("HasError", "true");
                    else
                        map.put("Has error", "false");
                    continue;
                }

                if (nodeName.equals("ListadoCodigo")) {
                    NodeList listColonias = nodeitem.getChildNodes();
                    Node eachCP;
                    NodeList eachServNode;
                    Log.d("Lista cod", "size" + listColonias.getLength());

                    for (int f = 0; f < listColonias.getLength(); f++) {
                        eachCP = listColonias.item(f);
                        eachServNode = eachCP.getChildNodes();
                        //Log.d("eachCP", eachCP.getNodeName());
                        Map<String,String> map1  = new HashMap<String, String>();

                        for (int d = 0; d < eachServNode.getLength(); d++) {
                            String charac = eachServNode.item(d).getNodeName();
                            String nodeValue2 = eachServNode.item(d).getFirstChild()
                                    .getNodeValue();
                         //   Log.d("Node char", charac);
                         //   Log.d("Node value", nodeValue2);
                            if(charac.equals("a:CP")){
                                map1.put("cp",nodeValue2);
                            }
                            if (charac.equals("a:Ciudad")){
                                map1.put("ciudad",nodeValue2);
                            }
                            if (charac.equals("a:Colonia")){
                                map1.put("colonia",nodeValue2);
                            }
                            if (charac.equals("a:Estado")){
                                map1.put("estado",nodeValue2);
                            }
                            if (charac.equals("a:Pais")){
                                map1.put("pais",nodeValue2);
                            }
                        }

                        codigoMap.add(map1);
                    }
                }
            }
        }
        Log.d("ArrayList","tam:"+codigoMap.size());
        /*for(int i=0;i<codigoMap.size();i++)
            Log.d("ArrayList",codigoMap.get(i).get("colonia"));*/
        return codigoMap;
    }

    public ArrayList<Map<String,String>> parseGuide(Document doc) throws SAXException, IOException, ParserConfigurationException {
        ArrayList<Map<String,String>>  data = new ArrayList<>();
        Map<String,String> map  = new HashMap<String, String>();


        NodeList list = doc.getElementsByTagName("a:TrackingData");
        Node node = null;
        String value="";
        String nodeName;
        Node nodoItem;
        Node subNode;
        int i = 0, j = 0;
        // Every TrackingData
        for (i = 0; i < list.getLength(); i++) {
            node = list.item(i);
            NodeList list2 = node.getChildNodes();
            // Every element inside TrackingData
            for (j = 0; j < list2.getLength(); j++) {
                nodoItem=list2.item(j);
                nodeName = nodoItem.getNodeName();
                Log.d("nodeName",""+nodeName);

                if(!nodeName.startsWith("#"))
                    if (nodeName != null) {
                        //System.out.println(nodeName);
                        subNode=nodoItem.getFirstChild();

                        if(subNode==null){
                            continue;
                        }else
                            //Log.d("nodeNameSub",subNode.getNodeName());

                        value=subNode.getNodeValue();
                        //Log.d("subNode","value: "+value);

                        if ("a:waybill".equals(nodeName)) {
                            map.put("wayBill",value);
                            Log.d("waybill", ""+value);
                        }
                        if ("a:shortWaybillId".equals(nodeName)) {
                            map.put("shortWayBillId",value);
                            Log.d("shortWayBillId", ""+value);
                        }
                        if ("a:serviceId".equals(nodeName)) {
                            map.put("serviceId",value);
                            Log.d("serviceId", ""+value);
                        }
                        if ("a:serviceDescriptionSPA".equals(nodeName)) {
                            map.put("serviceDescriptionSPA",value);
                            Log.d("serviceDescriptionSPA", ""+value);
                        }
                        if ("a:serviceDescriptionENG".equals(nodeName)) {
                            map.put("serviceDescriptionENG",value);
                            Log.d("serviceDescriptionENG", ""+value);
                        }
                        if ("a:customerNumber".equals(nodeName)) {
                            map.put("customerNumber",value);
                            Log.d("customerNumber", ""+value);
                        }
                        if ("a:packageType".equals(nodeName)) {
                            map.put("packageType",value);
                            Log.d("packageType", ""+value);
                        }
                        if ("a:additionalInformation".equals(nodeName)) {
                            map.put("additionalInformation",value);
                            Log.d("additionalInformation", ""+value);
                        }
                        if ("a:statusSPA".equals(nodeName)) {
                            map.put("statusSPA",value);
                            Log.d("statusSPA", ""+value);
                        }
                        if ("a:statusENG".equals(nodeName)) {
                            map.put("statusENG",value);
                            Log.d("statusENG", ""+value);
                        }
                        if ("a:signature".equals(nodeName)) {
                            map.put("signature", value);
                            Log.d("signature", ""+value);
                        }

                        ////
                        if ("a:pickupData".equals(nodeName)) {
                            NodeList listpk=list2.item(j).getChildNodes();
                            String nameElement="";
                            String valuePk="";

                            for (int l = 0; l < listpk.getLength(); l++) {
                                Node dataNode = listpk.item(l);
                                nameElement = dataNode.getNodeName();
                                Log.d("nameElement PK",""+nameElement);
                                if(!nameElement.startsWith("#"))
                                    if (nameElement != null) {
                                        Node subNodepk=dataNode.getFirstChild();
                                        if(subNodepk==null){
                                            continue;
                                        }
                                        valuePk=subNodepk.getNodeValue();
                                        if (nameElement.equals("originAcronym")) {
                                            map.put("PK_originAcronym",valuePk);
                                            Log.d("PK_originAcronym", ""+valuePk);
                                            continue;
                                        }
                                        if (nameElement.equals("a:originName")) {
                                            map.put("PK_originName",valuePk);
                                            Log.d("PK_originName", ""+valuePk);
                                            continue;
                                        }
                                        if (nameElement.equals("a:pickupDateTime")) {
                                            map.put("PK_pickupDateTime",valuePk);
                                            Log.d("PK_pickupDateTime", ""+valuePk);
                                            continue;
                                        }
                                    }
                            }
                           continue;
                        }
                        if ("a:deliveryData".equals(nodeName)) {
                            NodeList listdd=list2.item(j).getChildNodes();
                            String nameElement="";
                            String valueDD="";

                            for (int l = 0; l < listdd.getLength(); l++) {
                                Node dataNode = listdd.item(l);
                                nameElement = dataNode.getNodeName();
                                Log.d("nameElement DD",""+nameElement);
                                if(!nameElement.startsWith("#"))
                                    if (nameElement != null) {
                                        Node subNodeDD=dataNode.getFirstChild();
                                        if(subNodeDD==null){
                                            continue;
                                        }
                                        valueDD=subNodeDD.getNodeValue();
                                        if (nameElement.equals("a:destinationAcronym")) {
                                            map.put("DD_destinationAcronym",valueDD);
                                            Log.d("DD_destinationAcronym",""+ valueDD);
                                            continue;
                                        }
                                        if (nameElement.equals("a:destinationName")) {
                                            map.put("DD_destinationName",valueDD);
                                            Log.d("DD_destinationName", ""+valueDD);
                                            continue;
                                        }
                                        if (nameElement.equals("a:deliveryDateTime")) {
                                            map.put("DD_deliveryDateTime",valueDD);
                                            Log.d("DD_deliveryDateTime", ""+valueDD);
                                            continue;
                                        }
                                        if (nameElement.equals("a:zipCode")) {
                                            map.put("DD_zipCode",valueDD);
                                            Log.d("DD_zipCode", ""+valueDD);
                                            continue;
                                        }
                                        if (nameElement.equals("a:receiverName")) {
                                            map.put("DD_receiverName",valueDD);
                                            Log.d("DD_receiverName", ""+valueDD);
                                            continue;
                                        }
                                    }
                            }
                           continue;
                        }

                        if ("a:dimensions".equals(nodeName)) {
                            NodeList listD=list2.item(j).getChildNodes();
                            String nameElement="";
                            String valueD="";
                            for (int l = 0; l < listD.getLength(); l++) {
                                Node dataNode = listD.item(l);
                                nameElement = dataNode.getNodeName();
                                Log.d("nameElement Dim",""+nameElement);
                                if(!nameElement.startsWith("#"))
                                    if (nameElement != null) {
                                        Node subNodeD=dataNode.getFirstChild();
                                        if(subNodeD==null){
                                            continue;
                                        }
                                        valueD=subNode.getNodeValue();
                                        if (nameElement.equals("a:weight")) {
                                            map.put("Dim_weight",valueD);
                                            Log.d("Dim_weight", ""+valueD);
                                            continue;
                                        }
                                        if (nameElement.equals("a:volumetricWeight")) {
                                            map.put("Dim_volumetricWeight",valueD);
                                            Log.d("Dim_volumetricWeight", ""+valueD);
                                            continue;
                                        }
                                        if (nameElement.equals("a:width")) {
                                            map.put("Dim_width",valueD);
                                            Log.d("Dim_width", ""+valueD);
                                            continue;
                                        }
                                        if (nameElement.equals("a:length")) {
                                            map.put("Dim_length",valueD);
                                            Log.d("Dim_length", ""+valueD);
                                            continue;
                                        }
                                        if (nameElement.equals("a:height")) {
                                            map.put("Dim_height",valueD);
                                            Log.d("Dim_height", ""+valueD);
                                            continue;
                                        }
                                    }
                            }
                            continue;
                        }
                        if ("a:customerInfo".equals(nodeName)) {
                            NodeList listCI = list2.item(j).getChildNodes();
                            String nameElement="";
                            String valueCI="";

                            for (int l = 0; l < listCI.getLength(); l++) {
                                Node dataNode = listCI.item(l);
                                nameElement = dataNode.getNodeName();
                                Log.d("nameElement CI",""+nameElement);
                                if(!nameElement.startsWith("#"))
                                    if (nameElement != null) {
                                        Node subNodeCI=dataNode.getFirstChild();
                                        if(subNodeCI == null){
                                            continue;
                                        }
                                        valueCI=subNode.getNodeValue();
                                        if (nameElement.equals("a:reference")) {
                                            map.put("CI_reference", valueCI);
                                            Log.d("CI_reference", ""+valueCI);
                                            continue;
                                        }
                                        if (nameElement.equals("a:costsCentre")) {
                                            map.put("CI_costsCentre",valueCI);
                                            Log.d("CI_costsCentre", ""+valueCI);
                                            continue;
                                        }
                                    }
                            }


                            continue;
                        }
                        if ("a:history".equals(nodeName)) {
                            NodeList listHistory = list2.item(j).getChildNodes();
                            String nameElement = null;
                            String valueH = "";
                            for (int k = 0; k < listHistory.getLength(); k++) {
                                Map<String,String> list_history = new HashMap<>();
                                Node nodeHist = listHistory.item(k);
                                String name = nodeHist.getNodeName();
                                Log.e("name History", "" + name);
                                if(!name.startsWith("#"))
                                    if (name != null) {
                                        NodeList historyData = nodeHist.getChildNodes();
                                        // Every element inside History
                                        for (int l = 0; l < historyData.getLength(); l++) {
                                            Node dataNode = historyData.item(l);
                                            nameElement = dataNode.getNodeName();
                                            //Log.e("nameElement History",""+nameElement);
                                            if(!nameElement.startsWith("#"))
                                                if (nameElement != null) {
                                                    Node subNodeH=dataNode.getFirstChild();
                                                    if(subNodeH==null){
                                                        continue;
                                                    }
                                                    value=subNodeH.getNodeValue();
                                                    if (nameElement.equals("a:eventDateTime")) {
                                                        list_history.put("H_eventDateTime",value);
                                                        Log.v("H_eventDateTime", ""+value);
                                                        //history.setEventDateTime(value);
                                                        continue;
                                                    }
                                                    if (nameElement.equals("a:eventId")) {
                                                        list_history.put("H_eventId",value);
                                                        Log.v("H_eventId", ""+value);
                                                        continue;
                                                    }
                                                    if (nameElement.equals("a:eventDescriptionSPA")) {
                                                        list_history.put("H_eventDescriptionSPA",value);
                                                        Log.v("H_eventDescriptionSPA", ""+value);
                                                        continue;
                                                    }
                                                    if (nameElement.equals("a:eventDescriptionENG")) {
                                                        list_history.put("H_eventDescriptionENG",value);
                                                        Log.v("H_eventDescriptionENG", ""+value);
                                                        continue;
                                                    }
                                                    if (nameElement.equals("a:eventPlaceAcronym")) {
                                                        list_history.put("H_eventPlaceAcronym",value);
                                                        Log.v("H_eventPlaceAcronym", ""+value);
                                                        continue;
                                                    }
                                                    if (nameElement.equals("a:eventPlaceName")) {
                                                        list_history.put("H_eventPlaceName",value);
                                                        Log.v("H_eventPlaceName", ""+value);
                                                        continue;
                                                    }
                                                    if (nameElement.equals("a:exceptionCode")) {
                                                        list_history.put("H_exceptionCode",value);
                                                        Log.v("H_exceptionCode",""+ value);
                                                        continue;
                                                    }
                                                    if (nameElement.equals("a:exceptionCodeDescriptionSPA")) {
                                                        list_history.put("H_exceptionCodeDescriptionSPA",value);
                                                        Log.v("H_exceptionCodeDescriptionSPA", ""+value);
                                                        continue;
                                                    }
                                                    if (nameElement.equals("a:exceptionCodeDescriptionENG")) {
                                                        list_history.put("H_exceptionCodeDescriptionENG",value);
                                                        Log.v("H_exceptionCodeDescriptionENG", ""+value);
                                                        continue;
                                                    }
                                                    if (nameElement.equals("a:exceptionCodeDetails")) {
                                                        list_history.put("H_exceptionCodeDetails",value);
                                                        Log.v("H_exceptionCodeDetails", ""+value);
                                                        continue;
                                                    }
                                                }
                                        }
                                        data.add(list_history);
                                    }

                                //data.add(list_history);
                            }


                        }
                    }
            }

        }

        data.add(0, map);

        if (data.size() <0)
            return null;
        else {
           /* for (int ii = 0; ii < data.size(); ii++)
                Log.d("ArrayList", data.get(ii).get("wayBill"));*/
            return data;
        }
    }


    public ArrayList<Map<String,String>> parseOffices(Document doc) throws SAXException, IOException, ParserConfigurationException{

        ArrayList<Map<String ,String>> officeMap =new ArrayList<>();
        doc.getDocumentElement().normalize();

        NodeList sucursales = doc.getElementsByTagName("a:Sucursal");

        if(sucursales!=null){
            if(sucursales.getLength()==0){
                return null;
            }
        }
        Node node = null;
        String value="";
        for (int i = 0; i < sucursales.getLength(); i++) {
            node = sucursales.item(i);
            Map<String,String> map  = new HashMap<String, String>();
            NodeList list2 = node.getChildNodes();

            //Log.d("Node size",":"+list2.getLength());
            for (int j = 0; j < list2.getLength(); j++) {
                Node nodoItem=list2.item(j);
                String nodeName = nodoItem.getNodeName();
                //Log.d("Node name", nodeName);

                if(!nodeName.startsWith("#"))
                    if (nodeName != null) {
                        Node subNode=nodoItem.getFirstChild();
                       /* Log.d("Node child", "true");
                        Log.d("Node name sub", subNode.getNodeName());*/

                        if(subNode==null){
                            continue;
                        }
                        value=subNode.getNodeValue();
                        //Log.d("Node value", value);

                        if ("a:calle1".equals(nodeName)) {
                            map.put("calle1", value);
                            continue;
                        }
                        if ("a:calle2".equals(nodeName)) {
                            map.put("calle2", value);
                            continue;
                        }
                        if ("a:ciudad".equals(nodeName)) {
                            map.put("ciudad", value);
                            continue;
                        }
                        if ("a:codigoPostal".equals(nodeName)) {
                            map.put("codigoPostal", value);
                            continue;
                        }
                        if ("a:colonia".equals(nodeName)) {
                            map.put("colonia", value);
                            continue;
                        }
                        if ("a:correoE".equals(nodeName)) {
                            map.put("correoE", value);
                            continue;
                        }
                        if ("a:entregaOcurre".equals(nodeName)) {
                            map.put("entregaOcurre", value);
                            continue;
                        }
                        if ("a:estado".equals(nodeName)) {
                            int val=0;

                            if (value.equalsIgnoreCase("BAJA CALIFORNIA")) {
                                value = "Baja California";
                            }
							else if (value.equalsIgnoreCase("EDO. DE MEXICO")) {
								value = "Estado de Mexico";
							}
                            else if (value.equalsIgnoreCase("DISTRITO FEDERAL")) {
                                value = "México, D.F.";
                            }

                            map.put("estado_name",value);
                            continue;
                        }
                        if ("a:ext1".equals(nodeName)) {
                            map.put("ext1",value);
                            continue;
                        }
                        if ("a:ext2".equals(nodeName)) {
                            map.put("ext2",value);
                            continue;
                        }
                        if ("a:horarioComida".equals(nodeName)) {
                            map.put("horarioComida",value);
                            continue;
                        }
                        if ("a:horarioExtendido".equals(nodeName)) {
                            map.put("horarioExtendido",value);
                            continue;
                        }
                        if ("a:horarioSabatino".equals(nodeName)) {
                            map.put("horarioSabatino",value);
                            continue;
                        }
                        if ("a:horariosAtencion".equals(nodeName)) {
                            map.put("horariosAtencion",value);
                            continue;
                        }
                        if ("a:idOficina".equals(nodeName)) {
                            map.put("idOficina",value);
                            continue;
                        }
                        if ("a:idTipoOficina".equals(nodeName)) {
                            map.put("idTipoOficina",value);
                            continue;
                        }
                        if ("a:latitud".equals(nodeName)) {
                            //value = value.trim();
                            String auxla = value.replaceAll("\\s+","");
                            map.put("latitud",auxla);
                            //Log.d("latitud", value);
                            continue;
                        }
                        if ("a:longitud".equals(nodeName)) {
                            //value = value.trim();
                            String auxlo = value.replaceAll("\\s+","");
                            map.put("longitud",auxlo);
                            continue;
                        }
                        if ("a:nombreOficina".equals(nodeName)) {
                            map.put("nombreOficina",value);
                            continue;
                        }
                        if ("a:telefono1".equals(nodeName)) {
                            map.put("telefono1",value);
                            continue;
                        }
                        if ("a:telefono2".equals(nodeName)) {
                            map.put("telefono2",value);
                            continue;
                        }
                        if ("a:tiposPago".equals(nodeName)) {
                            map.put("tiposPago",value);
                            continue;
                        }
                        if("a:status".equals(nodeName)){
                            map.put("status",value);
                            //Log.d("status:",value);
                            continue;
                        }
                        if ("a:ultimaAct".equals(nodeName)) {
                            map.put("ultimaAct",value);
                            continue;

                        }
                    }
            }
            map.put("method",METHOD.REQUEST_OFFICES.toString());
            officeMap.add(map);
           // data.addElement(sucursal);

        }
        if(officeMap.size()>0){
           /* for(int i=0;i<officeMap.size();i++)
            Log.d("ArrayList",officeMap.get(i).get("nombreOficina"));*/
            Log.d("Response Manager","endMayor que 0");
            return officeMap;
        }
        Log.d("Response Manager", "end");
        return null;
    }



    public ArrayList<Map<String,String>> parseCotizador(Document doc) throws SAXException, IOException, ParserConfigurationException {
        ArrayList<Map<String, String>> cotizadorMap = new ArrayList<>();
        Map<String,String> map  = new HashMap<String, String>();
        Node nodeitem;
        String nodeValue = "";
        String nodeName = "";
        NodeList list = doc.getElementsByTagName("a:Respuesta");
        if (list != null) {
            if (list.getLength() == 0) {
                return null;
            }
        }

        Node node = null;
        node = list.item(0);
        NodeList all = node.getChildNodes();

        nodeitem = all.item(5);
        nodeName = nodeitem.getNodeName();
        Log.d("Node name", nodeName);
            if (nodeitem.hasChildNodes()) {

                Node error = nodeitem.getFirstChild();
                if (error.getNodeValue() != null)
                    nodeValue = error.getNodeValue();

                if ("a:Error".equals(nodeName)) {

                    if(!nodeValue.equals("000")){
                        map.put("Error", nodeValue);
                        Log.v("Error", nodeValue);
                        cotizadorMap.add(map);
                        return cotizadorMap;
                    }else{
                        map.put("Error", nodeValue);
                        Log.v("Error", nodeValue);
                    }
                }

            }



        for (int i = 0; i < all.getLength(); i++) {

            nodeitem = all.item(i);
            nodeName = nodeitem.getNodeName();
            Log.d("Node name", nodeName);
            if (!nodeName.startsWith("#"))
                if (nodeitem.hasChildNodes()) {

                    Node subNode = nodeitem.getFirstChild();


                    if (subNode.getNodeValue() != null)
                        nodeValue = subNode.getNodeValue();

                    if ("a:CodigoPosOri".equals(nodeName)) {
                        map.put("CodigoPosOri", nodeValue);
                        Log.v("CodigoPosOri", nodeValue);
                        continue;
                    }

                    if ("a:Destino".equals(nodeName)) {
                        NodeList destinysChild = nodeitem.getChildNodes();
                        for (int d = 0; d < destinysChild.getLength(); d++) {
                            String destiny = destinysChild.item(d)
                                    .getNodeName();
                            String nodeValue2 = destinysChild.item(d)
                                    .getFirstChild().getNodeValue();
                            if ("a:CpDestino".equals(destiny)) {
                                map.put("CpDestino", nodeValue2);
                                Log.v("CpDestino", nodeValue2);
                                continue;
                            }
                            if ("a:Estado".equals(destiny)) {
                                if (destinysChild.item(d).hasChildNodes())
                                    map.put("Estado", nodeValue2);
                                Log.v("Estado", nodeValue2);
                                continue;
                            }
                            if ("a:Municipio".equals(destiny)) {
                                if (destinysChild.item(d).hasChildNodes())
                                    map.put("Municipio", nodeValue2);
                                Log.v("Municipio", nodeValue2);
                                continue;
                            }
                            if("a:Plaza1".equals(destiny)){
                                if(destinysChild.item(d).hasChildNodes())
                                    map.put("Plaza1", nodeValue2);
                                Log.v("Plaza1", nodeValue2);
                                continue;
                            }
                        }
                        continue;
                    }
                    if ("a:CostoReexpedicion".equals(nodeName)) {
                        map.put("CostoReexpedicion", nodeValue);
                        Log.v("CostoReexpedicion", nodeValue);
                        continue;
                    }
                    if ("a:DiasEntrega".equals(nodeName)) {
                        NodeList days = nodeitem.getChildNodes();
                        for (int d = 0; d < days.getLength(); d++) {
                            String dayName = days.item(d).getNodeName();
                            String nodeValue2 = days.item(d)
                                    .getFirstChild().getNodeValue();
                            if ("a:Lunes".equals(dayName)) {
                                if (days.item(d).hasChildNodes())
                                    map.put("Lunes", nodeValue2);
                                Log.v("Lunes", nodeValue2);
                                continue;
                            }
                            if ("a:Martes".equals(dayName)) {
                                if (days.item(d).hasChildNodes())
                                    map.put("Martes", nodeValue2);
                                Log.v("Martes", nodeValue2);
                                continue;
                            }
                            if ("a:Miercoles".equals(dayName)) {
                                if (days.item(d).hasChildNodes())
                                    map.put("Miercoles", nodeValue2);
                                Log.v("Miercoles", nodeValue2);
                                continue;
                            }
                            if ("a:Jueves".equals(dayName)) {
                                if (days.item(d).hasChildNodes())
                                    map.put("Jueves", nodeValue2);
                                Log.v("Jueves", nodeValue2);
                                continue;
                            }
                            if ("a:Viernes".equals(dayName)) {
                                if (days.item(d).hasChildNodes())
                                    map.put("Viernes", nodeValue2);
                                Log.v("Viernes", nodeValue2);
                                continue;
                            }
                            if ("a:Sabado".equals(dayName)) {
                                if (days.item(d).hasChildNodes())
                                    map.put("Sabado", nodeValue2);
                                Log.v("Sabado", nodeValue2);
                                continue;
                            }
                            if ("a:Domingo".equals(dayName)) {
                                if (days.item(d).hasChildNodes())
                                    map.put("Domingo", nodeValue2);
                                Log.v("Domingo", nodeValue2);
                                continue;
                            }
                        }
                        continue;
                    }

                    if ("a:Origen".equals(nodeName)) {
                        NodeList originsChild = nodeitem.getChildNodes();
                        for (int d = 0; d < originsChild.getLength(); d++) {
                            String origin = originsChild.item(d)
                                    .getNodeName();
                            String nodeValue2 = originsChild.item(d)
                                    .getFirstChild().getNodeValue();

                            if ("a:CodigoPosOri".equals(origin)) {
                                map.put("CodigoPosOri", nodeValue2);
                                Log.v("CodigoPosOri", nodeValue2);
                                continue;
                            }

                            if ("a:MunicipioOri".equals(origin)) {
                                map.put("MunicipioOri", nodeValue2);
                                Log.v("MunicipioOri", nodeValue2);
                                continue;
                            }
                            if ("a:EstadoOri".equals(origin)) {
                                if (originsChild.item(d).hasChildNodes())
                                    map.put("EstadoOri", nodeValue2);
                                Log.v("EstadoOri", nodeValue2);
                                continue;
                            }
                        }
                        continue;
                    }

                    if ("a:ModalidadEntrega".equals(nodeName)) {
                        NodeList mode = nodeitem.getChildNodes();
                        for (int d = 0; d < mode.getLength(); d++) {
                            String freOcc = mode.item(d).getNodeName();
                            String nodeValue2 = mode.item(d)
                                    .getFirstChild().getNodeValue();
                            if ("a:Frecuencia".equals(freOcc)) {
                                map.put("Frecuencia", nodeValue2);
                                Log.v("Frecuencia", nodeValue2);
                                continue;
                            }
                            if ("a:OcurreForzoso".equals(freOcc)) {
                                map.put("OcurreForzoso", nodeValue2);
                                Log.v("OcurreForzoso", nodeValue2);
                                continue;
                            }
                        }
                        continue;
                    }
                    if ("a:TipoEnvio".equals(nodeName)) {
                        NodeList sentType = nodeitem.getChildNodes();
                        for (int d = 0; d < sentType.getLength(); d++) {
                            String charac = sentType.item(d).getNodeName();
                            String nodeValue2 = sentType.item(d)
                                    .getFirstChild().getNodeValue();
                            if ("Alto".equals(charac)) {
                                map.put("Alto", nodeValue2);
                                Log.v("Alto", nodeValue2);
                                continue;
                            }
                            if ("Ancho".equals(charac)) {
                                map.put("Ancho", nodeValue2);
                                Log.v("Ancho", nodeValue2);
                                continue;
                            }
                            if ("EsPaquete".equals(charac)) {
                                map.put("EsPaquete", nodeValue2);
                                Log.v("EsPaquete", nodeValue2);
                                continue;
                            }
                            if ("Largo".equals(charac)) {
                                map.put("Largo", nodeValue2);
                                Log.v("Largo", nodeValue2);
                                continue;
                            }
                            if ("Peso".equals(charac)) {
                                map.put("Peso", nodeValue2);
                                Log.v("Peso", nodeValue2);
                                continue;
                            }
                        }
                        Log.v("ADD MAP","-------------------------------------------");
                        cotizadorMap.add(map);
                        continue;
                    }
                    if ("a:TipoServicio".equals(nodeName)) {

                        NodeList respuestacotiza = nodeitem.getChildNodes();
                        Node eachservice;
                        NodeList eachServNode;
                        for (int f = 0; f < respuestacotiza.getLength(); f++) {
                            map  = new HashMap<String, String>();
                            eachservice = respuestacotiza.item(f);
                            eachServNode = eachservice.getChildNodes();
                            for (int d = 0; d < eachServNode.getLength(); d++) {


                                String charac = eachServNode.item(d)
                                        .getNodeName();
                                String nodeValue2 = eachServNode
                                        .item(d).getFirstChild()
                                        .getNodeValue();
                                if ("a:CCSobrePeso".equals(charac)) {
                                    map.put("CCSobrePeso", ""+nodeValue2);
                                    Log.v("CCSobrePeso", nodeValue2);
                                    continue;
                                }
                                if ("a:CCTarifaBase".equals(charac)) {
                                    map.put("CCTarifaBase", ""+nodeValue2);
                                    Log.v("CCTarifaBase", nodeValue2);
                                    continue;
                                }
                                if ("a:CargosExtra".equals(charac)) {
                                    map.put("CargosExtra", ""+nodeValue2);
                                    Log.v("CargosExtra", nodeValue2);
                                    continue;
                                }
                                if ("a:CostoTotal".equals(charac)) {
                                    map.put("CostoTotal", ""+nodeValue2);
                                    Log.v("CostoTotal", nodeValue2);

                                    continue;
                                }
                                if ("a:DescripcionServicio".equals(charac)) {
                                    map.put("DescripcionServicio", ""+nodeValue2);
                                    Log.v("DescripcionServicio", nodeValue2);
                                    continue;
                                }
                                if ("a:Peso".equals(charac)) {
                                    map.put("Peso", ""+nodeValue2);
                                    Log.v("Peso", nodeValue2);
                                    continue;
                                }
                                if ("a:SobrePeso".equals(charac)) {
                                    map.put("SobrePeso", ""+nodeValue2);
                                    Log.v("SobrePeso", nodeValue2);
                                    continue;
                                }
                                if ("a:TarifaBase".equals(charac)) {
                                    map.put("TarifaBase", ""+nodeValue2);
                                    Log.v("TarifaBase", nodeValue2);
                                    continue;
                                }
                                if ("a:TipoEnvioRes".equals(charac)) {
                                    map.put("TipoEnvioRes", ""+nodeValue2);
                                    Log.v("TipoEnvioRes", nodeValue2);
                                    continue;
                                }

                                if ("a:AplicaCotizacion".equals(charac)) {
                                    map.put("AplicaCotizacion", ""+nodeValue2);
                                    Log.v("AplicaCotizacion", nodeValue2);
                                    continue;
                                }

                                if ("a:AplicaServicio".equals(charac)) {
                                    map.put("AplicaServicio", ""+nodeValue2);
                                    Log.v("AplicaServicio", nodeValue2);
                                    continue;
                                }
                                Log.v("ADD MAP", "-------------------------------------------");

                            }
                            cotizadorMap.add(map);
                        }
                        continue;
                    }
                }


        }

        if(cotizadorMap.size()>0){
           /* for(int i=0;i<officeMap.size();i++)
            Log.d("ArrayList",officeMap.get(i).get("nombreOficina"));*/
            Log.d("Response Manager","end");
            return cotizadorMap;
        }

        return null;

    }

    public ArrayList<Map<String,String>> parseCotizadorInternational(Document doc) throws SAXException, IOException, ParserConfigurationException {
        ArrayList<Map<String, String>> cotizadorMap = new ArrayList<>();
        Map<String, String> map = new HashMap<String, String>();
        Node nodeitem;
        String nodeName = "";
        String nodeValue = "";


        Node subNode;
        /*
        NodeList errores = doc.getElementsByTagName("HasError");
        if(errores.getLength()>0){
            subNode = errores.item(0);
            String value=subNode.getNodeValue();
            if ("true".equals(value)) {
                Log.v("HasError",value);
                return null;
            }
        }
*/
        NodeList response = doc.getElementsByTagName("CotizaPlanoResult");
        if (response != null) {
            if (response.getLength() == 0) {
                return null;
            }
        }


        Node node = null;
        node = response.item(0);
        NodeList all = node.getChildNodes();


        for (int i = 0; i < all.getLength(); i++) {
            nodeitem = all.item(i);
            nodeName = nodeitem.getNodeName();
            Log.d("Node name", nodeName);

            if (nodeitem.hasChildNodes()) {

                subNode = nodeitem.getFirstChild();

                if (subNode.getNodeValue() != null) {
                    nodeValue = subNode.getNodeValue();
                }

                if ("HasError".equals(nodeName)) {
                    if (subNode.getNodeValue().equals("true"))
                        map.put("HasError",nodeValue);
                        Log.d("HasError", "" + nodeValue);
                    continue;
                }

                if("ErrorMessageESP".equals(nodeName)){
                        map.put("ErrorMessageESP",nodeValue);
                        Log.d("ErrorMessageESP", "" + nodeValue);
                        continue;
                }

                if ("PrecioCombustible".equals(nodeName)) {
                    Log.d("PrecioCombustible", "" + nodeValue);
                    map.put("PrecioCombustible", nodeValue);
                    continue;
                }

                if ("PrecioPaquete".equals(nodeName)) {
                    Log.d("PrecioPaquete", nodeValue);
                    map.put("PrecioPaquete", nodeValue);
                    continue;
                }

                if ("PrecioCotizado".equals(nodeName)) {
                    Log.d("PrecioCotizado", nodeValue);
                    map.put("PrecioCotizado", nodeValue);
                    continue;
                }
            }
        }
        cotizadorMap.add(map);

        if(cotizadorMap.size()>0){
           /* for(int i=0;i<officeMap.size();i++)
            Log.d("ArrayList",officeMap.get(i).get("nombreOficina"));*/
            Log.d("Response Manager","end");
            return cotizadorMap;
        }

        return null;
    }



    public ArrayList<Map<String,String>> ExceptionCodes(Document doc) throws SAXException, IOException, ParserConfigurationException{

        ArrayList<Map<String ,String>> codesMap =new ArrayList<>();
        doc.getDocumentElement().normalize();

        NodeList clavesExcepcion = doc.getElementsByTagName("a:ClaveExcepcion");

        if(clavesExcepcion!=null){
            if(clavesExcepcion.getLength()==0){
                return null;
            }
        }
        Node node = null;
        String value="";

        for (int i = 0; i < clavesExcepcion.getLength(); i++) {
            node = clavesExcepcion.item(i);
            Map<String,String> map  = new HashMap<String, String>();
            NodeList list2 = node.getChildNodes();
            for (int j = 0; j < list2.getLength(); j++) {
                Node nodoItem=list2.item(j);
                String nodeName = nodoItem.getNodeName();
                Log.d("nodeName", nodeName);

                if(!nodeName.startsWith("#"))
                    if (nodeName != null) {
                        Node subNode=nodoItem.getFirstChild();
                        if(subNode==null){
                            continue;
                        }
                        value=subNode.getNodeValue();
                        if ("a:clave".equals(nodeName)) {
                            Log.d("clave", value);
                            map.put("clave",value);
                            continue;
                        } else if ("a:descripcion".equals(nodeName)) {
                            Log.d("descripcion", value);
                            map.put("descripcion",value);
                            continue;
                        } else if ("a:status".equals(nodeName)) {
                            Log.d("status", value);
                            map.put("status", value);
                            if(value.equals("false")){
                                Log.d("borrar", value);
                                map.put("borrar", "true");
                            } else {
                                Log.d("borrar", value);
                                map.put("borrar", "false");
                            }
                            continue;
                        } else if ("a:ultimaAct".equals(nodeName)) {
                            Log.d("ultimaAct", value);
                            map.put("ultimaAct", value);
                            String cdb = Codes.getCodeByClave(activity ,map.get("clave"));
                            if(cdb!=null){
                                Log.d("encontrado", value);
                                map.put("encontrado", "true");
                                if(!value.equals(cdb)){
                                    Log.d("modificado", value);
                                    map.put("modificado", "true");
                                } else {
                                    Log.d("modificado", value);
                                    map.put("modificado","false");
                                }
                            } else {
                                Log.d("encontrado", value);
                                map.put("encontrado", "false");
                            }
                            continue;
                        }
                    }
            }
            Log.d("method",METHOD.REQUEST_EXCEPTION_CODES.toString());
            map.put("method",METHOD.REQUEST_EXCEPTION_CODES.toString());

            codesMap.add(map);
        }


        if(codesMap.size()>0){
           /* for(int i=0;i<officeMap.size();i++)
            Log.d("ArrayList",officeMap.get(i).get("nombreOficina"));*/
            Log.d("Response Manager","end");
            return codesMap;
        }
        Log.d("Response Manager","end");
        return null;
    }



}
