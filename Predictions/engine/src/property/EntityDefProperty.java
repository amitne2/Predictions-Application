package property;

import dto.EntityDefPropertyDTO;
import entity.Type;
import property.Property;

public class EntityDefProperty extends Property {

    private boolean randomInitialize;
    private Object initValue;

    /*public EntityDefProperty(EntityDefPropertyDTO entityDefPropertyDTO) {
        super(entityDefPropertyDTO.getName(), entityDefPropertyDTO.getType().toString(),
                entityDefPropertyDTO.getFromRange(), entityDefPropertyDTO.getToRange());
        this.randomInitialize = entityDefPropertyDTO.isRandomInitialize();
        this.initValue = entityDefPropertyDTO.getInitValue();
    }*/
    public EntityDefProperty(EntityDefProperty entityDefProperty) {
        super(entityDefProperty.getName(), entityDefProperty.getType().toString(), entityDefProperty.getFromRange(), entityDefProperty.getToRange());
        this.randomInitialize = entityDefProperty.randomInitialize;
        this.initValue = entityDefProperty.initValue;
    }
    public EntityDefProperty(String name, String entityType, float from, float to, boolean toRandom, String init) throws Exception { // randomInitialize need to be false
        super(name, entityType, from, to);

        if(toRandom) { //error can't send toRandom true with init value
            throw new Exception("Can't send random as initialize as true with init value");
        }

        this.randomInitialize = toRandom;
        this.initValue = Type.getInitValue(type, init);
    }

    public EntityDefProperty(String name, String entityType, float from, float to, boolean toRandom) throws Exception { // randomInitialize is true, no init value
        super(name, entityType, from, to);
        if(!toRandom) { //error can't set random initialize to false without giving init value
            throw new Exception("can't set random initialize to false without giving init value");
        }
        this.randomInitialize = toRandom;
    }

    public EntityDefProperty(String name, String entityType, boolean toRandom, String init) throws Exception { //set init value for types without range
        super(name, entityType);
        if(toRandom) {
            //error can't send toRandom true with init value
            throw new Exception("Can't send random as initialize as true with init value");
        }
        this.randomInitialize = toRandom;
        this.initValue = Type.getInitValue(type, init);
    }

    public EntityDefProperty(String name, String entityType, boolean toRandom) throws Exception {
        super(name, entityType);
        if(!toRandom) { //error can't set random initialize to false without giving init value
            throw new Exception("can't set random initialize to false without giving init value");
        }
        this.randomInitialize = toRandom;
    }

    public Object getValueProperty () {
        Object value =null;
        if(!randomInitialize) {
           value = initValue;
       }
        else {
           value= Type.getRandomValue(type, getFromRange(), getToRange(), existRange);
        }
        return  value;
    }
    @Override
    public String toString() {

        if(existRange) {
            return "Name property: " + getName() + ", type: " + type.toString() +
                    ", range: from" + getFromRange() + ", to: " + getToRange() + "isRandom: " + randomInitialize;

        } else {
            return "Name property: " + getName() + ", type: " + type.toString() +
                     "isRandom: " + randomInitialize;
        }
    }

    public boolean isRandomInitialize() {
        return randomInitialize;
    }

    public Object getInit(){ return initValue; }

    public EntityDefPropertyDTO createEntityDefPropertyDTO() {
        EntityDefPropertyDTO entityDefPropertyDTO = new EntityDefPropertyDTO(getName(), getType().toString(), existRange,
                getFromRange(), getToRange(), randomInitialize, initValue);
        return entityDefPropertyDTO;
    }

    public void setRandomInitialize(boolean value) {
        this.randomInitialize = value;
    }

    public void setInitValue(Object value) {
        this.initValue = value;
    }
}
