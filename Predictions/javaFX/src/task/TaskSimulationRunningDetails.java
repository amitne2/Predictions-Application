package task;

import body.results.ResultsBodyController;
import body.results.fillPopulation.itemInTable;
import dto.taskDetails.RunningDataSimulationDTO;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import simulations.simulationExecution.SimulationExecution;
import simulations.simulationExecution.Status;
import simulations.simulationExecution.TicksPopulation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TaskSimulationRunningDetails implements Runnable {

    private SimulationExecution simulationExecution;
    private SimpleLongProperty propertyCurrTick;
    private SimpleLongProperty runningTimeProperty;
    private TableView<itemInTable> entitiesDetailsTable; //population entity table
    private Map<String, SimpleIntegerProperty> propertyMap;
    private VBox simulationProgressDetails; //simulation id details
    private Button stopButton;
    private Button pauseButton;
    private Button resumeButton;
    private ResultsBodyController controller;
    private Label terminationLabel;
    private  Label errorSimulationLabel;
    private Button startNewRunButton;
    public TaskSimulationRunningDetails(SimulationExecution simulationExecution, SimpleLongProperty propertyCurrTick,
                                        SimpleLongProperty runningTimeProperty, TableView<itemInTable> entitiesDetailsTable,
                                        Map<String, SimpleIntegerProperty> propertyMap, VBox simulationProgressDetails,
                                        Button stopButton, Button pauseButton, Button resumeButton, ResultsBodyController controller,
                                        Label terminationLabel, Label errorSimulationLabel, Button startNewRunButton) {
         this.simulationExecution = simulationExecution;
         this.propertyCurrTick = propertyCurrTick;
         this.runningTimeProperty = runningTimeProperty;
         this.entitiesDetailsTable = entitiesDetailsTable;
         this.propertyMap = propertyMap;
         this.simulationProgressDetails = simulationProgressDetails;
         this.stopButton = stopButton;
         this.pauseButton = pauseButton;
         this.resumeButton = resumeButton;
         this.controller = controller;
         this.terminationLabel = terminationLabel;
         this.errorSimulationLabel = errorSimulationLabel;
         this.startNewRunButton= startNewRunButton;
     }

    @Override
    public void run() {
        Status status;
        boolean isSelected;
        do {
            status = simulationExecution.getStatus();
            isSelected = simulationExecution.isSelected();
            RunningDataSimulationDTO dto = simulationExecution.getDTOdata();
            Status finalStatus = status;

            Platform.runLater(() -> {
                if(finalStatus !=Status.FINISH && finalStatus !=Status.FINISH_WITH_ERROR) {
                    terminationLabel.setText("");
                    errorSimulationLabel.setText("");
                    startNewRunButton.setDisable(true);
                    pauseButton.setDisable(false);
                    controller.getDiaplayPropertiesDetailsVbox().getChildren().clear();
                    controller.getSelectEntityToDisplayData().getChildren().clear();

                    if(simulationExecution.getSimulation().isPause())
                        resumeButton.setDisable(false);
                    else
                        resumeButton.setDisable(true);

                    stopButton.setDisable(false);
                    controller.setLineChartVisible(false);

                    runningTimeProperty.set(dto.getRunningInSeconds());
                    propertyCurrTick.set(dto.getTick());

                    for (itemInTable row : entitiesDetailsTable.getItems()) {
                        int listSize = dto.getEntitiesCountDetails().get(row.getEntityName());
                        if (listSize > 0) {
                            Integer newPopulation = dto.getEntitiesCountDetails().get(row.getEntityName());
                            propertyMap.get(row.getEntityName()).setValue(newPopulation);
                        }
                        entitiesDetailsTable.refresh();
                    }
                }
            });
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                //handle
            }

        } while (status != Status.FINISH && status != Status.FINISH_WITH_ERROR && isSelected);

        if(simulationExecution.getStatus() == Status.FINISH_WITH_ERROR) {
            Platform.runLater(()-> {
                Label labelStatus = (Label) ((HBox)simulationProgressDetails.getChildren().get(simulationExecution.getId()-1)).getChildren().get(1);
                labelStatus.setText(" ERROR");
                labelStatus.setStyle("-fx-text-fill:rgba(227,13,13,0.88)");
                errorSimulationLabel.setText(simulationExecution.getErrorMessage());
                terminationLabel.setText("FINISH WITH ERROR");
                terminationLabel.setStyle("-fx-text-fill:#da151c");
                stopButton.setDisable(true);
                pauseButton.setDisable(true);
                resumeButton.setDisable(true);
            });
            }

        else if(simulationExecution.getStatus() == Status.FINISH) {
            // Create a data series for graph
            List <XYChart.Series<Number, Number>> dataSeriesEntities = new ArrayList<>();
            for(String entityName: simulationExecution.getSimulation().getEntityInstanceManager().getInstances().keySet()) {
                XYChart.Series<Number, Number> dataSeries = new XYChart.Series<>();
                dataSeries.setName(entityName);
                dataSeriesEntities.add(dataSeries);
            }
                // Add data points to the series
                for (XYChart.Series<Number, Number> dataSeries : dataSeriesEntities) {
                    String entity = dataSeries.getName();
                    List<TicksPopulation> s = controller.getCurrSimulation().getSimulation().getEntitiesCountPerTick(entity);

                    for (TicksPopulation ticksPopulation : s) {
                        Integer countentities = ticksPopulation.getCountEntities();
                        XYChart.Data<Number, Number> dataPoint = new XYChart.Data<>(ticksPopulation.getTick(), countentities);
                        //dataSeries.getData().add(new XYChart.Data<>(ticksPopulation.getTick(),countentities));

                        dataSeries.getData().add(dataPoint);
                    }
                }

            Platform.runLater(()-> {
                Label labelStatus = (Label) ((HBox)simulationProgressDetails.getChildren().get(simulationExecution.getId()-1)).getChildren().get(1);
                labelStatus.setText(" FINISH");
                labelStatus.setStyle("-fx-text-fill:GREEN");
                stopButton.setDisable(true);
                pauseButton.setDisable(true);
                resumeButton.setDisable(true);
                startNewRunButton.setDisable(false);
                terminationLabel.setText(simulationExecution.getSimulation().getTerminationReason()); //set termination reason
                terminationLabel.setStyle("-fx-text-fill:#15da15");
                controller.createGraphForDetails(dataSeriesEntities);
                controller.createSelectEntitiesPropertiesDisplay(simulationExecution);
            });
        }
        //System.out.println("TaskSimulationDetails END");
    }
}
