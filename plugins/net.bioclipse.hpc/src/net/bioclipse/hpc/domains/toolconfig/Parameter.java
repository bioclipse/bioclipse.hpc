package net.bioclipse.hpc.domains.toolconfig;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing a parameter in a galaxy tool configuration
 * @author samuel
 */
public class Parameter {
	// For distinguishing "normal" and "output" type params
	private String m_paramType;

	private String m_name;
	private String m_type;
	private String m_format;
	private String m_label;
	private String m_help;
	private int m_size;
	private String m_value;
	private List<Option> m_selectOptions;
	private boolean m_isOptional;
	
	public Parameter() {
		// TODO: Should they be initialized to null instead?
		m_name = "";
		m_type = "";
		m_format = "";
		m_label = "";
		m_help = "";
		m_size = 0;
		m_value = "";
		m_selectOptions = new ArrayList<Option>();
		m_isOptional = false; // is false by default also in Galaxy
	}
	
	public String getName() {
		return m_name;
	}
	public void setName(String name) {
		m_name = name;
	}
	public String getType() {
		return m_type;
	}
	public void setType(String type) {
		m_type = type;
	}
	public String getFormat() {
		return m_format;
	}
	public void setFormat(String format) {
		m_format = format;
	}
	public String getLabel() {
		return m_label;
	}
	public void setLabel(String label) {
		m_label = label;
	}
	public String getHelp() {
		return m_help;
	}
	public void setHelp(String help) {
		m_help = help;
	}
	public int getSize() {
		return m_size;
	}
	public void setSize(int size) {
		m_size = size;
	}
	public String getValue() {
		return m_value;
	}
	public void setValue(String value) {
		m_value = value;
	}

	public void setSelectOptions(List<Option> selectOptions) {
		m_selectOptions = selectOptions;
	}

	public List<Option> getSelectOptions() {
		return m_selectOptions;
	}

	public List<String> getSelectOptionValues() {
		List<String> optionValues = new ArrayList<String>();
		List<Option> options = getSelectOptions();
		for (Option option : options) {
			optionValues.add(option.getValue());
		}
		return optionValues;
	}

	public Option getSelectedOption() {
		for (Option option : getSelectOptions()) {
			if (option.getSelected() == true) {
				return option;
			}
		}
		return null;
	}

	public void setParamType(String paramType) {
		m_paramType = paramType;
	}

	public String getParamType() {
		return m_paramType;
	}

	public boolean getIsOptional() {
		return m_isOptional;
	}
	
	public void setIsOptional(boolean isOptional) {
		m_isOptional = isOptional;
	}
}
