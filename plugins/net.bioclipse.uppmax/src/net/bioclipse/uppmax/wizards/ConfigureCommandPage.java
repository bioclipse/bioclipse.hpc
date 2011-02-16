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
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.custom.StyledTextPrintOptions;
import org.eclipse.swt.internal.gtk.GdkColor;
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
			List<Parameter> parameters = currentTool.getParameters();
			createWidgetsForParams(parameters);
		} else {
			System.out.println("Tool with name '" + selectedToolName + "' not found.");
		}
		
	    this.composite.pack();
	}

	private void createWidgetsForParams(List<Parameter> parameters) {
		for (Parameter parameter : parameters) {
			String paramLabel = parameter.getLabel();
			String paramName = parameter.getName();
			if (paramLabel != "" && paramLabel != null) {
				createLabel(paramLabel);
			} else if (paramName != "" && paramName != null) {
				createLabel(paramLabel);
			}
			createTextField();
		}
	}

	private void createLabel(String labelText) {
		StyledText fieldLabel = new StyledText(this.composite, SWT.RIGHT | SWT.WRAP );
		labelText = UppmaxUtils.ensureEndsWithColon(labelText);
		fieldLabel.setBackground(new Color(null, 240, 240, 240));
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

}
