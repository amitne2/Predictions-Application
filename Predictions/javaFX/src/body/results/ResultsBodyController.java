package body.results;

import body.results.fillPopulation.itemInTable;
import dto.EntityDefPropertyDTO;
import dto.EntityDefinitionDTO;
import dto.FinishSimulationDataDTO;
import dto.WorldDTO;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Pair;
import main.AppController;
import simulations.simulationExecution.SimulationExecution;
import simulations.simulationExecution.Status;
import task.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResultsBodyController {

    private AppController appController;
    private boolean firstTime;
    private TableView<itemInTable> entitiesDetailsTable; //entity population table
    private SimpleLongProperty currTickProperty;
    private SimpleLongProperty runningTimeProperty;
    private Map<String, SimpleIntegerProperty> propertyMap;
    @FXML private VBox tableInformation; //table entity population details
    @FXML private Label currTickLabel; //ticks label
    @FXML private Label currSecondsLabel; //seconds label
    @FXML private VBox simulationProgressDetails; //simulation ID's details
    @FXML private Button stopButton;
    @FXML private Button pauseButton;
    @FXML private Button resumeButton;
    @FXML private LineChart<Number, Number> lineChart;
    @FXML private VBox selectEntityToDisplayData;
    @FXML private VBox diaplayPropertiesDetailsVbox;
    @FXML private Label terminationByLabel;
    @FXML private Label errorSimulationLabel;
    @FXML private Button startNewRunButton;

    public ResultsBodyController() {
        firstTime = false;
        this.entitiesDetailsTable = new TableView<>();
        currTickProperty = new SimpleLongProperty();
        runningTimeProperty = new SimpleLongProperty();
        this.propertyMap = new HashMap<>();
    }

    public void setMainController(AppController appController) {
        this.appController = appController;
    }

    @FXML
    public void initialize() {
        currTickLabel.textProperty().bind(Bindings.format("%,d", currTickProperty));
        currSecondsLabel.textProperty().bind(Bindings.format("%,d", runningTimeProperty));
        lineChart.setVisible(false);
        startNewRunButton.setDisable(true);
        pauseButton.setStyle("-fx-background-color: rgba(80,192,51,0.88);");
        // Add a mouse entered event handler to change the background color to pink when hovered
        pauseButton.setOnMouseEntered(event -> {
            pauseButton.setStyle(
                    "-fx-background-color: #93dc89;" // Pink background color on hover
            );
        });

        // Add a mouse exited event handler to revert to the original style when the mouse leaves
        pauseButton.setOnMouseExited(event -> {
            pauseButton.setStyle(
                    "-fx-border: 2px solid #333; " +
                            "-fx-background-color: rgba(80,192,51,0.88);" // Original background color
            );
        });

        stopButton.setStyle("-fx-background-color: rgba(227,13,13,0.88);");

        // Add a mouse entered event handler to change the background color to pink when hovered
        stopButton.setOnMouseEntered(event -> {
            stopButton.setStyle(
                    "-fx-background-color: #e05f64;" // Pink background color on hover
            );
        });

        // Add a mouse exited event handler to revert to the original style when the mouse leaves
        stopButton.setOnMouseExited(event -> {
            stopButton.setStyle(
                    "-fx-border: 2px solid #333; " +
                            "-fx-background-color: rgba(227,13,13,0.88);" // Original background color
            );
        });


        resumeButton.setStyle("-fx-background-color: rgba(241,221,7,0.88);");
        // Add a mouse entered event handler to change the background color to pink when hovered
        resumeButton.setOnMouseEntered(event -> {
            resumeButton.setStyle(
                    "-fx-background-color: #eade8a;" // Pink background color on hover
            );
        });

        // Add a mouse exited event handler to revert to the original style when the mouse leaves
        resumeButton.setOnMouseExited(event -> {
            resumeButton.setStyle(
                    "-fx-border: 2px solid #333; " +
                            "-fx-background-color: rgba(241,221,7,0.88);" // Original background color
            );
        });
        resumeButton.setDisable(true);
    }

    public void addEntityToTable() {
        HashMap<String, Integer> entityMap = appController.getCurrSimulation().getEntitiesProcess();

        propertyMap.clear();
        for (String entityName : entityMap.keySet()) {
            propertyMap.put(entityName, new SimpleIntegerProperty());
        }

        if (this.entitiesDetailsTable.getColumns().isEmpty()) {
            TableColumn entityName = new TableColumn<itemInTable, String>("Entity Name");
            entityName.setCellValueFactory(new PropertyValueFactory<>("entityName"));
            TableColumn population = new TableColumn<itemInTable, Integer>("Population");
            population.setCellValueFactory(new PropertyValueFactory<>("population"));

            this.entitiesDetailsTable.getColumns().add(entityName);
            this.entitiesDetailsTable.getColumns().add(population);
            this.entitiesDetailsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
            tableInformation.getChildren().add(entitiesDetailsTable);
        } else {
            this.entitiesDetailsTable.getItems().clear();
        }

        for (EntityDefinitionDTO currEntityName : appController.getWorldDTO().getEntityDefinitionDTOList()) {
            itemInTable row = new itemInTable(currEntityName.getName());
            if(propertyMap.get(currEntityName.getName())!= null)
                row.poIntegerProperty().bind(propertyMap.get(currEntityName.getName()));
            this.entitiesDetailsTable.getItems().add(row);
        }
    }

    @FXML
    public void pauseOnclick(ActionEvent event) {
        int id = appController.getCurrSimulation().getId();
        new Thread(() -> {
            TaskSimulationPause task = new TaskSimulationPause(appController.getCurrSimulation(), resumeButton);
            task.run();
        }).start();
    }

    @FXML
    public void stopOnClick(ActionEvent actionEvent) {
        new Thread(() -> {
            TaskSimulationStop task = new TaskSimulationStop(appController.getCurrSimulation());
            task.run();
        }).start();
    }

    @FXML
    public void resumeOnClick(ActionEvent actionEvent) {
        int id = appController.getCurrSimulation().getId();
        new Thread(() -> {
            TaskSimulationResume task = new TaskSimulationResume(id, appController.getCurrSimulation());
            task.run();
        }).start();
    }

    public HBox createSimulationHBox(int id) {
        Label labelSimulationId = new Label(String.valueOf(id));
        Label labelSimulationStatus = new Label(" IN PROCESS");
        labelSimulationStatus.setStyle("-fx-text-fill:#e0b306");
        HBox dynamicHbox = new HBox();
        dynamicHbox.getChildren().addAll(labelSimulationId, labelSimulationStatus);

        EventHandler<MouseEvent> HBoxClickHandler = event -> {

            HBox clicked = (HBox) event.getSource();
            int index = simulationProgressDetails.getChildren().indexOf(clicked);

            //set this simulation on the current simulation
            appController.setCurrentSimulation(index + 1);
            appController.setAllSelectedToFalse();
            appController.getCurrSimulation().setSelected(true);
            if (appController.getCurrSimulation().getStatus() == Status.FINISH_WITH_ERROR)
                this.entitiesDetailsTable.getItems().clear();
            else
                    addEntityToTable();

            new Thread(new TaskSimulationRunningDetails(appController.getCurrSimulation(), currTickProperty, runningTimeProperty,
                    entitiesDetailsTable, propertyMap, simulationProgressDetails, stopButton, pauseButton, resumeButton, this,
                    terminationByLabel, errorSimulationLabel, startNewRunButton)).start();
        };
        dynamicHbox.setOnMouseClicked(HBoxClickHandler);
        return dynamicHbox;
    }

    public VBox getSimulationProgressDetails() {
        return simulationProgressDetails;
    }

    public void createGraphForDetails(List<XYChart.Series<Number, Number>> dataSeriesList) {
        setLineChartVisible(true);
        lineChart.getData().clear();
        for(XYChart.Series<Number, Number> dataSeries: dataSeriesList) {
            lineChart.getData().add(dataSeries);
        }
    }

    public VBox getSelectEntityToDisplayData() {
        return selectEntityToDisplayData;
    }

    public VBox getDiaplayPropertiesDetailsVbox() {
        return diaplayPropertiesDetailsVbox;
    }

    public void setLineChartVisible(boolean visible) {
        this.lineChart.setVisible(visible);
    }

    public SimulationExecution getCurrSimulation() {
        return appController.getCurrSimulation();
    }

    public void createEntitiesData() {
        WorldDTO worldDTO = appController.getWorldDTO();

    }

    public void createSelectEntitiesPropertiesDisplay(SimulationExecution simulationExecution) {
        selectEntityToDisplayData.getChildren().clear();
        WorldDTO worldDTO = appController.getWorldDTO();
        List<Pair<String, String>> enterData = new ArrayList<>();
        for(String entityName: simulationExecution.getSimulation().getEntityInstanceManager().getInstances().keySet()) {
            if (simulationExecution.getSimulation().getEntityInstanceManager().getInstancesListByName(entityName).size() != 0) {
                for (EntityDefPropertyDTO propertyDTO : worldDTO.getEntity(entityName).getProperties()) {
                    Label data = new Label(entityName + "-->" + propertyDTO.getName());
                    data.setStyle("-fx-text-fill:#d523bd");
                    HBox dynamicHbox = new HBox();
                    dynamicHbox.getChildren().add(data);
                    enterData.add(new Pair<>(entityName, propertyDTO.getName()));

                    EventHandler<MouseEvent> HBoxClickHandler = event -> {
                        HBox clicked = (HBox) event.getSource();
                        int index = selectEntityToDisplayData.getChildren().indexOf(clicked);
                        Pair<String, String> clickedProperty = enterData.get(index);
                        simulationExecution.getFinishSimulationDataDTO().setDataForProperty(simulationExecution.getSimulation(),
                                clickedProperty.getKey(), clickedProperty.getValue(), simulationExecution.getSimulation().getCurrTick());
                        displayPropertyHistogram(simulationExecution.getFinishSimulationDataDTO());
                    };
                    dynamicHbox.setOnMouseClicked(HBoxClickHandler);
                    selectEntityToDisplayData.getChildren().add(dynamicHbox);
                }
            }
        }
    }

    private void displayPropertyHistogram(FinishSimulationDataDTO finishSimulationDataDTO) {
        diaplayPropertiesDetailsVbox.getChildren().clear();
        Label consistency;
        HBox dynamicHbox = new HBox();
        if(finishSimulationDataDTO.getConsistency() == 0)
            consistency = new Label("Consistency: Propery value hasn't changed.");
        else
            consistency = new Label("Consistency: " + finishSimulationDataDTO.getConsistency());
        consistency.setStyle("-fx-text-fill:#233bd5");
        dynamicHbox.getChildren().add(consistency);
        diaplayPropertiesDetailsVbox.getChildren().add(dynamicHbox);

        if(finishSimulationDataDTO.isNumber()) {
            HBox averageBox = new HBox();
            Label average = new Label("Average value of this property: " + finishSimulationDataDTO.getAverageValue());
            average.setStyle("-fx-text-fill:#233bd5");
            averageBox.getChildren().add(average);
            diaplayPropertiesDetailsVbox.getChildren().add(averageBox);
        }

        for(Object value: finishSimulationDataDTO.getPropertyHistogram().keySet()) {
            Label data = new Label("value: " + value + "--> count: " + finishSimulationDataDTO.getPropertyHistogram().get(value));
            data.setStyle("-fx-text-fill:#233bd5");
            HBox valueCountBox = new HBox();
            valueCountBox.getChildren().add(data);
            diaplayPropertiesDetailsVbox.getChildren().add(valueCountBox);
        }
    }

    @FXML
    public void startNewRunSimulation(){
        appController.moveToNewExecutionScreen();
        appController.clearDataInExecution();
        appController.getNewExecutionTabComponentController().reRunSimulation();
    }

    public void removeSimulationsForNewFile(Pane resultsTabComponent) {
        lineChart.getData().clear();
        entitiesDetailsTable.getItems().clear();
        selectEntityToDisplayData.getChildren().clear();
        diaplayPropertiesDetailsVbox.getChildren().clear();
        errorSimulationLabel.setText("");
        terminationByLabel.setText("");
        runningTimeProperty.set(0);
        currTickProperty.set(0);
    }
}


