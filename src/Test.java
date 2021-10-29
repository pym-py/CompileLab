public class Test {


    public static void main(String[] args) {
        String s = "int main() {\n" +
                "    int a = 2, b = 3, c = a+ b; return c;\n" +
                "}";
        SyntaxAnalyzer syntaxAnalyzer = new SyntaxAnalyzer(s);
        syntaxAnalyzer.read();
        syntaxAnalyzer.removeComment();
        System.out.println("declare i32 @getint()\n" +
                "declare void @putint(i32)");
        try {
            syntaxAnalyzer.start();
        } catch (MyError e) {
            e.printErrorMessage();
            System.exit(-1);
        }
    }
}
