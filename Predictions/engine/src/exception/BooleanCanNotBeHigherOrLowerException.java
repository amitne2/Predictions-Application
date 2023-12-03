package exception;

public class BooleanCanNotBeHigherOrLowerException extends  Exception {

    private String exceptionMessage;
    public BooleanCanNotBeHigherOrLowerException(String message){
        exceptionMessage = message;
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
