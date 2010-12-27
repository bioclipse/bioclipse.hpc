package net.bioclipse.uppmax.views;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.management.openmbean.CompositeData;

import net.bioclipse.uppmax.business.ProjInfoContentModel;
import net.bioclipse.uppmax.business.ProjInfoContentProvider;
import net.bioclipse.uppmax.business.ProjInfoLabelProvider;
import net.bioclipse.uppmax.business.ProjInfoGroup;
import net.bioclipse.uppmax.business.ProjInfoPerson;
import net.bioclipse.uppmax.business.UppmaxManager;

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

public class ProjInfoView extends ViewPart {

	private List _selectedFiles;
	private ProjInfoContentModel contentModel = new ProjInfoContentModel();
	private TableTreeViewer tableTreeViewer;

	public static final String ID = "net.bioclipse.uppmax.views.ProjInfoView"; //$NON-NLS-1$

	public ProjInfoView() {
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
						UppmaxManager uppmaxManagerObj = new UppmaxManager();
						uppmaxManagerObj.updateProjectInfoView();
					}
				});
				btnUpdate.setText("Update");

		Composite composite = new Composite(container, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		composite.setLayout(new FillLayout());
		
		tableTreeViewer = new TableTreeViewer(composite, SWT.BORDER | SWT.FULL_SELECTION);
	    // Set the content and label providers
		tableTreeViewer.setContentProvider(new ProjInfoContentProvider());
		tableTreeViewer.setLabelProvider(new ProjInfoLabelProvider());
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
	      table.getColumn(i).pack();
	    }
	    
	    // Turn on the header and the lines
	    table.setHeaderVisible(true);
	    table.setLinesVisible(true);
	    
		createActions();
		initializeToolBar();
		initializeMenu();
	}


	private void updateProjectInfoTable() {
		// 
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

	public void setContentsFromXML(String xmlString) {
		System.out.println("XML String:\n" + xmlString);
		List<String> groupXMLParts = getMatches("<groupinfo>.*?</groupinfo>", xmlString);
		contentModel.clearProjInfoGroups();
		for (String g : groupXMLParts) {
			ProjInfoGroup projInfoGroup = new ProjInfoGroup();
			String groupName = getMatch("<name>(.*?)</name>", g, 1);
			String groupUsedHours = getMatch("<time>(.*?)</time>", g, 1);
			String groupCurrentAllocation = getMatch("<allocation>(.*?)</allocation>", g, 1);
			projInfoGroup.setGroupName(groupName);
			projInfoGroup.setGroupUsedHours(groupUsedHours);
			projInfoGroup.setGroupCurrentAllocation(groupCurrentAllocation);
			
			List<String> userXMLParts = getMatches("<user>.*?</user>", xmlString);
			for (String s : userXMLParts) {
				String userName = getMatch("<name>(.*?)</name>", s, 1);
				String usedHours = getMatch("<time>(.*?)</time>", s, 1);
				String currentAllocation = getMatch("<allocation>(.*?)</allocation>", s, 1);
				ProjInfoPerson p = new ProjInfoPerson();
				p.setName(userName);
				p.setUsedHours(usedHours);
				p.setCurrentAllocation(currentAllocation);
				projInfoGroup.addPersonToGroup(p);
			}
			contentModel.addProjInfoGroup(projInfoGroup);
		}
		tableTreeViewer.refresh();
	}
	
	protected List<String> getMatches(String regexPattern, String text) {
		List<String> result = new ArrayList<String>();
		Pattern p = Pattern.compile(regexPattern);
		Matcher m = p.matcher(text);
		while (m.find()) {
			String currentMatchString = m.group();
			result.add(currentMatchString); 
		}
		return result;
	}
	
	protected String getMatch(String regexPattern, String text, int group) {
		String result = null;
		Pattern p = Pattern.compile(regexPattern);
		Matcher m = p.matcher(text);
		if (m.find()) {
			result = m.group(group);
		}
		return result;
	}

	public ProjInfoContentModel getContentModel() {
		return contentModel;
	}

	public void setContentModel(ProjInfoContentModel contentModel) {
		this.contentModel = contentModel;
	}
}
