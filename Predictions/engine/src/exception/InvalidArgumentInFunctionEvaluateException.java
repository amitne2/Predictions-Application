package exception;

public class InvalidArgumentInFunctionEvaluateException extends Exception{
    private String name;

    public InvalidArgumentInFunctionEvaluateException(String name) {
        this.name = name;
    }
    public String getMessage(){
        return name;
    }
}
