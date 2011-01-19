package net.bioclipse.uppmax.xmldisplay;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class XmlContentProvider implements ITreeContentProvider {
	List<XmlRow> entityList = new ArrayList<XmlRow>();
	XmlDataProviderFactory xmlDataProvider;
		
	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object[] getElements(Object inputElement) {
		Object[] elements = null;
		if (inputElement instanceof XmlRowCollection) {
			if (((XmlRowCollection) inputElement).isRootNode()) {
				List<XmlRowCollection> elementsLst = new ArrayList<XmlRowCollection>();
				((XmlRowCollection) inputElement).setIsRootNode(false);
				elementsLst.add((XmlRowCollection) inputElement);
				elements = elementsLst.toArray();
			} else {
				return getChildren(inputElement);
			}
		} 
		return elements;
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		Object[] children = null;
		// TODO Auto-generated method stub
		if (parentElement instanceof XmlRowCollection) {
			children = ((XmlRowCollection) parentElement).getRowCollection().toArray();
			return children;
		} else {
			return null;
		}
	}

	@Override
	public Object getParent(Object element) {
		if (element instanceof XmlRow) {
			return ((XmlRow) element).getParentRowCollection(); 
		} else {
			return null;
		}
	}

	@Override
	public boolean hasChildren(Object element) {
		// TODO Auto-generated method stub
		if (element instanceof XmlRowCollection) {
			boolean hasChildren = ((XmlRowCollection) element).getRowCollection().size() > 0;
			return hasChildren;
		} else {
			return false;
		}
	}

	private XmlDataProviderFactory getXmlDataProvider() {
		return xmlDataProvider;
	}

	public void setXmlDataProvider(XmlDataProviderFactory aXmlDataProvider) {
		this.xmlDataProvider = aXmlDataProvider;
	}

}
