package net.bioclipse.hpc.xmldisplay;

import static org.w3c.dom.Node.TEXT_NODE;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.bioclipse.hpc.wizards.ExecScriptAsBatchJobAction;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.TreeColumn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Factory class with methods for parsing and storing of the the original XML 
 * content as well as the parsed form (in form of W3C DOM objects), for creating
 * content and label providers, and for creating columns.
 */
public class XmlDataProviderFactory {

	private List<XmlRowCollection> fRowCollections = new ArrayList<XmlRowCollection>();
	private List<TreeViewerColumn> fColumns = new ArrayList<TreeViewerColumn>();
	private String fRawXmlContent;
	private NodeList fNodeList;
	private Document fXmlDocument;
	private Map<String, String> fColumnLabelMappings = new HashMap<String, String>();
	private static final Logger logger = LoggerFactory.getLogger(XmlDataProviderFactory.class);	
	
	public XmlDataProviderFactory() {}
	
	/**
	 * 	Do some XPath parsing here, á la 
	 *	http://www.ibm.com/developerworks/library/x-javaxpathapi.html
	 *	http://eclipse.dzone.com/news/dynamic-jface-xml-tableviewer-
	 *
	 * @param newRawXmlContent
	 */
	public void setAndParseXmlContent(String newRawXmlContent, String xpathExprForItems) {
		setRawXmlContent(newRawXmlContent);

		String rawXmlContent = getRawXmlContent();
		Document xmlDoc = XmlUtils.xmlToDOMDocument(rawXmlContent);
		setXmlDocument(xmlDoc);
		
		setNodeList((NodeList) XmlUtils.evalXPathExprToNodeList(xpathExprForItems, getXmlDocument()));
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
					logger.debug("Column ID: " + colIdStr + ", Label: " + labelStr); // TODO: Remove debug-code
					fColumnLabelMappings.put(colIdStr, labelStr);
				} catch (Exception e) {
					logger.error("Could not find attributes in columnlabelmappings/mapping!");
					e.printStackTrace();
				}
			}
		}
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
			logger.debug("Cat to add: " + cat);
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

