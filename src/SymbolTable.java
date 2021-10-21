import java.util.ArrayList;
import java.util.HashMap;

public class SymbolTable {
    ArrayList<HashMap<String, Variable> > variables;  //保存变量名，数组名
    HashMap<String, Integer> reservedWords; //保存保留字和保留符号
    HashMap<String, MyFunc> functions; //函数名
    int index = -1;  //block

    private final static SymbolTable Instance = new SymbolTable();

    private SymbolTable(){
        reservedWords = new HashMap<>();
        functions = new HashMap<>();
        for(ReservedWords r: ReservedWords.values()){
            reservedWords.put(r.getName(), r.getI());
        }
    }

    public static SymbolTable getInstance(){
        return Instance;
    }

    public boolean isReservedWord(String s){
        return reservedWords.get(s) != null;
    }

    public boolean isVariable(String s){
        return false;
    }

    public boolean isFuncName(String s){
        return functions.get(s) != null;
    }

    public boolean isUnaryOp(String s){
        return s.equals("+")||s.equals("-")||s.equals("!");
    }

    public void createNewBlock(){
        variables.add(new HashMap<>());
        index++;
    }

    public void addNewVariable(String name, Variable v){
        variables.get(index).put(name, v);
    }


    public void quitCurrentBlock(){
        variables.remove(index--);
    }

}
