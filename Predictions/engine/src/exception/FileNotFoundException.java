package exception;

public class FileNotFoundException extends Exception{
    private String name;

    public FileNotFoundException(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
    public String getMessage(){
        return name;
    }
}
