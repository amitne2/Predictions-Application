package body.details.calculationDetails;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class CalculationController {
    @FXML private Label actionTypeLabel;

    @FXML private Label calculationEntityNameLabel;
    @FXML private Label calculationExistSecondaryLabel;
    @FXML private Label calculationSecondaryLabel;
    @FXML private Label calculationResultPropLabel;
    @FXML private Label calculationExpressionLabel;


    public void setCalculationEntityNameLabel(String entityNameLabel) {
        calculationEntityNameLabel.setText(entityNameLabel);
    }

    public void setCalculationExistSecondaryLabel(String existSecondaryLabel) {
        calculationExistSecondaryLabel.setText(existSecondaryLabel);
    }

     public void setCalculationSecondaryLabel(String secondaryLabel) {
        calculationSecondaryLabel.setText(secondaryLabel);
     }

    public void setCalculationExpressionLabel(String arg1,String arg2, String type) {
        if(type.equals("divide"))
            calculationExpressionLabel.setText(arg1 + " " + '\\' + " " + arg2);
        else
            calculationExpressionLabel.setText(arg1 + " * " + arg2);
    }
     public void setCalculationResultPropLabel(String resultPropLabel) {
        calculationResultPropLabel.setText(resultPropLabel);
     }

     public void SetCalculationActionName(String actionName) {
         actionTypeLabel.setText(actionName);
     }
}

