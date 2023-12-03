package body.details.decreaseDetails;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class DeacreaseController {

    @FXML private Label deacreaseTypeLabel;
    @FXML private Label deacreasePrimaryEntity;
    @FXML private Label deacreaseExistSecondary;
    @FXML private Label deacreaseSecondaryEntity;
    @FXML private Label deacreasePropertyName;
    @FXML private Label deacreseByExp;

    public void setTextTypeLabel(String type) {
        deacreaseTypeLabel.setText(type);
    }

    public void setTextPrimaryLabel(String primary) {
        deacreasePrimaryEntity.setText(primary);
    }

    public void setIncreaseByExp(String by) {
        deacreseByExp.setText(by);
    }

    public void setIsExistSecondaryEntity(String exist){
        deacreaseExistSecondary.setText(exist);
    }

    public void setPropertyName(String propertyName) {
        deacreasePropertyName.setText(propertyName);
    }

    public void setExistSecondaryEntity(String secondaryEntity) {
        deacreaseSecondaryEntity.setText(secondaryEntity);
    }
}
