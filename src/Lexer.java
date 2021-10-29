public class Lexer {

    CharMachine cm ;

    SymbolTable symbolTable = SymbolTable.getInstance();
    String lastToken ;

    public Lexer(CharMachine cm) {
        this.cm = cm;
    }

    public Lexer(String s){
        this.cm = new CharMachine(s);
    }


    public String getToken(){
        return cm.getToken();
    }

    public int getLine(){
        return cm.getCurrentLine();
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
        if(s.matches("(0x|0X)[0-9a-fA-F]+"))
            return true;
        if(s.matches("[1-9][0-9]*"))
            return true;
        if(s.matches("[0-7]+"))
            return true;
        return false;
    }


    public void toNextToken() throws MyError {
        lastToken = cm.getToken();
        if(!cm.isDone()){
            cm.getNextToken();
            if(cm.getToken().equals(""))
                return;
            if(!isLegalToken(cm.getToken())){
                throw new MyError(cm.getCurrentLine(), "TokenIllegal");
            }
        }else{
            cm.clearToken();
        }
    }

    public void toLastToken(){
        cm.setToLastToken();
        cm.setToken(new StringBuffer(lastToken));
    }

    public boolean isLegalToken(String s){
        return      symbolTable.isReservedWord(s)
                ||  isNumber(s)
                ||  symbolTable.isVariable(s)
                ||  symbolTable.isFuncName(s)
                ||  symbolTable.isIdent(s);

    }
    public boolean tokenEquals(String s){
        return s.equals(getToken());
    }

}



//        int i = 0;
//        if(s.charAt(i) == '0'){
//            i++;
//            if(i == s.length()){
//                return true;
//            }
//            if(s.charAt(i) == 'x'||s.charAt(i) == 'X'){
//                i++;
//                while(i<s.length()){
//                    if(!isHexadecimalDigit(Character.toString(s.charAt(i)))){
//                        return false;
//                    }
//                    i++;
//                }
//                return true;
//            }else if(isOctalDigit(Character.toString(s.charAt(i)))){
//                while(i<s.length()){
//                    if(!isOctalDigit(Character.toString(s.charAt(i)))){
//                        return false;
//                    }
//                    i++;
//                }
//                return true;
//            }else
//                return false;
//        }else if(isNonzeroDigit(Character.toString(s.charAt(i)))){
//            while(i<s.length()){
//                if(!isDigit(Character.toString(s.charAt(i)))){
//                    return false;
//                }
//                i++;
//            }
//            return true;
//        }else
//            return false;


