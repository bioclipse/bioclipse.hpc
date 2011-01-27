package net.bioclipse.uppmax.wizards;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;

public class ConfigParamsPage extends WizardPage implements Listener {

	public Text txtCommand;

	
	final static String[] commands = {
		"jobinfo",
		"projinfo"
	};
	
	IWorkbench workbench;
	IStructuredSelection selection;

	protected ConfigParamsPage(IWorkbench workbench, IStructuredSelection selection) {
		super("Page 2");
		setTitle("Configure command");
		setDescription("Configure a command that can then be executed on the command line on the remote system");
		this.workbench = workbench;
		this.selection = selection;
	}
	
	public boolean canFlipToNextPage() {
		return false;
	}
	
	@Override
	public void createControl(Composite parent) {
	    // create the composite to hold the widgets
		GridData gd;
		Composite composite =  new Composite(parent, SWT.NULL);

	    // create the desired layout for this wizard page
		GridLayout gl = new GridLayout();
		int ncol = 2;
		gl.numColumns = ncol;
		composite.setLayout(gl);
		
		new Label (composite, SWT.NONE).setText("Command to execute:");
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalAlignment = GridData.BEGINNING;
		txtCommand = new Text(composite, SWT.BORDER);
		txtCommand.setLayoutData(gd);
		txtCommand.setText("Enter parameters here ...");
		
	    // set the composite as the control for this page
		setControl(composite);		
	}

	@Override
	public void handleEvent(Event event) {
		// TODO Auto-generated method stub

	}

}
