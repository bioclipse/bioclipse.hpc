package net.bioclipse.uppmax.xmldisplay;

import java.util.ArrayList;
import java.util.List;

public class XmlRowCollection {
	private static XmlRowCollection content;
	private String fCategory;
	private List<XmlRow> rowCollection = new ArrayList<XmlRow>();
	private boolean fIsRootNode = false;
	
	public static synchronized XmlRowCollection getNewInstance() {
		return new XmlRowCollection();
	}

	public List<XmlRow> getRowCollection() {
		return rowCollection;
	}

	public void setRowCollection(List<XmlRow> rowCollection) {
		this.rowCollection = rowCollection;
	}

	public void addRow(XmlRow row) {
		row.setParentRowCollection(this);
		this.rowCollection.add(row);
	}

	public String getCategory() {
		return fCategory;
	}
 
	public void setCategory(String category) {
		fCategory = category;
	}

	public void setIsRootNode(boolean isRootNode) {
		fIsRootNode = isRootNode;
	}

	public boolean isRootNode() {
		return fIsRootNode;
	}
}
