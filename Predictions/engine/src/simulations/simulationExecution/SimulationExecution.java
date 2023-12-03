package simulations.simulationExecution;

import dto.FinishSimulationDataDTO;
import dto.taskDetails.RunningDataSimulationDTO;
import simulations.simulationExecution.simulation.Simulation;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class SimulationExecution {

    private Simulation simulation;
    private int id;
    private boolean isSelected;
    private Status status;
    private long startTime;
    private  long pauseTime;
    private FinishSimulationDataDTO finishSimulationDataDTO;
    private String errorMessage;

    private long currSecondsPassed; //stay the time as before when isPause is true

    public SimulationExecution(int id, Simulation simulation) {
        this.id = id;
        this.simulation = simulation;
        this.status = Status.NOT_START;
        this.isSelected = false;
        this.startTime = System.currentTimeMillis();
        this.pauseTime = 0;
    }
public void setSimulationStarted(){
        this.status = Status.IN_PROGRESS;
}
    public void setSimulationFinished() {
        this.status = Status.FINISH;
    }
    public void setSimulationFinishedWithError(String errorMessage) {
        this.status = Status.FINISH_WITH_ERROR;
        this.errorMessage = errorMessage;
    }

    public Status getStatus(){
        return status;
    }

    private String getDateFormat() {
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy | HH.mm.ss");
        String formattedDate = dateFormat.format(currentDate);
        return formattedDate;
    }
    public RunningDataSimulationDTO getDTOdata() {
        HashMap<String, Integer> entitiesData = getEntitiesProcess();

        // Calculate the time difference in milliseconds
        if(!(simulation.isPause()))
             this.currSecondsPassed = System.currentTimeMillis() - startTime;

        // Convert milliseconds to seconds
        long timeDifferenceSeconds = this.currSecondsPassed / 1000;

        RunningDataSimulationDTO dto = new RunningDataSimulationDTO(simulation.getCurrTick(), timeDifferenceSeconds, entitiesData);
        return dto;

    }

    public HashMap<String, Integer> getEntitiesProcess(){
        return  simulation.getSimulationEntitiesProcess();
    }

    public boolean isSelected(){
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public void userStopped() {
        simulation.userStopped();
        this.status = Status.FINISH;
    }
    public Simulation getSimulation(){
        return simulation;
    }

    public int getId() {
        return id;
    }

    public void setSimulation(Simulation simulation) {
        this.simulation = simulation;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
    public void setPauseTime(){
        this.pauseTime = System.currentTimeMillis();
    }

    public void setStartTimeAfterPause() {
        this.startTime = startTime + ((System.currentTimeMillis() - pauseTime));
        simulation.setStartTime(startTime);
        this.pauseTime = 0;
    }

    public FinishSimulationDataDTO getFinishSimulationDataDTO() {
        return finishSimulationDataDTO;
    }

    public void createFinishSimulationDataDTO() {
        this.finishSimulationDataDTO = new FinishSimulationDataDTO();
    }

    public String getErrorMessage(){ return errorMessage;}
}
