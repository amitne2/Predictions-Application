package body.details.KillDetails;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class KillController {

    @FXML private Label killEntityNameLabel;
    @FXML private Label killActionNameLabel;
    @FXML private Label killExistSecondary;
    @FXML private Label killSecondaryEntityLabel;

    public void setKillEntityNameLabel(String entityNameLabel) {
        killEntityNameLabel.setText(entityNameLabel);
    }

    public void setKillActionNameLabel(String actionNameLabel) {
        killActionNameLabel.setText(actionNameLabel);
    }

    public void setKillExistSecondary(String existSecondary) {
        killExistSecondary.setText(existSecondary);
    }

    public void setKillSecondaryEntityLabel(String secondaryEntityLabel) {
        killSecondaryEntityLabel.setText(secondaryEntityLabel);
    }
}
