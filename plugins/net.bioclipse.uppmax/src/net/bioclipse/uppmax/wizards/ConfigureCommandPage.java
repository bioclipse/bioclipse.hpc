package net.bioclipse.uppmax.wizards;

import java.util.ArrayList;
import java.util.List;

import net.bioclipse.uppmax.business.UppmaxUtils;
import net.bioclipse.uppmax.toolconfig.Option;
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
	List<Parameter> parameters;

	protected ConfigureCommandPage(IWorkbench workbench, IStructuredSelection selection) {
		super("Page 3");
		setTitle("Select tool");
		setDescription("Select a tool from the ones available in the tool group just selected ...");
		this.workbench = workbench;
		this.selection = selection;
		this.parameters = new ArrayList<Parameter>();
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
			parameters = currentTool.getParameters();
			for (Parameter parameter : parameters) {
				createWidgetsForParam(parameter);
			}
			String commandString = currentTool.getCompleteCommand();
			createCommandTextbox(commandString);
		} else {
			System.out.println("Tool with name '" + selectedToolName + "' not found.");
		}
		
	    this.composite.pack();
	}

	private void createCommandTextbox(String commandString) {
		StyledText commandText = new StyledText(composite, SWT.BORDER);
		commandText.setText(commandString);
		GridData gridLayoutData = new GridData( SWT.NONE );
		gridLayoutData.horizontalSpan = 2;
		commandText.setLayoutData(gridLayoutData);
	}

	private void createWidgetsForParam(Parameter parameter) {
		String paramLabel = parameter.getLabel();
		String paramName = parameter.getName();
		String paramType = parameter.getType();
		if (paramLabel != "" && paramLabel != null) {
			createLabel(paramLabel);
		} else if (!paramName.equals("") && paramName != null) {
			createLabel(paramLabel);
		}
		String paramValue = parameter.getValue();
		if (paramValue == null) {
			paramValue = "";
		}
		if (paramType.equals("select")) {
			List<String> selectOptions = parameter.getSelectOptionValues();
			Combo currentCombo = UppmaxUtils.createCombo(composite);
			String[] selectOptionsArr = UppmaxUtils.stringListToArray(selectOptions);
			currentCombo.setItems(selectOptionsArr);
			Option selectedOption = parameter.getSelectedOption();
			if (selectedOption != null) {
				String selectedOptionValue = selectedOption.getValue();
				currentCombo.setText(selectedOptionValue);
			}
		} else {
			createTextField(paramValue);
		} 
	}

	private void createLabel(String labelText) {
		StyledText fieldLabel = new StyledText(this.composite, SWT.RIGHT | SWT.WRAP | SWT.READ_ONLY );
		labelText = UppmaxUtils.ensureEndsWithColon(labelText);
		fieldLabel.setText(labelText);
		GridData labelGridData = new GridData(GridData.HORIZONTAL_ALIGN_END);
		labelGridData.widthHint = 160;
		fieldLabel.setLayoutData(labelGridData);
	}

	private void createTextField(String defaultText) {
		Text textField = new Text(this.composite, SWT.BORDER);
		textField.setText(defaultText);
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
