// $ANTLR 3.3 Nov 30, 2010 12:45:30 /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g 2011-07-27 19:01:00

package net.bioclipse.uppmax.galaxytoolconfigparser;

import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class GalaxyToolConfigLexer extends Lexer {
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

    public GalaxyToolConfigLexer() {;} 
    public GalaxyToolConfigLexer(CharStream input) {
        this(input, new RecognizerSharedState());
    }
    public GalaxyToolConfigLexer(CharStream input, RecognizerSharedState state) {
        super(input,state);

    }
    public String getGrammarFileName() { return "/home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g"; }

    // $ANTLR start "WORD"
    public final void mWORD() throws RecognitionException {
        try {
            int _type = WORD;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:20:6: ( ( 'a' .. 'z' | 'A' .. 'Z' ) ( 'a' .. 'z' | 'A' .. 'Z' | '.' | '_' | '0' .. '9' )* )
            // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:20:8: ( 'a' .. 'z' | 'A' .. 'Z' ) ( 'a' .. 'z' | 'A' .. 'Z' | '.' | '_' | '0' .. '9' )*
            {
            if ( (input.LA(1)>='A' && input.LA(1)<='Z')||(input.LA(1)>='a' && input.LA(1)<='z') ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:20:27: ( 'a' .. 'z' | 'A' .. 'Z' | '.' | '_' | '0' .. '9' )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( (LA1_0=='.'||(LA1_0>='0' && LA1_0<='9')||(LA1_0>='A' && LA1_0<='Z')||LA1_0=='_'||(LA1_0>='a' && LA1_0<='z')) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:
            	    {
            	    if ( input.LA(1)=='.'||(input.LA(1)>='0' && input.LA(1)<='9')||(input.LA(1)>='A' && input.LA(1)<='Z')||input.LA(1)=='_'||(input.LA(1)>='a' && input.LA(1)<='z') ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    break loop1;
                }
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "WORD"

    // $ANTLR start "VARIABLE"
    public final void mVARIABLE() throws RecognitionException {
        try {
            int _type = VARIABLE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:24:2: ( '$' ( '{' )? WORD ( '}' )? )
            // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:24:4: '$' ( '{' )? WORD ( '}' )?
            {
            match('$'); 
            // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:24:7: ( '{' )?
            int alt2=2;
            int LA2_0 = input.LA(1);

            if ( (LA2_0=='{') ) {
                alt2=1;
            }
            switch (alt2) {
                case 1 :
                    // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:24:8: '{'
                    {
                    match('{'); 

                    }
                    break;

            }

            mWORD(); 
            // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:24:17: ( '}' )?
            int alt3=2;
            int LA3_0 = input.LA(1);

            if ( (LA3_0=='}') ) {
                alt3=1;
            }
            switch (alt3) {
                case 1 :
                    // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:24:18: '}'
                    {
                    match('}'); 

                    }
                    break;

            }


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "VARIABLE"

    // $ANTLR start "STRING"
    public final void mSTRING() throws RecognitionException {
        try {
            int _type = STRING;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:27:8: ( '\"' ( 'a' .. 'z' | 'A' .. 'Z' )+ '\"' )
            // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:27:10: '\"' ( 'a' .. 'z' | 'A' .. 'Z' )+ '\"'
            {
            match('\"'); 
            // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:27:13: ( 'a' .. 'z' | 'A' .. 'Z' )+
            int cnt4=0;
            loop4:
            do {
                int alt4=2;
                int LA4_0 = input.LA(1);

                if ( ((LA4_0>='A' && LA4_0<='Z')||(LA4_0>='a' && LA4_0<='z')) ) {
                    alt4=1;
                }


                switch (alt4) {
            	case 1 :
            	    // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:
            	    {
            	    if ( (input.LA(1)>='A' && input.LA(1)<='Z')||(input.LA(1)>='a' && input.LA(1)<='z') ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    if ( cnt4 >= 1 ) break loop4;
                        EarlyExitException eee =
                            new EarlyExitException(4, input);
                        throw eee;
                }
                cnt4++;
            } while (true);

            match('\"'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "STRING"

    // $ANTLR start "IF"
    public final void mIF() throws RecognitionException {
        try {
            int _type = IF;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:30:4: ( '#if' )
            // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:30:6: '#if'
            {
            match("#if"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "IF"

    // $ANTLR start "ELSE"
    public final void mELSE() throws RecognitionException {
        try {
            int _type = ELSE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:33:6: ( '#else' )
            // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:33:8: '#else'
            {
            match("#else"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "ELSE"

    // $ANTLR start "ENDIF"
    public final void mENDIF() throws RecognitionException {
        try {
            int _type = ENDIF;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:36:8: ( '#end if' )
            // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:36:10: '#end if'
            {
            match("#end if"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "ENDIF"

    // $ANTLR start "EQ"
    public final void mEQ() throws RecognitionException {
        try {
            int _type = EQ;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:39:4: ( '=' )
            // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:39:6: '='
            {
            match('='); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "EQ"

    // $ANTLR start "EQTEST"
    public final void mEQTEST() throws RecognitionException {
        try {
            int _type = EQTEST;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:42:9: ( '==' )
            // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:42:11: '=='
            {
            match("=="); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "EQTEST"

    // $ANTLR start "DBLDASH"
    public final void mDBLDASH() throws RecognitionException {
        try {
            int _type = DBLDASH;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:45:9: ( '--' )
            // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:45:11: '--'
            {
            match("--"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "DBLDASH"

    // $ANTLR start "COLON"
    public final void mCOLON() throws RecognitionException {
        try {
            int _type = COLON;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:48:8: ( ':' )
            // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:48:10: ':'
            {
            match(':'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "COLON"

    // $ANTLR start "WS"
    public final void mWS() throws RecognitionException {
        try {
            int _type = WS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:51:5: ( ( ' ' | '\\t' | '\\r' | '\\n' ) )
            // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:51:9: ( ' ' | '\\t' | '\\r' | '\\n' )
            {
            if ( (input.LA(1)>='\t' && input.LA(1)<='\n')||input.LA(1)=='\r'||input.LA(1)==' ' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            _channel=HIDDEN;

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "WS"

    public void mTokens() throws RecognitionException {
        // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:1:8: ( WORD | VARIABLE | STRING | IF | ELSE | ENDIF | EQ | EQTEST | DBLDASH | COLON | WS )
        int alt5=11;
        alt5 = dfa5.predict(input);
        switch (alt5) {
            case 1 :
                // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:1:10: WORD
                {
                mWORD(); 

                }
                break;
            case 2 :
                // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:1:15: VARIABLE
                {
                mVARIABLE(); 

                }
                break;
            case 3 :
                // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:1:24: STRING
                {
                mSTRING(); 

                }
                break;
            case 4 :
                // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:1:31: IF
                {
                mIF(); 

                }
                break;
            case 5 :
                // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:1:34: ELSE
                {
                mELSE(); 

                }
                break;
            case 6 :
                // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:1:39: ENDIF
                {
                mENDIF(); 

                }
                break;
            case 7 :
                // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:1:45: EQ
                {
                mEQ(); 

                }
                break;
            case 8 :
                // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:1:48: EQTEST
                {
                mEQTEST(); 

                }
                break;
            case 9 :
                // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:1:55: DBLDASH
                {
                mDBLDASH(); 

                }
                break;
            case 10 :
                // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:1:63: COLON
                {
                mCOLON(); 

                }
                break;
            case 11 :
                // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:1:69: WS
                {
                mWS(); 

                }
                break;

        }

    }


    protected DFA5 dfa5 = new DFA5(this);
    static final String DFA5_eotS =
        "\5\uffff\1\14\11\uffff";
    static final String DFA5_eofS =
        "\17\uffff";
    static final String DFA5_minS =
        "\1\11\3\uffff\1\145\1\75\4\uffff\1\154\4\uffff";
    static final String DFA5_maxS =
        "\1\172\3\uffff\1\151\1\75\4\uffff\1\156\4\uffff";
    static final String DFA5_acceptS =
        "\1\uffff\1\1\1\2\1\3\2\uffff\1\11\1\12\1\13\1\4\1\uffff\1\10\1\7"+
        "\1\5\1\6";
    static final String DFA5_specialS =
        "\17\uffff}>";
    static final String[] DFA5_transitionS = {
            "\2\10\2\uffff\1\10\22\uffff\1\10\1\uffff\1\3\1\4\1\2\10\uffff"+
            "\1\6\14\uffff\1\7\2\uffff\1\5\3\uffff\32\1\6\uffff\32\1",
            "",
            "",
            "",
            "\1\12\3\uffff\1\11",
            "\1\13",
            "",
            "",
            "",
            "",
            "\1\15\1\uffff\1\16",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA5_eot = DFA.unpackEncodedString(DFA5_eotS);
    static final short[] DFA5_eof = DFA.unpackEncodedString(DFA5_eofS);
    static final char[] DFA5_min = DFA.unpackEncodedStringToUnsignedChars(DFA5_minS);
    static final char[] DFA5_max = DFA.unpackEncodedStringToUnsignedChars(DFA5_maxS);
    static final short[] DFA5_accept = DFA.unpackEncodedString(DFA5_acceptS);
    static final short[] DFA5_special = DFA.unpackEncodedString(DFA5_specialS);
    static final short[][] DFA5_transition;

    static {
        int numStates = DFA5_transitionS.length;
        DFA5_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA5_transition[i] = DFA.unpackEncodedString(DFA5_transitionS[i]);
        }
    }

    class DFA5 extends DFA {

        public DFA5(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 5;
            this.eot = DFA5_eot;
            this.eof = DFA5_eof;
            this.min = DFA5_min;
            this.max = DFA5_max;
            this.accept = DFA5_accept;
            this.special = DFA5_special;
            this.transition = DFA5_transition;
        }
        public String getDescription() {
            return "1:1: Tokens : ( WORD | VARIABLE | STRING | IF | ELSE | ENDIF | EQ | EQTEST | DBLDASH | COLON | WS );";
        }
    }
 

}