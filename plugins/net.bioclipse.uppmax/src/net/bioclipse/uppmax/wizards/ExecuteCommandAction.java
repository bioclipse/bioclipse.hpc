package net.bioclipse.uppmax.wizards;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

public class ExecuteCommandAction implements IObjectActionDelegate {

	IWorkbenchPart part;
	ISelection selection;
	
	@Override
	public void run(IAction action) {
		ExecuteCommandWizard wizard = new ExecuteCommandWizard();
		if ((selection instanceof IStructuredSelection) || (selection == null)) {
			wizard.init(part.getSite().getWorkbenchWindow().getWorkbench(), (IStructuredSelection)selection);
			
			WizardDialog dialog = new WizardDialog( part.getSite().getShell(), wizard);
			dialog.create();
			dialog.open();
		}

	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		this.selection = selection;
	}

	@Override
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		this.part = targetPart;
	}

}
