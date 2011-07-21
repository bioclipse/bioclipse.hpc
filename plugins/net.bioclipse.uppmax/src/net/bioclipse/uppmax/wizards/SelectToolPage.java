package net.bioclipse.uppmax.wizards;

import net.bioclipse.uppmax.business.UppmaxUtils;
import net.bioclipse.uppmax.toolconfig.ToolConfigDomain;

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

	public Combo comboTool;

	IWorkbench workbench;
	IStructuredSelection selection;

	protected SelectToolPage(IWorkbench workbench, IStructuredSelection selection) {
		super("Page 2");
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
	    // create the composite to hold the widgets
		Composite composite =  new Composite(parent, SWT.NULL);
	    UppmaxUtils.createGridLayout(composite, 2);

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
		SelectToolGroupPage selectToolGroupPage = ((SelectToolGroupPage) this.getWizard().getPage("Page 1"));
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
		ConfigureCommandPage configCommandPage = ((ConfigureCommandPage) this.getWizard().getPage("Page 3"));
		configCommandPage.onEnterPage();
		return configCommandPage;
	}

}
