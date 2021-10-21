public class MyInt extends Variable{
    int value;


    public MyInt(String name, int i){
        super(name);
        this.value = i;
    }

    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }
}
