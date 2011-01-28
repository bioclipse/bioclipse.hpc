package net.bioclipse.uppmax.xmldisplay;

import javax.xml.xpath.XPathConstants;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class GalaxyConfigReader {

	private String galaxyRawXml;
	private Document galaxyXmlDocument;
	
	private String interpreter;
	
	public void read(String galaxyToolConfigFileContent) {
		setGalaxyRawXml(galaxyToolConfigFileContent);
		setGalaxyXmlDocument(XmlUtils.parseXmlToDocument(galaxyToolConfigFileContent));
		
		// Retrieve
		Document doc = getGalaxyXmlDocument();
		String xPathExpr1 = "tool/command/@interpreter";
		String interpreter = (String) XmlUtils.evaluateXPathExpr(doc, xPathExpr1, XPathConstants.STRING);
		if (interpreter != null) {
			System.out.println("Interpreter: " + interpreter);
			setInterpreter(interpreter);
		}

		String xPathExpr2 = "tool/command";
		String command = (String) XmlUtils.evaluateXPathExpr(doc, xPathExpr2, XPathConstants.STRING);
		if (command != null) {
			System.out.println("Command: " + command);
		} else {
			System.out.println("No command node");
		}
		
	}

	public String getGalaxyRawXml() {
		return galaxyRawXml;
	}

	public void setGalaxyRawXml(String galaxyRawXml) {
		this.galaxyRawXml = galaxyRawXml;
	}

	public Document getGalaxyXmlDocument() {
		return galaxyXmlDocument;
	}

	public void setGalaxyXmlDocument(Document galaxyXmlDocument) {
		this.galaxyXmlDocument = galaxyXmlDocument;
	}
	
	public String getExampleGalaxyXmlContent () {
		String content = "<tool id=\"fasta2tab\" name=\"FASTA-to-Tabular\" version=\"1.0.0\">	<description>converts FASTA file to tabular format</description>	<command interpreter=\"python\">fasta_to_tabular.py $input $output $keep_first</command>	<inputs>		<param name=\"input\" type=\"data\" format=\"fasta\" label=\"Convert these sequences\"/>		<param name=\"keep_first\" type=\"integer\" size=\"5\" value=\"0\" label=\"How many title characters to keep?\" help=\"'0' = keep the whole thing\"/>	</inputs>	<outputs>		<data name=\"output\" format=\"tabular\"/>	</outputs>	<tests>		<test>			<param name=\"input\" value=\"454.fasta\" />			<param name=\"keep_first\" value=\"0\"/>			<output name=\"output\" file=\"fasta_to_tabular_out1.tabular\" />		</test>				<test>			<param name=\"input\" value=\"4.fasta\" />			<param name=\"keep_first\" value=\"0\"/>			<output name=\"output\" file=\"fasta_to_tabular_out2.tabular\" />		</test>				<test>			<param name=\"input\" value=\"454.fasta\" />			<param name=\"keep_first\" value=\"14\"/>			<output name=\"output\" file=\"fasta_to_tabular_out3.tabular\" />		</test>	</tests>	<help>	**What it does**This tool converts FASTA formatted sequences to TAB-delimited format. The option *How many characters to keep?* allows to select a specified number of letters from the beginning of each FASTA entry. -----	**Example**Suppose you have the following FASTA formatted sequences from a Roche (454) FLX sequencing run::    &gt;EYKX4VC02EQLO5 length=108 xy=1826_0455 region=2 run=R_2007_11_07_16_15_57_    TCCGCGCCGAGCATGCCCATCTTGGATTCCGGCGCGATGACCATCGCCCGCTCCACCACG    TTCGGCCGGCCCTTCTCGTCGAGGAATGACACCAGCGCTTCGCCCACG    &gt;EYKX4VC02D4GS2 length=60 xy=1573_3972 region=2 run=R_2007_11_07_16_15_57_    AATAAAACTAAATCAGCAAAGACTGGCAAATACTCACAGGCTTATACAATACAAATGTAARunning this tool while setting **How many characters to keep?** to **14** will produce this::		EYKX4VC02EQLO5  TCCGCGCCGAGCATGCCCATCTTGGATTCCGGCGCGATGACCATCGCCCGCTCCACCACGTTCGGCCGGCCCTTCTCGTCGAGGAATGACACCAGCGCTTCGCCCACG	EYKX4VC02D4GS2  AATAAAACTAAATCAGCAAAGACTGGCAAATACTCACAGGCTTATACAATACAAATGTAA	</help></tool>";
		return content;
	}

	public String getInterpreter() {
		return interpreter;
	}

	public void setInterpreter(String interpreter) {
		this.interpreter = interpreter;
	}

	
}
