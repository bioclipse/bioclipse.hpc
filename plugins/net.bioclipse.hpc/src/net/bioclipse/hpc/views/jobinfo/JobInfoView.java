package net.bioclipse.hpc.views.jobinfo;

import java.util.List;

import net.bioclipse.hpc.domains.hpc.Job;
import net.bioclipse.hpc.domains.hpc.JobState;
import net.bioclipse.hpc.views.projinfo.ProjInfoView;
import net.bioclipse.hpc.xmldisplay.XmlDataProviderFactory;
import net.bioclipse.hpc.xmldisplay.XmlUtils;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
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
		
		createActions();
		initializeToolBar();
		initializeMenu();
	}
	
	public void updateViewFromXml(String rawXmlContent) {
		// Set content and label providers
		treeViewer.setContentProvider(new JobInfoContentProvider());
		treeViewer.setLabelProvider(new JobInfoLabelProvider());

		
		Document xmlDoc = XmlUtils.xmlToDOMDocument(rawXmlContent);
		if (xmlDoc != null) {
			List<Node> jobDOMNodes = XmlUtils.evalXPathExprToListOfNodes("/simpleapi/jobinfo/jobs/job", xmlDoc);			

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
				
				treeViewer.setInput(contentModel); // FIXME: Input is not correctly set here, so CRASHES
				treeViewer.refresh();				
			} else {
				logger.error("Didn't get any jobs to parse!");
			}
		} else {
			logger.error("Could not parse XML to DOM document!");
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

	public JobInfoContentModel getContentModel() {
		return contentModel;
	}

	public void setContentModel(JobInfoContentModel contentModel) {
		this.contentModel = contentModel;
	}

}
