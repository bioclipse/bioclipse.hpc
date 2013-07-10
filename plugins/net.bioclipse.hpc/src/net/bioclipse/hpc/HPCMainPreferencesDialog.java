package net.bioclipse.hpc;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceStore;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class that manages settings in the preferences menu in Bioclipse
 * @author Samuel Lampa <samuel.lampa@gmail.com>
 */
public class HPCMainPreferencesDialog extends Dialog {
	public StringFieldEditor hostName;
	public StringFieldEditor userName;
	public DirectoryFieldEditor galaxyToolConfigPath;
	public BooleanFieldEditor showPrefDialogOnStartup;
	public FieldEditorPreferencePage page;
    private static final Logger logger = LoggerFactory.getLogger(HPCMainPreferencesDialog.class);
	
	protected HPCMainPreferencesDialog(Shell parentShell) {
		super(parentShell);		
	}
	
    @Override
    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);
        newShell.setText("Connection settings");
        newShell.setSize(500, 220);
        newShell.setLocation(300, 300);
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        Composite composite = (Composite) super.createDialogArea(parent);

        page = new FieldEditorPreferencePage(FieldEditorPreferencePage.GRID) {

        	@Override
            protected void createFieldEditors() {
        		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
        		
            	hostName = new StringFieldEditor("hostname", "Hostname", getFieldEditorParent());
            	hostName.setPreferenceStore(store);
            	hostName.load();

            	userName = new StringFieldEditor("username", "Username", getFieldEditorParent());
            	userName.setPreferenceStore(store);
            	userName.load();
        		
        		galaxyToolConfigPath = new DirectoryFieldEditor("galaxytoolconfigpath", "Path to Galaxy ToolConfigs", getFieldEditorParent());
        		galaxyToolConfigPath.setPreferenceStore(store);
        		galaxyToolConfigPath.load();
        		
        		showPrefDialogOnStartup = new BooleanFieldEditor("showdialogonstartup", "Show dialog on start up?", getFieldEditorParent());
        		showPrefDialogOnStartup.setPreferenceStore(store);
        		showPrefDialogOnStartup.load();
        		
            	addField(hostName);
        		addField(userName);
        		addField(galaxyToolConfigPath);
        		addField(showPrefDialogOnStartup);        		
            }
            
        	@Override
            public void createControl(Composite parentComposite) {
                noDefaultAndApplyButton();
                super.createControl(parentComposite);
            }
        };
        
        page.createControl(composite);
        Control pageControl = page.getControl();
        pageControl.setLayoutData(new GridData(GridData.FILL_BOTH));
        return pageControl;

    }
    
    protected void buttonPressed(int buttonId) {
        if (buttonId == IDialogConstants.OK_ID) {
        	logger.debug("OK button is pressed ..."); // TODO Remove debug code
        	IPreferenceStore store = Activator.getDefault().getPreferenceStore();
        	
        	hostName.setPreferenceStore(store);
        	userName.setPreferenceStore(store);
        	galaxyToolConfigPath.setPreferenceStore(store);
        	showPrefDialogOnStartup.setPreferenceStore(store);

        	hostName.store();
        	userName.store();
        	galaxyToolConfigPath.store();
        	showPrefDialogOnStartup.store();

        }
        super.buttonPressed(buttonId);
    }


}
