package net.bioclipse.uppmax.wizards;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
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
		System.out.println("Workbench: " + workbench.toString());
		System.out.println("Selection: " + selection.toString());
		ConfigCommandPage confCommandPage = new ConfigCommandPage(workbench, selection);
		addPage(confCommandPage);
		ConfigParamsPage confParamsPage = new ConfigParamsPage(workbench, selection);
		addPage(confParamsPage);
	}

	@Override
	public boolean performFinish() {
		ConfigCommandPage page1 = (ConfigCommandPage) this.getPage("Page 1");
		ConfigParamsPage page2 = (ConfigParamsPage) this.getPage("Page 2");
		String command = page1.cmbCommand.getText();
		String params = page2.txtCommand.getText();
		setResultingCommand(command + " " + params);
		MessageDialog.openInformation(getShell(), "Resulting command", "Resulting command: " + getResultingCommand());
		Shell shell = workbench.getActiveWorkbenchWindow().getShell();
		return true;
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


}
