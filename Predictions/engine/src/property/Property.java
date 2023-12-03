package property;

import dto.PropertyDTO;
import entity.Type;

public class Property {
    private String name;
    Type type;
    boolean existRange;
    private float fromRange;
    private float toRange;

    public Property(String name, String typeName, float from, float to) {
        this.name = name;
        this.type = Type.getType(typeName);
        if(type == Type.DECIMAL || type == Type.FLOAT) {
            this.existRange = true;
            this.fromRange = from;
            this.toRange = to;
        }
        else {
            throw new IllegalArgumentException("can not add range for this type: " + typeName);
        }
    }

    public Property(String name, String typeName) {
        this.name = name;
        this.type = Type.getType(typeName);
        this.existRange = false;
    }

    public Property(Property propertyToCopy) {
        this.name = propertyToCopy.name;
        this.type = propertyToCopy.type;
        this.toRange = propertyToCopy.toRange;
        this.fromRange = propertyToCopy.fromRange;
        this.existRange = propertyToCopy.existRange;
    }
    public float getFromRange() { return fromRange;}

    public float getToRange() { return toRange ;};

    public String getName() { return name; }

    public Type getType() { return type; }

    public boolean isExistRange() { return existRange; }


}
