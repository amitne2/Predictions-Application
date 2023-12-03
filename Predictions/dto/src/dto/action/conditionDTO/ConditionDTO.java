package dto.action.conditionDTO;

import action.condition.ConditionAction;
import action.condition.thenAndElse.ThenElseImpl;
import dto.action.ActionDTO;

public class ConditionDTO extends ActionDTO {
    private String entityName;

    //private  String singularity;
    private ThenElseDTO thenOp;
    private ThenElseDTO elseOp;

    public ConditionDTO(ConditionAction conditionAction){
        super(conditionAction);
        this.entityName = conditionAction.getEntityName();
        this.thenOp = new ThenElseDTO(conditionAction.getThenOp());
        this.elseOp = new ThenElseDTO(conditionAction.getElseOp());
    }

    @Override
    public String getActionName() {
        return super.getActionName();
    }

    public String getEntityName() {
        return entityName;
    }

    public int getSizeOfThenActions(){
        return thenOp.getNumOfActions();
    }

    public int getSizeOfElseActions() {
        return elseOp.getNumOfActions();
    }

}
