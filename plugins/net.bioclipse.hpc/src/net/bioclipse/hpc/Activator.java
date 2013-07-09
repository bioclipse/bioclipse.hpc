/*******************************************************************************
 * Copyright (c) 2010  Samuel Lampa <samuel.lampa@gmail.com>
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contact: http://www.bioclipse.net/
 ******************************************************************************/
package net.bioclipse.hpc;

import net.bioclipse.hpc.business.IJavaScriptHPCManager;
import net.bioclipse.hpc.business.IJavaHPCManager;
import net.bioclipse.hpc.business.IHPCManager;
import net.bioclipse.hpc.domains.application.HPCApplication;
import net.bioclipse.hpc.domains.application.HPCUtils;
import net.bioclipse.hpc.domains.toolconfig.ToolConfigDomain;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceStore;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.prefs.PreferencesService;
import org.osgi.util.tracker.ServiceTracker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

    // Domain specific Application object
    public static HPCApplication application;

    private static final Logger logger = LoggerFactory.getLogger(Activator.class);

    // The shared instance
    private static Activator plugin;

    // Trackers for getting the managers
    private ServiceTracker javaFinderTracker;
    private ServiceTracker jsFinderTracker;
    
    public Activator() {
    }

    public void start(BundleContext context) throws Exception {
        super.start(context);
        plugin = this;
        javaFinderTracker
            = new ServiceTracker( context,
                                  IJavaHPCManager.class.getName(),
                                  null );

        javaFinderTracker.open();
        jsFinderTracker
            = new ServiceTracker( context,
                                  IJavaScriptHPCManager.class.getName(),
                                  null );

        jsFinderTracker.open();
        
        // Create an application object
        application = new HPCApplication();
        
        HPCUtils.getApplication().readGalaxyToolConfigFiles();
        
        // Need to set this here to, since initializeDefaultPreferences() is not always run
        HPCUtils.getApplication().setDefaultPreferences();
        
        /*
         * TODO: Start with IStartup and org.eclipse.ui.startup instead:
         * http://help.eclipse.org/juno/index.jsp?topic=%2Forg.eclipse.platform.doc.isv%2Freference%2Fextension-points%2Forg_eclipse_ui_startup.html
         * 
        boolean showPrefDialogOnStartup = getPreferenceStore().getBoolean("showdialogonstartup");
        if (showPrefDialogOnStartup) {
        	Dialog mainPrefsDialog = new HPCMainPreferencesDialog(HPCUtils.getApplication().getShell());
        	mainPrefsDialog.open();
        } else {
        	logger.debug("Preferences dialog set to not start..."); // TODO Remove debug code
        }
        */
        
    }

    public void stop(BundleContext context) throws Exception {
        plugin = null;
        
        // Null out the application
        application = null;
        
        super.stop(context);
    }

    /**
     * Returns the shared instance
     *
     * @return the shared instance
     */
    public static Activator getDefault() {
        return plugin;
    }

    public IHPCManager getJavaHPCManager() {
        IHPCManager manager = null;
        try {
            manager = (IHPCManager)
                      javaFinderTracker.waitForService(1000*10);
        }
        catch (InterruptedException e) {
            throw new IllegalStateException(
                          "Could not get the Java HPCManager",
                          e );
        }
        if (manager == null) {
            throw new IllegalStateException(
                          "Could not get the Java HPCManager");
        }
        return manager;
    }

    public IJavaScriptHPCManager getJavaScriptHPCManager() {
        IJavaScriptHPCManager manager = null;
        try {
            manager = (IJavaScriptHPCManager)
                      jsFinderTracker.waitForService(1000*10);
        }
        catch (InterruptedException e) {
            throw new IllegalStateException(
                          "Could not get the JavaScript HPCManager",
                          e );
        }
        if (manager == null) {
            throw new IllegalStateException(
                          "Could not get the JavaScript HPCManager");
        }
        return manager;
    }
    
	protected void initializeDefaultPreferences(IPreferenceStore store) {
		HPCUtils.getApplication().setDefaultPreferences();
	}

}
