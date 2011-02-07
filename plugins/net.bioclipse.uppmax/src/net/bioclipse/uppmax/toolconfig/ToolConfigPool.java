package net.bioclipse.uppmax.toolconfig;

import java.io.File;
import java.text.AttributedCharacterIterator.Attribute;
import java.util.ArrayList;
import java.util.Arrays;
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
	private static ToolConfigPool instanceOfClass = new ToolConfigPool();
	
	private BundleContext m_context;
	private ServiceReference m_serviceref;
	private PreferencesService m_service;
	private Preferences m_systemPrefs;
	private Preferences m_toolConfigPrefs;
	private ArrayList<ToolGroup> m_toolGroups;
	
	private static ToolConfigPool instance = new ToolConfigPool(); 

	public String[] getToolGroupNames() {
		initContext();
		try {
			String[] toolGroupNames = m_toolConfigPrefs.childrenNames();
			return toolGroupNames;
		} catch (BackingStoreException e) {
			System.err.println("DEBUG: Could not get childrenNames of toolConfigPrefs object");
			e.printStackTrace();
		}
		return null;
	}
	
	// TODO: Remove test-code
	public void testRetrievePreferences(Object currentObject) {
		initContext();

		try {
			String[] toolGroups = m_toolConfigPrefs.childrenNames();
			for (String toolGroup : toolGroups) {
				Preferences currentToolGroup = m_toolConfigPrefs.node(toolGroup);
				System.out.println("\n\n--------------------------------------------\nTool Group: " + toolGroup);
				String[] tools = currentToolGroup.childrenNames();
				for (String name : tools) {
					System.out.println("--------------------------------------------\nTool: " + name);
					Preferences currentTool = currentToolGroup.node(name);
					String prefDescription = currentTool.get("description", "");
					String prefCommand = currentTool.get("command", "");
					System.out.println("Description from prefs: " + prefDescription);
					System.out.println("Command from prefs: " + prefCommand);
				}
				System.out.println("--------------------------------------------");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void initContext() {
		m_context = FrameworkUtil.getBundle(instanceOfClass.getClass()).getBundleContext();
		m_serviceref = m_context.getServiceReference(PreferencesService.class.getName());
		m_service = (PreferencesService) m_context.getService(m_serviceref);
		m_systemPrefs = m_service.getSystemPreferences();
		m_toolConfigPrefs = m_systemPrefs.node("toolconfigs");
	}

	public String[] getToolsForGroup(String selectedToolGroup) {
		initContext();
		Preferences toolGroupNode = m_toolConfigPrefs.node(selectedToolGroup);
		try {
			String[] tools = toolGroupNode.childrenNames(); 
			List<String> toolNames = new ArrayList<String>();
			for (String tool : tools) {
				Preferences toolNode = toolGroupNode.node(tool);
				String toolName = toolNode.get("name", "");
				toolNames.add(toolName);
			}
			String[] toolNamesArray = toolNames.toArray(new String[] {});
			return toolNamesArray;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void initToolConfigPrefsNG(String folderPath) {
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
			m_toolGroups = new ArrayList<ToolGroup>();
		}
		m_toolGroups.add(toolGroup);
	}

	public void initToolConfigPrefs(String folderPath) {
		File folder = new File(folderPath);

		if (!folder.isDirectory()) {
			System.out.println("Not a directory: " + folderPath);
		} 

		File[] toolFolders = folder.listFiles();
		List<String> fileNames = new ArrayList<String>();
		List<String[]> fileContentList = new ArrayList<String[]>();

		// ***** INIT PREFERENCES SERVICE ***** 
		BundleContext context = FrameworkUtil.getBundle(ToolConfigPool.getInstance().getClass()).getBundleContext();
		ServiceReference serviceref = context.getServiceReference(PreferencesService.class.getName());
		// Add to preferences service

		if (serviceref != null)
		{
			PreferencesService service = (PreferencesService) context.getService(serviceref);
			//store and retrieve your preferences here

			Preferences systemPrefs = service.getSystemPreferences();
			Preferences toolConfigPrefs = systemPrefs.node("toolconfigs");

			int xmlFilesCnt = 0;
			for (File toolGroupFolder : toolFolders) {
				
				if (toolGroupFolder.isDirectory()) {
					String toolGroupName = toolGroupFolder.getName();
					
					Preferences currentToolGroupPrefs = toolConfigPrefs.node(toolGroupName);
					
					fileNames.add(toolGroupName);
					File[] toolFiles = toolGroupFolder.listFiles();
					for (File toolFile : toolFiles) {
						String toolName = toolFile.getName();
						if (toolName.endsWith(".xml")) {
							// Create new preferences node, for the current tool
							Preferences currentToolPrefs = currentToolGroupPrefs.node(toolName);
							
							fileNames.add(toolFile.getName());
							xmlFilesCnt++;

							String folderFilePath = toolFile.getAbsolutePath();
							String[] fileContentLines = UppmaxUtils.readFileToStringArray(folderFilePath);

							// Remove the initial XML header, if existing, since it messes up the SAX parser
							fileContentLines[0] = fileContentLines[0].replaceFirst("<\\?xml.*\\?>", ""); 
							// Convert to String
							String fileContent = UppmaxUtils.arrayToString(fileContentLines); 
							// Create XML Document
							Document xmlDoc = XmlUtils.parseXmlToDocument(fileContent);

							try {
								////// The description part //////
								// Get the description, with XPath
								String name = (String) XmlUtils.evaluateXPathExpr(xmlDoc, "/tool/@name", XPathConstants.STRING);
								// Add to preferences service
								currentToolPrefs.put("name", name);

								////// The description part //////
								// Get the description, with XPath
								String description = (String) XmlUtils.evaluateXPathExpr(xmlDoc, "/tool/description", XPathConstants.STRING);
								// Add to preferences service
								currentToolPrefs.put("description", description);
								
								////// The description part //////
								// Get the command, with XPath
								String command = (String) XmlUtils.evaluateXPathExpr(xmlDoc, "/tool/command", XPathConstants.STRING);
								// Remove whitespace at start and end
								command = command.trim();
								// Replace repeating whitespaces
								command = command.replaceAll("[\\ ]+", " ");
								// Add to preferences service
								currentToolPrefs.put("command", command);

								Preferences currentToolParams = currentToolPrefs.node("params");
								
								////// The params //////
								// Get the param nodes
								NodeList paramNodes = (NodeList) XmlUtils.evaluateXPathExprToNodeSet("/tool/inputs/param", xmlDoc);
								int nodesCnt = paramNodes.getLength();
								for (int i=0; i<nodesCnt; i++) {
									Node currentNode = paramNodes.item(i);
									NamedNodeMap attrs = currentNode.getAttributes();

									// Create a new pref node for the current parameter
									String attrNodeName = attrs.getNamedItem("name").getNodeValue();
									Preferences currentParamPrefs = currentToolParams.node(attrNodeName);
									
									int attrsCnt = attrs.getLength();
									// Loop over all attributes for the current param, and add the name,value pair to the preferences service
									// (if neither is null)
									for (int j=0; j<attrsCnt; j++) {
										Node currentAttr = attrs.item(j);
										String currentAttrName = currentAttr.getNodeName();
										String currentAttrValue = currentAttr.getNodeValue();
										if ((currentAttrName != null) && (currentAttrValue != null)) {
											currentParamPrefs.put(currentAttrName, currentAttrValue);
										} else {
											System.out.println("INFO: Attribute '" + currentAttrName + "' missing for attribute '" + attrNodeName + "' in tool '" + name + "'.");
										}
									}
								}

								
							} catch (Exception e) {
								System.err.println("Error: " + e.getMessage());
							}

						}
					}
				} else {
					fileNames.add(toolGroupFolder.getName());
				}
			}
			String timeStamp = UppmaxUtils.currentTime(); 
			System.out.println(timeStamp + " INFO  [net.bioclipse.uppmax.business.GalaxyConfig] Initialized Galaxy tool configurations (" + xmlFilesCnt + " XML files)");
		}
	}
	
	public static ToolConfigPool getInstance() {
		return instance;
	}

	public String[] getParamNamesForTool(String currentTool, String currentToolGroup) {
		initContext();
		Preferences toolGroupNode = m_toolConfigPrefs.node(currentToolGroup);
		try {
			String[] paramNames = toolGroupNode.node(currentTool).node("params").childrenNames();
			return paramNames;
		} catch (BackingStoreException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return null;

	}

}
