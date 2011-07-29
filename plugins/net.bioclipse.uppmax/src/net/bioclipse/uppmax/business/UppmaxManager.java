/*******************************************************************************
 * Copyright (c) 2010  Samuel Lampa <samuel.lampa@gmail.com>
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contact: http://www.bioclipse.net/
 ******************************************************************************/
package net.bioclipse.uppmax.business;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.rse.shells.ui.RemoteCommandHelpers;
import org.eclipse.rse.subsystems.files.core.subsystems.IRemoteFile;
import org.eclipse.rse.subsystems.files.core.subsystems.RemoteFileEmpty;
import org.eclipse.rse.subsystems.shells.core.model.SimpleCommandOperation;
import org.eclipse.rse.subsystems.shells.core.subsystems.IRemoteCmdSubSystem;
import org.eclipse.rse.ui.SystemBasePlugin;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.Workbench;

import net.bioclipse.managers.business.IBioclipseManager;
import net.bioclipse.uppmax.domains.toolconfig.ToolConfigDomain;
import net.bioclipse.uppmax.views.JobInfoView;
import net.bioclipse.uppmax.views.ProjInfoView;
import net.bioclipse.uppmax.wizards.ExecuteCommandWizard;

import org.apache.log4j.Logger;
import org.eclipse.rse.core.*;
import org.eclipse.rse.core.model.IHost;
import org.eclipse.rse.core.model.ISystemRegistry;
import org.eclipse.rse.core.model.SystemStartHere;
import org.eclipse.rse.core.subsystems.ISubSystem;
import org.eclipse.rse.subsystems.shells.core.*;
import org.eclipse.rse.subsystems.shells.core.subsystems.IRemoteCmdSubSystem;

public class UppmaxManager implements IBioclipseManager {

	private static final Logger logger = Logger.getLogger(UppmaxManager.class);
	private List _selectedFiles;

	/**
	 * @wbp.parser.entryPoint
	 */
	public UppmaxManager() {
		_selectedFiles = new ArrayList();
	}

	/**
	 * Gives a short one word name of the manager used as variable name when
	 * scripting.
	 */
	public String getManagerName() {
		return "uppmax";
	}

	/* Main methods */

	public void executeCommand(String command) {
		// TODO: This code should probably not be stored here, but in a more centrally stored place
		IRemoteCmdSubSystem cmdss = getRemoteCmdSubSystem();
		if (cmdss != null && cmdss.isConnected()) {
			// Run the command in a visible shell
			RemoteCommandHelpers.runUniversalCommand(getShell(), command, ".", cmdss); //$NON-NLS-1$
		} else {
			MessageDialog.openError(getShell(), "No command subsystem", "Found no command subsystem");
		}
	}

	public void readToolConfigFiles(String folderPath) {
		ToolConfigDomain.getInstance().readToolConfigsFromXmlFiles(folderPath);
	}
	
	public void updateProjectInfoView() {
		String commandOutput;
		IHost uppmaxHost;

		System.out.println("Button was clicked!");
		// find the right view
		ProjInfoView projInfoView = (ProjInfoView) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(ProjInfoView.ID);
		if (projInfoView!=null) {
			System.out.println("Found jobInfoView: " + projInfoView);

			commandOutput = executeRemoteCommand("showprojinfo");
			
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
	
	public void updateJobInfoView() {
		String commandOutput;

		System.out.println("Update JobInfo-Button was clicked!");
		// find the right view
		JobInfoView jobInfoView = (JobInfoView) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(JobInfoView.ID);
		if (jobInfoView!=null) {
			System.out.println("Found jobInfoView: " + jobInfoView);

//			String rawContent = executeRemoteCommand("python /home/samuel/projects/bioclipseclient/clusterproxy.py -t jobinfo nimar" /* "clusterproxy -t jobinfo" */ );
			String rawContent = executeRemoteCommand("/home/samuel/projects/bioclipseclient/output_squeue_as_xml.sh pontuss" /* "clusterproxy -t jobinfo" */ );
			String jobInfoXml = getMatch("<infodocument>.*?</infodocument>", rawContent);
			if (jobInfoXml != null) {
				jobInfoView.updateViewFromXml(jobInfoXml);
			} else {
				System.out.println("Could not extract XML for jobinfo! Are you logged in?!");
			}
		} else {
			System.out.println("No View found!");
		}
	}
	
	public String executeRemoteCommand(String command) {
		IHost uppmaxHost;
		String temp = "";
		String allOutput = "";
		
		uppmaxHost = getUppmaxHost();
		
		if (uppmaxHost == null) {
			System.out.println("No active UPPMAX host!");
		} else {
			IRemoteCmdSubSystem cmdss = RemoteCommandHelpers.getCmdSubSystem(uppmaxHost);
			SimpleCommandOperation simpleCommandOp = new SimpleCommandOperation(cmdss, new RemoteFileEmpty(), true);
			try {
				allOutput = "";
				temp = "";
				simpleCommandOp.runCommand(command, true);
				while (temp != null) {
					temp = null;
					temp = simpleCommandOp.readLine(true);
					if (temp != "") {
						allOutput += temp;
						System.out.println("Output from : " + temp);
					}
					try {
						Thread.sleep(15);
					} catch (Exception sleepError) {
						sleepError.printStackTrace();
					}
				}
			} catch (Exception commandError) {
				// TODO Auto-generated catch block
				commandError.printStackTrace();
			}
		}
		return allOutput;
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
	
	/* Utility methods */

	/**
	 * Find an RSE host which is currently connected to the
	 * the kalkyl cluster, by checking the hostname of each host
	 * @return
	 */
	public IHost getUppmaxHost() {
		IHost uppmaxHost = null;
		ISystemRegistry reg = SystemStartHere.getSystemRegistry();
		IHost[] hosts = reg.getHosts();
		if (hosts.length == 0) {
			System.out.println("No host names found!");
		}

		for (IHost host : hosts) {
			String hostAlias = host.getAliasName();
			if (hostAlias.equals("kalkyl.uppmax.uu.se")) {
				uppmaxHost = host;
				break;
			}
		}
		return uppmaxHost;
	}

	protected Shell getShell() {
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

	protected ISubSystem getSubSystem() {
		return getFirstSelectedRemoteFile().getParentRemoteFileSubSystem();
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

}
