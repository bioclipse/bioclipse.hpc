package net.bioclipse.uppmax.wizards;

import net.bioclipse.uppmax.toolconfig.ToolConfigPool;
import net.bioclipse.uppmax.xmldisplay.XmlUtils;

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
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ConfigureCommandPage extends WizardPage implements Listener {

	public Combo comboTool;

	IWorkbench workbench;
	IStructuredSelection selection;

	protected ConfigureCommandPage(IWorkbench workbench, IStructuredSelection selection) {
		super("Page 2");
		setTitle("Select tool");
		setDescription("Select a tool from the ones available in the tool group just selected ...");
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
		
		new Label (composite, SWT.NONE).setText("Select tool:");
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalAlignment = GridData.BEGINNING;
		
		comboTool = new Combo(composite, SWT.BORDER);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalAlignment = GridData.BEGINNING;
		comboTool.setLayoutData(gd);
		
		String[] emptyStringArray = {"(No tools loaded)"};
		comboTool.setItems(emptyStringArray);
		comboTool.setText(comboTool.getItem(0));
		
	    // set the composite as the control for this page
		setControl(composite);
	}

	public void updateDroplist(String[] tools) {
		comboTool.removeAll();
		comboTool.setItems(tools);
		comboTool.setText(comboTool.getItem(0));
	}

	@Override
	public void handleEvent(Event event) {
		// TODO Auto-generated method stub

	}


}