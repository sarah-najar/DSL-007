import io.github.mosser.arduinoml.externals.antlr.ArduinomlBaseListener;
import io.github.mosser.arduinoml.externals.antlr.ArduinomlParser;

import io.github.mosser.arduinoml.kernel.App;
import io.github.mosser.arduinoml.kernel.behavioral.*;
import io.github.mosser.arduinoml.kernel.structural.*;

import java.util.HashMap;
import java.util.Map;

public class ModelBuilder extends ArduinomlBaseListener {

    private App theApp;
    private boolean built = false;

    public App retrieve() {
        if (!built) {
            throw new RuntimeException("Model not built yet!");
        }
        return theApp;
    }

    private Map<String, Sensor> sensors = new HashMap<>();
    private Map<String, Actuator> actuators = new HashMap<>();
    private Map<String, State> states = new HashMap<>();

    private State currentState = null;

    private State getOrCreateState(String name) {
        State state = states.get(name);
        if (state == null) {
            state = new State();
            state.setName(name);
            states.put(name, state);
        }
        return state;
    }

    @Override
    public void enterRoot(ArduinomlParser.RootContext ctx) {
        built = false;
        theApp = new App();
    }

    @Override
    public void exitRoot(ArduinomlParser.RootContext ctx) {
        built = true;
    }

    /* ---------- Application & bricks ---------- */

    @Override
    public void enterDeclaration(ArduinomlParser.DeclarationContext ctx) {
        theApp.setName(ctx.name.getText());
    }

    @Override
    public void enterSensor(ArduinomlParser.SensorContext ctx) {
        Sensor s = new Sensor();
        s.setName(ctx.pin().id.getText());
        s.setPin(Integer.parseInt(ctx.pin().port.getText()));
        sensors.put(s.getName(), s);
        theApp.getBricks().add(s);
    }

    @Override
    public void enterActuator(ArduinomlParser.ActuatorContext ctx) {
        Actuator a = new Actuator();
        a.setName(ctx.pin().id.getText());
        a.setPin(Integer.parseInt(ctx.pin().port.getText()));
        actuators.put(a.getName(), a);
        theApp.getBricks().add(a);
    }

    @Override
    public void enterBuzzer(ArduinomlParser.BuzzerContext ctx) {
        Buzzer b = new Buzzer();
        b.setName(ctx.pin().id.getText());
        b.setPin(Integer.parseInt(ctx.pin().port.getText()));
        actuators.put(b.getName(), b);
        theApp.getBricks().add(b);
    }

    @Override
    public void enterLcd(ArduinomlParser.LcdContext ctx) {
        LCDScreen lcd = new LCDScreen();
        lcd.setName(ctx.id.getText());
        lcd.setColumns(Integer.parseInt(ctx.columns.getText()));
        lcd.setRows(Integer.parseInt(ctx.rows.getText()));
        actuators.put(lcd.getName(), lcd);
        theApp.getBricks().add(lcd);
    }

    /* ---------- States ---------- */

    @Override
    public void enterState(ArduinomlParser.StateContext ctx) {
        String stateName = ctx.name.getText();
        currentState = getOrCreateState(stateName);
    }

    @Override
    public void exitState(ArduinomlParser.StateContext ctx) {
        theApp.getStates().add(currentState);
        currentState = null;
    }

    @Override
    public void enterInitial(ArduinomlParser.InitialContext ctx) {
        theApp.setInitial(currentState);
    }

    /* ---------- Actions ---------- */

    @Override
    public void enterActuatorAction(ArduinomlParser.ActuatorActionContext ctx) {
        ActuatorAction a = new ActuatorAction();
        a.setActuator(actuators.get(ctx.target.getText()));
        a.setValue(SIGNAL.valueOf(ctx.value.getText()));
        currentState.getActions().add(a);
    }

    @Override
    public void enterLcdAction(ArduinomlParser.LcdActionContext ctx) {
        LCDAction a = new LCDAction();
        String raw = ctx.message.getText();
        a.setMessage(raw.substring(1, raw.length() - 1));
        a.setActuator((LCDScreen) actuators.get(ctx.screen.getText()));
        currentState.getActions().add(a);
    }

    @Override
    public void enterBuzzerAction(ArduinomlParser.BuzzerActionContext ctx) {
        BuzzerAction a = new BuzzerAction();
        a.setActuator((Buzzer) actuators.get(ctx.target.getText()));
        currentState.getActions().add(a);
    }

    /* ---------- Transitions ---------- */

    @Override
    public void enterConditionalTransition(ArduinomlParser.ConditionalTransitionContext ctx) {
        ConditionalTransition t = new ConditionalTransition();
        t.setCondition(buildCondition(ctx.condition()));
        t.setNext(getOrCreateState(ctx.next.getText()));
        currentState.getTransitions().add(t);
    }

    @Override
    public void enterTemporalTransition(ArduinomlParser.TemporalTransitionContext ctx) {
        TemporalTransition t = new TemporalTransition();
        t.setDuration(Integer.parseInt(ctx.duration.getText()));
        t.setUnit(TimeUnit.valueOf(ctx.unit.getText()));
        t.setNext(getOrCreateState(ctx.next.getText()));
        currentState.getTransitions().add(t);
    }


    private Condition buildCondition(ArduinomlParser.ConditionContext ctx) {
        return buildOr(ctx.orCondition());
    }

    private Condition buildOr(ArduinomlParser.OrConditionContext ctx) {
        Condition left = buildAnd(ctx.andCondition(0));
        for (int i = 1; i < ctx.andCondition().size(); i++) {
            OrCondition or = new OrCondition();
            or.setLeft(left);
            or.setRight(buildAnd(ctx.andCondition(i)));
            left = or;
        }
        return left;
    }

    private Condition buildAnd(ArduinomlParser.AndConditionContext ctx) {
        Condition left = buildUnary(ctx.unaryCondition(0));
        for (int i = 1; i < ctx.unaryCondition().size(); i++) {
            AndCondition and = new AndCondition();
            and.setLeft(left);
            and.setRight(buildUnary(ctx.unaryCondition(i)));
            left = and;
        }
        return left;
    }

    private Condition buildUnary(ArduinomlParser.UnaryConditionContext ctx) {
        if (ctx.unaryCondition() != null) { // 'not' unaryCondition
            NotCondition not = new NotCondition();
            not.setCondition(buildUnary(ctx.unaryCondition()));
            return not;
        } else {
            return buildPrimary(ctx.primaryCondition());
        }
    }

    private Condition buildPrimary(ArduinomlParser.PrimaryConditionContext ctx) {
        if (ctx.sensorCondition() != null) {
            return buildSensorCondition(ctx.sensorCondition());
        } else {
            // '(' condition ')'
            return buildCondition(ctx.condition());
        }
    }

    private Condition buildSensorCondition(ArduinomlParser.SensorConditionContext ctx) {
        String sensorName = ctx.sensorId.getText();
        Sensor sensor = sensors.get(sensorName);

        if (ctx.value != null) {
            SensorLevelCondition c = new SensorLevelCondition();
            c.setSensor(sensor);
            c.setExpectedValue(SIGNAL.valueOf(ctx.value.getText()));
            return c;
        } else {
            PushEventCondition c = new PushEventCondition();
            c.setSensor(sensor);
            return c;
        }
    }

}
