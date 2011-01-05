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

	public List<Row> getRowCollection() {
		return rowCollection;
	}

	public void setRowCollection(List<Row> rowCollection) {
		this.rowCollection = rowCollection;
	}

	public void addRow(Row row) {
		this.rowCollection.add(row);
	}

}
