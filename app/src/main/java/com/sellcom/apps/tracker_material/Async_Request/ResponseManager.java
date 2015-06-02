package com.sellcom.apps.tracker_material.Async_Request;

import android.app.Activity;
import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
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


    public ArrayList<Map<String,String>> parseZipCodes(InputStream response, String tipo) throws SAXException, IOException, ParserConfigurationException {
        Document doc;
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        documentBuilder.isValidating();

        ArrayList<Map<String ,String>> codigoMap =new ArrayList<>();
        Map<String,String> map  = new HashMap<String, String>();


        doc = documentBuilder.parse(response);
        response.close();
        doc.getDocumentElement().normalize();
        NodeList respDoc;
        respDoc = doc.getElementsByTagName("ConsultaDatosCPResult");

        if(respDoc == null || respDoc.getLength() == 0 ){
            return null;
        }
        Node node = null;
        node = respDoc.item(0);
        NodeList all = node.getChildNodes();
        Node nodeitem;
        String nodeName;
        for (int i = 0; i < all.getLength(); i++) {
            nodeitem = all.item(i);
            nodeName = nodeitem.getNodeName();
            if (!nodeName.startsWith("#"))
                if (nodeitem.hasChildNodes()) {
                    Node subNode = nodeitem.getFirstChild();
                    String nodeValue = "";
                    if (subNode.getNodeValue() != null)
                        nodeValue = subNode.getNodeValue();

                    if ("ListadoCodigo".equals(nodeName)){
                        NodeList listColonias = nodeitem.getChildNodes();
                        Node eachCP;
                        NodeList eachServNode;
                        for (int f = 0; f < listColonias.getLength(); f++) {
                            eachCP = listColonias.item(f);
                            eachServNode = eachCP.getChildNodes();
                            for (int d = 0; d < eachServNode.getLength(); d++) {
                                String charac = eachServNode.item(d)
                                        .getNodeName();
                                String nodeValue2 = eachServNode.item(d)
                                        .getFirstChild()
                                        .getNodeValue();

                                if ("a:Ciudad".equals(charac)) {
                                    map.put("ciudad",nodeValue2);
                                    continue;
                                }
                                if ("a:Colonia".equals(charac)) {
                                    map.put("Colonia",nodeValue2);
                                    continue;
                                }
                                if ("a:Estado".equals(charac)) {
                                    map.put("estado", nodeValue2);
                                    continue;
                                }
                            }
                        }

                    codigoMap.add(map);
                        Log.d("Response Manager","tam"+codigoMap.size());
                    }
                }
            return codigoMap;
        }




       /* String contentDocument = doc.getDocumentElement().getTextContent();
        return contentDocument;*/
        return null;
    }

}
