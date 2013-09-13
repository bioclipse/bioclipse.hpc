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

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.rse.core.events.ISystemResourceChangeEvents;
import org.eclipse.rse.core.events.SystemResourceChangeEvent;
import org.eclipse.rse.core.model.IHost;
import org.eclipse.rse.core.model.ISystemRegistry;
import org.eclipse.rse.core.model.SystemStartHere;
import org.eclipse.rse.core.subsystems.ISubSystem;
import org.eclipse.rse.shells.ui.RemoteCommandHelpers;
import org.eclipse.rse.subsystems.files.core.model.RemoteFileUtility;
import org.eclipse.rse.subsystems.files.core.subsystems.IRemoteFile;
import org.eclipse.rse.subsystems.files.core.subsystems.IRemoteFileSubSystem;
import org.eclipse.rse.subsystems.files.core.subsystems.RemoteFileEmpty;
import org.eclipse.rse.subsystems.shells.core.model.SimpleCommandOperation;
import org.eclipse.rse.subsystems.shells.core.subsystems.IRemoteCmdSubSystem;
import org.eclipse.rse.ui.SystemBasePlugin;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class HPCApplication extends AbstractModelObject {
	private List<IRemoteFile> _selectedFiles;
	private static final Logger log = LoggerFactory.getLogger(HPCApplication.class);
	public enum InfoType {
		USERINFO,
		CLUSTERINFO,
		PROJINFO,
		JOBINFO,
		MODULESFORBIN
	}

	public HPCApplication() {
		_selectedFiles = new ArrayList<IRemoteFile>();
	}

	public void refreshJobInfoView(final boolean showMessageOnZeroJobs) {
		// Run as a background job
		Job bgJob = new Job("Updating job info ...") {		
			String jobInfoXml;
			protected IStatus run(IProgressMonitor monitor) {
				jobInfoXml = getInfoFromCluster(InfoType.JOBINFO);
				Display.getDefault().asyncExec(new Runnable() {
					public void run() {
						JobInfoView jobInfoView = getJobInfoView();
						if (jobInfoView != null) {
							if (jobInfoXml != null && !(jobInfoXml.equals(""))) {
								// TODO: Get a List/HashMap structure instead, so that it can be used
								// to put out to the console as well, not just for updating the view.
								jobInfoView.updateViewFromXml(jobInfoXml, showMessageOnZeroJobs);
							} else {
								log.error("Could not extract XML for jobinfo! Are you logged in?!");
							}
						} else {
							log.error("Job view not found!");
						}
					}
				}); 
				return Status.OK_STATUS;
			}
		};
		bgJob.setPriority(Job.SHORT);
		bgJob.schedule();
	}

	public void refreshProjInfoView() {
			// Run as a background job
			Job bgJob = new Job("Updating project info ...") {
				String projInfoXml;
				protected IStatus run(IProgressMonitor monitor) {
					projInfoXml = getInfoFromCluster(InfoType.PROJINFO);
					Display.getDefault().asyncExec(new Runnable() {
						public void run() {
							ProjInfoView projInfoView = getProjInfoView();
							if (projInfoView!=null) {
								if (projInfoXml != null && !(projInfoXml.equals(""))) {
									projInfoView.updateViewFromXml(projInfoXml);
								} else {
									log.error("Could not extract XML for projinfo! Are you logged in?!");
								}
							} else {
								log.error("Projinfo view not found!");
							}
						}
					}); 
					return Status.OK_STATUS;
				}
			};			
			bgJob.setPriority(Job.SHORT);
			bgJob.schedule();
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
			log.error("Could not extract XML for userinfo!");
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
			log.error("Could not extract XML for clusterinfo!");
		}
		return clusterInfo;
	}

	public List<String> getModulesForBinary(String binary) {
		List<String> modulesForBin = new ArrayList<String>();
		String xmlStr = getInfoFromCluster(InfoType.MODULESFORBIN, binary);		
		if (xmlStr != null) {	
			Document xmlDoc = XmlUtils.xmlToDOMDocument(xmlStr);
			List<Node> listOfModuleNodes = XmlUtils.evalXPathExprToListOfNodes("/simpleapi/modulesforbin/modules/module", xmlDoc);
			for (Node moduleNode : listOfModuleNodes) {
				NamedNodeMap attrs = moduleNode.getAttributes();
				Node attr = attrs.getNamedItem("name");
				String moduleStr = attr.getNodeValue();
				modulesForBin.add(moduleStr);
			}
		} else {
			log.error("Could not extract XML for modules for binary " + binary + "!");
		}
		return modulesForBin;
	}

	public void readToolConfigFiles(String folderPath) {
		ToolConfigDomain.getInstance().parseGalaxyXmlConfigs(folderPath);
	}

	/**
	 * Convenience function for retrieveing info from the Cluster, via it's API
	 * @param infoType
	 * @return commandOutput
	 */
	private String getInfoFromCluster(InfoType infoType, String binary) {
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
			case MODULESFORBIN:
				infoTypeStr = "modulesforbin";
				if (binary != null) {
					infoTypeStr += " " + binary;
				} else {
					this.log.error("No binary specified to modulesforbin API command");
				}
				break;
			default:
				infoTypeStr = "";
				break;
		}
		
		
		commandOutput = execRemoteCommand("simpleapi " + infoTypeStr);
		// Extract the API part from the messy terminal output
		apiOutput = getMatch("<simpleapi>.*?</simpleapi>", commandOutput);
		if (apiOutput == null) {
			showErrorMessage("Could not get info from cluster", "Could not get information from the remote cluster. Cluster API file seem to be missing. Please report this problem to the cluster administrator!");
		}
		
		return apiOutput;
	}	
	
	private String getInfoFromCluster(InfoType infoType) {
		return getInfoFromCluster(infoType, null);	
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
			log.error("No file selected");
			return (IRemoteFile)_selectedFiles.get(0);
		}
		log.error("No file selected");
		return null;
	}

	private String getMatch(String regexPattern, String text) {
		String result = null;
		Pattern p = Pattern.compile(regexPattern, Pattern.DOTALL);
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

	public void showErrorMessage(String title, String message) {
	    MessageBox messageDialog = new MessageBox(getShell(), SWT.ERROR);
	    messageDialog.setText(title);
	    messageDialog.setMessage(message);
	    int returnCode = messageDialog.open();
	    log.error("Error opening MessageBox: " + returnCode);
	}
	
	public void showInfoMessage(String title, String message) {
	    MessageBox messageDialog = new MessageBox(getShell(), 
	            SWT.ICON_INFORMATION);
        messageDialog.setText(title);
        messageDialog.setMessage(message);
        int returnCode = messageDialog.open();
        System.out.println(returnCode);
	}
	
	/**
	 * Execute a remote command via SSH
	 * @param String command
	 * @return String allOutput
	 */
	public String execRemoteCommand(String command) {
		String errTitle;
		String errMsg;
		IHost hpcHost;
		String tempStr = "";
		String allOutput = "";
		hpcHost = getHPCHost();

		if (hpcHost == null) {
			errTitle = "No active HPC hosts";
			errMsg = "You need to set up an SSH connection to the remote cluster, and add it's hostname and other details in Windows > Preferences > HPC Integration";
			showErrorMessage(errTitle, errMsg);
			log.error(errTitle);
			return null;
		} else if (hpcHost.isOffline()) {
			errMsg = "You must log in before executing remote commands!";
			showErrorMessage("You are not logged in", errMsg);
			log.error(errMsg);
			return null;
		} else {
			IRemoteCmdSubSystem cmdSubSys = RemoteCommandHelpers.getCmdSubSystem(hpcHost); // It is here that it breaks!
			if (cmdSubSys == null) {
				errMsg = "Could not find CmdSubSystem in RemoteCommandHelpers.getCmdSubSystem(hpcHost), where hpcHost is " + hpcHost + "!";
				showErrorMessage("Command sub-system not found!", errMsg);
				log.error(errMsg);
				return null;
			}
			SimpleCommandOperation cmdOp = new SimpleCommandOperation(cmdSubSys, new RemoteFileEmpty(), true);
			try {
				allOutput = "";
				tempStr = "";
				cmdOp.runCommand(command, true);
				boolean captureOutput = false; 
				while (tempStr != null) {
					tempStr = cmdOp.readLine(true);
					// Only start reading after we have seen the command being echoed once
					if (!captureOutput && tempStr.contains(command)) {
						captureOutput = true;
					} else if (captureOutput) {
						allOutput += tempStr + "\n";						
					}
				}
			} catch (Exception cmdError) {
				showErrorMessage("Error on executing remote command", "Failed to execute remote command!\nAre you logged in?");
				errMsg = "Failed to execute a remote command: " + command;
				log.warn(errMsg);
				log.warn(cmdError.getMessage());
				return null;
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
			log.error("No host names found!");
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
		ToolConfigDomain.getInstance().parseGalaxyXmlConfigs(galaxyToolConfigPath);
	}

	public void setDefaultPreferences() {
        IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		store.setDefault("hostname", "localhost");
		store.setDefault("username", "anonymous");
		store.setDefault("galaxytoolconfigpath", "/var/www/galaxy/tools");	
		store.setDefault("showdialogonstartup", true);
	}

	public void refreshFileBrowser() {
		// Update the file browser, to show any newly created files
		IRemoteFileSubSystem rfss = RemoteFileUtility.getFileSubSystem(getHPCHost());
		SystemResourceChangeEvent refreshFileSubStystemEvent = new SystemResourceChangeEvent(this, 
				ISystemResourceChangeEvents.EVENT_REFRESH_SELECTED_PARENT, rfss);
		ISystemRegistry registry = SystemStartHere.getSystemRegistry();
		registry.fireEvent(refreshFileSubStystemEvent);
	}

	public void cancelJobWithId(String jobId) {
		execRemoteCommand("scancel " + jobId);
		showInfoMessage("Job cancelled", "Successfully cancelled job witn id " + jobId + "!");
		refreshJobInfoView(false);
	}	
	
	/**
	 * Return true if the current user is currently logged in to the remote system
	 * defined in the preferences dialog, and false otherwise.
	 * @return
	 */
	public boolean isLoggedIn() {
		IHost host = getHPCHost();
		if (host.isOffline()) {
			return false;
		} else {
			return true;
		}
	}

	public void showErrorMessageForNotLoggedIn() {
		showErrorMessage("Not logged in", "You must log in to perform this action!");
	}
}
