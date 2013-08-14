package net.bioclipse.hpc.views.jobinfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.bioclipse.hpc.domains.application.HPCUtils;
import net.bioclipse.hpc.domains.application.XmlUtils;
import net.bioclipse.hpc.domains.hpc.Job;
import net.bioclipse.hpc.domains.hpc.JobState;
import net.bioclipse.hpc.views.projinfo.ProjInfoView;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.ui.part.ViewPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class JobInfoView extends ViewPart {
	public static final String ID = "net.bioclipse.hpc.views.JobInfoView"; //$NON-NLS-1$
	private JobInfoContentModel contentModel;
	private TreeViewer treeViewer;
	private static final Logger logger = LoggerFactory.getLogger(ProjInfoView.class);
	
	// Constructor
	public JobInfoView() {
		// Create the content model instance
		contentModel = new JobInfoContentModel();		
	}

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

		// Must com
		for (String label : new ArrayList<String>(Arrays.asList("Id", 
				 												"Partition", 
				 												"Name", 
				 												"User name", 
				 												"State", 
				 												"Time elapsed", 
				 												"# Nodes", 
				 												"Node list"))) 
		{
			TreeViewerColumn treeViewerColumn = new TreeViewerColumn(treeViewer, SWT.NONE);
			TreeColumn aTreeColumn = treeViewerColumn.getColumn();
			if (label.equals("Id")) {
				aTreeColumn.setWidth(140);
			} else {
				aTreeColumn.setWidth(80);				
			}
			aTreeColumn.setText(label);			
		}

		// Set content and label providers
		treeViewer.setContentProvider(new JobInfoContentProvider());
		treeViewer.setLabelProvider(new JobInfoLabelProvider());
		treeViewer.setInput(contentModel); 
		
		// Create context menu
		treeViewer.addSelectionChangedListener(
            new ISelectionChangedListener(){
                public void selectionChanged(SelectionChangedEvent event) {
                    if(event.getSelection() instanceof IStructuredSelection) {
                        IStructuredSelection selection = (IStructuredSelection)event.getSelection();            
                        Object o = selection.getFirstElement();     
                        MenuManager menuMgr = new MenuManager();
                        Menu menu = menuMgr.createContextMenu(treeViewer.getControl());
                        treeViewer.getControl().setMenu(menu);
                        getSite().registerContextMenu(menuMgr, treeViewer);
                        if (o instanceof Job){
                            menuMgr.add(new JobInfoCancelJobAction());
                        } else {
                            menuMgr.removeAll();
                        }
                    }
                }
            }   
	    );
		
		initializeToolBar();
		initializeMenu();
	}
	
	public void updateViewFromXml(String rawXmlContent) {

		Document xmlDoc = XmlUtils.xmlToDOMDocument(rawXmlContent);
		if (xmlDoc != null) {
			List<Node> jobDOMNodes = XmlUtils.evalXPathExprToListOfNodes("/simpleapi/jobinfo/jobs/job", xmlDoc);
			contentModel.clearJobsinJobStates();

			if (!jobDOMNodes.isEmpty()) {
				for (Node jobNode : jobDOMNodes) {
					NamedNodeMap attributes = jobNode.getAttributes();

					// Extract info from DOM structure
					String id = attributes.getNamedItem("id").getNodeValue();
					String partition = attributes.getNamedItem("partition").getNodeValue();
					String name = attributes.getNamedItem("name").getNodeValue();
					String userName = attributes.getNamedItem("username").getNodeValue();
					String state = attributes.getNamedItem("state").getNodeValue();
					String timeElapsed = attributes.getNamedItem("time_elapsed").getNodeValue();
					int nodesCount = Integer.parseInt(attributes.getNamedItem("nodescnt").getNodeValue());
					String nodeList = attributes.getNamedItem("nodelist").getNodeValue();
					
					Job job = new Job(); 
					job.setId(id);
					job.setPartition(partition);
					job.setName(name);
					job.setUserName(userName);
					job.setState(state);
					job.setTimeElapsed(timeElapsed);
					job.setNodesCount(nodesCount);
					job.setNodelist(nodeList);
					
					JobState jobState = contentModel.getJobStateByJobStateString(state); // FIXME: Crashes here
					if (jobState != null) {
						jobState.addJob(job);					
					} else {
						logger.error("Could not find a job state object for state: " + state + "!");
					}
				}
			} else {
				HPCUtils.getApplication().showInfoMessage("No jobs found", "Could not find any running or pending jobs.");
				logger.warn("Didn't get any jobs to parse!");
			}
			treeViewer.refresh();
			treeViewer.expandAll();
		} else {
			logger.error("Could not parse XML to DOM document!");
		}
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
	
	// ------------ Getters and setters ------------

	public JobInfoContentModel getContentModel() {
		return contentModel;
	}

	public void setContentModel(JobInfoContentModel contentModel) {
		this.contentModel = contentModel;
	}

}
