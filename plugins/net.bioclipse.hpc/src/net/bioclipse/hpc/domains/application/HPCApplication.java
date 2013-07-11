package net.bioclipse.hpc.domains.application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.bioclipse.hpc.Activator;
import net.bioclipse.hpc.domains.toolconfig.ToolConfigDomain;
import net.bioclipse.hpc.views.jobinfo.JobInfoView;
import net.bioclipse.hpc.views.projinfo.ProjInfoView;
import net.bioclipse.hpc.xmldisplay.XmlUtils;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.rse.core.model.IHost;
import org.eclipse.rse.core.model.ISystemRegistry;
import org.eclipse.rse.core.model.SystemStartHere;
import org.eclipse.rse.core.subsystems.ISubSystem;
import org.eclipse.rse.shells.ui.RemoteCommandHelpers;
import org.eclipse.rse.subsystems.files.core.subsystems.IRemoteFile;
import org.eclipse.rse.subsystems.files.core.subsystems.RemoteFileEmpty;
import org.eclipse.rse.subsystems.shells.core.model.SimpleCommandOperation;
import org.eclipse.rse.subsystems.shells.core.subsystems.IRemoteCmdSubSystem;
import org.eclipse.rse.ui.SystemBasePlugin;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class HPCApplication extends AbstractModelObject {
	private List<IRemoteFile> _selectedFiles;
	private static final Logger logger = LoggerFactory.getLogger(HPCApplication.class);
	private enum InfoType {
		USERINFO,
		CLUSTERINFO,
		PROJINFO,
		JOBINFO
	}

	public HPCApplication() {
		_selectedFiles = new ArrayList<IRemoteFile>();
	}

	public void updateJobInfoView() {
		JobInfoView jobInfoView = getJobInfoView();

		if (jobInfoView != null) {
			String jobInfoXml = getInfoFromCluster(InfoType.JOBINFO);
			if (jobInfoXml != null && !(jobInfoXml.equals(""))) {
				// TODO: Get a List/HashMap structure instead, so that it can be used
				// to put out to the console as well, not just for updating the view.
				jobInfoView.updateViewFromXml(jobInfoXml);
			} else {
				logger.error("Could not extract XML for jobinfo! Are you logged in?!");
			}
		} else {
			logger.error("Job view not found!");
		}
	}

	public void updateProjInfoView() {
		ProjInfoView projInfoView = getProjInfoView();

		if (projInfoView!=null) {
			String projInfoXml = getInfoFromCluster(InfoType.PROJINFO);
			if (projInfoXml != null && !(projInfoXml.equals(""))) {
				projInfoView.updateViewFromXml(projInfoXml);
			} else {
				logger.error("Could not extract XML for projinfo! Are you logged in?!");
				// TODO: Show message to user!
			}
		} else {
			logger.error("Projinfo view not found!");
		}
	}

	public Map<String,Object> getUserInfo() {
		HashMap<String,Object> userInfo = null;

		String userInfoXml = getInfoFromCluster(InfoType.USERINFO);
		if (userInfoXml != null) {

			// Convert from XML to DOM Document
			Document userInfoXmlDoc = XmlUtils.xmlToDOMDocument(userInfoXml);

			// Extract info from DOM Document
			String userName = (String) XmlUtils.evalXPathExprToString("/simpleapi/userinfo/username", userInfoXmlDoc);
			List<String> projects = XmlUtils.evalXPathExprToListOfStrings("/simpleapi/userinfo/projects/project", userInfoXmlDoc);

			// Store extracted info in userInfo HashMap
			userInfo = new HashMap<String,Object>();
			userInfo.put("username", userName);
			userInfo.put("projects", projects);
			
		} else {
			logger.error("Could not extract XML for userinfo!");
		}
		return userInfo;
	}

	/**
	 * Get a HashMap structure containing some basic information about the cluster
	 * currently connected to.
	 * @return
	 */
	public Map<String, Object> getClusterInfo() {
		HashMap<String,Object> clusterInfo = null;
		String clusterInfoXml = getInfoFromCluster(InfoType.CLUSTERINFO);

		if (clusterInfoXml != null) {
			Document clusterInfoDoc = XmlUtils.xmlToDOMDocument(clusterInfoXml);
			
			// Extract info from DOM Document
			String maxNodes = (String) XmlUtils.evalXPathExprToString("/simpleapi/clusterinfo/maxnodes", clusterInfoDoc);
			String maxCpus = (String) XmlUtils.evalXPathExprToString("/simpleapi/clusterinfo/maxcpus", clusterInfoDoc);
			List<String> partitions = XmlUtils.evalXPathExprToListOfStrings("/simpleapi/clusterinfo/partitions/partition", clusterInfoDoc);

			// Put the above extracted info into a HashMap
			clusterInfo = new HashMap<String,Object>(); // New empty hashmap
			clusterInfo.put("maxnodes", maxNodes);
			clusterInfo.put("maxcpus", maxCpus);
			clusterInfo.put("partitions", partitions);

		} else {
			logger.error("Could not extract XML for clusterinfo!");
		}
		return clusterInfo;
	}

	// TODO: Refactor in similar way as the same funcitons in this class!
	public List<String> getModulesForBinary(String currentBinary) {
		String commandOutput;
		List<String> modulesForBinary = new ArrayList<String>();

		commandOutput = execRemoteCommand("fimsproxy -t modulesforbin -c " + currentBinary); // FIXME: Replace with simpleapi call

		String clusterInfoXml = getMatch("<modulesforbinary>.*</modulesforbinary>", commandOutput);
		if (clusterInfoXml != null) {	
			Document clusterInfoXmlDoc = XmlUtils.xmlToDOMDocument(clusterInfoXml);
			
			List<Node> modForBinListOfNodes = XmlUtils.evalXPathExprToListOfNodes("/modulesforbinary/module", clusterInfoXmlDoc);
			List<String> partitions = new ArrayList<String>(); // FIXME: Why isn't this one used?
			for (Node modForBinNode : modForBinListOfNodes) {
				String modForBinStr = modForBinNode.getTextContent();
				modulesForBinary.add(modForBinStr);
			}
		} else {
			logger.error("Could not extract XML for modules for binary " + currentBinary + "!");
		}
		return modulesForBinary;
	}

	public void readToolConfigFiles(String folderPath) {
		ToolConfigDomain.getInstance().readToolConfigsFromXmlFiles(folderPath);
	}

	/**
	 * Convenience function for retrieveing info from the Cluster, via it's API
	 * @param infoType
	 * @return commandOutput
	 */
	private String getInfoFromCluster(InfoType infoType) {
		String infoTypeStr;
		String commandOutput;
		String apiOutput;
		
		switch(infoType) {
			case USERINFO:
				infoTypeStr = "userinfo";
				break;
			case CLUSTERINFO:
				infoTypeStr = "clusterinfo";
				break;
			case PROJINFO:
				infoTypeStr = "projinfo";
				break;
			case JOBINFO:
				infoTypeStr = "jobinfo";
				break;
			default:
				infoTypeStr = "";
				break;
		}
		
		commandOutput = execRemoteCommand("simpleapi " + infoTypeStr);
		// Extract the API part from the messy terminal output
		apiOutput = getMatch("<simpleapi>.*?</simpleapi>", commandOutput);
		
		return apiOutput;
	}	
	
	/**
	 * Gets the Command subsystem associated with the current host
	 */
	private IRemoteCmdSubSystem getRemoteCmdSubSystem() {
		IHost myHost = getSubSystem().getHost();
		IRemoteCmdSubSystem[] subsys = RemoteCommandHelpers.getCmdSubSystems(myHost);
		for (int i = 0; i < subsys.length; i++) {
			if (subsys[i].getSubSystemConfiguration().supportsCommands()) {
				return subsys[i];
			}
		}
		return null;
	}


	private ISubSystem getSubSystem() {
		return getFirstSelectedRemoteFile().getParentRemoteFileSubSystem();
	}

	private Shell getShell() {
		return SystemBasePlugin.getActiveWorkbenchShell();
	}

	private IRemoteFile getFirstSelectedRemoteFile() {
		if (_selectedFiles.size() > 0) {
			logger.error("No file selected");
			return (IRemoteFile)_selectedFiles.get(0);
		}
		logger.error("No file selected");
		return null;
	}

	private String getMatch(String regexPattern, String text) {
		String result = null;
		Pattern p = Pattern.compile(regexPattern);
		Matcher m = p.matcher(text);
		if (m.find()) {
			result = m.group();
		}
		return result;
	}
	
	private JobInfoView getJobInfoView() {
		JobInfoView jobInfoView = (JobInfoView) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(JobInfoView.ID);
		return jobInfoView;
	}

	private ProjInfoView getProjInfoView() {
		ProjInfoView projInfoView = (ProjInfoView) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(ProjInfoView.ID);
		return projInfoView;
	}	

	/* ------------ Utility methods ------------ */

	/**
	 * Execute a remote command via SSH
	 * @param String command
	 * @return String allOutput
	 */
	public String execRemoteCommand(String command) {
		String errMsg;
		IHost hpcHost;
		String temp = "";
		String allOutput = "";
		hpcHost = getHPCHost();

		if (hpcHost == null) {
			errMsg = "No active HPC hosts!";
			logger.error(errMsg);
			return errMsg;
		} else if (hpcHost.isOffline()) {
			errMsg = "You must log in before executing remote commands!";
			logger.error(errMsg);
			return errMsg;
		} else {
			IRemoteCmdSubSystem cmdss = RemoteCommandHelpers.getCmdSubSystem(hpcHost); // It is here that it breaks!
			if (cmdss == null) {
				errMsg = "Could not find CmdSubSystem in RemoteCommandHelpers.getCmdSubSystem(hpcHost)!";
				logger.error(errMsg);
				return errMsg;
			}
			SimpleCommandOperation simpleCommandOp = new SimpleCommandOperation(cmdss, new RemoteFileEmpty(), true);
			try {
				allOutput = "";
				temp = "";
				simpleCommandOp.runCommand(command, true);
				while (temp != null) {
					temp = null;
					temp = simpleCommandOp.readLine(true);
					// if (temp != "") {
					allOutput += temp;
					// logger.debug("Output from : " + temp);
					// }
					// try {
					//     Thread.sleep(0);
					// } catch (Exception sleepError) {
					//     sleepError.printStackTrace();
					// }
				}
			} catch (Exception commandError) {
				errMsg = "Could not execute command! Are you logged in?";
				logger.error(errMsg + ", Exception message: " + commandError.getMessage());
				return errMsg;
			}
		}
		return allOutput;
	}
	
	/**
	 * Find the RSE host which is currently connected to the cluster configured in preferences
	 * @return String hpcHost
	 */
	public IHost getHPCHost() {
		IHost hpcHost = null;
		ISystemRegistry reg = SystemStartHere.getSystemRegistry();
		IHost[] hosts = reg.getHosts();
		if (hosts.length == 0) {
			logger.error("No host names found!");
		}

		for (IHost host : hosts) {
			String hostAlias = host.getAliasName();
			String hostNameInPrefPage = Activator.getDefault().getPreferenceStore().getString("hostname");
			if (hostAlias.equals(hostNameInPrefPage)) {
				hpcHost = host;
				break;
			}
		}
		return hpcHost;
	}

	public void readGalaxyToolConfigFiles() {
		// Activate Galaxy tool configuration
		String galaxyToolConfigPath = Activator.getDefault().getPreferenceStore().getString("galaxytoolconfigpath");
		ToolConfigDomain.getInstance().readToolConfigsFromXmlFiles(galaxyToolConfigPath);
	}

	public void setDefaultPreferences() {
        IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		store.setDefault("hostname", "localhost");
		store.setDefault("username", "anonymous");
		store.setDefault("galaxytoolconfigpath", "/var/www/galaxy/tools");	
		store.setDefault("showdialogonstartup", true);
	}	
}
