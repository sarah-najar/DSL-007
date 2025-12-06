# GroovuinoML — Bilan par rapport au sujet DSL

Ce document synthétise ce qui a été réalisé dans `embeddeds/groovy/GroovuinoML` au regard des attendus de `docs/DSL_LAB.md` et du modèle de domaine `docs/DSL_MDL.png`. Il décrit aussi rapidement la DSL côté Groovy et renvoie vers les exemples fournis.

## Correspondance avec les attendus du projet

- Abstraction (AS) et modèle de domaine
  - L’AS est portée par le kernel JVM ArduinoML (package `io.github.mosser.arduinoml.kernel.*`). Le module Groovy construit directement les objets du kernel via `GroovuinoMLModel` et les lie dans l’environnement Groovy.
  - Fichiers clés:
    - `embeddeds/groovy/GroovuinoML/src/main/groovy/groovuinoml/dsl/GroovuinoMLModel.java`
    - `embeddeds/groovy/GroovuinoML/src/main/groovy/groovuinoml/dsl/GroovuinoMLBasescript.groovy`
    - `embeddeds/groovy/GroovuinoML/src/main/groovy/groovuinoml/dsl/GroovuinoMLDSL.groovy`

- Syntaxe concrète (DSL embarquée)
  - DSL fluide de type Groovy fournissant: déclaration de briques (`sensor`, `actuator`, `buzzer`, `lcd`, `potentiometer`), états/effets (`state ... means ... and ...`), transitions (`from ... to ... when ...` et `after ...`).
  - Exemples représentatifs dans `embeddeds/groovy/GroovuinoML/scripts` (voir la section « Scénarios » ci‑dessous).

- Validation
  - Le kernel assure des vérifications à la génération (p. ex. cohérence des actions, taille des messages LCD). Il n’y a pas de module de validation dédié côté Groovy, mais les constructions exposées empêchent plusieurs incohérences (ex.: `display` uniquement sur `LCD`, `shortBeep/longBeep` uniquement sur `Buzzer`).

- Génération de code
  - Implémentée via le visiteur `ToWiring` du kernel. L’appel `export "appname"` génère un sketch Arduino prêt à coller dans l’IDE (voir `GroovuinoMLModel.generateCode`).

## Couverture des scénarios de base (DSL_LAB.md)

- 1) Alarme simple (bouton active LED + buzzer; relâchement = OFF)
  - Etat actuel: partiellement couvert. `scripts/Alarm.groovy` active LED + beep court et revient à l’idle après 500 ms (tempo), pas sur relâchement; et le buzzer n’est pas maintenu HIGH.
  - Faisabilité DSL: oui. On peut écrire: `state "alarm" means "led" becomes high and "bz" becomes high` puis `from "alarm" to "idle" when "button" becomes low`.

- 2) Double appui synchronisé (buzzer uniquement si deux boutons HIGH; relâchement d’un seul = OFF)
  - Etat actuel: partiellement couvert. `scripts/TwoButtonsAnd.groovy` illustre la conjonction de deux boutons mais agit sur une LED (pas de buzzer).
  - Faisabilité DSL: oui, remplacer l’action par `"bz" becomes high` et garder les retours sur `low`.

- 3) Alarme à états (un push = ON, push suivant = OFF)
  - Couvert: `scripts/StateSwitch.groovy` et `scripts/Switch.groovy` montrent le toggle par appuis successifs.

- 4) Multi‑états (Buzz → Light → Quiet en boucle)
  - Couvert: `scripts/MultiState.groovy`.

Conclusion: la DSL permet d’exprimer les 4 scénarios. Deux exemples fournis (1 et 2) ne correspondent pas mot‑à‑mot aux énoncés, mais une variante trivialement équivalente est directement exprimable.

## Extensions implémentées (DSL_LAB.md)

- Transitions temporelles
  - Implémenté: `after 500` et `after 1, s` via `TemporalTransition` (`TimeUnit` injectés comme variables `ms`, `s`, …).
  - Exemples: `scripts/TimerBlink.groovy`, `scripts/TemporalAfterPush.groovy`.

- Support de l’écran LCD
  - Implémenté: `lcd "screen" size 16, 2` et action `display "..."` (validation de longueur côté kernel).
  - Exemple: `scripts/LCD_OK.groovy`.

- Gestion de briques analogiques
  - Implémenté: `potentiometer ... on <Aindex>` (A0..A5 → 0..5) et conditions de seuil `whenPotAtLeast/whenPotBelow` (classe `AnalogThresholdCondition`).
  - Exemple: `scripts/PotOnly.groovy`, `scripts/DualButtonAndPot.groovy`.

- Usage non‑bruyant du buzzer (signaux courts/longs)
  - Implémenté: actions `shortBeep` et `longBeep` sur les états; permet de composer des motifs (plusieurs `and`).
  - Exemple: `scripts/Beeps.groovy`.

- Extensions non implémentées
  - Communication distante (Serial), Régions parallèles périodiques, Mealy (actions sur transitions), Mélodie complète.

## Alignement avec le modèle de domaine (DSL_MDL.png)

- Correspondances OK
  - Actions d’entrée d’état: `ActuatorAction` (HIGH/LOW), `BuzzerAction`, `LCDAction`.
  - Transitions: `TemporalTransition`, `ConditionalTransition` avec `AndCondition`/`OrCondition` et `SensorLevelCondition`.
  - Briques: `Sensor`, `Actuator` (dont `Buzzer`, `LCDScreen`).

- Écarts identifiés
  - Le diagramme montre `PushEventCondition`, alors que l’implémentation DSL crée aujourd’hui des `SensorLevelCondition` (sémantique « devient HIGH/LOW » gérée au niveau génération/exécution). Fonctionnellement, l’intention côté DSL est respectée.
  - Le module Groovy introduit l’analogique (`AnalogSensor`, `AnalogThresholdCondition`) qui n’apparaît pas dans l’image `DSL_MDL.png`. Si l’extension analogique est au périmètre, le diagramme devrait être mis à jour pour refléter ces classes.

Bilan: l’implémentation Groovy respecte le cœur du modèle de domaine présenté; elle l’étend pour l’analogique et elle matérialise l’événement « becomes » via des conditions de niveau.

## Aperçu de la DSL (Groovy)

- Déclaration des briques
  - `sensor "b1" pin 2`
  - `actuator "led" pin 12`
  - `buzzer "bz" pin 8`
  - `lcd "screen" size 16, 2`
  - `potentiometer "pot" on 0`  (A0 → 0)

- Etats et actions
  - `state "idle" means "led" becomes low`
  - Chaînage: `state "alarm" means "led" becomes high and "bz" shortBeep`
  - Buzzer: `shortBeep`, `longBeep` — ou niveau digital: `"bz" becomes high/low`
  - LCD: `"screen" display "HELLO"`

- Transitions
  - Evénement: `from "idle" to "on" when "b1" becomes high`
  - Temporel: `from "on" to "idle" after 800` ou `after 1, s`
  - Composées: `whenBothHigh`, `whenPotAtLeast`, `whenPotBelow`, `whenButtonAndPotAtLeast`, `whenEitherButtonLowOrPotBelow`

- Export
  - `export "appname"` → imprime le sketch Wiring/INO (redirigeable vers un fichier).

## Construction et exécution

- Prérequis: JDK 8+, Maven 3+
- Construire le kernel et le module Groovy (fat‑jar): voir `embeddeds/groovy/GroovuinoML/README.md`.
- Lancer un script d’exemple, p. ex. `scripts/TwoButtonsAnd.groovy`.

## Recommandations mineures

- Ajouter un exemple « strictement conforme » pour les scénarios 1 et 2 (avec relâchement et buzzer maintenu HIGH) afin d’éviter toute ambiguïté.
- Si l’extension analogique est maintenue, mettre à jour `docs/DSL_MDL.png` pour inclure `AnalogSensor` et `AnalogThresholdCondition`.

---
Références utiles:
- DSL: `embeddeds/groovy/GroovuinoML/src/main/groovy/groovuinoml/dsl/GroovuinoMLBasescript.groovy`
- Modèle DSL: `embeddeds/groovy/GroovuinoML/src/main/groovy/groovuinoml/dsl/GroovuinoMLModel.java`
- Scripts: `embeddeds/groovy/GroovuinoML/scripts`
- Sujet: `docs/DSL_LAB.md`
- Modèle de domaine: `docs/DSL_MDL.png`
