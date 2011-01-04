package net.bioclipse.uppmax.xmldisplay;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.IOUtils;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.TreeColumn;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XmlDataProvider {
	private List/* <RowCollection> */rowCollections = new ArrayList();
	private String rawXmlContent;
	private NodeList nodeList;
	private Document xmlDocument;
	
	public List getRowCollections() {
		return rowCollections;
	}

	public void setRowCollections(List rowCollections) {
		this.rowCollections = rowCollections;
	}	

	public void getColumns() {
		// Nothing so far ...
	}

	public List<TreeViewerColumn> createColumns(TreeViewer treeViewer) {
		List<TreeViewerColumn> columns = new ArrayList<TreeViewerColumn>();
		for (int i=0; i<nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			TreeViewerColumn treeViewerColumn = new TreeViewerColumn(treeViewer, SWT.NONE);
			TreeColumn trclmnName = treeViewerColumn.getColumn();
			trclmnName.setWidth(100);
			trclmnName.setText(node.getNodeValue());
			columns.add(treeViewerColumn);
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
	}
}
