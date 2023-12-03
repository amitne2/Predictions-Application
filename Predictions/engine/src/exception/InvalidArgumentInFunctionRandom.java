package exception;

public class InvalidArgumentInFunctionRandom extends Exception {
    private String name;

    public InvalidArgumentInFunctionRandom(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
    public String getMessage(){
        return name;
    }

}
