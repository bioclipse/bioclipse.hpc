package net.bioclipse.hpc.wizards;

import net.bioclipse.hpc.domains.application.HPCApplication;
import net.bioclipse.hpc.domains.application.HPCUtils;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExecScriptAsBatchJobAction implements IObjectActionDelegate {
	
	IWorkbenchPart part;
	ISelection selection;
	private static final Logger logger = LoggerFactory.getLogger(ExecScriptAsBatchJobAction.class);	
	
	@Override
	public void run(IAction action) {
		String filePath = HPCUtils.getFileAbsolutePathFromSelection((IStructuredSelection) selection);
		String commandString = "sbatch " + filePath;
		logger.debug("File path: " + filePath);
		HPCApplication app = HPCUtils.getApplication();
		String cmdOutput = app.execRemoteCommand(commandString);
		String JobId = HPCUtils.getMatch("Submitted batch job ([0-9]+)", cmdOutput, 1);
		logger.debug("Executing remote command (" + commandString + ")");
		HPCUtils.getApplication().showInfoMessage("Jub submitted", "Successfully submitted job with id: " + JobId);
		HPCUtils.getApplication().refreshJobInfoView(false);
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