// Generated from C:/Code/EzASM/src/main/java/com/ezasm/assembler\EzASM.g4 by ANTLR 4.10.1
package com.ezasm.assembler.parser;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class EzASMLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.10.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, Comment=3, Identifier=4, LabelDef=5, NewLine=6, Register=7, 
		Decimal=8, Hex=9, Char=10, String=11, WS=12;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"T__0", "T__1", "Comment", "Identifier", "LabelDef", "NewLine", "Register", 
			"Decimal", "Hex", "Char", "String", "WS"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'('", "')'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, "Comment", "Identifier", "LabelDef", "NewLine", "Register", 
			"Decimal", "Hex", "Char", "String", "WS"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}


	public EzASMLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "EzASM.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\u0004\u0000\f\\\u0006\uffff\uffff\u0002\u0000\u0007\u0000\u0002\u0001"+
		"\u0007\u0001\u0002\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004"+
		"\u0007\u0004\u0002\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007"+
		"\u0007\u0007\u0002\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b"+
		"\u0007\u000b\u0001\u0000\u0001\u0000\u0001\u0001\u0001\u0001\u0001\u0002"+
		"\u0001\u0002\u0005\u0002 \b\u0002\n\u0002\f\u0002#\t\u0002\u0001\u0003"+
		"\u0004\u0003&\b\u0003\u000b\u0003\f\u0003\'\u0001\u0004\u0001\u0004\u0001"+
		"\u0004\u0001\u0005\u0004\u0005.\b\u0005\u000b\u0005\f\u0005/\u0001\u0006"+
		"\u0001\u0006\u0004\u00064\b\u0006\u000b\u0006\f\u00065\u0001\u0007\u0003"+
		"\u00079\b\u0007\u0001\u0007\u0004\u0007<\b\u0007\u000b\u0007\f\u0007="+
		"\u0001\u0007\u0003\u0007A\b\u0007\u0001\b\u0003\bD\b\b\u0001\b\u0001\b"+
		"\u0001\b\u0001\t\u0001\t\u0001\t\u0001\t\u0001\n\u0001\n\u0005\nO\b\n"+
		"\n\n\f\nR\t\n\u0001\n\u0001\n\u0001\u000b\u0004\u000bW\b\u000b\u000b\u000b"+
		"\f\u000bX\u0001\u000b\u0001\u000b\u0001P\u0000\f\u0001\u0001\u0003\u0002"+
		"\u0005\u0003\u0007\u0004\t\u0005\u000b\u0006\r\u0007\u000f\b\u0011\t\u0013"+
		"\n\u0015\u000b\u0017\f\u0001\u0000\u0007\u0002\u0000\n\n\r\r\u0005\u0000"+
		"..AZ\\\\__az\u0003\u000009AZaz\u0002\u0000++--\u0001\u000009\u0003\u0000"+
		"09AFaf\u0002\u0000\t\t  e\u0000\u0001\u0001\u0000\u0000\u0000\u0000\u0003"+
		"\u0001\u0000\u0000\u0000\u0000\u0005\u0001\u0000\u0000\u0000\u0000\u0007"+
		"\u0001\u0000\u0000\u0000\u0000\t\u0001\u0000\u0000\u0000\u0000\u000b\u0001"+
		"\u0000\u0000\u0000\u0000\r\u0001\u0000\u0000\u0000\u0000\u000f\u0001\u0000"+
		"\u0000\u0000\u0000\u0011\u0001\u0000\u0000\u0000\u0000\u0013\u0001\u0000"+
		"\u0000\u0000\u0000\u0015\u0001\u0000\u0000\u0000\u0000\u0017\u0001\u0000"+
		"\u0000\u0000\u0001\u0019\u0001\u0000\u0000\u0000\u0003\u001b\u0001\u0000"+
		"\u0000\u0000\u0005\u001d\u0001\u0000\u0000\u0000\u0007%\u0001\u0000\u0000"+
		"\u0000\t)\u0001\u0000\u0000\u0000\u000b-\u0001\u0000\u0000\u0000\r1\u0001"+
		"\u0000\u0000\u0000\u000f@\u0001\u0000\u0000\u0000\u0011C\u0001\u0000\u0000"+
		"\u0000\u0013H\u0001\u0000\u0000\u0000\u0015L\u0001\u0000\u0000\u0000\u0017"+
		"V\u0001\u0000\u0000\u0000\u0019\u001a\u0005(\u0000\u0000\u001a\u0002\u0001"+
		"\u0000\u0000\u0000\u001b\u001c\u0005)\u0000\u0000\u001c\u0004\u0001\u0000"+
		"\u0000\u0000\u001d!\u0005#\u0000\u0000\u001e \b\u0000\u0000\u0000\u001f"+
		"\u001e\u0001\u0000\u0000\u0000 #\u0001\u0000\u0000\u0000!\u001f\u0001"+
		"\u0000\u0000\u0000!\"\u0001\u0000\u0000\u0000\"\u0006\u0001\u0000\u0000"+
		"\u0000#!\u0001\u0000\u0000\u0000$&\u0007\u0001\u0000\u0000%$\u0001\u0000"+
		"\u0000\u0000&\'\u0001\u0000\u0000\u0000\'%\u0001\u0000\u0000\u0000\'("+
		"\u0001\u0000\u0000\u0000(\b\u0001\u0000\u0000\u0000)*\u0003\u0007\u0003"+
		"\u0000*+\u0005:\u0000\u0000+\n\u0001\u0000\u0000\u0000,.\u0007\u0000\u0000"+
		"\u0000-,\u0001\u0000\u0000\u0000./\u0001\u0000\u0000\u0000/-\u0001\u0000"+
		"\u0000\u0000/0\u0001\u0000\u0000\u00000\f\u0001\u0000\u0000\u000013\u0005"+
		"$\u0000\u000024\u0007\u0002\u0000\u000032\u0001\u0000\u0000\u000045\u0001"+
		"\u0000\u0000\u000053\u0001\u0000\u0000\u000056\u0001\u0000\u0000\u0000"+
		"6\u000e\u0001\u0000\u0000\u000079\u0007\u0003\u0000\u000087\u0001\u0000"+
		"\u0000\u000089\u0001\u0000\u0000\u00009;\u0001\u0000\u0000\u0000:<\u0007"+
		"\u0004\u0000\u0000;:\u0001\u0000\u0000\u0000<=\u0001\u0000\u0000\u0000"+
		"=;\u0001\u0000\u0000\u0000=>\u0001\u0000\u0000\u0000>A\u0001\u0000\u0000"+
		"\u0000?A\u00050\u0000\u0000@8\u0001\u0000\u0000\u0000@?\u0001\u0000\u0000"+
		"\u0000A\u0010\u0001\u0000\u0000\u0000BD\u0007\u0003\u0000\u0000CB\u0001"+
		"\u0000\u0000\u0000CD\u0001\u0000\u0000\u0000DE\u0001\u0000\u0000\u0000"+
		"EF\u0007\u0005\u0000\u0000FG\u0005h\u0000\u0000G\u0012\u0001\u0000\u0000"+
		"\u0000HI\u0005\'\u0000\u0000IJ\t\u0000\u0000\u0000JK\u0005\'\u0000\u0000"+
		"K\u0014\u0001\u0000\u0000\u0000LP\u0005\"\u0000\u0000MO\t\u0000\u0000"+
		"\u0000NM\u0001\u0000\u0000\u0000OR\u0001\u0000\u0000\u0000PQ\u0001\u0000"+
		"\u0000\u0000PN\u0001\u0000\u0000\u0000QS\u0001\u0000\u0000\u0000RP\u0001"+
		"\u0000\u0000\u0000ST\u0005\"\u0000\u0000T\u0016\u0001\u0000\u0000\u0000"+
		"UW\u0007\u0006\u0000\u0000VU\u0001\u0000\u0000\u0000WX\u0001\u0000\u0000"+
		"\u0000XV\u0001\u0000\u0000\u0000XY\u0001\u0000\u0000\u0000YZ\u0001\u0000"+
		"\u0000\u0000Z[\u0006\u000b\u0000\u0000[\u0018\u0001\u0000\u0000\u0000"+
		"\u000b\u0000!\'/58=@CPX\u0001\u0006\u0000\u0000";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}