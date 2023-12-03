package dto;

public class EnvVariablesIncstanceDTO {
    private String name;
    private String type;
    private boolean existRange;
    private float fromRange;
    private float toRange;
    private  Object value;
    private boolean wasRandom;

    public EnvVariablesIncstanceDTO(String name, String type, boolean existRange, float fromRange, float toRange, Object value, boolean wasRandom){
        this.name = name;
        this.type = type;
        this.existRange = existRange;
        this.fromRange = fromRange;
        this.toRange= toRange;
        this.value = value;
        this.wasRandom = wasRandom;
    }

    public Object getValue() {
        return value;
    }

    public boolean isExistRange() {
        return existRange;
    }

    public String getName() {
        return name;
    }

    public float getToRange() {
        return toRange;
    }

    public float getFromRange() {
        return fromRange;
    }

    public String getType() {
        return type;
    }

    public boolean isWasRandom() {
        return wasRandom;
    }
}
