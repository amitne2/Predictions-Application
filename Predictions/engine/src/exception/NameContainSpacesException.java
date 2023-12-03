package exception;

public class NameContainSpacesException extends Exception {

    private String name;

    public NameContainSpacesException(String name) {
        this.name = name;
    }

    public String getName() { return name;}
    public String getMessage(){
        return name;
    }

}
