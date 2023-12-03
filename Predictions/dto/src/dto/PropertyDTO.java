package dto;

import entity.Type;
import property.EntityDefProperty;
import property.Property;

public class PropertyDTO {

    private String name;
    String type;
    boolean existRange;
    private float fromRange;
    private float toRange;

    public PropertyDTO(String name, String type, boolean existRange, float fromRange, float toRange) {
        this.name = name;
        this.type = type;
        this.existRange =existRange;
        this.fromRange = fromRange;
        this.toRange = toRange;
    }

    public String getName() {
        return name;
    }

    public boolean isExistRange() {
        return existRange;
    }

    public float getFromRange() {
        return fromRange;
    }

    public float getToRange() {
        return toRange;
    }

    public String getType() {
        return type;
    }
}
