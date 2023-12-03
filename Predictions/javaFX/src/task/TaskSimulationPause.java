package task;

import javafx.application.Platform;
import javafx.scene.control.Button;
import simulations.simulationExecution.SimulationExecution;

public class TaskSimulationPause implements Runnable {
    private SimulationExecution simulation;
    private Button resumeButton;

    public TaskSimulationPause(SimulationExecution simulation, Button resumeButton) {
        this.simulation = simulation;
        this.resumeButton = resumeButton;
    }


    @Override
    public void run() {
        do {
            simulation.setPauseTime();
            simulation.getSimulation().pauseSimulation();
            Platform.runLater(()-> {
                resumeButton.setDisable(false);
            });
        } while (!(simulation.getSimulation().isPause()));
    }
}
