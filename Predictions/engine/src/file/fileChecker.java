package file;

import action.Action;
import exception.*;
import generated.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class fileChecker {
    private PRDWorld prdWorld;
    private final static String JAXB_XML_GAME_PACKAGE_NAME = "generated";

    public InputStream isFileExist(String fileName) throws FileNotFoundException { //1
        return new FileInputStream(fileName);
    }

    public void isXMLFile(String fileName) throws FileNotXMLException { //1
        if(!fileName.endsWith(".xml"))
            throw new FileNotXMLException("No xml file name:" + fileName + "exists in this directory");
    }

    public PRDWorld openFile(InputStream file) throws JAXBException {
        return deserializeFrom(file);
    }
     //Convert from XML to java objects
    private static PRDWorld deserializeFrom(InputStream in) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(JAXB_XML_GAME_PACKAGE_NAME);
        Unmarshaller u = jc.createUnmarshaller();
        return (PRDWorld) u.unmarshal(in);
    }

    public void checkXMLContent(PRDWorld prdWorld) throws Exception {
        this.prdWorld = prdWorld;
        checkGridRange(prdWorld.getPRDGrid());
        PRDEnvironment environment = prdWorld.getPRDEnvironment();
       PRDEntities entities = prdWorld.getPRDEntities();
       List<PRDRule> rulesList = prdWorld.getPRDRules().getPRDRule();

       //checkRulesNameUnique(rulesList); //not must.
        checkEnvironmentNamesUnique(environment); //2
        checkRangeInPropertiesEnvironment(environment.getPRDEnvProperty());

       for(PRDEntity entity: entities.getPRDEntity()) {
           checkPropertyNameUniqueInEntity(entity.getPRDProperties()); //3
           checkRangeInPropertiesEntity(entity.getPRDProperties().getPRDProperty()); //check range in property
       }

        checkEntityExistsInRules(rulesList, entities); //4
        checkPropertyExistsInRules(rulesList, entities); //5 //return to it!!
        checkCalculationOperationsIsValidInAction(prdWorld); //6 //return to it!!
    }

    private void checkGridRange(PRDWorld.PRDGrid prdGrid) throws Exception {
        if(prdGrid.getRows() < 10 || prdGrid.getRows() > 100)
            throw new Exception("The rows is " + prdGrid.getRows() + " but need to be between 10 to 100 include.");

        if(prdGrid.getColumns() < 10 || prdGrid.getColumns() > 100)
            throw new Exception("The columns is " + prdGrid.getColumns() + " but need to be between 10 to 100 include.");

    }

    private void checkRangeInPropertiesEntity(List<PRDProperty> prdProperties) {
        for (PRDProperty property : prdProperties) {
            if (property.getType().equalsIgnoreCase("decimal")) {
                if (property.getPRDRange() != null) {
                    if (property.getPRDRange().getTo() <= property.getPRDRange().getFrom()) {
                        throw new ArithmeticException("The 'to' value is bigger than 'from' " +
                                "value in range in property: " + property.getPRDName());
                    }
                }
            } else if (property.getType().equalsIgnoreCase("float")) {
                if (property.getPRDRange() != null) {
                    if (property.getPRDRange().getTo() <= property.getPRDRange().getFrom()) {
                        throw new ArithmeticException("The 'to' value is bigger than 'from' " +
                                "value in range in property: " + property.getPRDName());
                    }
                }
            } else if(property.getType().equalsIgnoreCase("boolean")) {
                if(property.getPRDRange() != null) {
                    throw new IllegalArgumentException("property " + property.getPRDName() + "from type boolean can't have range");
                }
            } else if(property.getType().equalsIgnoreCase("string")) {
                if(property.getPRDRange() !=null) {
                    throw new IllegalArgumentException("property " + property.getPRDName() + "from type string can't have range");
                }
            }
        }
    }

    private void checkRangeInPropertiesEnvironment(List<PRDEnvProperty> prdProperties) {
        for (PRDEnvProperty property : prdProperties) {
            if (property.getType().equalsIgnoreCase("decimal")) {
                if (property.getPRDRange() != null) {
                    if (property.getPRDRange().getTo() <= property.getPRDRange().getFrom()) {
                        throw new ArithmeticException("The 'to' value is bigger than 'from' " +
                                "value in range in environment property: " + property.getPRDName());
                    }
                }
            } else if (property.getType().equalsIgnoreCase("float")) {
                if (property.getPRDRange() != null) {
                    if (property.getPRDRange().getTo() <= property.getPRDRange().getFrom()) {
                        throw new ArithmeticException("The 'to' value is bigger than 'from' " +
                                "value in range in property: " + property.getPRDName());
                    }
                }
            } else if(property.getType().equalsIgnoreCase("boolean")) {
                if(property.getPRDRange() != null) {
                    throw new IllegalArgumentException("property " + property.getPRDName() + "from type boolean can't have range");
                }
            } else if(property.getType().equalsIgnoreCase("string")) {
                if(property.getPRDRange() !=null) {
                    throw new IllegalArgumentException("property " + property.getPRDName() + "from type string can't have range");
                }
            }
        }
    }

    private void checkRulesNameUnique(List<PRDRule> rulesList) throws NameContainSpacesException, SameEnvironmentNameException {
        String tmpName;
        int counter =0;
        List <String> rulesName = new ArrayList<>();

        for(PRDRule rule: rulesList) {
            String name = rule.getName().trim();
            if(name.contains(" "))  //invalid name
                throw new NameContainSpacesException("Name can't contain spaces, this name: " + name + "doesn't valid.");
            rulesName.add(name);
        }

        for(PRDRule rule: rulesList) {
            tmpName = rule.getName().trim();

            for(String name: rulesName) {
                if(name.equalsIgnoreCase(tmpName))  //ignore capital case
                    counter++;

                if(counter > 1) //name exist more than 1 time
                    throw  new SameEnvironmentNameException("Rule name unique, the name " + tmpName + "already exist in system.");

                counter =0;
            }
        }
    }

    //check if same
    private void checkEnvironmentNamesUnique(PRDEnvironment evironment) throws SameEnvironmentNameException, NameContainSpacesException {
        String tmpName;
        int counter =0;
        List <String> environmentName = new ArrayList<>();

        for(PRDEnvProperty property: evironment.getPRDEnvProperty()) {
            String name = property.getPRDName().trim();
            if(name.contains(" "))  //invalid name
                throw new NameContainSpacesException("Name can't contain spaces, this name: " + name + "doesn't valid.");
            environmentName.add(name);
        }

        for(PRDEnvProperty envProperty: evironment.getPRDEnvProperty()) {
            tmpName = envProperty.getPRDName().trim();

            for(String name: environmentName) {
                if (name.equalsIgnoreCase(tmpName))  //ignore capital case
                    counter++;
            }
                if(counter > 1) //name exist more than 1 time
                    throw  new SameEnvironmentNameException("Environment variable name should be unique. the name " + tmpName + " already exists in system.");

                counter =0;
            }

    }

    //get list of property on entity and check that no name exist more than one time.
   private void checkPropertyNameUniqueInEntity(PRDProperties entityProperties) throws SamePropertyNameInEntityException, NameContainSpacesException {
       String tmpName;
       int counter =0;
       List <String> propertyName = new ArrayList<>();

      for(PRDProperty property: entityProperties.getPRDProperty()) {
          String name = property.getPRDName().trim();
          if(name.contains(" "))
              throw new NameContainSpacesException("Name can't contain spaces, this name: " + name + "doesn't valid.");

          propertyName.add(name);
      }

      for(PRDProperty property: entityProperties.getPRDProperty()) {
          tmpName = property.getPRDName().trim();

          for(String name: propertyName) {
              if (name.equalsIgnoreCase(tmpName))
                  counter++;
          }
              if(counter > 1)
                  throw new SamePropertyNameInEntityException("property name unique, the name " + tmpName + "already exist in this entity");

              counter=0;
          }

   }

   //check that the entity name if each rule is an exist entity in the system
   private void checkEntityExistsInRules(List<PRDRule> rulesList, PRDEntities entities) throws NoSuchEntityException {
       String tmpName;
       String ruleName;
       PRDEntity entityChecker;
       int counter = 0;
       List<String> entitiesList = new ArrayList<>();
       for (PRDEntity entity : entities.getPRDEntity()) { //enter names into list
           entitiesList.add(entity.getName().trim());
       }

       for (PRDRule rule : rulesList) {//rules list
           ruleName = rule.getName().trim();
           for (PRDAction action : rule.getPRDActions().getPRDAction()) {
               if(action.getPRDSecondaryEntity() != null) { //check entity in secondary entity
                   checkSecondaryEntity(action.getPRDSecondaryEntity());
               }
               if (action.getType().equals("proximity"))
                   checkEntitiesNameInProximityAction(action, entitiesList, ruleName);
               else if (action.getType().equals("replace"))
                   checkEntitiesNameInReplaceAction(action, entitiesList, ruleName);
               else {
                   tmpName = action.getEntity().trim();

                   for (String name : entitiesList) {
                       if (name.equalsIgnoreCase(tmpName)) {
                           counter++;
                       }
                   }
                   if (counter == 0) { //the name doesn't exist in list
                       throw new NoSuchEntityException("The rule: " + ruleName + " contains entity name: " + tmpName + " that doesn't exist.");
                   }
                   counter = 0;
               }
           }
       }
   }

    private void checkEntitiesNameInProximityAction(PRDAction action, List<String> entitiesList, String ruleName) throws NoSuchEntityException {
        String tmpName;
        int counter =0;
        tmpName = action.getPRDBetween().getSourceEntity(); //check source entity first
        for(int i=0;i<2;i++){
            for (String name : entitiesList) {
                if (name.equalsIgnoreCase(tmpName)) {
                    counter++;
                }
            }
            if (counter == 0) { //the name doesn't exist in list
                throw new NoSuchEntityException("The rule: " + ruleName + " contains entity name: " + tmpName + " that doesn't exist.");
            }
            counter = 0;
            tmpName = action.getPRDBetween().getTargetEntity(); //check target entity
            }

            for(PRDAction actionInProximity: action.getPRDActions().getPRDAction()) {
                if (actionInProximity.getType().equals("proximity"))
                    checkEntitiesNameInProximityAction(actionInProximity, entitiesList, ruleName);
                else if (actionInProximity.getType().equals("replace"))
                    checkEntitiesNameInReplaceAction(actionInProximity, entitiesList, ruleName);
                else {
                    tmpName = actionInProximity.getEntity().trim();

                    for (String name : entitiesList) {
                        if (name.equalsIgnoreCase(tmpName)) {
                            counter++;
                        }
                    }
                    if (counter == 0) { //the name doesn't exist in list
                        throw new NoSuchEntityException("The rule: " + ruleName + " contains entity name: " + tmpName + " that doesn't exist.");
                    }
                    counter = 0;
                }
            }
        }

    //check source and target name in proximity name
    private void checkEntitiesNameInReplaceAction(PRDAction action, List<String> entitiesList, String ruleName) throws NoSuchEntityException {
        String tmpName;
        int counter =0;
        tmpName = action.getKill(); //check kill entity first
        for(int i=0;i<2;i++) {
            for (String name : entitiesList) {
                if (name.equalsIgnoreCase(tmpName)) {
                    counter++;
                }
            }
            if (counter == 0) { //the name doesn't exist in list
                throw new NoSuchEntityException("The rule: " + ruleName + " contains entity name: " + tmpName + " that doesn't exist.");
            }
            counter = 0;
            tmpName = action.getCreate(); //check create entity
        }
    }

    //check that each property related to the right property name in entity
   private void checkPropertyExistsInRules(List<PRDRule> ruleList, PRDEntities entities) throws NoSuchProperyOnThisEntity, NoSuchEntityException {
       String entityNameOnAction;
       PRDActions actions;
       List<PRDProperty> properties;
       Map<String, List<PRDProperty>> propertiesMap = new HashMap<>();

       Map<String, PRDEntity> mapOfEntitiesByNames = entities.getPRDEntity().stream()
               .collect(Collectors.toMap(PRDEntity::getName, prd -> prd));

       for (PRDEntity entity : entities.getPRDEntity()) {
           propertiesMap.put(entity.getName().trim(), entity.getPRDProperties().getPRDProperty());
       }

       for (PRDRule rule : ruleList) {
           actions = rule.getPRDActions(); //get actions of rule
           for (PRDAction action : actions.getPRDAction()) {  //run on actions list

               if(action.getType().equals("proximity")) {
                   checkEntityNameInProximityAction(action);

               } else if(action.getType().equals("replace")) {
                   checkEntityNameInReplaceAction(action);

               }
               /*else if (action.getType().equals("condition")) {
                    //return to it!!
               }*/ else {
                   entityNameOnAction = action.getEntity().trim();

                   for (String name : propertiesMap.keySet()) { //search the relevant entity
                       if (name.equalsIgnoreCase(entityNameOnAction))
                           entityNameOnAction = name;
                   }
                   Map<String, PRDProperty> mapOfPropertiesByNames = mapOfEntitiesByNames.get(entityNameOnAction).getPRDProperties()
                           .getPRDProperty().stream().collect(Collectors.toMap(PRDProperty::getPRDName, prd -> prd));

                   checkSingleAction(action, mapOfPropertiesByNames, mapOfEntitiesByNames);
                   properties = propertiesMap.get(entityNameOnAction);

                   if (action.getProperty() != null) {
                       if (!isPropertyExistInPropertiesList(action.getProperty(), properties))  //property name doesn't exist in this entity
                           throw new NoSuchProperyOnThisEntity("The entity: " + entityNameOnAction + "doesn't have the property: " + action.getProperty());
                   }
               }
           }
       }
   }

    private void checkEntityNameInReplaceAction(PRDAction action) throws NoSuchEntityException {
        String killName = action.getKill();
        String createName = action.getCreate();
        boolean foundKill = false;
        boolean foundCreate = false;
        for(PRDEntity entity: prdWorld.getPRDEntities().getPRDEntity()) {
            if(entity.getName().equals(killName)) {
                foundKill = true;
            }
            if(entity.getName().equals(createName)) {
                foundCreate = true;
            }
        }
        if(!foundCreate)
            throw new NoSuchEntityException("In replace action the create entity: " + createName + " not exist in world.");
        if(!foundKill)
            throw new NoSuchEntityException("In replace action the kill entity: " + killName + " not exist in world.");
    }

    //check the properties name in proximity action
   private void checkEntityNameInProximityAction(PRDAction action) throws NoSuchProperyOnThisEntity {
        for(PRDAction actionInProximity: action.getPRDActions().getPRDAction()) {
            if(!(actionInProximity.getType().equals("proximity") || actionInProximity.getType().equals("replace") || actionInProximity.getType().equals("condition"))) {
                String entityName = actionInProximity.getEntity().trim();
                String propertyName = actionInProximity.getProperty().trim();
                checkPropertyExistsInEntity(entityName, propertyName);
            }
        }
    }

    private void checkSecondaryEntity(PRDAction.PRDSecondaryEntity prdSecondaryEntity) throws NoSuchEntityException { //return to it!!
        boolean found = false;
        String entityName = prdSecondaryEntity.getEntity();
        for(PRDEntity prdEntity: prdWorld.getPRDEntities().getPRDEntity()) {
            if(prdEntity.getName().equals(entityName)) {
                found = true;
            }
        }
        if(!found)
            throw new NoSuchEntityException("The entity: " + entityName + " in secondery entity doesn't exist in world.");
    }

    //get property name and list of property and return true if the property exists in list, else false
   private boolean isPropertyExistInPropertiesList(String propertyName, List<PRDProperty> properties) {
        for(PRDProperty property: properties) {
            if(property.getPRDName().equals(propertyName)){
                return true;
            }
        }
        return false;
   }
//
    private void checkCalculationOperationsIsValidInAction(PRDWorld world) throws Exception {

        List<PRDAction> actionList;
        List<PRDRule> rulesList = world.getPRDRules().getPRDRule();

        for (PRDRule rule : rulesList) {
            actionList = rule.getPRDActions().getPRDAction();
            for (PRDAction action : actionList) {
                checkIfMathActionVariablesAreNumeric(world, action);
            }
        }
    }

    //this function checks if action in calculation operator functions and calls to the checks functions
    private void checkIfMathActionVariablesAreNumeric(PRDWorld world, PRDAction action) throws Exception {

        /*Map<String, PRDEntity> mapOfEntitiesByName = world.getPRDEntities().getPRDEntity()
                .stream().collect(Collectors.toMap(PRDEntity::getName, prd -> prd));*/

        if(isOperationAction(action.getType().trim())) {  //increase, decrease actions
            checkPropertyTypeIsNumber(action, world);
            checkByExpression(action, world, action.getBy(), action.getProperty().trim());
        }
        else if(action.getType().trim().equals("set")){
            checkByExpression(action, world, action.getValue(), action.getProperty().trim());
        }
         else if(action.getType().trim().equals("calculation")){ //calculation action
            checkIfCalculationVariablesAreNumber(world, action);

        } else if(action.getType().trim().equals("condition")) {
             if(action.getPRDCondition().getSingularity().equals("single")) {
                 checkValueAndPropertyInConditionActionFromTheSameType(action); //check property and value exp
             }
            if(action.getPRDCondition().getSingularity().equals("multiple")) {
                if(action.getPRDThen() != null) {
                    for(PRDAction actionInThenMode: action.getPRDThen().getPRDAction()) {
                        checkIfMathActionVariablesAreNumeric(world, actionInThenMode);
                    }
                }
                if(action.getPRDElse() != null) {
                    for(PRDAction actionInThenMode: action.getPRDThen().getPRDAction()) {
                        checkIfMathActionVariablesAreNumeric(world, actionInThenMode);
                    }
                }
            }
        }
    }

    private void checkValueAndPropertyInConditionActionFromTheSameType(PRDAction action) {
        String propertyExp = action.getProperty();
        String valueExp = action.getValue();

        //String proertyExpType = getExpType(propertyExp);

    }



    //This function check the calculation args that they are a number
    private void checkIfCalculationVariablesAreNumber(PRDWorld prdWorld, PRDAction action) throws Exception {
        String entityName = action.getEntity().trim();
        String propertyName = action.getResultProp().trim(); //?????

        Map<String, PRDEntity> mapOfEntitiesByName = prdWorld.getPRDEntities().getPRDEntity()
                .stream().collect(Collectors.toMap(PRDEntity::getName, prd -> prd));

        Map<String, PRDProperty> mapOfEntityPropertiesByName = mapOfEntitiesByName.get(entityName).getPRDProperties()
                .getPRDProperty().stream().collect(Collectors.toMap(PRDProperty::getPRDName, prd -> prd));

        String arg1, arg2;
        boolean checkArg1, checkArg2;
        String resProp = action.getResultProp().trim();

        if (!(action.getPRDMultiply() == null)) {
            arg1 = action.getPRDMultiply().getArg1();
            arg2 = action.getPRDMultiply().getArg2();
        } else {
            arg1 = action.getPRDDivide().getArg1();
            arg2 = action.getPRDDivide().getArg2();
        }
        checkByExpression(action, prdWorld,arg1, propertyName);
        checkByExpression(action, prdWorld, arg2, propertyName);


        if(mapOfEntityPropertiesByName.get(propertyName).getType().equals("decimal")) {
            try {
                //int num1 = Integer.parseInt(arg1);
               // int num2 = Integer.parseInt(arg2);

                if(mapOfEntityPropertiesByName.get(resProp).getType().equals("string") || mapOfEntityPropertiesByName.get(resProp).getType().equals("boolean"))
                    throw new ByExpNotMatchPropertyTypeException("The property: " + resProp +"is from type: " + mapOfEntityPropertiesByName.get(resProp).getType()
                            + "can not get a number value (float/decimal)");

            } catch (NumberFormatException e) {
                throw new ByExpNotMatchPropertyTypeException("For action: calculation, with" +
                        " PRDProperty " + propertyName + " the values should be decimal, but they are not.");
            }
        } else if(mapOfEntityPropertiesByName.get(propertyName).getType().equals("float")) {
            try{
                //float num1 = Float.parseFloat(arg1);
               // float num2 = Float.parseFloat(arg2);

                if(!(mapOfEntityPropertiesByName.get(resProp).getType().equals("float"))) {
                    throw new ByExpNotMatchPropertyTypeException("The property: " + resProp + " from type: " +  mapOfEntityPropertiesByName.get(resProp).getType() +
                            ". can not get value number from type float.");
                }
            } catch (NumberFormatException e) {
                throw new ByExpNotMatchPropertyTypeException("For action: calculation, with" +
                        " PRDProperty " + propertyName + " the values should be float, but they are not.");
            }
        }
    }

    private void checkPropertyTypeIsNumber(PRDAction action, PRDWorld world) {
        String propertyName = action.getProperty().trim();
        for(PRDEntity entity: world.getPRDEntities().getPRDEntity()) {
            for(PRDProperty property: entity.getPRDProperties().getPRDProperty()) {
                if(property.getPRDName().equals(propertyName)) {
                    if(property.getType().equalsIgnoreCase("boolean") || property.getType().equalsIgnoreCase("string")) {
                        throw new ArithmeticException("The property: " + propertyName + " is type: " + property.getType()+
                                " You can't calculation actions on this property.");
                    }
                }
            }
        }
    }

    private boolean isOperationAction(String type) {
        if(type.equalsIgnoreCase("Increase") || type.equalsIgnoreCase("Decrease")) {
            return true;
        }
        return false;
    }

    private void checkByExpression(PRDAction action, PRDWorld world, String byExp, String propertyName) throws Exception {

        String propertyType = getPropertyType(action.getEntity().trim(), propertyName, world.getPRDEntities().getPRDEntity());

        if(byExp.startsWith("random") || byExp.startsWith("environment") || byExp.startsWith("percent")
        || byExp.startsWith("ticks") || byExp.startsWith("evaluate")) { //helper functions
            checkHelperFunction(byExp, propertyType, world);
        } else if (isPropertyName(byExp, world.getPRDEntities().getPRDEntity())) //property name
            checkPropertyTypeInByExp(byExp, propertyType, world.getPRDEntities().getPRDEntity());
        else { //free text
            checkFreeTextExp(byExp, propertyType);

        }
    }

    //This function check free text in by exp. check the free text type matchs the property type
    private void checkFreeTextExp(String byExp, String propertyType) throws ByExpNotMatchPropertyTypeException {
        if(propertyType.equalsIgnoreCase("decimal")) {
            try{
                int res = Integer.parseInt(byExp);
            } catch (Exception e){
                throw new ByExpNotMatchPropertyTypeException("The by expression: " + byExp + " not match the property type: " + propertyType);
            }
        } else if(propertyType.equalsIgnoreCase("float")) {
            try {
                float res = Float.parseFloat(byExp);
            }catch (Exception e) {
                throw new ByExpNotMatchPropertyTypeException("The by expression: " + byExp + " not match the property type: " + propertyType);
            }
        } else if(propertyType.equalsIgnoreCase("boolean")) {
            try {
                boolean res = Boolean.parseBoolean(byExp);
            } catch (Exception e) {
                throw new ByExpNotMatchPropertyTypeException("The by expression: " + byExp + " not match the property type: " + propertyType);
            }
            // else it's string
        }
    }

    //This function get the property type that wrote in by expression
    private void checkPropertyTypeInByExp(String by,String propertyType, List<PRDEntity> entityList) throws DifferentTypeException {
        for(PRDEntity entity: entityList) {
            for(PRDProperty property: entity.getPRDProperties().getPRDProperty()) {
                String propertyName = property.getPRDName().trim();
                if(propertyName.equalsIgnoreCase(by)) {
                    String actualType = property.getType().trim();
                    if(!(actualType.equalsIgnoreCase(propertyType))) {
                        throw new DifferentTypeException("The property type: " + propertyType + "doesn't match by expression type: " + actualType);
                    }
                }
            }
        }
    }

    private boolean isPropertyName(String byExp, List<PRDEntity> entityList) {
        for(PRDEntity entity: entityList) {
            for(PRDProperty property: entity.getPRDProperties().getPRDProperty()) {
                String name = property.getPRDName().trim();
                if(name.equalsIgnoreCase(byExp)) {
                    return true;
                }
            }
        }
        return false;
    }

    //this function gets entity name, property name and entities list
    // return the type of the property we are looking for
    private String getPropertyType(String entityName, String propertyName, List<PRDEntity> entityList) throws NoSuchEntityException, NoSuchProperyOnThisEntity {
        String tmpEntityName, tmpPropertyName;
        for(PRDEntity entity: entityList) {
            tmpEntityName = entity.getName().trim();
            if(tmpEntityName.equalsIgnoreCase(entityName)) {
                for(PRDProperty property: entity.getPRDProperties().getPRDProperty()) {
                    tmpPropertyName = property.getPRDName().trim();
                    if(tmpPropertyName.equalsIgnoreCase(propertyName)) {
                        return property.getType();
                    }
                } // no have property name in this entity
                throw new NoSuchProperyOnThisEntity("the property: " + propertyName + "doesn't exist in the entity: " + entityName);
            }
        } //no exist entity with this name
        throw new NoSuchEntityException("No exist entity in system with the name: " + entityName);
    }

    public void checkHelperFunction(String byExp,String propertyType, PRDWorld world) throws Exception {
        String valueInFunction;
        if(byExp.startsWith("random"))
            checkRandomFunction(byExp, propertyType, world.getPRDEntities().getPRDEntity());
        else if(byExp.startsWith("environment"))
            checkEnvironmentFunction(byExp, world, propertyType);
        else if(byExp.startsWith("percent"))
            checkPercentFunction(byExp,world); //need to return
            else if (byExp.startsWith("ticks"))
                checkTicksFunction(byExp, world);
        else if(byExp.startsWith("evaluate"))
            checkEvaluateFunction(byExp, world); //need to return

    }

    private void checkEvaluateFunction(String byExp, PRDWorld world) throws InvalidArgumentInFunctionEvaluateException {
        //return to it!!
    }

    private void checkPercentFunction(String byExp, PRDWorld world) throws InvalidArgumentInFunctionPercentException{

    }

    private void checkTicksFunction(String byExp, PRDWorld world) throws InvalidArgumentInFunctionTicksException {
        String entityName, propertyName;
        boolean foundEntity = false, foundProperty = false;
        int startIndex = byExp.indexOf('(');
        int endIndex = byExp.indexOf(')');

        // Check if both parentheses are found
        if (startIndex != -1 && endIndex != -1) {
            // Extract the content between the parentheses
            String content = byExp.substring(startIndex + 1, endIndex);

            // Split the content by the dot to get "ent-1" and "p1"
            String[] parts = content.split("\\.");

            if (parts.length != 2)
                throw new InvalidArgumentInFunctionTicksException("By exp in evaluate should be   evaluate(entityName.propertyName). The by was: " + byExp);
            else {
                entityName = parts[0];
                propertyName = parts[1];
            }

            for (PRDEntity entity : world.getPRDEntities().getPRDEntity()) {
                if (entity.getName().equals(entityName)) {
                    foundEntity = true;
                    for (PRDProperty property : entity.getPRDProperties().getPRDProperty()) {
                        if (property.getPRDName().equals(propertyName)) {
                            foundProperty = true;
                        }
                    }
                    if (!foundProperty)
                        throw new InvalidArgumentInFunctionTicksException(propertyName + " not exists in" + entityName);
                }
            }
            if (!foundEntity)
                throw new InvalidArgumentInFunctionTicksException(entityName + " not exist in this world.");
        }
        else
            throw new InvalidArgumentInFunctionTicksException("ticks function need to be from format ticks(entityName.propertyName) but got: " + byExp);
    }

    public void checkRandomFunction(String byExp,String propertyType, List<PRDEntity> entities) throws InvalidArgumentInFunctionRandom {
        String valueInFunction;

        // Extract the substring after "random(xx)"
        int startIndex = byExp.indexOf("(") + 1;
        int endIndex = byExp.indexOf(")");

        valueInFunction = byExp.substring(startIndex, endIndex);

        if(propertyType.equalsIgnoreCase("decimal")) {
            try{
                int res = Integer.parseInt(valueInFunction);
            } catch (Exception e) {
                throw new InvalidArgumentInFunctionRandom("the property type: " + valueInFunction + "doesn't match to arg for function random, excepted for int");
            }
        }
        else if(propertyType.equalsIgnoreCase("float")) {
            try{
                float res = Float.parseFloat(valueInFunction);
            } catch (Exception e) {
                throw new InvalidArgumentInFunctionRandom("the arg in function: " + valueInFunction + "doesn't match to arg for function random, excepted for int");
            }
        }
        else { //else invalid option
            throw new InvalidArgumentInFunctionRandom("the arg in function: " + valueInFunction + "doesn't match to arg for function random, excepted for int");
        }
    }

    public void checkEnvironmentFunction(String byExp , PRDWorld world, String propertyType) throws InvalidArgumentInFunctionEnvironmentException {
        boolean found = false;
        String valueInFunction;

        // Extract the substring after "environment("
        int startIndex = byExp.indexOf("(") + 1;
        int endIndex = byExp.indexOf(")");
        valueInFunction = byExp.substring(startIndex, endIndex);

      for(PRDEnvProperty envProperty: world.getPRDEnvironment().getPRDEnvProperty()) {
          String name = envProperty.getPRDName().trim();
          if(name.equalsIgnoreCase(valueInFunction)) {
              found = true;
              if((envProperty.getType().equals("decimal") && propertyType.equals("float")) ||  (envProperty.getType().equals("string") && propertyType.equals("boolean")))
                  return;

          else if(!(envProperty.getType().equals(propertyType))) {
              throw new InvalidArgumentInFunctionEnvironmentException("The property that needed to set the value is from type : "
                       + propertyType + ". It's not match to environment: " + name + " because he is from type: " + envProperty.getType() + "."); }
         }
      }
      if(!found) {
          throw new InvalidArgumentInFunctionEnvironmentException("The arg doesn't match for function environment, excepted for env name. got: " + valueInFunction);
      }
    }



    public void checkSingleAction(PRDAction prdAction, Map<String, PRDProperty> mapOfPropertiesByNames, Map<String, PRDEntity> mapOfEntitiesByNames)
            throws NoSuchProperyOnThisEntity, NoSuchEntityException {

        if(prdAction.getType().equals("proximity")) { //return to it!!!

        }
        else if(prdAction.getType().equals("replace")) {
            checkEntityNameInReplaceAction(prdAction);
        }
        else if (prdAction.getType().equals("condition")) {
            checkIfPropertyExistsInConditionAction(prdAction.getPRDCondition(), mapOfPropertiesByNames);
            if (prdAction.getPRDThen()!= null) {
                for (PRDAction currAction : prdAction.getPRDThen().getPRDAction())
                    checkSingleAction(currAction, mapOfPropertiesByNames, mapOfEntitiesByNames);
            }
            if (prdAction.getPRDElse()!= null) {
                for (PRDAction currAction : prdAction.getPRDElse().getPRDAction()) {
                    checkSingleAction(currAction, mapOfPropertiesByNames, mapOfEntitiesByNames);
                }
            }
        }else if(prdAction.getType().equals("calculation")) {
            String propertyName = prdAction.getResultProp().trim();
            if(mapOfPropertiesByNames.get(propertyName) == null) {
                throw new NoSuchProperyOnThisEntity("In PRDAction: " + prdAction.getType() +
                        ", PRDResult-Prop name: " + propertyName + " must be exists in system, but it doesn't.");
            }
        }  else if(prdAction.getType().trim().equals("kill")) {
            PRDEntity prdEntity = mapOfEntitiesByNames.get(prdAction.getEntity().trim());
            if(prdEntity == null)
                throw new NoSuchEntityException("Not exists entity name with name: " + prdAction.getEntity() + "to kill");
        }
        else {
            String currPropertyName = prdAction.getProperty();
            if (mapOfPropertiesByNames.get(currPropertyName)== null)
                throw new NoSuchProperyOnThisEntity("In PRDAction: " + prdAction.getType() +
                        ", PRDProperty " + currPropertyName + " is required but does not exists.");
        }
    }

    public void checkIfPropertyExistsInConditionAction(PRDCondition prdCondition, Map<String, PRDProperty> mapOfPropertiesByNames) throws NoSuchProperyOnThisEntity {
        String entityName, propertyName=null;
         if (prdCondition.getSingularity().equals("single")) {
            String currPropertyName = prdCondition.getProperty();
            if(currPropertyName.contains("ticks")) {
                int openParenIndex = currPropertyName.indexOf('(');
                int dotIndex = currPropertyName.indexOf('.', openParenIndex);
                int closeParenIndex = currPropertyName.indexOf(')', dotIndex);
                if (openParenIndex != -1 && dotIndex != -1 && closeParenIndex != -1) {
                     entityName = currPropertyName.substring(openParenIndex + 1, dotIndex);
                     propertyName = currPropertyName.substring(dotIndex + 1, closeParenIndex);
                     checkPropertyExistsInEntity(entityName, propertyName);
                }
                else {
                    throw new NoSuchProperyOnThisEntity("ticks doesn't write in the format. need to be ticks(entityName.propertyName) and got" + currPropertyName);
                }
                    if(mapOfPropertiesByNames.get(propertyName) == null) {
                    throw new NoSuchProperyOnThisEntity("In PRDAction: PRD-condition, PRDProperty is required" +
                            " but does not exists.");
                }
            }
            else if (mapOfPropertiesByNames.get(currPropertyName)==null)
                throw new NoSuchProperyOnThisEntity("In PRDAction: PRD-condition, PRDProperty is required" +
                        " but does not exists.");
            //else return true;
        }
         else {
            List<PRDCondition> conditionList = prdCondition.getPRDCondition();
            for (PRDCondition currConditionNode : conditionList)
                checkIfPropertyExistsInConditionAction(currConditionNode, mapOfPropertiesByNames);
        }
    }

    private void checkPropertyExistsInEntity(String entityName, String propertyName) throws NoSuchProperyOnThisEntity {
        boolean found = false;
        for(PRDEntity entity: prdWorld.getPRDEntities().getPRDEntity()) {
            if(entity.getName().equalsIgnoreCase(entityName)) {
                for(PRDProperty property: entity.getPRDProperties().getPRDProperty()) {
                    if(property.getPRDName().equalsIgnoreCase(propertyName)) {
                        found = true;
                    }
                }
                if(!found)
                    throw new NoSuchProperyOnThisEntity("The entity: " + entityName + " doesn't have property name: " + propertyName + " ,your arg don't valid.");
            }
        }
    }

}


