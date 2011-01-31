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

import java.util.List;

import org.eclipse.rse.subsystems.files.core.subsystems.IRemoteFile;
import net.bioclipse.core.PublishedClass;
import net.bioclipse.core.PublishedMethod;
import net.bioclipse.core.Recorded;
import net.bioclipse.managers.business.IBioclipseManager;

@PublishedClass(
    value="TODO: Describe the manager here."
)
public interface IUppmaxManager extends IBioclipseManager {

	
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
	public List<String> readToolConfigFiles(String folderPath);
	
}
