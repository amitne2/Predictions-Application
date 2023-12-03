package simulations.simulationTask;

import dto.SimulationDTO;
import simulations.simulationExecution.SimulationExecution;
import simulations.simulationExecution.simulation.Simulation;

public class RunTask implements Runnable{
    private int id;
    private Simulation simulation;
    private SimulationExecution simulationExecution;


    public RunTask(int id, Simulation simulation, SimulationExecution simulationExecution) {
        this.id = id;
        this.simulation = simulation;
        this.simulationExecution = simulationExecution;
    }


    @Override
    public void run() {
        try {
            simulationExecution.setSimulationStarted();
            SimulationDTO simulationDTO =  this.simulation.startSimulation(simulation.getWorld(), id);
            //System.out.println(simulationExecution.getId());
            simulationExecution.setSimulationFinished();
            simulationExecution.createFinishSimulationDataDTO();

        } catch (Exception e) {
            simulationExecution.setSimulationFinishedWithError(e.getMessage());
        }
    }
}
