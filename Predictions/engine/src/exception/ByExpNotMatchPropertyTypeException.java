package exception;

public class ByExpNotMatchPropertyTypeException extends Exception {
    private String name;

    public ByExpNotMatchPropertyTypeException(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
    public String getMessage(){
        return name;
    }

}
