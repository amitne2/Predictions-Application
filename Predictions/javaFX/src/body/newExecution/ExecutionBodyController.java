package body.newExecution;

import dto.*;
import entity.Type;
import environment.EnvVariableManagerImpl;
import exception.OutOfRangeException;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.util.Pair;
import main.AppController;
import resources.Resources;
import simulations.simulationExecution.SimulationExecution;
import simulations.simulationExecution.simulation.Simulation;

import java.util.Random;

public class ExecutionBodyController {
private AppController appController;
@FXML private Button clearBtn;
@FXML private Button startBtn;
@FXML private VBox entitiesDetails;
@FXML private VBox envDetails;
@FXML private Button prepareSimulation;
@FXML private VBox errorVBox;

    @FXML
    public void initialize(){
        startBtn.setStyle("-fx-background-color: -fx-shadow-highlight-color, -fx-outer-border, -fx-inner-border, -fx-body-color;");
        clearBtn.setStyle("-fx-background-color: -fx-shadow-highlight-color, -fx-outer-border, -fx-inner-border, -fx-body-color;");
        prepareSimulation.setStyle("-fx-background-color: -fx-shadow-highlight-color, -fx-outer-border, -fx-inner-border, -fx-body-color;");
    }
    public void setMainController(AppController appController) {
        this.appController = appController;
    }

    @FXML
    public void clearData(){ //clear entities and env data
        entitiesDetails.getChildren().clear();
        envDetails.getChildren().clear();
        envDetails.getChildren().removeAll();
        prepareSimulation.setDisable(false);
        errorVBox.getChildren().clear();
    }

    @FXML
    public void prepareDataForSimulation() {
        prepareSimulation.setDisable(!prepareSimulation.isDisable());
        setDataEntities();
        setEnvDetails();
    }

    public void setDataEntities(){
        WorldDTO worldDTO = appController.getWorldDTO();
        for(EntityDefinitionDTO entityDefinitionDTO: worldDTO.getEntityDefinitionDTOList()) {
            VBox entityVbox = new VBox();
            Label entityName = new Label(entityDefinitionDTO.getName());
            TextField populationTF = new TextField();
            populationTF.setId(entityDefinitionDTO.getName());
            populationTF.setPromptText("enter decimal number..");
            entityVbox.getChildren().addAll(entityName, populationTF);
            entitiesDetails.getChildren().add(entityVbox);
        }
    }

    public void setEnvDetails(){
        WorldDTO worldDTO = appController.getWorldDTO();
        envDetails.getChildren().clear();
        String rangeText;
        for(PropertyDTO envProperty: worldDTO.getEnvVariableManagerDefinitionDTO().getPropertiesData()) {
            VBox envBox = new VBox();
            Label envName = new Label(envProperty.getName());
            envBox.getChildren().add(envName);

            Label envType = new Label("Type: " + envProperty.getType());
            envBox.getChildren().add(envType);

            if (envProperty.isExistRange()) {
                if(envProperty.getType().equals("decimal")) {
                     rangeText = "Range from: " + Math.round(envProperty.getFromRange()) + " to: " + Math.round(envProperty.getToRange());
                } else {
                     rangeText = "Range from: " + envProperty.getFromRange() + " to: " + envProperty.getToRange();
                }
                Label envRange = new Label(rangeText);
                envBox.getChildren().add(envRange);
            }

            // Create a CheckBox
            CheckBox checkBoxRandomValue = new CheckBox("Random Value for this env");
            TextField envValue = new TextField();
            envValue.setPromptText("enter env value..");
            envValue.setId(envProperty.getName());
            envBox.getChildren().addAll(checkBoxRandomValue, envValue);

            checkBoxRandomValue.setOnAction(event -> {
                if (checkBoxRandomValue.isSelected()) {
                    // Disable envValue when the checkbox is selected
                    envValue.setDisable(true);
                }
                else {
                    envValue.setDisable(false);
                }
            });
            envDetails.getChildren().add(envBox);
        }
    }

public void validateDataForStartSimulation() throws Exception {
    WorldDTO worldDTO = appController.getWorldDTO();
    int totalEntities = worldDTO.getGridCols() * worldDTO.getGridRows();
    int currentCount=0;
    errorVBox.getChildren().clear();
    int index = 0;
    boolean passMaxEntities = false;

    while (entitiesDetails.getChildren().size() != (index)) {
        VBox entity = (VBox) entitiesDetails.getChildren().get(index);
        TextField entityPopulation = (TextField) entity.getChildren().get(1);

        if(entityPopulation.getText().isEmpty()) {
            entityPopulation.clear();
            appController.setPopulation(entityPopulation.getId(), 0); //default value when not enter
        }
        else if (!(entityPopulation.getText().matches("\\d+"))) { //contains characters
            entityPopulation.clear();
            Label entityContainCharachers = new Label("entity " + entityPopulation.getId() + " contain characters in population field.");
            errorVBox.getChildren().add(entityContainCharachers);
        }
        else {
            int population = StringToInt(entityPopulation.getText(), 0, totalEntities, errorVBox);
            if(population == Resources.NO_LIMIT) {
                Label label = new Label("The input isn't decimal number" + entityPopulation.getText());
                errorVBox.getChildren().add(label);
            }
            currentCount += population;

            if(currentCount > totalEntities && !passMaxEntities) {
                Label maxEntitiesCount = new Label("The max entities count is: " + totalEntities +
                        ". You reach to: " + currentCount + " entities, please remove a few.");
                errorVBox.getChildren().add(maxEntitiesCount);
                passMaxEntities = true;
            } else if(!passMaxEntities) {
                appController.setPopulation(entityPopulation.getId(), population);
            }
        }
        index++;
    }
    Object value;
    index = 0;
    while (envDetails.getChildren().size() != index) {
        VBox envVbox = (VBox) envDetails.getChildren().get(index);
        Label envName = (Label) envVbox.getChildren().get(0);
        PropertyDTO envProperty = worldDTO.getEnvVariableManagerDefinitionDTO().getEnvProperty(envName.getText());
        CheckBox randomValue;
        TextField initValueTF;

        if(envProperty.isExistRange()){
            randomValue = (CheckBox) envVbox.getChildren().get(3);
            initValueTF = (TextField) envVbox.getChildren().get(4);
        }
        else {
            randomValue = (CheckBox) envVbox.getChildren().get(2);
            initValueTF = (TextField) envVbox.getChildren().get(3);
        }

        if(randomValue.isSelected()){ //random value is selected
            //random value
            value = setValueRandom(envProperty);
            appController.setEnvValue(envProperty, value, true);
        }
        else {
            if(initValueTF.getText().isEmpty()){
                initValueTF.clear();
                Label initValueEmpty = new Label("You didn't enter init value for environment: " + initValueTF.getId() + ".");
                errorVBox.getChildren().add(initValueEmpty);
            }
            else { //check init value
                String valueTF = initValueTF.getText().toString();
                value = checkValidEnvVal(envProperty.getType(), envProperty, valueTF);
                appController.setEnvValue(envProperty, value, false);
            }
        }
        index++;
    }
}
    @FXML
    public void start() throws Exception {
        validateDataForStartSimulation();
        if(errorVBox.getChildren().size() == 0) { //no error in the vbox
            Label finish = new Label("You finish enter the data, now the simulation will start to run!");
            errorVBox.getChildren().add(finish);
            entitiesDetails.getChildren().clear();
            envDetails.getChildren().clear();
            errorVBox.getChildren().clear();
            prepareSimulation.setDisable(false);

            appController.moveToResultScreen();
            appController.runSimulation();
        }
    }

    private int StringToInt(String text, int low, int high, VBox errorVBox) {
        int amount = 0;
        if (text.isEmpty())
            return amount; //return 0 for population

        try {
            amount = Integer.parseInt(text);
                if(amount < low) {
                    Label amountLower = new Label("The amount of entity if lower than 0");
                    errorVBox.getChildren().add(amountLower);
                }

                if(high != Resources.NO_LIMIT) {
                    if (amount > high) {
                        Label amountHigher = new Label("The amount of entity is bigger than the max. the max is" + high);
                        errorVBox.getChildren().add(amountHigher);
                    }
                }
        } catch (Exception e) {
           return Resources.NO_LIMIT;
        }
        return amount;
    }



    private Object checkValidEnvVal (String type, PropertyDTO propertyDTO, String value) {
        boolean validValue = false;
        Object res = null;
        Pair<Boolean, Object> resPair =null;
            switch (type) {
                case "decimal":
                    try {
                        float tmpRes = Float.parseFloat(value);
                        if (Float.parseFloat(value) % 1.0 == 0)
                            res = Math.round(tmpRes);
                        else
                            res = Integer.parseInt(value);

                        if(propertyDTO.isExistRange()) { //check the user's value in range
                            if(propertyDTO.getFromRange() <= (int)res && ((int)res <= propertyDTO.getToRange()))
                                validValue = true;
                            else {
                                Label error = new Label("Env value not in range for env: " + propertyDTO.getName());
                                errorVBox.getChildren().add(error);
                                validValue = false;
                            }
                        } else
                            validValue = true;
                    } catch (Exception e) {
                        Label error = new Label("Invalid value. got " + value + " but you need to enter a decimal number for env" + propertyDTO.getName());
                        errorVBox.getChildren().add(error);
                    }
                    break;
                case "float":
                    try {
                        res = Float.parseFloat(value);
                        if(propertyDTO.isExistRange()) { //check the user's value in range
                            if(propertyDTO.getFromRange() <= (float)res && ((float)res <= propertyDTO.getToRange()))
                                validValue = true;
                            else {
                                Label error = new Label("Env value not in range for env: " + propertyDTO.getName());
                                errorVBox.getChildren().add(error);
                                validValue = false;
                            }
                        } else
                            validValue = true;

                    } catch (Exception e) {
                        Label errorFloat = new Label("Invalid value, got " + value + " but you need to enter a float number for env " + propertyDTO.getName());
                        errorVBox.getChildren().add(errorFloat);
                    }
                    break;
                case "boolean":
                    try {
                        if(value.toString().equals("true") || value.toString().equals("false")) {
                            res = Boolean.parseBoolean(value);
                            validValue = true;
                        } else {
                            validValue = false;
                            Label errorBoolean = new Label("Invalid value. you need to enter a true/false value for env " + propertyDTO.getName());
                            errorVBox.getChildren().add(errorBoolean);
                        }
                    } catch (Exception e) {
                        validValue = false;
                        Label errorBoolean = new Label("Invalid value. you need to enter a true/false value for env " + propertyDTO.getName());
                        errorVBox.getChildren().add(errorBoolean);
                    }
                    break;
                case "string":
                    try {
                        res = value;
                        validValue = true;
                    } catch (Exception e) {
                        validValue = false;
                        Label errorString = new Label("Invalid value in env: " + propertyDTO.getName() + " need to be string.");
                        errorVBox.getChildren().add(errorString);
                    }
                    break;
            }
        return res;
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

    public void reRunSimulation() {
       prepareSimulation.setDisable(true);
        setEntitiesDataWithValues();
        setEnvDetailsWithData();
    }

    private void setEnvDetailsWithData() {
        EnvVariableManagerImpl envVariableManager= appController.getCurrSimulation().getSimulation().getEnvVariablesManager();
        WorldDTO worldDTO = appController.getWorldDTO();
        envDetails.getChildren().clear();
        String rangeText;
        for(PropertyDTO envProperty: worldDTO.getEnvVariableManagerDefinitionDTO().getPropertiesData()) {
            VBox envBox = new VBox();
            Label envName = new Label(envProperty.getName());
            envBox.getChildren().add(envName);

            Label envType = new Label("Type: " + envProperty.getType());
            envBox.getChildren().add(envType);

            if (envProperty.isExistRange()) {
                if(envProperty.getType().equals("decimal")) {
                    rangeText = "Range from: " + Math.round(envProperty.getFromRange()) + " to: " + Math.round(envProperty.getToRange());
                } else {
                    rangeText = "Range from: " + envProperty.getFromRange() + " to: " + envProperty.getToRange();
                }
                Label envRange = new Label(rangeText);
                envBox.getChildren().add(envRange);
            }

            // Create a CheckBox
            CheckBox checkBoxRandomValue = new CheckBox("Random Value for this env");
            TextField envValue = new TextField();
            envValue.setId(envProperty.getName());
            envBox.getChildren().addAll(checkBoxRandomValue, envValue);

            if(envVariableManager.envWasRandom(envProperty.getName()))
                checkBoxRandomValue.setSelected(true);
            else
                envValue.setText(String.valueOf(envVariableManager.getEnvValue(envProperty.getName())));
            checkBoxRandomValue.setOnAction(event -> {
                if (checkBoxRandomValue.isSelected()) {
                    // Disable envValue when the checkbox is selected
                    envValue.setDisable(true);
                }
                else {
                    envValue.setDisable(false);
                }
            });
            envDetails.getChildren().add(envBox);
        }
    }

    private void setEntitiesDataWithValues() {
        SimulationExecution simulationExecution = appController.getCurrSimulation();
        WorldDTO worldDTO = appController.getWorldDTO();
        for(EntityDefinitionDTO entityDefinitionDTO: worldDTO.getEntityDefinitionDTOList()) {
            VBox entityVbox = new VBox();
            Label entityName = new Label(entityDefinitionDTO.getName());
            TextField populationTF = new TextField();
            populationTF.setId(entityDefinitionDTO.getName());
            populationTF.setText(String.valueOf(simulationExecution.getSimulation().getWorld().getEntity(entityDefinitionDTO.getName()).getPopulation()));
            entityVbox.getChildren().addAll(entityName, populationTF);
            entitiesDetails.getChildren().add(entityVbox);
        }
    }
    public Button getPrepareButton(){
        return prepareSimulation;
    }
}
