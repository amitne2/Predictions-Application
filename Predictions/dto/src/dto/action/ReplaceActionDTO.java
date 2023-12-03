package dto.action;

import action.ReplaceAction;

public class ReplaceActionDTO extends ActionDTO {
    private String killEntityName;
    private String createEntityName;
    private String mode;

    public ReplaceActionDTO(ReplaceAction replaceAction) {
        super(replaceAction);
        this.createEntityName = replaceAction.getCreateEntityName();
        this.killEntityName = replaceAction.getKillEntityName();
        this.mode = replaceAction.getMode();
    }

    @Override
    public String getActionName() {
        return super.getActionName();
    }

    public String getKillEntityName() {
        return killEntityName;
    }

    public String getCreateEntityName() {
        return createEntityName;
    }

    public String getMode() {
        return mode;
    }

}
