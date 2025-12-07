package io.github.mosser.arduinoml.externals.antlr;// Generated from Arduinoml.g4 by ANTLR 4.13.1
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue"})
public class ArduinomlParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.13.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, T__10=11, T__11=12, T__12=13, T__13=14, T__14=15, T__15=16, T__16=17, 
		T__17=18, T__18=19, T__19=20, T__20=21, T__21=22, T__22=23, T__23=24, 
		T__24=25, T__25=26, PORT_NUMBER=27, INT=28, IDENTIFIER=29, SIGNAL=30, 
		TIMEUNIT=31, STRING=32, NEWLINE=33, WS=34, COMMENT=35;
	public static final int
		RULE_root = 0, RULE_declaration = 1, RULE_bricks = 2, RULE_sensor = 3, 
		RULE_actuator = 4, RULE_buzzer = 5, RULE_lcd = 6, RULE_pin = 7, RULE_states = 8, 
		RULE_state = 9, RULE_stateBlock = 10, RULE_action = 11, RULE_actuatorAction = 12, 
		RULE_lcdAction = 13, RULE_buzzerAction = 14, RULE_transition = 15, RULE_conditionalTransition = 16, 
		RULE_temporalTransition = 17, RULE_initial = 18, RULE_condition = 19, 
		RULE_orCondition = 20, RULE_andCondition = 21, RULE_unaryCondition = 22, 
		RULE_primaryCondition = 23, RULE_sensorCondition = 24;
	private static String[] makeRuleNames() {
		return new String[] {
			"root", "declaration", "bricks", "sensor", "actuator", "buzzer", "lcd", 
			"pin", "states", "state", "stateBlock", "action", "actuatorAction", "lcdAction", 
			"buzzerAction", "transition", "conditionalTransition", "temporalTransition", 
			"initial", "condition", "orCondition", "andCondition", "unaryCondition", 
			"primaryCondition", "sensorCondition"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'application'", "'sensor'", "'actuator'", "'buzzer'", "'lcd'", 
			"':'", "'x'", "'state'", "'{'", "'}'", "'set'", "'to'", "'display'", 
			"'on'", "'sound'", "'when'", "'goto'", "'after'", "'initial'", "'or'", 
			"'and'", "'not'", "'('", "')'", "'is'", "'pushed'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, "PORT_NUMBER", "INT", "IDENTIFIER", "SIGNAL", "TIMEUNIT", 
			"STRING", "NEWLINE", "WS", "COMMENT"
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

	@Override
	public String getGrammarFileName() { return "Arduinoml.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public ArduinomlParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@SuppressWarnings("CheckReturnValue")
	public static class RootContext extends ParserRuleContext {
		public DeclarationContext declaration() {
			return getRuleContext(DeclarationContext.class,0);
		}
		public BricksContext bricks() {
			return getRuleContext(BricksContext.class,0);
		}
		public StatesContext states() {
			return getRuleContext(StatesContext.class,0);
		}
		public TerminalNode EOF() { return getToken(ArduinomlParser.EOF, 0); }
		public RootContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_root; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ArduinomlListener) ((ArduinomlListener)listener).enterRoot(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ArduinomlListener) ((ArduinomlListener)listener).exitRoot(this);
		}
	}

	public final RootContext root() throws RecognitionException {
		RootContext _localctx = new RootContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_root);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(50);
			declaration();
			setState(51);
			bricks();
			setState(52);
			states();
			setState(53);
			match(EOF);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class DeclarationContext extends ParserRuleContext {
		public Token name;
		public TerminalNode IDENTIFIER() { return getToken(ArduinomlParser.IDENTIFIER, 0); }
		public DeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_declaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ArduinomlListener) ((ArduinomlListener)listener).enterDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ArduinomlListener) ((ArduinomlListener)listener).exitDeclaration(this);
		}
	}

	public final DeclarationContext declaration() throws RecognitionException {
		DeclarationContext _localctx = new DeclarationContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_declaration);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(55);
			match(T__0);
			setState(56);
			((DeclarationContext)_localctx).name = match(IDENTIFIER);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class BricksContext extends ParserRuleContext {
		public List<SensorContext> sensor() {
			return getRuleContexts(SensorContext.class);
		}
		public SensorContext sensor(int i) {
			return getRuleContext(SensorContext.class,i);
		}
		public List<ActuatorContext> actuator() {
			return getRuleContexts(ActuatorContext.class);
		}
		public ActuatorContext actuator(int i) {
			return getRuleContext(ActuatorContext.class,i);
		}
		public List<BuzzerContext> buzzer() {
			return getRuleContexts(BuzzerContext.class);
		}
		public BuzzerContext buzzer(int i) {
			return getRuleContext(BuzzerContext.class,i);
		}
		public List<LcdContext> lcd() {
			return getRuleContexts(LcdContext.class);
		}
		public LcdContext lcd(int i) {
			return getRuleContext(LcdContext.class,i);
		}
		public BricksContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_bricks; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ArduinomlListener) ((ArduinomlListener)listener).enterBricks(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ArduinomlListener) ((ArduinomlListener)listener).exitBricks(this);
		}
	}

	public final BricksContext bricks() throws RecognitionException {
		BricksContext _localctx = new BricksContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_bricks);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(62); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				setState(62);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case T__1:
					{
					setState(58);
					sensor();
					}
					break;
				case T__2:
					{
					setState(59);
					actuator();
					}
					break;
				case T__3:
					{
					setState(60);
					buzzer();
					}
					break;
				case T__4:
					{
					setState(61);
					lcd();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				setState(64); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & 60L) != 0) );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SensorContext extends ParserRuleContext {
		public PinContext pin() {
			return getRuleContext(PinContext.class,0);
		}
		public SensorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_sensor; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ArduinomlListener) ((ArduinomlListener)listener).enterSensor(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ArduinomlListener) ((ArduinomlListener)listener).exitSensor(this);
		}
	}

	public final SensorContext sensor() throws RecognitionException {
		SensorContext _localctx = new SensorContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_sensor);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(66);
			match(T__1);
			setState(67);
			pin();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ActuatorContext extends ParserRuleContext {
		public PinContext pin() {
			return getRuleContext(PinContext.class,0);
		}
		public ActuatorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_actuator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ArduinomlListener) ((ArduinomlListener)listener).enterActuator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ArduinomlListener) ((ArduinomlListener)listener).exitActuator(this);
		}
	}

	public final ActuatorContext actuator() throws RecognitionException {
		ActuatorContext _localctx = new ActuatorContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_actuator);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(69);
			match(T__2);
			setState(70);
			pin();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class BuzzerContext extends ParserRuleContext {
		public PinContext pin() {
			return getRuleContext(PinContext.class,0);
		}
		public BuzzerContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_buzzer; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ArduinomlListener) ((ArduinomlListener)listener).enterBuzzer(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ArduinomlListener) ((ArduinomlListener)listener).exitBuzzer(this);
		}
	}

	public final BuzzerContext buzzer() throws RecognitionException {
		BuzzerContext _localctx = new BuzzerContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_buzzer);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(72);
			match(T__3);
			setState(73);
			pin();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class LcdContext extends ParserRuleContext {
		public Token id;
		public Token columns;
		public Token rows;
		public TerminalNode IDENTIFIER() { return getToken(ArduinomlParser.IDENTIFIER, 0); }
		public List<TerminalNode> INT() { return getTokens(ArduinomlParser.INT); }
		public TerminalNode INT(int i) {
			return getToken(ArduinomlParser.INT, i);
		}
		public LcdContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_lcd; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ArduinomlListener) ((ArduinomlListener)listener).enterLcd(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ArduinomlListener) ((ArduinomlListener)listener).exitLcd(this);
		}
	}

	public final LcdContext lcd() throws RecognitionException {
		LcdContext _localctx = new LcdContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_lcd);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(75);
			match(T__4);
			setState(76);
			((LcdContext)_localctx).id = match(IDENTIFIER);
			setState(77);
			match(T__5);
			setState(78);
			((LcdContext)_localctx).columns = match(INT);
			setState(79);
			match(T__6);
			setState(80);
			((LcdContext)_localctx).rows = match(INT);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class PinContext extends ParserRuleContext {
		public Token id;
		public Token port;
		public TerminalNode IDENTIFIER() { return getToken(ArduinomlParser.IDENTIFIER, 0); }
		public TerminalNode PORT_NUMBER() { return getToken(ArduinomlParser.PORT_NUMBER, 0); }
		public PinContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_pin; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ArduinomlListener) ((ArduinomlListener)listener).enterPin(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ArduinomlListener) ((ArduinomlListener)listener).exitPin(this);
		}
	}

	public final PinContext pin() throws RecognitionException {
		PinContext _localctx = new PinContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_pin);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(82);
			((PinContext)_localctx).id = match(IDENTIFIER);
			setState(83);
			match(T__5);
			setState(84);
			((PinContext)_localctx).port = match(PORT_NUMBER);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class StatesContext extends ParserRuleContext {
		public List<StateContext> state() {
			return getRuleContexts(StateContext.class);
		}
		public StateContext state(int i) {
			return getRuleContext(StateContext.class,i);
		}
		public StatesContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_states; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ArduinomlListener) ((ArduinomlListener)listener).enterStates(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ArduinomlListener) ((ArduinomlListener)listener).exitStates(this);
		}
	}

	public final StatesContext states() throws RecognitionException {
		StatesContext _localctx = new StatesContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_states);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(87); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(86);
				state();
				}
				}
				setState(89); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==T__7 );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class StateContext extends ParserRuleContext {
		public Token name;
		public StateBlockContext stateBlock() {
			return getRuleContext(StateBlockContext.class,0);
		}
		public TerminalNode IDENTIFIER() { return getToken(ArduinomlParser.IDENTIFIER, 0); }
		public InitialContext initial() {
			return getRuleContext(InitialContext.class,0);
		}
		public StateContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_state; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ArduinomlListener) ((ArduinomlListener)listener).enterState(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ArduinomlListener) ((ArduinomlListener)listener).exitState(this);
		}
	}

	public final StateContext state() throws RecognitionException {
		StateContext _localctx = new StateContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_state);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(91);
			match(T__7);
			setState(92);
			((StateContext)_localctx).name = match(IDENTIFIER);
			setState(93);
			match(T__8);
			setState(94);
			stateBlock();
			setState(95);
			match(T__9);
			setState(97);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__18) {
				{
				setState(96);
				initial();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class StateBlockContext extends ParserRuleContext {
		public List<ActionContext> action() {
			return getRuleContexts(ActionContext.class);
		}
		public ActionContext action(int i) {
			return getRuleContext(ActionContext.class,i);
		}
		public List<TransitionContext> transition() {
			return getRuleContexts(TransitionContext.class);
		}
		public TransitionContext transition(int i) {
			return getRuleContext(TransitionContext.class,i);
		}
		public StateBlockContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_stateBlock; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ArduinomlListener) ((ArduinomlListener)listener).enterStateBlock(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ArduinomlListener) ((ArduinomlListener)listener).exitStateBlock(this);
		}
	}

	public final StateBlockContext stateBlock() throws RecognitionException {
		StateBlockContext _localctx = new StateBlockContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_stateBlock);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(101); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				setState(101);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case T__10:
				case T__12:
				case T__14:
					{
					setState(99);
					action();
					}
					break;
				case T__15:
				case T__17:
					{
					setState(100);
					transition();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				setState(103); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & 370688L) != 0) );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ActionContext extends ParserRuleContext {
		public ActuatorActionContext actuatorAction() {
			return getRuleContext(ActuatorActionContext.class,0);
		}
		public LcdActionContext lcdAction() {
			return getRuleContext(LcdActionContext.class,0);
		}
		public BuzzerActionContext buzzerAction() {
			return getRuleContext(BuzzerActionContext.class,0);
		}
		public ActionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_action; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ArduinomlListener) ((ArduinomlListener)listener).enterAction(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ArduinomlListener) ((ArduinomlListener)listener).exitAction(this);
		}
	}

	public final ActionContext action() throws RecognitionException {
		ActionContext _localctx = new ActionContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_action);
		try {
			setState(108);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__10:
				enterOuterAlt(_localctx, 1);
				{
				setState(105);
				actuatorAction();
				}
				break;
			case T__12:
				enterOuterAlt(_localctx, 2);
				{
				setState(106);
				lcdAction();
				}
				break;
			case T__14:
				enterOuterAlt(_localctx, 3);
				{
				setState(107);
				buzzerAction();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ActuatorActionContext extends ParserRuleContext {
		public Token target;
		public Token value;
		public TerminalNode IDENTIFIER() { return getToken(ArduinomlParser.IDENTIFIER, 0); }
		public TerminalNode SIGNAL() { return getToken(ArduinomlParser.SIGNAL, 0); }
		public ActuatorActionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_actuatorAction; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ArduinomlListener) ((ArduinomlListener)listener).enterActuatorAction(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ArduinomlListener) ((ArduinomlListener)listener).exitActuatorAction(this);
		}
	}

	public final ActuatorActionContext actuatorAction() throws RecognitionException {
		ActuatorActionContext _localctx = new ActuatorActionContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_actuatorAction);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(110);
			match(T__10);
			setState(111);
			((ActuatorActionContext)_localctx).target = match(IDENTIFIER);
			setState(112);
			match(T__11);
			setState(113);
			((ActuatorActionContext)_localctx).value = match(SIGNAL);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class LcdActionContext extends ParserRuleContext {
		public Token message;
		public Token screen;
		public TerminalNode STRING() { return getToken(ArduinomlParser.STRING, 0); }
		public TerminalNode IDENTIFIER() { return getToken(ArduinomlParser.IDENTIFIER, 0); }
		public LcdActionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_lcdAction; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ArduinomlListener) ((ArduinomlListener)listener).enterLcdAction(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ArduinomlListener) ((ArduinomlListener)listener).exitLcdAction(this);
		}
	}

	public final LcdActionContext lcdAction() throws RecognitionException {
		LcdActionContext _localctx = new LcdActionContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_lcdAction);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(115);
			match(T__12);
			setState(116);
			((LcdActionContext)_localctx).message = match(STRING);
			setState(117);
			match(T__13);
			setState(118);
			((LcdActionContext)_localctx).screen = match(IDENTIFIER);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class BuzzerActionContext extends ParserRuleContext {
		public Token target;
		public TerminalNode IDENTIFIER() { return getToken(ArduinomlParser.IDENTIFIER, 0); }
		public BuzzerActionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_buzzerAction; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ArduinomlListener) ((ArduinomlListener)listener).enterBuzzerAction(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ArduinomlListener) ((ArduinomlListener)listener).exitBuzzerAction(this);
		}
	}

	public final BuzzerActionContext buzzerAction() throws RecognitionException {
		BuzzerActionContext _localctx = new BuzzerActionContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_buzzerAction);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(120);
			match(T__14);
			setState(121);
			((BuzzerActionContext)_localctx).target = match(IDENTIFIER);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class TransitionContext extends ParserRuleContext {
		public ConditionalTransitionContext conditionalTransition() {
			return getRuleContext(ConditionalTransitionContext.class,0);
		}
		public TemporalTransitionContext temporalTransition() {
			return getRuleContext(TemporalTransitionContext.class,0);
		}
		public TransitionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_transition; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ArduinomlListener) ((ArduinomlListener)listener).enterTransition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ArduinomlListener) ((ArduinomlListener)listener).exitTransition(this);
		}
	}

	public final TransitionContext transition() throws RecognitionException {
		TransitionContext _localctx = new TransitionContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_transition);
		try {
			setState(125);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__15:
				enterOuterAlt(_localctx, 1);
				{
				setState(123);
				conditionalTransition();
				}
				break;
			case T__17:
				enterOuterAlt(_localctx, 2);
				{
				setState(124);
				temporalTransition();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ConditionalTransitionContext extends ParserRuleContext {
		public Token next;
		public ConditionContext condition() {
			return getRuleContext(ConditionContext.class,0);
		}
		public TerminalNode IDENTIFIER() { return getToken(ArduinomlParser.IDENTIFIER, 0); }
		public ConditionalTransitionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_conditionalTransition; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ArduinomlListener) ((ArduinomlListener)listener).enterConditionalTransition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ArduinomlListener) ((ArduinomlListener)listener).exitConditionalTransition(this);
		}
	}

	public final ConditionalTransitionContext conditionalTransition() throws RecognitionException {
		ConditionalTransitionContext _localctx = new ConditionalTransitionContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_conditionalTransition);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(127);
			match(T__15);
			setState(128);
			condition();
			setState(129);
			match(T__16);
			setState(130);
			((ConditionalTransitionContext)_localctx).next = match(IDENTIFIER);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class TemporalTransitionContext extends ParserRuleContext {
		public Token duration;
		public Token unit;
		public Token next;
		public TerminalNode INT() { return getToken(ArduinomlParser.INT, 0); }
		public TerminalNode TIMEUNIT() { return getToken(ArduinomlParser.TIMEUNIT, 0); }
		public TerminalNode IDENTIFIER() { return getToken(ArduinomlParser.IDENTIFIER, 0); }
		public TemporalTransitionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_temporalTransition; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ArduinomlListener) ((ArduinomlListener)listener).enterTemporalTransition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ArduinomlListener) ((ArduinomlListener)listener).exitTemporalTransition(this);
		}
	}

	public final TemporalTransitionContext temporalTransition() throws RecognitionException {
		TemporalTransitionContext _localctx = new TemporalTransitionContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_temporalTransition);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(132);
			match(T__17);
			setState(133);
			((TemporalTransitionContext)_localctx).duration = match(INT);
			setState(134);
			((TemporalTransitionContext)_localctx).unit = match(TIMEUNIT);
			setState(135);
			match(T__16);
			setState(136);
			((TemporalTransitionContext)_localctx).next = match(IDENTIFIER);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class InitialContext extends ParserRuleContext {
		public InitialContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_initial; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ArduinomlListener) ((ArduinomlListener)listener).enterInitial(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ArduinomlListener) ((ArduinomlListener)listener).exitInitial(this);
		}
	}

	public final InitialContext initial() throws RecognitionException {
		InitialContext _localctx = new InitialContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_initial);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(138);
			match(T__18);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ConditionContext extends ParserRuleContext {
		public OrConditionContext orCondition() {
			return getRuleContext(OrConditionContext.class,0);
		}
		public ConditionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_condition; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ArduinomlListener) ((ArduinomlListener)listener).enterCondition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ArduinomlListener) ((ArduinomlListener)listener).exitCondition(this);
		}
	}

	public final ConditionContext condition() throws RecognitionException {
		ConditionContext _localctx = new ConditionContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_condition);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(140);
			orCondition();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class OrConditionContext extends ParserRuleContext {
		public List<AndConditionContext> andCondition() {
			return getRuleContexts(AndConditionContext.class);
		}
		public AndConditionContext andCondition(int i) {
			return getRuleContext(AndConditionContext.class,i);
		}
		public OrConditionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_orCondition; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ArduinomlListener) ((ArduinomlListener)listener).enterOrCondition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ArduinomlListener) ((ArduinomlListener)listener).exitOrCondition(this);
		}
	}

	public final OrConditionContext orCondition() throws RecognitionException {
		OrConditionContext _localctx = new OrConditionContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_orCondition);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(142);
			andCondition();
			setState(147);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__19) {
				{
				{
				setState(143);
				match(T__19);
				setState(144);
				andCondition();
				}
				}
				setState(149);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class AndConditionContext extends ParserRuleContext {
		public List<UnaryConditionContext> unaryCondition() {
			return getRuleContexts(UnaryConditionContext.class);
		}
		public UnaryConditionContext unaryCondition(int i) {
			return getRuleContext(UnaryConditionContext.class,i);
		}
		public AndConditionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_andCondition; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ArduinomlListener) ((ArduinomlListener)listener).enterAndCondition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ArduinomlListener) ((ArduinomlListener)listener).exitAndCondition(this);
		}
	}

	public final AndConditionContext andCondition() throws RecognitionException {
		AndConditionContext _localctx = new AndConditionContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_andCondition);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(150);
			unaryCondition();
			setState(155);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__20) {
				{
				{
				setState(151);
				match(T__20);
				setState(152);
				unaryCondition();
				}
				}
				setState(157);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class UnaryConditionContext extends ParserRuleContext {
		public UnaryConditionContext unaryCondition() {
			return getRuleContext(UnaryConditionContext.class,0);
		}
		public PrimaryConditionContext primaryCondition() {
			return getRuleContext(PrimaryConditionContext.class,0);
		}
		public UnaryConditionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_unaryCondition; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ArduinomlListener) ((ArduinomlListener)listener).enterUnaryCondition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ArduinomlListener) ((ArduinomlListener)listener).exitUnaryCondition(this);
		}
	}

	public final UnaryConditionContext unaryCondition() throws RecognitionException {
		UnaryConditionContext _localctx = new UnaryConditionContext(_ctx, getState());
		enterRule(_localctx, 44, RULE_unaryCondition);
		try {
			setState(161);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__21:
				enterOuterAlt(_localctx, 1);
				{
				setState(158);
				match(T__21);
				setState(159);
				unaryCondition();
				}
				break;
			case T__22:
			case IDENTIFIER:
				enterOuterAlt(_localctx, 2);
				{
				setState(160);
				primaryCondition();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class PrimaryConditionContext extends ParserRuleContext {
		public SensorConditionContext sensorCondition() {
			return getRuleContext(SensorConditionContext.class,0);
		}
		public ConditionContext condition() {
			return getRuleContext(ConditionContext.class,0);
		}
		public PrimaryConditionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_primaryCondition; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ArduinomlListener) ((ArduinomlListener)listener).enterPrimaryCondition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ArduinomlListener) ((ArduinomlListener)listener).exitPrimaryCondition(this);
		}
	}

	public final PrimaryConditionContext primaryCondition() throws RecognitionException {
		PrimaryConditionContext _localctx = new PrimaryConditionContext(_ctx, getState());
		enterRule(_localctx, 46, RULE_primaryCondition);
		try {
			setState(168);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IDENTIFIER:
				enterOuterAlt(_localctx, 1);
				{
				setState(163);
				sensorCondition();
				}
				break;
			case T__22:
				enterOuterAlt(_localctx, 2);
				{
				setState(164);
				match(T__22);
				setState(165);
				condition();
				setState(166);
				match(T__23);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SensorConditionContext extends ParserRuleContext {
		public Token sensorId;
		public Token value;
		public TerminalNode IDENTIFIER() { return getToken(ArduinomlParser.IDENTIFIER, 0); }
		public TerminalNode SIGNAL() { return getToken(ArduinomlParser.SIGNAL, 0); }
		public SensorConditionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_sensorCondition; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ArduinomlListener) ((ArduinomlListener)listener).enterSensorCondition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ArduinomlListener) ((ArduinomlListener)listener).exitSensorCondition(this);
		}
	}

	public final SensorConditionContext sensorCondition() throws RecognitionException {
		SensorConditionContext _localctx = new SensorConditionContext(_ctx, getState());
		enterRule(_localctx, 48, RULE_sensorCondition);
		try {
			setState(175);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,12,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(170);
				((SensorConditionContext)_localctx).sensorId = match(IDENTIFIER);
				setState(171);
				match(T__24);
				setState(172);
				((SensorConditionContext)_localctx).value = match(SIGNAL);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(173);
				((SensorConditionContext)_localctx).sensorId = match(IDENTIFIER);
				setState(174);
				match(T__25);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN =
		"\u0004\u0001#\u00b2\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002"+
		"\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004\u0002"+
		"\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007\u0007\u0007\u0002"+
		"\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b\u0007\u000b\u0002"+
		"\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e\u0002\u000f\u0007\u000f"+
		"\u0002\u0010\u0007\u0010\u0002\u0011\u0007\u0011\u0002\u0012\u0007\u0012"+
		"\u0002\u0013\u0007\u0013\u0002\u0014\u0007\u0014\u0002\u0015\u0007\u0015"+
		"\u0002\u0016\u0007\u0016\u0002\u0017\u0007\u0017\u0002\u0018\u0007\u0018"+
		"\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002"+
		"\u0004\u0002?\b\u0002\u000b\u0002\f\u0002@\u0001\u0003\u0001\u0003\u0001"+
		"\u0003\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0005\u0001\u0005\u0001"+
		"\u0005\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001"+
		"\u0006\u0001\u0006\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001"+
		"\b\u0004\bX\b\b\u000b\b\f\bY\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001"+
		"\t\u0003\tb\b\t\u0001\n\u0001\n\u0004\nf\b\n\u000b\n\f\ng\u0001\u000b"+
		"\u0001\u000b\u0001\u000b\u0003\u000bm\b\u000b\u0001\f\u0001\f\u0001\f"+
		"\u0001\f\u0001\f\u0001\r\u0001\r\u0001\r\u0001\r\u0001\r\u0001\u000e\u0001"+
		"\u000e\u0001\u000e\u0001\u000f\u0001\u000f\u0003\u000f~\b\u000f\u0001"+
		"\u0010\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0011\u0001"+
		"\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0012\u0001"+
		"\u0012\u0001\u0013\u0001\u0013\u0001\u0014\u0001\u0014\u0001\u0014\u0005"+
		"\u0014\u0092\b\u0014\n\u0014\f\u0014\u0095\t\u0014\u0001\u0015\u0001\u0015"+
		"\u0001\u0015\u0005\u0015\u009a\b\u0015\n\u0015\f\u0015\u009d\t\u0015\u0001"+
		"\u0016\u0001\u0016\u0001\u0016\u0003\u0016\u00a2\b\u0016\u0001\u0017\u0001"+
		"\u0017\u0001\u0017\u0001\u0017\u0001\u0017\u0003\u0017\u00a9\b\u0017\u0001"+
		"\u0018\u0001\u0018\u0001\u0018\u0001\u0018\u0001\u0018\u0003\u0018\u00b0"+
		"\b\u0018\u0001\u0018\u0000\u0000\u0019\u0000\u0002\u0004\u0006\b\n\f\u000e"+
		"\u0010\u0012\u0014\u0016\u0018\u001a\u001c\u001e \"$&(*,.0\u0000\u0000"+
		"\u00a8\u00002\u0001\u0000\u0000\u0000\u00027\u0001\u0000\u0000\u0000\u0004"+
		">\u0001\u0000\u0000\u0000\u0006B\u0001\u0000\u0000\u0000\bE\u0001\u0000"+
		"\u0000\u0000\nH\u0001\u0000\u0000\u0000\fK\u0001\u0000\u0000\u0000\u000e"+
		"R\u0001\u0000\u0000\u0000\u0010W\u0001\u0000\u0000\u0000\u0012[\u0001"+
		"\u0000\u0000\u0000\u0014e\u0001\u0000\u0000\u0000\u0016l\u0001\u0000\u0000"+
		"\u0000\u0018n\u0001\u0000\u0000\u0000\u001as\u0001\u0000\u0000\u0000\u001c"+
		"x\u0001\u0000\u0000\u0000\u001e}\u0001\u0000\u0000\u0000 \u007f\u0001"+
		"\u0000\u0000\u0000\"\u0084\u0001\u0000\u0000\u0000$\u008a\u0001\u0000"+
		"\u0000\u0000&\u008c\u0001\u0000\u0000\u0000(\u008e\u0001\u0000\u0000\u0000"+
		"*\u0096\u0001\u0000\u0000\u0000,\u00a1\u0001\u0000\u0000\u0000.\u00a8"+
		"\u0001\u0000\u0000\u00000\u00af\u0001\u0000\u0000\u000023\u0003\u0002"+
		"\u0001\u000034\u0003\u0004\u0002\u000045\u0003\u0010\b\u000056\u0005\u0000"+
		"\u0000\u00016\u0001\u0001\u0000\u0000\u000078\u0005\u0001\u0000\u0000"+
		"89\u0005\u001d\u0000\u00009\u0003\u0001\u0000\u0000\u0000:?\u0003\u0006"+
		"\u0003\u0000;?\u0003\b\u0004\u0000<?\u0003\n\u0005\u0000=?\u0003\f\u0006"+
		"\u0000>:\u0001\u0000\u0000\u0000>;\u0001\u0000\u0000\u0000><\u0001\u0000"+
		"\u0000\u0000>=\u0001\u0000\u0000\u0000?@\u0001\u0000\u0000\u0000@>\u0001"+
		"\u0000\u0000\u0000@A\u0001\u0000\u0000\u0000A\u0005\u0001\u0000\u0000"+
		"\u0000BC\u0005\u0002\u0000\u0000CD\u0003\u000e\u0007\u0000D\u0007\u0001"+
		"\u0000\u0000\u0000EF\u0005\u0003\u0000\u0000FG\u0003\u000e\u0007\u0000"+
		"G\t\u0001\u0000\u0000\u0000HI\u0005\u0004\u0000\u0000IJ\u0003\u000e\u0007"+
		"\u0000J\u000b\u0001\u0000\u0000\u0000KL\u0005\u0005\u0000\u0000LM\u0005"+
		"\u001d\u0000\u0000MN\u0005\u0006\u0000\u0000NO\u0005\u001c\u0000\u0000"+
		"OP\u0005\u0007\u0000\u0000PQ\u0005\u001c\u0000\u0000Q\r\u0001\u0000\u0000"+
		"\u0000RS\u0005\u001d\u0000\u0000ST\u0005\u0006\u0000\u0000TU\u0005\u001b"+
		"\u0000\u0000U\u000f\u0001\u0000\u0000\u0000VX\u0003\u0012\t\u0000WV\u0001"+
		"\u0000\u0000\u0000XY\u0001\u0000\u0000\u0000YW\u0001\u0000\u0000\u0000"+
		"YZ\u0001\u0000\u0000\u0000Z\u0011\u0001\u0000\u0000\u0000[\\\u0005\b\u0000"+
		"\u0000\\]\u0005\u001d\u0000\u0000]^\u0005\t\u0000\u0000^_\u0003\u0014"+
		"\n\u0000_a\u0005\n\u0000\u0000`b\u0003$\u0012\u0000a`\u0001\u0000\u0000"+
		"\u0000ab\u0001\u0000\u0000\u0000b\u0013\u0001\u0000\u0000\u0000cf\u0003"+
		"\u0016\u000b\u0000df\u0003\u001e\u000f\u0000ec\u0001\u0000\u0000\u0000"+
		"ed\u0001\u0000\u0000\u0000fg\u0001\u0000\u0000\u0000ge\u0001\u0000\u0000"+
		"\u0000gh\u0001\u0000\u0000\u0000h\u0015\u0001\u0000\u0000\u0000im\u0003"+
		"\u0018\f\u0000jm\u0003\u001a\r\u0000km\u0003\u001c\u000e\u0000li\u0001"+
		"\u0000\u0000\u0000lj\u0001\u0000\u0000\u0000lk\u0001\u0000\u0000\u0000"+
		"m\u0017\u0001\u0000\u0000\u0000no\u0005\u000b\u0000\u0000op\u0005\u001d"+
		"\u0000\u0000pq\u0005\f\u0000\u0000qr\u0005\u001e\u0000\u0000r\u0019\u0001"+
		"\u0000\u0000\u0000st\u0005\r\u0000\u0000tu\u0005 \u0000\u0000uv\u0005"+
		"\u000e\u0000\u0000vw\u0005\u001d\u0000\u0000w\u001b\u0001\u0000\u0000"+
		"\u0000xy\u0005\u000f\u0000\u0000yz\u0005\u001d\u0000\u0000z\u001d\u0001"+
		"\u0000\u0000\u0000{~\u0003 \u0010\u0000|~\u0003\"\u0011\u0000}{\u0001"+
		"\u0000\u0000\u0000}|\u0001\u0000\u0000\u0000~\u001f\u0001\u0000\u0000"+
		"\u0000\u007f\u0080\u0005\u0010\u0000\u0000\u0080\u0081\u0003&\u0013\u0000"+
		"\u0081\u0082\u0005\u0011\u0000\u0000\u0082\u0083\u0005\u001d\u0000\u0000"+
		"\u0083!\u0001\u0000\u0000\u0000\u0084\u0085\u0005\u0012\u0000\u0000\u0085"+
		"\u0086\u0005\u001c\u0000\u0000\u0086\u0087\u0005\u001f\u0000\u0000\u0087"+
		"\u0088\u0005\u0011\u0000\u0000\u0088\u0089\u0005\u001d\u0000\u0000\u0089"+
		"#\u0001\u0000\u0000\u0000\u008a\u008b\u0005\u0013\u0000\u0000\u008b%\u0001"+
		"\u0000\u0000\u0000\u008c\u008d\u0003(\u0014\u0000\u008d\'\u0001\u0000"+
		"\u0000\u0000\u008e\u0093\u0003*\u0015\u0000\u008f\u0090\u0005\u0014\u0000"+
		"\u0000\u0090\u0092\u0003*\u0015\u0000\u0091\u008f\u0001\u0000\u0000\u0000"+
		"\u0092\u0095\u0001\u0000\u0000\u0000\u0093\u0091\u0001\u0000\u0000\u0000"+
		"\u0093\u0094\u0001\u0000\u0000\u0000\u0094)\u0001\u0000\u0000\u0000\u0095"+
		"\u0093\u0001\u0000\u0000\u0000\u0096\u009b\u0003,\u0016\u0000\u0097\u0098"+
		"\u0005\u0015\u0000\u0000\u0098\u009a\u0003,\u0016\u0000\u0099\u0097\u0001"+
		"\u0000\u0000\u0000\u009a\u009d\u0001\u0000\u0000\u0000\u009b\u0099\u0001"+
		"\u0000\u0000\u0000\u009b\u009c\u0001\u0000\u0000\u0000\u009c+\u0001\u0000"+
		"\u0000\u0000\u009d\u009b\u0001\u0000\u0000\u0000\u009e\u009f\u0005\u0016"+
		"\u0000\u0000\u009f\u00a2\u0003,\u0016\u0000\u00a0\u00a2\u0003.\u0017\u0000"+
		"\u00a1\u009e\u0001\u0000\u0000\u0000\u00a1\u00a0\u0001\u0000\u0000\u0000"+
		"\u00a2-\u0001\u0000\u0000\u0000\u00a3\u00a9\u00030\u0018\u0000\u00a4\u00a5"+
		"\u0005\u0017\u0000\u0000\u00a5\u00a6\u0003&\u0013\u0000\u00a6\u00a7\u0005"+
		"\u0018\u0000\u0000\u00a7\u00a9\u0001\u0000\u0000\u0000\u00a8\u00a3\u0001"+
		"\u0000\u0000\u0000\u00a8\u00a4\u0001\u0000\u0000\u0000\u00a9/\u0001\u0000"+
		"\u0000\u0000\u00aa\u00ab\u0005\u001d\u0000\u0000\u00ab\u00ac\u0005\u0019"+
		"\u0000\u0000\u00ac\u00b0\u0005\u001e\u0000\u0000\u00ad\u00ae\u0005\u001d"+
		"\u0000\u0000\u00ae\u00b0\u0005\u001a\u0000\u0000\u00af\u00aa\u0001\u0000"+
		"\u0000\u0000\u00af\u00ad\u0001\u0000\u0000\u0000\u00b01\u0001\u0000\u0000"+
		"\u0000\r>@Yaegl}\u0093\u009b\u00a1\u00a8\u00af";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}