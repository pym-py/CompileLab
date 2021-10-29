import java.util.Spliterator;

public class MyInt extends Variable{
    int value;


    public MyInt(String regName,String name, int i){
        super(regName, name);
        super.setType(Variable.VariableInt);
        this.value = i;
    }

    public MyInt(String regName, int value) {
        super(regName);
        super.setType(Variable.ConstInt);
        this.value = value;
    }

    public MyInt(String regName){
        super(regName);
        super.setType(Variable.UnDef);
    }

    public int getValue() {
        return value;
    }

    public String getName() {
        return this.name;
    }
    public void setValue(int value){
        this.value = value;
    }

}
