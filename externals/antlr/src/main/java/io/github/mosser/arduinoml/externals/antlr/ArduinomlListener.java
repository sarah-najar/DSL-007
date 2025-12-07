package io.github.mosser.arduinoml.externals.antlr;// Generated from Arduinoml.g4 by ANTLR 4.13.1
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link ArduinomlParser}.
 */
public interface ArduinomlListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link ArduinomlParser#root}.
	 * @param ctx the parse tree
	 */
	void enterRoot(ArduinomlParser.RootContext ctx);
	/**
	 * Exit a parse tree produced by {@link ArduinomlParser#root}.
	 * @param ctx the parse tree
	 */
	void exitRoot(ArduinomlParser.RootContext ctx);
	/**
	 * Enter a parse tree produced by {@link ArduinomlParser#declaration}.
	 * @param ctx the parse tree
	 */
	void enterDeclaration(ArduinomlParser.DeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link ArduinomlParser#declaration}.
	 * @param ctx the parse tree
	 */
	void exitDeclaration(ArduinomlParser.DeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link ArduinomlParser#bricks}.
	 * @param ctx the parse tree
	 */
	void enterBricks(ArduinomlParser.BricksContext ctx);
	/**
	 * Exit a parse tree produced by {@link ArduinomlParser#bricks}.
	 * @param ctx the parse tree
	 */
	void exitBricks(ArduinomlParser.BricksContext ctx);
	/**
	 * Enter a parse tree produced by {@link ArduinomlParser#sensor}.
	 * @param ctx the parse tree
	 */
	void enterSensor(ArduinomlParser.SensorContext ctx);
	/**
	 * Exit a parse tree produced by {@link ArduinomlParser#sensor}.
	 * @param ctx the parse tree
	 */
	void exitSensor(ArduinomlParser.SensorContext ctx);
	/**
	 * Enter a parse tree produced by {@link ArduinomlParser#actuator}.
	 * @param ctx the parse tree
	 */
	void enterActuator(ArduinomlParser.ActuatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link ArduinomlParser#actuator}.
	 * @param ctx the parse tree
	 */
	void exitActuator(ArduinomlParser.ActuatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link ArduinomlParser#buzzer}.
	 * @param ctx the parse tree
	 */
	void enterBuzzer(ArduinomlParser.BuzzerContext ctx);
	/**
	 * Exit a parse tree produced by {@link ArduinomlParser#buzzer}.
	 * @param ctx the parse tree
	 */
	void exitBuzzer(ArduinomlParser.BuzzerContext ctx);
	/**
	 * Enter a parse tree produced by {@link ArduinomlParser#lcd}.
	 * @param ctx the parse tree
	 */
	void enterLcd(ArduinomlParser.LcdContext ctx);
	/**
	 * Exit a parse tree produced by {@link ArduinomlParser#lcd}.
	 * @param ctx the parse tree
	 */
	void exitLcd(ArduinomlParser.LcdContext ctx);
	/**
	 * Enter a parse tree produced by {@link ArduinomlParser#pin}.
	 * @param ctx the parse tree
	 */
	void enterPin(ArduinomlParser.PinContext ctx);
	/**
	 * Exit a parse tree produced by {@link ArduinomlParser#pin}.
	 * @param ctx the parse tree
	 */
	void exitPin(ArduinomlParser.PinContext ctx);
	/**
	 * Enter a parse tree produced by {@link ArduinomlParser#states}.
	 * @param ctx the parse tree
	 */
	void enterStates(ArduinomlParser.StatesContext ctx);
	/**
	 * Exit a parse tree produced by {@link ArduinomlParser#states}.
	 * @param ctx the parse tree
	 */
	void exitStates(ArduinomlParser.StatesContext ctx);
	/**
	 * Enter a parse tree produced by {@link ArduinomlParser#state}.
	 * @param ctx the parse tree
	 */
	void enterState(ArduinomlParser.StateContext ctx);
	/**
	 * Exit a parse tree produced by {@link ArduinomlParser#state}.
	 * @param ctx the parse tree
	 */
	void exitState(ArduinomlParser.StateContext ctx);
	/**
	 * Enter a parse tree produced by {@link ArduinomlParser#stateBlock}.
	 * @param ctx the parse tree
	 */
	void enterStateBlock(ArduinomlParser.StateBlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link ArduinomlParser#stateBlock}.
	 * @param ctx the parse tree
	 */
	void exitStateBlock(ArduinomlParser.StateBlockContext ctx);
	/**
	 * Enter a parse tree produced by {@link ArduinomlParser#action}.
	 * @param ctx the parse tree
	 */
	void enterAction(ArduinomlParser.ActionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ArduinomlParser#action}.
	 * @param ctx the parse tree
	 */
	void exitAction(ArduinomlParser.ActionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ArduinomlParser#actuatorAction}.
	 * @param ctx the parse tree
	 */
	void enterActuatorAction(ArduinomlParser.ActuatorActionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ArduinomlParser#actuatorAction}.
	 * @param ctx the parse tree
	 */
	void exitActuatorAction(ArduinomlParser.ActuatorActionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ArduinomlParser#lcdAction}.
	 * @param ctx the parse tree
	 */
	void enterLcdAction(ArduinomlParser.LcdActionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ArduinomlParser#lcdAction}.
	 * @param ctx the parse tree
	 */
	void exitLcdAction(ArduinomlParser.LcdActionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ArduinomlParser#buzzerAction}.
	 * @param ctx the parse tree
	 */
	void enterBuzzerAction(ArduinomlParser.BuzzerActionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ArduinomlParser#buzzerAction}.
	 * @param ctx the parse tree
	 */
	void exitBuzzerAction(ArduinomlParser.BuzzerActionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ArduinomlParser#transition}.
	 * @param ctx the parse tree
	 */
	void enterTransition(ArduinomlParser.TransitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ArduinomlParser#transition}.
	 * @param ctx the parse tree
	 */
	void exitTransition(ArduinomlParser.TransitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ArduinomlParser#conditionalTransition}.
	 * @param ctx the parse tree
	 */
	void enterConditionalTransition(ArduinomlParser.ConditionalTransitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ArduinomlParser#conditionalTransition}.
	 * @param ctx the parse tree
	 */
	void exitConditionalTransition(ArduinomlParser.ConditionalTransitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ArduinomlParser#temporalTransition}.
	 * @param ctx the parse tree
	 */
	void enterTemporalTransition(ArduinomlParser.TemporalTransitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ArduinomlParser#temporalTransition}.
	 * @param ctx the parse tree
	 */
	void exitTemporalTransition(ArduinomlParser.TemporalTransitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ArduinomlParser#initial}.
	 * @param ctx the parse tree
	 */
	void enterInitial(ArduinomlParser.InitialContext ctx);
	/**
	 * Exit a parse tree produced by {@link ArduinomlParser#initial}.
	 * @param ctx the parse tree
	 */
	void exitInitial(ArduinomlParser.InitialContext ctx);
	/**
	 * Enter a parse tree produced by {@link ArduinomlParser#condition}.
	 * @param ctx the parse tree
	 */
	void enterCondition(ArduinomlParser.ConditionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ArduinomlParser#condition}.
	 * @param ctx the parse tree
	 */
	void exitCondition(ArduinomlParser.ConditionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ArduinomlParser#orCondition}.
	 * @param ctx the parse tree
	 */
	void enterOrCondition(ArduinomlParser.OrConditionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ArduinomlParser#orCondition}.
	 * @param ctx the parse tree
	 */
	void exitOrCondition(ArduinomlParser.OrConditionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ArduinomlParser#andCondition}.
	 * @param ctx the parse tree
	 */
	void enterAndCondition(ArduinomlParser.AndConditionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ArduinomlParser#andCondition}.
	 * @param ctx the parse tree
	 */
	void exitAndCondition(ArduinomlParser.AndConditionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ArduinomlParser#unaryCondition}.
	 * @param ctx the parse tree
	 */
	void enterUnaryCondition(ArduinomlParser.UnaryConditionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ArduinomlParser#unaryCondition}.
	 * @param ctx the parse tree
	 */
	void exitUnaryCondition(ArduinomlParser.UnaryConditionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ArduinomlParser#primaryCondition}.
	 * @param ctx the parse tree
	 */
	void enterPrimaryCondition(ArduinomlParser.PrimaryConditionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ArduinomlParser#primaryCondition}.
	 * @param ctx the parse tree
	 */
	void exitPrimaryCondition(ArduinomlParser.PrimaryConditionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ArduinomlParser#sensorCondition}.
	 * @param ctx the parse tree
	 */
	void enterSensorCondition(ArduinomlParser.SensorConditionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ArduinomlParser#sensorCondition}.
	 * @param ctx the parse tree
	 */
	void exitSensorCondition(ArduinomlParser.SensorConditionContext ctx);
}