package net.bioclipse.uppmax.galaxytoolconfigparser;
import java.util.ArrayList;

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
	// Token ID:s, from the generated Parser code
    public static final int EOF=-1;
    public static final int PARAM=4;
    public static final int ELSE=5;
    public static final int ENDIF=6;
    public static final int WORD=7;
    public static final int IF=8;
    public static final int STRING=9;
    public static final int VARIABLE=10;
    public static final int EQTEST=11;
    public static final int COLON=12;
    public static final int DBLDASH=13;
    public static final int EQ=14;
    public static final int WS=15;
    
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
		
		System.out.println("Starting to parse ...");
		// GalaxyToolConfigParser.command_return command = parser.command();
		CommonTree tree = (CommonTree)parser.command().getTree();
		System.out.println("Done parsing ...");
		
		ArrayList<Tree> ifCondition = extractIfCondition(tree);
		
		if (ifCondition.size() > 0) {
			System.out.println("Inside IF statement:");
			System.out.println("------------------------------");
			for (Tree currTree : ifCondition) {
				System.out.println(currTree.getText());
			}
			System.out.println("------------------------------");
			System.out.println("End, IF statement.");
		}
		
		// Generate DOT Syntax tree
		//DOTTreeGenerator gen = new DOTTreeGenerator();
	    //StringTemplate st = gen.toDOT(tree);
	    //System.out.println("Tree: \n" + st);

		System.out.println("Done!");
	}

	private static ArrayList<Tree> extractIfCondition(CommonTree tree) {
		ArrayList<Tree> ifCondition = new ArrayList<Tree>();
		boolean isInsideIfCond = false;
		int i = 0;
		while (i<tree.getChildCount()) {
			Tree subTree = tree.getChild(i);
			if (isInsideIfCond) {
				ifCondition.add(subTree);
			}
			if (subTree.getType() == IF) {
				isInsideIfCond = true;
			} else if (tree.getChild(i+1) != null && (tree.getChild(i+1).getType() == COLON || tree.getChild(i+1).getType() == ENDIF)) {
				isInsideIfCond = false;
			}
		    // System.out.println("Tree child: " + subTree.getText() + ", (Token type: " + subTree.getType() + ")");
		    i++;
	    }
		return ifCondition;
	}
}
