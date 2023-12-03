package body.details.conditionDetails;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class MultipleConditionController {

    @FXML private Label actionTypeLabel;
    @FXML private Label primaryEntityLabel;
    @FXML private Label isExistSecondaryLabel;
    @FXML private Label secondaryEntityLabel;
    @FXML private Label numOfConditionsLabel;
    @FXML private Label logicalLabel;

    public void setActionTypeLabel(String type) {
        actionTypeLabel.setText(type);
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

    public void setNumOfConditionsLabel(String numOfConditionsLabel) {
        this.numOfConditionsLabel.setText(numOfConditionsLabel);
    }
    public void setLogicalLabel(String logical) {
        logicalLabel.setText(logical);
    }

}
