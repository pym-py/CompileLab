public class Variable {
    static private int regIndex = 0;
    static int ConstInt = 1;
    static int VariableInt = 2;
    static int UnDef = 3;
    static int ConstVariable = 4;

    private String regName = "";
    String name = null;
    int type = 0;


    public Variable(String regName) {
        type = ConstInt;
        this.regName = regName;
    }

    public Variable(String regName, String name) {
        type = VariableInt;
        this.regName = regName;
        this.name = name;
    }

    public Variable(String regName, int mark){
        type = UnDef;
        this.regName = regName;
    }



    public String getRegName() {
        return regName;
    }

    public String getName() {
        return name;
    }

    public void setType(int type) {
        this.type = type;
    }
    public boolean isUnDef(){
        return type == UnDef;
    }

    public boolean isConst(){
        return type == ConstVariable;
    }

    public static String getNextReg(){
        return new String("%"+regIndex++);
    }

    public void setRegName(String regName) {
        this.regName = regName;
    }

}
