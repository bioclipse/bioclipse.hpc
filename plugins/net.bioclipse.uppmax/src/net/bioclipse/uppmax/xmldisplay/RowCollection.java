package net.bioclipse.uppmax.xmldisplay;

import galang.research.jface.Row;
import java.util.List;

public class RowCollection {
	private static RowCollection content;
	private List<Row> rowCollection;

	public static synchronized RowCollection getInstance() {
		if (content != null) {
			return content;
		} else {
			content = new RowCollection();
			return content;
		}
	}

	public List<Row> getEntityList() {
		return rowCollection;
	}

	private List<Row> getRowCollection() {
		return rowCollection;
	}

	private void setRowCollection(List<Row> rowCollection) {
		this.rowCollection = rowCollection;
	}

}
