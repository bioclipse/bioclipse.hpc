package net.bioclipse.hpc.views.projinfo;

import java.util.List;

import net.bioclipse.hpc.domains.application.HPCUtils;
import net.bioclipse.hpc.domains.application.XmlUtils;
import net.bioclipse.hpc.domains.hpc.Person;
import net.bioclipse.hpc.domains.hpc.Project;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.viewers.TableTreeViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.rse.subsystems.files.core.subsystems.IRemoteFile;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableTree;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.ui.part.ViewPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProjInfoView extends ViewPart {
	public static final String ID = "net.bioclipse.hpc.views.ProjInfoView"; //$NON-NLS-1$
	private List _selectedFiles;
	private ProjInfoContentModel contentModel;
	private TreeViewer treeViewer;
	private static final Logger logger = LoggerFactory.getLogger(ProjInfoView.class); 

	public ProjInfoView() {
		contentModel = new ProjInfoContentModel();
	}

	public void updateViewFromXml(String xmlString) {
		updateContentModelFromXml(xmlString);
		treeViewer.refresh();
	    treeViewer.expandAll();		
	}

	private void updateContentModelFromXml(String rawXmlContent) {
		// Get groupInfo from XML
		List<String> groupXmlStrings = XmlUtils.extractTags("groupinfo", rawXmlContent);

		// Clear content of contentModel
		contentModel.clearProjInfoGroups();

		// Parse groupInfo XML strings into Project objects and add to content model
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
		String userName = XmlUtils.extractTagContent("name", userXml);
		String usedHrs = XmlUtils.extractTagContent("time", userXml);
		String curAlloc = XmlUtils.extractTagContent("allocation", userXml);

		// Create new Person object and populate
		Person person = new Person();
		person.setName(userName);
		person.setUsedHours(usedHrs);
		person.setCurrentAllocation(curAlloc);

		return person;
	}

	private Project createProjectFromXml(String groupXml) {
		// Extract info from XML
		String grpName = XmlUtils.extractTagContent("name", groupXml);
		String grpUsedHrs = XmlUtils.extractTagContent("time", groupXml);
		String grpCurAlloc = XmlUtils.extractTagContent("allocation", groupXml);

		// Create new Project object and populate
		Project project = new Project();
		project.setGroupName(grpName);
		project.setGroupUsedHours(grpUsedHrs);
		project.setGroupCurrentAllocation(grpCurAlloc);

		return project;
	}
	
	/**
	 * Create contents of the view part.
	 * @param parent
	 */
	@Override
	public void createPartControl(Composite parent) {
		Composite container = new Composite(parent, SWT.V_SCROLL);
		container.setLayout(new GridLayout(1, false));

		treeViewer = createTreeViewerInComposite(container);
		configureTableTreeViewer(treeViewer);

		// Set the content and label providers
		treeViewer.setContentProvider(new ProjInfoContentProvider());
		treeViewer.setLabelProvider(new ProjInfoLabelProvider());
		treeViewer.setInput(this.getContentModel());
		
		initializeToolBar();
		initializeMenu();
	}	
	
	// FIXME: It seems, from the JobInfo view, that a TreeViewer will do equally well
	//        as a TableTreViewer, so should change!
	private TreeViewer createTreeViewerInComposite(Composite container) {
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
		TreeViewer treeViewer = new TreeViewer(composite, tableTreeViewerStyle);
		
		return treeViewer;
	}
	
	private void configureTableTreeViewer(TreeViewer treeViewer) {
	    // Turn on the header and the lines
		treeViewer.getTree().setHeaderVisible(true);
		treeViewer.getTree().setLinesVisible(true);
		
	    // Set up the columns
		TreeViewerColumn col1 = new TreeViewerColumn(treeViewer, SWT.LEFT);
		TreeColumn col1Column = col1.getColumn();
		col1Column.setText("Name");
		col1Column.setWidth(120);
	    new TreeViewerColumn(treeViewer, SWT.RIGHT).getColumn().setText("Used hours");
	    new TreeViewerColumn(treeViewer, SWT.RIGHT).getColumn().setText("Current allocation");
	    
	    for (int i = 1; i<3; i++) {
	    	TreeColumn col = treeViewer.getTree().getColumn(i);
	    	col.setWidth(80);
	    }
	}	

	private void updateProjectInfoTable() {
		HPCUtils.getApplication().refreshProjInfoView();
	}

	protected IRemoteFile getFirstSelectedRemoteFile() {
		if (_selectedFiles.size() > 0) {
			return (IRemoteFile) _selectedFiles.get(0);
		}
		return null;
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
