package dto;

import rule.Rule;
import termination.Termination;
import world.World;

import java.util.*;

public class WorldDTO {


    private HashMap<String, EntityDefinitionDTO> entityDefinitionDTOMap;

    private EnvVariableManagerDefinitionDTO envVariablesManager;
    private List<RuleDTO> rulesList;
    private TerminationDTO termination;
    private int gridRows;
    private int gridCols;
    public WorldDTO(HashMap<String, EntityDefinitionDTO> entityDefinitionDTOMap, EnvVariableManagerDefinitionDTO envVariableManagerDefinitionDTO,
                    List<RuleDTO> ruleDTOList, TerminationDTO terminationDTO, int gridRows, int gridCols) {
       // this.entity = entityDefinitionDTO;
        this.entityDefinitionDTOMap = new HashMap<>();
        this.entityDefinitionDTOMap= entityDefinitionDTOMap;
        this.envVariablesManager = envVariableManagerDefinitionDTO;
        this.rulesList = ruleDTOList;
        this.termination = terminationDTO;
        this.gridRows= gridRows;
        this.gridCols= gridCols;
    }
    public List<RuleDTO> getRulesList() {
        return rulesList;
    }

    public TerminationDTO getTermination() {
        return termination;
    }

    public EntityDefinitionDTO getEntity(String key) {
        return entityDefinitionDTOMap.get(key);
    }

    public EnvVariableManagerDefinitionDTO getEnvVariableManagerDefinitionDTO()
    { return  envVariablesManager; }

    public Map<String, PropertyDTO> getEnvVariablesDefinition() {
        return envVariablesManager.getEnvVariables();
    }

     /*public void updateWorldDTO(World world) {
        this.entity = new EntityDefinitionDTO(world.getEntity());
        this.envVa);
        this.termination = new TerminationDTO(world.getTermination());

        for(Rule rule: world.getRulesList()) {
            List<ActionDTO> actionDTOList = new ArrayList<>();
            RuleDTO ruleDTO = new RuleDTO(rule);
           rulesList.add(ruleDTO);
        }*/

    public Collection<EntityDefinitionDTO> getEntityDefinitionDTOList() {
        return entityDefinitionDTOMap.values();
    }

    public int getGridCols() {
        return gridCols;
    }

    public int getGridRows() {
        return gridRows;
    }
}
