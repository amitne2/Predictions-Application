package rule;

import EntityInstanceManager.EntityInstanceManagerImpl;
import action.*;
import action.condition.multipleCondition;
import action.condition.singleCondition;
import context.Context;
import dto.*;
import dto.action.*;
import dto.action.conditionDTO.MultipleConditionActionDTO;
import dto.action.conditionDTO.SingleConditionDTO;
import entity.instance.EntityInstanceImpl;
import environment.EnvVariableManagerImpl;
import grid.GridInstances;
import world.World;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Rule {
    private String name;
    private Activation activation;
    private List<Action> actionsToPerform;

    public Rule(String name, Activation activation, List<Action> actions) {
        this.name = name;
        this.activation = activation;
        this.actionsToPerform = actions;
    }

    public Rule(String name) {
        this.name = name;
        this.activation = new Activation();
        this.actionsToPerform = new ArrayList<>();
    }

    public List<Action> getActionsToPerform() {
        return actionsToPerform;
    }

    public String getName() {
        return name;
    }

    public boolean checkIfRuleOperable(long currentTick) {
        Random random = new Random();
        double randomProb = random.nextDouble();
        if (currentTick % activation.getTicks() == 0) {
            if (randomProb < activation.getProbability()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        String res = "Rule Name: " + name + ", activation: " + activation.toString() + "List actions: ";
        for (Action action : actionsToPerform) {
            res = res + action.getActionName();
        }
        return res;
    }

    public void addAction(Action action) {
        actionsToPerform.add(action);
    }

    public void startActions(Context context) throws Exception {
        for (Action action : actionsToPerform) {
            action.doAction(context);
        }
    }

    public Activation getActivation() {
        return activation;
    }

    public RuleDTO createRuleDTO() {
        ActivationDTO activationDTO = new ActivationDTO(activation.getTicks(), activation.getProbability());
       List<ActionDTO> actionDTOList = new ArrayList<>();
       for(Action action: actionsToPerform) {
           switch (action.getActionName()) {
               case "increase" :
                   ActionDTO actionIncrease = new IncreaseActionDTO((IncreaseAction) action);
                   actionDTOList.add(actionIncrease);
                   break;
               case "decrease" :
                   ActionDTO actionDecrease = new DecreaseActionDTO((DecreaseAction) action);
                   actionDTOList.add(actionDecrease);
                   break;
               case "calculation":
                   ActionDTO actionCalculation = new CalculationActionDTO((CalculationAction) action);
                   actionDTOList.add(actionCalculation);
                   break;
               case "set":
                   ActionDTO actionSet = new SetActionDTO((SetAction) action);
                   actionDTOList.add(actionSet);
                   break;
               case "kill":
                   ActionDTO actionKill = new KillActionDTO((KillAction) action);
                   actionDTOList.add(actionKill);
                   break;
               case "replace":
                   ActionDTO actionReplace = new ReplaceActionDTO((ReplaceAction) action);
                   actionDTOList.add(actionReplace);
                   break;
               case "proximity":
                   ActionDTO actionProximity = new ProximityActionDTO((ProximityAction) action);
                   actionDTOList.add(actionProximity);
                   break;
               case "condition":
                   if(action.getClass() == multipleCondition.class) {
                       ActionDTO multipleConditionAction = new MultipleConditionActionDTO((multipleCondition) action);
                       actionDTOList.add(multipleConditionAction);
                   } else if(action.getClass() == singleCondition.class) {
                       ActionDTO singleConditionAction = new SingleConditionDTO((singleCondition) action);
                       actionDTOList.add(singleConditionAction);
                       break;
                   }
           }
       }
        RuleDTO ruleDTO = new RuleDTO(name, activationDTO, actionDTOList);
        return ruleDTO;
    }

    public void invokeRule(EntityInstanceManagerImpl instanceManager, EnvVariableManagerImpl envVariableManager, EntityInstanceImpl currEntity,
                           World world, long currTick, GridInstances gridInstances) throws Exception {

            // go over the action of the activeRules and check if the primary entity is like the curr instance
            for (Action currAction : actionsToPerform) {
                String entityNameInAction = currAction.getPrimaryEntityNameOnAction(currAction);

                if (currEntity.getName().equals(entityNameInAction)) {

                    // check if action has secondary and create array of secondary
                    if (currAction.isExistSecondary()) {
                        List<EntityInstanceImpl> secondaryInstancesList = currAction.createSecondaryList(instanceManager, envVariableManager, currTick, world, gridInstances);

                        // invoke for every pair (curr entity, secondary entity)
                        if(secondaryInstancesList != null) {
                            for (EntityInstanceImpl currSecondary : secondaryInstancesList) {
                                Context context = new Context(currEntity, currSecondary, envVariableManager, instanceManager, currTick, world, gridInstances);
                                currAction.doAction(context);
                            }
                        }
                        //else exist secondary condition but list in empty
                        /*else {
                            Context context = new Context(currEntity, envVariableManager, instanceManager, currTick, world, gridInstances);
                            currAction.doAction(context);
                        }*/
                    } // else no exist secondary - invoke rule only on curr instance
                    else {
                        Context context = new Context(currEntity, envVariableManager, instanceManager, currTick, world, gridInstances);
                        currAction.doAction(context);
                    }
                } //else the action not acting on this entity so move to next instance.
            }
        }

}
