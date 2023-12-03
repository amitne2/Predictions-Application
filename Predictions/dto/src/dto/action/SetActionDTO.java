package dto.action;

import action.SetAction;
import dto.action.ActionDTO;

public class SetActionDTO extends ActionDTO {
    private String entityName;
    private String propertyName;
    private String value;

    public SetActionDTO(SetAction setAction) {
        super(setAction);
        this.entityName = setAction.getEntityName();
        this.propertyName = setAction.getPropertyName();
        this.value = setAction.getValue();
    }

    public String getPropertyName() {
        return propertyName;
    }

    public String getValue() {
        return value;
    }

    public String getEntityName() {
        return entityName;
    }
}
