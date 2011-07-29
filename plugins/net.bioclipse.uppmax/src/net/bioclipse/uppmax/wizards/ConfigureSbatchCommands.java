package net.bioclipse.uppmax.wizards;

import java.util.ArrayList;
import java.util.List;

import net.bioclipse.uppmax.business.UppmaxUtils;
import net.bioclipse.uppmax.toolconfig.Option;
import net.bioclipse.uppmax.toolconfig.Parameter;
import net.bioclipse.uppmax.toolconfig.Tool;

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

public class ConfigureSbatchCommands extends WizardPage implements Listener {
	// These are set in the constructor
	IWorkbench workbench;
	IStructuredSelection selection;

	// Taken care of in the createControl(), and onEnterPage() functions
	Composite parentComposite;
	Composite composite;

	StyledText scriptText; 	// TODO: Make sure to implement this one
	List<Widget> widgets;

	
	protected ConfigureSbatchCommands(IWorkbench workbench, IStructuredSelection selection) {
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
		UppmaxUtils.createGridLayout(composite, 3);
		setControl(composite);
	}
	
	void onEnterPage() {
		createControl(parentComposite);
	}

	private void createComboBox(String label, List<String> optionValues, int horizontalSpan) {
		Combo currentCombo = UppmaxUtils.createCombo(composite);

		// Layout stuff
		GridData comboLayoutData = new GridData();
		comboLayoutData.horizontalSpan = horizontalSpan;
		currentCombo.setLayoutData(comboLayoutData);
		
		// Misc stuff
		currentCombo.addListener(SWT.Selection, this);
		widgets.add((Widget) currentCombo);

		// Populate
		String[] selectOptionsArr = UppmaxUtils.stringListToArray(optionValues);
		currentCombo.setItems(selectOptionsArr);
		
		// TODO: Is this really needed?
		// Option selectedOption = ???
		//	if (selectedOption != null) {
		//		String selectedOptionValue = selectedOption.getValue();
		//		currentCombo.setText(selectedOptionValue);
		//	}
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
