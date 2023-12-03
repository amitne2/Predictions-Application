package exception;

public class NoSuchEntityException extends Exception {
    private String name;
    public NoSuchEntityException(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

   @Override
    public String getMessage() {
        return name;
    }
}
