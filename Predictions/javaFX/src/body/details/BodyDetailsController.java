package body.details;

import body.details.KillDetails.KillController;
import body.details.calculationDetails.CalculationController;
import body.details.conditionDetails.MultipleConditionController;
import body.details.conditionDetails.SingleConditionController;
import body.details.decreaseDetails.DeacreaseController;
import body.details.increaseDetails.IncreaseController;
import body.details.proximityDetails.proximityController;
import body.details.replaceDetails.RepalceController;
import body.details.setDetails.SetController;
import dto.*;
import dto.action.*;
import dto.action.conditionDTO.MultipleConditionActionDTO;
import dto.action.conditionDTO.SingleConditionDTO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.VBox;
import main.AppController;
import resources.Resources;
import java.io.IOException;
import java.net.URL;

public class BodyDetailsController {

    private AppController appController;
    private WorldDTO worldDTO;

    @FXML
    private TreeView treeView;
    @FXML
    private VBox informationDetails;
    @FXML private Button displayDataBTN;


    public void setMainController(AppController appController) {
        this.appController = appController;
    }

    public void setDisplayDataBtnDisable(boolean toDisable){
        displayDataBTN.setDisable(toDisable);
    }
    public void showDetails() {
        this.worldDTO = appController.getWorldDTO();
        informationDetails.getChildren().removeAll();
        TreeItem<Object> rootItem = new TreeItem<>("Simulation's Information");
        TreeItem<Object> entities = new TreeItem<>("Entities");

        for (EntityDefinitionDTO entityDefinitionDTO : worldDTO.getEntityDefinitionDTOList()) {
            TreeItem<Object> entity = new TreeItem<>(entityDefinitionDTO.getName());
            for (EntityDefPropertyDTO propertyDTO : entityDefinitionDTO.getProperties()) {
                TreeItem<Object> property = new TreeItem<>(propertyDTO.getName());
                entity.getChildren().add(property);
            }
            entities.getChildren().add(entity);
        }

        TreeItem<Object> envVariables = new TreeItem<>("Environment Variables");
        for (PropertyDTO envProperty : worldDTO.getEnvVariableManagerDefinitionDTO().getPropertiesData()) {
            TreeItem<Object> env = new TreeItem<>(envProperty.getName());
            envVariables.getChildren().add(env);
        }

        TreeItem<Object> rules = new TreeItem<>("Rules");
        for (RuleDTO ruleDTO : worldDTO.getRulesList()) {
            TreeItem<Object> rule = new TreeItem<>(ruleDTO.getName());
            TreeItem <Object> Activation = new TreeItem<>("Activation");
            TreeItem<Object> actions = new TreeItem<>("Actions");

            for (ActionDTO actionDTO : ruleDTO.getActionsToPerform()) {
                TreeItem<Object> actionName = new TreeItem<>(actionDTO.getActionName());
                actions.getChildren().add(actionName);
            }
            rule.getChildren().addAll(actions, Activation);
            rules.getChildren().add(rule);
        }

        TreeItem<Object> termination = new TreeItem<>("Termination");

        rootItem.getChildren().addAll(entities, envVariables, rules, termination);
        treeView.setRoot(rootItem);
    }

    public TreeView getTreeView() {
        return treeView;
    }

    @FXML
    public void selectItem() {
        int index;
        TreeItem<Object> item = (TreeItem<Object>) treeView.getSelectionModel().getSelectedItem();

        if (item == null || item.getValue().equals("Simulation's Information"))
            return;

        String parent = (String) item.getParent().getValue();
        informationDetails.getChildren().clear();
        if (parent != null) {

                if (parent.equals("Simulation's Information") && !item.getValue().equals("Termination"))
                    return;

                for (EntityDefinitionDTO entityDefinitionDTO : worldDTO.getEntityDefinitionDTOList()) {
                    if (parent.equals(entityDefinitionDTO.getName())) {
                        EntityDefPropertyDTO entityDefPropertyDTO = entityDefinitionDTO.getPropertyByName(item.getValue().toString());
                        getPropertyEntityDetails(entityDefPropertyDTO);
                    }
                }
                if (parent.equals("Environment Variables")) {
                    PropertyDTO EnvProperty = worldDTO.getEnvVariablesDefinition().get(item.getValue().toString());
                    getEnvVariables(EnvProperty);

                }
                else if (item.getValue().equals("Termination")) {
                    getTerminationDetails(worldDTO.getTermination());
                }

                else {
                    for (RuleDTO ruleDTO : worldDTO.getRulesList()) {
                        if (item.getValue().equals("Activation")) {
                            if (ruleDTO.getName().equals(parent)) {
                                getActiovationDetails(ruleDTO.getActivation());
                            }
                        } else {
                            informationDetails.getChildren().removeAll();
                            String ruleName = (String) item.getParent().getParent().getValue();
                            int indexAction = item.getParent().getChildren().indexOf(item);
                            if (ruleName.equals(ruleDTO.getName())) {
                                ActionDTO actionDTO = ruleDTO.getActionsToPerform().get(indexAction);
                                createUIAdapter(actionDTO);
                            }
                        }
                    }
                }
            }
        }
    private void getTerminationDetails(TerminationDTO termination) {
        informationDetails.getChildren().add(new Label("Termination Details:"));
        informationDetails.getChildren().add(new Label("*********************"));
        if(termination.isTicksExist())
            informationDetails.getChildren().add(new Label("Ticks: " + termination.getTicksCount()));
            if(termination.isSecondsExist())
        informationDetails.getChildren().add(new Label("Probability: " + termination.getSecondsCount()));
            if(termination.isUserCanStop())
                informationDetails.getChildren().add(new Label("The simulation can stop by USER."));

    }

    public VBox getInformationDetails() {
        return informationDetails;
    }

    private void getActiovationDetails(ActivationDTO activation) {
        informationDetails.getChildren().add(new Label("Activation Details:"));
        informationDetails.getChildren().add(new Label("*********************"));
        informationDetails.getChildren().add(new Label("Ticks: " + activation.getTicks()));
        informationDetails.getChildren().add(new Label("Probability: " + activation.getProbability()));

        informationDetails.getChildren().add(new Label("-----------------"));
    }

    private void getEnvVariables(PropertyDTO propertyDTO) {

        informationDetails.getChildren().add(new Label("Environment Variable Property"));
        informationDetails.getChildren().add(new Label("*********************"));
        informationDetails.getChildren().add(new Label("Name: " + propertyDTO.getName()));
        informationDetails.getChildren().add(new Label("Type: " + propertyDTO.getType()));
        if (propertyDTO.isExistRange())
            informationDetails.getChildren().add(new Label("Range from: " + propertyDTO.getFromRange() + " to: " + propertyDTO.getToRange()));

        informationDetails.getChildren().add(new Label("-----------------"));
    }

    private void  getTicksTerminationDetails(int ticks) {
        informationDetails.getChildren().add(new Label("Termination Details: "));
        informationDetails.getChildren().add(new Label("*********************"));
        informationDetails.getChildren().add(new Label("The simulation end at max " + ticks + " ticks. "));
    }

    private void getSecondsTerminationDetails(int seconds) {
        informationDetails.getChildren().add(new Label("Termination Details: "));
        informationDetails.getChildren().add(new Label("*********************"));
        informationDetails.getChildren().add(new Label("The simulation end at max " + seconds + " seconds. "));
    }

    private void getPropertyEntityDetails(EntityDefPropertyDTO property) {

        informationDetails.getChildren().add(new Label("Entity Property"));
        informationDetails.getChildren().add(new Label("*********************"));
        informationDetails.getChildren().add(new Label("Property Name: " + property.getName()));
        informationDetails.getChildren().add(new Label("Property Type: " + property.getType().toString()));
        if (property.isExistRange())
            informationDetails.getChildren().add(new Label("Property Range from: " + property.getFromRange() + " to: " + property.getToRange()));

        informationDetails.getChildren().add(new Label("Is Random Initialize: " + property.isRandomInitialize()));

        if (!property.isRandomInitialize())
            informationDetails.getChildren().add(new Label("Init Value: " + property.getInitValue()));
    }

    private void createUIAdapter(ActionDTO actionDTO) {
        String actionName = actionDTO.getActionName();
        switch (actionName) {
            case "increase":
                createIncreaseController(actionDTO);
                break;
            case "decrease":
                createDeacreaseController(actionDTO);
                break;
            case "calculation":
                createCalculationController(actionDTO);
                break;
            case"kill":
                createKillController(actionDTO);
                break;
            case "set":
                createSetController(actionDTO);
                break;
            case "replace":
                createReplaceController(actionDTO);
                break;
            case "proximity":
                createProximityController(actionDTO);
                break;
            case "condition":
            if(actionDTO.getClass() == SingleConditionDTO.class)
                createSingleConditionController(actionDTO);
            else
                createMultipleConditionController(actionDTO);
                break;
        }
    }

    private void createMultipleConditionController(ActionDTO actionDTO) {
        try {
            FXMLLoader loader = new FXMLLoader();
            URL url = getClass().getResource(Resources.MULTIPLE_CONDITION_FXML_RESOURCE);
            loader.setLocation(url);
            Node tile = loader.load();
            MultipleConditionController actionController = loader.getController();
            MultipleConditionActionDTO multipleConditionActionDTO = ((MultipleConditionActionDTO) actionDTO);

            actionController.setActionTypeLabel("Multiple Condition");
            actionController.setPrimaryEntityLabel(multipleConditionActionDTO.getEntityName());
            if(multipleConditionActionDTO.isExistSecondary()) {
                actionController.setIsExistSecondaryLabel("true");
                actionController.setSecondaryEntityLabel(multipleConditionActionDTO.getEntitySecondary());
            } else
                actionController.setIsExistSecondaryLabel("false");
            String numOfConditions = String.valueOf(multipleConditionActionDTO.getSizeConditions());
            actionController.setLogicalLabel(multipleConditionActionDTO.getLogical());
            actionController.setNumOfConditionsLabel(numOfConditions);

            informationDetails.getChildren().add(tile);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createSingleConditionController(ActionDTO actionDTO) {
        try {
            FXMLLoader loader = new FXMLLoader();
            URL url = getClass().getResource(Resources.SINGLE_CONDITION_FXML_RESOURCE);
            loader.setLocation(url);
            Node tile = loader.load();
            SingleConditionController actionController = loader.getController();
            SingleConditionDTO singleConditionDTO = ((SingleConditionDTO) actionDTO);

            actionController.setActionTypeLabel("Single Condition");
            actionController.setPrimaryEntityLabel(singleConditionDTO.getEntityName());
            if(singleConditionDTO.isExistSecondary()) {
                actionController.setIsExistSecondaryLabel("true");
                actionController.setSecondaryEntityLabel(singleConditionDTO.getEntitySecondary());
            }
            else
                actionController.setIsExistSecondaryLabel("false");

            actionController.setOperatorLabel(singleConditionDTO.getOperator());
            actionController.setPropertyNameLabel(singleConditionDTO.getPropertyName());
            actionController.setValueLabel(singleConditionDTO.getValue());
            String thenSize = String.valueOf(singleConditionDTO.getSizeOfThenActions());
            String elseSize = String.valueOf(singleConditionDTO.getSizeOfElseActions());
            actionController.setNumOfActionsThenLabel(thenSize);
            actionController.setNumOfActionsElseLabel(elseSize);

            informationDetails.getChildren().add(tile);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createProximityController(ActionDTO actionDTO) {
        try {
            FXMLLoader loader = new FXMLLoader();
            URL url = getClass().getResource(Resources.PROXIMITY_FXML_RESOURCE);
            loader.setLocation(url);
            Node tile = loader.load();
            proximityController actionController = loader.getController();
            ProximityActionDTO proximityActionDTO = ((ProximityActionDTO) actionDTO);

            actionController.setActionTypeLabel(proximityActionDTO.getActionName());
            actionController.setSourceEntityLabel(proximityActionDTO.getSourceEntity());
            actionController.setTargetEntityLabel(proximityActionDTO.getTargetEntity());

            if(proximityActionDTO.isExistSecondary()) {
                actionController.setIsExistSecondaryEntityLabel("true");
                actionController.setSecondaryEntityLabel(proximityActionDTO.getEntitySecondary());
            } else
                actionController.setIsExistSecondaryEntityLabel("false");

                actionController.setDepthExpLabel(proximityActionDTO.getEnvDepthOf());
                String numOfActions = String.valueOf(proximityActionDTO.getNumOfActions());
                actionController.setNumOfActionsLabel(numOfActions);

                informationDetails.getChildren().add(tile);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createReplaceController(ActionDTO actionDTO) {
        try {
            FXMLLoader loader = new FXMLLoader();
            URL url = getClass().getResource(Resources.REPLACE_FXML_RESOURCE);
            loader.setLocation(url);
            Node tile = loader.load();
            RepalceController actionController = loader.getController();
            ReplaceActionDTO replaceActionDTO = ((ReplaceActionDTO) actionDTO);

            actionController.setActionTypeLabel(replaceActionDTO.getActionName());
            actionController.setKillEntityLabel(replaceActionDTO.getKillEntityName());
            actionController.setCreateEntityLabel(replaceActionDTO.getCreateEntityName());
            if(replaceActionDTO.isExistSecondary()) {
                actionController.setIsExistSecondaryLabel("true");
                actionController.setSecondaryEntityLabel(replaceActionDTO.getEntitySecondary());
            } else
                actionController.setIsExistSecondaryLabel("false");

            actionController.setModeLabel(replaceActionDTO.getMode());
            informationDetails.getChildren().add(tile);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createSetController(ActionDTO actionDTO) {
        try {
            FXMLLoader loader = new FXMLLoader();
            URL url = getClass().getResource(Resources.SET_FXML_RESOURCE);
            loader.setLocation(url);
            Node tile = loader.load();
            SetController actionController = loader.getController();
            SetActionDTO setActionDTO = (SetActionDTO) actionDTO;

            actionController.setSetActionTypeLabel(setActionDTO.getActionName());
            actionController.setSetActionPrimaryEntityLabel(setActionDTO.getEntityName());
            if(setActionDTO.isExistSecondary()) {
                actionController.setIsExistSecondaryEntityLabel("true");
                actionController.setSetSecondaryEntityLabel(setActionDTO.getEntitySecondary());
            } else
                actionController.setIsExistSecondaryEntityLabel("false");

            actionController.setSetActionPropertyNameLabel(setActionDTO.getPropertyName());
            actionController.setSetActionValueLabel(setActionDTO.getValue());

            informationDetails.getChildren().add(tile);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void createKillController(ActionDTO actionDTO) {
        try {
            FXMLLoader loader = new FXMLLoader();
            URL url = getClass().getResource(Resources.KILL_FXML_RESOURCE);
            loader.setLocation(url);
            Node tile = loader.load();
            KillController actionController = loader.getController();
            KillActionDTO killActionDTO = (KillActionDTO) actionDTO;

            actionController.setKillActionNameLabel(killActionDTO.getActionName());
            actionController.setKillEntityNameLabel(killActionDTO.getEntityName());

            if(killActionDTO.isExistSecondary()) {
                actionController.setKillExistSecondary("true");
                actionController.setKillSecondaryEntityLabel(killActionDTO.getEntitySecondary());
            } else
                actionController.setKillExistSecondary("false");

            informationDetails.getChildren().add(tile);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createCalculationController(ActionDTO actionDTO) {
        try {
            FXMLLoader loader = new FXMLLoader();
            URL url = getClass().getResource(Resources.CALCULATION_FXML_RESOURCE);
            loader.setLocation(url);
            Node tile = loader.load();
            CalculationController actionController = loader.getController();
            CalculationActionDTO calculationActionDTO = (CalculationActionDTO) actionDTO;

            actionController.SetCalculationActionName(calculationActionDTO.getActionName());
            actionController.setCalculationEntityNameLabel(calculationActionDTO.getEntityName());
            if (calculationActionDTO.isExistSecondary()) {
                actionController.setCalculationExistSecondaryLabel("true");
                actionController.setCalculationSecondaryLabel(calculationActionDTO.getEntitySecondary());
            } else
                actionController.setCalculationExistSecondaryLabel("false");

            actionController.setCalculationExpressionLabel(calculationActionDTO.getArg1(), calculationActionDTO.getArg2(), calculationActionDTO.getCalculationType());
            actionController.setCalculationResultPropLabel(calculationActionDTO.getResultProp());

            informationDetails.getChildren().add(tile);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createIncreaseController(ActionDTO actionDTO) {
        try {
            FXMLLoader loader = new FXMLLoader();
            URL url = getClass().getResource(Resources.INCREASE_FXML_RESOURCE);
            loader.setLocation(url);
            Node tile = loader.load();
            IncreaseController actionController = loader.getController();
            IncreaseActionDTO increaseActionDTO = (IncreaseActionDTO) actionDTO;

            actionController.setTextTypeLabel(increaseActionDTO.getActionName());
            actionController.setTextPrimaryLabel(increaseActionDTO.getEntityName());
            if(increaseActionDTO.isExistSecondary()) {
                actionController.setIsExistSecondaryEntity("true");
                actionController.setIncreaseExistSecondaryEntity(increaseActionDTO.getEntitySecondary());
            }
            else {
                actionController.setIsExistSecondaryEntity("false");
            }
            actionController.setPropertyName(increaseActionDTO.getPropertyName());
            actionController.setIncreaseByExp(increaseActionDTO.getBy());

            informationDetails.getChildren().add(tile);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createDeacreaseController(ActionDTO actionDTO) {
        try {
            FXMLLoader loader = new FXMLLoader();
            URL url = getClass().getResource(Resources.DEACRESE_FXML_RESOURCE);
            loader.setLocation(url);
            Node tile = loader.load();
            DeacreaseController actionController = loader.getController();
            DecreaseActionDTO deacreaseActionDTO = (DecreaseActionDTO) actionDTO;

            actionController.setTextTypeLabel(deacreaseActionDTO.getActionName());
            actionController.setTextPrimaryLabel(deacreaseActionDTO.getEntityName());
            if(deacreaseActionDTO.isExistSecondary()) {
                actionController.setIsExistSecondaryEntity("true");
                actionController.setExistSecondaryEntity(deacreaseActionDTO.getEntitySecondary());
            }
            else {
                actionController.setIsExistSecondaryEntity("false");
            }
            actionController.setPropertyName(deacreaseActionDTO.getPropertyName());
            actionController.setIncreaseByExp(deacreaseActionDTO.getBy());

            informationDetails.getChildren().add(tile);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
