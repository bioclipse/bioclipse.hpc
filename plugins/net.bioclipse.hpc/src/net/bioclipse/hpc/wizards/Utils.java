package net.bioclipse.hpc.wizards;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;

public class Utils {

	public static Combo createCombo(Composite composite) {
		GridData gridData;
		Combo combo = new Combo(composite, SWT.BORDER);
		gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalAlignment = GridData.BEGINNING;
		combo.setLayoutData(gridData);
		return combo;
	}
	
}
