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
package net.bioclipse.hpc.business;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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

import net.bioclipse.hpc.Activator;
import net.bioclipse.hpc.domains.application.HPCApplication;
import net.bioclipse.hpc.domains.toolconfig.ToolConfigDomain;
import net.bioclipse.hpc.views.JobInfoView;
import net.bioclipse.hpc.views.ProjInfoView;
import net.bioclipse.hpc.wizards.ExecuteCommandWizard;
import net.bioclipse.managers.business.IBioclipseManager;

import org.apache.log4j.Logger;
import org.eclipse.rse.core.*;
import org.eclipse.rse.core.model.IHost;
import org.eclipse.rse.core.model.ISystemRegistry;
import org.eclipse.rse.core.model.SystemStartHere;
import org.eclipse.rse.core.subsystems.ISubSystem;
import org.eclipse.rse.subsystems.shells.core.*;
import org.eclipse.rse.subsystems.shells.core.subsystems.IRemoteCmdSubSystem;

public class HPCManager implements IBioclipseManager {

	private static final Logger logger = Logger.getLogger(HPCManager.class);
	public final HPCApplication application = getApplication();

	/**
	 * @wbp.parser.entryPoint
	 */
	public HPCManager() {
	}

	/**
	 * Gives a short one word name of the manager used as variable name when
	 * scripting.
	 */
	public String getManagerName() {
		return "hpc";
	}

	/* 
	 * Main methods (mostly just passed on to the application object) 
	 */

	public Map<String,Object> getClusterInfo() {
		return application.getClusterInfo();
	}
	
	public Map<String,Object> getUserInfo() {
		return application.getUserInfo();
	}
	
	public List<String> getModulesForBinary(String binaryName) {
		return application.getModulesForBinary(binaryName);
	}
	
	public void executeCommand(String command) {
		application.executeCommand(command);
	}

	public void readToolConfigFiles(String folderPath) {
		application.readToolConfigFiles(folderPath);
	}
	
	public void updateProjInfoView() {
		application.updateProjInfoView();
	}
	
	public void updateJobInfoView() {
		application.updateJobInfoView();
	}
	
	protected HPCApplication getApplication() {
		Activator plugin = Activator.getDefault();
		HPCApplication application = plugin.application;
		return application;
	}

}
