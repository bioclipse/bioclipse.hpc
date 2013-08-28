package net.bioclipse.hpc.domains.toolconfig;

/**
 * Class representing a parameter option in a galaxy tool configuration
 * @author samuel
 */
public class Option {
	private String m_value;
	private Boolean m_selected;
	private String m_text;
	
	public Option() {
		this.m_value = "";
		this.m_selected = false;
		this.m_text = "";
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

	public String getText() {
		return m_text;
	}

	public void setText(String m_text) {
		this.m_text = m_text;
	}
	
}
