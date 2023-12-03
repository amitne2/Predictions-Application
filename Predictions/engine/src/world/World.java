package world;
import java.io.File;
import java.io.FileInputStream;
import action.*;
import action.condition.ConditionAction;
import action.condition.multipleCondition;
import action.condition.singleCondition;
import action.condition.thenAndElse.ThenElseImpl;
import dto.*;
import entity.definition.EntityDefinitionImpl;
import entity.instance.EntityInstanceImpl;
import environment.definition.EnvVariableManagerDefinition;
import exception.BooleanCanNotBeHigherOrLowerException;
import exception.VariableDoesNotExistsException;
import file.fileChecker;
import generated.*;
import property.EntityDefProperty;
import property.Property;
import rule.Activation;
import rule.Rule;
import termination.Termination;

import java.io.InputStream;
import java.util.*;
import java.util.concurrent.locks.Condition;

public class World {
    private int threadsCount;
    private boolean fileExist;
    private HashMap<String, EntityDefinitionImpl> entityDefinitionMap;
    private EnvVariableManagerDefinition envVariableManagerDefinition;
    private int rowsGrid;
    private int colsGrid;
    private List<Rule> rulesList;
    private Termination termination;


    public World() {
        fileExist = false;
        rulesList = new ArrayList<>();
        entityDefinitionMap = new HashMap<>();
        envVariableManagerDefinition = new EnvVariableManagerDefinition();
    }

    public boolean isFileExist() {
        return fileExist;
    }

    public void readFile(File file) throws Exception {
        PRDWorld prdWorld = null;
        fileChecker fileChecker = new fileChecker();

        InputStream in = new FileInputStream(file);

        prdWorld = fileChecker.openFile(in);
        fileChecker.checkXMLContent(prdWorld);
        clearData();
        fromJAXBObjToWorld(prdWorld);
        fileExist = true;
    }

    private void clearData() {
        fileExist= false;
        rulesList = new ArrayList<>();
        entityDefinitionMap = new HashMap<>();
        envVariableManagerDefinition = new EnvVariableManagerDefinition();
    }

    public void fromJAXBObjToWorld(PRDWorld prdWorld) throws Exception {
        takeThreadsCount(prdWorld.getPRDThreadCount());
        takeGridDetails(prdWorld.getPRDGrid());
        takeEntitiesDefinition(prdWorld.getPRDEntities());
        takeRules(prdWorld.getPRDRules());
        takeTermination(prdWorld, prdWorld.getPRDTermination());
        takeEnvironmentVariables(prdWorld.getPRDEnvironment());
    }
    public int getThreadsCount() { return threadsCount; }
    private void takeThreadsCount(int prdThreadCount) {
        this.threadsCount = prdThreadCount;
    }

    private void takeGridDetails(PRDWorld.PRDGrid prdGrid) {
        this.rowsGrid = prdGrid.getRows();
        this.colsGrid = prdGrid.getColumns();
    }

    public void takeEntitiesDefinition(PRDEntities entities) throws Exception {
        EntityDefinitionImpl entityToCopy = null;
        EntityDefProperty defProperty = null;

        for(PRDEntity entity: entities.getPRDEntity()) {
            String nameEntity = entity.getName().trim();
            int population = 0; //set to 0 and will update it when the user enters
            List<EntityDefProperty> entityDefProperties = new ArrayList<>();

            for (PRDProperty property : entity.getPRDProperties().getPRDProperty()) {
                defProperty = createEntityDefProperty(property);
                entityDefProperties.add(defProperty);
            }
            entityToCopy = new EntityDefinitionImpl(nameEntity,population, entityDefProperties);
            setEntityDefinition(nameEntity, entityToCopy);
        }
    }

    //this function create entityDefProperty from PRDProperty and return it
    public EntityDefProperty createEntityDefProperty(PRDProperty property) throws Exception {
        EntityDefProperty resProperty = null;
        boolean rangeExist = false, initvalue = false;
        float from = 0, to = 0; //default val (will not send to ctor)
        String init = null;

        String nameProperty = property.getPRDName().trim();
        String typeProperty = property.getType().trim();
        if (property.getPRDRange() != null) {
            from = (float) property.getPRDRange().getFrom();
            to = (float) property.getPRDRange().getTo();
            rangeExist = true;
        }

        boolean isRandom = property.getPRDValue().isRandomInitialize();
        if (!isRandom) {
            init = property.getPRDValue().getInit().trim();
            initvalue = true;
        }

        if (rangeExist) { //range exists
            if (initvalue) { //init value and range exists
                resProperty = new EntityDefProperty(nameProperty, typeProperty, from, to, isRandom, init);

            } else {  //range exist and init doesn't exist
                resProperty = new EntityDefProperty(nameProperty, typeProperty, from, to, isRandom);
            }

        } else { //range doesn't exist
            if (initvalue) { //init value exist and range doesn't exist
                resProperty = new EntityDefProperty(nameProperty, typeProperty, isRandom, init);
            } else { //init and range don't exist
                resProperty = new EntityDefProperty(nameProperty, typeProperty, isRandom);
            }
        }
        return resProperty;
    }

    public void takeRules(PRDRules rules) throws Exception {
        List<Rule> rulesList = new ArrayList<>();

        for (PRDRule rule : rules.getPRDRule()) {

            String ruleName = rule.getName().trim();
            Activation activation = createActivation(rule.getPRDActivation());

            List<Action> ruleActionList = new ArrayList<>();
            for (PRDAction prdAction : rule.getPRDActions().getPRDAction()) {
                Action actionToAdd = createAction(prdAction);
                ruleActionList.add(actionToAdd);
            }
            Rule ruleToAdd = new Rule(ruleName, activation, ruleActionList);
            rulesList.add(ruleToAdd);
        }
        setRulesList(rulesList);
    }

    public Activation createActivation(PRDActivation prdActivation) {
        Activation res = null;
        if (prdActivation != null) {
            if (prdActivation.getProbability() == null && prdActivation.getTicks() != null) {
                res = new Activation(prdActivation.getTicks());
            } else if (prdActivation.getTicks() == null && prdActivation.getProbability() != null) {
                res = new Activation(prdActivation.getProbability());
            } else if (prdActivation.getProbability() == null && prdActivation.getProbability() == null) {
                res = new Activation();
            } else { //both not null
                res = new Activation(prdActivation.getTicks(), prdActivation.getProbability());
            }
        } else {
            res = new Activation();
        }
        return res;
    }

    public Action createAction(PRDAction action) throws Exception {
        Action res = null;
        boolean existSeconderyEntity = false;
        SeconderyEntity seconderyEntity =null;
        String actionType = action.getType().toLowerCase().trim();
        String propertyName, byExp, resultProp, value, entityName;

        if(action.getPRDSecondaryEntity()!=null) { //check if exist secondery entity
            PRDAction.PRDSecondaryEntity.PRDSelection selection = action.getPRDSecondaryEntity().getPRDSelection();
            ConditionAction conditionActionInSeconderayEntity = copyPRDConditionActionInSeconderayEntity(selection.getPRDCondition());
            seconderyEntity = new SeconderyEntity(Integer.parseInt(selection.getCount()), action.getPRDSecondaryEntity().getEntity(), (ConditionAction) conditionActionInSeconderayEntity);
            existSeconderyEntity = true;
        }

        switch (actionType) {
            case "increase":
                 entityName = action.getEntity().trim();
                propertyName = action.getProperty().trim();
                byExp = action.getBy().trim();
                if(existSeconderyEntity)
                    res = new IncreaseAction(actionType, entityName, propertyName, byExp, seconderyEntity);
                else
                     res = new IncreaseAction(actionType, entityName, propertyName, byExp);
                break;
            case "decrease":
                 entityName = action.getEntity().trim();
                propertyName = action.getProperty().trim();
                byExp = action.getBy().trim();
                if(existSeconderyEntity)
                    res = new DecreaseAction(actionType, entityName, propertyName, byExp, seconderyEntity);
                else
                    res = new DecreaseAction(actionType, entityName, propertyName, byExp);
                break;
            case "calculation":
                String calculationType, arg1, arg2;
                entityName = action.getEntity().trim();
                resultProp = action.getResultProp().trim();

                if (action.getPRDMultiply() != null) {
                    calculationType = "multiply";
                    arg1 = action.getPRDMultiply().getArg1().trim();
                    arg2 = action.getPRDMultiply().getArg2().trim();
                } else { //PRDDivide not null, PRDMultiply null
                    calculationType = "divide";
                    arg1 = action.getPRDDivide().getArg1().trim();
                    arg2 = action.getPRDDivide().getArg2().trim();
                }

                if(existSeconderyEntity)
                    res = new CalculationAction(entityName, actionType, calculationType, resultProp, arg1, arg2, seconderyEntity);
                else
                    res = new CalculationAction(entityName, actionType, calculationType, resultProp, arg1, arg2);
                break;
            case "condition":
                entityName = action.getEntity().trim();
                boolean elseThenAlreadyCreated = false;
                if(existSeconderyEntity) {
                    res = copyPRDConditionAction(action.getPRDCondition(), action, elseThenAlreadyCreated, seconderyEntity);
                }
                else
                    res = copyPRDConditionAction(action.getPRDCondition(), action, elseThenAlreadyCreated, null);

                break;
            case "set":
                entityName = action.getEntity().trim();
                propertyName = action.getProperty().trim();
                value = action.getValue().trim();
                res = new SetAction(actionType, entityName, propertyName, value);
                break;
            case "kill":
                entityName = action.getEntity().trim();
                res = new KillAction(entityName, actionType);
                break;
            case "replace":
                String killEntity = action.getKill();
                String createEntity = action.getCreate();
                String mode = action.getMode();
                if(existSeconderyEntity)
                    res = new ReplaceAction(actionType, killEntity, createEntity, mode, seconderyEntity);
                else
                    res = new ReplaceAction(actionType, killEntity, createEntity, mode);
                break;
            case "proximity":
                if(existSeconderyEntity)
                    res = copyProximityAction(actionType, action, seconderyEntity);
                else
                    res = copyProximityAction(actionType, action, null);
                break;

        }
        return res;
    }

    private Action copyProximityAction(String actionType, PRDAction action, SeconderyEntity seconderyEntity) throws Exception {
        Action res;
        List<Action> actionList = new ArrayList<>();
        String sourceEntity = action.getPRDBetween().getSourceEntity();
        String targetEntity = action.getPRDBetween().getTargetEntity();
        String envDepthOf = action.getPRDEnvDepth().getOf();
        if(action.getPRDActions().getPRDAction()!=null) {
            for(PRDAction actionInProximity: action.getPRDActions().getPRDAction()){
                actionList.add(createAction(actionInProximity));
            }
        }
        res = new ProximityAction(actionType, sourceEntity, targetEntity, envDepthOf, actionList, seconderyEntity);
        return res;
    }

    private ConditionAction copyPRDConditionActionInSeconderayEntity(PRDCondition prdCondition) throws VariableDoesNotExistsException, BooleanCanNotBeHigherOrLowerException {
        String singularity = prdCondition.getSingularity();
        ConditionAction returnedAction = null;

        if (singularity.trim().equals("single")) {
            returnedAction = new singleCondition(prdCondition.getEntity(), "condition", null, null, prdCondition.getProperty(), prdCondition.getOperator(), prdCondition.getValue());
        } else { //have multiple conditions, need to call recursive to the next one

            multipleCondition multipleCondition = new multipleCondition(prdCondition.getEntity(), "condition", null, null, prdCondition.getLogical());
            for (PRDCondition currCondition : prdCondition.getPRDCondition()) {
                ConditionAction newCondition = copyPRDConditionActionInSeconderayEntity(currCondition);
                multipleCondition.addCondition(newCondition);
            }

            returnedAction = multipleCondition;
        }
        return returnedAction;
    }

    private Action copyPRDConditionAction(PRDCondition prdCondition, PRDAction action, boolean elseThenAlreadyCreated , SeconderyEntity seconderyEntity) throws Exception {
        String singularity = prdCondition.getSingularity();
        Action returnedAction;

        ThenElseImpl thenOp = new ThenElseImpl("then");
        ThenElseImpl elseOp = new ThenElseImpl("else");

        if(!elseThenAlreadyCreated) {
            //creating then actions list
            for(PRDAction prdActionInThen: action.getPRDThen().getPRDAction()) {
                thenOp.addAction(createAction(prdActionInThen));
            }

            //creating else actions list
            if(action.getPRDElse()!=null) {
                for(PRDAction prdActionInElse: action.getPRDElse().getPRDAction())
                    elseOp.addAction(createAction(prdActionInElse));
            }
            elseThenAlreadyCreated = true;
        }
       if(singularity.trim().equals("single")){
            returnedAction = new singleCondition(prdCondition.getEntity(), action.getType(), thenOp, elseOp, prdCondition.getProperty(), prdCondition.getOperator(), prdCondition.getValue(), seconderyEntity);
       }
       else { //have multiple conditions, need to call recursive to the next one
           multipleCondition multipleCondition =null;
                 multipleCondition = new multipleCondition(action.getEntity(), action.getType(), thenOp, elseOp, prdCondition.getLogical(), seconderyEntity);
                 seconderyEntity = null;
           for(PRDCondition currCondition: prdCondition.getPRDCondition()) {
               ConditionAction newCondition = (ConditionAction) copyPRDConditionAction(currCondition, action, elseThenAlreadyCreated,  seconderyEntity);
               multipleCondition.addCondition(newCondition);
           }

           returnedAction = multipleCondition;
       }
       return returnedAction;
    }

    public void takeTermination( PRDWorld prdWorld,PRDTermination termination) {
        Termination terminationToAdd = null;
        int ticks =0, seconds =0;
        boolean secondBool = false, tickBool = false;
        if(prdWorld.getPRDTermination().getPRDByUser() != null) {
             terminationToAdd = new Termination(true);
        }
        else if(!prdWorld.getPRDTermination().getPRDBySecondOrPRDByTicks().isEmpty()) {
            for(Object terminateCondition: prdWorld.getPRDTermination().getPRDBySecondOrPRDByTicks()) {
                if(terminateCondition.getClass().equals(PRDBySecond.class)) {
                    seconds = ((PRDBySecond) terminateCondition).getCount();
                    secondBool = true;
                }
                else {
                    ticks = ((PRDByTicks)terminateCondition).getCount();
                    tickBool = true;
                }
            }
             terminationToAdd = new Termination(ticks, seconds, tickBool, secondBool);
        }
        setTermination(terminationToAdd);
    }

    public void takeEnvironmentVariables(PRDEnvironment environments) throws Exception {
        Map<String, Property> envPropertyMap = new HashMap<>();
        Property property;

        for (PRDEnvProperty envProperty : environments.getPRDEnvProperty()) {
            String name = envProperty.getPRDName().trim();
            String type = envProperty.getType().toLowerCase().trim();
            if (envProperty.getPRDRange() != null) {
                float rangefrom = (float) envProperty.getPRDRange().getFrom();
                float rangeTo = (float) envProperty.getPRDRange().getTo();
                property = new Property(name, type, rangefrom, rangeTo);
            } else {
                property = new Property(name, type);

            }
            envPropertyMap.put(name, property);
        }
        setEnvVariablesManagerDef(envPropertyMap);
    }

    public void setEnvVariablesManagerDef(Map<String, Property> envVariables) {
        envVariableManagerDefinition.setEnvVariables(envVariables);
    }

    public List<Rule> getRulesList() {
        return rulesList;
    }

    public Termination getTermination() {
        return termination;
    }

    public EntityDefinitionImpl getEntity(String key) {
        return entityDefinitionMap.get(key);
    }

    public EntityDefinitionImpl getEntityByName(String name) {
        return  entityDefinitionMap.get(name);
    }

    public EnvVariableManagerDefinition getEnvVariableManagerDefinition()
    { return  envVariableManagerDefinition; }

    public void setEntityDefinition(String name,EntityDefinitionImpl entityToCopy) {
        this.entityDefinitionMap.put(name, entityToCopy);
    }

    public void setRulesList(List<Rule> ruleList) {
        this.rulesList = ruleList;
    }

    public void setTermination(Termination terminationToAdd) {
        this.termination = terminationToAdd;
    }

    public Collection<Property> getEnvVariablesDefinition() {
        return envVariableManagerDefinition.getEnvVariables();
    }

    public WorldDTO createWorldDTO(){
        EnvVariableManagerDefinitionDTO envVariableManagerDefinitionDTO = null;
        TerminationDTO terminationDTO = null;
        List <RuleDTO> ruleDTOList = new ArrayList<>();
        HashMap<String ,EntityDefinitionDTO> EntityDefinitionDTOHashMap= new HashMap<>();
        for(EntityDefinitionImpl entityDefinition: entityDefinitionMap.values()) {
            EntityDefinitionDTO entityDefinitionDTO = entityDefinition.createEntityDefinitionDTO();
            EntityDefinitionDTOHashMap.put(entityDefinitionDTO.getName(), entityDefinitionDTO);
        }
           envVariableManagerDefinitionDTO = envVariableManagerDefinition.creEnvVariableManagerDefinitionDTO();
           terminationDTO = termination.creTerminationDTO();

           for (Rule rule : rulesList) {
               RuleDTO ruleDTO = rule.createRuleDTO();
               ruleDTOList.add(ruleDTO);
           }

       int gridRows = getRowsGrid();
       int gridCols = getColsGrid();
        WorldDTO worldDTO = new WorldDTO(EntityDefinitionDTOHashMap, envVariableManagerDefinitionDTO, ruleDTOList, terminationDTO, gridRows, gridCols);
        return worldDTO;
    }

    public Collection<EntityDefinitionImpl> getEntityDefinitionList() {
        return entityDefinitionMap.values();
    }

    public void setPopulation(String entityName, int population) {
        EntityDefinitionImpl entityDefinition = entityDefinitionMap.get(entityName);
        entityDefinition.setPopulation(population);
    }

    public List<Rule> getActiveRules(long tick) {
        List<Rule> activeRules = new ArrayList<>();
        for(Rule rule: rulesList) {
            if(rule.checkIfRuleOperable(tick)) {
                activeRules.add(rule);
            }
        }
        return activeRules;
    }

    public int getColsGrid() {
        return colsGrid;
    }

    public int getRowsGrid() {
        return rowsGrid;
    }

    public HashMap<String, EntityDefinitionImpl> getEntityDefinitionMap() {
        return entityDefinitionMap;
    }

}

