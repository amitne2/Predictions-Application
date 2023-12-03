package exception;

public class OutOfRangeException extends  Exception {
    public OutOfRangeException(){}

    @Override
    public String getMessage() {
        return "Out of range number was entered";
    }

}
