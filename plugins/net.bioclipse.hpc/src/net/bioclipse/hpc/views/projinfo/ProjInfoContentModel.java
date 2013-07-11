package net.bioclipse.hpc.views.projinfo;

import java.util.ArrayList;
import java.util.List;

import net.bioclipse.hpc.domains.hpc.Project;

public class ProjInfoContentModel {
	private List/* <ProjInfoGroup> */projInfoGroups = new ArrayList();
	
	public ProjInfoContentModel() {}

	// --------- Getters and setters --------------
	
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
