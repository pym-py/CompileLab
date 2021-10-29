import java.util.ArrayList;
import java.util.HashMap;

public class SymbolTable {
    ArrayList<HashMap<String, Variable> > variables;  //保存变量名，数组名
    HashMap<String, Variable> globalVariables;
    HashMap<String, Integer> reservedWords; //保存保留字和保留符号
    HashMap<String, MyFunc> functions; //函数名
    int index = -1;  //block

    private final static SymbolTable Instance = new SymbolTable();

    private SymbolTable(){
        reservedWords = new HashMap<>();
        functions = new HashMap<>();
        globalVariables = new HashMap<>();
        variables = new ArrayList<>();
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

    public boolean isIdent(String s){
        return s.matches("[_a-zA-Z][_a-zA-Z0-9]{0,}");
//        for(int i = index;i>=0;i--){
//            for(String name: variables.get(i).keySet()){
//                if(name.equals(s))
//                    return true;
//            }
//        }
//        for(String name: globalVariables.keySet()){
//            if(name.equals(s))
//                return true;
//        }
//        return false;
    }

    public Variable getIdent(String s){
        for(int i = index;i>=0;i--){
            if(variables.get(i).get(s) != null){
                return variables.get(i).get(s);
            }
        }
        if(globalVariables.get(s) != null){
            return globalVariables.get(s);
        }
        return null;
    }

    public void createNewBlock(){
        variables.add(new HashMap<>());
        index++;
    }

    public void addNewVariable(String name, Variable v){
        variables.get(index).put(name, v);
    }

    public void assign(String identName, Variable newValue){
        for(int i = index;i>=0;i--){
            if(variables.get(i).get(identName) != null){
                variables.get(i).put(identName, newValue);
                return ;
            }
        }
        if(globalVariables.get(identName) != null){
            globalVariables.put(identName, newValue);
        }
    }


    public void quitCurrentBlock(){
        variables.remove(index--);
    }

}
