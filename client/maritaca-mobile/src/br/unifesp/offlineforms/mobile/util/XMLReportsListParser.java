package br.unifesp.offlineforms.mobile.util;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLReportsListParser {

	public void parse(String reportsListXml){
		try {
			InputStream is = new ByteArrayInputStream(reportsListXml.getBytes(Constants.M_ENCODING));
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(is);
			doc.getDocumentElement().normalize();
			NodeList nodeList = doc.getElementsByTagName(Constants.XML_REPORT);
			for(int i = 0; i < nodeList.getLength(); i++) {				 
				Node node = nodeList.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) node;
					System.out.println("R id: " + eElement.getAttribute(Constants.XML_ID));
					System.out.println("R na: " + eElement.getAttribute(Constants.XML_NAME));
				}
			}
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}	
}