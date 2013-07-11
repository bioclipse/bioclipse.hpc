package net.bioclipse.hpc.views.projinfo;

import org.eclipse.jface.databinding.viewers.TreeStructureAdvisor;

public class ProjInfoTreeStructureAdvisor extends TreeStructureAdvisor {
	@Override 
	public Object getParent(Object element) { 
		return null; 
	} 

	@Override 
	public Boolean hasChildren(Object element) { 
		return false; 
	} 

}
