package com.jiayiyao.wxpay.core.utils;

import com.jiayiyao.wxpay.core.net.WxResult;

import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by Jiayi Yao on 2015/7/25.
 */
public class XMLHelper {
    private static final String TAG = "XMLHelper";

    public static Element getXMLContent(String xmlString){
        try {
            DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = db.parse(new InputSource(new StringReader(xmlString)));
            return (Element)document.getFirstChild();
        } catch (SAXException | IOException | ParserConfigurationException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getValue(Element root, String tag){
        try {
            Element targetElement = (Element) root.getElementsByTagName(tag).item(0);
            return ((CharacterData)targetElement.getFirstChild()).getData();
        }catch(NullPointerException e){
            return "NULL";
        }
    }

    public static String string(Element xml){
        StringBuilder builder = new StringBuilder();
        NodeList list = xml.getChildNodes();
        for(int i=0; i<list.getLength(); i++){
            Element element = (Element)list.item(i);
            builder.append(((CharacterData)element.getFirstChild()).getData());
        }
        return builder.toString();
    }

    public static WxResult parseToWxResult(String xmlStr){
        WxResult result = new WxResult();
        Element root = XMLHelper.getXMLContent(xmlStr);
        result.setContent(root);
        return result;
    }
}
