package com.tsipl.king;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class King {
    static boolean hadError = false;

    public static void main(String[] args) throws IOException {
        if (args.length > 1) {
            // user has too many arguments
            System.out.println("Too many arguments: king [file name]");
            System.exit(64);
        } else if (args.length == 1) {
            // run king file
            prepareScript(args[0]);
        } else {
            // activate king repl
            runRepl();
        }
    }

    // interactive repl
    private static void runRepl() throws IOException {
        InputStreamReader inputStream = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(inputStream);

        System.out.println("Opening King REPL");
        for (;;) {
            System.out.print("> ");
            String text = reader.readLine();

            if (text == null) {
                break;
            }

            run(text);
            hadError = false;
        }
    }

    // parse script and execute code
    private static void prepareScript(String scriptPath) throws IOException {
        byte[] bytesFromScript = Files.readAllBytes(Paths.get(scriptPath));
        run(new String(bytesFromScript, Charset.defaultCharset()));

        if (hadError) {
            System.exit(65);
        }
    }

    // code runner
    private static void run(String source) {
        Scanner scanner = new Scanner(source);
        List<Token> tokens = scanner.scanTokens();

        for (Token token : tokens) {
            System.out.println(token);
        }
    }

    // error handling
    static void error(int lineNumber, String message) {
        report(lineNumber, "", message);
    }

    private static void report(int lineNumber, String location, String message) {
        System.err.println(
                "[line " + lineNumber + "] Error " + location + ": " + message);
        hadError = true;
    }
}