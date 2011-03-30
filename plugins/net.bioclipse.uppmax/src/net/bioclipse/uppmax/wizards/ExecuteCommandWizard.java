package net.bioclipse.uppmax.wizards;

import net.bioclipse.uppmax.business.UppmaxManager;

import org.eclipse.jface.viewers.IStructuredSelection;
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
		SelectToolGroupPage toolGroupPage = new SelectToolGroupPage(workbench, selection);
		addPage(toolGroupPage);
		SelectToolPage toolPage = new SelectToolPage(workbench, selection);
		addPage(toolPage);
		ConfigureCommandPage configCommandPage = new ConfigureCommandPage(workbench, selection);
		addPage(configCommandPage);
	}

	@Override
	public boolean performFinish() {

		ConfigureCommandPage page = (ConfigureCommandPage) this.getPage("Page 3");
		String command = page.getCommandText();

		// Save SBATCH file here instead ...
		
		UppmaxManager uppmaxManagerObj = new UppmaxManager();
		uppmaxManagerObj.executeRemoteCommand(command);
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
