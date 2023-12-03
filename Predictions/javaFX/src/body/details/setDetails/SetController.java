package body.details.setDetails;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class SetController {

    @FXML private Label setActionTypeLabel;
    @FXML private Label setActionPrimaryEntityLabel;
    @FXML private Label setActionPropertyNameLabel;
    @FXML private Label isExistSecondaryEntityLabel;
    @FXML private Label setSecondaryEntityLabel;

    @FXML private Label setActionValueLabel;

    public void setSetActionTypeLabel(String actionTypeLabel) {
        setActionTypeLabel.setText(actionTypeLabel);
    }

    public void setSetActionPrimaryEntityLabel(String primaryEntityLabel) {
        setActionPrimaryEntityLabel.setText(primaryEntityLabel);
    }

    public void setSetActionPropertyNameLabel(String propertyNameLabel) {
        setActionPropertyNameLabel.setText(propertyNameLabel);
    }

    public void setSetActionValueLabel(String valueLabel) {
        setActionValueLabel.setText(valueLabel);
    }

    public void setIsExistSecondaryEntityLabel(String isExistSecondaryEntityLabel) {
        this.isExistSecondaryEntityLabel.setText(isExistSecondaryEntityLabel);
    }

    public void setSetSecondaryEntityLabel(String secondaryEntityLabel) {
        setSecondaryEntityLabel.setText(secondaryEntityLabel);
    }


}
