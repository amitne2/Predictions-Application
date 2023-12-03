package dto;

import action.IncreaseAction;

import java.util.HashMap;
import java.util.Map;

public class SimulationsDTO {

    private Map<String, Integer> simulationsDateAndID;

    public SimulationsDTO(Map<String, Integer> simulationsDateAndID){
        this.simulationsDateAndID = simulationsDateAndID;
    }

    public Map<String, Integer> getSimulationsDateAndID(){
        return simulationsDateAndID;
    }



}
