public class CharMachine {
    String str = null;
    int pos = -1;
    char ch = 'a';
    StringBuffer token = new StringBuffer("");

    public CharMachine(String str) {
        this.str = str;
    }

    public void getChar(){
        if(!isDone())
            ch = str.charAt(++pos);
    }

    public String getToken(){
        return token.toString();
    }

    public void getNbc(){
        while(!isDone() && ch == ' '){
            this.getChar();
        }
    }

    public void clearToken(){
        token = new StringBuffer("");
    }

    public void cat(){
        token.append(ch);
    }

    public void unGetChar(){
        ch = str.charAt(--pos);
    }

    public boolean isReservedWords(String[] reservedWords){
        for(String i : reservedWords){
            if(i.compareTo(token.toString()) == 0){
                return true;
            }
        }
        return false;
    }

    public boolean isReservedSymbols(Character[] reservedsymbols){
        for(char i : reservedsymbols){
            if(i == ch){
                return true;
            }
        }
        return false;
    }

    public boolean isDigit(){
        if(isDone()){
            return false;
        }
        return Character.isDigit(ch);
    }

    public boolean isNondigit(){
        if(isDone()){
            return false;
        }
        return Character.isLetter(ch) || ch == '_';
    }

    public boolean isEqualSign(){
        if(isDone()){
            return false;
        }
        return ch == '=';
    }

    public boolean isDone(){
        return pos+1 >= str.length();
    }

}
