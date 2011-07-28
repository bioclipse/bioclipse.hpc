package net.bioclipse.uppmax.galaxytoolconfigparser;
import org.antlr.grammar.v3.*;
import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CharStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.TokenStream;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.DOTTreeGenerator;
import org.antlr.runtime.tree.Tree;
import org.antlr.runtime.tree.TreeAdaptor;
import org.antlr.stringtemplate.StringTemplate;

public class ParseTest {
    public static final int EOF=-1;
    public static final int ELSE=4;
    public static final int ENDIF=5;
    public static final int WORD=6;
    public static final int IF=7;
    public static final int STRING=8;
    public static final int VARIABLE=9;
    public static final int EQTEST=10;
    public static final int COLON=11;
    public static final int DBLDASH=12;
    public static final int EQ=13;
    public static final int WS=14;
    
	public static void main(String[] args) throws RecognitionException {
		System.out.println("Beginning ...");
		String testString = "	sam_to_bam.py" 
				+ "      --input1=$source.input1\n"
				+ "      --dbkey=${input1.metadata.dbkey}\n"
				+ "      #if $source.index_source == \"history\":\n"
				+ "        --ref_file=$source.ref_file\n" 
				+ "      #else\n"
				+ "        --ref_file=\"None\"\n" 
				+ "      #end if\n"
				+ "      --output1=$output1\n"
				+ "      --index_dir=${GALAXY_DATA_INDEX_DIR}\n"; 
		CharStream charStream = new ANTLRStringStream(testString);
		GalaxyToolConfigLexer lexer = new GalaxyToolConfigLexer(charStream);
		TokenStream tokenStream = new CommonTokenStream(lexer);
		GalaxyToolConfigParser parser = new GalaxyToolConfigParser(tokenStream, null);
		
		System.out.println("Starting ...");
		// GalaxyToolConfigParser.command_return command = parser.command();
		CommonTree tree = (CommonTree)parser.command().getTree();
		System.out.println("Done executing command ...");
		
		// System.out.println("Command: " + ((Tree)command.tree).toStringTree());
		// CommonTree tree = (CommonTree)parser.command().getTree();

		int i = 0;
		while (i<tree.getChildCount()) {
			Tree subTree = tree.getChild(i);
		    System.out.println("Subtree text: " + subTree.getText() + ", (Token type: " + subTree.getType() + ")");
		    i++;
	    }
		
		//DOTTreeGenerator gen = new DOTTreeGenerator();
	    //StringTemplate st = gen.toDOT(tree);
	    //System.out.println("Tree: \n" + st);

		System.out.println("Done!");
	}
}
