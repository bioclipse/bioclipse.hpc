package net.bioclipse.uppmax.xmldisplay;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.management.modelmbean.XMLParseException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.io.IOUtils;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.TreeColumn;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import static org.w3c.dom.Node.TEXT_NODE;

public class XmlDataProviderFactory {
	/**
	 * Factory class with methods for parsing and storing of the the original XML 
	 * content as well as the parsed form (in form of W3C DOM objects), for creating
	 * content and label providers, and for creating columns.
	 */
	private List<XmlRowCollection> fRowCollections = new ArrayList<XmlRowCollection>();
	private List<TreeViewerColumn> fColumns = new ArrayList<TreeViewerColumn>();
	private String fRawXmlContent;
	private NodeList fNodeList;
	private Document fXmlDocument;
	private Map<String, String> fColumnLabelMappings = new HashMap<String, String>();
	
	public XmlDataProviderFactory() {
	}
	
	/**
	 * 	Do some XPath parsing here, á la 
	 *	http://www.ibm.com/developerworks/library/x-javaxpathapi.html
	 *	http://eclipse.dzone.com/news/dynamic-jface-xml-tableviewer-
	 *
	 * @param newRawXmlContent
	 */
	public void setAndParseXmlContent(String newRawXmlContent) {
		setRawXmlContent(newRawXmlContent);

		String rawXmlContent = getRawXmlContent();
		Document xmlDoc = parseRawXmlToXmlDocument(rawXmlContent);
		setXmlDocument(xmlDoc);
		
		// TODO: Make this into a recursive function instead
		createColumnLabelMappings((NodeList) evaluateXPathExpr("infodocument/columnlabelmappings/mapping"));
		setNodeList((NodeList) evaluateXPathExpr("infodocument/item"));
		createRowCollectionsFromNodeList(getNodeList());
	}

	private void createColumnLabelMappings(NodeList mappings) {
		for ( int i = 0; i < mappings.getLength(); i++ ) {
			Node tempNode = mappings.item(i);
			String label = tempNode.getAttributes().getNamedItem("label").getTextContent();
			if ( tempNode.getNodeType() != TEXT_NODE ) {
				NamedNodeMap tempNodeAttrs = tempNode.getAttributes();
				try {
					Node colidAttr = tempNodeAttrs.getNamedItem("colid");
					Node labelAttr = tempNodeAttrs.getNamedItem("label");
					String colIdStr = colidAttr.getNodeValue();
					String labelStr = labelAttr.getNodeValue();
					System.out.println("Column ID: " + colIdStr + ", Label: " + labelStr); // TODO: Remove debug-code
					fColumnLabelMappings.put(colIdStr, labelStr);
				} catch (Exception e) {
					System.out.println("Could not find attributes in columnlabelmappings/mapping!");
					e.printStackTrace();
				}
			}
		}
	}

	private Document parseRawXmlToXmlDocument(String rawXmlContent) {
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

	private Object evaluateXPathExpr(String pathExpr) {
		XPathFactory xPathFactory = XPathFactory.newInstance();
		XPath xpathObj = xPathFactory.newXPath();
		XPathExpression expr;
		Object result = null;
		try {
			expr = xpathObj.compile(pathExpr);
			result = expr.evaluate(getXmlDocument(), XPathConstants.NODESET);
		} catch (XPathExpressionException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return result;
	}
	
	private void createRowCollectionsFromNodeList(NodeList nodeList) {
		// We create a temporary hashmap so that we can easily check if a row collection
		// for a certain category is already created or not ... 
		Map<String, XmlRowCollection> rowCollections = new HashMap<String, XmlRowCollection>();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node tempNode = nodeList.item(i);
			String category = tempNode.getAttributes().getNamedItem("category").getNodeValue();
			XmlRow tempRow = new XmlRow();
			tempRow.setNode(tempNode); 	// TODO: Do some parsing of labels before this ... maybe move out
										// the current parsing from the XmlRow class ...
			if (rowCollections.containsKey(category)) {
				XmlRowCollection tempRowCollection = rowCollections.get(category);
				tempRowCollection.addRow(tempRow);
			} else {
				// Create new rowcollection
				XmlRowCollection tempRowCollection = XmlRowCollection.getNewInstance();
				tempRowCollection.setCategory(category);
				tempRowCollection.addRow(tempRow);
				rowCollections.put(category, tempRowCollection);
			}
		}
		for (Map.Entry<String, XmlRowCollection> entry : rowCollections.entrySet()) {
			String cat = entry.getKey();
			System.out.println("Cat to add: " + cat);
			XmlRowCollection tempRowCollection = entry.getValue();
			getRowCollections().add(tempRowCollection);
		}
	}

	public void createColumnsForTreeViewer(TreeViewer treeViewer) {
		// Get all subnodes of the first item, for determining column labels
		
		Map<String, String> colLabelMappings = getColumnLabelMappings();
		
		for (int i = 0; i < colLabelMappings.size(); i++ ) {
			String label = colLabelMappings.get(Integer.toString(i));
		
			TreeViewerColumn treeViewerColumn = new TreeViewerColumn(treeViewer, SWT.NONE);
			
			TreeColumn aTreeColumn = treeViewerColumn.getColumn();
			aTreeColumn.setWidth(80);
			aTreeColumn.setText(label);
			getColumns().add(treeViewerColumn);
		}
	}

	public XmlContentProvider getContentProvider() {
		XmlContentProvider aXmlContentProvider = new XmlContentProvider();
		// Link this factory class from the content provider, so it can 
		// retrieve the elements from it's original storage therein
		aXmlContentProvider.setXmlDataProvider(this);
		return aXmlContentProvider;
	}

	public XmlLabelProvider getLabelProvider() {
		XmlLabelProvider aXmlRowLabelProvider = new XmlLabelProvider();
		return aXmlRowLabelProvider;
	}
	
	public List<XmlRowCollection> getRowCollections() {
		return fRowCollections;
	}

	public void setRowCollections(List<XmlRowCollection> rowCollections) {
		this.fRowCollections = rowCollections;
	}	

	public List<TreeViewerColumn> getColumns() {
		return fColumns;
	}
	
	public List<XmlRowCollection> getContent() {
		List<XmlRowCollection> rowCollections = getRowCollections();
		return rowCollections;
	}
	
	private String getRawXmlContent() {
		return fRawXmlContent;
	}

	private void setRawXmlContent(String rawXmlContent) {
		this.fRawXmlContent = rawXmlContent;
	}

	private NodeList getNodeList() {
		return fNodeList;
	}

	private void setNodeList(NodeList nodeList) {
		this.fNodeList = nodeList;
	}

	private Document getXmlDocument() {
		return fXmlDocument;
	}

	private void setXmlDocument(Document xmlDocument) {
		this.fXmlDocument = xmlDocument;
	}

	private Map<String, String> getColumnLabelMappings() {
		return fColumnLabelMappings;
	}

	private void setColumnLabelMappings(Map<String, String> columnLabelMappings) {
		this.fColumnLabelMappings = columnLabelMappings;
	}

}

