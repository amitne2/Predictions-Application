package task;

import simulations.simulationExecution.SimulationExecution;
import simulations.simulationExecution.Status;

public class TaskSimulationStop implements Runnable {

    private SimulationExecution simulationExecution;

    public TaskSimulationStop(SimulationExecution simulationExecution) {
        this.simulationExecution = simulationExecution;
    }

    @Override
    public void run() {
        do {
            if(simulationExecution.getSimulation().isPause()) {
                synchronized (simulationExecution.getSimulation().getPauseObject()) {
                    simulationExecution.getSimulation().getPauseObject().notifyAll();
                }
            }
            simulationExecution.userStopped();
        }
        while (simulationExecution.getStatus() != Status.FINISH);
    }
}
