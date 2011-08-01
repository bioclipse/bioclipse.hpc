package net.bioclipse.hpc.domains.toolconfig.parser;
// $ANTLR 3.3 Nov 30, 2010 12:45:30 /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g 2011-07-28 13:19:41

import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class GalaxyToolConfigLexer extends Lexer {
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

    public GalaxyToolConfigLexer() {;} 
    public GalaxyToolConfigLexer(CharStream input) {
        this(input, new RecognizerSharedState());
    }
    public GalaxyToolConfigLexer(CharStream input, RecognizerSharedState state) {
        super(input,state);

    }
    public String getGrammarFileName() { return "/home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g"; }

    // $ANTLR start "PARAM"
    public final void mPARAM() throws RecognitionException {
        try {
            int _type = PARAM;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:7:8: ( DBLDASH ( WORD )* EQ ( VARIABLE | STRING ) )
            // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:7:10: DBLDASH ( WORD )* EQ ( VARIABLE | STRING )
            {
            mDBLDASH(); 
            // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:7:18: ( WORD )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( ((LA1_0>='A' && LA1_0<='Z')||(LA1_0>='a' && LA1_0<='z')) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:7:18: WORD
            	    {
            	    mWORD(); 

            	    }
            	    break;

            	default :
            	    break loop1;
                }
            } while (true);

            mEQ(); 
            // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:7:27: ( VARIABLE | STRING )
            int alt2=2;
            int LA2_0 = input.LA(1);

            if ( (LA2_0=='$') ) {
                alt2=1;
            }
            else if ( (LA2_0=='\"') ) {
                alt2=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 2, 0, input);

                throw nvae;
            }
            switch (alt2) {
                case 1 :
                    // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:7:28: VARIABLE
                    {
                    mVARIABLE(); 

                    }
                    break;
                case 2 :
                    // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:7:37: STRING
                    {
                    mSTRING(); 

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
    // $ANTLR end "PARAM"

    // $ANTLR start "WORD"
    public final void mWORD() throws RecognitionException {
        try {
            int _type = WORD;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:8:6: ( ( 'a' .. 'z' | 'A' .. 'Z' ) ( 'a' .. 'z' | 'A' .. 'Z' | '.' | '_' | '0' .. '9' )* )
            // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:8:8: ( 'a' .. 'z' | 'A' .. 'Z' ) ( 'a' .. 'z' | 'A' .. 'Z' | '.' | '_' | '0' .. '9' )*
            {
            if ( (input.LA(1)>='A' && input.LA(1)<='Z')||(input.LA(1)>='a' && input.LA(1)<='z') ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:8:27: ( 'a' .. 'z' | 'A' .. 'Z' | '.' | '_' | '0' .. '9' )*
            loop3:
            do {
                int alt3=2;
                int LA3_0 = input.LA(1);

                if ( (LA3_0=='.'||(LA3_0>='0' && LA3_0<='9')||(LA3_0>='A' && LA3_0<='Z')||LA3_0=='_'||(LA3_0>='a' && LA3_0<='z')) ) {
                    alt3=1;
                }


                switch (alt3) {
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
            	    break loop3;
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
            // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:10:2: ( '$' ( '{' )? WORD ( '}' )? )
            // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:10:4: '$' ( '{' )? WORD ( '}' )?
            {
            match('$'); 
            // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:10:7: ( '{' )?
            int alt4=2;
            int LA4_0 = input.LA(1);

            if ( (LA4_0=='{') ) {
                alt4=1;
            }
            switch (alt4) {
                case 1 :
                    // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:10:8: '{'
                    {
                    match('{'); 

                    }
                    break;

            }

            mWORD(); 
            // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:10:17: ( '}' )?
            int alt5=2;
            int LA5_0 = input.LA(1);

            if ( (LA5_0=='}') ) {
                alt5=1;
            }
            switch (alt5) {
                case 1 :
                    // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:10:18: '}'
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
            // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:11:8: ( '\"' ( 'a' .. 'z' | 'A' .. 'Z' )+ '\"' )
            // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:11:10: '\"' ( 'a' .. 'z' | 'A' .. 'Z' )+ '\"'
            {
            match('\"'); 
            // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:11:13: ( 'a' .. 'z' | 'A' .. 'Z' )+
            int cnt6=0;
            loop6:
            do {
                int alt6=2;
                int LA6_0 = input.LA(1);

                if ( ((LA6_0>='A' && LA6_0<='Z')||(LA6_0>='a' && LA6_0<='z')) ) {
                    alt6=1;
                }


                switch (alt6) {
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
            	    if ( cnt6 >= 1 ) break loop6;
                        EarlyExitException eee =
                            new EarlyExitException(6, input);
                        throw eee;
                }
                cnt6++;
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
            // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:12:4: ( '#if' )
            // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:12:6: '#if'
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
            // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:13:6: ( '#else' )
            // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:13:8: '#else'
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
            // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:14:8: ( '#end if' )
            // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:14:10: '#end if'
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
            // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:15:4: ( '=' )
            // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:15:6: '='
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
            // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:16:9: ( '==' )
            // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:16:11: '=='
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
            // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:17:9: ( '--' )
            // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:17:11: '--'
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
            // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:18:8: ( ':' )
            // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:18:10: ':'
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
            // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:19:5: ( ( ' ' | '\\t' | '\\r' | '\\n' ) )
            // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:19:9: ( ' ' | '\\t' | '\\r' | '\\n' )
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
        // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:1:8: ( PARAM | WORD | VARIABLE | STRING | IF | ELSE | ENDIF | EQ | EQTEST | DBLDASH | COLON | WS )
        int alt7=12;
        alt7 = dfa7.predict(input);
        switch (alt7) {
            case 1 :
                // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:1:10: PARAM
                {
                mPARAM(); 

                }
                break;
            case 2 :
                // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:1:16: WORD
                {
                mWORD(); 

                }
                break;
            case 3 :
                // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:1:21: VARIABLE
                {
                mVARIABLE(); 

                }
                break;
            case 4 :
                // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:1:30: STRING
                {
                mSTRING(); 

                }
                break;
            case 5 :
                // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:1:37: IF
                {
                mIF(); 

                }
                break;
            case 6 :
                // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:1:40: ELSE
                {
                mELSE(); 

                }
                break;
            case 7 :
                // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:1:45: ENDIF
                {
                mENDIF(); 

                }
                break;
            case 8 :
                // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:1:51: EQ
                {
                mEQ(); 

                }
                break;
            case 9 :
                // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:1:54: EQTEST
                {
                mEQTEST(); 

                }
                break;
            case 10 :
                // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:1:61: DBLDASH
                {
                mDBLDASH(); 

                }
                break;
            case 11 :
                // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:1:69: COLON
                {
                mCOLON(); 

                }
                break;
            case 12 :
                // /home/samuel/projects/galaxy-toolconfig-bnf/GalaxyToolConfig.g:1:75: WS
                {
                mWS(); 

                }
                break;

        }

    }


    protected DFA7 dfa7 = new DFA7(this);
    static final String DFA7_eotS =
        "\6\uffff\1\15\2\uffff\1\16\10\uffff";
    static final String DFA7_eofS =
        "\22\uffff";
    static final String DFA7_minS =
        "\1\11\1\55\3\uffff\1\145\1\75\2\uffff\1\75\1\uffff\1\154\6\uffff";
    static final String DFA7_maxS =
        "\1\172\1\55\3\uffff\1\151\1\75\2\uffff\1\172\1\uffff\1\156\6\uffff";
    static final String DFA7_acceptS =
        "\2\uffff\1\2\1\3\1\4\2\uffff\1\13\1\14\1\uffff\1\5\1\uffff\1\11"+
        "\1\10\1\12\1\1\1\6\1\7";
    static final String DFA7_specialS =
        "\22\uffff}>";
    static final String[] DFA7_transitionS = {
            "\2\10\2\uffff\1\10\22\uffff\1\10\1\uffff\1\4\1\5\1\3\10\uffff"+
            "\1\1\14\uffff\1\7\2\uffff\1\6\3\uffff\32\2\6\uffff\32\2",
            "\1\11",
            "",
            "",
            "",
            "\1\13\3\uffff\1\12",
            "\1\14",
            "",
            "",
            "\1\17\3\uffff\32\17\6\uffff\32\17",
            "",
            "\1\20\1\uffff\1\21",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA7_eot = DFA.unpackEncodedString(DFA7_eotS);
    static final short[] DFA7_eof = DFA.unpackEncodedString(DFA7_eofS);
    static final char[] DFA7_min = DFA.unpackEncodedStringToUnsignedChars(DFA7_minS);
    static final char[] DFA7_max = DFA.unpackEncodedStringToUnsignedChars(DFA7_maxS);
    static final short[] DFA7_accept = DFA.unpackEncodedString(DFA7_acceptS);
    static final short[] DFA7_special = DFA.unpackEncodedString(DFA7_specialS);
    static final short[][] DFA7_transition;

    static {
        int numStates = DFA7_transitionS.length;
        DFA7_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA7_transition[i] = DFA.unpackEncodedString(DFA7_transitionS[i]);
        }
    }

    class DFA7 extends DFA {

        public DFA7(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 7;
            this.eot = DFA7_eot;
            this.eof = DFA7_eof;
            this.min = DFA7_min;
            this.max = DFA7_max;
            this.accept = DFA7_accept;
            this.special = DFA7_special;
            this.transition = DFA7_transition;
        }
        public String getDescription() {
            return "1:1: Tokens : ( PARAM | WORD | VARIABLE | STRING | IF | ELSE | ENDIF | EQ | EQTEST | DBLDASH | COLON | WS );";
        }
    }
 

}