from pyArduinoML.methodchaining.AppBuilder import AppBuilder


def app():
    return AppBuilder("beeps") \
        .buzzer("bz").on_pin(8) \
        .state("short_beep").set("bz").shortBeep() \
            .after(800).go_to_state("long_beep") \
        .state("long_beep").set("bz").longBeep() \
            .after(1000).go_to_state("short_beep") \
        .initial("short_beep") \
        .get_contents()

if __name__ == '__main__':
    print(app())

