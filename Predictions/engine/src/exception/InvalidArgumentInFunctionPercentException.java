package exception;

public class InvalidArgumentInFunctionPercentException extends Exception{
    private String name;

    public InvalidArgumentInFunctionPercentException(String name) {
        this.name = name;
    }
    public String getMessage(){
        return name;
    }

}
