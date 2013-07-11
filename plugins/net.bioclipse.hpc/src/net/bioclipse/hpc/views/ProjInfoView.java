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
import net.bioclipse.hpc.views.projinfo.ProjInfoContentModel;
import net.bioclipse.hpc.views.projinfo.ProjInfoContentProvider;
import net.bioclipse.hpc.views.projinfo.ProjInfoProjectLabelProvider;
import net.bioclipse.hpc.xmldisplay.XmlUtils;

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
	private ProjInfoContentModel contentModel;
	private TableTreeViewer tableTreeViewer;
	private static final Logger logger = LoggerFactory.getLogger(ProjInfoView.class); 

	public ProjInfoView() {
		contentModel = new ProjInfoContentModel();
	}

	public void updateViewFromXml(String xmlString) {
		updateContentModelFromXml(xmlString);
		tableTreeViewer.refresh();
	}

	private void updateContentModelFromXml(String rawXmlContent) {
		// Get groupInfo from XML
		List<String> groupXmlStrings = XmlUtils.extractTags("groupinfo", rawXmlContent);

		// Clear content of contentModel
		contentModel.clearProjInfoGroups();

		// Parse groupInfo XML strings into Project obmects and add to content model
		for (String groupXml : groupXmlStrings) {
			Project project = createProjectFromXml(groupXml);
			// Parse info for each individual user
			List<String> userXmlStrings = XmlUtils.extractTags("user", groupXml);
			for (String userXml : userXmlStrings) {
				Person person = createPersonFromXml(userXml);
				project.addPersonToGroup(person);
			}
			contentModel.addProjInfoGroup(project);
		}
	}

	private Person createPersonFromXml(String userXml) {
		// Extract info from Xml
		String userName = XmlUtils.extractTag("name", userXml);
		String usedHrs = XmlUtils.extractTag("time", userXml);
		String curAlloc = XmlUtils.extractTag("allocation", userXml);

		// Create new Person object and populate
		Person person = new Person();
		person.setName(userName);
		person.setUsedHours(usedHrs);
		person.setCurrentAllocation(curAlloc);

		return person;
	}

	private Project createProjectFromXml(String groupXml) {
		// Extract info from XML
		String grpName = XmlUtils.extractTag("name", groupXml);
		String grpUsedHrs = XmlUtils.extractTag("time", groupXml);
		String grpCurAlloc = XmlUtils.extractTag("allocation", groupXml);

		// Create new Project object and populate
		Project project = new Project();
		project.setGroupName(grpName);
		project.setGroupUsedHours(grpUsedHrs);
		project.setGroupCurrentAllocation(grpCurAlloc);

		return project;
	}
	
	private void configureTableTreeViewer(TableTreeViewer tableTreeViewer) {
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
	      // ColumnSortListener sortListen = new ColumnSortListener();
	      // sortListen.setTable(table);
	      // column.addListener(SWT.Selection, sortListen);
	    }
	    
	    // Turn on the header and the lines
	    table.setHeaderVisible(true);
	    table.setLinesVisible(true);
	}

	/**
	 * Create contents of the view part.
	 * @param parent
	 */
	@Override
	public void createPartControl(Composite parent) {
		Composite container = new Composite(parent, SWT.V_SCROLL);
		container.setLayout(new GridLayout(1, false));

		createUpdateButton(container);
		tableTreeViewer = createTableTreeViewerInComposite(container);
		configureTableTreeViewer(tableTreeViewer);
	    
		createActions();
		initializeToolBar();
		initializeMenu();
	}
	
	// FIXME: It seems, from the JobInfo view, that a TreeViewer will do equally well
	//        as a TableTreViewer, so should change!
	private TableTreeViewer createTableTreeViewerInComposite(Composite container) {
		Composite composite = new Composite(container, SWT.NONE);
		
		// Configure the layout
		int horizAlignment = SWT.FILL;
		int vertAlignment = SWT.FILL;
		boolean grabExcessHorizSpace = true;
		boolean grabExcessVertSpace = true;
		int horizSpan = 1;
		int vertSpan = 1;
		
		composite.setLayoutData(new GridData(horizAlignment, 
											 vertAlignment, 
											 grabExcessHorizSpace, 
											 grabExcessVertSpace, 
											 horizSpan, 
											 vertSpan));
		composite.setLayout(new FillLayout());
		
		int tableTreeViewerStyle = SWT.BORDER | SWT.FULL_SELECTION;
		tableTreeViewer = new TableTreeViewer(composite, tableTreeViewerStyle);

		return tableTreeViewer;
	}

	private void createUpdateButton(Composite container) {
		Button btnUpdate = new Button(container, SWT.NONE);
		btnUpdate.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				updateProjectInfoTable();
			}
		});
		btnUpdate.setText("Update");
	}	

	private void updateProjectInfoTable() {
		HPCUtils.getApplication().updateProjInfoView();
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
