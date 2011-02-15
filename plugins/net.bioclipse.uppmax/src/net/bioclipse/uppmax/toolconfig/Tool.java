package net.bioclipse.uppmax.toolconfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.bioclipse.uppmax.business.UppmaxUtils;

public class Tool {
	private String m_name;
	private String m_description;
	private String m_command;
	private Map<String,String> m_attributes;
	private List<Parameter> m_parameters; 

	public Tool() {
		m_name = "";
		m_description = "";
		m_command = "";
		m_attributes = new HashMap<String,String>();
		m_parameters = new ArrayList<Parameter>();
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

	public String[] getParamNames() {
		List<String> paramNames = new ArrayList<String>();
		if (m_parameters != null) {
			for (Parameter param : m_parameters) {
				String paramName = param.getName();
				paramNames.add(paramName);
			}
		}
		return UppmaxUtils.stringListToStringArray(paramNames);
	}
	
	public void addParameter(Parameter newParameter) {
		m_parameters.add(newParameter);
	}
	
	public List<Parameter> getParameterList() {
		return m_parameters;
	}

}
