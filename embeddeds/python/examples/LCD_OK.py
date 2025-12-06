from pyArduinoML.methodchaining.AppBuilder import AppBuilder


def app():
    return AppBuilder("lcd_ok") \
        .lcd("screen").size(16, 2) \
        .state("show").set("screen").display("HELLO") \
        .initial("show") \
        .get_contents()

if __name__ == '__main__':
    print(app())

