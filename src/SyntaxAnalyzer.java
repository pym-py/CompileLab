import java.util.Scanner;

public class SyntaxAnalyzer {
    Lexer lexer ;

    public SyntaxAnalyzer(String s) {
        lexer = new Lexer(s);
    }

    public void setLexer(Lexer lexer) {
        this.lexer = lexer;
    }

    public void start() throws MyError {
        lexer.toNextToken();
        CompUnit();
    }
    public void CompUnit() throws MyError {
        FuncDef();
    }

    public void FuncDef() throws MyError {
        FuncType();
        Ident();
        if(!lexer.getToken().equals("(")){
            throw new MyError(lexer.getLine(), "Error 1");
        }else{
            System.out.println("(");
            lexer.toNextToken();
            if (!lexer.getToken().equals(")")) {
                throw new MyError(lexer.getLine(), "Error 1");
            }else{
                System.out.println(")");
                lexer.toNextToken();
                Block();
            }
        }
    }

    public void FuncType() throws MyError {
        if(lexer.getToken().equals("int")){
            System.out.println("define dso_local i32 ");
        }else{
            throw new MyError(lexer.getLine(), "Error 1");
        }
        lexer.toNextToken();
    }

    public void Ident() throws MyError {
        if(!lexer.getToken().equals("main")){
            throw new MyError(lexer.getLine(), "Error 1");
        }else{
            System.out.println("@main");
        }
        lexer.toNextToken();
    }

    public void Block() throws MyError {
        if(lexer.getToken().equals("{")){
            System.out.println("{");
            lexer.toNextToken();
            Stmt();
            if(lexer.getToken().equals("}")){
                System.out.println("}");
            }else{
                throw new MyError(lexer.getLine(), "Error 1");
            }
        }else{
            throw new MyError(lexer.getLine(), "Error 1");
        }
        lexer.toNextToken();
    }


    public void Stmt() throws MyError {
        if(!lexer.getToken().equals("return")){
            throw new MyError(lexer.getLine(), "Error 1");
        }else{
            System.out.println("ret");
            lexer.toNextToken();
            int ret = Exp();
            System.out.println("i32 "+ ret);
            if(!lexer.getToken().equals(";")){
                throw new MyError(lexer.getLine(), "Error 1");
            }else
                lexer.toNextToken();
        }
    }

    public int Exp() throws MyError {
        int ret =  AddExp();
//        lexer.toNextToken();
        return ret;
    }

    public int AddExp() throws MyError {
        int ret = MulExp();
        while(lexer.getToken().equals("+")||lexer.getToken().equals("-")){
            if(lexer.getToken().equals("+")){
                lexer.toNextToken();
                ret += MulExp();
            }else{
                lexer.toNextToken();
                ret -= MulExp();
            }
        }
        return ret;
    }

    public int MulExp() throws MyError {
        int ret = UnaryExp();
        while(lexer.getToken().equals("*")||lexer.getToken().equals("/")||lexer.getToken().equals("%")){
            if(lexer.getToken().equals("*")){
                lexer.toNextToken();
                ret *= UnaryExp();
            }else if(lexer.getToken().equals("/")){
                lexer.toNextToken();
                ret /= UnaryExp();
            }else{
                lexer.toNextToken();
                ret %= UnaryExp();
            }
        }
        return ret;
    }

    public int UnaryExp() throws MyError {
        int ret;
        if(SymbolTable.getInstance().isUnaryOp(lexer.getToken())){
            int pre = lexer.getToken().equals("-")?-1:1;
            lexer.toNextToken();
            ret = UnaryExp();
            return pre*ret;
        }else{
            return PrimaryExp();
        }
    }

    public int PrimaryExp() throws MyError {
        int ret;
        if(lexer.getToken().equals("(")){
            lexer.toNextToken();
            ret = Exp();
            if(!lexer.getToken().equals(")")){
                throw new MyError(lexer.getLine(), "Error 1");
            }
            lexer.toNextToken();
        }else if(lexer.isNumber(lexer.getToken())){
            ret =  toDecimal(lexer.getToken());
            lexer.toNextToken();
        }else{
            throw new MyError(lexer.getLine(), "Error 1");
        }
        return ret;
    }

    public int toDecimal(String numberString){
        if(numberString.startsWith("0x")||numberString.startsWith("0X"))
            return Integer.parseInt(numberString.substring(2),16);
        else if(numberString.startsWith("0")){
            if(numberString.length() == 1)
                return 0;
            return Integer.parseInt(numberString.substring(1),8);
        }
        else{
            return Integer.parseInt(numberString);
        }
    }

    public void removeComment(){
        String s = lexer.cm.str;
        s = s.replaceAll("//[\\s\\S]*?\\n", "\n");
        lexer.cm.str = s.replaceAll("/\\*{1,2}[\\s\\S]*?\\*/", " ");


    }

    public void read(){
        StringBuffer sb = new StringBuffer("");
        Scanner in = new Scanner(System.in);
        while(in.hasNextLine()){
            sb.append(in.nextLine()+"\n");
        }
        setLexer(new Lexer(sb.toString()));
    }
}
