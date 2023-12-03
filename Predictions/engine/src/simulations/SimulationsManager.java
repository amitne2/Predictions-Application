package simulations;

import dto.*;
import dto.HistoryDataDTO.HistogramProperiesDTO;
import dto.HistoryDataDTO.HistoryDataDTO;
import simulations.simulationExecution.SimulationExecution;
import simulations.simulationExecution.Status;
import simulations.simulationTask.RunTask;
import simulations.simulationExecution.simulation.Simulation;
import world.World;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SimulationsManager {
    private List<Simulation> simulationsDetails;
    private HashMap<Integer, SimulationExecution> simulationExecutionData;
    private ExecutorService executorService;
    private int IDcount;
    public SimulationsManager(){
        simulationsDetails = new ArrayList<>();
        simulationExecutionData = new HashMap<>();
        IDcount = 1;
    }

    public void clearData() {
        IDcount =1;
        simulationExecutionData = new HashMap<>();
        simulationsDetails = new ArrayList<>();
    }

    public List<Simulation> getSimulationsDetails() {
        return simulationsDetails;
    }

    public SimulationDTO start(World world, Map<String, EnvVariablesIncstanceDTO> envVariablesInstanceDTOList) throws Exception {
        Simulation simulation = new Simulation(world, envVariablesInstanceDTOList);
        SimulationDTO simulationDTO = null;
        try {
            SimulationExecution simulationExecution = new SimulationExecution(IDcount, simulation); //create simulation execution
            simulationExecutionData.put(IDcount, simulationExecution); //add to manager
            RunTask runTask = new RunTask(IDcount, simulation, simulationExecution);
            executorService.execute(runTask); //add to threads pool
            IDcount++; //increase the id after run simulation

        } catch (Exception e) {
            System.out.println("An error was happened doing running the simulation: " + e.getMessage());
        }
        return  simulationDTO;
    }

   /* public SimulationDTO start(World world, Map<String, EnvVariablesIncstanceDTO> envVariablesIncstanceDTOList) {
        Simulation simulation = new Simulation(world);
        SimulationDTO simulationDTO = null;
        try {
            simulationDTO= simulation.startSimulation(world, envVariablesIncstanceDTOList);
            simulationsDetails.add(simulation);
            System.out.println("Simulator "  + simulation.getDateRun() + " ended successfully!");

        } catch (Exception e) {
            System.out.println("An error was happened doing running the simulation: " + e.getMessage());
            //System.out.println("Error while running Simulation.");
        }
        return  simulationDTO;
    }*/

    public SimulationsDTO createSimulationsDTO() {
        Map <String, Integer> simulationsDTOdetails = new HashMap<>();

        for(int i=0;i<simulationsDetails.size();i++) {
            int id = simulationsDetails.get(i).getId();
            String date = simulationsDetails.get(i).getDateRun();
            simulationsDTOdetails.put(date, id);
        }
        SimulationsDTO simulationsDTO = new SimulationsDTO(simulationsDTOdetails);
        return simulationsDTO;
    }

    public HistoryDataDTO createSimulationDTOHistory(int userSimulationChoose, int choose, int entityNum) {
        Simulation simulation = simulationsDetails.get(userSimulationChoose-1);
        switch (choose){

            case 1: // count entities
                //CountEntitiesDTO countEntitiesDTO = simulation.createCountEntitiesHistory(entityNum);
                //return countEntitiesDTO;
                return null;
            case 2:
                HistogramProperiesDTO histogramProperiesDTO = simulation.getHistogramProperiesDTO(entityNum);
                return histogramProperiesDTO;
            default:
                return null;
        }
    }

    public int getNumOfSimulations(){
        return simulationsDetails.size();
    }

    public void removeAllSimulations() {
       simulationsDetails = new ArrayList<>();
    }

    public void createThreadsPool(int numThreads) {
        executorService = Executors.newFixedThreadPool(numThreads);
    }

    public ExecutorService getExecutorService() {
        return executorService;
    }

    public int getCurrID() {
        return IDcount;
    }

    public SimulationExecution getSimulationByID(int id) {
        return simulationExecutionData.get(id);
    }

    public Status getSimulationStatusByID(int id) {
        return simulationExecutionData.get(id).getStatus();
    }

    public void setIsSelected(int id, boolean isSelected) {
        simulationExecutionData.get(id).setSelected(isSelected);
    }

    public HashMap<Integer, SimulationExecution> getSimulationExecutionData() {
        return simulationExecutionData;
    }
}
