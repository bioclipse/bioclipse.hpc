package net.bioclipse.uppmax.xmldisplay;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XmlLabelProvider implements ITableLabelProvider {

	
	public void dispose() {
		// Nothing ...
	}

	@Override
	public void addListener(ILabelProviderListener listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isLabelProperty(Object element, String property) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		String columnText = "";
		if (element instanceof XmlRow) {
			XmlRow xmlRowElem = (XmlRow) element;
			columnText = xmlRowElem.getLabel(columnIndex);
		} else if (element instanceof XmlRowCollection) {
			if (columnIndex == 0) {
				columnText = ((XmlRowCollection) element).getCategory();
			}
		}
		return columnText;
	}
	
}
