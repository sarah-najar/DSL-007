grammar Arduinoml;

/******************
 ** Parser rules **
 ******************/

// Point d'entrée :
// 1-on commence par declarer l'application
// 2-les composants(bricks)
// 3-les etats
// 4-indication de la fin d'une sequence EOF
root
    : declaration bricks states EOF
    ;

// application myApp
declaration
    : 'application' name=IDENTIFIER
    ;

// Déclarations matérielles
bricks
    : (sensor | actuator | buzzer | lcd)+
    ;

sensor
    : 'sensor' pin
    ;

actuator
    : 'actuator' pin
    ;

buzzer
    : 'buzzer' pin
    ;

// lcd screen : On a un nombre defini de colonnes et de lignes pour l'ecran
lcd
    : 'lcd' id=IDENTIFIER ':' columns=INT 'x' rows=INT
    ;

// nom + broche
pin
    : id=IDENTIFIER ':' port=PORT_NUMBER
    ;


/***************
 **   States  **
 ***************/

states
    : state+
    ;

// state idle { ... } initial
state
    : 'state' name=IDENTIFIER '{' stateBlock '}' initial?
    ;

// contenu d'un état = actions + transitions
stateBlock
    : (action | transition)+
    ;

// ----- Actions -----

action
    : actuatorAction
    | lcdAction
    | buzzerAction
    ;

// set led to HIGH
actuatorAction
    : 'set' target=IDENTIFIER 'to' value=SIGNAL
    ;

// display "HELLO" on screen
lcdAction
    : 'display' message=STRING 'on' screen=IDENTIFIER
    ;

// sound buzz
buzzerAction
    : 'sound' target=IDENTIFIER
    ;

// ----- Transitions -----

transition
    : conditionalTransition
    | temporalTransition
    ;

// when <condition> goto stateX
conditionalTransition
    : 'when' condition 'goto' next=IDENTIFIER
    ;

// after 5 S goto stateY
temporalTransition
    : 'after' duration=INT unit=TIMEUNIT 'goto' next=IDENTIFIER
    ;

// initial
initial
    : 'initial'
    ;


/*****************
 **  Conditions **
 *****************/

// condition booléenne avec priorité OR > AND > NOT

condition
    : orCondition
    ;

orCondition
    : andCondition ('or' andCondition)*
    ;

andCondition
    : unaryCondition ('and' unaryCondition)*
    ;

unaryCondition
    : 'not' unaryCondition
    | primaryCondition
    ;

primaryCondition
    : sensorCondition
    | '(' condition ')'
    ;

// condition atomique sur un capteur
sensorCondition
    : sensorId=IDENTIFIER 'is' value=SIGNAL      // SensorLevelCondition
    | sensorId=IDENTIFIER 'pushed'              // PushEventCondition
    ;


/******************
 ** Lexer rules  **
 ******************/

// pins possibles = 1..9, 11, 12
PORT_NUMBER
    : [1-9] | '11' | '12'
    ;

// entier générique
INT
    : [0-9]+
    ;

// identifiant (minuscule initiale)
IDENTIFIER
    : LOWERCASE (LOWERCASE | UPPERCASE | DIGIT)*
    ;

// signaux
SIGNAL
    : 'HIGH'
    | 'LOW'
    ;

// unités de temps
TIMEUNIT
    : 'MS'
    | 'S'
    ;

// "message LCD"
STRING
    : '"' (~["\r\n])* '"'
    ;


/*************
 ** Helpers **
 *************/

fragment LOWERCASE : [a-z];
fragment UPPERCASE : [A-Z];
fragment DIGIT     : [0-9];

NEWLINE
    : ('\r'? '\n' | '\r')+ -> skip
    ;

WS
    : (' ' | '\t')+ -> skip
    ;

COMMENT
    : '#' ~('\r' | '\n')* -> skip
    ;
