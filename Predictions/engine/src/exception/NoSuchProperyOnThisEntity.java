package exception;

public class NoSuchProperyOnThisEntity extends Exception {
    private String name;

    public NoSuchProperyOnThisEntity(String name) {
        this.name = name;
    }
public String getName() { return name; }
    public String getMessage(){
        return name;
    }

}
