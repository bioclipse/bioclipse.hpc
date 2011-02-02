package net.bioclipse.uppmax.business;

import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;
import org.osgi.service.prefs.PreferencesService;

public class PrefsUtils {
	private static PrefsUtils instanceOfClass = new PrefsUtils();
	private static BundleContext context;
	private static ServiceReference serviceref;
	private static PreferencesService service;
	private static Preferences systemPrefs;
	private static Preferences toolConfigPrefs;
	private static String[] toolGroups;

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
			return tools;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
