import dto.*;
import dto.HistoryDataDTO.CountEntitiesDTO;
import dto.HistoryDataDTO.HistogramProperiesDTO;
import dto.HistoryDataDTO.HistoryDataDTO;
import entity.Type;

import javafx.util.Pair;
import simulations.SimulationsManager;
import world.World;

import java.util.*;

public class Console {
    private SimulationsManager simulationsManager;
    private World world;
    private Menu menu;

    public Console() {
        simulationsManager = new SimulationsManager();
        world = new World();
        menu = new Menu();
    }

    private boolean isNoFileButNeeded(int choice) {
        if (!world.isFileExist())
            return choice >= 2 && choice < 5;
        return false;
    }

    public void readFile() {
        world = new World();
        String fileName;
        fileName = menu.getFileName();
        try {
            //world.readFile(fileName);
            System.out.println("File was read successfully!");

        } catch (Exception e) {
            handleErrorMessage(e);
            System.out.println("File was not read! an error was happened.");
        }
        if(simulationsManager.getNumOfSimulations()!=0) {
            simulationsManager.removeAllSimulations();

        }
    }

    private void handleErrorMessage(Exception e) {
        System.out.println(e.getMessage());
    }

    public void start() {
        System.out.println("Welcome to Predictions system ! ! ! ");
        int choice;
        boolean exit = false;
        do {
            choice = menu.getMainChoice();
            if (isNoFileButNeeded(choice)) {
                System.out.println("There is no file but is needed!");
                continue;
            }
            switch (choice) {
                case 1:
                    readFile();
                    break;
                case 2:
                    printWorldDetails();
                    break;
                case 3:
                    startSimulation();
                    break;
                case 4:
                    showHistorySimulations();
                    break;
                case 5:
                    exit = true;
                    break;
            }
        } while (!exit);
        if(choice == 5) {
            System.out.println("Goodbye, see you soon ... :) ");
        }
    }

    private void showHistorySimulations() {
        int entityNum;
        if(simulationsManager.getNumOfSimulations() == 0) {
            System.out.println("There is not simulation to show details. You need to run at least one simulation before.");
            return;
        }
        WorldDTO worldDTO = world.createWorldDTO();
        System.out.println("*********************************");
        SimulationsDTO simulationsDTOData = simulationsManager.createSimulationsDTO();
        //int userSimulationChoose = printDataAndIdForPastSimulations(simulationsDTOData);
        int choose = menu.getWayToDisplayDetails();
        if(choose ==3)
            return;

        HistoryDataDTO historyDataDTO = null;

        System.out.println("Please choose the entity number you want to see: ");
       /* for(int i=0;i<worldDTO.getEntityDefinitionDTOList().size();i++) {
            System.out.println(i+1 + ". " + worldDTO.getEntity(i).getName());
        }*/
        entityNum = menu.getNumberFromUser(world.getEntityDefinitionList().size());
        //historyDataDTO = simulationsManager.createSimulationDTOHistory(userSimulationChoose, choose, entityNum);
        printHistoryData(historyDataDTO);
      /*  switch (choose) {
            case 1:
                historyDataDTO = simulations.createSimulationDTOHistory(userSimulationChoose, choose, entityNum);
                break;
            case 2:
                historyDataDTO = simulations.createSimulationDTOHistory(userSimulationChoose, choose, entityNum);
                break;
            case 3:
                return;
        }*/
      //  printHistoryData(historyDataDTO);
    }

    private void printHistoryData(HistoryDataDTO historyDataDTO) {

        if(historyDataDTO.getClass() == CountEntitiesDTO.class) {
            for (String name: ((CountEntitiesDTO) historyDataDTO).getEntitiesHistoryCount().keySet()) {
                Pair<Integer, Integer> data = ((CountEntitiesDTO) historyDataDTO).getDetailOnEntity(name);
                System.out.println("****************");
                System.out.println("For entity name: " + name);
                System.out.println("before count: " + data.getKey());
                System.out.println("after count: " + data.getValue());
            }
        } else if(historyDataDTO.getClass() == HistogramProperiesDTO.class) {
            if(((HistogramProperiesDTO) historyDataDTO).getSizeOfInstances() == 0) {
                System.out.println("There are no entities left after running this simulation to view information.");
                return;
            }
            int nameIndex = 0;
           /* System.out.println("List of entities names: ");
                for(String name: ((HistogramProperiesDTO) historyDataDTO).getNames()) {
                    System.out.println(nameIndex+1 + ". " + name);
                    nameIndex++;
            }
            System.out.println("Please the number of entity you want to see:");
            int NameChoose = menu.getNumberFromUser(((HistogramProperiesDTO) historyDataDTO).getSizeOfInstances());*/

                Collection <EntityPropertyInstanceDTO> propertyDTOList = ((HistogramProperiesDTO) historyDataDTO).getListProperties(0);
                int propertyIndex=0;
                System.out.println("****************");
            System.out.println("List of properties names: ");
            for (EntityPropertyInstanceDTO entityPropertyInstanceDTO: propertyDTOList){
                System.out.println(propertyIndex+1 + ". " + entityPropertyInstanceDTO.getName());
                propertyIndex++;
            }
            System.out.println("Please the number of property you want to see:");
            int propertyChoose = menu.getNumberFromUser(propertyDTOList.size());
            Map<Object, Integer> propertiesDetails = historyDataDTO.getHistogramDetailOnPropery(0 ,propertyChoose);
            printSummeryHistogram(propertiesDetails);
        }
    }

    private void printSummeryHistogram(Map<Object, Integer> propertiesDetails) {
        for (Map.Entry<Object, Integer> entry : propertiesDetails.entrySet()) {
            System.out.println("Value: " + entry.getKey() + ", Count: " + entry.getValue());
        }
    }

   /* private int printDataAndIdForPastSimulations(SimulationsDTO simulationsDTOData) {
        Map<String, String> details = simulationsDTOData.getSimulationsDateAndID();
        int i = 0;
        System.out.println("The details of the saved simulations: ");
        for(String date: details.keySet()) {
            System.out.println(i+1 + "." + "Date run simulation: " + date);
            System.out.println("The id of this run: " + details.get(date));
            i++;
        }
        System.out.println("Please choose run number to show history:");
        int res = menu.getUserChooseToSeeSimulationResults(simulationsManager.getSimulationsDetails().size());
        return res;
    }*/

    private void startSimulation()  {
        try {
            WorldDTO worldDTO = world.createWorldDTO();
            EnvironmentDataDTO environmentDataDTO = prepareSimulation();
            Map<String, EnvVariablesIncstanceDTO> envVariablesIncstanceDTOList = createEnvironmentVariablesWithValues(worldDTO, environmentDataDTO);
            printEnvVariablesInstances(envVariablesIncstanceDTOList);
            SimulationDTO simulationDTO = simulationsManager.start(world, envVariablesIncstanceDTOList);
            printSimulationEndDetails(simulationDTO);
        }catch (Exception e) {

        }
    }

    private void printSimulationEndDetails(SimulationDTO simulationDTO) {
        System.out.println("The id of simulation: " + simulationDTO.getId());
        System.out.println("End by reason: " + simulationDTO.getTerminationReason());
    }

    private void printEnvVariablesInstances(Map<String, EnvVariablesIncstanceDTO> envVariablesIncstanceDTOList) {
        System.out.println("*********************************************");
        System.out.println("Environment Variables:");

        for(EnvVariablesIncstanceDTO envVariablesIncstanceDTO: envVariablesIncstanceDTOList.values()) {
            System.out.println("Name:" + envVariablesIncstanceDTO.getName());
            System.out.println("Type: " + envVariablesIncstanceDTO.getType());
            if(envVariablesIncstanceDTO.isExistRange()) {
                System.out.println("range from: " + envVariablesIncstanceDTO.getFromRange());
                System.out.println(" to: " + envVariablesIncstanceDTO.getToRange());
            }
            printValue(envVariablesIncstanceDTO.getValue(), envVariablesIncstanceDTO.getType());
        }
    }

    private void printValue(Object value, String type) {
        if(type.equals("decimal"))
            System.out.println("value: " + (int)value);
        else if(type.equals("float"))
            System.out.println("value: " + (float)value);
        else if(type.equals("boolean"))
            System.out.println("value: " + (boolean)value);
        else
            System.out.println("value: " + value);
    }

    private Map<String, EnvVariablesIncstanceDTO> createEnvironmentVariablesWithValues(WorldDTO worldDTO, EnvironmentDataDTO environmentDataDTO) throws Exception {
        Map<String, EnvVariablesIncstanceDTO> envVariablesInstanceDTOList = new HashMap<>();

        Map<String, PropertyDTO> propertyDTOList = worldDTO.getEnvVariablesDefinition();

        for(PropertyDTO propertyDTO: propertyDTOList.values()) {
            Object value = environmentDataDTO.getValue(propertyDTO.getName());
            if(value == null)
                value = setValueRandom(propertyDTO);

            EnvVariablesIncstanceDTO variableInstanceDTO = new EnvVariablesIncstanceDTO(propertyDTO.getName(), propertyDTO.getType(),
                                            propertyDTO.isExistRange(), propertyDTO.getFromRange(), propertyDTO.getToRange(), value, true);
            envVariablesInstanceDTOList.put(propertyDTO.getName(),variableInstanceDTO);
        }

        return envVariablesInstanceDTOList;
    }

    private EnvironmentDataDTO prepareSimulation() {
        int choose =1;
        WorldDTO worldDTO = world.createWorldDTO();
        EnvironmentDataDTO environmentDataDTO;
        System.out.println("********************");
        System.out.println("Environment variables:");
        System.out.println("note: if you not choose some environment to enter a value, the system will random value for you.");
        Map<String, Object> envInitMap = new HashMap<>();
        Map<String, PropertyDTO> envVariableManagerDefinitionDTO = worldDTO.getEnvVariablesDefinition();

            while (choose != 0) { //end with 0
                for (Map.Entry<String, PropertyDTO> entry : envVariableManagerDefinitionDTO.entrySet()) {
                    System.out.println(entry.getKey()  + ". Name: " + entry.getValue().getName());
                }
                System.out.println("Please choose the env number to want to enter value: ");
                System.out.println("Enter 0 to finish set the environment variables and start the simulation.");
                choose = menu.getNumberFromUserWithZeroToFinish(envVariableManagerDefinitionDTO.size());
                if(choose != 0 ) {
                    PropertyDTO propertyDTO = envVariableManagerDefinitionDTO.get(choose);
                    System.out.println("----------------");
                    System.out.println("Name: " + propertyDTO.getName());
                    System.out.println("Type: " + propertyDTO.getType().toString());
                    if(propertyDTO.isExistRange())
                        System.out.println("Range from: " + propertyDTO.getFromRange() + "to: " + propertyDTO.getToRange());

                    System.out.println("Please enter the value: ");
                    Object value = checkValidEnvVal(propertyDTO.getType(), propertyDTO);

                    if(envInitMap.containsKey(propertyDTO.getName()))
                        envInitMap.replace(propertyDTO.getName(), value);
                    else
                        envInitMap.put(propertyDTO.getName(), value);
                }
            }
    //return hashmap with the names of environment that the user set their value.
        environmentDataDTO = new EnvironmentDataDTO(envInitMap);
        return environmentDataDTO;
    }

    //this function checks the user input for environment variable is valid(related to the type of the environment)
    private Object checkValidEnvVal (String type, PropertyDTO propertyDTO) {
        boolean validValue = false;
        Object res = null;
        Pair<Boolean, Object> resPair =null;
        do {
            String value = menu.getEnvValue();
            switch (type) {
                case "decimal":
                    try {
                         res = Integer.parseInt(value);
                         if(propertyDTO.isExistRange()) { //check the user's value in range
                             if(propertyDTO.getFromRange() <= (int)res && ((int)res <= propertyDTO.getToRange()))
                                 validValue = true;
                             else {
                                 System.out.println("the value not in range.");
                                 validValue = false;
                             }
                         } else
                            validValue = true;
                    } catch (Exception e) {
                        System.out.println("Invalid value. you need to enter a decimal number. ");
                    }
                    break;
                case "float":
                    try {
                         res = Float.parseFloat(value);
                        if(propertyDTO.isExistRange()) { //check the user's value in range
                            if(propertyDTO.getFromRange() <= (float)res && ((float)res <= propertyDTO.getToRange()))
                                validValue = true;
                            else {
                                System.out.println("the value not in range.");
                                validValue = false;
                            }
                        } else
                            validValue = true;

                    } catch (Exception e) {
                        System.out.println("Invalid value. You need to enter a float number.");
                    }
                    break;
                case "boolean":
                    try {
                         res = Boolean.parseBoolean(value);
                        validValue = true;
                    } catch (Exception e) {
                        validValue = false;
                        System.out.println("Invalid value. You need to enter true/false value");
                    }
                    break;
                case "string":
                    try {
                         res = value;
                        validValue = true;
                    } catch (Exception e) {
                        validValue = false;
                        System.out.println("Invalid value.");
                    }
                    break;
            }
        } while (!validValue);
        return res;
    }


    private void printWorldDetails() {
        WorldDTO worldDTO = world.createWorldDTO();
        printEntitiesDetails(worldDTO);
        printRulesDetails(worldDTO);
        printTerminationDetail(worldDTO);
    }

    private void printTerminationDetail(WorldDTO worldDTO) {
        System.out.println("*********************************");
        System.out.println("Termination:");
        TerminationDTO termination = worldDTO.getTermination();
        if(termination.isSecondsExist() && termination.isTicksExist()) {
            System.out.println("The simulation end with: " + termination.getTicksCount() + " ticks");
            System.out.println("or end with: " + termination.getSecondsCount() + " seconds");
        }
        else if(termination.isTicksExist())
            System.out.println("The simulation end with: " + termination.getTicksCount() + " ticks");
        else if(termination.isSecondsExist())
            System.out.println("The simulation end with: " + termination.getSecondsCount() + " seconds");
    }

    private void printRulesDetails(WorldDTO worldDTO) {
        System.out.println("****************************");
        System.out.println("Rules:");
        List <dto.RuleDTO> rulesList = worldDTO.getRulesList();
        for(RuleDTO rule: rulesList) {
            System.out.println("Rule Name: " + rule.getName());
            System.out.println("The rule active in: ");
            System.out.println("Ticks: " + rule.getActivation().getTicks());
            System.out.println("Probability: " + rule.getActivation().getProbability());
            System.out.println("Num of actions: " + rule.getActionsToPerform().size());
            /*for(String name: rule.getActionsToPerform()) {
                System.out.println("Action name: " + name);
            }*/
        }
    }

    private void printEntitiesDetails(WorldDTO worldDTO) {
        System.out.println("****************************");
        System.out.println("Entities:");
       /* for (int i = 0; i < worldDTO.getEntityDefinitionDTOList().size(); i++) {
            System.out.println("Entity #" + i+1 + " : ");
            EntityDefinitionDTO entityDefinition = worldDTO.getEntity(i);
            System.out.println("Name: " + entityDefinition.getName());
            System.out.println("count population: " + entityDefinition.getPopulation());
            System.out.println("List properties: ");
            for (EntityDefPropertyDTO property : entityDefinition.getProperties()) {
                System.out.println("Name property: " + property.getName());
                System.out.println("Type property: " + property.getType().toString());
                if (property.isExistRange()) {
                    if (property.getType() == Type.DECIMAL.toString())
                        System.out.println("Range: from " + (int) property.getFromRange() + " to: " + (int) property.getToRange());
                    else
                        System.out.println("Range: from " + property.getFromRange() + " to: " + property.getToRange());
                }
                System.out.println("Is random initialize: " + property.isRandomInitialize());
            }
        }*/
    }
    //this function random a value for environment variable
    public Object setValueRandom(PropertyDTO propertyDTO) throws Exception {
        Random random = new Random();
        if (propertyDTO.getType() == Type.DECIMAL.toString()) {
                int res;
                if (propertyDTO.isExistRange()) {
                     res = (int) propertyDTO.getFromRange() +
                            random.nextInt((int) propertyDTO.getToRange() - (int) propertyDTO.getFromRange() + 1);
                } else {
                     res = random.nextInt();
                }
                return res;
        } else if (propertyDTO.getType() == Type.FLOAT.toString()) {
                float res;
                if (propertyDTO.isExistRange()) {
                    res = propertyDTO.getFromRange() +
                            random.nextFloat() * (propertyDTO.getToRange() - propertyDTO.getFromRange());
                } else {
                    res = random.nextFloat();
                }
                return res;
        } else if (propertyDTO.getType() == Type.BOOLEAN.toString()) {
                boolean res = random.nextBoolean();
                return res;
        } else if (propertyDTO.getType() == Type.STRING.toString()) {
                int size = random.nextInt(50); //random size between 1 to 50
                String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+~`|}{[]:;?><,./-= ";
                StringBuilder stringBuilder = new StringBuilder();

                for (int i = 0; i < size; i++) {
                    int randomIndex = random.nextInt(characters.length());
                    stringBuilder.append(characters.charAt(randomIndex));
                }
                String res = stringBuilder.toString();
                return res;
        } else {
            throw new Exception("error while trying to random for enviroment variable: " + propertyDTO.getName());
        }
    }
}
