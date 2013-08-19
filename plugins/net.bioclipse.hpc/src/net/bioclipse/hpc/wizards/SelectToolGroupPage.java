package net.bioclipse.hpc.wizards;

import java.util.Map;

import net.bioclipse.hpc.Activator;
import net.bioclipse.hpc.domains.application.HPCUtils;
import net.bioclipse.hpc.domains.toolconfig.ToolConfigDomain;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SelectToolGroupPage extends WizardPage {
	private static final Logger logger = LoggerFactory.getLogger(SelectToolGroupPage.class);
	public Combo comboToolGroup;
	public String[] toolGroups;
	IWorkbench workbench;
	IStructuredSelection selection;
	IWizard wizard;
	String selectedToolGroup;

	protected SelectToolGroupPage(IWorkbench workbench, IStructuredSelection selection) {
		// Set the name for this page
		super("Select ToolGroup Page");
		setTitle("Configure command: Select tool group");
		setDescription("Select the group of commands that you wish to execute");
		this.workbench = workbench;
		this.selection = selection;
		this.wizard = this.getWizard(); 

		getClusterAndUserInfo();
	}

	/**
	 * Update info about the cluster and the current user,
	 * and update the SBATCH config page with that, to be
	 * used later when configuring SBATCH commands
	 */
	private void getClusterAndUserInfo() {
		System.out.println("Test");
		Job job = new Job("Retrieving cluster info ...") {
			protected IStatus run(IProgressMonitor monitor) {
				Map<String,Object> clusterInfo = HPCUtils.getApplication().getClusterInfo();
				getSbatchConfigPage().setClusterInfo(clusterInfo);
				Map<String,Object> userInfo = HPCUtils.getApplication().getUserInfo();
				getSbatchConfigPage().setUserInfo(userInfo);
				return Status.OK_STATUS;
			}
		};
		job.setPriority(Job.SHORT);
		job.schedule();		
	}

	@Override
	public void createControl(Composite parent) {
		Composite composite =  new Composite(parent, SWT.NULL);
		initializeGridLayout(composite);
		((WizardDialog) this.getWizard().getContainer()).setMinimumPageSize(300, 440);

		String labelText = "Tool group:";
		createLabelWithText(composite, labelText);
		comboToolGroup = Utils.createCombo(composite);

		String[] toolGroups = ToolConfigDomain.getInstance().getToolGroupNames();
		if (toolGroups.length > 0) {
			setItemsOfCombo(toolGroups);
		} else {
			logger.error("No galaxy tool definitions loaded. Are the XML files in place?");
		}

		// set the composite as the control for this page
		setControl(composite);		
	}

	private void setItemsOfCombo(String[] toolGroups) {
		comboToolGroup.setItems(toolGroups);
		comboToolGroup.setText(comboToolGroup.getItem(0));
	}

	private void createLabelWithText(Composite composite, String labelText) {
		new Label (composite, SWT.NONE).setText(labelText);
	}

	private void initializeGridLayout(Composite composite) {
		GridLayout gridLayout = new GridLayout();
		int ncol = 2;
		gridLayout.numColumns = ncol;
		composite.setLayout(gridLayout);
	}

	public void onEnterPage() {

	}
	
	@Override
	public boolean canFlipToNextPage() {
		return true;
	}

	public String getSelectedToolGroup() {
		return selectedToolGroup;
	}

	public Combo getComboToolGroup() {
		return comboToolGroup;
	}

	@Override
	public IWizardPage getNextPage() {
		SelectToolPage selectToolPage = ((SelectToolPage) this.getWizard().getPage("Select Tool Page"));
		selectToolPage.onEnterPage();
		return selectToolPage;
	}
	
	public ConfigureSbatchScriptPage getSbatchConfigPage() {
		ConfigureSbatchScriptPage sbatchConfigPage = ((ConfigureSbatchScriptPage) this.getWizard().getPage("Configure Sbatch Page"));
		return sbatchConfigPage;		
	}

}
