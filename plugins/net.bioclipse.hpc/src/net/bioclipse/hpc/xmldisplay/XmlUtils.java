package net.bioclipse.hpc.xmldisplay;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import net.bioclipse.hpc.business.HPCUtils;

import org.apache.commons.io.IOUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XmlUtils {
	
	public static Document parseXmlToDocument(String rawXmlContent) {
		Document resultXmlDocument = null;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true); // never forget this!
		DocumentBuilder builder = null;
		
		try {
			builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		InputStream xmlContentIS = IOUtils.toInputStream(rawXmlContent);
		
		try {
			resultXmlDocument = builder.parse(xmlContentIS);
		} catch (SAXException e) {
			System.out.println("SAX Exception:");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("IO Exception:");
			e.printStackTrace();
		}
		
		return resultXmlDocument;
	}
	
	public static Document parseXmlFileToXmlDoc(File toolFile) {
		String toolFilePath = toolFile.getAbsolutePath();
		String[] fileContentLines = HPCUtils.readFileToStringArray(toolFilePath);
		
		// Remove the XML Definition line
		fileContentLines = HPCUtils.removeXmlDefinitionLine(fileContentLines);
		// Convert to String
		String fileContent = HPCUtils.arrayToString(fileContentLines); 
		// Parse to Xml Document
		Document xmlDoc = XmlUtils.parseXmlToDocument(fileContent);
		return xmlDoc;
	}
	
	public static Object evalXPathExpr(Document xmlDocument, String pathExpr, QName returnType) {
		XPathFactory xPathFactory = XPathFactory.newInstance();
		XPath xpathObj = xPathFactory.newXPath();
		XPathExpression expr;
		Object result = null;
		try {
			expr = xpathObj.compile(pathExpr);
			result = expr.evaluate(xmlDocument, returnType);
		} catch (XPathExpressionException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return result;
	}
	
	public static Object evaluateXPathExprToNodeSet(String pathExpr, Document xmlDocument) {
		XPathFactory xPathFactory = XPathFactory.newInstance();
		XPath xpathObj = xPathFactory.newXPath();
		XPathExpression expr;
		Object result = null;
		try {
			expr = xpathObj.compile(pathExpr);
			result = expr.evaluate(xmlDocument, XPathConstants.NODESET);
		} catch (XPathExpressionException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return result;
	}
	
	public static Node getNodeChildByName(Node parentNode, String childNodeName) {
		Node childNode = null;
		NodeList childNodeList = parentNode.getChildNodes();
		int len = childNodeList.getLength();
		for (int i=0; i<len; i++) {
			Node tmpChildNode = childNodeList.item(i);
			if (tmpChildNode.getNodeName().equals(childNodeName)) {
				return tmpChildNode;
			}
		}
		childNode = null;
		return childNode;
	}

}
