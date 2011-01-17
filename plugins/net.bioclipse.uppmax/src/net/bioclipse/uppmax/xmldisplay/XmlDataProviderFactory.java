package net.bioclipse.uppmax.xmldisplay;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

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
	private List<XmlRowCollection>rowCollections = new ArrayList<XmlRowCollection>();
	private List<TreeViewerColumn> columns = new ArrayList<TreeViewerColumn>();
	private String rawXmlContent;
	private NodeList nodeList;
	private Document xmlDocument;
	
	public List<XmlRowCollection> getRowCollections() {
		return rowCollections;
	}

	public void setRowCollections(List<XmlRowCollection> rowCollections) {
		this.rowCollections = rowCollections;
	}	

	public List<TreeViewerColumn> getColumns() {
		return columns;
	}
	
	public List<XmlRow> getXmlRows() {
		List<XmlRow> rows = new ArrayList<XmlRow>();
		for (int i=0; i<nodeList.getLength(); i++) {
			XmlRow tempXmlRow = new XmlRow();
			Node tempNode = nodeList.item(i);
			tempXmlRow.setNode(tempNode);
			rows.add(tempXmlRow);
		}
		return rows;
	}

	public List<TreeViewerColumn> createColumns(TreeViewer treeViewer) {
		// Get all subnodes of the first item, for determining column labels
		int nodeListLen = nodeList.getLength();
		if (nodeList != null && nodeListLen > 0) {
			NodeList children = nodeList.item(0).getChildNodes();
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
					columns.add(treeViewerColumn);
				} else {
					System.out.println("Skipping text node ..."); // TODO: Remove debug-code
				}
			}
		} 
		return columns;
	}
	
	private String getRawXmlContent() {
		return rawXmlContent;
	}

	private void setRawXmlContent(String rawXmlContent) {
		this.rawXmlContent = rawXmlContent;
	}

	private NodeList getNodeList() {
		return nodeList;
	}

	private void setNodeList(NodeList nodeList) {
		this.nodeList = nodeList;
	}

	/**
	 * 	Do some XPath parsing here, ála 
	 *	http://www.ibm.com/developerworks/library/x-javaxpathapi.html
	 *	http://eclipse.dzone.com/news/dynamic-jface-xml-tableviewer-
	 *
	 * @param newRawXmlContent
	 */
	public void setAndParseXmlContent(String newRawXmlContent) {
		this.rawXmlContent = newRawXmlContent;

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
			xmlDocument = builder.parse(xmlContentIS);
		} catch (SAXException e) {
			System.out.println("SAX Exception:");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("IO Exception:");
			e.printStackTrace();
		}
		
		XPathFactory xPathFactory = XPathFactory.newInstance();
		XPath xpathObj = xPathFactory.newXPath();
		XPathExpression expr;
		Object result;
		try {
			XmlRowCollection tempRowCollection = XmlRowCollection.getInstance();
			expr = xpathObj.compile("/jobinfo/runningjobs/runningjob");
			result = expr.evaluate(xmlDocument, XPathConstants.NODESET);
			nodeList = (NodeList) result;
			for (int i = 0; i < nodeList.getLength(); i++) {
				Node tempNode = nodeList.item(i);
				XmlRow tempRow = new XmlRow();
				tempRow.setNode(tempNode);
				tempRowCollection.addRow(tempRow);
			}
			rowCollections.add(tempRowCollection);
		} catch (XPathExpressionException e) {
			e.printStackTrace();
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
}

