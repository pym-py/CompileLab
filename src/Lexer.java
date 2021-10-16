public class Lexer {

    CharMachine cm ;

    SymbolTable symbolTable = SymbolTable.getInstance();

    public Lexer(CharMachine cm) {
        this.cm = cm;
    }

    public Lexer(String s){
        this.cm = new CharMachine(s);
    }


    public String getToken(){
        return cm.getToken();
    }

    public boolean isOctalDigit(String s){
        return s.matches("[0-7]");
    }

    public boolean isNonzeroDigit(String s){
        return s.matches("[1-9]");
    }

    public boolean isHexadecimalDigit(String s){
        return s.matches("[0-9a-fA-F]");
    }

    public boolean isLetter(String s){
        return s.matches("[a-zA-Z]");
    }

    public boolean isDigit(String s){
        return s.matches("[0-9]");
    }

    public boolean isNumber(String s){
        int i = 0;
        if(s.charAt(i) == '0'){
            i++;
            if(i == s.length()){
                return true;
            }
            if(s.charAt(i) == 'x'||s.charAt(i) == 'X'){
                i++;
                while(i<s.length()){
                    if(!isHexadecimalDigit(Character.toString(s.charAt(i)))){
                        return false;
                    }
                    i++;
                }
                return true;
            }else if(isOctalDigit(Character.toString(s.charAt(i)))){
                while(i<s.length()){
                    if(!isOctalDigit(Character.toString(s.charAt(i)))){
                        return false;
                    }
                    i++;
                }
                return true;
            }else
                return false;
        }else if(isNonzeroDigit(Character.toString(s.charAt(i)))){
            while(i<s.length()){
                if(!isDigit(Character.toString(s.charAt(i)))){
                    return false;
                }
                i++;
            }
            return true;
        }else
            return false;
    }

    public boolean isEnd(){
        return cm.isDone();
    }

    public void toNextToken(){
        if(!cm.isDone()){
            cm.getNextToken();
            if(symbolTable.isReservedWord(cm.getToken())){
//                System.out.println(cm.getToken()); //保留字
            }else if(isNumber(cm.getToken())){
//                System.out.println(cm.getToken()); //数字
            }else if(symbolTable.isVariable(cm.getToken())){
//                System.out.println("变量");
            }else{
//                System.out.println("ERROR");
            }
        }
    }





    public static void main(String[] args) {
        Lexer lexer = new Lexer(new CharMachine("a = 3;\n" +
                "If = 0\n" +
                "while (a < 4396) {\n" +
                "    if (a == 010) {\n" +
                "        ybb = 233;\n" +
                "        a = a + ybb;\n" +
                "        continue;\n" +
                "    } else {\n" +
                "        a = a + 7;\n" +
                "    }\n" +
                "    If = If + a * 2;\n" +
                "}"));
        String target = "int main() {\n" +
                "    /*\n" +
                "    return 123;\n" +
                "}\n";
        String s = target.replaceAll("\\/\\/[^\\n]*|\\/\\*([^\\*^\\/]*|[\\*^\\/*]*|[^\\**\\/]*)*\\*+\\/", "");
        System.out.println(s);
//        while(!lexer.isEnd()){
//            lexer.toNextToken();
//        }
    }
}


