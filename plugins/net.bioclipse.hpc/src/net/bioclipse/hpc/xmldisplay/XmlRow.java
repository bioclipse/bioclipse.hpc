package net.bioclipse.hpc.xmldisplay;

import java.util.ArrayList;
import java.util.List;

import net.bioclipse.hpc.Activator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class XmlRow {
	private XmlRowCollection fParentRowCollection = null;
	private Node node = null;
	private List<String> labels = new ArrayList<String>();
	
    private static final Logger logger = LoggerFactory.getLogger(Activator.class);	

	public Node getNode() {
		return node;
	}

	public void setNode(Node aNode) {
		this.node = aNode;
		setLabelsListFromNode();
	}
	
	public String getLabel(int index) {
		String label = "";
		if (index < labels.size()) {
			label = labels.get(index);
		} else {
			logger.error("Index larger than number of labels, in XmlRow.getLabel: " + Integer.toString(index));
		}
		return label;
	}

	private void setLabelsListFromNode() {
		NodeList subNodes = node.getChildNodes();
		for (int j = 0; j < subNodes.getLength(); j++) {
			Node subNode = subNodes.item(j);
			if (!"#text".equalsIgnoreCase(subNode.getNodeName())) {
				String nodeName = subNode.getNodeName();
				String label = subNode.getAttributes().getNamedItem("value").getTextContent();
				labels.add(label);
			}
		}	
	}

	public void setParentRowCollection(XmlRowCollection xmlRowCollection) {
		fParentRowCollection = xmlRowCollection;
	}

	public XmlRowCollection getParentRowCollection() {
		return fParentRowCollection;
	}
}