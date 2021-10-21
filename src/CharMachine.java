public class CharMachine {
    String str ;
    int pos = -1;
    String ch = "";
    int currentLine = 1;
    StringBuffer token = new StringBuffer("");

    public CharMachine(String str) {
        this.str = str;
    }

    public void getChar(){
        if(!isDone()){
            ch = Character.toString(str.charAt(++pos));
        }
    }

    public int getCurrentLine(){
        return currentLine;
    }

    public String getToken(){
        return token.toString();
    }

    public void getNbc(){
        while(!isDone() && (ch.equals(" ")||ch.equals("\n")||ch.equals("\t"))){
            if(ch.equals("\n")){
                currentLine++;
            }
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
        } else if(ch.equals("!")){
            cat();
            getChar();
            if(ch.equals("=")){
                cat();
            }else
                unGetChar();
        }else if(ch.equals("<")){
            cat();
            getChar();
            if(ch.equals("=")){
                cat();
            }else
                unGetChar();
        }else if(ch.equals(">")){
            cat();
            getChar();
            if(ch.equals("=")){
                cat();
            }else
                unGetChar();
        }else if(ch.equals("&")){
            cat();
            getChar();
            if(ch.equals("&")){
                cat();
            }else
                unGetChar();
        }else if(ch.equals("|")){
            cat();
            getChar();
            if(ch.equals("|")){
                cat();
            }else
                unGetChar();
        } else if(ch.matches("[0-9a-zA-Z_]")){
            while(!isDone() && !isBlank() &&  ch.matches("[0-9a-zA-Z_]")){
                cat();
                getChar();
            }
            unGetChar();
        }else if(SymbolTable.getInstance().isReservedWord(ch)){
            cat();
        }else{
           
        }
    }

    public boolean isDone(){
        return pos+1 >= str.length();
    }
}

