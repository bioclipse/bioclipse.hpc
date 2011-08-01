package net.bioclipse.hpc.xmldisplay;

import java.util.ArrayList;
import java.util.List;

public class XmlRootNode {
	private List<XmlRowCollection> fXmlRowCollections = new ArrayList<XmlRowCollection>();

	public List<XmlRowCollection> getXmlRowCollections() {
		return fXmlRowCollections;
	}

	public void setXmlRowCollections(List<XmlRowCollection> xmlRowCollections) {
		this.fXmlRowCollections = xmlRowCollections;
	} 
}
