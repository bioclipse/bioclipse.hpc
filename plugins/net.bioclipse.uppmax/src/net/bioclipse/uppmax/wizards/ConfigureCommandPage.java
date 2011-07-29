package net.bioclipse.uppmax.wizards;

import java.util.ArrayList;
import java.util.List;

import net.bioclipse.uppmax.business.UppmaxManager;
import net.bioclipse.uppmax.business.UppmaxUtils;
import net.bioclipse.uppmax.domains.toolconfig.Option;
import net.bioclipse.uppmax.domains.toolconfig.Parameter;
import net.bioclipse.uppmax.domains.toolconfig.Tool;
import net.bioclipse.uppmax.domains.toolconfig.ToolConfigDomain;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.rse.core.IRSECoreRegistry;
import org.eclipse.rse.core.RSECorePlugin;
import org.eclipse.rse.core.model.IHost;
import org.eclipse.rse.core.model.ISystemRegistry;
import org.eclipse.rse.files.ui.dialogs.SystemRemoteFileDialog;
import org.eclipse.rse.subsystems.files.core.subsystems.IRemoteFile;
import org.eclipse.rse.ui.SystemBasePlugin;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.custom.StyledTextPrintOptions;
import org.eclipse.swt.internal.gtk.GdkColor;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.IWorkbench;

public class ConfigureCommandPage extends WizardPage implements Listener {

	IWorkbench workbench;
	IStructuredSelection selection;
	Composite parentComposite;
	Composite composite;
	List<Parameter> parameters;
	StyledText commandText;
	Tool currentTool;
	List<Widget> widgets;
	
	protected ConfigureCommandPage(IWorkbench workbench, IStructuredSelection selection) {
		super("Page 3");
		setTitle("Select tool");
		setDescription("Select a tool from the ones available in the tool group just selected ...");
		this.workbench = workbench;
		this.selection = selection;
		this.parameters = new ArrayList<Parameter>();
		this.widgets = new ArrayList<Widget>();
	}
	
	public boolean canFlipToNextPage() {
		return true;
	}
	
	@Override
	public void createControl(Composite parent) {
		parentComposite = parent;
		composite =  new Composite(parent, SWT.NULL);
		UppmaxUtils.createGridLayout(composite, 3);
		setControl(composite);
	}
	
	void onEnterPage() {
		
		createControl(parentComposite);
		
		Combo comboTool = ((SelectToolPage) this.getWizard().getPage("Page 2")).comboTool;
		String selectedToolName = comboTool.getText();
		currentTool = ToolConfigDomain.getInstance().getToolByName(selectedToolName);

		if (currentTool != null) {
			parameters = currentTool.getParameters();
			for (Parameter parameter : parameters) {
				createWidgetsForParam(parameter);
			}
			String commandString = currentTool.getCompleteCommand();
			createResultingCommandTextbox(commandString);
		} else {
			System.out.println("Tool with name '" + selectedToolName + "' not found.");
		}
		
	    this.composite.pack();
	}

	private void createResultingCommandTextbox(String commandString) {
		createLabel("Resulting command");
		
		commandText = new StyledText(composite, SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL );
		commandText.setText(commandString);
		GridData gridLayoutData = new GridData( SWT.NONE|GridData.FILL_BOTH );
		gridLayoutData.horizontalSpan = 2;
		gridLayoutData.grabExcessHorizontalSpace = true;
		gridLayoutData.heightHint = 48;
		gridLayoutData.widthHint = 200;
		commandText.setLayoutData(gridLayoutData);
	}

	private void createWidgetsForParam(Parameter parameter) {
		String paramLabel = parameter.getLabel();
		String paramName = parameter.getName();
		String paramType = parameter.getType();
		
		if (paramLabel != "" && paramLabel != null) {
			createLabel(paramLabel);
		} else if (!paramName.equals("") && paramName != null) {
			createLabel(paramName);
		}
		
		String paramValue = parameter.getValue();
		if (paramValue == null) {
			paramValue = "";
		}
		
		if (paramType.equals("data")) {
			createSelectRemoteFileForParam(parameter);
		} else if (paramType.equals("select")) {
			createComboBoxForParam(parameter, 2);
		} else {
			createTextFieldForParam(parameter, 2);
		} 
	}

	private void createLabel(String labelText) {
		StyledText fieldLabel = new StyledText(this.composite, SWT.RIGHT | SWT.WRAP | SWT.READ_ONLY );
		labelText = UppmaxUtils.ensureEndsWithColon(labelText);
		fieldLabel.setBackground (composite.getBackground());
		fieldLabel.setText(labelText);
		GridData labelGridData = new GridData(GridData.HORIZONTAL_ALIGN_END);
		labelGridData.widthHint = 160;
		fieldLabel.setLayoutData(labelGridData);
	}
	
	private void createSelectRemoteFileForParam(Parameter parameter) {
		// TODO: These are not used, no?
		// ISystemRegistry sysReg = RSECorePlugin.getTheSystemRegistry();
		// final IRSECoreRegistry coreReg = RSECorePlugin.getTheCoreRegistry();
		
		final Text textField = createTextFieldForParam(parameter, 1);
		
		Button btnBrowseRemoteFiles = new Button(composite, SWT.NONE);
		btnBrowseRemoteFiles.setText("Browse...");
		btnBrowseRemoteFiles.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				UppmaxManager uppmaxMgr = new UppmaxManager();
				IHost uppmaxHost = uppmaxMgr.getUppmaxHost();
				
				if (uppmaxHost == null) {
					MessageDialog.openWarning(SystemBasePlugin.getActiveWorkbenchShell(), "uppmaxHost was null!", "uppmaxHost was null!");
				} else {
					SystemRemoteFileDialog dialog = new SystemRemoteFileDialog(SystemBasePlugin.getActiveWorkbenchShell());

					dialog.setDefaultSystemConnection(uppmaxHost, true);
					
					dialog.open();
					Object o = dialog.getSelectedObject();
					if (o instanceof IRemoteFile) {
						IRemoteFile file = (IRemoteFile) o; 
						textField.setText(file.getAbsolutePath());
						textField.notifyListeners(SWT.KeyUp, new Event());
					} else {
						System.out.println("No valid file selected!");
					}
				}
			}
		});
	}
	
	private void createComboBoxForParam(Parameter parameter, int horizontalSpan) {
		List<String> selectOptions = parameter.getSelectOptionValues();
		Combo currentCombo = UppmaxUtils.createCombo(composite);

		// Layout stuff
		GridData comboLayoutData = new GridData();
		comboLayoutData.horizontalSpan = horizontalSpan;
		currentCombo.setLayoutData(comboLayoutData);
		
		// Misc stuff
		currentCombo.addListener(SWT.Selection, this);
		widgets.add((Widget) currentCombo);

		// Populate
		currentCombo.setData(parameter);
		String[] selectOptionsArr = UppmaxUtils.stringListToArray(selectOptions);
		currentCombo.setItems(selectOptionsArr);
		Option selectedOption = parameter.getSelectedOption();
		if (selectedOption != null) {
			String selectedOptionValue = selectedOption.getValue();
			currentCombo.setText(selectedOptionValue);
		}
	}

	private Text createTextFieldForParam(Parameter parameter, int horizontalSpan) {
		String defaultText = parameter.getValue(); 
		Text textField = new Text(this.composite, SWT.BORDER);
		textField.setText(defaultText);
		// Connect the widget to it's corresponding parameter
		textField.setData(parameter);
		textField.addListener(SWT.KeyUp, this);
		widgets.add((Widget) textField);
		GridData textGridData = new GridData(GridData.FILL_HORIZONTAL);
		textGridData.horizontalAlignment = GridData.HORIZONTAL_ALIGN_BEGINNING;
		textGridData.horizontalSpan = horizontalSpan;
		textGridData.widthHint = 248;
		textField.setLayoutData(textGridData);
		return textField;
	}

	@Override
	public void handleEvent(Event event) {
		if (event.type == SWT.Selection || event.type == SWT.KeyUp) {
			String tempCommand = currentTool.getCompleteCommand();
			for (Widget widget : this.widgets) {
				// Get the new value from the widget
				String newValue = null;
				if (widget instanceof Combo) {
					newValue = ((Combo) widget).getText();
				} else if (widget instanceof Text) {
					newValue = ((Text) widget).getText();
				} else {
					System.out.println("Could not set newValue");
				}

				Parameter parameter = (Parameter) widget.getData();
				if (parameter != null && newValue != null && !newValue.equals("")) {
					tempCommand = tempCommand.replace("$" + parameter.getName(), newValue);
					commandText.setText(tempCommand);
				} else {
					System.out.println("ERROR: Parameter or widget value was null");
				}
			}
		}
	}

	public String getCommandText() {
		return commandText.getText();
	}

}
