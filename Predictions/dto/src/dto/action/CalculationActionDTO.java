package dto.action;

import action.CalculationAction;
import dto.action.ActionDTO;

public class CalculationActionDTO extends ActionDTO {
    private String calculationType;
    private String entityName;
    private String resultProp;
    private String arg1;
    private String arg2;


    public CalculationActionDTO(CalculationAction calculationAction) {
        super(calculationAction);
        this.entityName = calculationAction.getEntityName();
        this.arg1 = calculationAction.getArg1();
        this.arg2 = calculationAction.getArg2();
        this.calculationType = calculationAction.getCalculationType();
        this.resultProp = calculationAction.getResultProp();
    }

    public String getArg1() {
        return arg1;
    }

    public String getArg2() {
        return arg2;
    }

    public String getResultProp() {
        return resultProp;
    }

    public String getCalculationType() {
        return calculationType;
    }


    public String getEntityName() {
        return entityName;
    }

}
