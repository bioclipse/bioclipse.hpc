package net.bioclipse.hpc.domains.toolconfig;

public class Option {
	private String m_value;
	private Boolean m_selected;
	
	public Option() {
		m_value = "";
		m_selected = false;
	}

	public String getValue() {
		return m_value;
	}

	public void setValue(String value) {
		m_value = value;
	}

	public Boolean getSelected() {
		return m_selected;
	}

	public void setSelected(Boolean selected) {
		m_selected = selected;
	}
	
}
