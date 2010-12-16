package net.bioclipse.uppmax.views;

import java.util.Vector;

import org.eclipse.rse.core.model.IHost;
import org.eclipse.rse.core.model.ISystemRegistry;
import org.eclipse.rse.core.model.SystemStartHere;
import org.eclipse.rse.shells.ui.RemoteCommandHelpers;
import org.eclipse.rse.subsystems.files.core.subsystems.RemoteFileEmpty;
import org.eclipse.rse.subsystems.files.core.subsystems.RemoteFileRoot;
import org.eclipse.rse.subsystems.shells.core.model.SimpleCommandOperation;
import org.eclipse.rse.subsystems.shells.core.subsystems.IRemoteCmdSubSystem;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

public class UpdateUppmaxView implements Runnable {
	UppmaxView parentView;
	UppmaxView view;

	public UpdateUppmaxView(UppmaxView parentView) {
		this.parentView = parentView;
	}

	@Override
	public void run() {
		boolean shouldCont = true;
		Runnable myRunnable = new Runnable() {
			String commandOutput;
			IHost uppmaxHost;

			@Override
			public void run() {
				UppmaxView uppmaxView = (UppmaxView) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(UppmaxView.ID);
				if (uppmaxView!=null) {
					System.out.println("Found UVIEW: " + uppmaxView);

					ISystemRegistry reg = SystemStartHere.getSystemRegistry();
					IHost[] hosts = reg.getHosts();
					if (hosts.length == 0) {
						System.out.println("No host names found!");
						uppmaxView.setContents("No host names found!");
					}
					for (IHost host : hosts) {
						String hostAlias = host.getAliasName();
						if (hostAlias.equals("kalkyl.uppmax.uu.se")) {
							uppmaxHost = host;
							IRemoteCmdSubSystem cmdss = RemoteCommandHelpers.getCmdSubSystem(uppmaxHost);
							
							SimpleCommandOperation simpleCommandOp = new SimpleCommandOperation(cmdss, new RemoteFileEmpty(), true);
							try {
								simpleCommandOp.runCommand("projinfo", true);
								commandOutput = simpleCommandOp.readLine(false);
								uppmaxView.setContents(commandOutput);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							break;
						}
					}
				} else {
					System.out.println("No View found!");
				}
			}
		}; 

		while (shouldCont) {
			Display.getDefault().asyncExec(myRunnable);

			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				System.out.println("Error: Could not sleep for 1 sec ...");
				e.printStackTrace();
			}
		}

	}
}
