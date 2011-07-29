package net.bioclipse.uppmax.views;

import java.util.ArrayList;
import java.util.List;

import net.bioclipse.uppmax.domains.hpc.Project;

public class ProjInfoContentModel {
	private List/* <ProjInfoGroup> */projInfoGroups = new ArrayList();
	
	public void ProjInfoContentModel() {
		// Nothing
	}

	public List<Project> getProjInfoGroups() {
		return projInfoGroups;
	}

	public void setProjInfoGroups(List<Project> projInfoGroups) {
		this.projInfoGroups = projInfoGroups;
	}

	public void addProjInfoGroup(Project projInfoGroup) {
		projInfoGroups.add(projInfoGroup);
	}

	public void clearProjInfoGroups() {
		projInfoGroups.clear();
	}

}
