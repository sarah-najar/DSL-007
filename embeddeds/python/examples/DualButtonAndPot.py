from pyArduinoML.methodchaining.AppBuilder import AppBuilder
from pyArduinoML.model.SIGNAL import HIGH, LOW


def app():
    TH = 512
    return AppBuilder("dual") \
        .sensor("b1").on_pin(12) \
        .potentiometer("pot").on(0) \
        .buzzer("bz").on_pin(8) \
        .state("idle").set("bz").to(LOW) \
            .whenButtonAndPotAtLeast("b1", "pot", TH).go_to_state("alarm") \
        .state("alarm").set("bz").to(HIGH) \
            .whenEitherButtonLowOrPotBelow("b1", "pot", TH).go_to_state("idle") \
        .initial("idle") \
        .get_contents()

if __name__ == '__main__':
    print(app())

