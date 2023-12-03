package exception;

public class InvalidArgumentInFunctionEnvironmentException extends Exception {

    private String name;

    public InvalidArgumentInFunctionEnvironmentException(String name) {
        this.name = name;
    }
    public String getMessage(){
        return name;
    }


}
