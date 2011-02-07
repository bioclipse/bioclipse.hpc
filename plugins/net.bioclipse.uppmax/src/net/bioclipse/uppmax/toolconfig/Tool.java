package net.bioclipse.uppmax.toolconfig;

import java.util.HashMap;
import java.util.Map;

public class Tool {
	private String m_name;
	private String m_description;
	private String m_command;
	private Map<String,String> m_attributes;

	public void Tool() {
		m_name = "";
		m_description = "";
		m_command = "";
		m_attributes = new HashMap<String,String>();
	}
	
	public String getName() {
		return m_name;
	}

	public String getDescription() {
		return m_description;
	}

	public String getCommand() {
		return m_command;
	}

	public Map<String, String> getAttributes() {
		return m_attributes;
	}

	public void setName(String name) {
		m_name = name;
	}

	public void setDescription(String description) {
		m_description = description;
	}

	public void setCommand(String command) {
		m_command = command;
	}

	public void setAttributes(Map<String, String> attributes) {
		m_attributes = attributes;
	}

}
