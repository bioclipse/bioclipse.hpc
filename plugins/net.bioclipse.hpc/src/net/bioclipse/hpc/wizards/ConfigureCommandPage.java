package net.bioclipse.hpc.wizards;

import java.util.ArrayList;
import java.util.List;

import net.bioclipse.hpc.domains.application.HPCUtils;
import net.bioclipse.hpc.domains.application.HPCApplication.InfoType;
import net.bioclipse.hpc.domains.toolconfig.Option;
import net.bioclipse.hpc.domains.toolconfig.Parameter;
import net.bioclipse.hpc.domains.toolconfig.Tool;
import net.bioclipse.hpc.domains.toolconfig.ToolConfigDomain;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
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
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
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
	Composite contentComposite;
	ScrolledComposite scrollComposite;
	List<Parameter> toolParams;
	StyledText commandText;
	Tool currentTool;
	List<Widget> widgets;
	private static Logger log;
	
	/**
	 * Constructor. Initialize Class variables etc.
	 * @param workbench
	 * @param selection
	 */
	protected ConfigureCommandPage(IWorkbench workbench, IStructuredSelection selection) {
		super("Configure Command Page");
		setTitle("Select tool");
		setDescription("Select a tool from the ones available in the tool group just selected ...");
		this.workbench = workbench;
		this.selection = selection;
		this.toolParams = new ArrayList<Parameter>();
		this.widgets = new ArrayList<Widget>();
		this.log = LoggerFactory.getLogger(ConfigureCommandPage.class);
	}
	
	/**
	 * Set whether the "Next" button is active
	 */
	public boolean canFlipToNextPage() {
		return true;
	}
	
	/**
	 * Tells what happens when we enter the page. 
	 * Note that different things need to happen depending on if it is the first
	 * time we enter the page, or not, and whether we have changed the selected
	 * tool in the previous page or not etc.
	 */
	void onEnterPage() {
		String toolSelectedInCombo = getToolSelectedInComboAsText();
		if ((this.currentTool == null) || (!toolSelectedInCombo.equals(this.currentTool.getCommand()))) {
			this.currentTool = getToolByName(toolSelectedInCombo);
			drawPageForTool(this.currentTool);
			updateSbatchPageWithModulesForBinary(this.currentTool.getBinary());
			clearLayoutCache();
		}
	}

	/**
	 * Needs to clear layout cache for some reason. 
	 * For more info, see: http://stackoverflow.com/questions/586414
	 */
	private void clearLayoutCache() {
		getShell().layout(true, true);
	}

	/**
	 * @param toolSelectedInCombo
	 * @return
	 */
	private Tool getToolByName(String toolSelectedInCombo) {
		return ToolConfigDomain.getInstance().getToolByName(toolSelectedInCombo);
	}

	/**
	 * Get the name of the currently selected tool on the tool select page.
	 * Note that this is not the same as the "currentTool", but instead
	 * the return value of this function can be used to update the currentTool
	 * variable at the appropriate time. 
	 * @return
	 */
	private String getToolSelectedInComboAsText() {
		Combo comboTool = getToolSelectCombo();
		String selectedToolName = comboTool.getText();
		return selectedToolName;
	}

	/**
	 * Get the widget (a dropbox / combo, in this case) where the current 
	 * selected tool is set
	 * @return comboTool
	 */
	private Combo getToolSelectCombo() {
		Combo comboTool = ((SelectToolPage) this.getWizard().getPage("Select Tool Page")).comboTool;
		return comboTool;
	}
	
	/**
	 * Update the SBATCH page with the modules that contain a binary with name
	 * binaryName. Useful to run every time a new tool is selected.
	 * Is run as a background job, as not to freeze the user interface. 
	 * @param binaryName
	 */
	private void updateSbatchPageWithModulesForBinary(final String binaryName) {
		Job bgJob = new Job("Retrieving modules for " + binaryName + " ...") {
			List<String> modules;
			protected IStatus run(IProgressMonitor monitor) {
				modules = HPCUtils.getApplication().getModulesForBinary(binaryName);
				System.out.println("Tool name: " + binaryName);
				((ConfigureSbatchScriptPage) getWizard().getPage("Configure Sbatch Page")).setModulesForCommand(modules);
				return Status.OK_STATUS;
			}
		};
		bgJob.setPriority(Job.SHORT);
		bgJob.schedule();
	}

	/**
	 * The main method for drawing the wizard page.
	 * @param tool
	 */
	private void drawPageForTool(Tool tool) {
		createControl(parentComposite);
		toolParams = tool.getParameters();
		for (Parameter param : toolParams) {
			createWidgetsForParam(param);
		}
		String commandStr = tool.getFullCommand();
		createCommandTextbox(commandStr);
	    packComposites();
	}

	/**
	 * Pack both the scrolled composite, and the content composite (contained
	 * INSIDE the scrolled composite), in the correct order.
	 */
	private void packComposites() {
		// Order of the packing is important here!
		this.contentComposite.pack();
        this.scrollComposite.pack();
        this.scrollComposite.setMinSize(this.contentComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
	}

	/**
	 * Create composites (scroll- and content-) and connect with each other.
	 */
	@Override
	public void createControl(Composite parent) {
		// Initialization
		this.parentComposite = parent;
		this.scrollComposite = new ScrolledComposite(parent, SWT.V_SCROLL | SWT.H_SCROLL | SWT.BORDER );		
		this.contentComposite =  new Composite(scrollComposite, SWT.NULL);

		// Connecting things
		HPCUtils.createGridLayout(contentComposite, 3);
        scrollComposite.setContent(contentComposite);
        scrollComposite.setExpandHorizontal(true);
        scrollComposite.setExpandVertical(true);
		setControl(scrollComposite);
	}
	
	private void createCommandTextbox(String commandString) {
		// Create widget
		createLabel("Resulting command");
		commandText = new StyledText(contentComposite, SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL );
		commandText.setText(commandString);
	
		// Create layout data object and connect
		GridData gridLayoutData = new GridData( SWT.NONE|GridData.FILL_BOTH );
		gridLayoutData.horizontalSpan = 2;
		gridLayoutData.grabExcessHorizontalSpace = true;
		gridLayoutData.minimumHeight = 120;
		gridLayoutData.widthHint = 200; 
		commandText.setLayoutData(gridLayoutData);
	}

	private void createWidgetsForParam(Parameter param) {
		String label = param.getLabel();
		String name = param.getName();
		String type = param.getType();
		String paramType = param.getParamType();

		if (paramType.equals("normal")) {
			if (label != "" && label != null) {
				createLabel(label);
			} else if (!name.equals("") && name != null) {
				createLabel(name);
			}
			
			String paramValue = param.getValue();
			if (paramValue == null) {
				paramValue = "";
			}

			int optionsCnt = param.getSelectOptions().size();
			
			// This is a way to recognize fields for specifying "input data file"
			// TODO: Use more different kinds of widgets here
			if (type.equals("data")) {
				createSelectRemoteFile(param);
			} else if (type.equals("select") && optionsCnt < 4) {
				createRadioButtons(param, 2);
			} else if (type.equals("select") && optionsCnt >= 4) {
				createComboBox(param, 2);
			} else if (type.equals("boolean")) {
				createCheckBox(param, 2);
			} else {
				createTextField(param, 2);
			} 

		} else if (paramType.equals("output")) {
			createOutputFileNameWidgets(param);
		}
	}

	private void createCheckBox(Parameter param, int horizSpan) {
		Button checkBox = new Button(contentComposite, SWT.CHECK);
		checkBox.setData(param);
		// Not currently needed, since we have a separate label for this.
		// checkBox.setText(param.getLabel());

		GridData gd = new GridData();
		gd.horizontalSpan = horizSpan;
		checkBox.setLayoutData(gd);

		this.widgets.add(checkBox);
	}

	private void createRadioButtons(Parameter parameter, int horizSpan) {
		List<Option> selectOptions = parameter.getSelectOptions();
		Group radioGroup = new Group(contentComposite, SWT.HORIZONTAL);
		radioGroup.setLayout(new RowLayout());
		// radioGroup.setText(parameter.getName());

		GridData gd = new GridData();
		gd.horizontalSpan = horizSpan;
		radioGroup.setLayoutData(gd);
		
		for (Option option : selectOptions) {
			Button btn = createRadioOption(radioGroup, option);
			this.widgets.add(btn);
		}
	}

	/**
	 * @param radioGroup
	 * @param option
	 * @return
	 */
	private Button createRadioOption(Group radioGroup, Option option) {
		Button btn = new Button(radioGroup, SWT.RADIO);
		btn.setData(option);
		btn.setText(option.getText());
		// Event handling stuff
		btn.addListener(SWT.Selection, this);
		return btn;
	}

	private void createLabel(String labelText) {
		StyledText fieldLabel = new StyledText(this.contentComposite, SWT.RIGHT | SWT.WRAP | SWT.READ_ONLY );
		labelText = HPCUtils.ensureEndsWithColon(labelText);
		fieldLabel.setBackground (contentComposite.getBackground());
		fieldLabel.setText(labelText);
		GridData labelGridData = new GridData(GridData.HORIZONTAL_ALIGN_END);
		labelGridData.widthHint = 160;
		fieldLabel.setLayoutData(labelGridData);
	}
	
	private void createSelectRemoteFile(Parameter parameter) {
		// TODO: These are not used, no?
		// ISystemRegistry sysReg = RSECorePlugin.getTheSystemRegistry();
		// final IRSECoreRegistry coreReg = RSECorePlugin.getTheCoreRegistry();
		
		final Text textField = createTextField(parameter, 1);
		
		Button btnBrowseRemoteFiles = new Button(contentComposite, SWT.NONE);
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
					Object obj = dialog.getSelectedObject();
					if (obj instanceof IRemoteFile) {
						IRemoteFile file = (IRemoteFile) obj; 
						textField.setText(file.getAbsolutePath());
						textField.notifyListeners(SWT.KeyUp, new Event());
					} else {
						log.error("No valid file selected!");
					}
				}
			}
		});
	}
	
	private void createComboBox(Parameter parameter, int horizontalSpan) {
		List<String> selectOptions = parameter.getSelectOptionValues();
		Combo currentCombo = Utils.createCombo(contentComposite);

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

	private Text createTextField(Parameter parameter, int horizontalSpan) {
		Text textField = new Text(this.contentComposite, SWT.BORDER);
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
		final Text outputFolder = new Text(this.contentComposite, SWT.BORDER|SWT.READ_ONLY);
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
		
		Button btnBrowseRemoteOutputFolder = new Button(contentComposite, SWT.NONE);
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
						log.error("No valid file selected!");
					}
				}
			}
		});
		
		createLabel("Output filename");
		final Text outputFileName = new Text(this.contentComposite, SWT.BORDER);
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

	/** 
	 * Main event handling loop.
	 */
	@Override
	public void handleEvent(Event event) {
		if (event.type == SWT.Selection || event.type == SWT.KeyUp) {
			String tempCmd = currentTool.getFullCommand();
			for (Widget widget : this.widgets) {
				Object widgetData = widget.getData();
				if (widgetData instanceof Parameter || widgetData instanceof Option) {
					// Make sure we're dealing with a parameter here
					// This is a workaround for the fact that we sometimes have a 
					// parameter and sometimes an option, as widget data.
					Parameter param = null;
					if (widgetData instanceof Parameter) {
						param = (Parameter) widgetData;
					} else if (widgetData instanceof Option) {
						param = ((Option) widgetData).getParameter();
					}

					// Take care of "boolean" flags that can either be present or not
					if (param.getType().equals("boolean")) {
						// tempCmd = replaceParamInStr(tempCmd, param.getName(), falseVal);
						boolean selected = ((Button) widget).getSelection();
						if (selected) {
							String trueValue;
							if (param.getTrueValue() != null) {
								trueValue = param.getTrueValue();
							} else {
								trueValue = "yes";
							}
							tempCmd = replaceParamInStr(tempCmd, param.getName(), trueValue);
						} else {
							String falseValue;
							if (param.getFalseValue() != null) {
								falseValue = param.getFalseValue();
							} else {
								falseValue = "no";
							}
							tempCmd = replaceParamInStr(tempCmd, param.getName(),falseValue);							
						}
					} else { // If not of type "boolean" ...
						String newParamVal = getSelectedParamVal(widget);
						if (param.getIsOptional() && newParamVal.equals("")) {
							tempCmd = removeParamAndFlagInStr(tempCmd, param.getName());
						}

						// From here on, we assume that we're dealing with a parameter
						if (param.getParamType().equals("output")) {
							String outFilePath = newParamVal + "/" + (String) ((Text) getWidgetForData("Output filename")).getText();
							tempCmd = replaceParamInStr(tempCmd, param.getName(), outFilePath);
							this.commandText.setText(tempCmd);
						} else {
							if (param != null && newParamVal != null && !newParamVal.equals("")) {
								tempCmd = replaceParamInStr(tempCmd, param.getName(), newParamVal);
								this.commandText.setText(tempCmd);
							} else {
								// logger.error("Parameter or widget value was null");
							}
						}						
					}
				}
			}
		}
	}

	private String removeParamAndFlagInStr(String tempCmd, String name) {
		String regex = "[ ]*-{1,2}[a-zA-Z0-9]+[ ]+\\$" + name;
		tempCmd = tempCmd.replaceAll(regex, "");
		return tempCmd;
	}

	/**
	 * Does the actual replacement of the parameter pars in the command string
	 * (or any other string, of course)
	 * @param tempCmd
	 * @param paramName
	 * @param newVal
	 * @return
	 */
	private String replaceParamInStr(String tempCmd, String paramName, String newVal) {
		return tempCmd.replace("$" + paramName, newVal);
	}

	/**
	 * @param widget
	 * @return
	 */
	private String getSelectedParamVal(Widget widget) {
		// Get the new value from the widget
		String newParamVal = null;
		if (widget instanceof Combo) {
			newParamVal = ((Combo) widget).getText();
		} else if (widget instanceof Text) {
			newParamVal = ((Text) widget).getText();
		} else if (widget instanceof Button && ((Button) widget).getSelection()) {
			newParamVal = ((Option) ((Button) widget).getData()).getValue();
		} else {
			log.debug("Did not set newValue of widget: " + widget.toString());
			newParamVal = "";
		}
		return newParamVal;
	}

	private Widget getWidgetForData(Object data) {
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
	
	public String getToolName() {
		return this.currentTool.getName();
	}
	
	@Override
	public IWizardPage getNextPage() {
		ConfigureSbatchScriptPage configSbatchScriptPage = ((ConfigureSbatchScriptPage) this.getWizard().getPage("Configure Sbatch Page"));
		configSbatchScriptPage.onEnterPage();
		return configSbatchScriptPage;
	}
}
