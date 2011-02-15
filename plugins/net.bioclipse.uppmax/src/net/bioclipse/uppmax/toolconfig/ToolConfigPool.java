package net.bioclipse.uppmax.toolconfig;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.xpath.XPathConstants;

import net.bioclipse.uppmax.business.UppmaxUtils;
import net.bioclipse.uppmax.xmldisplay.XmlUtils;

import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;
import org.osgi.service.prefs.PreferencesService;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ToolConfigPool {
	private Map<String,ToolGroup> m_toolGroups;
	
	private static ToolConfigPool instance = new ToolConfigPool();
	
	public ToolConfigPool() {
		m_toolGroups  = new HashMap<String,ToolGroup>();
	} 

	public String[] getToolGroupNames() {
		List<String> toolGroupNames = new ArrayList<String>();
		for (ToolGroup toolGroup : getToolGroups().values()) {
			toolGroupNames.add(toolGroup.getToolGroupName());
		}
		String[] toolGroupNamesArray = UppmaxUtils.stringListToStringArray(toolGroupNames);
		return toolGroupNamesArray;
	}
	
	public Map<String,ToolGroup> getToolGroups() {
		return m_toolGroups;
	}

	public String[] getToolNamesForGroupName(String selectedToolGroupName) {
		ToolGroup toolGroup = getToolGroups().get(selectedToolGroupName);
		List<Tool> tools = toolGroup.getTools();
		List<String> toolNames = new ArrayList<String>(); 
		for (Tool tool : tools) {
			toolNames.add(tool.getName());
		}
		String[] toolNamesArray = UppmaxUtils.stringListToStringArray(toolNames);
		return toolNamesArray;
	}

	public List<Tool> getToolNamesForGroup(String selectedToolGroupName) {
		ToolGroup toolGroup = getToolGroups().get(selectedToolGroupName);
		List<Tool> tools = toolGroup.getTools();
		return tools;
	}

	public void initToolConfigPrefs(String folderPath) {
		File folder = new File(folderPath);
		int xmlFilesCount = 0;

		if (!folder.isDirectory()) {
			System.out.println("Not a directory: " + folderPath);
		} 

		File[] toolFolders = folder.listFiles();

		for (File toolFolder : toolFolders) {
			String toolFolderName = toolFolder.getName();
			if (toolFolder.isDirectory()) {
				ToolGroup toolGroup = new ToolGroup(toolFolderName);
				this.addToolGroup(toolGroup);
				
				File[] toolFiles = toolFolder.listFiles();
				for (File toolFile : toolFiles) {
					String toolName = toolFile.getName();
					if (toolName.endsWith(".xml")) {
						String folderFilePath = toolFile.getAbsolutePath();
						String[] fileContentLines = UppmaxUtils.readFileToStringArray(folderFilePath);
						
						// Remove the XML Definition line
						fileContentLines = UppmaxUtils.removeXmlDefinitionLine(fileContentLines);
						// Convert to String
						String fileContent = UppmaxUtils.arrayToString(fileContentLines); 
						// Parse to Xml Document
						Document xmlDoc = XmlUtils.parseXmlToDocument(fileContent);
						Tool tool = initializeToolFromXmlDoc(xmlDoc);

						toolGroup.addTool(tool);
						xmlFilesCount++;
					}
				}
			} else {
				System.out.println("Current tool folder is not a directory: " + toolFolder.getName());
			}
		}
		String timeStamp = UppmaxUtils.currentTime(); 
		System.out.println(timeStamp + " INFO  [net.bioclipse.uppmax.business.GalaxyConfig] Initialized Galaxy tool configurations (" + xmlFilesCount + " XML files)");
	}
	
	private Tool initializeToolFromXmlDoc(Document xmlDoc) {
		Tool tool = new Tool();
		try {
			// XPath Expressions
			String name = (String) XmlUtils.evaluateXPathExpr(xmlDoc, "/tool/@name", XPathConstants.STRING);
			String description = (String) XmlUtils.evaluateXPathExpr(xmlDoc, "/tool/description", XPathConstants.STRING);
			String command = (String) XmlUtils.evaluateXPathExpr(xmlDoc, "/tool/command", XPathConstants.STRING);
			command = command.trim();
			command = command.replaceAll("[\\ ]+", " ");

			tool.setName(name);
			tool.setDescription(description);
			tool.setCommand(command);
			
			NodeList paramNodes = (NodeList) XmlUtils.evaluateXPathExprToNodeSet("/tool/inputs/param", xmlDoc);
			Map<String,String> attributes = new HashMap<String,String>(); 

			for (int i=0; i<paramNodes.getLength(); i++) {
				Node currentNode = paramNodes.item(i);
				NamedNodeMap attrs = currentNode.getAttributes();

				// Create a new pref node for the current parameter
				String attrNodeName = attrs.getNamedItem("name").getNodeValue();

				int attrsCnt = attrs.getLength();
				// Loop over all attributes for the current param, and add the name,value pair to the preferences service
				// (if neither is null)
				for (int j=0; j<attrsCnt; j++) {
					Node currentAttr = attrs.item(j);
					String currentAttrName = currentAttr.getNodeName();
					String currentAttrValue = currentAttr.getNodeValue();
					if ((currentAttrName != null) && (currentAttrValue != null)) {
						attributes.put(currentAttrName, currentAttrValue);
					} else {
						System.out.println("INFO: Attribute '" + currentAttrName + "' missing for attribute '" + attrNodeName + "' in tool '" + name + "'.");
					}
				}
			}
			
			tool.setAttributes(attributes);

		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
		}
		return tool;
	}

	public void addToolGroup(ToolGroup toolGroup) {
		if (m_toolGroups == null) {
			m_toolGroups = new HashMap<String,ToolGroup>();
		}
		m_toolGroups.put(toolGroup.getToolGroupName(),toolGroup);
	}

	public static ToolConfigPool getInstance() {
		return instance;
	}

	public static String[] getParamNamesForTool(String currentTool, String currentToolGroup) {
		// TODO: Refactor to new Object Oriented config system
//		Preferences toolGroupNode = m_toolConfigPrefs.node(currentToolGroup);
//		try {
//			String[] paramNames = toolGroupNode.node(currentTool).node("params").childrenNames();
//			return paramNames;
//		} catch (BackingStoreException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
		return null;

	}

	public Tool getToolByName(String toolNameToGet) {
		System.out.println("Tool name to get: " + toolNameToGet);
		Map<String,ToolGroup> toolGroups = getToolGroups();
		for (Map.Entry<String,ToolGroup> entry : toolGroups.entrySet()) {
			ToolGroup toolGroup = entry.getValue();
			List<Tool> tools = toolGroup.getTools();
			for (Tool tool : tools) {
				if (tool.getName().equals(toolNameToGet)) {
					return tool;
				}
			}
		}
		return null;
	}

}
