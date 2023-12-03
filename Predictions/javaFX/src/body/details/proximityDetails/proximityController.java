package body.details.proximityDetails;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class proximityController {

    @FXML private Label actionTypeLabel;
    @FXML private Label sourceEntityLabel;
    @FXML private Label targetEntityLabel;
    @FXML private Label isExistSecondaryEntityLabel;
    @FXML private Label secondaryEntityLabel;
    @FXML private Label depthExpLabel;
    @FXML private Label numOfActionsLabel;

    public void setActionTypeLabel(String label) {
        actionTypeLabel.setText(label);
    }

    public void setIsExistSecondaryEntityLabel(String isExist) {
        isExistSecondaryEntityLabel.setText(isExist);
    }

    public void setSecondaryEntityLabel(String secondaryEntityLabel) {
        this.secondaryEntityLabel.setText(secondaryEntityLabel);
    }

    public void setSourceEntityLabel(String sourceEntity) {
        sourceEntityLabel.setText(sourceEntity);
    }

    public void setTargetEntityLabel(String targetEntity) {
        targetEntityLabel.setText(targetEntity);
    }

    public void setDepthExpLabel(String depth) {
        depthExpLabel.setText(depth);
    }

    public void setNumOfActionsLabel(String num) {
        numOfActionsLabel.setText(num);
    }

}

