package net.bioclipse.hpc.views;

import net.bioclipse.hpc.business.HPCUtils;
import net.bioclipse.hpc.wizards.ExecuteCommandWizard;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPart;

public class OpenJobConfigWizardAction implements IViewActionDelegate {

	IWorkbenchPart part;
	ISelection selection;
	
	@Override
	public void run(IAction action) {
		HPCUtils.openJobConfigWizard(selection, part);
	}

	@Override
	public void selectionChanged(IAction action, ISelection targetSelection) {
		this.selection = targetSelection;
	}

	@Override
	public void init(IViewPart targetPart) {
		this.part = targetPart;
	}

}
