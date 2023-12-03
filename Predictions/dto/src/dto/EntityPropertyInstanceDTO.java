package dto;

import entity.Type;
import entity.instance.EntityInstanceImpl;
import property.EntityProperty;

public class EntityPropertyInstanceDTO {
    private String name;
    String type;
    boolean existRange;
    private float fromRange;
    private float toRange;
    private Object value;

    public EntityPropertyInstanceDTO(String name, String type, Boolean existRange, float fromRange, float toRange, Object value) {
        this.name = name;
        this.type = type;
        this.existRange = existRange;
        this.fromRange = fromRange;
        this.toRange = toRange;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public float getFromRange() {
        return fromRange;
    }

    public float getToRange() {
        return toRange;
    }

    public boolean isExistRange() {
        return existRange;
    }

    public Object getValue() {
        return value;
    }
}
