package net.bioclipse.hpc.views.jobinfo;

import org.eclipse.jface.databinding.viewers.TreeStructureAdvisor;

public class JobInfoTreeStructureAdvisor extends TreeStructureAdvisor {
	@Override 
	public Object getParent(Object element) { 
		return null; 
	} 

	@Override 
	public Boolean hasChildren(Object element) { 
		return false; 
	} 

}
