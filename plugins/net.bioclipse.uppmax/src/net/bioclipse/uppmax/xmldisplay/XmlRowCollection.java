package net.bioclipse.uppmax.xmldisplay;

import java.util.ArrayList;
import java.util.List;

public class XmlRowCollection {
	private static XmlRowCollection content;
	private String fCategory;
	private List<XmlRow> rowCollection = new ArrayList<XmlRow>();

	public static synchronized XmlRowCollection getInstance() {
		if (content != null) {
			return content;
		} else {
			content = new XmlRowCollection();
			return content;
		}
	}

	public List<XmlRow> getRowCollection() {
		return rowCollection;
	}

	public void setRowCollection(List<XmlRow> rowCollection) {
		this.rowCollection = rowCollection;
	}

	public void addRow(XmlRow row) {
		this.rowCollection.add(row);
	}

	public String getCategory() {
		return fCategory;
	}
 
	public void setCategory(String category) {
		fCategory = category;
	}

}
