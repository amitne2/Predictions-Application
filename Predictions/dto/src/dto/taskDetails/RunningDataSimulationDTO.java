package dto.taskDetails;

import simulations.simulationExecution.simulation.Simulation;

import java.util.HashMap;
import java.util.Map;

public class RunningDataSimulationDTO {

    private long tick;
    private long runningInSeconds;
    private HashMap<String, Integer> entitiesCountDetails;

    public RunningDataSimulationDTO(long tick, long runningInSeconds, HashMap<String, Integer> entitiesCountDetails) {
        this.tick= tick;
        this.runningInSeconds = runningInSeconds;
        this.entitiesCountDetails = entitiesCountDetails;
    }

    public long getTick() {
        return tick;
    }

    public long getRunningInSeconds() {
        return runningInSeconds;
    }

    public HashMap<String, Integer> getEntitiesCountDetails() {
        return entitiesCountDetails;
    }
}
