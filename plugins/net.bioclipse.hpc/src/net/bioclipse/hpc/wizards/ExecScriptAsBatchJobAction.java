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
		HPCApplication app = HPCUtils.getApplication();
		if (app.isLoggedIn()) {
			String filePath = HPCUtils.getFileAbsolutePathFromSelection((IStructuredSelection) selection);
			String commandString = "sbatch " + filePath;
			String cmdOutput = app.execRemoteCommand(commandString);
			if (cmdOutput == null) {
				app.showErrorMessage("Failed to communicate with remote host", "Could not communicate with the remote host. Please check your network connection!");
			} else {
				String JobId = HPCUtils.getMatch("Submitted batch job ([0-9]+)", cmdOutput, 1);
				if (JobId == null) {
					String sbatchOutput = extractSbatchLinesFromCommandOutput(cmdOutput);
					String errMsg = "Could not start job. See SBATCH output below for possible reasons:\n\n" + sbatchOutput;
					app.showErrorMessage("Failed to submit job", errMsg);
				} else {
					app.showInfoMessage("Jub submitted", "Successfully submitted job with id: " + JobId);
					app.refreshJobInfoView(false);				
				}
			}
		} else {
			app.showErrorMessageForNotLoggedIn();
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
	
	private String extractSbatchLinesFromCommandOutput(String commandOutput) {
		String sbatchLines = "";
		for (String cmdOutputLine : commandOutput.split("\n")) {
			if (cmdOutputLine.startsWith("sbatch:")) {
				sbatchLines += cmdOutputLine + "\n";
			}
		}
		return sbatchLines;
	}
}