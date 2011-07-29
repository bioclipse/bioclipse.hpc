package net.bioclipse.hpc.wizards;

import net.bioclipse.hpc.Activator;
import net.bioclipse.hpc.business.HPCManager;
import net.bioclipse.hpc.business.HPCUtils;
import net.bioclipse.hpc.domains.application.HPCApplication;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.rse.subsystems.files.core.subsystems.IRemoteFile;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

public class ExecuteCommandWizard extends Wizard implements INewWizard {
	
	private String resultingCommand;
	// the workbench instance
	protected IWorkbench workbench;
	// workbench selection when the wizard was started
	protected IStructuredSelection selection;

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
		// Nothing so far
	}
	
	public void addPages() {
		// TODO: Clean up test code
		Object selObj = selection.getFirstElement();
		if (selObj instanceof IRemoteFile){
			IRemoteFile remFile = (IRemoteFile) selObj;
			System.out.println("Selected remote file path: " + remFile.getAbsolutePath());
		}
		
		// TODO: Clean up test code
		System.out.println("Workbench: " + workbench.toString());
		System.out.println("Selection: " + selection.toString());

		SelectToolGroupPage toolGroupPage = new SelectToolGroupPage(workbench, selection);
		addPage(toolGroupPage);
		
		SelectToolPage toolPage = new SelectToolPage(workbench, selection);
		addPage(toolPage);
		
		ConfigureCommandPage configCommandPage = new ConfigureCommandPage(workbench, selection);
		addPage(configCommandPage);
		
		ConfigureSbatchCommands configSbatchPage = new ConfigureSbatchCommands(workbench, selection);
		addPage(configSbatchPage);
	}

	@Override
	public boolean performFinish() {

		ConfigureCommandPage page = (ConfigureCommandPage) this.getPage("Page 3");
		String command = page.getCommandText();
		String dateTimeStamp = HPCUtils.dateTimeStamp();
		String fileName = "temp-command-file." + dateTimeStamp + ".sh";
		String scriptString = "";

		// Save SBATCH file here instead ...
		scriptString = addLineToScript("#!/bin/bash -l", scriptString, fileName);
		scriptString = addLineToScript(command, scriptString, fileName);
		
		getApplication().executeRemoteCommand(scriptString);
		return true;
	}
	
	public String addLineToScript(String line, String scriptString, String scriptFileName) {
		line = escapeDoubleQuotes(line);
		scriptString += "echo '" + line + "' >> " + scriptFileName + ";";
		return scriptString;
	}
	
	private String escapeDoubleQuotes(String line) {
		line = line.replace("\"", "\\\"");
		return line;
	}

	public boolean canFinish() {
		return true;
	}

	public String getResultingCommand() {
		return resultingCommand;
	}

	public void setResultingCommand(String resultingCommand) {
		this.resultingCommand = resultingCommand;
	}
	
	protected HPCApplication getApplication() {
		Activator plugin = Activator.getDefault();
		HPCApplication application = plugin.application;
		return application;
	}

}
