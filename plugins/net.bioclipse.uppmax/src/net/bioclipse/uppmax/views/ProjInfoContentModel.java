package net.bioclipse.uppmax.views;

import java.util.ArrayList;
import java.util.List;

public class ProjInfoContentModel {
	private List/* <ProjInfoGroup> */projInfoGroups = new ArrayList();
	
	public void ProjInfoContentModel() {
		// Nothing
	}

	public List<ProjInfoGroup> getProjInfoGroups() {
		return projInfoGroups;
	}

	public void setProjInfoGroups(List<ProjInfoGroup> projInfoGroups) {
		this.projInfoGroups = projInfoGroups;
	}

	public void addProjInfoGroup(ProjInfoGroup projInfoGroup) {
		projInfoGroups.add(projInfoGroup);
	}

	public void clearProjInfoGroups() {
		projInfoGroups.clear();
	}

}
