package body.details.replaceDetails;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class RepalceController {

    @FXML private Label actionTypeLabel;
    @FXML private Label killEntityLabel;
    @FXML private Label createEntityLabel;
    @FXML private Label isExistSecondaryLabel;
    @FXML private Label secondaryEntityLabel;
    @FXML private Label modeLabel;


    public void setActionTypeLabel(String actionTypeLabel) {
        this.actionTypeLabel.setText(actionTypeLabel);
    }

    public void setKillEntityLabel(String killLabel) {
        killEntityLabel.setText(killLabel);
    }

    public void setCreateEntityLabel(String createLabel) {
        createEntityLabel.setText(createLabel);
    }

    public void setIsExistSecondaryLabel(String isExist) {
        isExistSecondaryLabel.setText(isExist);
    }

    public void setSecondaryEntityLabel(String secondaryLabel) {
        secondaryEntityLabel.setText(secondaryLabel);
    }

    public void setModeLabel(String mode) {
        modeLabel.setText(mode);
    }

}
