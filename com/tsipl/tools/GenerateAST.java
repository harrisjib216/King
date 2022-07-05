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

        // set up the file
        writer.println("package com.tsipl.king;");
        writer.println();
        writer.println("import java.util.List;");
        writer.println();
        writer.println("abstract class " + baseName + " {");

        // set up accepting visitor patter
        defineVisitor(writer, baseName, types);

        // define the expression classes
        for (String type : types) {
            String className = type.split(":")[0].trim();
            String fields = type.split(":")[1].trim();
            defineType(writer, baseName, className, fields);
        }

        // base accept method
        writer.println();
        writer.println("\tabstract <R> R accept(Visitor<R> visitor);");

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

        // visitor pattern definition
        writer.println("\t@Override");
        writer.println("\t<R> R accept(Visitor<R> visitor) {");
        writer.println("\t\treturn visitor.visit" + className + baseName + "(this);");
        writer.println("}");

        // fields
        writer.println();
        for (String field : fields) {
            writer.println("\t\tfinal " + field + ";");
        }

        // close constructor
        writer.println("\t}\n");
    }

    // iterate thru all subclasses and declare visit method
    private static void defineVisitor(PrintWriter writer, String baseName, List<String> types) {
        writer.println("\tinterface Visitor<R> {");

        for (String type : types) {
            String typeName = type.split(":")[0].trim();
            writer.println("\t\tR visit" + typeName + baseName + "(" + typeName + " " + baseName.toLowerCase() + ");");
        }

        writer.println("\t}");
    }
}
