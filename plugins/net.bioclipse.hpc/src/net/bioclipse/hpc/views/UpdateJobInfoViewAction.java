package net.bioclipse.hpc.views;

import net.bioclipse.hpc.business.HPCUtils;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;

public class UpdateJobInfoViewAction implements IViewActionDelegate {

	@Override
	public void run(IAction arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void selectionChanged(IAction arg0, ISelection arg1) {
		HPCUtils.getApplication().updateJobInfoView();
	}

	@Override
	public void init(IViewPart arg0) {
		// TODO Auto-generated method stub
		
	}

}
