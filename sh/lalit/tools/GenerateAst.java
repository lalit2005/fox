package sh.lalit.tools;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

public class GenerateAst {
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.err.println("Usage: generate_ast <output dir>");
            System.exit(64);
        }
        String outDir = args[0];
        defineAst(
                outDir,
                "Expr",
                Arrays.asList(
                        "Assign   : Token name, Expr value",
                        "Binary   : Expr left, Token operator, Expr right",
                        "Call     : Expr callee, Token paren, List<Expr> arguments",
                        "Get      : Expr object, Token name",
                        "Set      : Expr object, Token name, Expr value", // name is the field name in the instance
                        "Super    : Token keyword, Token method",
                        "This     : Token keyword",
                        "Grouping : Expr expression",
                        "Literal  : Object value",
                        "Logical  : Expr left, Token operator, Expr right",
                        "Unary    : Token operator, Expr right",
                        "Variable : Token name"));

        defineAst(outDir, "Stmt", Arrays.asList(
                "Block      : List<Stmt> statements",
                "Class      : Token name, Expr.Variable superclass, List<Stmt.Function> methods",
                "Expression : Expr expression",
                "Function   : Token name, List<Token> params, List<Stmt> body",
                "If         : Expr condition, Stmt thenBranch, Stmt elseBranch",
                "Print      : Expr expression",
                "Return     : Token keyword, Expr value", // storing the keyword `return` to report errors later
                "Var        : Token name, Expr initializer",
                "While      : Expr condition, Stmt body"));
    }

    private static void defineAst(String outDir, String baseName, List<String> types)
            throws IOException {
        String path = outDir + "/" + baseName + ".java";
        PrintWriter writer = new PrintWriter(path, "UTF-8");
        writer.println("package sh.lalit.fox;");
        writer.println();
        writer.println("import java.util.List;");
        writer.println();
        writer.println("abstract class " + baseName + " {");
        writer.println();
        defineVisitor(writer, baseName, types);
        for (String type : types) {
            String className = type.split(":")[0].trim();
            String fields = type.split(":")[1].trim();
            defineType(writer, baseName, className, fields);
        }
        writer.println();
        writer.println("  abstract <R> R accept(Visitor<R> visitor);");
        writer.println("}");
        writer.close();
    }

    private static void defineType(
            PrintWriter writer, String baseName, String className, String fieldList) {
        writer.println("  static class " + className + " extends " + baseName + " {");
        writer.println("    " + className + "(" + fieldList + ") {"); // constructor
        String fields[] = fieldList.split(",");
        for (String field : fields) {
            String name = field.trim().split(" ")[1];
            writer.println("      this." + name + " = " + name + ";");
        }
        writer.println("    }");
        writer.println();
        writer.println("    @Override");
        writer.println("    <R> R accept(Visitor<R> visitor) {");
        writer.println("    return visitor.visit" + className + baseName + "(this);");
        writer.println("    }");
        for (String field : fields) {
            writer.println("    final " + field + ";");
        }
        writer.println("  }");
    }

    private static void defineVisitor(PrintWriter writer, String baseName, List<String> types) {
        writer.println("  interface Visitor<R> {");
        for (String type : types) {
            String typeName = type.split(":")[0].trim();
            writer.println(
                    "    R visit"
                            + typeName
                            + baseName
                            + "("
                            + typeName
                            + " "
                            + baseName.toLowerCase()
                            + ");");
        }
        writer.println("  }");
    }
}
