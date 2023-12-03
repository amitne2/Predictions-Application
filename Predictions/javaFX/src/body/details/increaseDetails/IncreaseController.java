package body.details.increaseDetails;

import dto.action.IncreaseActionDTO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import resources.Resources;
import javafx.scene.Node;

import java.io.IOException;
import java.net.URL;

public class IncreaseController {
    private IncreaseActionDTO increaseActionDTO;

    @FXML private VBox increaseVbox;
    @FXML private Label IncreaseTypeLabelTxt;
    @FXML private Label increasePrimaryEntity;
    @FXML private Label increaseExistSecondaryEntity;
    @FXML private Label increaseSecondaryEntity;
    @FXML private Label increasePropertyName;
    @FXML private Label increaseByExp;

    public void setTextTypeLabel(String type) {
        IncreaseTypeLabelTxt.setText(type);
    }

    public void setTextPrimaryLabel(String primary) {
        increasePrimaryEntity.setText(primary);
    }

    public void setIncreaseByExp(String by) {
        increaseByExp.setText(by);
    }

    public void setIsExistSecondaryEntity(String exist){
       increaseExistSecondaryEntity.setText(exist);
    }

    public void setPropertyName(String propertyName) {
        increasePropertyName.setText(propertyName);
    }

    public void setIncreaseExistSecondaryEntity(String secondaryEntity) {
        increaseExistSecondaryEntity.setText(secondaryEntity);
    }
}
