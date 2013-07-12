/**
 * 
 */
package net.bioclipse.hpc.views.jobinfo;

import java.io.CharArrayReader;
import java.io.CharArrayWriter;
import java.util.List;
import java.util.Arrays;
import java.util.HashMap;

import net.bioclipse.hpc.domains.hpc.JobState;
import net.bioclipse.hpc.domains.hpc.Job;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;

/**
 * @author samuel
 *
 */
public class JobInfoLabelProvider implements ITableLabelProvider {
	private List<String> jobColumnLabels = Arrays.asList("Id", 
			"Partition", 
			"Name", 
			"User name", 
			"State", 
			"Time elapsed", 
			"# Nodes", 
			"Node list");
	private HashMap<Integer,String> jobColumnLabelMap = new HashMap<Integer, String>();
	
	public JobInfoLabelProvider() {
		// Initialize the columnLabelMap
		for (int i = 0; i < jobColumnLabelMap.size(); i++) {
			getColumnLabelMap().put(i, jobColumnLabelMap.get(i));			
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java.lang.Object, int)
	 */
	@Override
	public String getColumnText(Object element, int columnIndex) {
		if (element instanceof JobState) {
			if (columnIndex == 0) {
				String label = ((JobState) element).getJobState();
				label = upperCaseFirst(label);
				return label + " jobs"; 				
			} else {
				return "";
			}
		} else if (element instanceof Job) {
			switch (columnIndex) {
				case 0:
					return ((Job) element).getId();
				case 1:
					return ((Job) element).getPartition();
				case 2:
					return ((Job) element).getName();
				case 3:
					return ((Job) element).getUserName();
				case 4:
					return ((Job) element).getState();
				case 5:
					return ((Job) element).getTimeElapsed();
				case 6:
					return Integer.toString(((Job) element).getNodesCount());
				case 7:
					return ((Job) element).getNodelist();
				default:
					return "";
			}
		} else {
			return "Column " + Integer.toString(columnIndex);
		}
	}

	private String upperCaseFirst(String label) {
		char[] labelArr = label.toLowerCase().toCharArray();
		labelArr[0] = Character.toString(labelArr[0]).toUpperCase().toCharArray()[0];
		label = new String(labelArr);
		return label;
	}	

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.IBaseLabelProvider#addListener(org.eclipse.jface.viewers.ILabelProviderListener)
	 */
	@Override
	public void addListener(ILabelProviderListener listener) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.IBaseLabelProvider#dispose()
	 */
	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.IBaseLabelProvider#isLabelProperty(java.lang.Object, java.lang.String)
	 */
	@Override
	public boolean isLabelProperty(Object element, String property) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.IBaseLabelProvider#removeListener(org.eclipse.jface.viewers.ILabelProviderListener)
	 */
	@Override
	public void removeListener(ILabelProviderListener listener) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnImage(java.lang.Object, int)
	 */
	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @return the columnLabels
	 */
	public List<String> getColumnLabels() {
		return jobColumnLabels;
	}

	/**
	 * @param columnLabels the columnLabels to set
	 */
	public void setColumnLabels(List<String> columnLabels) {
		this.jobColumnLabels = columnLabels;
	}

	/**
	 * @return the columnLabelMap
	 */
	public HashMap<Integer, String> getColumnLabelMap() {
		return jobColumnLabelMap;
	}

	/**
	 * @param columnLabelMap the columnLabelMap to set
	 */
	public void setColumnLabelMap(HashMap<Integer, String> columnLabelMap) {
		this.jobColumnLabelMap = columnLabelMap;
	}

}
