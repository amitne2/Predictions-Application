package dto;

import EntityInstanceManager.EntityInstanceManagerImpl;
import environment.EnvVariableManagerImpl;

public class SimulationDTO {

    private WorldDTO worldDTO;

    private int id;
    private String dateRun;
    private String terminationReason;
    private int countInstances;

    enum TerminationReason {BY_TICKS, BY_SECOND};

public SimulationDTO(WorldDTO worldDTO, int id, String dateRun, String terminationReason) {
    this.worldDTO = worldDTO;
    this.id = id;
    this.dateRun = dateRun;
    this.terminationReason = terminationReason;
    }

    public int getId() {
        return id;
    }

    public String getDateRun() {
        return dateRun;
    }

    public String getTerminationReason() {
        return terminationReason;
    }

    public WorldDTO getWorldDTO() {
        return worldDTO;
    }
}
