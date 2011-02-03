package net.bioclipse.uppmax.domain;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.xpath.XPathConstants;

import net.bioclipse.uppmax.xmldisplay.XmlUtils;

import org.eclipse.ui.internal.FolderLayout;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import org.osgi.service.prefs.Preferences;
import org.osgi.service.prefs.PreferencesService;
import org.w3c.dom.Document;

public class PluginKernel {
	private static PluginKernel fPluginKernel = new PluginKernel();

	public static PluginKernel getPluginKernel() {
		return fPluginKernel;
	}

	// This class is a singleton, so don't let anyone else instantiate it 
	private PluginKernel() {}

}
