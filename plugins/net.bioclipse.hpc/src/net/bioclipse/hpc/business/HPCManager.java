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

import java.util.List;
import java.util.Map;

import net.bioclipse.hpc.Activator;
import net.bioclipse.hpc.domains.application.HPCApplication;
import net.bioclipse.managers.business.IBioclipseManager;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

public class HPCManager implements IBioclipseManager {

	//private static final Logger logger = LoggerFactory.getLogger(HPCManager.class);
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
