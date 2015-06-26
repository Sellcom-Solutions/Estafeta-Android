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
                //Log.d("nodeName",nodeName);

                if(!nodeName.startsWith("#"))
                    if (nodeName != null) {
                        //System.out.println(nodeName);
                        subNode=nodoItem.getFirstChild();

                        if(subNode==null){
                            continue;
                        }else
                           // Log.d("nodeNameSub",subNode.getNodeName());

                        value=subNode.getNodeValue();
                       // Log.d("subNode","value: "+value);

                        if ("a:waybill".equals(nodeName)) {
                            map.put("wayBill",value);
                        }
                        if ("a:shortWaybillId".equals(nodeName)) {
                            map.put("shortWayBillId",value);
                        }
                        if ("a:serviceId".equals(nodeName)) {
                            map.put("serviceId",value);
                        }
                        if ("a:serviceDescriptionSPA".equals(nodeName)) {
                            map.put("serviceDescriptionSPA",value);
                        }
                        if ("a:serviceDescriptionENG".equals(nodeName)) {
                            map.put("serviceDescriptionENG",value);
                        }
                        if ("a:customerNumber".equals(nodeName)) {
                            map.put("customerNumber",value);
                        }
                        if ("a:packageType".equals(nodeName)) {
                            map.put("packageType",value);
                        }
                        if ("a:additionalInformation".equals(nodeName)) {
                            map.put("additionalInformation",value);
                        }
                        if ("a:statusSPA".equals(nodeName)) {
                            map.put("statusSPA",value);
                        }
                        if ("a:statusENG".equals(nodeName)) {
                            map.put("statusENG",value);
                        }
                        if ("a:signature".equals(nodeName)) {
                            Log.d("signature",value);
                            map.put("signature",value);
                        }

                        ////
                        if ("a:pickupData".equals(nodeName)) {
                            NodeList listpk=list2.item(j).getChildNodes();
                            String nameElement="";
                            String valuePk="";

                            for (int l = 0; l < listpk.getLength(); l++) {
                                Node dataNode = listpk.item(l);
                                nameElement = dataNode.getNodeName();
                                //Log.d("nameElement PK",nameElement);
                                if(!nameElement.startsWith("#"))
                                    if (nameElement != null) {
                                        Node subNodepk=dataNode.getFirstChild();
                                        if(subNodepk==null){
                                            continue;
                                        }
                                        valuePk=subNodepk.getNodeValue();
                                        if (nameElement.equals("originAcronym")) {
                                            map.put("PK_originAcronym",valuePk);
                                            continue;
                                        }
                                        if (nameElement.equals("a:originName")) {
                                            map.put("PK_originName",valuePk);
                                            continue;
                                        }
                                        if (nameElement.equals("a:pickupDateTime")) {
                                            map.put("PK_pickupDateTime",valuePk);
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
                               // Log.d("nameElement DD",nameElement);
                                if(!nameElement.startsWith("#"))
                                    if (nameElement != null) {
                                        Node subNodeDD=dataNode.getFirstChild();
                                        if(subNodeDD==null){
                                            continue;
                                        }
                                        valueDD=subNodeDD.getNodeValue();
                                        if (nameElement.equals("a:destinationAcronym")) {
                                            map.put("DD_destinationAcronym",valueDD);
                                            continue;
                                        }
                                        if (nameElement.equals("a:destinationName")) {
                                            map.put("DD_destinationName",valueDD);
                                            continue;
                                        }
                                        if (nameElement.equals("a:deliveryDateTime")) {
                                            map.put("DD_deliveryDateTime",valueDD);
                                            continue;
                                        }
                                        if (nameElement.equals("a:zipCode")) {
                                            map.put("DD_zipCode",valueDD);
                                            continue;
                                        }
                                        if (nameElement.equals("a:receiverName")) {
                                            map.put("DD_receiverName",valueDD);
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
                                //Log.d("nameElement Dim",nameElement);
                                if(!nameElement.startsWith("#"))
                                    if (nameElement != null) {
                                        Node subNodeD=dataNode.getFirstChild();
                                        if(subNodeD==null){
                                            continue;
                                        }
                                        valueD=subNode.getNodeValue();
                                        if (nameElement.equals("a:weight")) {
                                            map.put("Dim_weight",valueD);
                                            continue;
                                        }
                                        if (nameElement.equals("a:volumetricWeight")) {
                                            map.put("Dim_volumetricWeight",valueD);
                                            continue;
                                        }
                                        if (nameElement.equals("a:width")) {
                                            map.put("Dim_width",valueD);
                                            continue;
                                        }
                                        if (nameElement.equals("a:length")) {
                                            map.put("Dim_length",valueD);
                                            continue;
                                        }
                                        if (nameElement.equals("a:height")) {
                                            map.put("Dim_height",valueD);
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
                                //Log.d("nameElement CI",nameElement);
                                if(!nameElement.startsWith("#"))
                                    if (nameElement != null) {
                                        Node subNodeCI=dataNode.getFirstChild();
                                        if(subNodeCI == null){
                                            continue;
                                        }
                                        valueCI=subNode.getNodeValue();
                                        if (nameElement.equals("a:reference")) {
                                            map.put("CI_reference",valueCI);
                                            continue;
                                        }
                                        if (nameElement.equals("a:costsCentre")) {
                                            map.put("CI_costsCentre",valueCI);
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
                                if(!name.startsWith("#"))
                                    if (name != null) {
                                        NodeList historyData = nodeHist.getChildNodes();
                                        // Every element inside History
                                        for (int l = 0; l < historyData.getLength(); l++) {
                                            Node dataNode = historyData.item(l);
                                            nameElement = dataNode.getNodeName();
                                            Log.d("nameElement History",nameElement);
                                            if(!nameElement.startsWith("#"))
                                                if (nameElement != null) {
                                                    Node subNodeH=dataNode.getFirstChild();
                                                    if(subNodeH==null){
                                                        continue;
                                                    }
                                                    value=subNodeH.getNodeValue();
                                                    Log.d("Value",value);
                                                    if (nameElement.equals("a:eventDateTime")) {
                                                        map.put("H_eventDateTime",value);
                                                        //list_history.put("H_eventDateTime",value);
                                                        //history.setEventDateTime(value);
                                                        continue;
                                                    }
                                                    if (nameElement.equals("a:eventId")) {
                                                        map.put("H_eventId",value);
                                                        //list_history.put("H_eventId",value);
                                                        continue;
                                                    }
                                                    if (nameElement.equals("a:eventDescriptionSPA")) {
                                                        map.put("H_eventDescriptionSPA",value);
                                                        //list_history.put("H_eventDescriptionSPA",value);
                                                        continue;
                                                    }
                                                    if (nameElement.equals("a:eventDescriptionENG")) {
                                                        //map.put("H_eventDescriptionENG",value);
                                                        list_history.put("H_eventDescriptionENG",value);
                                                        continue;
                                                    }
                                                    if (nameElement.equals("a:eventPlaceAcronym")) {
                                                        map.put("H_eventPlaceAcronym",value);
                                                        //list_history.put("H_eventPlaceAcronym",value);
                                                        continue;
                                                    }
                                                    if (nameElement.equals("a:eventPlaceName")) {
                                                        map.put("H_eventPlaceName",value);
                                                        //list_history.put("H_eventPlaceName",value);
                                                        continue;
                                                    }
                                                    if (nameElement.equals("a:exceptionCode")) {
                                                        map.put("H_exceptionCode",value);
                                                        //list_history.put("H_exceptionCode",value);
                                                        continue;
                                                    }
                                                    if (nameElement.equals("a:exceptionCodeDescriptionSPA")) {
                                                        map.put("H_exceptionCodeDescriptionSPA",value);
                                                        //list_history.put("H_exceptionCodeDescriptionSPA",value);
                                                        continue;
                                                    }
                                                    if (nameElement.equals("a:exceptionCodeDescriptionENG")) {
                                                        map.put("H_exceptionCodeDescriptionENG",value);
                                                        //list_history.put("H_exceptionCodeDescriptionENG",value);
                                                        continue;
                                                    }
                                                    if (nameElement.equals("a:exceptionCodeDetails")) {
                                                        map.put("H_exceptionCodeDetails",value);
                                                        //list_history.put("H_exceptionCodeDetails",value);
                                                        continue;
                                                    }
                                                }
                                        }
                                    }
                                //data.add(list_history);
                            }


                        }
                    }
            }
            data.add(map);

        }

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
                                value = "Baja California Norte";
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
            Log.d("Response Manager","end");
            return officeMap;
        }
        Log.d("Response Manager","end");
        return null;
    }
}
