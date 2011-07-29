package net.bioclipse.hpc.domains.hpc;

import java.util.ArrayList;
import java.util.List;

import net.bioclipse.hpc.domains.application.AbstractModelObject;


public class Project extends AbstractModelObject {
	private String groupName;
	private String groupUsedHours;
	private String groupCurrentAllocation;
	private List/* <ProjInfoPerson> */projInfoPersons = new ArrayList();
	
	public void ProjInfoGroup(String groupName) {
		setGroupName(groupName);
	}
	
	public void addPersonToGroup(Person person) {
		projInfoPersons.add(person);
		System.out.println("Added person to group: " + person.getName()); // TODO: Debug-code
	}

	public List getProjInfoPersons() {
		return projInfoPersons;
	}

	public void setProjInfoPersons(List projInfoPersons) {
		this.projInfoPersons = projInfoPersons;
	}
	
	public List getChildren() {
		return getProjInfoPersons();
	}
	
	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getGroupUsedHours() {
		return groupUsedHours;
	}

	public void setGroupUsedHours(String groupUsedHours) {
		this.groupUsedHours = groupUsedHours;
	}

	public String getGroupCurrentAllocation() {
		return groupCurrentAllocation;
	}

	public void setGroupCurrentAllocation(String groupCurrentAllocation) {
		this.groupCurrentAllocation = groupCurrentAllocation;
	}

}
