import java.lang.invoke.MutableCallSite;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class SyntaxAnalyzer {
    Lexer lexer ;
    Queue<Variable> funcParamsQueue = new LinkedList<>();
    //q.offer进入 q.poll弹出

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

    public void Decl() throws MyError {
        if(lexer.tokenEquals("const")){
            ConstDecl();
        }else{
            VarDecl();
        }
    }

    public void ConstDecl() throws MyError {
        if(lexer.tokenEquals("const")){
            lexer.toNextToken();
            if(!lexer.tokenEquals("int"))
                throwError("E");
            lexer.toNextToken();
            ConstDef();
            while(lexer.tokenEquals(",")){
                lexer.toNextToken();
                ConstDef();
            }
            if(!lexer.tokenEquals(";"))
                throwError("E");
            lexer.toNextToken();
        }
    }

    public void ConstDef() throws MyError {
        if(!SymbolTable.getInstance().isIdent(lexer.getToken()))
            throwError("E");
        String identName = lexer.getToken();
        lexer.toNextToken();
        if(!lexer.tokenEquals("="))
            throwError("E");
        lexer.toNextToken();
        MyInt ret = new MyInt(Variable.getNextReg(), identName, -1);
        System.out.println(ret.getRegName()+" = alloca i32");
        MyInt v = (MyInt) ConstInitVal();
        ret.setValue(v.getValue());
        ret.setType(Variable.ConstVariable);
        ret.setRegName("*"+ret.getRegName());
        storeVariable(v, ret);
        SymbolTable.getInstance().addNewVariable(identName, ret);
    }

    public Variable ConstInitVal() throws MyError {
        return ConstExp();
    }

    public Variable ConstExp() throws MyError {
        Variable v = AddExp();
        if(v.isUnDef()){
            throwError("const常量赋值错误");
        }
        return v;
    }

    public void VarDecl() throws MyError{
        if(lexer.tokenEquals("int")){
            lexer.toNextToken();
            VarDef();
            while(lexer.tokenEquals(",")){
                lexer.toNextToken();
                VarDef();
            }
            if(!lexer.tokenEquals(";"))
                throwError(" ");
            lexer.toNextToken();
        }
    }

    public void VarDef() throws MyError {
        if(!SymbolTable.getInstance().isIdent(lexer.getToken()))
            throwError("E");
        String identName = lexer.getToken();
        lexer.toNextToken();
        if(!lexer.tokenEquals("=")){
            String reg = Variable.getNextReg();
            MyInt v = new MyInt("*"+reg, -1);
            v.setType(Variable.UnDef);
            SymbolTable.getInstance().addNewVariable(identName, v);
            System.out.println(reg+" = alloca i32");
            return;
        }
        lexer.toNextToken();
        String retReg = Variable.getNextReg();
        System.out.println(retReg+" = alloca i32");
        MyInt v = (MyInt) InitVal();
        MyInt ret = new MyInt("*"+retReg, identName, -1);
        if(!v.isUnDef()){
            ret.setValue(v.getValue());
        }
        SymbolTable.getInstance().addNewVariable(identName, ret);
        storeVariable(v, ret);
    }

    public Variable InitVal() throws MyError {
        return Exp();
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
            System.out.println("define dso_local i32");
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
            SymbolTable.getInstance().createNewBlock();
            System.out.println("{");
            lexer.toNextToken();
            while(lexer.tokenEquals("const")
                    ||lexer.tokenEquals("int")
                    ||lexer.tokenEquals("return")
                    ||SymbolTable.getInstance().isIdent(lexer.getToken())
                    ||lexer.tokenEquals("+")
                    ||lexer.tokenEquals("-"))
                BlockItem();
            if(lexer.getToken().equals("}")){
                System.out.println("}");
                SymbolTable.getInstance().quitCurrentBlock();
            }else{
                throw new MyError(lexer.getLine(), "Error 1");
            }
        }else{
            throw new MyError(lexer.getLine(), "Error 1");
        }
        lexer.toNextToken();
    }

    public void BlockItem() throws MyError{
        if(lexer.tokenEquals("const")|| lexer.tokenEquals("int")) {    //Decl->ConstDecl
            Decl();
        }else {
            Stmt();
        }
    }


    public void Stmt() throws MyError {
        if(lexer.tokenEquals("return")){
            lexer.toNextToken();
            Variable v = Exp();
            if(!lexer.tokenEquals(";"))
                throwError("E");
            System.out.println("ret i32 "+v.getRegName());
            lexer.toNextToken();
        } else if(SymbolTable.getInstance().isIdent(lexer.getToken())){
            String identName = lexer.getToken();
            MyInt v = (MyInt) LVal();
            if(!lexer.tokenEquals("="))
                throwError("E");
            lexer.toNextToken();
            Variable v2 = Exp();
            if(v.isConst())
                throwError("assign a const variable");
            if(v.isUnDef())
                v.setType(Variable.VariableInt);
            v.setValue(((MyInt)v2).getValue());
            storeVariable(v2, v);
            if(!lexer.tokenEquals(";"))
                throwError("E");
            lexer.toNextToken();
        }else{
            if(!lexer.tokenEquals(";")){
                Exp();
            }else{
                Exp();
                if(!lexer.tokenEquals(";"))
                    throwError("E");
                lexer.toNextToken();
            }
        }
    }


    public Variable Exp() throws MyError {
        return AddExp();
    }


    public Variable LVal() throws MyError {
        if(SymbolTable.getInstance().isIdent(lexer.getToken())){
            String identName = lexer.getToken();
            lexer.toNextToken();
//            MyInt oldVariable = (MyInt) SymbolTable.getInstance().getIdent(identName);
//            MyInt newVariable = new MyInt(Variable.getNextReg(), oldVariable.getName(), (oldVariable).getValue());
//            System.out.println(newVariable.getRegName()+" = load i32, i32 "+oldVariable.getRegName());
            MyInt newVariable = (MyInt) SymbolTable.getInstance().getIdent(identName);
            return newVariable;
        }else{
            throw new MyError(lexer.getLine(), "Error 1");
        }

    }

    public Variable PrimaryExp() throws MyError {
        Variable v ;
        if(lexer.getToken().equals("(")){
            lexer.toNextToken();
            v = Exp();
            if(!lexer.getToken().equals(")")){
                throw new MyError(lexer.getLine(), "Error 1");
            }
            lexer.toNextToken();
        }else if(lexer.isNumber(lexer.getToken())){
            int num =  toDecimal(lexer.getToken());
            v = addAndAssign(new MyInt("0", 0), new MyInt(String.valueOf(num), num));
            lexer.toNextToken();
        }else{
            v = LVal();
            MyInt mi = new MyInt(Variable.getNextReg(), ((MyInt)v).getValue());
            loadVariable(v, mi);
            return mi;
        }
        return v;
    }

    public Variable UnaryExp() throws MyError {
        Variable ret;
        if(SymbolTable.getInstance().isUnaryOp(lexer.getToken())){
            if(lexer.getToken().equals("-")){
                lexer.toNextToken();
                Variable next = UnaryExp();
                ret = subAndAssign(new MyInt("0", 0), next);
            }else{
                lexer.toNextToken();
                Variable next = UnaryExp();
                ret = addAndAssign(new Variable(Variable.getNextReg()), next);
            }
        }else if(SymbolTable.getInstance().isIdent(lexer.getToken())){
            String identName = lexer.getToken();
            lexer.toNextToken();
            if(!lexer.tokenEquals("(")){
                lexer.toLastToken();
                return PrimaryExp();
            }
            lexer.toNextToken();
            FuncRParams();
            if(!lexer.tokenEquals(")"))
                throwError("E");
            lexer.toNextToken();
            ret = null;
        }else{
            ret = PrimaryExp();
        }
        return ret;
    }


    public Variable AddExp() throws MyError {
        Variable ret = MulExp();
        while(lexer.getToken().equals("+")||lexer.getToken().equals("-")){
            if(lexer.getToken().equals("+")){
                lexer.toNextToken();
                Variable v = MulExp();
                ret = addAndAssign(ret, v);
            }else{
                lexer.toNextToken();
                Variable v = MulExp();
                ret = subAndAssign(ret, v);
            }
        }
        return ret;
    }


    public Variable MulExp() throws MyError {
        Variable ret = UnaryExp();
        while(lexer.getToken().equals("*")||lexer.getToken().equals("/")||lexer.getToken().equals("%")){
            if(lexer.getToken().equals("*")){
                lexer.toNextToken();
                Variable next = UnaryExp();
                ret = mulAndAssign(ret, next);
            }else if(lexer.getToken().equals("/")){
                lexer.toNextToken();
                Variable next = UnaryExp();
                ret = divAndAssign(ret, next);
            }else{
                lexer.toNextToken();
                Variable next = UnaryExp();
                ret = modAndAssign(ret, next);
            }
        }
        return ret;
    }

    public void FuncRParams() throws MyError {
        Variable v = Exp();
        while(lexer.tokenEquals(",")){
            Variable next = Exp();
        }
    }





//    public Variable RelExp() throws MyError {  //比较
//        int ret = AddExp();
//        while(lexer.getToken().equals("<")||lexer.getToken().equals(">")||lexer.getToken().equals("<=")||lexer.getToken().equals(">=")){
//            if(lexer.getToken().equals("<")){
//                lexer.toNextToken();
//                ret += MulExp();
//            }else{
//                lexer.toNextToken();
//                ret -= MulExp();
//            }
//        }
//        return ret;
//    }

//    public Variable EqExp() throws MyError {  //
//        int ret = RelExp();
//        while(lexer.getToken().equals("==")||lexer.getToken().equals("!=")){
//            if(lexer.getToken().equals("+")){
//                lexer.toNextToken();
//                ret += MulExp();
//            }else{
//                lexer.toNextToken();
//                ret -= MulExp();
//            }
//        }
//        return ret;
//    }

    public Variable Cond (){
        return null;
//        return LOrExp();
    }

    public void LAndExp(){

    }

    public int LOrExp(){

        return 0;
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

    public Variable mulAndAssign(Variable a, Variable b){
        if(!a.isUnDef()&&!b.isUnDef()){
            MyInt mi = new MyInt(Variable.getNextReg(), ((MyInt)a).getValue()*((MyInt)b).getValue());
            System.out.print(mi.getRegName()+" = ");
            System.out.print("mul i32 ");
            System.out.println(a.getRegName()+", "+b.getRegName());
            return mi;
        }else{
            MyInt mi = new MyInt(Variable.getNextReg());
            System.out.print(mi.getRegName()+" = ");
            System.out.print("mul i32 ");
            System.out.println(a.getRegName()+", "+b.getRegName());
            return mi;
        }
    }

    public Variable addAndAssign(Variable a, Variable b){
        if(!a.isUnDef()&&!b.isUnDef()){
            MyInt mi = new MyInt(Variable.getNextReg(), ((MyInt)a).getValue()+((MyInt)b).getValue());
            System.out.print(mi.getRegName()+" = ");
            System.out.print("add i32 ");
            System.out.println(a.getRegName()+", "+b.getRegName());
            return mi;
        }else{
            MyInt mi = new MyInt(Variable.getNextReg());
            System.out.print(mi.getRegName()+" = ");
            System.out.print("add i32 ");
            System.out.println(a.getRegName()+", "+b.getRegName());
            return mi;
        }

    }

    public Variable subAndAssign(Variable a, Variable b){
        if(!a.isUnDef()&&!b.isUnDef()){
            MyInt mi = new MyInt(Variable.getNextReg(), ((MyInt)a).getValue()-((MyInt)b).getValue());
            System.out.print(mi.getRegName()+" = ");
            System.out.print("sub i32 ");
            System.out.println(a.getRegName()+", "+b.getRegName());
            return mi;
        }else{
            MyInt mi = new MyInt(Variable.getNextReg());
            System.out.print(mi.getRegName()+" = ");
            System.out.print("sub i32 ");
            System.out.println(a.getRegName()+", "+b.getRegName());
            return mi;
        }
    }

    public Variable divAndAssign(Variable a, Variable b){
        if(!a.isUnDef()&&!b.isUnDef()){
            MyInt mi = new MyInt(Variable.getNextReg(), ((MyInt)a).getValue()/((MyInt)b).getValue());
            System.out.print(mi.getRegName()+" = ");
            System.out.print("sdiv i32 ");
            System.out.println(a.getRegName()+", "+b.getRegName());
            return mi;
        }else{
            MyInt mi = new MyInt(Variable.getNextReg());
            System.out.print(mi.getRegName()+" = ");
            System.out.print("sdiv i32 ");
            System.out.println(a.getRegName()+", "+b.getRegName());
            return mi;
        }
    }

    public Variable modAndAssign(Variable a, Variable b){
        if(!a.isUnDef()&&!b.isUnDef()){
            MyInt mi = new MyInt(Variable.getNextReg(), ((MyInt)a).getValue()%((MyInt)b).getValue());
            System.out.print(mi.getRegName()+" = ");
            System.out.print("mod i32 ");
            System.out.println(a.getRegName()+", "+b.getRegName());
            return mi;
        }else{
            MyInt mi = new MyInt(Variable.getNextReg());
            System.out.print(mi.getRegName()+" = ");
            System.out.print("mod i32 ");
            System.out.println(a.getRegName()+", "+b.getRegName());
            return mi;
        }
    }

    public void storeVariable(Variable a, Variable b){
        System.out.println("store i32 "+a.getRegName()+", i32 "+b.getRegName());
    }

    public void loadVariable(Variable a, Variable b){
        System.out.println(b.getRegName()+" = load i32, i32 "+a.getRegName());
    }

    public void throwError(String s) throws MyError {
        throw new MyError(lexer.getLine(), s);
    }

}
