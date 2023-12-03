package header;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.control.Button;
import javafx.scene.control.Label;

import javafx.stage.FileChooser;
import main.AppController;


import java.awt.*;
import java.io.File;

public class HeaderController {
    private AppController appController;
    @FXML  private Label filePath;
    @FXML  private Button uploadFileButton;
    @FXML  private Button queueManagmentButton;
    @FXML private Label fileStatus;
    @FXML private Button displayDataBTN;

    public Button getQueueManagmentButton() {
        return queueManagmentButton;
    }

    public Button getUploadFileButton() {
        return uploadFileButton;
    }
    @FXML
    public void initialize(){
        //uploadFileButton.setStyle("-fx-background-color: -fx-shadow-highlight-color, -fx-outer-border, -fx-inner-border, -fx-body-color;");
        // Set the button's initial style
        uploadFileButton.setStyle(
                "-fx-font-size: 13px; " +
                        "-fx-font-weight: normal; " +
                        "-fx-padding: 10px 20px; " +
                        "-fx-border: 2px solid #333; " +
                        "-fx-background-color: rgba(119,200,203,0.66);"
        );

        // Add a mouse entered event handler to change the background color to pink when hovered
        uploadFileButton.setOnMouseEntered(event -> {
            uploadFileButton.setStyle(
                    "-fx-font-size: 13px; " +
                            "-fx-font-weight: normal; " +
                            "-fx-padding: 10px 20px; " +
                            "-fx-border: 2px solid #333; " +
                            "-fx-background-color: #d6ecee;" // Pink background color on hover
            );
        });

        // Add a mouse exited event handler to revert to the original style when the mouse leaves
        uploadFileButton.setOnMouseExited(event -> {
            uploadFileButton.setStyle(
                    "-fx-font-size: 13px; " +
                            "-fx-font-weight: normal; " +
                            "-fx-padding: 10px 20px; " +
                            "-fx-border: 2px solid #333; " +
                            "-fx-background-color: rgba(119,200,203,0.66);" // Original background color
            );
        });
    }

    public void displayFilePath(String path){
        filePath.setText(path);
    }

    public void readFile(File file, String path){
        try {
            appController.getWorld().readFile(file);
            filePath.setText(path);
            appController.setThreadsCount();

            disableQueueManagmentButton(false);
            appController.getBodyDetailsController().setDisplayDataBtnDisable(false);
            appController.clearDataInExecution();

            if(appController.isFileExist())
                appController.removeSimulationsHistory();

            appController.setFileExists();

        } catch (Exception e) {
            appController.showPopUpError(e.getMessage());
        }

    }
    @FXML
    public void readFile(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select XML file");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("xml files", "*.xml"));
        File selectedFile = fileChooser.showOpenDialog(appController.getPrimaryStage());
        if(selectedFile == null)
            return;

        String absolutePath = selectedFile.getAbsolutePath();
        readFile(selectedFile, absolutePath);
        if(appController.isFileExist())
            fileStatus.setText("File was read successfully!");
    }
    public void disableQueueManagmentButton(boolean toDisable){
        queueManagmentButton.setDisable(toDisable);
    }
    @FXML
    public void showQueueDetails(ActionEvent actionEvent){
        appController.showQueueManagementDetails();
    }
    public void setMainController(AppController appController) {
        this.appController = appController;
    }

    /*@FXML
    public void showDetails(ActionEvent actionEvent) {
        appController.showDetails();
    }*/

}
