package net.bioclipse.hpc.wizards;

import java.util.ArrayList;
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

	StyledText scriptText; 	// TODO: Make sure to implement this one
	List<Widget> widgets;

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
		
		// Populate wizard here
		// -A [project name] | Combo  // TODO: Retrieve the user's project automatic
		// -p [partition]    | Combo  // Simple list, or get info from cluster?
		// -N [no of nodes]  | Text-field / up-down number field?
		// -n [no of cpus]   | Text-field / up-down number field?
		// -t d-hh:mm:ss     | ?      // TODO: Find a good widget for setting the time
		// --qos=short       | Combo (yes/no)
		// -J [JobName]      | TextField
		
		// Get user info, to use for writing the SBATCH config
		Map<String,Object> userInfo = HPCUtils.getApplication().getUserInfo();
		
		String username = (String) userInfo.get("username");
		List<String> projects = (List<String>) userInfo.get("projects");
		
		createLabel("Project to account");
		createComboBox(projects, 2);
		
		// TODO: Remove Debug code
		System.out.println("Username: " + username + ", Projects: " + projects.toString());
		
	    this.composite.pack();
	}

	private void createComboBox(List<String> optionValues, int horizontalSpan) {
		Combo currentCombo = HPCUtils.createCombo(composite);

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
		
		// TODO: Is this really needed?
		// Option selectedOption = ???
		//	if (selectedOption != null) {
		//		String selectedOptionValue = selectedOption.getValue();
		//		currentCombo.setText(selectedOptionValue);
		//	}
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

	// TODO: Check that it works!
	@Override
	public void handleEvent(Event event) {
		if (event.type == SWT.Selection || event.type == SWT.KeyUp) {
			for (Widget widget : this.widgets) {
				// Get the new value from the widget
				String newValue = null;
				if (widget instanceof Combo) {
					newValue = ((Combo) widget).getText();
					System.out.println("Selected option: " + newValue);
				} else if (widget instanceof Text) {
					newValue = ((Text) widget).getText();
					System.out.println("Selected option: " + newValue);
				} else {
					System.out.println("Could not set newValue");
				}
			}
		}
	}

}
