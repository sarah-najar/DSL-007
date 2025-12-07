import io.github.mosser.arduinoml.externals.antlr.ArduinomlLexer;
import io.github.mosser.arduinoml.externals.antlr.ArduinomlParser;
import io.github.mosser.arduinoml.kernel.App;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import io.github.mosser.arduinoml.externals.antlr.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

    public static void main (String[] args) throws Exception {
        System.out.println("\n\nRunning the ANTLR compiler for ArduinoML");

        CharStream stream = getCharStream(args);
        App theApp = buildModel(stream);
        exportToCode(theApp);

    }

    private static CharStream getCharStream(String[] args) throws IOException {
        if (args.length < 1)
            throw new RuntimeException("no input file");
        Path input = Paths.get(new File(args[0]).toURI());
        System.out.println("Using input file: " + input);
        return CharStreams.fromPath(input);
    }

    private static App buildModel(CharStream stream) {
        ArduinomlLexer lexer   = new ArduinomlLexer(stream);
        lexer.removeErrorListeners();
        lexer.addErrorListener(new StopErrorListener());

        ArduinomlParser parser  = new ArduinomlParser(new CommonTokenStream(lexer));
        parser.removeErrorListeners();
        parser.addErrorListener(new StopErrorListener());

        ParseTreeWalker   walker  = new ParseTreeWalker();
        ModelBuilder      builder = new ModelBuilder();

        walker.walk(builder, parser.root()); // parser.root() is the entry point of the grammar

        return builder.retrieve();
    }

    private static void exportToCode(App theApp) {
        AntlrToWiring codeGenerator = new AntlrToWiring();
        theApp.accept(codeGenerator);
        System.out.println(codeGenerator.getResult());
    }

}
