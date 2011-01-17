package net.bioclipse.uppmax.xmldisplay;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

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
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

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
	private String[] fXmlPathsExpr;
	private String fDataItemExpr;
	private String fDataItemLabelExpr;
	
	public XmlDataProviderFactory(String[] xmlPathsExpr, String dataItemExpr, String dataItemLabelExpr) {
		setXmlPathsExpr(xmlPathsExpr);
		setDataItemExpr(dataItemExpr);
		setDataItemLabelExpr(dataItemLabelExpr);
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
		String pathExpr = getXmlPathsExpr()[0];
		
		Object result = evaluateXPathExpr(pathExpr);
		setNodeList((NodeList) result);
		createRowCollectionsFromNodeList(getNodeList());
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
		XmlRowCollection tempRowCollection = XmlRowCollection.getInstance();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node tempNode = nodeList.item(i);
			XmlRow tempRow = new XmlRow();
			tempRow.setNode(tempNode);
			tempRowCollection.addRow(tempRow);
		}
		getRowCollections().add(tempRowCollection);
	}

	public void createColumnsForTreeViewer(TreeViewer treeViewer) {
		// Get all subnodes of the first item, for determining column labels
		int nodeListLen = getNodeList().getLength();
		if (getNodeList() != null && nodeListLen > 0) {
			NodeList children = getNodeList().item(0).getChildNodes();
			// Loop through all the children of the first item
			for (int i=0; i<children.getLength(); i++) {
				Node childNode = children.item(i);
				// Skip skip the nodes consisting of just the actual text content of the nodes
				// (themselves being nodes)
				if (!"#text".equalsIgnoreCase(childNode.getNodeName())) {
					TreeViewerColumn treeViewerColumn = new TreeViewerColumn(treeViewer, SWT.NONE);
					TreeColumn aTreeColumn = treeViewerColumn.getColumn();
					aTreeColumn.setWidth(100);
					// Use the XML Node name as column header label
					// TODO: Define and use some custom label attribute instead
					String colHeader = childNode.getNodeName();
					if (colHeader == null) {
						colHeader = "Untitled";
					}
					aTreeColumn.setText(colHeader);
					getColumns().add(treeViewerColumn);
				} else {
					System.out.println("Skipping text node ..."); // TODO: Remove debug-code
				}
			}
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
	
	public List<XmlRow> getXmlRows() {
		List<XmlRow> rows = new ArrayList<XmlRow>();
		for (int i=0; i<fNodeList.getLength(); i++) {
			XmlRow tempXmlRow = new XmlRow();
			Node tempNode = fNodeList.item(i);
			tempXmlRow.setNode(tempNode);
			rows.add(tempXmlRow);
		}
		return rows;
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

	private String[] getXmlPathsExpr() {
		return fXmlPathsExpr;
	}

	private void setXmlPathsExpr(String[] xmlPathsExpr) {
		this.fXmlPathsExpr = xmlPathsExpr;
	}

	private String getDataItemExpr() {
		return fDataItemExpr;
	}

	private void setDataItemExpr(String dataItemExpr) {
		this.fDataItemExpr = dataItemExpr;
	}

	private String getDataItemLabelExpr() {
		return fDataItemLabelExpr;
	}

	private void setDataItemLabelExpr(String dataItemLabelExpr) {
		this.fDataItemLabelExpr = dataItemLabelExpr;
	}
}

