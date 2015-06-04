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


    public ArrayList<Map<String,String>> parseZipCodes(Document doc, String tipo) throws SAXException, IOException, ParserConfigurationException {
       /* Document doc;
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        documentBuilder.isValidating();*/

        ArrayList<Map<String ,String>> codigoMap =new ArrayList<>();
        Map<String,String> map  = new HashMap<String, String>();


       /* doc = documentBuilder.parse(response);
        response.close();*/
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
        Log.d("Node size",":"+all.getLength());
        Node nodeitem;
        String nodeName;
        for (int i = 0; i < all.getLength(); i++) {
            nodeitem = all.item(i);
            nodeName = nodeitem.getNodeName();
            Log.d("Node name", nodeName);
            if (nodeitem.hasChildNodes()) {
                Node subNode = nodeitem.getFirstChild();
                Log.d("Node child", "true");
                Log.d("Node name sub", subNode.getNodeName());
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
                        Log.d("eachCP", eachCP.getNodeName());
                        Map<String,String> map1  = new HashMap<String, String>();

                        for (int d = 0; d < eachServNode.getLength(); d++) {
                            String charac = eachServNode.item(d).getNodeName();
                            String nodeValue2 = eachServNode.item(d).getFirstChild()
                                    .getNodeValue();
                            Log.d("Node char", charac);
                            Log.d("Node value", nodeValue2);
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
        for(int i=0;i<codigoMap.size();i++)
            Log.d("ArrayList",codigoMap.get(i).get("colonia"));
        return codigoMap;
    }

}
