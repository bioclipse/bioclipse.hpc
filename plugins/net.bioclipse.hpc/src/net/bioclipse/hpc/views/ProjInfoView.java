package net.bioclipse.hpc.views;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.management.openmbean.CompositeData;

import net.bioclipse.hpc.business.HPCManager;
import net.bioclipse.hpc.domains.application.HPCUtils;
import net.bioclipse.hpc.domains.hpc.Person;
import net.bioclipse.hpc.domains.hpc.Project;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.rse.core.model.IHost;
import org.eclipse.rse.core.model.ISystemRegistry;
import org.eclipse.rse.core.model.SystemStartHere;
import org.eclipse.rse.shells.ui.RemoteCommandHelpers;
import org.eclipse.rse.subsystems.files.core.subsystems.IRemoteFile;
import org.eclipse.rse.subsystems.files.core.subsystems.RemoteFileEmpty;
import org.eclipse.rse.subsystems.shells.core.model.SimpleCommandOperation;
import org.eclipse.rse.subsystems.shells.core.subsystems.IRemoteCmdSubSystem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.swt.widgets.Table;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.widgets.TrayItem;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.custom.TableTree;
import org.eclipse.jface.viewers.TableTreeViewer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProjInfoView extends ViewPart {
	public static final String ID = "net.bioclipse.hpc.views.ProjInfoView"; //$NON-NLS-1$

	private List _selectedFiles;
	private ProjInfoContentModel contentModel = new ProjInfoContentModel();
	private TableTreeViewer tableTreeViewer;
	private static final Logger logger = LoggerFactory.getLogger(ProjInfoView.class); 

	public ProjInfoView() {
		// Nothing here
	}

	/**
	 * Create contents of the view part.
	 * @param parent
	 */
	@Override
	public void createPartControl(Composite parent) {
		Composite container = new Composite(parent, SWT.V_SCROLL);
		container.setLayout(new GridLayout(1, false));
		
		Button btnUpdate = new Button(container, SWT.NONE);
		btnUpdate.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				updateProjectInfoTable();
			}
		});
		btnUpdate.setText("Update");

		Composite composite = new Composite(container, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		composite.setLayout(new FillLayout());
		
		tableTreeViewer = new TableTreeViewer(composite, SWT.BORDER | SWT.FULL_SELECTION);
	    // Set the content and label providers
		tableTreeViewer.setContentProvider(new ProjInfoContentProvider());
		tableTreeViewer.setLabelProvider(new ProjInfoProjectLabelProvider());
		tableTreeViewer.setInput(this.getContentModel());
	    
		TableTree tableTree = tableTreeViewer.getTableTree();
		
	    // Set up the table
	    Table table = tableTreeViewer.getTableTree().getTable();
	    new TableColumn(table, SWT.LEFT).setText("Name");
	    new TableColumn(table, SWT.LEFT).setText("Used hours");
	    new TableColumn(table, SWT.RIGHT).setText("Current allocation");
	    tableTreeViewer.expandAll();
	    
	    // Pack the columns
	    for (int i = 0, n = table.getColumnCount(); i < n; i++) {
	      TableColumn column = table.getColumn(i);
	      column.pack();
	      // Make columns sortable (doesn't work for TableTreeViewer though)
//	      ColumnSortListener sortListen = new ColumnSortListener();
//	      sortListen.setTable(table);
//	      column.addListener(SWT.Selection, sortListen);
	    }
	    
	    // Turn on the header and the lines
	    table.setHeaderVisible(true);
	    table.setLinesVisible(true);
	    
		createActions();
		initializeToolBar();
		initializeMenu();
	}

	public void setContentsFromXML(String xmlString) {
		// Why am I not doing this with XPath expressions, such as in the JobInfoView?
		// ... and should this XML format dependent stuff really be stored here?
		logger.debug("XML String:\n" + xmlString);
		List<String> groupXMLParts = HPCUtils.getMatches("<groupinfo>.*?</groupinfo>", xmlString);
		contentModel.clearProjInfoGroups();
		for (String g : groupXMLParts) {
			Project projInfoGroup = new Project();
			String groupName = HPCUtils.getMatch("<name>(.*?)</name>", g, 1);
			String groupUsedHours = HPCUtils.getMatch("<time>(.*?)</time>", g, 1);
			String groupCurrentAllocation = HPCUtils.getMatch("<allocation>(.*?)</allocation>", g, 1);
			projInfoGroup.setGroupName(groupName);
			projInfoGroup.setGroupUsedHours(groupUsedHours);
			projInfoGroup.setGroupCurrentAllocation(groupCurrentAllocation);
			
			List<String> userXMLParts = HPCUtils.getMatches("<user>.*?</user>", g);
			for (String s : userXMLParts) {
				String userName = HPCUtils.getMatch("<name>(.*?)</name>", s, 1);
				String usedHours = HPCUtils.getMatch("<time>(.*?)</time>", s, 1);
				String currentAllocation = HPCUtils.getMatch("<allocation>(.*?)</allocation>", s, 1);
				Person p = new Person();
				p.setName(userName);
				p.setUsedHours(usedHours);
				p.setCurrentAllocation(currentAllocation);
				projInfoGroup.addPersonToGroup(p);
			}
			contentModel.addProjInfoGroup(projInfoGroup);
		}
		tableTreeViewer.refresh();
	}

	private void updateProjectInfoTable() {
		HPCManager hpcManagerObj = new HPCManager();
		hpcManagerObj.updateProjInfoView();
	}

	protected IRemoteFile getFirstSelectedRemoteFile() {
		if (_selectedFiles.size() > 0) {
			return (IRemoteFile) _selectedFiles.get(0);
		}
		return null;
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
	
	public ProjInfoContentModel getContentModel() {
		return contentModel;
	}

	public void setContentModel(ProjInfoContentModel contentModel) {
		this.contentModel = contentModel;
	}
}
