package task;

import simulations.simulationExecution.SimulationExecution;

public class TaskSimulationResume implements Runnable{
    private int id;
    private SimulationExecution simulation;

    public TaskSimulationResume(int id, SimulationExecution simulation) {
        this.id= id;
        this.simulation = simulation;
    }
    @Override
    public void run() {
        do {
            if(simulation.getSimulation().isPause()) {
                synchronized (simulation.getSimulation().getPauseObject()) {
                    simulation.setStartTimeAfterPause();
                    simulation.getSimulation().resumeSimulation();
                    simulation.getSimulation().getPauseObject().notifyAll();
                }
            }
        } while (simulation.getSimulation().isPause());
    }
}
