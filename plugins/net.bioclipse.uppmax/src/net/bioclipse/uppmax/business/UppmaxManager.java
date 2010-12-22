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

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
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

import net.bioclipse.managers.business.IBioclipseManager;
import net.bioclipse.uppmax.views.UppmaxView;

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
		IRemoteCmdSubSystem cmdss = getRemoteCmdSubSystem();
		if (cmdss != null && cmdss.isConnected()) {
			// Run the command in a visible shell
			RemoteCommandHelpers.runUniversalCommand(getShell(), command, ".", cmdss); //$NON-NLS-1$
		} else {
			MessageDialog.openError(getShell(), "No command subsystem", "Found no command subsystem");
		}
	}

	/**
	 * Gets the Command subsystem associated with the current host
	 */
	public IRemoteCmdSubSystem getRemoteCmdSubSystem() {
		IHost myHost = getSubSystem().getHost();
		IRemoteCmdSubSystem[] subsys = RemoteCommandHelpers.getCmdSubSystems(myHost);
		for (int i = 0; i < subsys.length; i++) {
			if (subsys[i].getSubSystemConfiguration().supportsCommands()) {
				return subsys[i];
			}
		}
		return null;
	}
	
	public void updateProjectInfoView() {
		String temp;
		String allOutput;
		IHost uppmaxHost;
		
		System.out.println("Button was clicked!");
		// updateProjectInfoTable();
		UppmaxView uppmaxView = (UppmaxView) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(UppmaxView.ID);
		if (uppmaxView!=null) {
			System.out.println("Found UVIEW: " + uppmaxView);

			ISystemRegistry reg = SystemStartHere.getSystemRegistry();
			IHost[] hosts = reg.getHosts();
			if (hosts.length == 0) {
				System.out.println("No host names found!");
				uppmaxView.setContents("No host names found!");
			}
			for (IHost host : hosts) {
				String hostAlias = host.getAliasName();
				if (hostAlias.equals("kalkyl.uppmax.uu.se")) {
					uppmaxHost = host;
					IRemoteCmdSubSystem cmdss = RemoteCommandHelpers.getCmdSubSystem(uppmaxHost);
					SimpleCommandOperation simpleCommandOp = new SimpleCommandOperation(cmdss, new RemoteFileEmpty(), true);
					try {
						allOutput = "";
						temp = "";
						simpleCommandOp.runCommand("projinfo", true);
						for (int i=0;i<100;i++) {
							temp = "";
							temp = simpleCommandOp.readLine(true);
							if (temp == null) {
								System.out.println("Temp is nulĺ!");
							} else if (temp.equals("")) {
								System.out.println("*** readLine returned empty result!");
							} else {
								System.out.println("Temp: " + temp);
								allOutput += temp + "\n";
							}		
							try {
								Thread.sleep(25);
							} catch (Exception e4) {
								e4.printStackTrace();
							}
						}
						uppmaxView.setContents(allOutput);
					} catch (Exception e3) {
						// TODO Auto-generated catch block
						e3.printStackTrace();
					}
					break;
				}
			}
		} else {
			System.out.println("No View found!");
		}
	}
	
	/* Utility methods */
	
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
	
}
