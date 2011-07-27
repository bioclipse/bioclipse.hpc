package net.bioclipse.uppmax.galaxytoolconfigparser;

import java.io.*;
import org.antlr.runtime.*;
import org.antlr.runtime.debug.DebugEventSocketProxy;


public class __Test__ {

    public static void main(String args[]) throws Exception {
        GalaxyToolConfigLexer lex = new GalaxyToolConfigLexer(new ANTLRFileStream("/home/samuel/projects/galaxy-toolconfig-bnf/output/__Test___input.txt", "UTF8"));
        CommonTokenStream tokens = new CommonTokenStream(lex);

        GalaxyToolConfigParser g = new GalaxyToolConfigParser(tokens, null);
        try {
            g.command();
        } catch (RecognitionException e) {
            e.printStackTrace();
        }
    }
}