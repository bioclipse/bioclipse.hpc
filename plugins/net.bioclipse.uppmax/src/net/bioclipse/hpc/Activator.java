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
import net.bioclipse.hpc.domains.toolconfig.ToolConfigDomain;

import org.apache.log4j.Logger;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.prefs.PreferencesService;
import org.osgi.util.tracker.ServiceTracker;

/**
 * The Activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

    private static final Logger logger = Logger.getLogger(Activator.class);

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
        
        // Activate Galaxy tool configuration
		ToolConfigDomain.getInstance().readToolConfigsFromXmlFiles("/home/samuel/.galaxy/tools");
    }

    public void stop(BundleContext context) throws Exception {
        plugin = null;
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

}
