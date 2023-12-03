package exception;

public class InvalidArgumentInFunctionTicksException extends  Exception{
    private String name;

    public InvalidArgumentInFunctionTicksException(String name) {
        this.name = name;
    }
    public String getMessage(){
        return name;
    }

}
