package net.bioclipse.hpc.preferences;

import net.bioclipse.hpc.business.HPCManager;
import net.bioclipse.hpc.domains.application.HPCUtils;

import net.bioclipse.hpc.Activator;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.jface.preference.FontFieldEditor;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class HPCPreferencePage 
	extends FieldEditorPreferencePage 
	implements IWorkbenchPreferencePage {

	public StringFieldEditor hostName;
	public StringFieldEditor userName;
	public DirectoryFieldEditor galaxyToolConfigPath;
	public BooleanFieldEditor showPrefDialogOnStartup;

	public HPCPreferencePage() {
		super(FieldEditorPreferencePage.GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setDescription("HPC Integration Preferences");
	}

	@Override
	public void init(IWorkbench arg0) {
	}

	@Override
	protected void createFieldEditors() {
		hostName = new StringFieldEditor("hostname", "Hostname", getFieldEditorParent());
		hostName.load();
		addField(hostName);
		userName = new StringFieldEditor("username", "Username", getFieldEditorParent());
		userName.load();
		addField(userName);
		galaxyToolConfigPath = new DirectoryFieldEditor("galaxytoolconfigpath", "Path to Galaxy ToolConfigs", getFieldEditorParent());
		galaxyToolConfigPath.load();
		addField(galaxyToolConfigPath);
		showPrefDialogOnStartup = new BooleanFieldEditor("showdialogonstartup", "Show dialog on start up?", getFieldEditorParent());
		showPrefDialogOnStartup.load();
		addField(showPrefDialogOnStartup);
	}
	
	@Override
	protected void performDefaults() {
		super.performDefaults();
		hostName.loadDefault();
		userName.loadDefault();
		galaxyToolConfigPath.loadDefault();
		showPrefDialogOnStartup.loadDefault();
	}

}
