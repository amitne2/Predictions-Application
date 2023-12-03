package dto.action;

import action.DecreaseAction;

public class DecreaseActionDTO extends ActionDTO {
    private String entityName;
    private final String propertyName;
    private String by;

    public DecreaseActionDTO(DecreaseAction decreaseAction) {
        super(decreaseAction);
        this.entityName = decreaseAction.getEntityName();
        this.by = decreaseAction.getBy();
        this.propertyName = decreaseAction.getPropertyName();
    }
    public String getPropertyName() {
        return propertyName;
    }

    public String getBy() {
        return by;
    }

    public String getEntityName() { return entityName; }

}
