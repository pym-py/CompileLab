public class Test {


    public static void main(String[] args) {
        String s = "int main() {\n" +
                "    return (15)/ 0x5;\n" +
                "}";
        SyntaxAnalyzer syntaxAnalyzer = new SyntaxAnalyzer(s);
        syntaxAnalyzer.read();
        syntaxAnalyzer.removeComment();
        try {
            syntaxAnalyzer.start();
        } catch (MyError e) {
            e.printErrorMessage();
            System.exit(-1);
        }
    }
}
