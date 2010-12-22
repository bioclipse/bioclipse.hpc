package net.bioclipse.uppmax.views;

import java.util.ArrayList;
import java.util.List;

import javax.management.openmbean.CompositeData;

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

public class ProjInfoView extends ViewPart {

	private List _selectedFiles;

	public static final String ID = "net.bioclipse.uppmax.views.ProjInfoView"; //$NON-NLS-1$
	private Table tableProjInfo;

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
		
		TableViewer tableViewerProjInfo = new TableViewer(composite, SWT.BORDER | SWT.FULL_SELECTION | SWT.FILL);
		tableProjInfo = tableViewerProjInfo.getTable();
		tableProjInfo.setBounds(0, 0, 400, 160);
		tableProjInfo.setHeaderVisible(true);
		
		TableViewerColumn tableViewerColumn_1 = new TableViewerColumn(tableViewerProjInfo, SWT.NONE);
		TableColumn tblclmnProperty = tableViewerColumn_1.getColumn();
		tblclmnProperty.setWidth(130);
		tblclmnProperty.setText("Property");

		TableViewerColumn tableViewerColumn = new TableViewerColumn(tableViewerProjInfo, SWT.NONE);
		TableColumn tblclmnValue = tableViewerColumn.getColumn();
		tblclmnValue.setWidth(100);
		tblclmnValue.setText("Value");

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
}
