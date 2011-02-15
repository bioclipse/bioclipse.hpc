package net.bioclipse.uppmax.wizards;

import java.util.ArrayList;
import java.util.List;

import net.bioclipse.uppmax.toolconfig.Parameter;
import net.bioclipse.uppmax.toolconfig.Tool;
import net.bioclipse.uppmax.toolconfig.ToolConfigPool;
import net.bioclipse.uppmax.xmldisplay.XmlUtils;

import org.eclipse.jface.viewers.IStructuredSelection;
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
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

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
	    // create the composite to hold the widgets
		composite =  new Composite(parent, SWT.NULL);

	    // create the desired layout for this wizard page
		GridLayout gl = new GridLayout();
		int ncol = 2;
		gl.numColumns = ncol;
		composite.setLayout(gl);


	    // set the composite as the control for this page
		setControl(composite);
	}
	
	void onEnterPage() {
		
		createControl(parentComposite);
		
		System.out.println("Entered page!");
		Combo comboTool = ((SelectToolPage) this.getWizard().getPage("Page 2")).comboTool;
		
		String selectedToolName = comboTool.getText();
		Tool currentTool = ToolConfigPool.getInstance().getToolByName(selectedToolName);
		if (currentTool != null) {
			System.out.println("Current tool name: " + currentTool.getName());
			List<Parameter> parameters = currentTool.getParameters();
			List<String> parameterNames = new ArrayList<String>();
			List<String> parameterLabels = new ArrayList<String>();
			for (Parameter parameter : parameters) {
				parameterNames.add(parameter.getName());
				parameterLabels.add(parameter.getLabel());
			}
			if (parameterNames != null) {
				for (String labelName : parameterLabels) {
					Label fieldLabel = new Label(this.composite, SWT.NONE);
					fieldLabel.setText(labelName + ":");
					fieldLabel.setSize(320, 24);
					GridData labelGridData = new GridData(GridData.FILL_HORIZONTAL);
					labelGridData.horizontalAlignment = GridData.END;
					fieldLabel.setLayoutData(labelGridData);
					
					Text textField = new Text(this.composite, SWT.BORDER);
					textField.setSize(240, 24);
					GridData textGridData = new GridData(GridData.VERTICAL_ALIGN_END);
					// textGridData.horizontalAlignment = GridData.BEGINNING;
					textField.setLayoutData(textGridData);
				}
				((ConfigureCommandPage) this.getWizard().getPage("Page 3")).createWidgetsForParams(parameterLabels);
			} 
		} else {
			System.out.println("Tool not found, with name. " + selectedToolName);
		}
		
	    this.composite.pack();
	}


	@Override
	public void handleEvent(Event event) {
		// TODO Auto-generated method stub

	}

	public void createWidgetsForParams(List<String> parameterLabels) {
		// TODO Auto-generated method stub
		
	}

}
