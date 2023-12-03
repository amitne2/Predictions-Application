package dto;


public class EntityDefPropertyDTO  {
    private String name;
    String type;
    boolean existRange;
    private float fromRange;
    private float toRange;
    private boolean randomInitialize;
    private Object initValue;

    public EntityDefPropertyDTO(String name, String type, boolean existRange,
                                float fromRange, float toRange, boolean randomInitialize, Object initValue) {
        this.name = name;
        this.type = type;
        this.existRange = existRange;
        this.fromRange = fromRange;
        this.toRange = toRange;
        this.randomInitialize = randomInitialize;
        this.initValue = initValue;
    }
    public float getFromRange() { return fromRange;}

    public float getToRange() { return toRange ;};


    public String getType() { return type; }

    public boolean isExistRange() { return existRange; }

    public String getName() { return name; }
    public boolean isRandomInitialize() {
        return randomInitialize;
    }
    public Object getInitValue() { return initValue; }

}
