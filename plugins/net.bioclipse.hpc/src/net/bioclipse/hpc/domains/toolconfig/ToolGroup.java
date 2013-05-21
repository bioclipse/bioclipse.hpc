package net.bioclipse.hpc.domains.toolconfig;

/**
 * Class representing a group of Galaxy tool configurations
 * @author samuel
 */
import java.util.ArrayList;
import java.util.List;

public class ToolGroup {
	private String m_toolGroupName;
	private List<Tool> m_tools;

	public ToolGroup(String toolGroupName) {
		m_tools = new ArrayList<Tool>();
		setToolGroupName(toolGroupName);
	}

	public void setToolGroupName(String toolGroupName) {
		this.m_toolGroupName = toolGroupName;
	}

	public void addTool(Tool tool) {
		m_tools.add(tool);
	}

	public String getToolGroupName() {
		return m_toolGroupName;
	}

	public List<Tool> getTools() {
		return m_tools;
	}

}
