public class MyError extends Throwable {
    int line ;
    String message;

    public MyError(int line, String message) {
        this.line = line;
        this.message = message;
    }

    public void printErrorMessage(){
        System.out.println("Error:"+message+"\nAt line:"+line);
    }
}
