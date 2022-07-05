package com.tsipl.tools;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

public class GenerateAST {
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.err.println("Usage: generate_ast <output directory>");
            System.exit(64);
        }
        String outputDir = args[0];

        defineAST(outputDir, "Expression", Arrays.asList(
                "Binary : Expression left, Token operator, Expression right",
                "Grouping : Expression expression",
                "Literal : Object value",
                "Unary : Token operator, Expression right"));
    }

    private static void defineAST(String outputDir, String baseName, List<String> types) throws IOException {
        String file = outputDir + "/" + baseName + ".java";

        PrintWriter writer = new PrintWriter(file, "UTF-8");

        writer.println("package com.tsipl.king;");
        writer.println();
        writer.println("import java.util.List;");
        writer.println();
        writer.println("abstract class " + baseName + " {");

        for (String type : types) {
            String className = type.split(":")[0].trim();
            String fields = type.split(":")[1].trim();
            defineType(writer, baseName, className, fields);
        }

        writer.println("}");
        writer.close();
    }

    private static void defineType(PrintWriter writer, String baseName, String className, String fieldList) {
        writer.println("\tstatic class " + className + " extends " + baseName + " {");

        // constructor
        writer.println("\t\t" + className + "(" + fieldList + ") {");

        // create field for each parameter
        String[] fields = fieldList.split(", ");
        for (String field : fields) {
            String name = field.split(" ")[1];
            writer.println("\t\t\tthis." + name + " = " + name + ";");
        }
        writer.println("\t\t}");

        // fields
        writer.println();
        for (String field : fields) {
            writer.println("\t\tfinal " + field + ";");
        }

        // close constructor
        writer.println("\t}\n");
    }
}
