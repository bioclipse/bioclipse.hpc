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
package net.bioclipse.uppmax;

import net.bioclipse.uppmax.business.IUppmaxManager;
import net.bioclipse.uppmax.business.IJavaUppmaxManager;
import net.bioclipse.uppmax.business.IJavaScriptUppmaxManager;
import net.bioclipse.uppmax.domain.PluginKernel;

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
    
    private PluginKernel pluginKernel;

    public Activator() {
    }

    public void start(BundleContext context) throws Exception {
        super.start(context);
        plugin = this;
        javaFinderTracker
            = new ServiceTracker( context,
                                  IJavaUppmaxManager.class.getName(),
                                  null );

        javaFinderTracker.open();
        jsFinderTracker
            = new ServiceTracker( context,
                                  IJavaScriptUppmaxManager.class.getName(),
                                  null );

        jsFinderTracker.open();
        
        pluginKernel = PluginKernel.getPluginKernel();
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

    public IUppmaxManager getJavaUppmaxManager() {
        IUppmaxManager manager = null;
        try {
            manager = (IUppmaxManager)
                      javaFinderTracker.waitForService(1000*10);
        }
        catch (InterruptedException e) {
            throw new IllegalStateException(
                          "Could not get the Java UppmaxManager",
                          e );
        }
        if (manager == null) {
            throw new IllegalStateException(
                          "Could not get the Java UppmaxManager");
        }
        return manager;
    }

    public IJavaScriptUppmaxManager getJavaScriptUppmaxManager() {
        IJavaScriptUppmaxManager manager = null;
        try {
            manager = (IJavaScriptUppmaxManager)
                      jsFinderTracker.waitForService(1000*10);
        }
        catch (InterruptedException e) {
            throw new IllegalStateException(
                          "Could not get the JavaScript UppmaxManager",
                          e );
        }
        if (manager == null) {
            throw new IllegalStateException(
                          "Could not get the JavaScript UppmaxManager");
        }
        return manager;
    }

	public PluginKernel getPluginKernel() {
		return pluginKernel;
	}

}
