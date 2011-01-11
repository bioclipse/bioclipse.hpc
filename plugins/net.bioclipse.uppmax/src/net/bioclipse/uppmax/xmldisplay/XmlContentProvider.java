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
		if (xmlDataProvider != null) {
			return xmlDataProvider.getXmlRows().toArray();
		} else {
			System.err.println("member field xmlDataProvider not set when executing getElements method in XMLContentProvider.java");
		}
		return null;
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getParent(Object element) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		// TODO Auto-generated method stub
		return false;
	}

	private XmlDataProviderFactory getXmlDataProvider() {
		return xmlDataProvider;
	}

	public void setXmlDataProvider(XmlDataProviderFactory aXmlDataProvider) {
		this.xmlDataProvider = aXmlDataProvider;
	}

}
