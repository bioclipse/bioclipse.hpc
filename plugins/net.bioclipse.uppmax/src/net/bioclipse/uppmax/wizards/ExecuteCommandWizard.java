package net.bioclipse.uppmax.wizards;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Composite;
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
		SelectToolGroupPage toolGroupPage = new SelectToolGroupPage(workbench, selection);
		addPage(toolGroupPage);
		SelectToolPage toolPage = new SelectToolPage(workbench, selection);
		addPage(toolPage);
		ConfigureCommandPage configCommandPage = new ConfigureCommandPage(workbench, selection);
		addPage(configCommandPage);
	}

	@Override
	public boolean performFinish() {

//		SelectToolGroupPage page1 = (SelectToolGroupPage) this.getPage("Page 1");
//		SelectToolPage page2 = (SelectToolPage) this.getPage("Page 2");

//		String command = page1.cmbCommand.getText();
//		String params = page2.txtCommand.getText();
//		setResultingCommand(command + " " + params);
//		MessageDialog.openInformation(getShell(), "Resulting command", "Resulting command: " + getResultingCommand());
		
		// TODO: Remove this testing code
//		PrefsUtils.testRetrievePreferences(this);
		
//		Shell shell = workbench.getActiveWorkbenchWindow().getShell();
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
