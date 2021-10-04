import java.util.HashMap;
import java.util.Scanner;


public class Lexer {

    CharMachine cm ;

    String[] reservedWords = {  "if",  "else",  "while",  "break", "continue",  "return"};

    Character[] reservedSymbols = { '=', ';', '(', ')', '{', '}',
                                    '+', '*', '/', '<', '>'};

    String[] printWords = {"If", "Else", "While", "Break", "Continue", "Return"};

    String[] printSymbols = {"Assign", "Semicolon", "LPar", "RPar", "LBrace", "RBrace",
                            "Plus", "Mult", "Div", "Lt", "Gt"};

    HashMap<String, String> words2word = new HashMap<>();
    HashMap<Character, String> char2word = new HashMap<>();

    {
        for(int i = 0;i<reservedWords.length;i++){
            words2word.put(reservedWords[i], printWords[i]);
        }
        for(int i = 0;i<reservedSymbols.length;i++){
            char2word.put(reservedSymbols[i], printSymbols[i]);
        }
    }

    public void read(String[] args){
        String s = "a = 3;\n" +
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
                "}";
        Scanner in = new Scanner(System.in);
        StringBuffer sb = new StringBuffer("");
        while( in.hasNextLine() ){
            sb.append(in.next());
        }
        cm = new CharMachine(sb.toString());
    }

    public void start(){
        while(!cm.isDone()){
            cm.getChar();
            cm.getNbc();
            cm.clearToken();
            if(cm.isNondigit()){
                while(cm.isDigit() || cm.isNondigit()){
                    cm.cat();
                    cm.getChar();
                }
                cm.unGetChar();
                if(cm.isReservedWords(reservedWords)){
                    System.out.println(words2word.get(cm.getToken()));
                }else{
                    System.out.println("Ident("+cm.getToken()+")");
                }

            }else if(cm.isDigit()){
                while(cm.isDigit()){
                    cm.cat();
                    cm.getChar();
                }
                cm.unGetChar();
                System.out.println("Number("+cm.getToken()+")");
            }else if(cm.isEqualSign()){
                cm.getChar();
                if(cm.isEqualSign()){
                    System.out.println("Eq");
                }else{
                    System.out.println("Assign");
                    cm.unGetChar();
                }
            }else if(cm.isReservedSymbols(reservedSymbols)){
                System.out.println(char2word.get(cm.ch));
            }else{
                if(cm.ch == '\n'){
                    continue;
                }
                System.out.println("Err");
                System.exit(0);
            }
        }
    }

    public static void main(String[] args) {
        Lexer lexer = new Lexer();
        lexer.read(args);
        lexer.start();
    }
}
