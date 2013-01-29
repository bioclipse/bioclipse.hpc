package net.bioclipse.hpc.wizards;

import net.bioclipse.hpc.business.HPCUtils;
import net.bioclipse.hpc.domains.application.HPCApplication;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

public class ExecScriptAsBatchJobAction implements IObjectActionDelegate {
	
	IWorkbenchPart part;
	ISelection selection;
	
	@Override
	public void run(IAction action) {
		String filePath = HPCUtils.getFileAbsolutePathFromSelection((IStructuredSelection) selection);
		String commandString = "sbatch " + filePath;
		System.out.println("File path: " + filePath);
		HPCApplication app = HPCUtils.getApplication();
		System.out.print("Executing remote command (" + commandString + "):\n" + app.execRemoteCommand(commandString));
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