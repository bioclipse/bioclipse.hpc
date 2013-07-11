package net.bioclipse.hpc.wizards;

import java.util.ArrayList;
import java.util.List;

import net.bioclipse.hpc.domains.application.HPCUtils;
import net.bioclipse.hpc.domains.toolconfig.Parameter;
import net.bioclipse.hpc.domains.toolconfig.Tool;
import net.bioclipse.hpc.domains.toolconfig.ToolConfigDomain;
import net.bioclipse.hpc.views.projinfo.ProjInfoView;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.rse.core.model.IHost;
import org.eclipse.rse.files.ui.dialogs.SystemRemoteFileDialog;
import org.eclipse.rse.files.ui.dialogs.SystemRemoteFolderDialog;
import org.eclipse.rse.subsystems.files.core.subsystems.IRemoteFile;
import org.eclipse.rse.ui.SystemBasePlugin;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.IWorkbench;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigureCommandPage extends WizardPage implements Listener {

	IWorkbench workbench;
	IStructuredSelection selection;
	Composite parentComposite;
	Composite composite;
	List<Parameter> parameters;
	StyledText commandText;
	Tool currentTool;
	List<Widget> widgets;
	boolean initialized;
	private static final Logger logger = LoggerFactory.getLogger(ConfigureCommandPage.class);
	
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
		HPCUtils.createGridLayout(composite, 3);
		setControl(composite);
	}
	
	void onEnterPage() {
		if (!this.initialized) {
			this.initialized = true;
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
				logger.error("Tool with name '" + selectedToolName + "' not found.");
			}
			
		    this.composite.pack();
		}
	}

	private void createResultingCommandTextbox(String commandString) {
		createLabel("Resulting command");
		
		commandText = new StyledText(composite, SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL );
		commandText.setText(commandString);
		GridData gridLayoutData = new GridData( SWT.NONE|GridData.FILL_BOTH );
		gridLayoutData.horizontalSpan = 2;
		gridLayoutData.grabExcessHorizontalSpace = true;
		gridLayoutData.minimumHeight = 60;
		gridLayoutData.widthHint = 200;
		commandText.setLayoutData(gridLayoutData);
	}

	private void createWidgetsForParam(Parameter parameter) {
		String label = parameter.getLabel();
		String name = parameter.getName();
		String type = parameter.getType();
		String paramType = parameter.getParamType();

		if (paramType.equals("normal")) {
			if (label != "" && label != null) {
				createLabel(label);
			} else if (!name.equals("") && name != null) {
				createLabel(name);
			}
			
			String paramValue = parameter.getValue();
			if (paramValue == null) {
				paramValue = "";
			}
			
			// This is a way to recognize fields for specifying "input data file"
			// TODO Use more different kinds of widgets here
			
			int optionsCnt = parameter.getSelectOptions().size();
			
			if (type.equals("data")) {
				createSelectRemoteFileForParam(parameter);
			} else if (type.equals("select") && optionsCnt < 4) {
				createRadioButtonsForParam(parameter, 2);
			} else if (type.equals("select") && optionsCnt >= 4) {
				createComboBoxForParam(parameter, 2);
			} else {
				createTextFieldForParam(parameter, 2);
			} 
		} else if (paramType.equals("output")) {
			createOutputFileNameWidgets(parameter);
		}
	}

	private void createRadioButtonsForParam(Parameter parameter, int i) {
		List<String> selectOptions = parameter.getSelectOptionValues();
		Group radioGroup = new Group(composite, SWT.HORIZONTAL);
		radioGroup.setLayout(new RowLayout());
		// radioGroup.setText(parameter.getName());

		GridData layoutData = new GridData();
		layoutData.horizontalSpan = i;
		radioGroup.setLayoutData(layoutData);
		
		for (String optionValue : selectOptions) {
			Button btn = new Button(radioGroup, SWT.RADIO);
			btn.setText(optionValue);
			// Event handling stuff
			btn.addListener(SWT.Selection, this);
			btn.setData(parameter);
			widgets.add(btn);
		}
	}

	private void createLabel(String labelText) {
		StyledText fieldLabel = new StyledText(this.composite, SWT.RIGHT | SWT.WRAP | SWT.READ_ONLY );
		labelText = HPCUtils.ensureEndsWithColon(labelText);
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
				IHost hpcHost = HPCUtils.getApplication().getHPCHost();
				
				if (hpcHost == null) {
					MessageDialog.openWarning(SystemBasePlugin.getActiveWorkbenchShell(), "HPC Host was null!", "HPC Host was null!");
				} else {
					SystemRemoteFileDialog dialog = new SystemRemoteFileDialog(SystemBasePlugin.getActiveWorkbenchShell());

					dialog.setDefaultSystemConnection(hpcHost, true);
					
					dialog.open();
					Object o = dialog.getSelectedObject();
					if (o instanceof IRemoteFile) {
						IRemoteFile file = (IRemoteFile) o; 
						textField.setText(file.getAbsolutePath());
						textField.notifyListeners(SWT.KeyUp, new Event());
					} else {
						logger.error("No valid file selected!");
					}
				}
			}
		});
	}
	
	private void createComboBoxForParam(Parameter parameter, int horizontalSpan) {
		List<String> selectOptions = parameter.getSelectOptionValues();
		Combo currentCombo = Utils.createCombo(composite);

		// Layout stuff
		GridData comboLayoutData = new GridData();
		comboLayoutData.horizontalSpan = horizontalSpan;
		currentCombo.setLayoutData(comboLayoutData);
		
		// Event handling stuff
		currentCombo.addListener(SWT.Selection, this);
		widgets.add((Widget) currentCombo);

		// Populate
		currentCombo.setData(parameter);
		currentCombo.setItems(HPCUtils.stringListToArray(selectOptions));
		if (parameter.getSelectedOption() != null) {
			currentCombo.setText(parameter.getSelectedOption().getValue());
		}
	}

	private Text createTextFieldForParam(Parameter parameter, int horizontalSpan) {
		Text textField = new Text(this.composite, SWT.BORDER);
		textField.setText(parameter.getValue());

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
	
	private void createOutputFileNameWidgets(Parameter parameter) {

		createLabel("Output directory");

		String defaultText = parameter.getValue(); 
		final Text outputFolder = new Text(this.composite, SWT.BORDER|SWT.READ_ONLY);
		outputFolder.setText(defaultText);
		// Connect the widget to it's corresponding parameter
		outputFolder.setData(parameter);
		outputFolder.addListener(SWT.KeyUp, this);
		widgets.add((Widget) outputFolder);
		GridData outputFolderGridData = new GridData(GridData.FILL_HORIZONTAL);
		outputFolderGridData.horizontalAlignment = GridData.HORIZONTAL_ALIGN_BEGINNING;
		outputFolderGridData.widthHint = 248;
		outputFolder.setLayoutData(outputFolderGridData);
		outputFolder.setBackground(new Color(null, new RGB(240, 240, 240)));
		
		Button btnBrowseRemoteOutputFolder = new Button(composite, SWT.NONE);
		btnBrowseRemoteOutputFolder.setText("Browse...");
		
		btnBrowseRemoteOutputFolder.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				IHost hpcHost = HPCUtils.getApplication().getHPCHost();
				
				if (hpcHost == null) {
					MessageDialog.openWarning(SystemBasePlugin.getActiveWorkbenchShell(), "HPC Host was null!", "HPC Host was null!");
				} else {
					SystemRemoteFileDialog dialog = new SystemRemoteFolderDialog(SystemBasePlugin.getActiveWorkbenchShell());

					dialog.setDefaultSystemConnection(hpcHost, true);
					
					dialog.open();
					Object o = dialog.getSelectedObject();
					if (o instanceof IRemoteFile) {
						IRemoteFile file = (IRemoteFile) o; 
						outputFolder.setText(file.getAbsolutePath());
						outputFolder.notifyListeners(SWT.KeyUp, new Event());
					} else {
						logger.error("No valid file selected!");
					}
				}
			}
		});
		
		createLabel("Output filename");
		final Text outputFileName = new Text(this.composite, SWT.BORDER);
		outputFileName.setText(defaultText);
		// Connect the widget to it's corresponding parameter
		outputFileName.setData("Output filename");
		outputFileName.addListener(SWT.KeyUp, this);
		widgets.add((Widget) outputFileName);
		GridData outputFileNameGridData = new GridData(GridData.FILL_HORIZONTAL);
		outputFileNameGridData.horizontalAlignment = GridData.HORIZONTAL_ALIGN_BEGINNING;
		outputFileNameGridData.widthHint = 248;
		outputFileNameGridData.horizontalSpan = 2;
		outputFileName.setLayoutData(outputFileNameGridData);
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
				} else if (widget instanceof Button && ((Button) widget).getSelection()) {
					newValue = ((Button) widget).getText();
				} else {
					logger.error("Could not set newValue");
				}

				Object data = widget.getData();
				if (data instanceof Parameter) {
					Parameter parameter = (Parameter) data;
					if (parameter.getParamType().equals("output")) {
						String outputFolderAndFileName = newValue + "/" + (String) ((Text) getWidgetWithData("Output filename")).getText();
						tempCommand = tempCommand.replace("$" + parameter.getName(), outputFolderAndFileName);
						commandText.setText(tempCommand);
					} else {
						if (parameter != null && newValue != null && !newValue.equals("")) {
							tempCommand = tempCommand.replace("$" + parameter.getName(), newValue);
							commandText.setText(tempCommand);
						} else {
							// logger.error("Parameter or widget value was null");
						}
					}
				} else if (data instanceof String && ((String) data).equals("Output filename")) {
					// Nothing
				}
			}
		}
	}

	private Widget getWidgetWithData(Object data) {
		for (Widget widget : this.widgets) {
			if (widget.getData().equals(data)) {
				return widget;
			} 
		}
		return null;
	}

	public String getCommandText() {
		return commandText.getText();
	}
	
	@Override
	public IWizardPage getNextPage() {
		ConfigureSbatchScriptPage configSbatchScriptPage = ((ConfigureSbatchScriptPage) this.getWizard().getPage("Page 4"));
		configSbatchScriptPage.onEnterPage();
		return configSbatchScriptPage;
	}
}
