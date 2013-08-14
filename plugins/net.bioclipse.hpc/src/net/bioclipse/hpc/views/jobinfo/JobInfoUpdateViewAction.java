package net.bioclipse.hpc.views.jobinfo;

import net.bioclipse.hpc.domains.application.HPCUtils;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;

public class JobInfoUpdateViewAction implements IViewActionDelegate {

	@Override
	public void run(IAction arg0) {
		HPCUtils.getApplication().refreshJobInfoView(true);
	}

	@Override
	public void selectionChanged(IAction arg0, ISelection arg1) {}
		
	@Override
	public void init(IViewPart arg0) {}

}
