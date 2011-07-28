package net.bioclipse.uppmax.galaxytoolconfigparser;
// $ANTLR 3.3 Nov 30, 2010 12:45:30 /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g 2011-07-28 10:14:42

import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;


import org.antlr.runtime.tree.*;

public class GalaxyToolConfigParser extends Parser {
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


        public GalaxyToolConfigParser(TokenStream input) {
            this(input, new RecognizerSharedState());
        }
        public GalaxyToolConfigParser(TokenStream input, RecognizerSharedState state) {
            super(input, state);
             
        }
        
    protected TreeAdaptor adaptor = new CommonTreeAdaptor();

    public void setTreeAdaptor(TreeAdaptor adaptor) {
        this.adaptor = adaptor;
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

        try {
            // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:4:9: ( binary ( ifstatement ( param )+ ( ELSE ( param )+ )? ENDIF | param )* )
            // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:4:11: binary ( ifstatement ( param )+ ( ELSE ( param )+ )? ENDIF | param )*
            {
            root_0 = (Object)adaptor.nil();

            pushFollow(FOLLOW_binary_in_command17);
            binary1=binary();

            state._fsp--;

            adaptor.addChild(root_0, binary1.getTree());
            // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:4:18: ( ifstatement ( param )+ ( ELSE ( param )+ )? ENDIF | param )*
            loop4:
            do {
                int alt4=3;
                int LA4_0 = input.LA(1);

                if ( (LA4_0==IF) ) {
                    alt4=1;
                }
                else if ( (LA4_0==DBLDASH) ) {
                    alt4=2;
                }


                switch (alt4) {
            	case 1 :
            	    // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:4:19: ifstatement ( param )+ ( ELSE ( param )+ )? ENDIF
            	    {
            	    pushFollow(FOLLOW_ifstatement_in_command20);
            	    ifstatement2=ifstatement();

            	    state._fsp--;

            	    adaptor.addChild(root_0, ifstatement2.getTree());
            	    // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:4:31: ( param )+
            	    int cnt1=0;
            	    loop1:
            	    do {
            	        int alt1=2;
            	        int LA1_0 = input.LA(1);

            	        if ( (LA1_0==DBLDASH) ) {
            	            alt1=1;
            	        }


            	        switch (alt1) {
            	    	case 1 :
            	    	    // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:4:31: param
            	    	    {
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
            	                throw eee;
            	        }
            	        cnt1++;
            	    } while (true);

            	    // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:4:38: ( ELSE ( param )+ )?
            	    int alt3=2;
            	    int LA3_0 = input.LA(1);

            	    if ( (LA3_0==ELSE) ) {
            	        alt3=1;
            	    }
            	    switch (alt3) {
            	        case 1 :
            	            // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:4:39: ELSE ( param )+
            	            {
            	            ELSE4=(Token)match(input,ELSE,FOLLOW_ELSE_in_command26); 
            	            ELSE4_tree = (Object)adaptor.create(ELSE4);
            	            adaptor.addChild(root_0, ELSE4_tree);

            	            // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:4:44: ( param )+
            	            int cnt2=0;
            	            loop2:
            	            do {
            	                int alt2=2;
            	                int LA2_0 = input.LA(1);

            	                if ( (LA2_0==DBLDASH) ) {
            	                    alt2=1;
            	                }


            	                switch (alt2) {
            	            	case 1 :
            	            	    // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:4:44: param
            	            	    {
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
            	                        throw eee;
            	                }
            	                cnt2++;
            	            } while (true);


            	            }
            	            break;

            	    }

            	    ENDIF6=(Token)match(input,ENDIF,FOLLOW_ENDIF_in_command33); 
            	    ENDIF6_tree = (Object)adaptor.create(ENDIF6);
            	    adaptor.addChild(root_0, ENDIF6_tree);


            	    }
            	    break;
            	case 2 :
            	    // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:4:61: param
            	    {
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

        try {
            // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:7:9: ( WORD )
            // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:7:11: WORD
            {
            root_0 = (Object)adaptor.nil();

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

        try {
            // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:11:2: ( IF ( STRING | VARIABLE ) EQTEST ( STRING | VARIABLE ) COLON )
            // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:11:4: IF ( STRING | VARIABLE ) EQTEST ( STRING | VARIABLE ) COLON
            {
            root_0 = (Object)adaptor.nil();

            IF9=(Token)match(input,IF,FOLLOW_IF_in_ifstatement63); 
            IF9_tree = (Object)adaptor.create(IF9);
            adaptor.addChild(root_0, IF9_tree);

            set10=(Token)input.LT(1);
            if ( (input.LA(1)>=STRING && input.LA(1)<=VARIABLE) ) {
                input.consume();
                adaptor.addChild(root_0, (Object)adaptor.create(set10));
                state.errorRecovery=false;
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                throw mse;
            }

            EQTEST11=(Token)match(input,EQTEST,FOLLOW_EQTEST_in_ifstatement71); 
            EQTEST11_tree = (Object)adaptor.create(EQTEST11);
            adaptor.addChild(root_0, EQTEST11_tree);

            set12=(Token)input.LT(1);
            if ( (input.LA(1)>=STRING && input.LA(1)<=VARIABLE) ) {
                input.consume();
                adaptor.addChild(root_0, (Object)adaptor.create(set12));
                state.errorRecovery=false;
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                throw mse;
            }

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

        try {
            // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:14:8: ( DBLDASH ( WORD )* EQ ( VARIABLE | STRING ) )
            // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:14:10: DBLDASH ( WORD )* EQ ( VARIABLE | STRING )
            {
            root_0 = (Object)adaptor.nil();

            DBLDASH14=(Token)match(input,DBLDASH,FOLLOW_DBLDASH_in_param90); 
            DBLDASH14_tree = (Object)adaptor.create(DBLDASH14);
            adaptor.addChild(root_0, DBLDASH14_tree);

            // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:14:18: ( WORD )*
            loop5:
            do {
                int alt5=2;
                int LA5_0 = input.LA(1);

                if ( (LA5_0==WORD) ) {
                    alt5=1;
                }


                switch (alt5) {
            	case 1 :
            	    // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:14:18: WORD
            	    {
            	    WORD15=(Token)match(input,WORD,FOLLOW_WORD_in_param92); 
            	    WORD15_tree = (Object)adaptor.create(WORD15);
            	    adaptor.addChild(root_0, WORD15_tree);


            	    }
            	    break;

            	default :
            	    break loop5;
                }
            } while (true);

            EQ16=(Token)match(input,EQ,FOLLOW_EQ_in_param95); 
            EQ16_tree = (Object)adaptor.create(EQ16);
            adaptor.addChild(root_0, EQ16_tree);

            set17=(Token)input.LT(1);
            if ( (input.LA(1)>=STRING && input.LA(1)<=VARIABLE) ) {
                input.consume();
                adaptor.addChild(root_0, (Object)adaptor.create(set17));
                state.errorRecovery=false;
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
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
        return retval;
    }
    // $ANTLR end "param"

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

}