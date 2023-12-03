package exception;

public class SamePropertyNameInEntityException extends Exception{
    private String name;

    public SamePropertyNameInEntityException(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
    public String getMessage(){
        return name;
    }


}
