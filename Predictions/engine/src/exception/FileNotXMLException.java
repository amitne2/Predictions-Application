package exception;

public class FileNotXMLException extends Exception{

    private String name;

    public FileNotXMLException(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
    public String getMessage(){
        return name;
    }
}

