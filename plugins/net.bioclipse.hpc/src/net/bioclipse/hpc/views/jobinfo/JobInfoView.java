package net.bioclipse.hpc.views.jobinfo;

import java.util.Iterator;
import java.util.List;

import net.bioclipse.hpc.business.HPCManager;
import net.bioclipse.hpc.business.HPCManagerFactory;
import net.bioclipse.hpc.domains.application.HPCUtils;
import net.bioclipse.hpc.wizards.ExecuteCommandWizard;
import net.bioclipse.hpc.xmldisplay.XmlContentProvider;
import net.bioclipse.hpc.xmldisplay.XmlDataProviderFactory;
import net.bioclipse.hpc.xmldisplay.XmlLabelProvider;
import net.bioclipse.hpc.xmldisplay.XmlRootNode;
import net.bioclipse.hpc.xmldisplay.XmlRow;
import net.bioclipse.hpc.xmldisplay.XmlRowCollection;
import net.bioclipse.hpc.xmldisplay.XmlUtils;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.rse.core.IRSECoreRegistry;
import org.eclipse.rse.core.IRSESystemType;
import org.eclipse.rse.core.IRSESystemTypeConstants;
import org.eclipse.rse.core.IRSESystemTypeProvider;
import org.eclipse.rse.core.RSECorePlugin;
import org.eclipse.rse.core.SystemResourceHelpers;
import org.eclipse.rse.core.model.IHost;
import org.eclipse.rse.core.model.ISystemRegistry;
import org.eclipse.rse.core.subsystems.IConnectorService;
import org.eclipse.rse.core.subsystems.ISubSystem;
import org.eclipse.rse.files.ui.dialogs.SystemRemoteFileDialog;
import org.eclipse.rse.files.ui.resources.ISystemRemoteResource;
import org.eclipse.rse.internal.subsystems.files.ssh.SftpRemoteFile;
import org.eclipse.rse.subsystems.files.core.servicesubsystem.FileServiceSubSystem;
import org.eclipse.rse.subsystems.files.core.servicesubsystem.FileSubSystemInputStream;
import org.eclipse.rse.subsystems.files.core.servicesubsystem.IFileServiceSubSystem;
import org.eclipse.rse.subsystems.files.core.subsystems.IRemoteFile;
import org.eclipse.rse.subsystems.files.core.subsystems.IRemoteFileSubSystem;
import org.eclipse.rse.subsystems.files.core.subsystems.RemoteFile;
import org.eclipse.rse.subsystems.files.core.subsystems.RemoteFileSubSystem;
import org.eclipse.rse.ui.SystemBasePlugin;
import org.eclipse.rse.ui.dialogs.ISystemPromptDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.wizard.WizardDialog;

public class JobInfoView extends ViewPart {
	public static final String ID = "net.bioclipse.hpc.views.JobInfoView"; //$NON-NLS-1$
	private XmlDataProviderFactory xmlDataProvider = new XmlDataProviderFactory();
	TreeViewer treeViewer;
	
	// Constructor
	public JobInfoView() {}

	/**
	 * Create contents of the view part.
	 * @param parent
	 */
	@Override
	public void createPartControl(Composite parent) {
		final Composite container = new Composite(parent, SWT.V_SCROLL);
		container.setLayout(new GridLayout());

		treeViewer = new TreeViewer(container, SWT.BORDER);
		Tree tree = treeViewer.getTree();
		tree.setHeaderVisible(true);
		tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		createActions();
		initializeToolBar();
		initializeMenu();
	}
	
	public void updateViewFromXml(String rawXmlContent) {
		// Set content and label providers
		treeViewer.setContentProvider(new JobInfoContentProvider());
		treeViewer.setLabelProvider(new JobInfoLabelProvider());

		// Create the content model instance
		JobInfoContentModel contentModel = new JobInfoContentModel();
		
		// Populate the content model with data from the XML
		String jobsXml = XmlUtils.extractTag("jobs", rawXmlContent);
		
		
		treeViewer.setInput(contentModel);		

		treeViewer.refresh();
	}

	/**
	 * Create the actions.
	 */
	private void createActions() {
		// Create the actions
	}

	/**
	 * Initialize the toolbar.
	 */
	private void initializeToolBar() {
		IToolBarManager toolbarManager = getViewSite().getActionBars()
		.getToolBarManager();
	}

	/**
	 * Initialize the menu.
	 */
	private void initializeMenu() {
		IMenuManager menuManager = getViewSite().getActionBars()
		.getMenuManager();
	}

	@Override
	public void setFocus() {
		// Set the focus
	}

	public void setContentsFromXML(String projInfoXml) {
		// TODO Auto-generated method stub (Is this ever used?)
	}

	public XmlDataProviderFactory getContentModel() {
		return xmlDataProvider;
	}

	public void setContentModel(XmlDataProviderFactory newXmlDataProvider) {
		this.xmlDataProvider = newXmlDataProvider;
	}

}
