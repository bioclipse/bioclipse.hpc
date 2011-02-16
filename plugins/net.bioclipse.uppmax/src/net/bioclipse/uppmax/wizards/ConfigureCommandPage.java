package net.bioclipse.uppmax.wizards;

import java.util.ArrayList;
import java.util.List;

import net.bioclipse.uppmax.business.UppmaxUtils;
import net.bioclipse.uppmax.toolconfig.Parameter;
import net.bioclipse.uppmax.toolconfig.Tool;
import net.bioclipse.uppmax.toolconfig.ToolConfigPool;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;

public class ConfigureCommandPage extends WizardPage implements Listener {

	IWorkbench workbench;
	IStructuredSelection selection;
	Composite parentComposite;
	Composite composite;

	protected ConfigureCommandPage(IWorkbench workbench, IStructuredSelection selection) {
		super("Page 3");
		setTitle("Select tool");
		setDescription("Select a tool from the ones available in the tool group just selected ...");
		this.workbench = workbench;
		this.selection = selection;
	}
	
	public boolean canFlipToNextPage() {
		return false;
	}
	
	@Override
	public void createControl(Composite parent) {
		parentComposite = parent;
		composite =  new Composite(parent, SWT.NULL);
		UppmaxUtils.createGridLayout(composite, 2);
		setControl(composite);
	}
	
	void onEnterPage() {
		
		createControl(parentComposite);
		
		Combo comboTool = ((SelectToolPage) this.getWizard().getPage("Page 2")).comboTool;
		String selectedToolName = comboTool.getText();
		Tool currentTool = ToolConfigPool.getInstance().getToolByName(selectedToolName);

		if (currentTool != null) {
			System.out.println("Current tool name: " + currentTool.getName());
			List<String> parameterNames = new ArrayList<String>();
			List<String> parameterLabels = new ArrayList<String>();

			List<Parameter> parameters = currentTool.getParameters();
			for (Parameter parameter : parameters) {
				parameterNames.add(parameter.getName());
				parameterLabels.add(parameter.getLabel());
			}
			if (parameterNames != null) {
				for (String labelText : parameterLabels) {
					createLabel(labelText);
					createTextField();
				}
				((ConfigureCommandPage) this.getWizard().getPage("Page 3")).createWidgetsForParams(parameterLabels);
			} 
		} else {
			System.out.println("Tool not found, with name. " + selectedToolName);
		}
		
	    this.composite.pack();
	}

	private void createLabel(String labelText) {
		Label fieldLabel = new Label(this.composite, SWT.RIGHT | SWT.WRAP | SWT.BORDER );
		labelText = UppmaxUtils.ensureEndsWithColon(labelText);
		fieldLabel.setText(labelText);
		GridData labelGridData = new GridData(GridData.HORIZONTAL_ALIGN_END);
		labelGridData.widthHint = 160;
		fieldLabel.setLayoutData(labelGridData);
	}

	private void createTextField() {
		Text textField = new Text(this.composite, SWT.BORDER);
		GridData textGridData = new GridData(GridData.FILL_HORIZONTAL);
		textGridData.horizontalAlignment = GridData.HORIZONTAL_ALIGN_BEGINNING;
		textGridData.widthHint = 248;
		textField.setLayoutData(textGridData);
	}

	@Override
	public void handleEvent(Event event) {
		// TODO Auto-generated method stub

	}

	public void createWidgetsForParams(List<String> parameterLabels) {
		// TODO Auto-generated method stub
		
	}

}
