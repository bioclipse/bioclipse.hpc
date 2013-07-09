package net.bioclipse.hpc.views;

import net.bioclipse.hpc.domains.application.HPCApplication;
import net.bioclipse.hpc.domains.application.HPCUtils;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPart;

public class RefreshGalaxyToolConfigsAction implements IViewActionDelegate {

	IWorkbenchPart part;
	ISelection selection;

	@Override
	public void run(IAction action) {
		System.out.println("Trying to read Galaxy config files ...");
		HPCUtils.getApplication().readGalaxyToolConfigFiles();
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
