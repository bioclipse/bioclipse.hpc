package net.bioclipse.uppmax.xmldisplay;

import java.util.List;

public class GalaxyToolConfig {
	private String fName;
	private String fDescription;
	private String fCommand;
	private List<String> fParams;
	
	public void GalaxyToolConfig(String name) {
		setName(name);
	}

	public String getName() {
		return fName;
	}

	public void setName(String name) {
		this.fName = name;
	}

	public String getDescription() {
		return fDescription;
	}

	public void setDescription(String description) {
		this.fDescription = description;
	}

	public String getCommand() {
		return fCommand;
	}

	public void setCommand(String command) {
		this.fCommand = command;
	}

	public List<String> getParams() {
		return fParams;
	}

	public void setParams(List<String> params) {
		this.fParams = params;
	}
	
}
