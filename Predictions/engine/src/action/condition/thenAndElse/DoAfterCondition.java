package action.condition.thenAndElse;

import action.Action;

import java.util.List;

public interface DoAfterCondition {

    public void addAction(Action action);
    public List<Action> getActionsList();
}
