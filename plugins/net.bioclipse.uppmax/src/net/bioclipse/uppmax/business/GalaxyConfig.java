package net.bioclipse.uppmax.business;

import java.io.File;
import java.text.AttributedCharacterIterator.Attribute;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.xpath.XPathConstants;

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

public class GalaxyConfig {
	private static GalaxyConfig instanceOfClass = new GalaxyConfig();
	private static BundleContext context;
	private static ServiceReference serviceref;
	private static PreferencesService service;
	private static Preferences systemPrefs;
	private static Preferences toolConfigPrefs;
	private static String[] toolGroups;
	
	private static GalaxyConfig instance = new GalaxyConfig(); 

	public static String[] getToolGroups() {
		initContext();
		try {
			toolGroups = toolConfigPrefs.childrenNames();
			return toolGroups;
		} catch (BackingStoreException e) {
			System.err.println("DEBUG: Could not get childrenNames of toolConfigPrefs object");
			e.printStackTrace();
		}
		return null;
	}
	
	// TODO: Remove test-code
	public static void testRetrievePreferences(Object currentObject) {
		initContext();

		try {
			String[] toolGroups = toolConfigPrefs.childrenNames();
			for (String toolGroup : toolGroups) {
				Preferences currentToolGroup = toolConfigPrefs.node(toolGroup);
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

	public static void initContext() {
		context = FrameworkUtil.getBundle(instanceOfClass.getClass()).getBundleContext();
		serviceref = context.getServiceReference(PreferencesService.class.getName());
		service = (PreferencesService) context.getService(serviceref);
		systemPrefs = service.getSystemPreferences();
		toolConfigPrefs = systemPrefs.node("toolconfigs");
	}

	public static String[] getToolsForGroup(String selectedToolGroup) {
		initContext();
		Preferences toolGroupNode = toolConfigPrefs.node(selectedToolGroup);
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
	
	public static void initToolConfigPrefs(String folderPath) {
		File folder = new File(folderPath);

		if (!folder.isDirectory()) {
			System.out.println("Not a directory: " + folderPath);
		} 

		File[] toolFolders = folder.listFiles();
		List<String> fileNames = new ArrayList<String>();
		List<String[]> fileContentList = new ArrayList<String[]>();

		// ***** INIT PREFERENCES SERVICE ***** 
		BundleContext context = FrameworkUtil.getBundle(GalaxyConfig.getInstance().getClass()).getBundleContext();
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
	
	public static GalaxyConfig getInstance() {
		return instance;
	}

	public static String[] getParamNamesForTool(String currentTool, String currentToolGroup) {
		initContext();
		Preferences toolGroupNode = toolConfigPrefs.node(currentToolGroup);
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
