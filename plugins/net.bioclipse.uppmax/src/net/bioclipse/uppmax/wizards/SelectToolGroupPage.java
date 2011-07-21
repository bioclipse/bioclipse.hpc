package net.bioclipse.uppmax.wizards;

import net.bioclipse.uppmax.business.UppmaxUtils;
import net.bioclipse.uppmax.toolconfig.ToolConfigDomain;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;

public class SelectToolGroupPage extends WizardPage {

	public Combo comboToolGroup;
	
	public String[] toolGroups;
	
	IWorkbench workbench;
	IStructuredSelection selection;
	IWizard wizard = this.getWizard(); 
	String selectedToolGroup;

	protected SelectToolGroupPage(IWorkbench workbench, IStructuredSelection selection) {
		super("Page 1");
		setTitle("Configure command: Select tool group");
		setDescription("Select the group of commands that you wish to execute");
		this.workbench = workbench;
		this.selection = selection;
	}
	
	@Override
	public void createControl(Composite parent) {
	    // create the composite to hold the widgets
		Composite composite =  new Composite(parent, SWT.NULL);

		initializeGridLayout(composite);
		String labelText = "Tool group:";
		createLabelWithText(composite, labelText);
		comboToolGroup = UppmaxUtils.createCombo(composite);
		
		String[] toolGroups = ToolConfigDomain.getInstance().getToolGroupNames();
		if (toolGroups.length > 0) {
			setItemsOfCombo(toolGroups);
		} else {
			System.err.println("Error: No galaxy tool definitions loaded. Are the XML files in place?");
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
		SelectToolPage selectToolPage = ((SelectToolPage) this.getWizard().getPage("Page 2"));
		selectToolPage.onEnterPage();
		return selectToolPage;
	}

}
