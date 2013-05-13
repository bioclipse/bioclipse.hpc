package net.bioclipse.hpc.domains.application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.xpath.XPathConstants;

import net.bioclipse.hpc.domains.toolconfig.ToolConfigDomain;
import net.bioclipse.hpc.views.JobInfoView;
import net.bioclipse.hpc.views.ProjInfoView;
import net.bioclipse.hpc.wizards.ExecuteCommandWizard;
import net.bioclipse.hpc.xmldisplay.XmlUtils;

import net.bioclipse.hpc.Activator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
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
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class HPCApplication extends AbstractModelObject {
	private List _selectedFiles;

	public HPCApplication() {
		_selectedFiles = new ArrayList();
	}

	public void executeCommand(String command) {
		IRemoteCmdSubSystem cmdss = getRemoteCmdSubSystem();
		if (cmdss != null && cmdss.isConnected()) {
			// Run the command in a visible shell
			RemoteCommandHelpers.runUniversalCommand(getShell(), command, ".", cmdss); //$NON-NLS-1$
		} else {
			MessageDialog.openError(getShell(), "No command subsystem", "Found no command subsystem");
		}
	}

	public String execRemoteCommand(String command) {
		IHost hpcHost;
		String temp = "";
		String allOutput = "";

		hpcHost = getHPCHost();

		if (hpcHost == null) {
			System.out.println("No active HPC host!");
		} else {
			IRemoteCmdSubSystem cmdss = RemoteCommandHelpers.getCmdSubSystem(hpcHost); // It is here that it breaks!
			if (cmdss == null) {
				System.out.println("Could not find CmdSubSystem in RemoteCommandHelpers.getCmdSubSystem(hpcHost)!");
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
					// System.out.println("Output from : " + temp);
					// }
					// try {
					// 	Thread.sleep(0);
					// } catch (Exception sleepError) {
					// sleepError.printStackTrace();
					// }
				}
			} catch (Exception commandError) {
				commandError.printStackTrace();
			}
		}
		return allOutput;
	}

	public void updateJobInfoView() {
		// find the right view
		JobInfoView jobInfoView = (JobInfoView) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(JobInfoView.ID);
		if (jobInfoView!=null) {
			String rawContent = execRemoteCommand("fimsproxy -t jobinfo");
			String jobInfoXml = getMatch("<infodocument>.*?</infodocument>", rawContent);
			if (jobInfoXml != null) {
				jobInfoView.updateViewFromXml(jobInfoXml);
			} else {
				// TODO: Trigger an appropriate error message!
				System.out.println("Could not extract XML for jobinfo! Are you logged in?!");
			}
		} else {
			System.out.println("No View found!");
		}
	}

	public void updateProjInfoView() {
		String commandOutput;

		System.out.println("Button was clicked!");
		// find the right view
		ProjInfoView projInfoView = (ProjInfoView) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(ProjInfoView.ID);
		if (projInfoView!=null) {
			System.out.println("Found projInfoView: " + projInfoView);

			commandOutput = execRemoteCommand("fimsproxy -t projinfo");

			String projInfoXml = getMatch("<projinfo>.*</projinfo>", commandOutput);
			if (projInfoXml != null) {
				projInfoView.setContentsFromXML(projInfoXml);
			} else {
				System.out.println("Could not extract XML for projinfo!");
			}
		} else {
			System.out.println("No View found!");
		}
	}

	public Map<String,Object> getUserInfo() {
		String commandOutput;
		HashMap<String,Object> userInfo = new HashMap<String,Object>();

		commandOutput = execRemoteCommand("fimsproxy -t userinfo");

		String userInfoXmlString = getMatch("<userinfo>.*</userinfo>", commandOutput);
		if (userInfoXmlString != null) {
			Document userInfoXmlDoc = XmlUtils.parseXmlToDocument(userInfoXmlString);
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
			System.out.println("Could not extract XML for userinfo!");
		}
		return userInfo;
	}

	public Map<String, Object> getClusterInfo() {
		String commandOutput;
		HashMap<String,Object> clusterInfo = new HashMap<String,Object>();

		commandOutput = execRemoteCommand("fimsproxy -t clusterinfo");

		String clusterInfoXmlString = getMatch("<clusterinfo>.*</clusterinfo>", commandOutput);
		if (clusterInfoXmlString != null) {
			Document clusterInfoXmlDoc = XmlUtils.parseXmlToDocument(clusterInfoXmlString);
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
			System.out.println("Could not extract XML for clusterinfo!");
		}
		return clusterInfo;
	}	

	public List<String> getModulesForBinary(String currentBinary) {
		String commandOutput;
		List<String> modulesForBinary = new ArrayList<String>();

		commandOutput = execRemoteCommand("fimsproxy -t modulesforbin -c " + currentBinary);

		String clusterInfoXmlString = getMatch("<modulesforbinary>.*</modulesforbinary>", commandOutput);
		if (clusterInfoXmlString != null) {	
			Document clusterInfoXmlDoc = XmlUtils.parseXmlToDocument(clusterInfoXmlString);
			NodeList modForBinNodeList = (NodeList) XmlUtils.evalXPathExprToNodeList("/modulesforbinary/module", clusterInfoXmlDoc);

			List<Node> modForBinListOfNodes = XmlUtils.nodeListToListOfNodes(modForBinNodeList);
			List<String> partitions = new ArrayList<String>();
			for (Node modForBinNode : modForBinListOfNodes) {
				String modForBinStr = modForBinNode.getTextContent();
				modulesForBinary.add(modForBinStr);
			}
		} else {
			System.out.println("Could not extract XML for modules for binary " + currentBinary + "!");
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
			System.out.println("### No Selected file! ###");
			return (IRemoteFile)_selectedFiles.get(0);
		}
		System.out.println("### No Selected file! ###");
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

	/* Utility methods */

	/**
	 * Find an RSE host which is currently connected to the
	 * the kalkyl cluster, by checking the hostname of each host
	 * @return
	 */
	public IHost getHPCHost() {
		IHost hpcHost = null;
		ISystemRegistry reg = SystemStartHere.getSystemRegistry();
		IHost[] hosts = reg.getHosts();
		if (hosts.length == 0) {
			System.out.println("No host names found!");
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
