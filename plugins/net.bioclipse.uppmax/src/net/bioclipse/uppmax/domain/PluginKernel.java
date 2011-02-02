package net.bioclipse.uppmax.domain;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.xpath.XPathConstants;

import net.bioclipse.uppmax.xmldisplay.XmlUtils;

import org.eclipse.ui.internal.FolderLayout;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import org.osgi.service.prefs.Preferences;
import org.osgi.service.prefs.PreferencesService;
import org.w3c.dom.Document;

public class PluginKernel {
	private static PluginKernel fPluginKernel = new PluginKernel();

	public static PluginKernel getPluginKernel() {
		return fPluginKernel;
	}

	// This class is a singleton, so don't let anyone else instantiate it 
	private PluginKernel() {}

	public List<String> initToolConfigPrefs(String folderPath) {
		File folder = new File(folderPath);

		if (!folder.isDirectory()) {
			System.out.println("Not a directory: " + folderPath);
			return null;
		} 

		File[] toolFolders = folder.listFiles();
		List<String> fileNames = new ArrayList<String>();
		List<String[]> fileContentList = new ArrayList<String[]>();

		// ***** INIT PREFERENCES SERVICE ***** 
		BundleContext context = FrameworkUtil.getBundle(this.getClass()).getBundleContext();
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
//					System.out.println("Dir: " + dirName);
					File[] toolFiles = toolGroupFolder.listFiles();
					for (File toolFile : toolFiles) {
						String toolName = toolFile.getName();
						if (toolName.endsWith(".xml")) {
							// Create new preferences node, for the current tool
							Preferences currentToolPrefs = currentToolGroupPrefs.node(toolName);
							
							fileNames.add(toolFile.getName());
//							System.out.println("XML file: " + toolGroupName);
							xmlFilesCnt++;

							String folderFilePath = toolFile.getAbsolutePath();
							String[] fileContentLines = readFileToStringArray(folderFilePath);

							// Remove the initial XML header, if existing, since it messes up the SAX parser
							fileContentLines[0] = fileContentLines[0].replaceFirst("<\\?xml.*\\?>", ""); 
							// Convert to String
							String fileContent = arrayToString(fileContentLines); 
							// Create XML Document
							Document xmlDoc = XmlUtils.parseXmlToDocument(fileContent);

							try {
								// Get the description, with XPath
								String description = (String) XmlUtils.evaluateXPathExpr(xmlDoc, "/tool/description", XPathConstants.STRING);
								// Get the command, with XPath
								String command = (String) XmlUtils.evaluateXPathExpr(xmlDoc, "/tool/command", XPathConstants.STRING);
								// Remove whitespace at start and end
								command = command.trim();
								// Replace repeating whitespaces
								command = command.replaceAll("[\\ ]+", " ");
//								System.out.println("Description: " + description);
//								System.out.println("Command: " + command);

								// Add to preferences service
								currentToolPrefs.put("description", description);
								currentToolPrefs.put("command", command);
							} catch (Exception e) {
								System.err.println("Error: " + e.getMessage());
							}

						}
					}
				} else {
					fileNames.add(toolGroupFolder.getName());
//					System.out.println("File: " + toolFolder.getName());
				}
			}
			System.out.println("No of XML files: " + xmlFilesCnt);
		}

		return fileNames;
	}

	public String[] readFileToStringArray(String filePath) {
		File file = new File(filePath);
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		DataInputStream dis = null;
		List<String> stringBuffer = new ArrayList<String>();

		try {
			fis = new FileInputStream(file);

			// Here BufferedInputStream is added for fast reading.
			bis = new BufferedInputStream(fis);
			dis = new DataInputStream(bis);

			// dis.available() returns 0 if the file does not have more lines.
			int i = 0;
			while (dis.available() != 0) {
				stringBuffer.add(dis.readLine());
			}

			// dispose all the resources after using them.
			fis.close();
			bis.close();
			dis.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		String[] result = stringBuffer.toArray(new String[]{});
		return result;
	}


	private String arrayToString(String[] stringArray){
		String str = " ";
		for (int i = 0; i < stringArray.length; i++) {
			str = str + stringArray[i];
		}
		return str;
	}
}
