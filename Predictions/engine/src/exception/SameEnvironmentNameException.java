package exception;

public class SameEnvironmentNameException extends Exception {

    private String name;

    public SameEnvironmentNameException(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
    public String getMessage(){
        return name;
    }


}
