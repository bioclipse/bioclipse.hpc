package net.bioclipse.hpc.xmldisplay;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import net.bioclipse.hpc.domains.application.HPCUtils;
import net.bioclipse.hpc.wizards.ExecScriptAsBatchJobAction;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XmlUtils {
	private static final Logger logger = LoggerFactory.getLogger(ExecScriptAsBatchJobAction.class);	
	
	/**
	 * 
	 * @param rawXmlContent
	 * @return
	 */
	public static Document xmlToDOMDocument(String rawXmlContent) {
		Document resultXmlDocument = null;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true); // never forget this!
		DocumentBuilder builder = null;
		
		try {
			builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e1) {
			logger.error(e1.getMessage());
		}

		InputStream xmlContentIS = IOUtils.toInputStream(rawXmlContent);
		
		try {
			resultXmlDocument = builder.parse(xmlContentIS);
		} catch (SAXException e) {
			logger.error("SAX Exception: " + e.getMessage());
		} catch (IOException e) {
			logger.error("IO Exception: " + e.getMessage());
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
		Document xmlDoc = XmlUtils.xmlToDOMDocument(fileContent);
		return xmlDoc;
	}
	
	public static Object evalXPathExpr(String pathExpr, Document xmlDocument, QName returnType) {
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
	
	public static Object evalXPathExprToNodeList(String pathExpr, Document xmlDocument) {
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
	

	/**
	 * Convert W3C Dom NodeList to an Java ArrayList of Nodes
	 * @param nodeList
	 * @return
	 */
	public static List<Node> nodeListToListOfNodes(NodeList nodeList) {
		ArrayList<Node> listOfNodes = new ArrayList<Node>();
		for (int i=0; i<nodeList.getLength(); i++) {
			Node currentNode = nodeList.item(i);
			listOfNodes.add(currentNode);
		}
		return listOfNodes;
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
