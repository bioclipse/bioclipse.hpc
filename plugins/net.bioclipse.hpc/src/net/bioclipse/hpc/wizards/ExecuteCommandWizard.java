package net.bioclipse.hpc.wizards;

import net.bioclipse.hpc.domains.application.HPCUtils;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.rse.core.events.ISystemResourceChangeEvents;
import org.eclipse.rse.core.events.SystemResourceChangeEvent;
import org.eclipse.rse.core.model.ISystemRegistry;
import org.eclipse.rse.core.model.SystemStartHere;
import org.eclipse.rse.subsystems.files.core.model.RemoteFileUtility;
import org.eclipse.rse.subsystems.files.core.subsystems.IRemoteFileSubSystem;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExecuteCommandWizard extends Wizard implements INewWizard {

	public boolean canFinish;
	private String resultingCommand;
	// the workbench instance
	protected IWorkbench workbench;
	// workbench selection when the wizard was started
	protected IStructuredSelection selection;
	private static final Logger logger = LoggerFactory.getLogger(ExecuteCommandWizard.class);	

	public ExecuteCommandWizard() {
		// TODO Auto-generated constructor stub
	}

	@Override
	/**
	 * @see IWorkbenchWizard#init(IWorkbench, IStructuredSelection)
	 */
	public void init(IWorkbench workbench, IStructuredSelection selection) 
	{
		this.workbench = workbench;
		this.selection = selection;
	}

	public void createControl(Composite parent) {
		//
	}

	public void addPages() {
		// TODO: Clean up test code
		logger.debug("Selected file path: " + HPCUtils.getFileAbsolutePathFromSelection(selection));

		SelectToolGroupPage toolGroupPage = new SelectToolGroupPage(workbench, selection);
		addPage(toolGroupPage);

		SelectToolPage toolPage = new SelectToolPage(workbench, selection);
		addPage(toolPage);

		ConfigureCommandPage configCommandPage = new ConfigureCommandPage(workbench, selection);
		addPage(configCommandPage);

		ConfigureSbatchScriptPage configSbatchPage = new ConfigureSbatchScriptPage(workbench, selection);
		addPage(configSbatchPage);
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean performFinish() {

		// ConfigureCommandPage cmdPage = (ConfigureCommandPage) this.getPage("Page 3");
		// String command = cmdPage.getCommandText();

		ConfigureSbatchScriptPage sbatchPage = (ConfigureSbatchScriptPage) this.getPage("Page 4");
		String sbatchParam = sbatchPage.getResultingSbatchAndCommandText();

		String dateTimeStamp = HPCUtils.dateTimeStamp();
		String fileName = "autogenerated-job-script." + dateTimeStamp + ".sh";
		String scriptString = "";

		// Save SBATCH file here instead ...
		scriptString = addLineToScript("#!/bin/bash -l", scriptString, fileName);
		scriptString = addLineToScript(sbatchParam, scriptString, fileName);
		// Command line already added in sbatch config page
		// scriptString = addLineToScript(command, scriptString, fileName);

		HPCUtils.getApplication().execRemoteCommand(scriptString);

		// Update the file browser, to show any newly created files
		IRemoteFileSubSystem rfss = RemoteFileUtility.getFileSubSystem(HPCUtils.getApplication().getHPCHost());
		SystemResourceChangeEvent refreshFileSubStystemEvent = new SystemResourceChangeEvent(this, 
				ISystemResourceChangeEvents.EVENT_REFRESH_SELECTED_PARENT, rfss);
		ISystemRegistry registry = SystemStartHere.getSystemRegistry();
		registry.fireEvent(refreshFileSubStystemEvent);

		return true;
	}

	public String addLineToScript(String line, String scriptString, String scriptFileName) {
		line = escapeSpecialChars(line);
		scriptString += "echo '" + line + "' >> " + scriptFileName + ";";
		return scriptString;
	}

	private String escapeSpecialChars(String line) {
		// line = line.replace("\"", "\\\"");
		return line;
	}

	public boolean canFinish() {
		return canFinish;
	}

	public String getResultingCommand() {
		return resultingCommand;
	}

	public void setResultingCommand(String resultingCommand) {
		this.resultingCommand = resultingCommand;
	}

	public void setCanFinish(boolean b) {
		canFinish = b;
	}

}
