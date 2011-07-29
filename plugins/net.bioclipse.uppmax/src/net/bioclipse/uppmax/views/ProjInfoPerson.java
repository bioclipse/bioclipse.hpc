package net.bioclipse.uppmax.views;

import net.bioclipse.uppmax.business.AbstractModelObject;

public class ProjInfoPerson extends AbstractModelObject {
	private String name;
	private String usedHours;
	private String currentAllocation;
	private ProjInfoGroup group;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUsedHours() {
		return usedHours;
	}
	public void setUsedHours(String usedHours) {
		this.usedHours = usedHours;
	}
	public String getCurrentAllocation() {
		return currentAllocation;
	}
	public void setCurrentAllocation(String currentAllocation) {
		this.currentAllocation = currentAllocation;
	}
	public ProjInfoGroup getProjInfoGroup() {
		// TODO Auto-generated method stub
		return group;
	}
	
}
