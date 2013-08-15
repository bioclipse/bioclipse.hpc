package net.bioclipse.hpc.wizards;

import net.bioclipse.hpc.domains.application.HPCUtils;
import net.bioclipse.hpc.domains.toolconfig.ToolConfigDomain;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;

public class SelectToolPage extends WizardPage {
	protected Combo comboTool;
	private IWorkbench workbench;
	private IStructuredSelection selection;

	protected SelectToolPage(IWorkbench workbench, IStructuredSelection selection) {
		// Set the name for this page
		super("Select Tool Page");
		setTitle("Select tool");
		setDescription("Select a tool from the ones available in the tool group just selected ...");
		this.workbench = workbench;
		this.selection = selection;
	}
	
	@Override
	public boolean canFlipToNextPage() {
		// TODO: Don't allow to flip unless a proper tool is selected
		return true;
	}
	
	@Override
	public void createControl(Composite parent) {
		Composite composite =  new Composite(parent, SWT.NULL);
		HPCUtils.createGridLayout(composite, 2);

		String labelText = "Select tool:";
		createLabel(composite, labelText);
		createCombo(composite);
	    
		// set the composite as the control for this page
		setControl(composite);
	}

	private void createLabel(Composite composite, String labelText) {
		GridData gd;
		new Label (composite, SWT.NONE).setText(labelText);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalAlignment = GridData.BEGINNING;
	}

	private void createCombo(Composite composite) {
		GridData gd;
		comboTool = new Combo(composite, SWT.BORDER);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalAlignment = GridData.BEGINNING;
		comboTool.setLayoutData(gd);
		
		String[] emptyStringArray = {"(No tools loaded)"};
		comboTool.setItems(emptyStringArray);
		comboTool.setText(comboTool.getItem(0));
	}

	public void onEnterPage() {
		SelectToolGroupPage selectToolGroupPage = ((SelectToolGroupPage) this.getWizard().getPage("Select ToolGroup Page"));
		String currentToolGroupName = selectToolGroupPage.comboToolGroup.getText();
		String[] toolNames = ToolConfigDomain.getInstance().getToolNamesForGroupName(currentToolGroupName);
		updateDroplist(toolNames);
	}
	
	public void updateDroplist(String[] tools) {
		comboTool.removeAll();
		comboTool.setItems(tools);
		comboTool.setText(comboTool.getItem(0));
	}

	@Override
	public IWizardPage getNextPage() {
		ConfigureCommandPage configCommandPage = ((ConfigureCommandPage) this.getWizard().getPage("Configure Command Page"));
		configCommandPage.onEnterPage();
		return configCommandPage;
	}

}
