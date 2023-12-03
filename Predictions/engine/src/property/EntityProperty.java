package property;

import java.util.ArrayList;
import java.util.List;

public class EntityProperty extends Property {
    private Object value;
    private List<ChangesValueDuration> changesValueList;

    public EntityProperty(String name, String entityType, float from, float to, Object value) { //set value with init value (no random)
        super(name, entityType, from, to);
        this.value = value;
        this.changesValueList = new ArrayList<>();
        ChangesValueDuration changesValueDuration = new ChangesValueDuration(0, value);
        changesValueList.add(changesValueDuration);
    }

    public EntityProperty(String name, String entityType, Object value) throws Exception { //random number between range
        super(name, entityType); //without range
        this.value = value;
        this.changesValueList = new ArrayList<>();
        ChangesValueDuration changesValueDuration = new ChangesValueDuration(0, value);
        changesValueList.add(changesValueDuration);
    }

    public EntityProperty(Property property, Object value) {
        super(property);
        this.value = value;
        this.changesValueList = new ArrayList<>();
        ChangesValueDuration changesValueDuration = new ChangesValueDuration(0,value);
        changesValueList.add(changesValueDuration);
    }

    public Object getValue() { return value; }

    public void setValue(Object newValue) {
        value = newValue;
    }

    public void setEndTickAndAddNewElementForNextOne(long endTick, Object value) {
        changesValueList.get(changesValueList.size() - 1).setEndTick(endTick);
        changesValueList.add(new ChangesValueDuration(endTick, value));
    }

    public void setAnotherTickAsTheSameValue(long endTick) {
        changesValueList.get(changesValueList.size() - 1).setEndTick(endTick);
    }

    public List<ChangesValueDuration> getChangesValueList(){
        return changesValueList;
    }

}
