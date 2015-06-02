package com.sellcom.apps.tracker_material.Async_Request;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by rebecalopezmartinez on 02/06/15.
 */
public class ResponseManager {

    public ArrayList<Map<String,String>> parseZipCodes(InputStream response, String tipo) throws SAXException, IOException, ParserConfigurationException {
        Document doc;
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        documentBuilder.isValidating();

        doc = documentBuilder.parse(response);
        response.close();
        doc.getDocumentElement().normalize();
        NodeList respDoc;
        respDoc = doc.getElementsByTagName("ConsultaDatosCPResult");

        if(respDoc == null || respDoc.getLength() == 0 ){
            return null;
        }


       /* String contentDocument = doc.getDocumentElement().getTextContent();
        return contentDocument;*/
        return null;
    }

}
