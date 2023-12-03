package exception;

public class DifferentTypeException extends Exception {

    private String name;

    public DifferentTypeException(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
    public String getMessage(){
        return name;
    }

}
