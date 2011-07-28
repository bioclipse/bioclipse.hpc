package net.bioclipse.uppmax.wizards;

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
	IWorkbench workbench;
	IStructuredSelection selection;

	Composite parentComposite;
	Composite composite;

	StyledText scriptText;
	List<Widget> widgets;

	
	protected ConfigureSbatchCommands(IWorkbench workbench, IStructuredSelection selection) {
		super("Page 5");
		setTitle("Configure SBATCH parameters");
		setDescription("Set the parameters for the SBATCH job batch script to be sent to the SLURM resource manager");
		this.workbench = workbench;
		this.selection = selection;
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

	private void createComboBox(Parameter parameter, int horizontalSpan) {
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

	// TODO: Check that it works!
	@Override
	public void handleEvent(Event event) {
		if (event.type == SWT.Selection || event.type == SWT.KeyUp) {
			for (Widget widget : this.widgets) {
				// Get the new value from the widget
				String newValue = null;
				if (widget instanceof Combo) {
					newValue = ((Combo) widget).getText();
					// TODO: Remove Debug code
					System.out.println("Selected option: " + newValue);
				} else if (widget instanceof Text) {
					newValue = ((Text) widget).getText();
					// TODO: Remove Debug code
					System.out.println("Selected option: " + newValue);
				} else {
					System.out.println("Could not set newValue");
				}
			}
		}
	}

}
