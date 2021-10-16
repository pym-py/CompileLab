import java.util.Scanner;

public class Test {
    String ss = "int main() {\n" +
            "    return 0x12;\n" +
            "}";
    Lexer lexer ;

    public Test(String s) {
        this.ss = s;
        lexer = new Lexer(s);
    }
    public Test(){

    }

    public void start(){
        lexer.toNextToken();
        CompUnit();
    }
    public void CompUnit(){
        FuncDef();
    }

    public void FuncDef(){
        FuncType();
        Ident();
        if(!lexer.getToken().equals("(")){
            System.exit(-1);
        }else{
            System.out.println("(");
            lexer.toNextToken();
            if (!lexer.getToken().equals(")")) {
                System.exit(-1);
            }else{
                System.out.println(")");
                lexer.toNextToken();
                Block();
            }
        }
    }

    public void FuncType(){
        if(lexer.getToken().equals("int")){
            System.out.println("define dso_local i32 ");
            lexer.toNextToken();
        }else{
            System.exit(-1);
        }
    }

    public void Ident(){
        if(!lexer.getToken().equals("main")){
            System.exit(-1);
        }else{
            System.out.println("@main");
            lexer.toNextToken();
        }
    }

    public void Block(){
        if(lexer.getToken().equals("{")){
            System.out.println("{");
            lexer.toNextToken();
            Stmt();
            if(lexer.getToken().equals("}")){
                System.out.println("}");
            }else{
                System.exit(-1);
            }
        }else{
            System.exit(-1);
        }
    }

    public void Stmt(){
        if(!lexer.getToken().equals("return")){
            System.exit(-1);
        }else{
            System.out.println("ret");
            lexer.toNextToken();
            if(!lexer.isNumber(lexer.getToken())){
                System.exit(-1);
            }else{
                String numberString = lexer.getToken();
                if(numberString.startsWith("0x")||numberString.startsWith("0X"))
                    System.out.println("i32 " + Integer.parseInt(numberString.substring(2),16));
                else if(numberString.startsWith("0"))
                    System.out.println("i32 " + Integer.parseInt(numberString.substring(1),8));
                lexer.toNextToken();
                if(lexer.getToken().equals(";")){
                    lexer.toNextToken();
                }else{
                    System.exit(-1);
                }
            }
        }
    }

    public static void main(String[] args) {
        StringBuffer sb = new StringBuffer("");
        Scanner in = new Scanner(System.in);
        while(in.hasNextLine()){
            sb.append(in.nextLine()+"\n");
        }
        String s = sb.toString().replaceAll("\\/\\/[^\\n]*|\\/\\*([^\\*^\\/]*|[\\*^\\/*]*|[^\\**\\/]*)*\\*+\\/", "");
        String s2 = sb.toString().replaceAll("//[\\s\\S]*?\\n", "\n");
        s2 = s2.replaceAll("/\\*{1,2}[\\s\\S]*?\\*/", " ");
        Test test = new Test(s2);
        test.start();
    }
}
