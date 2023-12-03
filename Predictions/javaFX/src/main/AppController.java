package main;

import body.details.BodyDetailsController;
import body.newExecution.ExecutionBodyController;
import body.results.ResultsBodyController;
import dto.EnvVariablesIncstanceDTO;
import dto.PropertyDTO;
import dto.SimulationDTO;
import dto.WorldDTO;
import header.HeaderController;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;

import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import simulations.SimulationsManager;
import simulations.simulationExecution.SimulationExecution;
import simulations.simulationExecution.Status;
import world.World;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

public class AppController {
    private World world;
    private Map<String, EnvVariablesIncstanceDTO> envVariablesInstanceDTOList;
    private SimulationsManager simulationsManager;
    private Stage primaryStage;
    private SimpleBooleanProperty isFileExist;

    @FXML private Pane headerComponent;
    @FXML private HeaderController headerComponentController;
    @FXML private ScrollPane AppComponent;

    @FXML private Pane detailsTabComponent;
    @FXML private Pane newExecutionTabComponent;
    @FXML private Pane resultsTabComponent;
    @FXML  private BodyDetailsController detailsTabComponentController;
    @FXML private ExecutionBodyController newExecutionTabComponentController;
    @FXML private ResultsBodyController resultsTabComponentController;

    @FXML private TabPane tabPane;

    private SimulationExecution currSimulation;

    @FXML
    public void initialize() {
        if (headerComponentController != null && detailsTabComponentController != null &&
                newExecutionTabComponentController != null && resultsTabComponentController != null) {
            headerComponentController.setMainController(this);
            detailsTabComponentController.setMainController(this);
            newExecutionTabComponentController.setMainController(this);
            resultsTabComponentController.setMainController(this);
            resultsTabComponent.disableProperty().bind(isFileExist.not());
            newExecutionTabComponent.disableProperty().bind(isFileExist.not());
            headerComponentController.disableQueueManagmentButton(true);
            detailsTabComponentController.setDisplayDataBtnDisable(true);
        }
    }

    public AppController() {
        world = new World();
        isFileExist = new SimpleBooleanProperty(false);
        envVariablesInstanceDTOList = new HashMap<>();
        simulationsManager = new SimulationsManager();
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public boolean isFileExist() {
        return isFileExist.get();
    }

    public void setFileExists() {
        this.isFileExist.set(true);
    }

    public void setMainComponent(ScrollPane mainComponent) {
        this.AppComponent = mainComponent;
    }

    public World getWorld() {
        return world;
    }

    public WorldDTO getWorldDTO() {
        WorldDTO worldDTO = world.createWorldDTO();
        return worldDTO;
    }
    public void showPopUpError(String errorMsg) {
        Stage popUpWindow = new Stage();

        popUpWindow.initModality(Modality.APPLICATION_MODAL);
        popUpWindow.setTitle("Error popup");

        Label label1 = new Label(errorMsg);
        Button button1 = new Button("Close");

        button1.setOnAction(e -> popUpWindow.close());
        ScrollPane layout = new ScrollPane();
        VBox vbox = new VBox(120);
        vbox.getChildren().addAll(label1, button1);
        vbox.setAlignment(Pos.CENTER);
        layout.setContent(vbox);
        layout.setPrefWidth(500);

        Scene scene1 = new Scene(layout);
        popUpWindow.setScene(scene1);
        popUpWindow.showAndWait();
    }

    public void setPopulation(String entityName, int population) {
        world.setPopulation(entityName, population);
    }

    public void setEnvValue(PropertyDTO envProperty, Object value, boolean wasRandom) {
        EnvVariablesIncstanceDTO envVariablesIncstanceDTO = new EnvVariablesIncstanceDTO(envProperty.getName(), envProperty.getType(), envProperty.isExistRange(),
                envProperty.getFromRange(), envProperty.getToRange(), value, wasRandom);
        envVariablesInstanceDTOList.put(envProperty.getName(), envVariablesIncstanceDTO);
    }

    public void moveToResultScreen() {
        tabPane.getSelectionModel().select(2);
    }

    public void moveToNewExecutionScreen(){
        tabPane.getSelectionModel().select(1);
        newExecutionTabComponentController.getPrepareButton().setDisable(false);
    }

    public void moveToDetails(){
        tabPane.getSelectionModel().select(0);
    }


    public BodyDetailsController getBodyDetailsController() {
        return detailsTabComponentController;
    }
    public ExecutionBodyController getNewExecutionTabComponentController(){
        return newExecutionTabComponentController;
    }

    public void setThreadsCount() {
        int numThreads = world.getThreadsCount();
        simulationsManager.createThreadsPool(numThreads);
    }

    public int addSimulationToThreadsPool() throws Exception {
        int id = simulationsManager.getCurrID();
        simulationsManager.start(world, envVariablesInstanceDTOList);
        return id;
    }

    public void showQueueManagementDetails() {
        ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) simulationsManager.getExecutorService();
        Stage popUpWindow = new Stage();

        popUpWindow.initModality(Modality.APPLICATION_MODAL);
        popUpWindow.setTitle("Queue management details");

        // Get details
        int activeCount = threadPoolExecutor.getActiveCount();
        long completedTaskCount = threadPoolExecutor.getCompletedTaskCount();
        long QueueCount = threadPoolExecutor.getTaskCount();

        Label label1 = new Label("Amount of simulations waiting to be executed: " + QueueCount);
        Label label2 = new Label("Simulations currently running: " + activeCount);
        Label label3 = new Label("Simulations that have finished running: " + completedTaskCount);
        Button button1 = new Button("Close");

        button1.setOnAction(e -> popUpWindow.close());
        ScrollPane layout = new ScrollPane();
        VBox vbox = new VBox(10);
        vbox.getChildren().addAll(label1, label2, label3, button1);
        vbox.setAlignment(Pos.CENTER);
        layout.setContent(vbox);
        layout.setPrefWidth(500);

        Scene scene1 = new Scene(layout);
        popUpWindow.setScene(scene1);
        popUpWindow.showAndWait();
    }

    public void runSimulation() throws Exception {
        int id = addSimulationToThreadsPool();
        resultsTabComponentController.getSimulationProgressDetails().
                getChildren().add(resultsTabComponentController.createSimulationHBox(id));
    }

    public void removeSimulationsHistory(){
        simulationsManager.clearData();
        resultsTabComponentController.getSimulationProgressDetails().getChildren().clear();
        resultsTabComponentController.removeSimulationsForNewFile(resultsTabComponent);
        moveToDetails();
        detailsTabComponentController.getInformationDetails().getChildren().removeAll();
    }

public SimulationExecution getSimulationByID(int id) {
        return simulationsManager.getSimulationByID(id);
}
public Status getSimulationStatus(String item) {
    try {
        int id = Integer.parseInt(item);
        return  simulationsManager.getSimulationStatusByID(id);

    } catch (Exception e) {
        return null;
    }
}


/*    public void changeSkin(String skin) {
        Scene scene =  primaryStage.getScene();
        scene.getStylesheets().clear();
        switch (skin) {
            case "default":
                scene.getStylesheets().add(
                        getClass().getResource("/main/main.css").toExternalForm());
                break;
            case "ugly watermelon":
                scene.getStylesheets().add(
                        getClass().getResource("/main/uglyWatermelon.css").toExternalForm());
                break;
            case "the Beatles":
                scene.getStylesheets().add(
                        getClass().getResource("/main/theBeatles.css").toExternalForm());
                break;
        }

    }*/

    public void setAllSelectedToFalse() {
        for(int id: simulationsManager.getSimulationExecutionData().keySet()) {
            simulationsManager.setIsSelected(id, false);
        }
    }
    public  void setCurrentSimulation(int id) {
        this.currSimulation = simulationsManager.getSimulationByID(id);
    }
    public SimulationExecution getCurrSimulation() { return currSimulation; }

    public void clearDataInExecution() {
        newExecutionTabComponentController.clearData();
    }

    public ResultsBodyController getResultsTabComponentController() {
        return resultsTabComponentController;
    }
}
