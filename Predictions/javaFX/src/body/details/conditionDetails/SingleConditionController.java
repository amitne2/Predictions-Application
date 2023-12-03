package body.details.conditionDetails;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class SingleConditionController {

    @FXML private Label actionTypeLabel;
    @FXML private Label primaryEntityLabel;
    @FXML private Label isExistSecondaryLabel;
    @FXML private Label secondaryEntityLabel;
    @FXML private Label propertyNameLabel;
    @FXML private Label operatorLabel;
    @FXML private Label valueLabel;
    @FXML private Label numOfActionsThenLabel;
    @FXML private Label numOfActionsElseLabel;

    public void setActionTypeLabel(String actionType) {
        actionTypeLabel.setText(actionType);
    }

    public void setPrimaryEntityLabel(String primaryEntity) {
        primaryEntityLabel.setText(primaryEntity);
    }

    public void setIsExistSecondaryLabel(String isExist) {
        isExistSecondaryLabel.setText(isExist);
    }

    public void setSecondaryEntityLabel(String secondaryEntity) {
        secondaryEntityLabel.setText(secondaryEntity);
    }

    public void setPropertyNameLabel(String propertyName) {
        propertyNameLabel.setText(propertyName);
    }

    public void setOperatorLabel(String operator) {
        operatorLabel.setText(operator);
    }

    public void setValueLabel(String value) {
        valueLabel.setText(value);
    }
    public void setNumOfActionsThenLabel(String num) {
        numOfActionsThenLabel.setText(num);
    }

    public void setNumOfActionsElseLabel(String elseNum) {
        numOfActionsElseLabel.setText(elseNum);
    }


}
