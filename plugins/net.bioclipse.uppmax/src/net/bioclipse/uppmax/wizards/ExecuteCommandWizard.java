package net.bioclipse.uppmax.wizards;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;
import org.osgi.service.prefs.PreferencesService;

public class ExecuteCommandWizard extends Wizard implements INewWizard {
	
	private String resultingCommand;
	// the workbench instance
	protected IWorkbench workbench;
	// workbench selection when the wizard was started
	protected IStructuredSelection selection;

	public ExecuteCommandWizard() {
		// TODO Auto-generated constructor stub
	}

	@Override
	/**
	 * @see IWorkbenchWizard#init(IWorkbench, IStructuredSelection)
	 */
	public void init(IWorkbench workbench, IStructuredSelection selection) 
	{
		this.workbench = workbench;
		this.selection = selection;
	}
	
	public void createControl(Composite parent) {
		// Nothing so far
	}
	
	public void addPages() {
		System.out.println("Workbench: " + workbench.toString());
		System.out.println("Selection: " + selection.toString());
		ConfigCommandPage confCommandPage = new ConfigCommandPage(workbench, selection);
		addPage(confCommandPage);
		ConfigParamsPage confParamsPage = new ConfigParamsPage(workbench, selection);
		addPage(confParamsPage);
	}

	@Override
	public boolean performFinish() {

		ConfigCommandPage page1 = (ConfigCommandPage) this.getPage("Page 1");
		ConfigParamsPage page2 = (ConfigParamsPage) this.getPage("Page 2");
		String command = page1.cmbCommand.getText();
		String params = page2.txtCommand.getText();
		setResultingCommand(command + " " + params);
		MessageDialog.openInformation(getShell(), "Resulting command", "Resulting command: " + getResultingCommand());
		
		// Testing to retrieve system prefs

		BundleContext context = FrameworkUtil.getBundle(this.getClass()).getBundleContext();
		ServiceReference serviceref = context.getServiceReference(PreferencesService.class.getName());
		PreferencesService service = (PreferencesService) context.getService(serviceref);
		Preferences systemPrefs = service.getSystemPreferences();

		Preferences toolConfigPrefs = systemPrefs.node("toolconfigs");
		try {
			String[] toolGroups = toolConfigPrefs.childrenNames();
			for (String toolGroup : toolGroups) {
				Preferences currentToolGroup = toolConfigPrefs.node(toolGroup);
				System.out.println("\n\n--------------------------------------------\nTool Group: " + toolGroup);
				String[] tools = currentToolGroup.childrenNames();
				for (String name : tools) {
					System.out.println("--------------------------------------------\nTool: " + name);
					Preferences currentTool = currentToolGroup.node(name);
					String prefDescription = currentTool.get("description", "");
					String prefCommand = currentTool.get("command", "");
						System.out.println("Description from prefs: " + prefDescription);
						System.out.println("Command from prefs: " + prefCommand);
				}
				System.out.println("--------------------------------------------");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Shell shell = workbench.getActiveWorkbenchWindow().getShell();
		return true;
	}
	
	public boolean canFinish() {
		return true;
	}

	public String getResultingCommand() {
		return resultingCommand;
	}

	public void setResultingCommand(String resultingCommand) {
		this.resultingCommand = resultingCommand;
	}


}
