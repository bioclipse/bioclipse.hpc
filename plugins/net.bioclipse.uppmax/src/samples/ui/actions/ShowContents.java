package samples.ui.actions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.bioclipse.uppmax.views.UppmaxView;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.rse.core.model.IHost;
import org.eclipse.rse.core.subsystems.ISubSystem;
import org.eclipse.rse.shells.ui.RemoteCommandHelpers;
import org.eclipse.rse.subsystems.files.core.subsystems.IRemoteFile;
import org.eclipse.rse.subsystems.shells.core.model.SimpleCommandOperation;
import org.eclipse.rse.subsystems.shells.core.subsystems.IRemoteCmdSubSystem;
import org.eclipse.rse.ui.SystemBasePlugin;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

/**
 * An action that runs a command to display the contents of a Jar file.
 * The plugin.xml file restricts this action so it only appears for .jar files.
 */
public class ShowContents implements IObjectActionDelegate {
	private List _selectedFiles;
	String temp;

	/**
	 * Constructor for ShowJarContents.
	 */
	public ShowContents() {
		_selectedFiles = new ArrayList();
	}

	protected Shell getShell() {
		return SystemBasePlugin.getActiveWorkbenchShell();
	}

	protected IRemoteFile getFirstSelectedRemoteFile() {
		if (_selectedFiles.size() > 0) {
			return (IRemoteFile) _selectedFiles.get(0);
		}
		return null;
	}

	protected ISubSystem getSubSystem() {
		return getFirstSelectedRemoteFile().getParentRemoteFileSubSystem();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
	 */
	public void run(IAction action) {
		String commandOutput;
		String temp;
		
//		IRemoteFile selectedFile = getFirstSelectedRemoteFile();
//		String cmdToRun = "cat " + selectedFile.getAbsolutePath(); //$NON-NLS-1$
//		try {
//			runCommand(cmdToRun);
//		} catch (Exception e) {
//			String excType = e.getClass().getName();
//			MessageDialog.openError(getShell(), excType, excType + ": " + e.getLocalizedMessage()); //$NON-NLS-1$
//			e.printStackTrace();
//		}

		IRemoteFile selectedFile = getFirstSelectedRemoteFile();
		IHost host = selectedFile.getHost();
		IRemoteFile parentDirectory = selectedFile.getParentRemoteFile();
		IRemoteCmdSubSystem cmdss = RemoteCommandHelpers.getCmdSubSystem(host);
		SimpleCommandOperation simpleCommandOp = new SimpleCommandOperation(cmdss, parentDirectory, true);
		try {
			simpleCommandOp.runCommand("projinfo", false);
			commandOutput = "";
			for (int i=0;i<100;i++) {
				 temp = "";
				 temp = simpleCommandOp.readLine(false);
				 System.out.println("Temp: " + temp);
				 if (temp == null) {
					 System.out.println("Temp is nulĺ!");
				 } else if (temp.equals("")) {
					 System.out.println("Temp is empty!");
				 } else {
					 commandOutput += temp + "...\n";
				 }		
				 try {
					 Thread.sleep(10);
				 } catch (Exception e) {
					 e.printStackTrace();
				 }
			}
			//Also update UPPMAXC view (for now)
			updateUPPMAXView(commandOutput);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private void updateUPPMAXView(String content) {
		IRemoteCmdSubSystem cmdss = getRemoteCmdSubSystem();
		
		//Get the UPPMAX view
		UppmaxView view;
		try {
			view = (UppmaxView) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(UppmaxView.ID);
			view.setContents(content);
		} catch (PartInitException e) {
			e.printStackTrace();
		}
		
	}

	public IRemoteCmdSubSystem getRemoteCmdSubSystem() {
		//get the Command subsystem associated with the current host
		IHost myHost = getSubSystem().getHost();
		IRemoteCmdSubSystem[] subsys = RemoteCommandHelpers.getCmdSubSystems(myHost);
		for (int i = 0; i < subsys.length; i++) {
			if (subsys[i].getSubSystemConfiguration().supportsCommands()) {
				return subsys[i];
			}
		}
		return null;
	}

	public void runCommand(String command) throws Exception {
		IRemoteCmdSubSystem cmdss = getRemoteCmdSubSystem();
		if (cmdss != null && cmdss.isConnected()) {
			// Run the command in a visible shell
			RemoteCommandHelpers.runUniversalCommand(getShell(), command, ".", cmdss); //$NON-NLS-1$
		} else {
			MessageDialog.openError(getShell(), "No command subsystem", "Found no command subsystem");
		}
	}

	public void selectionChanged(org.eclipse.jface.action.IAction action, org.eclipse.jface.viewers.ISelection selection) {
		_selectedFiles.clear();
		// store the selected jars to be used when running
		Iterator theSet = ((IStructuredSelection) selection).iterator();
		while (theSet.hasNext()) {
			Object obj = theSet.next();
			if (obj instanceof IRemoteFile) {
				_selectedFiles.add(obj);
			}
		}
	}

	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
	}
}