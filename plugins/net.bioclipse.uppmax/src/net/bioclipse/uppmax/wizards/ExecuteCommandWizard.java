package net.bioclipse.uppmax.wizards;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

public class ExecuteCommandWizard extends Wizard implements INewWizard {
	
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
		ConfigCommandPage configCommandPage = new ConfigCommandPage(workbench, selection);
		addPage(configCommandPage);
	}

	@Override
	public boolean performFinish() {
		// TODO Auto-generated method stub
		return false;
	}

}
