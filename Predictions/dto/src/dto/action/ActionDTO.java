package dto.action;

import action.Action;
import action.SeconderyEntity;

public class ActionDTO {
    private final String actionName;
    private boolean isExistSecondary;
    private String entitySecondary;

   /* public ActionDTO(String actionName) {
        this.actionName= actionName;

    }*/

    public ActionDTO(Action action) {
        this.actionName = action.getActionName();
        this.isExistSecondary = action.isExistSecondary();
        if(isExistSecondary)
            this.entitySecondary = action.getSecondaryInfo().getEntityName();
    }

    public boolean isExistSecondary() {
        return isExistSecondary;
    }

    public String getEntitySecondary() {
        return entitySecondary;
    }

    public String getActionName() {
        return actionName;
    }
}
