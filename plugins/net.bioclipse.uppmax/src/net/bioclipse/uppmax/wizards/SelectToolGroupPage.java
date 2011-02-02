package net.bioclipse.uppmax.wizards;

import net.bioclipse.uppmax.business.GalaxyConfig;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;

public class SelectToolGroupPage extends WizardPage implements Listener {

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
		GridData gd;
		Composite composite =  new Composite(parent, SWT.NULL);

	    // create the desired layout for this wizard page
		GridLayout gl = new GridLayout();
		int ncol = 2;
		gl.numColumns = ncol;
		composite.setLayout(gl);
		
		new Label (composite, SWT.NONE).setText("Tool group:");
		
		comboToolGroup = new Combo(composite, SWT.BORDER);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalAlignment = GridData.BEGINNING;
		comboToolGroup.setLayoutData(gd);
		comboToolGroup.addListener(SWT.Selection, this);
		
		String[] toolGroups = GalaxyConfig.getToolGroups();
		comboToolGroup.setItems(toolGroups);
		comboToolGroup.setText(comboToolGroup.getItem(0));
		
	    // set the composite as the control for this page
		setControl(composite);		
	}
	
	@Override
	public boolean canFlipToNextPage() {
		return true;
	}
	
	public String getSelectedToolGroup() {
		return selectedToolGroup;
	}

	@Override
	public void handleEvent(Event event) {
		if (event.widget == comboToolGroup) {
			String currentTool = comboToolGroup.getText();
			String[] tools = GalaxyConfig.getToolsForGroup(currentTool);
			((SelectToolPage) this.getWizard().getPage("Page 2")).updateDroplist(tools);
		}
	}

}
