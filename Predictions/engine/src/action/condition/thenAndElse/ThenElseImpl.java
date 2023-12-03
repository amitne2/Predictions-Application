package action.condition.thenAndElse;

import action.Action;

import java.util.ArrayList;
import java.util.List;

public class ThenElseImpl implements DoAfterCondition{

    private String whichOne;
    private List<Action> actionsList;

    public ThenElseImpl(String name){
        whichOne = name;
        actionsList = new ArrayList<>();
    }
    public void addAction(Action action){
        actionsList.add(action);
    }
    public List<Action> getActionsList(){
        return actionsList;
    }
}
