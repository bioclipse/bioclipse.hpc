package net.bioclipse.uppmax.toolconfig;

import java.util.ArrayList;
import java.util.List;

public class ToolGroup {
	private String m_toolGroupName;
	private List<Tool> tools;

	public ToolGroup(String toolGroupName) {
		tools = new ArrayList<Tool>();
		setToolGroupName(toolGroupName);
	}

	public void setToolGroupName(String toolGroupName) {
		this.m_toolGroupName = toolGroupName;
	}

	public void addTool(Tool tool) {
		tools.add(tool);
	}

	public String getToolGroupName() {
		return m_toolGroupName;
	}

}
