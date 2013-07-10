package net.bioclipse.hpc.domains.application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.xpath.XPathConstants;

import net.bioclipse.hpc.Activator;
import net.bioclipse.hpc.domains.toolconfig.ToolConfigDomain;
import net.bioclipse.hpc.views.JobInfoView;
import net.bioclipse.hpc.views.ProjInfoView;
import net.bioclipse.hpc.xmldisplay.XmlUtils;

import org.eclipse.jface.dialogs.MessageDialog;
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

	public HPCApplication() {
		_selectedFiles = new ArrayList<IRemoteFile>();
	}

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
					// 	Thread.sleep(0);
					// } catch (Exception sleepError) {
					// sleepError.printStackTrace();
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

	public void updateJobInfoView() {
		// Find the right view
		JobInfoView jobInfoView = (JobInfoView) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(JobInfoView.ID);
		if (jobInfoView != null) {
			String rawContent = execRemoteCommand("~/opt/clusterapi/jobs -f xml"); // TODO: Get path from preferences
			// Extract the XML part from the other terminal output (including "message of the day"-text)
			String jobInfoXml = getMatch("<clusterapi>.*?</clusterapi>", rawContent);
			if (jobInfoXml != null) {
				jobInfoView.updateViewFromXml(jobInfoXml);
			} else {
				logger.error("Could not extract XML for jobinfo! Are you logged in?!");
			}
		} else {
			logger.error("Job view not found!");
		}
	}

	public void updateProjInfoView() {
		String commandOutput;
		logger.debug("Button was clicked!"); // FIXME: Remove

		// Find the right view
		ProjInfoView projInfoView = (ProjInfoView) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(ProjInfoView.ID);
		if (projInfoView!=null) {
			logger.debug("Found projInfoView: " + projInfoView);
			commandOutput = execRemoteCommand("fimsproxy -t projinfo"); // FIXME: Replace with clusterapi call

			String projInfoXml = getMatch("<projinfo>.*</projinfo>", commandOutput);
			if (projInfoXml != null) {
				projInfoView.setContentsFromXML(projInfoXml);
			} else {
				logger.error("Could not extract XML for projinfo!");
			}
		} else {
			logger.error("Projinfo view not found!");
		}
	}

	public Map<String,Object> getUserInfo() {
		String commandOutput;
		HashMap<String,Object> userInfo = new HashMap<String,Object>();

		commandOutput = execRemoteCommand("fimsproxy -t userinfo"); // FIXME: Replace with clusterapi call

		String userInfoXmlString = getMatch("<userinfo>.*</userinfo>", commandOutput);
		if (userInfoXmlString != null) {
			Document userInfoXmlDoc = XmlUtils.xmlToDOMDocument(userInfoXmlString);
			String userName = (String) XmlUtils.evalXPathExpr("/userinfo/username", userInfoXmlDoc, XPathConstants.STRING);
			userInfo.put("username", userName);
			NodeList projectsNodeList = (NodeList) XmlUtils.evalXPathExprToNodeList("/userinfo/projects/project", userInfoXmlDoc);
			// Easier to work with NodeList or List of Nodes? 
			List<Node> projectNodes = XmlUtils.nodeListToListOfNodes(projectsNodeList);
			List<String> projects = new ArrayList<String>();
			for (Node node : projectNodes) {
				String nodeVal = node.getTextContent();
				projects.add(nodeVal);
			}
			userInfo.put("projects", projects);
		} else {
			logger.error("Could not extract XML for userinfo!");
		}
		return userInfo;
	}

	public Map<String, Object> getClusterInfo() {
		String commandOutput;
		HashMap<String,Object> clusterInfo = new HashMap<String,Object>();

		commandOutput = execRemoteCommand("fimsproxy -t clusterinfo");

		String clusterInfoXmlString = getMatch("<clusterinfo>.*</clusterinfo>", commandOutput);
		if (clusterInfoXmlString != null) {
			Document clusterInfoXmlDoc = XmlUtils.xmlToDOMDocument(clusterInfoXmlString);
			String maxNodes = (String) XmlUtils.evalXPathExpr("/clusterinfo/maxnodes", clusterInfoXmlDoc, XPathConstants.STRING);
			String maxCpus = (String) XmlUtils.evalXPathExpr("/clusterinfo/maxcpus", clusterInfoXmlDoc, XPathConstants.STRING);
			clusterInfo.put("maxnodes", maxNodes);
			clusterInfo.put("maxcpus", maxCpus);

			NodeList partitionsNodeList = (NodeList) XmlUtils.evalXPathExprToNodeList("/clusterinfo/partitions/partition", clusterInfoXmlDoc);

			// Easier to work with NodeList or List of Nodes? 
			List<Node> partitionsNodes = XmlUtils.nodeListToListOfNodes(partitionsNodeList);
			List<String> partitions = new ArrayList<String>();
			for (Node partition : partitionsNodes) {
				String partitionNodeVal = partition.getTextContent();
				partitions.add(partitionNodeVal);
			}
			clusterInfo.put("partitions", partitions);
		} else {
			logger.error("Could not extract XML for clusterinfo!");
		}
		return clusterInfo;
	}	

	public List<String> getModulesForBinary(String currentBinary) {
		String commandOutput;
		List<String> modulesForBinary = new ArrayList<String>();

		commandOutput = execRemoteCommand("fimsproxy -t modulesforbin -c " + currentBinary);

		String clusterInfoXmlString = getMatch("<modulesforbinary>.*</modulesforbinary>", commandOutput);
		if (clusterInfoXmlString != null) {	
			Document clusterInfoXmlDoc = XmlUtils.xmlToDOMDocument(clusterInfoXmlString);
			NodeList modForBinNodeList = (NodeList) XmlUtils.evalXPathExprToNodeList("/modulesforbinary/module", clusterInfoXmlDoc);

			List<Node> modForBinListOfNodes = XmlUtils.nodeListToListOfNodes(modForBinNodeList);
			List<String> partitions = new ArrayList<String>();
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
	 * Gets the Command subsystem associated with the current host
	 */
	protected IRemoteCmdSubSystem getRemoteCmdSubSystem() {
		IHost myHost = getSubSystem().getHost();
		IRemoteCmdSubSystem[] subsys = RemoteCommandHelpers.getCmdSubSystems(myHost);
		for (int i = 0; i < subsys.length; i++) {
			if (subsys[i].getSubSystemConfiguration().supportsCommands()) {
				return subsys[i];
			}
		}
		return null;
	}


	protected ISubSystem getSubSystem() {
		return getFirstSelectedRemoteFile().getParentRemoteFileSubSystem();
	}

	public Shell getShell() {
		return SystemBasePlugin.getActiveWorkbenchShell();
	}

	protected IRemoteFile getFirstSelectedRemoteFile() {
		if (_selectedFiles.size() > 0) {
			logger.error("No file selected");
			return (IRemoteFile)_selectedFiles.get(0);
		}
		logger.error("No file selected");
		return null;
	}

	protected String getMatch(String regexPattern, String text) {
		String result = null;
		Pattern p = Pattern.compile(regexPattern);
		Matcher m = p.matcher(text);
		if (m.find()) {
			result = m.group();
		}
		return result;
	}

	/* ------------ Utility methods ------------ */

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
