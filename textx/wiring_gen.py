from pathlib import Path
from textx import metamodel_from_file


def load_model(model_path: str):
    base = Path(__file__).parent
    mm = metamodel_from_file(base / "arduinoml.tx", auto_init_attributes=True)
    model = mm.model_from_file(model_path)
    validate_model(model)
    return model


def validate_model(model):
    brick_names = set()
    lcd_present = any(b.__class__.__name__ == "LCD" for b in model.bricks)
    for b in model.bricks:
        if b.name in brick_names:
            raise ValueError(f"Duplicate brick name: {b.name}")
        brick_names.add(b.name)
        if b.__class__.__name__ == "Potentiometer":
            if b.analog < 0 or b.analog > 5:
                raise ValueError(f"Illegal analog pin A{b.analog} for {b.name}")
            continue
        if b.__class__.__name__ == "LCD":
            continue  # LCD has no direct pin mapping here
        allowed_all = {9, 10, 11, 12}
        forbidden_bus = set()
        if lcd_present:
            # BUS1 mapping: rs=2, rw=3, en=4, d4=5, d5=6, d6=7, d7=8
            forbidden_bus = {2, 3, 4, 5, 6, 7, 8}
        if getattr(b, "pin", 0) in forbidden_bus:
            raise ValueError(f"Pin {b.pin} is reserved for LCD bus; pick from {sorted(allowed_all - forbidden_bus)} for {b.name}")
        if getattr(b, "pin", 0) not in allowed_all - forbidden_bus:
            raise ValueError(f"Illegal pin {b.pin} for {b.name} (allowed: {sorted(allowed_all - forbidden_bus)})")
    state_names = set()
    initials = []
    for s in model.states:
        if s.name in state_names:
            raise ValueError(f"Duplicate state name: {s.name}")
        state_names.add(s.name)
        if getattr(s, "initial", False):
            initials.append(s)
        for action in getattr(s, "actions", []):
            if action.__class__.__name__ == "LCDAction":
                msg = strip_quotes(action.msg)
                lcd = action.target
                capacity = lcd.cols * lcd.rows
                if len(msg) > capacity:
                    raise ValueError(f"LCD message too long for {lcd.name}: {len(msg)}/{capacity}")
    if len(initials) != 1:
        raise ValueError("Exactly one initial state is required")


def to_wiring(model) -> str:
    app_name = model.decl.name
    sensors = [b for b in model.bricks if b.__class__.__name__ == "Sensor"]
    pots = [b for b in model.bricks if b.__class__.__name__ == "Potentiometer"]
    actuators = [b for b in model.bricks if b.__class__.__name__ in {"Actuator", "Buzzer"}]
    lcds = [b for b in model.bricks if b.__class__.__name__ == "LCD"]
    initial_state = next(s for s in model.states if s.initial is not None)

    lines = []
    emit = lambda l, i=0: lines.append("\t" * i + l)

    emit("// Wiring code generated from a textX ArduinoML model")
    emit(f"// Application name: {app_name}")
    emit("long debounce = 200;")
    emit("")
    if lcds:
        emit("#include <LiquidCrystal.h>")
        emit("// LCD BUS1 mapping: rs=2, rw=3, en=4, d4=5, d5=6, d6=7, d7=8")
        emit("LiquidCrystal lcd(2, 3, 4, 5, 6, 7, 8);")
        emit("")

    emit("enum STATE { " + ", ".join(s.name for s in model.states) + " };")
    emit(f"STATE currentState = {initial_state.name};")
    for s in sensors:
        emit(f"boolean {s.name}BounceGuard = false;")
        emit(f"long {s.name}LastDebounceTime = 0;")
    emit("")

    emit("void setup(){")
    for s in sensors:
        emit(f"pinMode({s.pin}, INPUT);  // {s.name}", 1)
    for p in pots:
        emit(f"// {p.name} uses analogRead on A{p.analog}", 1)
    for a in actuators:
        emit(f"pinMode({a.pin}, OUTPUT); // {a.name}", 1)
    for lcd in lcds:
        emit(f"// {lcd.name} [{lcd.cols}x{lcd.rows}] LCD on BUS1", 1)
        emit("pinMode(3, OUTPUT); // RW", 1)
        emit("digitalWrite(3, LOW); // tie RW to GND", 1)
    if lcds:
        first = lcds[0]
        emit(f"lcd.begin({first.cols}, {first.rows});", 1)
        emit("delay(50);", 1)
    emit("}")
    emit("")

    emit("void loop() {")
    emit("switch(currentState){", 1)
    for st in model.states:
        emit(f"case {st.name}:", 2)
        for s in sensors:
            emit(f"{s.name}BounceGuard = millis() - {s.name}LastDebounceTime > debounce;", 3)
        for act in getattr(st, "actions", []):
            emit_action(act, emit, 3)
        for tr in getattr(st, "transitions", []):
            emit_transition(tr, emit, 3)
        emit("break;", 3)
    emit("}", 1)
    emit("}")

    return "\n".join(lines)


def emit_action(action, emit, indent):
    kind = action.__class__.__name__
    if kind == "SetAction":
        emit(f"digitalWrite({action.target.pin},{action.value});", indent)
    elif kind == "BeepAction":
        length = 200 if action.kind in {"shortBeep", "sound"} else 1000
        emit(f"digitalWrite({action.target.pin}, HIGH);", indent)
        emit(f"delay({length});", indent)
        emit(f"digitalWrite({action.target.pin}, LOW);", indent)
    elif kind == "LCDAction":
        msg = strip_quotes(action.msg)
        pad = msg.ljust(16)[:16]
        emit("lcd.setCursor(0,0);", indent)
        emit(f'lcd.print("{pad}");', indent)


def emit_transition(tr, emit, indent):
    kind = tr.__class__.__name__
    if kind == "ConditionalTransition":
        expr = condition_to_expr(tr.cond)
        emit(f"if({expr})"+" {", indent)
        for name in sorted(collect_sensors(tr.cond)):
            emit(f"{name}LastDebounceTime = millis();", indent + 1)
        emit(f"currentState = {tr.next.name};", indent + 1)
        emit("}", indent)
    elif kind == "TemporalTransition":
        ms = tr.duration if tr.unit == "MS" else tr.duration * 1000
        emit(f"delay({ms});", indent)
        emit(f"currentState = {tr.next.name};", indent)


def collect_sensors(cond):
    names = set()
    if cond is None:
        return names
    kind = cond.__class__.__name__
    if kind in {"SensorLevel", "PushEvent"}:
        names.add(cond.sensor.name)
    elif kind == "AnalogThreshold":
        names.add(cond.sensor.name)
    elif kind == "AndCond":
        parts = [cond.left] + list(getattr(cond, "ands", []))
        for p in parts:
            names |= collect_sensors(p)
    elif kind == "OrCond":
        parts = [cond.left] + list(getattr(cond, "ors", []))
        for p in parts:
            names |= collect_sensors(p)
    elif kind == "UnaryCond":
        names |= collect_sensors(getattr(cond, "operand", None) or getattr(cond, "primary", None))
    elif kind == "ParenCond":
        names |= collect_sensors(cond.cond)
    return names


def condition_to_expr(cond):
    kind = cond.__class__.__name__
    if kind == "SensorLevel":
        return f"(digitalRead({cond.sensor.pin}) == {cond.value} && {cond.sensor.name}BounceGuard)"
    if kind == "PushEvent":
        return f"(digitalRead({cond.sensor.pin}) == HIGH && {cond.sensor.name}BounceGuard)"
    if kind == "AnalogThreshold":
        pin = f"A{cond.sensor.analog}"
        return f"(analogRead({pin}) {cond.op} {cond.threshold})"
    if kind == "AndCond":
        parts = [cond.left] + list(getattr(cond, "ands", []))
        return " && ".join(f"({condition_to_expr(p)})" for p in parts)
    if kind == "OrCond":
        parts = [cond.left] + list(getattr(cond, "ors", []))
        return " || ".join(f"({condition_to_expr(p)})" for p in parts)
    if kind == "UnaryCond":
        inner = getattr(cond, "operand", None) or getattr(cond, "primary", None)
        # operand set when 'not' is used; primary otherwise
        if getattr(cond, "operand", None):
            return f"!({condition_to_expr(inner)})"
        return condition_to_expr(inner)
    if kind == "ParenCond":
        return condition_to_expr(cond.cond)
    return "true"


def strip_quotes(txt):
    if txt is None:
        return ""
    if txt.startswith('"') and txt.endswith('"'):
        return txt[1:-1]
    return txt
