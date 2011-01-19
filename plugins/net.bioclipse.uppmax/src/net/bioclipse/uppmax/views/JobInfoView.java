package net.bioclipse.uppmax.views;

import java.util.Iterator;
import java.util.List;

import net.bioclipse.uppmax.business.UppmaxManager;
import net.bioclipse.uppmax.xmldisplay.XmlContentProvider;
import net.bioclipse.uppmax.xmldisplay.XmlDataProviderFactory;
import net.bioclipse.uppmax.xmldisplay.XmlRow;
import net.bioclipse.uppmax.xmldisplay.XmlLabelProvider;
import net.bioclipse.uppmax.xmldisplay.XmlRowCollection;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.jface.viewers.TreeViewer;

public class JobInfoView extends ViewPart {
	public static final String ID = "net.bioclipse.uppmax.views.JobInfoView"; //$NON-NLS-1$
	private XmlDataProviderFactory xmlDataProvider = new XmlDataProviderFactory();
	TreeViewer treeViewer;
	
	public JobInfoView() {
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
				uppmaxManagerObj.updateJobInfoView();
			}
		});
		btnUpdate.setText("Update");

		treeViewer = new TreeViewer(container, SWT.BORDER);
		Tree tree = treeViewer.getTree();
		tree.setHeaderVisible(true);
		tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		createActions();
		initializeToolBar();
		initializeMenu();
	}
	
	public void updateViewFromXml(String rawXmlContent) {
		xmlDataProvider.setAndParseXmlContent(rawXmlContent);
		XmlContentProvider aXmlContentProvider = xmlDataProvider.getContentProvider();
		XmlLabelProvider aXmlLabelProvider = xmlDataProvider.getLabelProvider();
		
		if ( xmlDataProvider.getColumns().size() == 0 ) {
			xmlDataProvider.createColumnsForTreeViewer(treeViewer);
		}
		
		List<XmlRowCollection> rowCollections = xmlDataProvider.getContent();

		treeViewer.setContentProvider(aXmlContentProvider);
		treeViewer.setLabelProvider(aXmlLabelProvider);
		treeViewer.refresh();

		for (Iterator<XmlRowCollection> i = rowCollections.iterator(); i.hasNext(); ) {
			XmlRowCollection rowCollection = i.next();
			rowCollection.setIsRootNode(true);
			treeViewer.setInput(rowCollection);
		}
		
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
		// TODO Auto-generated method stub

	}

	public XmlDataProviderFactory getContentModel() {
		return xmlDataProvider;
	}

	public void setContentModel(XmlDataProviderFactory newXmlDataProvider) {
		this.xmlDataProvider = newXmlDataProvider;
	}
}
