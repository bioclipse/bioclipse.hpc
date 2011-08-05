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

import net.bioclipse.core.PublishedClass;
import net.bioclipse.core.PublishedMethod;
import net.bioclipse.core.Recorded;
import net.bioclipse.managers.business.IBioclipseManager;

@PublishedClass(
    value="Manager for the HPC integration functionality in Bioclipse."
)
public interface IHPCManager extends IBioclipseManager {

	
	@Recorded
	@PublishedMethod(
			params="", 
			methodSummary="Returns info about the currently logged in user, such as user name and projects in which he/she is a member"
	)
	public Map<String,String> getUserInfo();

	@Recorded
	@PublishedMethod(
			params="", 
			methodSummary="Returns info about the currently logged in user, such as user name and projects in which he/she is a member"
	)
	public Map<String,Object> getClusterInfo();

	@Recorded
	@PublishedMethod(
			params="String binaryName", 
			methodSummary="Shows a list of the HPC modules, in which a binary of the specified name are found"
	)
	public List<String> getModulesForBinary(String binaryName);
	
	@Recorded
	@PublishedMethod(
			params="String command", 
			methodSummary="Executes a command"
	)
	public void executeCommand(String command);

	@Recorded
	@PublishedMethod(
			params="List<String> folderPath", 
			methodSummary="Reads a directory with subdirectories containing Galaxy tool config files"
	)
	public void readToolConfigFiles(String folderPath);
	
}
