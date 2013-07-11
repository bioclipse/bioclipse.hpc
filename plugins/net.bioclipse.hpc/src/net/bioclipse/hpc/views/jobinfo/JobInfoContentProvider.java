/**
 * 
 */
package net.bioclipse.hpc.views.jobinfo;

import net.bioclipse.hpc.domains.hpc.JobState;
import net.bioclipse.hpc.domains.hpc.Project;
import net.bioclipse.hpc.views.projinfo.ProjInfoContentModel;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

/**
 * @author Samuel Lampa <samuel.lampa@gmail.com>
 *
 */
public class JobInfoContentProvider implements ITreeContentProvider {
	private static final Object[] EMPTY_OBJ = new Object[] {};

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#getElements(java.lang.Object)
	 */
	@Override
	public Object[] getElements(Object inputElement) {
	    // Returns all the ProjInfoGroups in the model
	    return ((JobInfoContentModel) inputElement).getJobStates().toArray();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#getChildren(java.lang.Object)
	 */
	@Override
	public Object[] getChildren(Object parentElement) {
	    if (parentElement instanceof JobState) {
	        return ((JobState) parentElement).getJobs().toArray();
	    }
	    // Jobs have no children, so don't return anything for them.
	    return EMPTY_OBJ;
	}
	
	/*
	 * Check if this element has children
	 */
	@Override
	public boolean hasChildren(Object element) {
		return getChildren(element).length > 0;
	}

	
	// =================== UNIMPLEMENTED STUFF BELOW ===================
	
	
	@Override
	public void dispose() {}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {}

	@Override
	public Object getParent(Object element) {
		return null;
	}

}
