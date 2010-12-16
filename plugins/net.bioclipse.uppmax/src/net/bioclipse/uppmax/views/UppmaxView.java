package net.bioclipse.uppmax.views;


import org.eclipse.rse.core.model.ISystemRegistry;
import org.eclipse.rse.core.model.SystemStartHere;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.*;
import org.eclipse.jface.viewers.*;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.jface.action.*;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.*;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.SWT;


/**
 * This sample class demonstrates how to plug-in a new
 * workbench view. The view shows data obtained from the
 * model. The sample creates a dummy model on the fly,
 * but a real implementation would connect to the model
 * available either in this or another plug-in (e.g. the workspace).
 * The view is connected to the model using a content provider.
 * <p>
 * The view uses a label provider to define how model
 * objects should be presented in the view. Each
 * view can present the same model objects using
 * different labels and icons, if needed. Alternatively,
 * a single label provider can be shared between views
 * in order to ensure that objects of the same type are
 * presented in the same way everywhere.
 * <p>
 */

public class UppmaxView extends ViewPart {
	public Composite container;
	public Text txtContents;

	
	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "net.bioclipse.uppmax.views.UppmaxView";
	private Thread updateViewThread;
	

	@Override
	public void createPartControl(Composite parent) {

		container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(1, false));
		container.setLayoutData(new GridData(GridData.FILL_BOTH));

//		final Label lblName = new Label(container, SWT.NONE);
//		lblName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
//		lblName.setText("Name:");

		txtContents = new Text(container, SWT.BORDER | SWT.MULTI);
		GridData gridData_1 = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gridData_1.widthHint = 482;
		gridData_1.heightHint = 200;
		txtContents.setLayoutData(gridData_1);
		setContents("wee");
		
//		updateViewThread = new Thread(new UpdateUppmaxView(this)); 
//		updateViewThread.start();
	}

	@Override
	public void setFocus() {
		txtContents.setFocus();
	}
	
	public void setContents(String contents){
		txtContents.setText(contents);
	}

}