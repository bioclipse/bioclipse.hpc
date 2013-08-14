package net.bioclipse.hpc.views.projinfo;

import net.bioclipse.hpc.domains.application.HPCUtils;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;

public class UpdateProjInfoViewAction implements IViewActionDelegate {

	@Override
	public void run(IAction arg0) {
		HPCUtils.getApplication().updateProjInfoView();
	}

	@Override
	public void selectionChanged(IAction arg0, ISelection arg1) {}
		
	@Override
	public void init(IViewPart arg0) {}

}