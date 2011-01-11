package net.bioclipse.uppmax.xmldisplay;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class XmlRow {
	/**
	 * 
	 */

	private Node node = null;
	private List<String> labels = new ArrayList<String>();

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
			System.err.println("Index larger than number of labels, in XmlRow.getLabel: " + Integer.toString(index));
		}
		return label;
	}

	private void setLabelsListFromNode() {
		NodeList subNodes = node.getChildNodes();
		for (int j = 0; j < subNodes.getLength(); j++) {
			Node subNode = subNodes.item(j);
			if (!"#text".equalsIgnoreCase(subNode.getNodeName())) {
				String label = subNode.getTextContent();
				labels.add(label);
			}
		}	
	}

}