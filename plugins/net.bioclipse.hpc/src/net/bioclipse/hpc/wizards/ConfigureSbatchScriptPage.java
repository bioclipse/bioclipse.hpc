package net.bioclipse.hpc.wizards;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import net.bioclipse.hpc.domains.application.HPCUtils;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.IWorkbench;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigureSbatchScriptPage extends WizardPage implements Listener {
	// These are set in the constructor
	IWorkbench workbench;
	IStructuredSelection selection;
	// Taken care of in the createControl(), and onEnterPage() functions
	Composite parentComposite;
	Composite composite;
	String currentToolName;
	
	StyledText sbatchStyledText;
	List<Widget> widgets;
	List<String> modulesForCommand;
	Map<String,Object> clusterInfo;
	Map<String,Object> userInfo;

	boolean initialized = false;
	private static final Logger logger = LoggerFactory.getLogger(ConfigureSbatchScriptPage.class);

	String sbatchTemplate = 
		"#SBATCH -A [project]\n" +
		"#SBATCH -p [partition]\n" +
		"#SBATCH -N [noofnodes]\n" +
		"#SBATCH -n [noofcpus]\n" +
		"#SBATCH -t [runtime]\n" +
		"#SBATCH [qosshort]\n" +
		"#SBATCH -J [jobname]\n" +
		"\n" +
		"module load bioinfo-tools [modulename]\n";
		

	protected ConfigureSbatchScriptPage(IWorkbench workbench, IStructuredSelection selection) {
		super("Configure Sbatch Page");
		setTitle("Configure SBATCH parameters");
		setDescription("Set the parameters for the SBATCH job batch script to be sent to the SLURM resource manager");
		this.workbench = workbench;
		this.selection = selection;
		this.widgets = new ArrayList<Widget>();
		this.currentToolName = "";
		this.modulesForCommand = new ArrayList<String>();
		this.clusterInfo = null;
		this.userInfo = null;
	}

	@Override
	public void createControl(Composite parent) {
		parentComposite = parent;
		composite =  new Composite(parent, SWT.NULL );
		HPCUtils.createGridLayout(composite, 2);
		setControl(composite);
	}

	@SuppressWarnings("unchecked")
	void onEnterPage() {
		// Don't redraw wizard page on second visit
		if (!this.initialized || toolHasChangedSinceLastVisit()) {
			this.initialized = true;
			this.currentToolName = getCommandPage().getToolName();
					
			createControl(parentComposite);
			createSbatchConfigControls();
			createResultingSBatchScriptTextbox();
			updateCodeWindow();

			this.composite.pack();

			((ExecuteCommandWizard) this.getWizard()).setCanFinish(true);
		} else {
			updateCodeWindow();
		}
		getShell().layout(true, true);
	}
	
	/**
	 * Check if the selected tool on the command page has changed
	 * since last visit on this page.
	 * @return
	 */
	private boolean toolHasChangedSinceLastVisit() {
		String toolName = getCommandPage().getToolName();
		return (!toolName.equals(this.currentToolName));
	}

	private void createSbatchConfigControls() {
		// String username = (String) userInfo.get("username");
		List<String> projects = (List<String>) userInfo.get("projects");

		String maxNodesStr = (String) getClusterInfo().get("maxnodes");
		String maxCpusStr = (String) getClusterInfo().get("maxcpus");
		List<String> partitions = (List<String>) getClusterInfo().get("partitions");

		// Populate wizard here
		// Modules relevant to the binary chosen in the previous wizard page
		createLabel("HPC Module to load");
		createComboBox("module", modulesForCommand, 1);

		// -A [project name] | Combo  // TODO: Retrieve the user's project automatic
		createLabel("Project to account");
		createComboBox("project", projects, 1);

		// -p [partition]    | Combo  // Simple list, or get info from cluster?
		createLabel("Partition (type of job)");
		createComboBox("partition", partitions, 1);

		// -N [no of nodes]  | Text-field / up-down number field?
		createLabel("No of Nodes");
		List<String> maxNodesStringList = new ArrayList<String>();
		if (maxNodesStr != null) {
			int maxNodes = Integer.parseInt(maxNodesStr);
			if (maxNodes > 0) {
				int[] nodeNos = HPCUtils.range(1, maxNodes, 1);
				for (int i=0;i<maxNodes;i++) {
					String nodeNoStr = Integer.toString(nodeNos[i]);
					maxNodesStringList.add(nodeNoStr);
				}
			} else {
				logger.error("MaxCPUs is zero!");
			}
		}
		createComboBox("noofnodes", maxNodesStringList, 1);

		// -n [no of cpus]   | Text-field / up-down number field?
		createLabel("No of CPUs");
		List<String> maxCpusStringList = new ArrayList<String>();
		if (maxCpusStr != null) {
			int maxCpus = Integer.parseInt(maxCpusStr);
			if (maxCpus > 0) {
				int[] cpuNos = HPCUtils.range(1, maxCpus, 1);
				for (int i=0;i<maxCpus;i++) {
					String cpuNoStr = Integer.toString(cpuNos[i]);
					maxCpusStringList.add(cpuNoStr);
				}
			} else {
				logger.error("MaxCPUs is zero!");
			}
		}
		createComboBox("noofcpus", maxCpusStringList, 1);

		// -t d-hh:mm:ss     | ?      
		createLabel("Running time (d-hh:mm:ss)"); // TODO: Find a good widget for setting the time?
		createTextField("runtime", 1);

		// --qos=short       | Combo (yes/no)
		createLabel("Activate --qos=short option?");
		createComboBox("qosshort", Arrays.asList("no", "yes"), 1, "no");
		// -J [JobName]      | TextField
		createLabel("Job name");
		createTextField("jobname", 1, "Untitled");
	}

	private void createResultingSBatchScriptTextbox() {
		createLabel("Resulting SBATCH Script");
		sbatchStyledText = new StyledText(composite, SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL ); // SWT.WRAP
		GridData gd = new GridData( SWT.NONE | GridData.FILL_BOTH );
		gd.grabExcessHorizontalSpace = true;
		gd.minimumWidth = 320;
		gd.heightHint = 180;
		sbatchStyledText.setLayoutData(gd);
	}

	private void createComboBox(String identifier, List<String> optionValues, int horizontalSpan, String defValue) {
		Combo currentCombo = Utils.createCombo(composite);
		currentCombo.setData(identifier);

		// Layout stuff
		GridData comboLayoutData = new GridData();
		comboLayoutData.horizontalSpan = horizontalSpan;
		currentCombo.setLayoutData(comboLayoutData);

		// Misc stuff
		currentCombo.addListener(SWT.Selection, this);
		widgets.add((Widget) currentCombo);

		// Populate
		String[] selectOptionsArr = HPCUtils.stringListToArray(optionValues);
		currentCombo.setItems(selectOptionsArr);
		if (defValue != "") {
			currentCombo.setText(defValue);
		}
	}

	// Optional signature without the default value set
	private void createComboBox(String identifier, List<String> optionValues, int horizontalSpan) {
		createComboBox(identifier, optionValues, horizontalSpan, "");
	}

	private Text createTextField(String identifier, int horizontalSpan, String defaultText) {
		Text textField = new Text(this.composite, SWT.BORDER);
		textField.setText(defaultText);
		textField.setData(identifier);
		// Connect the widget to it's corresponding parameter
		textField.addListener(SWT.KeyUp, this);
		widgets.add((Widget) textField);

		GridData textGridData = new GridData(GridData.FILL_HORIZONTAL);
		textGridData.horizontalAlignment = GridData.HORIZONTAL_ALIGN_BEGINNING;
		textGridData.horizontalSpan = horizontalSpan;
		textGridData.widthHint = 160;
		textField.setLayoutData(textGridData);
		return textField;
	}

	private Text createTextField(String identifier, int horizontalSpan) {
		return createTextField(identifier, horizontalSpan, "");
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

	@Override
	public void handleEvent(Event event) {
		if (event.type == SWT.Selection || event.type == SWT.KeyUp) {
			updateCodeWindow();
		}
	}

	private void updateCodeWindow() {		
		String newSbatchText = getDefaultSbatchTextWithCommand();
		for (Widget widget : widgets) {
			String newValue = null;
			if (widget instanceof Combo) {
				newValue = ((Combo) widget).getText();
			} else if (widget instanceof Text) {
				newValue = ((Text) widget).getText();
			} else {
				logger.error("Could not set newValue");
			}
			
			if (newValue != null && !newValue.equals("")) {
				// Update the Resulting SBATCH StyledText
				String id = (String) widget.getData();
			    if (id.equals("module")) {
			    	newSbatchText = newSbatchText.replace("[modulename]", newValue);
			    } else if (id.equals("qosshort")) {
					// TODO: This is rather UPPMAX specific, no?
					if (newValue.equals("yes")) {
						newSbatchText = newSbatchText.replace("[" + id + "]", "--qos=short");
					} else {
						newSbatchText = newSbatchText.replace("#SBATCH [" + id + "]\n", "");
					}
				} else {
					newSbatchText = newSbatchText.replace("[" + id + "]", newValue);
					sbatchStyledText.setText(newSbatchText);
				}
				logger.debug(id + " = " + newValue);
			}
		}
	}

	private String getDefaultSbatchTextWithCommand() {
		String command = getCommandPage().getCommandText();
		String newSbatchText = sbatchTemplate + "\n" + command;
		return newSbatchText;
	}

	private ConfigureCommandPage getCommandPage() {
		return ((ConfigureCommandPage) this.getWizard().getPage("Configure Command Page"));
	}

	public String getResultingSbatchAndCommandText() {
		return this.sbatchStyledText.getText();
	}

	/**
	 * @return the modulesForCommand
	 */
	public List<String> getModulesForCommand() {
		return modulesForCommand;
	}

	/**
	 * @param modulesForCommand the modulesForCommand to set
	 */
	public void setModulesForCommand(List<String> modulesForCommand) {
		this.modulesForCommand = modulesForCommand;
	}

	/**
	 * @return the clusterInfo
	 */
	public Map<String, Object> getClusterInfo() {
		return clusterInfo;
	}

	/**
	 * @param clusterInfo the clusterInfo to set
	 */
	public void setClusterInfo(Map<String, Object> clusterInfo) {
		this.clusterInfo = clusterInfo;
	}

	/**
	 * @return the userInfo
	 */
	public Map<String, Object> getUserInfo() {
		return userInfo;
	}

	/**
	 * @param userInfo the userInfo to set
	 */
	public void setUserInfo(Map<String, Object> userInfo) {
		this.userInfo = userInfo;
	}

}
