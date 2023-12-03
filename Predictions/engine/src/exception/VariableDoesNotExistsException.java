package exception;

public class VariableDoesNotExistsException extends Exception {

    private String exceptionMessage;
    public VariableDoesNotExistsException(String message){
        exceptionMessage = message;
    }

    @Override
    public String getMessage() {
        return exceptionMessage;
    }
}
