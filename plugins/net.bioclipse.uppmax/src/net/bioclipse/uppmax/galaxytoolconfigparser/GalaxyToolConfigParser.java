// $ANTLR 3.3 Nov 30, 2010 12:45:30 /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g 2011-07-27 19:01:00

package net.bioclipse.uppmax.galaxytoolconfigparser;

import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

import org.antlr.runtime.debug.*;
import java.io.IOException;

import org.antlr.runtime.tree.*;

public class GalaxyToolConfigParser extends DebugParser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "ELSE", "ENDIF", "WORD", "IF", "STRING", "VARIABLE", "EQTEST", "COLON", "DBLDASH", "EQ", "WS"
    };
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

    // delegates
    // delegators

    public static final String[] ruleNames = new String[] {
        "invalidRule", "command", "binary", "text", "param", "ifstatement"
    };
    public static final boolean[] decisionCanBacktrack = new boolean[] {
        false, // invalid decision
        false, false, false, false, false, false
    };

     
        public int ruleLevel = 0;
        public int getRuleLevel() { return ruleLevel; }
        public void incRuleLevel() { ruleLevel++; }
        public void decRuleLevel() { ruleLevel--; }
        public GalaxyToolConfigParser(TokenStream input) {
            this(input, DebugEventSocketProxy.DEFAULT_DEBUGGER_PORT, new RecognizerSharedState());
        }
        public GalaxyToolConfigParser(TokenStream input, int port, RecognizerSharedState state) {
            super(input, state);
            DebugEventSocketProxy proxy =
                new DebugEventSocketProxy(this,port,adaptor);
            setDebugListener(proxy);
            setTokenStream(new DebugTokenStream(input,proxy));
            try {
                proxy.handshake();
            }
            catch (IOException ioe) {
                reportError(ioe);
            }
            TreeAdaptor adap = new CommonTreeAdaptor();
            setTreeAdaptor(adap);
            proxy.setTreeAdaptor(adap);
        }
    public GalaxyToolConfigParser(TokenStream input, DebugEventListener dbg) {
        super(input, dbg);

         
        TreeAdaptor adap = new CommonTreeAdaptor();
        setTreeAdaptor(adap);

    }
    protected boolean evalPredicate(boolean result, String predicate) {
        dbg.semanticPredicate(result, predicate);
        return result;
    }

    protected DebugTreeAdaptor adaptor;
    public void setTreeAdaptor(TreeAdaptor adaptor) {
        this.adaptor = new DebugTreeAdaptor(dbg,adaptor);

    }
    public TreeAdaptor getTreeAdaptor() {
        return adaptor;
    }


    public String[] getTokenNames() { return GalaxyToolConfigParser.tokenNames; }
    public String getGrammarFileName() { return "/home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g"; }


    public static class command_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "command"
    // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:4:1: command : binary ( ifstatement ( param )+ ( ELSE ( param )+ )? ENDIF | param )* ;
    public final GalaxyToolConfigParser.command_return command() throws RecognitionException {
        GalaxyToolConfigParser.command_return retval = new GalaxyToolConfigParser.command_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token ELSE4=null;
        Token ENDIF6=null;
        GalaxyToolConfigParser.binary_return binary1 = null;

        GalaxyToolConfigParser.ifstatement_return ifstatement2 = null;

        GalaxyToolConfigParser.param_return param3 = null;

        GalaxyToolConfigParser.param_return param5 = null;

        GalaxyToolConfigParser.param_return param7 = null;


        Object ELSE4_tree=null;
        Object ENDIF6_tree=null;

        try { dbg.enterRule(getGrammarFileName(), "command");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(4, 1);

        try {
            // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:4:9: ( binary ( ifstatement ( param )+ ( ELSE ( param )+ )? ENDIF | param )* )
            dbg.enterAlt(1);

            // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:4:11: binary ( ifstatement ( param )+ ( ELSE ( param )+ )? ENDIF | param )*
            {
            root_0 = (Object)adaptor.nil();

            dbg.location(4,11);
            pushFollow(FOLLOW_binary_in_command17);
            binary1=binary();

            state._fsp--;

            adaptor.addChild(root_0, binary1.getTree());
            dbg.location(4,18);
            // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:4:18: ( ifstatement ( param )+ ( ELSE ( param )+ )? ENDIF | param )*
            try { dbg.enterSubRule(4);

            loop4:
            do {
                int alt4=3;
                try { dbg.enterDecision(4, decisionCanBacktrack[4]);

                int LA4_0 = input.LA(1);

                if ( (LA4_0==IF) ) {
                    alt4=1;
                }
                else if ( (LA4_0==DBLDASH) ) {
                    alt4=2;
                }


                } finally {dbg.exitDecision(4);}

                switch (alt4) {
            	case 1 :
            	    dbg.enterAlt(1);

            	    // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:4:19: ifstatement ( param )+ ( ELSE ( param )+ )? ENDIF
            	    {
            	    dbg.location(4,19);
            	    pushFollow(FOLLOW_ifstatement_in_command20);
            	    ifstatement2=ifstatement();

            	    state._fsp--;

            	    adaptor.addChild(root_0, ifstatement2.getTree());
            	    dbg.location(4,31);
            	    // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:4:31: ( param )+
            	    int cnt1=0;
            	    try { dbg.enterSubRule(1);

            	    loop1:
            	    do {
            	        int alt1=2;
            	        try { dbg.enterDecision(1, decisionCanBacktrack[1]);

            	        int LA1_0 = input.LA(1);

            	        if ( (LA1_0==DBLDASH) ) {
            	            alt1=1;
            	        }


            	        } finally {dbg.exitDecision(1);}

            	        switch (alt1) {
            	    	case 1 :
            	    	    dbg.enterAlt(1);

            	    	    // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:4:31: param
            	    	    {
            	    	    dbg.location(4,31);
            	    	    pushFollow(FOLLOW_param_in_command22);
            	    	    param3=param();

            	    	    state._fsp--;

            	    	    adaptor.addChild(root_0, param3.getTree());

            	    	    }
            	    	    break;

            	    	default :
            	    	    if ( cnt1 >= 1 ) break loop1;
            	                EarlyExitException eee =
            	                    new EarlyExitException(1, input);
            	                dbg.recognitionException(eee);

            	                throw eee;
            	        }
            	        cnt1++;
            	    } while (true);
            	    } finally {dbg.exitSubRule(1);}

            	    dbg.location(4,38);
            	    // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:4:38: ( ELSE ( param )+ )?
            	    int alt3=2;
            	    try { dbg.enterSubRule(3);
            	    try { dbg.enterDecision(3, decisionCanBacktrack[3]);

            	    int LA3_0 = input.LA(1);

            	    if ( (LA3_0==ELSE) ) {
            	        alt3=1;
            	    }
            	    } finally {dbg.exitDecision(3);}

            	    switch (alt3) {
            	        case 1 :
            	            dbg.enterAlt(1);

            	            // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:4:39: ELSE ( param )+
            	            {
            	            dbg.location(4,39);
            	            ELSE4=(Token)match(input,ELSE,FOLLOW_ELSE_in_command26); 
            	            ELSE4_tree = (Object)adaptor.create(ELSE4);
            	            adaptor.addChild(root_0, ELSE4_tree);

            	            dbg.location(4,44);
            	            // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:4:44: ( param )+
            	            int cnt2=0;
            	            try { dbg.enterSubRule(2);

            	            loop2:
            	            do {
            	                int alt2=2;
            	                try { dbg.enterDecision(2, decisionCanBacktrack[2]);

            	                int LA2_0 = input.LA(1);

            	                if ( (LA2_0==DBLDASH) ) {
            	                    alt2=1;
            	                }


            	                } finally {dbg.exitDecision(2);}

            	                switch (alt2) {
            	            	case 1 :
            	            	    dbg.enterAlt(1);

            	            	    // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:4:44: param
            	            	    {
            	            	    dbg.location(4,44);
            	            	    pushFollow(FOLLOW_param_in_command28);
            	            	    param5=param();

            	            	    state._fsp--;

            	            	    adaptor.addChild(root_0, param5.getTree());

            	            	    }
            	            	    break;

            	            	default :
            	            	    if ( cnt2 >= 1 ) break loop2;
            	                        EarlyExitException eee =
            	                            new EarlyExitException(2, input);
            	                        dbg.recognitionException(eee);

            	                        throw eee;
            	                }
            	                cnt2++;
            	            } while (true);
            	            } finally {dbg.exitSubRule(2);}


            	            }
            	            break;

            	    }
            	    } finally {dbg.exitSubRule(3);}

            	    dbg.location(4,53);
            	    ENDIF6=(Token)match(input,ENDIF,FOLLOW_ENDIF_in_command33); 
            	    ENDIF6_tree = (Object)adaptor.create(ENDIF6);
            	    adaptor.addChild(root_0, ENDIF6_tree);


            	    }
            	    break;
            	case 2 :
            	    dbg.enterAlt(2);

            	    // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:4:61: param
            	    {
            	    dbg.location(4,61);
            	    pushFollow(FOLLOW_param_in_command37);
            	    param7=param();

            	    state._fsp--;

            	    adaptor.addChild(root_0, param7.getTree());

            	    }
            	    break;

            	default :
            	    break loop4;
                }
            } while (true);
            } finally {dbg.exitSubRule(4);}


            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        dbg.location(5, 2);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "command");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "command"

    public static class binary_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "binary"
    // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:7:1: binary : WORD ;
    public final GalaxyToolConfigParser.binary_return binary() throws RecognitionException {
        GalaxyToolConfigParser.binary_return retval = new GalaxyToolConfigParser.binary_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token WORD8=null;

        Object WORD8_tree=null;

        try { dbg.enterRule(getGrammarFileName(), "binary");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(7, 1);

        try {
            // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:7:9: ( WORD )
            dbg.enterAlt(1);

            // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:7:11: WORD
            {
            root_0 = (Object)adaptor.nil();

            dbg.location(7,11);
            WORD8=(Token)match(input,WORD,FOLLOW_WORD_in_binary51); 
            WORD8_tree = (Object)adaptor.create(WORD8);
            adaptor.addChild(root_0, WORD8_tree);


            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        dbg.location(8, 2);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "binary");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "binary"

    public static class ifstatement_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "ifstatement"
    // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:10:1: ifstatement : IF ( STRING | VARIABLE ) EQTEST ( STRING | VARIABLE ) COLON ;
    public final GalaxyToolConfigParser.ifstatement_return ifstatement() throws RecognitionException {
        GalaxyToolConfigParser.ifstatement_return retval = new GalaxyToolConfigParser.ifstatement_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token IF9=null;
        Token set10=null;
        Token EQTEST11=null;
        Token set12=null;
        Token COLON13=null;

        Object IF9_tree=null;
        Object set10_tree=null;
        Object EQTEST11_tree=null;
        Object set12_tree=null;
        Object COLON13_tree=null;

        try { dbg.enterRule(getGrammarFileName(), "ifstatement");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(10, 1);

        try {
            // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:11:2: ( IF ( STRING | VARIABLE ) EQTEST ( STRING | VARIABLE ) COLON )
            dbg.enterAlt(1);

            // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:11:4: IF ( STRING | VARIABLE ) EQTEST ( STRING | VARIABLE ) COLON
            {
            root_0 = (Object)adaptor.nil();

            dbg.location(11,4);
            IF9=(Token)match(input,IF,FOLLOW_IF_in_ifstatement63); 
            IF9_tree = (Object)adaptor.create(IF9);
            adaptor.addChild(root_0, IF9_tree);

            dbg.location(11,7);
            set10=(Token)input.LT(1);
            if ( (input.LA(1)>=STRING && input.LA(1)<=VARIABLE) ) {
                input.consume();
                adaptor.addChild(root_0, (Object)adaptor.create(set10));
                state.errorRecovery=false;
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                dbg.recognitionException(mse);
                throw mse;
            }

            dbg.location(11,25);
            EQTEST11=(Token)match(input,EQTEST,FOLLOW_EQTEST_in_ifstatement71); 
            EQTEST11_tree = (Object)adaptor.create(EQTEST11);
            adaptor.addChild(root_0, EQTEST11_tree);

            dbg.location(11,32);
            set12=(Token)input.LT(1);
            if ( (input.LA(1)>=STRING && input.LA(1)<=VARIABLE) ) {
                input.consume();
                adaptor.addChild(root_0, (Object)adaptor.create(set12));
                state.errorRecovery=false;
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                dbg.recognitionException(mse);
                throw mse;
            }

            dbg.location(11,50);
            COLON13=(Token)match(input,COLON,FOLLOW_COLON_in_ifstatement79); 
            COLON13_tree = (Object)adaptor.create(COLON13);
            adaptor.addChild(root_0, COLON13_tree);


            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        dbg.location(12, 2);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "ifstatement");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "ifstatement"

    public static class param_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "param"
    // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:14:1: param : DBLDASH ( WORD )* EQ ( VARIABLE | STRING ) ;
    public final GalaxyToolConfigParser.param_return param() throws RecognitionException {
        GalaxyToolConfigParser.param_return retval = new GalaxyToolConfigParser.param_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token DBLDASH14=null;
        Token WORD15=null;
        Token EQ16=null;
        Token set17=null;

        Object DBLDASH14_tree=null;
        Object WORD15_tree=null;
        Object EQ16_tree=null;
        Object set17_tree=null;

        try { dbg.enterRule(getGrammarFileName(), "param");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(14, 1);

        try {
            // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:14:8: ( DBLDASH ( WORD )* EQ ( VARIABLE | STRING ) )
            dbg.enterAlt(1);

            // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:14:10: DBLDASH ( WORD )* EQ ( VARIABLE | STRING )
            {
            root_0 = (Object)adaptor.nil();

            dbg.location(14,10);
            DBLDASH14=(Token)match(input,DBLDASH,FOLLOW_DBLDASH_in_param90); 
            DBLDASH14_tree = (Object)adaptor.create(DBLDASH14);
            adaptor.addChild(root_0, DBLDASH14_tree);

            dbg.location(14,18);
            // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:14:18: ( WORD )*
            try { dbg.enterSubRule(5);

            loop5:
            do {
                int alt5=2;
                try { dbg.enterDecision(5, decisionCanBacktrack[5]);

                int LA5_0 = input.LA(1);

                if ( (LA5_0==WORD) ) {
                    alt5=1;
                }


                } finally {dbg.exitDecision(5);}

                switch (alt5) {
            	case 1 :
            	    dbg.enterAlt(1);

            	    // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:14:18: WORD
            	    {
            	    dbg.location(14,18);
            	    WORD15=(Token)match(input,WORD,FOLLOW_WORD_in_param92); 
            	    WORD15_tree = (Object)adaptor.create(WORD15);
            	    adaptor.addChild(root_0, WORD15_tree);


            	    }
            	    break;

            	default :
            	    break loop5;
                }
            } while (true);
            } finally {dbg.exitSubRule(5);}

            dbg.location(14,24);
            EQ16=(Token)match(input,EQ,FOLLOW_EQ_in_param95); 
            EQ16_tree = (Object)adaptor.create(EQ16);
            adaptor.addChild(root_0, EQ16_tree);

            dbg.location(14,27);
            set17=(Token)input.LT(1);
            if ( (input.LA(1)>=STRING && input.LA(1)<=VARIABLE) ) {
                input.consume();
                adaptor.addChild(root_0, (Object)adaptor.create(set17));
                state.errorRecovery=false;
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                dbg.recognitionException(mse);
                throw mse;
            }


            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        dbg.location(15, 2);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "param");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "param"

    public static class text_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "text"
    // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:17:1: text : ( WORD )+ ;
    public final GalaxyToolConfigParser.text_return text() throws RecognitionException {
        GalaxyToolConfigParser.text_return retval = new GalaxyToolConfigParser.text_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token WORD18=null;

        Object WORD18_tree=null;

        try { dbg.enterRule(getGrammarFileName(), "text");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(17, 1);

        try {
            // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:17:7: ( ( WORD )+ )
            dbg.enterAlt(1);

            // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:17:9: ( WORD )+
            {
            root_0 = (Object)adaptor.nil();

            dbg.location(17,9);
            // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:17:9: ( WORD )+
            int cnt6=0;
            try { dbg.enterSubRule(6);

            loop6:
            do {
                int alt6=2;
                try { dbg.enterDecision(6, decisionCanBacktrack[6]);

                int LA6_0 = input.LA(1);

                if ( (LA6_0==WORD) ) {
                    alt6=1;
                }


                } finally {dbg.exitDecision(6);}

                switch (alt6) {
            	case 1 :
            	    dbg.enterAlt(1);

            	    // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:17:9: WORD
            	    {
            	    dbg.location(17,9);
            	    WORD18=(Token)match(input,WORD,FOLLOW_WORD_in_text113); 
            	    WORD18_tree = (Object)adaptor.create(WORD18);
            	    adaptor.addChild(root_0, WORD18_tree);


            	    }
            	    break;

            	default :
            	    if ( cnt6 >= 1 ) break loop6;
                        EarlyExitException eee =
                            new EarlyExitException(6, input);
                        dbg.recognitionException(eee);

                        throw eee;
                }
                cnt6++;
            } while (true);
            } finally {dbg.exitSubRule(6);}


            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        dbg.location(18, 2);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "text");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "text"

    // Delegated rules


 

    public static final BitSet FOLLOW_binary_in_command17 = new BitSet(new long[]{0x0000000000001082L});
    public static final BitSet FOLLOW_ifstatement_in_command20 = new BitSet(new long[]{0x0000000000001080L});
    public static final BitSet FOLLOW_param_in_command22 = new BitSet(new long[]{0x00000000000010B0L});
    public static final BitSet FOLLOW_ELSE_in_command26 = new BitSet(new long[]{0x0000000000001080L});
    public static final BitSet FOLLOW_param_in_command28 = new BitSet(new long[]{0x00000000000010A0L});
    public static final BitSet FOLLOW_ENDIF_in_command33 = new BitSet(new long[]{0x0000000000001082L});
    public static final BitSet FOLLOW_param_in_command37 = new BitSet(new long[]{0x0000000000001082L});
    public static final BitSet FOLLOW_WORD_in_binary51 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IF_in_ifstatement63 = new BitSet(new long[]{0x0000000000000300L});
    public static final BitSet FOLLOW_set_in_ifstatement65 = new BitSet(new long[]{0x0000000000000400L});
    public static final BitSet FOLLOW_EQTEST_in_ifstatement71 = new BitSet(new long[]{0x0000000000000300L});
    public static final BitSet FOLLOW_set_in_ifstatement73 = new BitSet(new long[]{0x0000000000000800L});
    public static final BitSet FOLLOW_COLON_in_ifstatement79 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DBLDASH_in_param90 = new BitSet(new long[]{0x0000000000002040L});
    public static final BitSet FOLLOW_WORD_in_param92 = new BitSet(new long[]{0x0000000000002040L});
    public static final BitSet FOLLOW_EQ_in_param95 = new BitSet(new long[]{0x0000000000000300L});
    public static final BitSet FOLLOW_set_in_param97 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_WORD_in_text113 = new BitSet(new long[]{0x0000000000000042L});

}