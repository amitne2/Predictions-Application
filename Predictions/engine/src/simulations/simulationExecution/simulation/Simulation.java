package simulations.simulationExecution.simulation;

import EntityInstanceManager.EntityInstanceManager;
import EntityInstanceManager.EntityInstanceManagerImpl;
import action.Action;
import dto.EnvVariablesIncstanceDTO;
import dto.HistoryDataDTO.CountEntitiesDTO;
import dto.HistoryDataDTO.HistogramProperiesDTO;
import dto.SimulationDTO;
import dto.WorldDTO;
import entity.definition.EntityDefinition;
import entity.definition.EntityDefinitionImpl;
import entity.instance.EntityInstanceImpl;
import environment.EnvVariableManagerImpl;
import grid.GridInstances;
import grid.Position;
import rule.Rule;
import simulations.simulationExecution.TicksPopulation;
import termination.Termination;
import world.World;

import java.text.SimpleDateFormat;
import java.util.*;


public class Simulation {

    private World world;
    private EntityInstanceManagerImpl entityInstanceManager;
    private EnvVariableManagerImpl envVariablesManager;
    private GridInstances gridInstances;
    private int id;
    private String dateRun;
    private long currTick;
    private TerminationReason terminationReason;
    private boolean isPause;
    private Object pauseObject;
    private long startTime;
    private Termination termination;

    private HashMap<String, List<TicksPopulation>> entitiesCountPerTick;

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    enum TerminationReason {BY_TICKS, BY_SECOND, BY_USER};

    public Simulation(World world, Map<String, EnvVariablesIncstanceDTO> envVariablesIncstanceDTOList) throws Exception {
        this.world = world;
        this.termination = new Termination(world.getTermination());
        entityInstanceManager = new EntityInstanceManagerImpl();
        envVariablesManager = new EnvVariableManagerImpl();
        this.gridInstances = new GridInstances(world.getRowsGrid(), world.getColsGrid());
        prepareSimulator(world, envVariablesIncstanceDTOList);
        this.currTick = 1;
        this.isPause = false;
        this.pauseObject = new Object();
        this.entitiesCountPerTick = new HashMap<>();
    }


    public Simulation(){
        entityInstanceManager = new EntityInstanceManagerImpl();
        envVariablesManager = new EnvVariableManagerImpl();
        this.gridInstances = new GridInstances(world.getRowsGrid(), world.getColsGrid());
        this.currTick = 1;
    }


    public boolean isSimulationFinished(long startTimeMillis, long ticks) {
        boolean isFinished = false;

        if(termination.isTheUserCanStop()) {
            if(termination.isStopped()) {
                isFinished = true;
                terminationReason = TerminationReason.BY_USER;
            }
        }
         else if(termination.isTicksExist() && termination.isSecondsExist()) {
            if (termination.getTicksCount() <= ticks) {
                isFinished = true;
                terminationReason = TerminationReason.BY_TICKS;
            } else if ((System.currentTimeMillis() - startTimeMillis) / 1000 >= (long) (termination.getSecondsCount())) {
                isFinished = true;
                terminationReason = TerminationReason.BY_SECOND;
            }
        }

        else if(termination.isTicksExist()) {
            if (termination.getTicksCount() <= ticks) {
                isFinished = true;
                terminationReason = TerminationReason.BY_TICKS;
            }
        }
        else if(termination.isSecondsExist()) {
            if ((System.currentTimeMillis() - startTimeMillis) / 1000 >= (long) (termination.getSecondsCount())) {
                isFinished = true;
                terminationReason = TerminationReason.BY_SECOND;
            }
        }

        return isFinished;
    }

    public void prepareSimulator(World world, Map<String, EnvVariablesIncstanceDTO> environmentDataDTO) throws Exception {
        Random random = new Random();
        for (EntityDefinitionImpl entityDefinition : world.getEntityDefinitionList()) { //for each entity definition in world
            for(int i=0;i<entityDefinition.getPopulation();i++) { //create population instance for this entity definition
                int row = random.nextInt(world.getRowsGrid());
                int col = random.nextInt(world.getColsGrid());
                while(!(gridInstances.isThisPositionFree(row, col))) {
                     row = random.nextInt(world.getRowsGrid());
                     col = random.nextInt(world.getColsGrid());
                }
                EntityInstanceImpl entityInstance = entityInstanceManager.create(entityDefinition, row, col);
                gridInstances.setInstanceOnLocation(entityInstance);
            }
        }
        envVariablesManager.createEnvInstances(world.getEnvVariablesDefinition(), environmentDataDTO);
    }

    public SimulationDTO startSimulation(World world, int id) throws Exception {
        this.world = world;
        dateRun = getDateFormat();
        this.id = id;
        //long startTimeMillis = System.currentTimeMillis();

        this.startTime = System.currentTimeMillis();
        while (!isSimulationFinished(startTime, currTick)) { //run until the simulation stops because time/ticks limit.
            synchronized (pauseObject) { //pause the simulation if the user click on pause
                while(isPause) {
                    try {
                        //System.out.println(pauseObject);
                        pauseObject.wait();
                    } catch (Exception e) {
                        throw new RuntimeException();
                    }
                }
            }
            // Move all instances on the screen
            for(List<EntityInstanceImpl> entityInstancesList: entityInstanceManager.getInstances().values()) {
                for (EntityInstanceImpl entityInstance : entityInstancesList) {
                    Position randomLocation = gridInstances.randomLocationForInstanceOnGrid(entityInstance.getPositionOnGrid());
                    gridInstances.freeLocationOnGrid(entityInstance.getPositionOnGrid());
                    entityInstance.setLocationInGrid(randomLocation);
                    gridInstances.setInstanceOnLocation(entityInstance);
                }
            }
            if(entitiesCountPerTick.isEmpty())
                //create entities count per tick data for graph
            {
                for (String entityInstance: entityInstanceManager.getSimulationEntitiesProcess().keySet()) {
                    List<TicksPopulation> entityPopulation = new ArrayList<>();
                    entityPopulation.add(new TicksPopulation(0,entityInstanceManager.getCountOfEntityByName(entityInstance)));
                    entitiesCountPerTick.put(entityInstance, entityPopulation);
                }
            }

            List<Rule> activeRules = world.getActiveRules(currTick);
            for (String currEntityName : entityInstanceManager.getInstances().keySet()) {
                // go over all the entities from the same category
                for (EntityInstanceImpl currEntity : entityInstanceManager.getInstancesListByName(currEntityName)) {
                    // on every entity run all active rules
                    if (!currEntity.isNeedToBeKilled()) {
                        for (Rule currRule : activeRules) {
                            currRule.invokeRule(entityInstanceManager, envVariablesManager, currEntity, world, currTick, gridInstances);
                        }
                    }
                }
            }
                //remove the instance that need to be killed
            HashMap<String, List<EntityInstanceImpl>> entityInstancesCopy = new HashMap<>(entityInstanceManager.getInstances());
            for (String entityName: entityInstanceManager.getInstances().keySet()) {
                List<EntityInstanceImpl> entityInstanceList = entityInstanceManager.getInstancesListByName(entityName);
                for(int i=0;i<entityInstanceList.size();i++) {
                    if(entityInstanceList.get(i).isNeedToBeKilled()) {
                        entityInstancesCopy.get(entityName).remove(i);
                        i--; //update for the next delete
                    }
                }
            }
            entityInstanceManager.setInstances(entityInstancesCopy); //set the new list

            //update data for entities-ticks graph
            for (String entityName : entityInstanceManager.getInstances().keySet()) {
                int size = entitiesCountPerTick.get(entityName).size();
                if (entitiesCountPerTick.get(entityName).get(size - 1).getCountEntities() != entityInstanceManager.getCountOfEntityByName(entityName)) {
                    entitiesCountPerTick.get(entityName).add(new TicksPopulation(currTick, entityInstanceManager.getCountOfEntityByName(entityName)));
                } else {
                    int lastIndex = entitiesCountPerTick.get(entityName).size() - 1;
                    entitiesCountPerTick.get(entityName).get(lastIndex).setTick(currTick);
                }
            }
            currTick++;
        }

        //System.out.println("simulation ended " + id);
        //simulation ended
        WorldDTO worldDTO = world.createWorldDTO();
        SimulationDTO simulationDTO = new SimulationDTO(worldDTO, id, dateRun, terminationReason.toString());
        return simulationDTO;
    }

    private String getDateFormat() {
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy | HH.mm.ss");
        String formattedDate = dateFormat.format(currentDate);
        return formattedDate;
    }

    public int getId() {
        return id;
    }

    public String getDateRun() {
        return dateRun;
    }


   /* public CountEntitiesDTO createCountEntitiesHistory(int entityNum) {
        CountEntitiesDTO countEntitiesDTO = new CountEntitiesDTO();
        EntityDefinition entityDefinition = world.getEntity(entityNum);
            int before = entityDefinition.getPopulation();
            int after = entityInstanceManager.getCountOfInstances();
            String name = world.getEntity(0).getName();
            countEntitiesDTO.putEntityData(name, before, after);
        return  countEntitiesDTO;
    }*/

    public HistogramProperiesDTO getHistogramProperiesDTO(int entityNum) {
        return entityInstanceManager.createHistogramPropertiesDTO(entityNum);

    }
    public World getWorld() { return  world; }

    public void setDateRun(String dateRun) {
        this.dateRun = dateRun;
    }

    public long getCurrTick(){
        return currTick;
    }

    public HashMap<String, Integer> getSimulationEntitiesProcess(){
        return entityInstanceManager.getSimulationEntitiesProcess();
    }
    public boolean isPause() {
        return isPause;
    }
    public Object getPauseObject() {
        return pauseObject;
    }
    public void userStopped() {
        termination.setUserStopped();
    }

    public void pauseSimulation() {
        this.isPause = true;
    }

    public void resumeSimulation() {
        this.isPause = false;
    }

    public List<TicksPopulation> getEntitiesCountPerTick(String name){
        return entitiesCountPerTick.get(name);
    }

    public EntityInstanceManagerImpl getEntityInstanceManager() { return entityInstanceManager;}
    public String getTerminationReason() {
        if(terminationReason == TerminationReason.BY_SECOND) {
            return "Termination: BY_SECONDS";
        } else if (terminationReason == TerminationReason.BY_USER)
            return "Termination: BY_USER";
        else if(terminationReason == TerminationReason.BY_TICKS)
            return "Termination: BY_TICKS";
        return null;
    }

    public EnvVariableManagerImpl getEnvVariablesManager() {
        return envVariablesManager;
    }
}

