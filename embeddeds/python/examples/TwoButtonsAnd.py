from pyArduinoML.methodchaining.AppBuilder import AppBuilder
from pyArduinoML.model.SIGNAL import HIGH, LOW


def app():
    return AppBuilder("two_and") \
        .sensor("b1").on_pin(2) \
        .sensor("b2").on_pin(3) \
        .buzzer("bz").on_pin(8) \
        .state("idle") \
            .set("bz").to(LOW) \
            .whenBothHigh("b1", "b2").go_to_state("on") \
        .state("on") \
            .set("bz").to(HIGH) \
            .when("b1").becomes(LOW).go_to_state("idle") \
            .when("b2").becomes(LOW).go_to_state("idle") \
        .initial("idle") \
        .get_contents()

if __name__ == '__main__':
    print(app())

