package net.bioclipse.hpc.wizards;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import net.bioclipse.hpc.business.HPCUtils;
import net.bioclipse.hpc.domains.application.HPCApplication;
import net.bioclipse.hpc.domains.toolconfig.Option;
import net.bioclipse.hpc.domains.toolconfig.Parameter;
import net.bioclipse.hpc.domains.toolconfig.Tool;

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

public class ConfigureSbatchScriptPage extends WizardPage implements Listener {
	// These are set in the constructor
	IWorkbench workbench;
	IStructuredSelection selection;
	// Taken care of in the createControl(), and onEnterPage() functions
	Composite parentComposite;
	Composite composite;

	StyledText sbatchStyledText;
	List<Widget> widgets;
	
	String sbatchTemplate = 
		"#SBATCH -A [project]\n" +
		"#SBATCH -p [partition]\n" +
		"#SBATCH -N [noofnodes]\n" +
		"#SBATCH -n [noofcpus]\n" +
		"#SBATCH -t [runtime]\n" +
		"#SBATCH [qosshort]\n" +
		"#SBATCH -J [jobname]\n";	

	protected ConfigureSbatchScriptPage(IWorkbench workbench, IStructuredSelection selection) {
		super("Page 5");
		setTitle("Configure SBATCH parameters");
		setDescription("Set the parameters for the SBATCH job batch script to be sent to the SLURM resource manager");
		this.workbench = workbench;
		this.selection = selection;
		this.widgets = new ArrayList<Widget>();
	}

	@Override
	public void createControl(Composite parent) {
		parentComposite = parent;
		composite =  new Composite(parent, SWT.NULL);
		HPCUtils.createGridLayout(composite, 3);
		setControl(composite);
	}
	
	void onEnterPage() {
		createControl(parentComposite);
		
		// Get user info, to use for writing the SBATCH config
		Map<String,Object> userInfo = HPCUtils.getApplication().getUserInfo();
		String username = (String) userInfo.get("username");
		List<String> projects = (List<String>) userInfo.get("projects");

		// Get various info used for writing SBATCH config
		Map<String,Object> clusterInfo = HPCUtils.getApplication().getClusterInfo();
		String maxNodesStr = (String) clusterInfo.get("maxnodes");
		String maxCpusStr = (String) clusterInfo.get("maxcpus");
		List<String> partitions = (List<String>) clusterInfo.get("partitions");

		// Populate wizard here
		// -A [project name] | Combo  // TODO: Retrieve the user's project automatic
		createLabel("Project to account");
		createComboBox("project", projects, 2);
		
		// -p [partition]    | Combo  // Simple list, or get info from cluster?
		createLabel("Partition (type of job)");
		createComboBox("partition", partitions, 2);
		
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
				System.out.println("ERROR: MaxCPUs is zero!");
			}
		}
		createComboBox("noofnodes", maxNodesStringList, 2);

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
				System.out.println("ERROR: MaxCPUs is zero!");
			}
		}
		createComboBox("noofcpus", maxCpusStringList, 2);
		
		// -t d-hh:mm:ss     | ?      
		createLabel("Running time (d-hh:mm:ss)"); // TODO: Find a good widget for setting the time?
		createTextField("runtime", 2);

		// --qos=short       | Combo (yes/no)
		createLabel("Activate --qos=short option?");
		createComboBox("qosshort", Arrays.asList("no", "yes"), 2, "no");
		// -J [JobName]      | TextField
		createLabel("Job name");
		createTextField("jobname", 2, "Untitled");
		
		createResultingSBatchScriptTextbox();
		
	    this.composite.pack();
	}

	private void createResultingSBatchScriptTextbox() {
		createLabel("Resulting SBATCH Script");

		sbatchStyledText = new StyledText(composite, SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL );
		sbatchStyledText.setText(sbatchTemplate);
		GridData gridLayoutData = new GridData( SWT.NONE|GridData.FILL_BOTH );
		gridLayoutData.horizontalSpan = 2;
		gridLayoutData.grabExcessHorizontalSpace = true;
		gridLayoutData.heightHint = 48;
		gridLayoutData.widthHint = 200;
		sbatchStyledText.setLayoutData(gridLayoutData);
	}

	private void createComboBox(String identifier, List<String> optionValues, int horizontalSpan, String defValue) {
		Combo currentCombo = HPCUtils.createCombo(composite);
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
			for (Widget widget : widgets) {
				String id = (String) widget.getData();
				String newValue = null;
				if (widget instanceof Combo) {
					newValue = ((Combo) widget).getText();
				} else if (widget instanceof Text) {
					newValue = ((Text) widget).getText();
				} else {
					System.out.println("Could not set newValue");
				}
				System.out.println(id + " = " + newValue);

				// Update the Resulting SBATCH StyledText
				String newSbatchText = "";
				newSbatchText = sbatchTemplate.replace("[" + id + "]", newValue);
				sbatchStyledText.setText(newSbatchText);
			}

//			if (id.equals("project")) {
//				//	
//			} else if (id.equals("partition")) {
//				//
//			} else if (id.equals("noofnodes")) {
//				//	
//			} else if (id.equals("noofcpus")) {
//				//	
//			} else if (id.equals("runtime")) {
//				//	
//			} else if (id.equals("qosshort")) {
//				//	
//			} else if (id.equals("jobname")) {
//				//	
//			}
		}
	}

}
