public class CharMachine {
    String str = null;
    int pos = -1;
    String ch = "";
    StringBuffer token = new StringBuffer("");

    public CharMachine(String str) {
        this.str = str;
    }

    public void getChar(){
        if(!isDone()){
            ch = Character.toString(str.charAt(++pos));
        }
    }

    public String getToken(){
        return token.toString();
    }

    public void getNbc(){
        while(!isDone() && (ch.equals(" ")||ch.equals("\n")||ch.equals("\t"))){
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
        ch = Character.toString(str.charAt(--pos));
    }


    public boolean isBlank(){
        return ch.equals("\t")||ch.equals(" ")||ch.equals("\n");
    }

    public void getNextToken(){
        clearToken();
        getChar();
        getNbc();
        if(ch.equals(";")){
            cat();
        }else if(ch.equals("=")){
            cat();
            getChar();
            if(ch.equals("=")){
                cat();
            }else {
                unGetChar();
            }
        } else if(ch.matches("[0-9a-zA-Z]")){
            while(!isDone() && !isBlank() &&  ch.matches("[0-9a-zA-Z]")){
                cat();
                getChar();
            }
            unGetChar();
        }else if(SymbolTable.getInstance().isReservedWord(ch)){
            cat();
        }else{
            System.out.println("ERROR");
        }
    }

    public boolean isDone(){
        return pos+1 >= str.length();
    }

}

