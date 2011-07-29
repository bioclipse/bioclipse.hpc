package net.bioclipse.hpc.domains.toolconfig.parser;
// $ANTLR 3.3 Nov 30, 2010 12:45:30 /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g 2011-07-28 13:19:41

import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;


import org.antlr.runtime.tree.*;

public class GalaxyToolConfigParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "PARAM", "ELSE", "ENDIF", "WORD", "IF", "STRING", "VARIABLE", "EQTEST", "COLON", "DBLDASH", "EQ", "WS"
    };
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
    // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:4:1: command : binary ( ifstmt ( PARAM )+ ( ELSE ( PARAM )+ )? ENDIF | PARAM )* ;
    public final GalaxyToolConfigParser.command_return command() throws RecognitionException {
        GalaxyToolConfigParser.command_return retval = new GalaxyToolConfigParser.command_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token PARAM3=null;
        Token ELSE4=null;
        Token PARAM5=null;
        Token ENDIF6=null;
        Token PARAM7=null;
        GalaxyToolConfigParser.binary_return binary1 = null;

        GalaxyToolConfigParser.ifstmt_return ifstmt2 = null;


        Object PARAM3_tree=null;
        Object ELSE4_tree=null;
        Object PARAM5_tree=null;
        Object ENDIF6_tree=null;
        Object PARAM7_tree=null;

        try {
            // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:4:9: ( binary ( ifstmt ( PARAM )+ ( ELSE ( PARAM )+ )? ENDIF | PARAM )* )
            // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:4:11: binary ( ifstmt ( PARAM )+ ( ELSE ( PARAM )+ )? ENDIF | PARAM )*
            {
            root_0 = (Object)adaptor.nil();

            pushFollow(FOLLOW_binary_in_command17);
            binary1=binary();

            state._fsp--;

            adaptor.addChild(root_0, binary1.getTree());
            // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:4:18: ( ifstmt ( PARAM )+ ( ELSE ( PARAM )+ )? ENDIF | PARAM )*
            loop4:
            do {
                int alt4=3;
                int LA4_0 = input.LA(1);

                if ( (LA4_0==IF) ) {
                    alt4=1;
                }
                else if ( (LA4_0==PARAM) ) {
                    alt4=2;
                }


                switch (alt4) {
            	case 1 :
            	    // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:4:19: ifstmt ( PARAM )+ ( ELSE ( PARAM )+ )? ENDIF
            	    {
            	    pushFollow(FOLLOW_ifstmt_in_command20);
            	    ifstmt2=ifstmt();

            	    state._fsp--;

            	    adaptor.addChild(root_0, ifstmt2.getTree());
            	    // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:4:26: ( PARAM )+
            	    int cnt1=0;
            	    loop1:
            	    do {
            	        int alt1=2;
            	        int LA1_0 = input.LA(1);

            	        if ( (LA1_0==PARAM) ) {
            	            alt1=1;
            	        }


            	        switch (alt1) {
            	    	case 1 :
            	    	    // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:4:26: PARAM
            	    	    {
            	    	    PARAM3=(Token)match(input,PARAM,FOLLOW_PARAM_in_command22); 
            	    	    PARAM3_tree = (Object)adaptor.create(PARAM3);
            	    	    adaptor.addChild(root_0, PARAM3_tree);


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

            	    // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:4:33: ( ELSE ( PARAM )+ )?
            	    int alt3=2;
            	    int LA3_0 = input.LA(1);

            	    if ( (LA3_0==ELSE) ) {
            	        alt3=1;
            	    }
            	    switch (alt3) {
            	        case 1 :
            	            // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:4:34: ELSE ( PARAM )+
            	            {
            	            ELSE4=(Token)match(input,ELSE,FOLLOW_ELSE_in_command26); 
            	            ELSE4_tree = (Object)adaptor.create(ELSE4);
            	            adaptor.addChild(root_0, ELSE4_tree);

            	            // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:4:39: ( PARAM )+
            	            int cnt2=0;
            	            loop2:
            	            do {
            	                int alt2=2;
            	                int LA2_0 = input.LA(1);

            	                if ( (LA2_0==PARAM) ) {
            	                    alt2=1;
            	                }


            	                switch (alt2) {
            	            	case 1 :
            	            	    // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:4:39: PARAM
            	            	    {
            	            	    PARAM5=(Token)match(input,PARAM,FOLLOW_PARAM_in_command28); 
            	            	    PARAM5_tree = (Object)adaptor.create(PARAM5);
            	            	    adaptor.addChild(root_0, PARAM5_tree);


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
            	    // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:4:56: PARAM
            	    {
            	    PARAM7=(Token)match(input,PARAM,FOLLOW_PARAM_in_command37); 
            	    PARAM7_tree = (Object)adaptor.create(PARAM7);
            	    adaptor.addChild(root_0, PARAM7_tree);


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
    // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:5:1: binary : WORD ;
    public final GalaxyToolConfigParser.binary_return binary() throws RecognitionException {
        GalaxyToolConfigParser.binary_return retval = new GalaxyToolConfigParser.binary_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token WORD8=null;

        Object WORD8_tree=null;

        try {
            // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:5:9: ( WORD )
            // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:5:11: WORD
            {
            root_0 = (Object)adaptor.nil();

            WORD8=(Token)match(input,WORD,FOLLOW_WORD_in_binary47); 
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

    public static class ifstmt_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "ifstmt"
    // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:6:1: ifstmt : IF ( STRING | VARIABLE ) EQTEST ( STRING | VARIABLE ) COLON ;
    public final GalaxyToolConfigParser.ifstmt_return ifstmt() throws RecognitionException {
        GalaxyToolConfigParser.ifstmt_return retval = new GalaxyToolConfigParser.ifstmt_return();
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
            // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:6:8: ( IF ( STRING | VARIABLE ) EQTEST ( STRING | VARIABLE ) COLON )
            // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:6:10: IF ( STRING | VARIABLE ) EQTEST ( STRING | VARIABLE ) COLON
            {
            root_0 = (Object)adaptor.nil();

            IF9=(Token)match(input,IF,FOLLOW_IF_in_ifstmt54); 
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

            EQTEST11=(Token)match(input,EQTEST,FOLLOW_EQTEST_in_ifstmt62); 
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

            COLON13=(Token)match(input,COLON,FOLLOW_COLON_in_ifstmt70); 
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
    // $ANTLR end "ifstmt"

    // Delegated rules


 

    public static final BitSet FOLLOW_binary_in_command17 = new BitSet(new long[]{0x0000000000000112L});
    public static final BitSet FOLLOW_ifstmt_in_command20 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_PARAM_in_command22 = new BitSet(new long[]{0x0000000000000070L});
    public static final BitSet FOLLOW_ELSE_in_command26 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_PARAM_in_command28 = new BitSet(new long[]{0x0000000000000050L});
    public static final BitSet FOLLOW_ENDIF_in_command33 = new BitSet(new long[]{0x0000000000000112L});
    public static final BitSet FOLLOW_PARAM_in_command37 = new BitSet(new long[]{0x0000000000000112L});
    public static final BitSet FOLLOW_WORD_in_binary47 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IF_in_ifstmt54 = new BitSet(new long[]{0x0000000000000600L});
    public static final BitSet FOLLOW_set_in_ifstmt56 = new BitSet(new long[]{0x0000000000000800L});
    public static final BitSet FOLLOW_EQTEST_in_ifstmt62 = new BitSet(new long[]{0x0000000000000600L});
    public static final BitSet FOLLOW_set_in_ifstmt64 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_COLON_in_ifstmt70 = new BitSet(new long[]{0x0000000000000002L});

}